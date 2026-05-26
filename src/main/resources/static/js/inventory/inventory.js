/**
 * 
 */
// 関数を定義するだけ
function toggleForm() {
    // 選択された操作区分を取得
    const type = document.getElementById('typeSelect').value;
    
    // ラベル要素と各入力欄の親要素を取得
    const dateLabel = document.getElementById('dateLabel');
    const inFields = document.getElementById('inFields');
    const productFields = document.getElementById('productFields');

    // 1. ラベルのテキストを動的に変更
    if (type === 'IN') {
        dateLabel.innerText = "入荷日:";
    } else if (type === 'PRODUCTION') {
        dateLabel.innerText = "製造使用日:";
    } else if (type === 'DISPOSAL') {
        dateLabel.innerText = "廃棄日:";
    }

    // 2. 入力欄の表示・非表示を切り替え
    inFields.style.display = (type === 'IN') ? 'block' : 'none';
    productFields.style.display = (type === 'PRODUCTION') ? 'block' : 'none';
}

// ページ読み込み時に実行
window.onload = toggleForm;