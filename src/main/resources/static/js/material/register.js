/**
 * 原料登録画面 dialog用
 */

function openConfirmModal() {
    const name = document.getElementById('name').value;
    // 他の項目も同様に取得
    
    // 内容をセット
    const content = `原料名：${name}`;
    document.getElementById('confirmContent').textContent = content;
    
    // モーダルを表示
    document.getElementById('confirmModal').showModal();
	
	// キャンセルボタンが押された時の処理
	function cancelModal() {
	    const modal = document.getElementById('confirmModal');
	    modal.close(); //入力内容はそのまま残す
}