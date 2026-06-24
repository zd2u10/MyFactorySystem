/**
 * 製造予定スケジューラー用スクリプト
 */

// 1. ページ読み込み時に、各カードに元のテキストを保存
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.order-card').forEach(card => {
        // 元のテキストの保存 
        card.dataset.originalText = card.querySelector('.item-name').innerText;
    });
    updateBatchCounts();
});

// 2. 全てのグループ化を解除して、元に戻す関数
function resetGrouping() {
    document.querySelectorAll('.order-card').forEach(card => {
        // 全てのカードの状態をクリーンにする（PLANNING/DRAFT問わず）
        card.style.display = 'flex'; // 表示に戻す
        card.classList.remove('grouped', 'collapsed', 'hidden-card', 'highlight-flash');
        card.onclick = null; // クリックイベント解除

        // HTMLに持たせた商品名を優先して表示(なければID
        const displayName = card.dataset.productName || card.dataset.originalText;
		if(displayName){
			card.querySelector('.item-name').innerText = displayName;
		}
    });
}

// 3. グループ化と並び替えを同時に行う関数
function applyGrouping() {
    // まずリセット（クラスやスタイルの初期化）
    resetGrouping();

    const areas = document.querySelectorAll('.day-column, .pool-area');
	
	// エリアごとにグループ化(キーの生成)    
    areas.forEach(column => {
        const groups = {}; 
		const cards = Array.from(column.querySelectorAll('.order-card'));
		        
        // (1). 通常エリアのグループ化
        cards.forEach(card => {
            const isDraft = card.querySelector('.badge.draft') !== null;
			const isPlanning = card.querySelector('.badge.planning') !== null;
			const isExpired = card.closest('.expired-draft-list') !== null;
			const isRescheduled = card.querySelector('.badge.rescheduled') !== null;
			
			const statusKey = isDraft ? 'DRAFT' : (isPlanning ? 'PLANNING' : 'OTHER');
			// 「再調整済」かどうかが違うカードは絶対に同じ束にしない
			const rescheduledKey = isRescheduled ? 'RESCHEDULED' : 'NORMAL';
			const productKey = card.getAttribute('data-original-text');
			
			let key;
			if(isExpired){
				// 期限切れ用：製品＋日付＋再調整履歴をキーにする
				const date = card.getAttribute('data-scheduled-date') || 'no-date';
				key = 'EXP_' + statusKey + "_" + rescheduledKey + "_" + productKey + "_" + date;
			} else {
				// 通常用：製品＋再調整履歴でまとめる
				key = statusKey + "_" + rescheduledKey + "_" + productKey;
			}
			
			if(!groups[key]){
				groups[key] = [];
			}
			groups[key].push(card);
		});

        // (3). グループごとにDOMを物理的に並び替えてから処理
        Object.keys(groups).forEach(key => {
            const list = groups[key];
            if (list.length > 1) {
                const mainCard = list[0];
				mainCard.classList.add('grouped');
                
				// 個数をテキストに追加
				const span = mainCard.querySelector('.item-name');
				if (span) {
				    span.textContent = span.textContent + ' (x' + list.length + ')';
				}	
				
				// 2枚目以降を隠す
				for (let i = 1; i < list.length; i++) {
				     list[i].classList.add('collapsed');
				}
				                				
                // クリックでトグル
                mainCard.onclick = function() {
                    let isExpanded = !list[1].classList.contains('collapsed');
					for (let i = 1; i < list.length; i++){
		 				if(isExpanded){
						   list[i].classList.add('collapsed');
					    } else {
                           list[i].classList.remove('collapsed');
                        }
                    }
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