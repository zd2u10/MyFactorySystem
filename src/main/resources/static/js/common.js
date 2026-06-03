/**
 * 削除確認モーダルを開く関数
 * @param {string} formId - 送信するフォームのID
 * @param {string} modalId - モーダルのID
 * @param {string} contentId - 内容を表示する要素のID
 * @param {string[]} nameIds - フォーム内の名前入力欄のID配列（表示用）
 * @param {string[]} labels - 確認画面に表示する項目のラベル
 * @param {string} actionName - アクション名（'登録', '削除', '更新'など）
 */
function openConfirmModal(formId, modalId, contentId, nameIds, labels) {
    const modal = document.getElementById(modalId);
    const content = document.getElementById(contentId);
    
    // 内容をクリア
    content.innerHTML = "";
    
    // 入力内容を表示
    let displayInfo = "";
    for (let i = 0; i < nameIds.length; i++) {
        const inputElement = document.getElementById(nameIds[i]);
		// 値を取得(inputタグの場合は.value、そうでない場合は .innerText などを考慮)
		const val = inputElement ? inputElement.value : "(値なし)";
        displayInfo += `<p><strong>${labels[i]}:</strong> ${val}</p>`;
    }
    
	// 動的なメッセージを表示
    content.innerHTML = displayInfo + "<p>本当に<strong>${actionName}</strong>しますか？</p>";
    
    // 確定ボタンのイベント設定
    const confirmBtn = document.getElementById('confirmBtn');
    if (confirmBtn) {
		// ボタンのテキストも動的に変える
		confirmBtn.innerText = actionName + "する";
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