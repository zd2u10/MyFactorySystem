/**
 * 入荷登録画面用の自動計算スクリプト
 * 1パッケージの容量(netWeight)と袋数(packageCount)から、総重量(quantity)を自動計算する
 */

// ページ内のHTML（DOM）が完全に読み込まれてから処理を開始する
document.addEventListener('DOMContentLoaded', function() {
    
    // 計算に必要なHTML要素（入力欄や表示領域）を取得
    const netWeightInput = document.getElementById('netWeight');       // 1パッケージあたりの内容量(g/ml)
    const packageCountInput = document.getElementById('packageCount'); // 入荷パッケージ数（袋/ケース）
    const quantityDisplay = document.getElementById('quantityDisplay');// ユーザーに見せるための総重量表示エリア（<span>や<div>など）
    const quantityHidden = document.getElementById('quantity');        // 実際にサーバー(Java)へ送信される隠しフィールド(<input type="hidden">)

    // 総重量を計算し、画面と隠しフィールドに反映する関数
    function calculateTotalWeight() {
        // 入力値を取得。未入力や文字が入力された場合は NaN になるため、その時は 0 として扱う (|| 0 の部分)
        const netWeight = parseFloat(netWeightInput.value) || 0;
        const count = parseFloat(packageCountInput.value) || 0;
        
        // 内容量 × 袋数 で総重量を算出
        const total = netWeight * count;
        
        // --- 1. 画面表示の更新 (ユーザー向け) ---
        // 合計が0より大きい場合のみ、「総重量: 〇〇 g/ml」とテキストを表示する
        if (total > 0) {
            quantityDisplay.innerText = `総重量: ${total} g/ml`;
        } else {
            // 未入力または0の場合はテキストをクリアする
            quantityDisplay.innerText = '';
        }

        // --- 2. 送信データの更新 (システム向け) ---
        // バックエンド（JavaのArrivalForm）の quantity フィールドに値を送るため、hidden要素に値をセット
        if (quantityHidden) {
            quantityHidden.value = total;
        }
    }

    // ユーザーが入力欄の数値を変更した瞬間（'input'イベント）に、上記の計算関数を自動実行させる設定
    if (netWeightInput && packageCountInput) {
        netWeightInput.addEventListener('input', calculateTotalWeight);
        packageCountInput.addEventListener('input', calculateTotalWeight);
    }
});