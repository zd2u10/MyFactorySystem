/**
 * 共通のモーダル表示処理
 * @param {string} formId - 送信フォームのID
 * @param {string} modalId - モーダルのID
 * @param {string} contentId - メッセージ表示先のID
 * @param {Array} fieldIds - 入力値を取得するIDの配列
 * @param {Array} labels - 表示する項目名の配列
 */
function openConfirmModal(formId, modalId, contentId, fieldIds, labels) {
    const form = document.getElementById(formId);
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }
    
    // 入力値の取得
    const values = fieldIds.map(id => document.getElementById(id).value);
    
    // ラベルと値を組み合わせてメッセージを作成
    let message = "";
    for (let i = 0; i < fieldIds.length; i++) {
        // 各項目を改行やスペースで区切ると見やすくなります
        message += `${labels[i]} : ${values[i]}　`;
    }
    
    document.getElementById(contentId).textContent = message;
    document.getElementById(modalId).showModal();
}

function closeModal(modalId) {
    document.getElementById(modalId).close(); 
}
 
function submitForm(formId) {
    document.getElementById(formId).submit(); 
}