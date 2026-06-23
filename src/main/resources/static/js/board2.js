/**
 * 製造予定スケジューラー用スクリプト
 */

// 1. ページ読み込み時に、各カードに元のテキストを保存
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.order-card').forEach(card => {
        // 元のテキストの保存 (スペルミス修正済み)
        card.dataset.originalText = card.querySelector('span:first-child').innerText;
    });
    updateBatchCounts();
});

// 2. 全てのグループ化を解除して、元に戻す関数
function resetGrouping() {
    document.querySelectorAll('.order-card').forEach(card => {
        card.style.display = 'flex'; // 全て表示
        card.classList.remove('grouped'); // 強制解除
        // テキストを戻す
        if (card.dataset.originalText) {
            card.querySelector('span:first-child').innerText = card.dataset.originalText;
        }
        // クリックイベントを解除
        card.onclick = null;
    });
}

// 3. グループ化を再計算する関数
function applyGrouping() {
    // まず一度リセット
    resetGrouping();

    // 以下のカラムとプールエリアの両方で実行
    const areas = document.querySelectorAll('.day-column, .pool-area');
    
    areas.forEach(column => {
        const groups = {}; 
        // 表示されているカードだけ取得
        const cards = Array.from(column.querySelectorAll('.order-card'));
        
        cards.forEach(card => {
            const text = card.dataset.originalText; 
            if (!groups[text]) groups[text] = [];
            groups[text].push(card);
        });

        Object.keys(groups).forEach(key => {
            const list = groups[key];
            if (list.length > 1) {
                // 集約処理
                list[0].querySelector('span:first-child').innerText = `${key} (x${list.length})`;
                list[0].classList.add('grouped');
                
                // 2枚目以降を隠す
                for (let i = 1; i < list.length; i++) {
                    list[i].style.display = 'none';
                }
                
                // クリックで展開（トグル）機能
                list[0].onclick = () => {
                    const isHidden = list[1].style.display === 'none';
                    list.slice(1).forEach(c => c.style.display = isHidden ? 'flex' : 'none');
                };
            }
        });
    });
}

// 4. カウント更新関数
function updateBatchCounts() {
    document.querySelectorAll('.day-column').forEach(column => {
        const count = column.querySelectorAll('.order-card').length;
        const countDisplay = column.querySelector('.count-value');
        if (countDisplay) countDisplay.innerText = count;
    });
    // 集計後にグループ化を適用
    applyGrouping();
}

// 5. ドラッグ＆ドロップ関連
function allowDrop(ev) { ev.preventDefault(); }
function drag(ev) { ev.dataTransfer.setData("orderId", ev.currentTarget.getAttribute("data-order-id")); }

function drop(ev) {
    ev.preventDefault();
    const orderId = ev.dataTransfer.getData("orderId");
    const dropZone = ev.currentTarget;
    const scheduledDate = dropZone.getAttribute("data-date");

    const card = document.querySelector(`[data-order-id='${orderId}']`);
    dropZone.appendChild(card);
    
    updateBatchCounts(); // ここで再描画される

    fetch('/api/production/move', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            orderId: orderId,
            scheduledDate: scheduledDate ? scheduledDate : null 
        })
    }).then(response => {
        if (!response.ok) alert("移動の保存に失敗しました。画面を更新してください。");
    });
}

// 6. 「確定して発番」
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
                badge.className = 'badge planning';
                badge.textContent = data.lotNumber;
            }
        } catch (e) { console.error(e); }
    }
    alert("確定処理が完了しました！");
    updateBatchCounts(); // 確定後に再計算
}