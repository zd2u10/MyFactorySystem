/**
 * 
 */

function addRow() {
    const tbody = document.getElementById('recipeTableBody');
    const newRow = tbody.rows[0].cloneNode(true);
    
    // 入力値をリセット
    newRow.querySelectorAll('input, select').forEach(el => {
        if (el.tagName === 'SELECT') el.value = "";
        else if (el.type !== 'hidden') el.value = "";
    });
    
    tbody.appendChild(newRow);
    updateIndices();
}

function removeRow(btn) {
    const rows = document.querySelectorAll('.recipe-row');
    if (rows.length > 1) {
        btn.closest('tr').remove();
        updateIndices();
    } else {
        alert("最低1行は必要です。");
    }
}

function updateIndices() {
    const rows = document.querySelectorAll('.recipe-row');
    rows.forEach((row, index) => {
        row.querySelectorAll('select, input').forEach(el => {
            const name = el.name.replace(/\[\d+\]/, `[${index}]`);
            el.name = name;
        });
    });
}

function submitForm() {
    // 必要ならここでバリデーションチェック
    document.getElementById('recipeForm').submit();
}