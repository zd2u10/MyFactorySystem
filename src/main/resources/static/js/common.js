/**
 * 削除確認モーダルを開く関数
 * @param {string} formId - 送信するフォームのID
 * @param {string} modalId - モーダルのID
 * @param {string} contentId - 内容を表示する要素のID
 * @param {string[]} nameIds - フォーム内の名前入力欄のID配列（表示用）
 * @param {string[]} labels - 確認画面に表示する項目のラベル
 */
function openConfirmModal(formId, modalId, contentId, nameIds, labels) {
    const modal = document.getElementById(modalId);
    const content = document.getElementById(contentId);
    
    // 内容をクリア
    content.innerHTML = "";
    
    // 削除対象の名前を取得して表示
    let displayInfo = "";
    for (let i = 0; i < nameIds.length; i++) {
        const val = document.getElementById(nameIds[i]).value;
        displayInfo += `<p><strong>${labels[i]}:</strong> ${val}</p>`;
    }
    
    content.innerHTML = displayInfo + "<p>本当に削除しますか？</p>";
    
    // フォーム送信用のイベントを設定
    const confirmBtn = document.getElementById('confirmBtn');
    if (confirmBtn) {
        confirmBtn.onclick = function() {
            document.getElementById(formId).submit();
        };
    }
    
    modal.showModal();
}

/**
 * モーダルを閉じる関数
 */
function closeConfirmModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.close();
}