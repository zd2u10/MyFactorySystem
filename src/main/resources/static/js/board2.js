/**
 * 製造予定スケジューラー用スクリプト
 */

// 1. ページ読み込み時に、各カードに元のテキストを保存
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.order-card').forEach(card => {
        // 元のテキストの保存 
        card.dataset.originalText = card.querySelector('span:first-child').innerText;
    });
    updateBatchCounts();
});

// 2. 全てのグループ化を解除して、元に戻す関数
function resetGrouping() {
    document.querySelectorAll('.order-card').forEach(card => {
        // 全てのカードの状態をクリーンにする（PLANNING/DRAFT問わず）
        card.style.display = 'flex'; // 表示に戻す
        card.classList.remove('grouped', 'hidden-card', 'highlight-flash');
        card.onclick = null; // クリックイベント解除

        // HTMLに持たせた商品名を優先して表示(なければID
        const displayName = card.dataset.productName || card.dataset.originalText;
		if(displayName){
			card.querySelector('span:first-child').innerText = displayName;
		}
    });
}

// 3. グループ化と並び替えを同時に行う関数
function applyGrouping() {
    // まずリセット（クラスやスタイルの初期化）
    resetGrouping();

    const areas = document.querySelectorAll('.day-column, .pool-area');
    
    areas.forEach(column => {
        const groups = {}; 
        const cards = Array.from(column.querySelectorAll('.order-card'));
        
        // (1). グループ情報の収集
        cards.forEach(card => {
            const text = card.dataset.originalText;
			if(!text) return;
			
            const isPlanning = card.querySelector('.badge.planning') !== null;
            const statusKey = isPlanning ? 'plan' : 'draft';
            const groupKey = `${statusKey}_${text}`;
            
            if (!groups[groupKey]) groups[groupKey] = [];
            groups[groupKey].push(card);
        });

        // (2). グループごとにDOMを物理的に並び替えてから処理
        Object.keys(groups).forEach(key => {
            const list = groups[key];
            if (list.length > 1) {
                const parent = list[0];
                
                // 【ここが重要】同じグループのカードを親の直下に物理移動させる
                // リストの2番目から順に、親カードのすぐ後ろに差し込む
                for (let i = 1; i < list.length; i++) {
                    column.insertBefore(list[i], parent.nextSibling);
                }

                // グループ化のスタイル適用
                parent.classList.add('grouped');
				
				// 親の表示を商品名に変更
				const displayName = parent.dataset.productName || parent.dataset.originalText;
                parent.querySelector('span:first-child').innerText = `${displayName} (x${list.length})`;

                // 2枚目以降を隠す
                list.slice(1).forEach(card => {
                    card.classList.add('hidden-card');
                    card.style.display = 'none'; 
                });

                // クリックでトグル
                parent.onclick = (e) => {
                    e.stopPropagation();
                    const isHidden = list[1].style.display === 'none';
                    list.slice(1).forEach(card => {
                        if (isHidden) {
                            card.classList.remove('hidden-card');
                            card.style.display = 'flex'; // 表示に戻す
                        } else {
                            card.classList.add('hidden-card');
                            card.style.display = 'none'; // 隠す
                        }
                    });
                };
            }
        });
    });
}

// 4. カウント更新とグループ化の再計算
function updateBatchCounts() {
    document.querySelectorAll('.day-column').forEach(column => {
        const count = column.querySelectorAll('.order-card').length;
        const countDisplay = column.querySelector('.count-value');
        if (countDisplay) countDisplay.innerText = count;
    });
    // 集計後にグループ化を適用
    applyGrouping();
}

// 5. ドロップ時の処理
function drop(ev) {
    ev.preventDefault();
    const orderId = ev.dataTransfer.getData("orderId");
    const dropZone = ev.currentTarget;
    const card = document.querySelector(`[data-order-id='${orderId}']`);
    
    // カードを移動
    dropZone.appendChild(card);
    
    // 再計算（ここでapplyGroupingも走る）
    updateBatchCounts();

    // API連携
    const scheduledDate = dropZone.getAttribute("data-date");
    fetch('/api/production/move', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ orderId: orderId, scheduledDate: scheduledDate || null })
    });
}

// 6. 「確定して発番」ボタン処理
async function confirmDay(dateStr) {
    const dayColumn = document.querySelector(`.day-column[data-date='${dateStr}']`);
    const draftCards = dayColumn.querySelectorAll('.order-card:has(.badge.draft)');

    if (draftCards.length === 0) {
        alert("確定できるDRAFTブロックがありません。");
        return;
    }

    if (!confirm(`${dateStr} の予定を確定し、ロット番号を発行しますか？`)) return;

    for (let card of draftCards) {
        const orderId = card.getAttribute("data-order-id");
        try {
            const res = await fetch('/api/production/confirm', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ orderId: orderId })
            });
            const data = await res.json();

            if (data.success) {
                card.draggable = false;
                const badge = card.querySelector('.badge');
                if (badge) {
                    badge.className = 'badge planning';
                    badge.innerText = data.lotNumber;
                }
            }
        } catch (e) {
            console.error("確定処理エラー:", e);
        }
    }
    // 確定後に再計算
    updateBatchCounts();
}

// ドラッグ開始
function drag(ev) {
    ev.dataTransfer.setData("orderId", ev.currentTarget.getAttribute("data-order-id"));
}
// ドロップ許可
function allowDrop(ev) {
    ev.preventDefault();
}