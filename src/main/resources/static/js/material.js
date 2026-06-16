/**
 * js/material.js
 * 原料・添加物の表示切り替えや、共通操作を管理
 */

// テーブルの表示切り替え（タイプ指定）
function filterMaterials(type) {
	
    const rows = document.querySelectorAll('.content-table tr');
   
	 rows.forEach(row => {
		const rowType = row. getAttribute('data-type')
		
		// data-typeがない行（もしあれば）はそのまま表示、
		// ある場合はtypeが一致するかALLなら表示
		
        if (!rowType || type === 'ALL' || rowType === type) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

// 削除確認モーダルの呼び出し用（既存のcommon.jsのopenConfirmModalを利用）
// 必要であればここにmaterial固有のJS処理を追加していきます