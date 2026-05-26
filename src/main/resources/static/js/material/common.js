/**
 * 
 */

// 共通のモーダル表示処理
function openConfirmModal(formId, modalId, contentId, fieldIds) {
    const form = document.getElementById(formId);
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }
    // 画面固有の入力値取得
    const values = fieldIds.map(id => document.getElementById(id).value);
    document.getElementById(contentId).textContent = `原料名：${values[0]} / 正味量：${values[1]}`;
    document.getElementById(modalId).showModal();
}

function closeModal(modalId) {
	 document.getElementById(modalId).close(); 
}
 
function submitForm(formId) {
	 document.getElementById(formId).submit(); 
}