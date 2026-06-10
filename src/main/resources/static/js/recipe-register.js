/**
 * recipe-register.js
 * レシピ登録画面のインタラクション処理
 */

// ────────────────────────────────────────────
// 行の追加・削除・インデックス更新
// ────────────────────────────────────────────

function addRow() {
    const tbody = document.getElementById('recipeTableBody');
    const newRow = tbody.rows[0].cloneNode(true);

    newRow.querySelectorAll('input, select').forEach(el => {
        if (el.tagName === 'SELECT') {
            el.selectedIndex = 0;
        } else if (el.type !== 'hidden') {
            el.value = '';
        }
    });

    // 単位・産地エリアをリセット
    const unitDisplay = newRow.querySelector('.unit-display');
    if (unitDisplay) unitDisplay.textContent = '';

    const originsCell = newRow.querySelector('.origins-cell');
    if (originsCell) {
        originsCell.innerHTML = '<span class="origins-placeholder" style="color:#999;font-size:0.9em;">材料を選択してください</span>';
    }

    tbody.appendChild(newRow);
    updateIndices();
    calculateRealtimeHydration();
}

function removeRow(btn) {
    const rows = document.querySelectorAll('.recipe-row');
    if (rows.length > 1) {
        btn.closest('tr').remove();
        updateIndices();
        calculateRealtimeHydration();
    } else {
        alert('最低1行は必要です。');
    }
}

function updateIndices() {
    const rows = document.querySelectorAll('.recipe-row');
    rows.forEach((row, index) => {
        row.querySelectorAll('select, input').forEach(el => {
            if (el.name) {
                el.name = el.name.replace(/\[\d+\]/, `[${index}]`);
            }
        });
    });
}

// ────────────────────────────────────────────
// 材料選択時: 単位 + 産地チェックボックスを更新
// ────────────────────────────────────────────

function updateUnitAndOrigins(selectEl) {
    const option = selectEl.options[selectEl.selectedIndex];
    const row = selectEl.closest('tr');

    // 単位の更新
    const unitDisplay = row.querySelector('.unit-display');
    if (unitDisplay) unitDisplay.textContent = option.getAttribute('data-unit') || '';

    // 産地チェックボックスの更新
    const materialId = selectEl.value;
    const originsCell = row.querySelector('.origins-cell');
    if (!originsCell) return;

    if (!materialId) {
        originsCell.innerHTML = '<span class="origins-placeholder" style="color:#999;font-size:0.9em;">材料を選択してください</span>';
        calculateRealtimeHydration();
        return;
    }

    // 行インデックスを取得
    const rows = Array.from(document.querySelectorAll('.recipe-row'));
    const rowIndex = rows.indexOf(row);

    const origins = ORIGINS_MAP[materialId] || [];

    if (origins.length === 0) {
        // 産地未設定の在庫のみ or 在庫なし → 産地不問表示
        originsCell.innerHTML = '<span style="color:#999;font-size:0.9em;">産地不問</span>';
    } else if (origins.length === 1) {
        // 産地が1件のみ → チェックボックスを自動チェック済みで表示
        originsCell.innerHTML = buildOriginsCheckboxes(origins, rowIndex, [origins[0]]);
    } else {
        // 産地が複数 → チェックボックスを表示（将来の拡張対応）
        originsCell.innerHTML = buildOriginsCheckboxes(origins, rowIndex, []);
    }

    calculateRealtimeHydration();
}

/**
 * 産地チェックボックスのHTMLを生成
 * @param {string[]} origins - 産地リスト
 * @param {number} rowIndex  - 行インデックス（name属性に使用）
 * @param {string[]} checked - 最初からチェック済みにする産地リスト
 */
function buildOriginsCheckboxes(origins, rowIndex, checked) {
    return origins.map(origin => {
        const isChecked = checked.includes(origin) ? 'checked' : '';
        const id = `origin_${rowIndex}_${origin.replace(/\s/g, '_')}`;
        return `
            <label style="display:inline-flex;align-items:center;gap:4px;margin-right:8px;font-size:0.9em;cursor:pointer;">
                <input type="checkbox"
                       name="recipeList[${rowIndex}].allowedOrigins"
                       value="${origin}"
                       id="${id}"
                       ${isChecked} />
                ${origin}
            </label>`;
    }).join('');
}

// ────────────────────────────────────────────
// リアルタイム加水率計算
// ────────────────────────────────────────────

function getTotalPowderWeight() {
    let totalPowder = 0;
    const rows = document.querySelectorAll('#recipeTableBody tr.recipe-row');
    
    rows.forEach(row => {
        const select = row.querySelector('.material-select');
        const qtyInput = row.querySelector('input[type="number"][name*=".quantity"]');
        
        if (select && select.value && qtyInput && qtyInput.value) {
            const selectedOption = select.options[select.selectedIndex];
            const isPowder = selectedOption.getAttribute('data-is-powder') === 'true';
            
            if (isPowder) {
                totalPowder += parseFloat(qtyInput.value) || 0;
            }
        }
    });
    return totalPowder;
}

/**
 * 加水率(%) が手入力されたとき、加水量(ml) を自動計算する
 * @param {string} type 'min' または 'max'
 */
function onHydrationRateChange(type) {
    const rateInput = document.getElementById(type + 'HydrationInput');
    const waterInput = document.getElementById(type + 'WaterInput');
    const totalPowder = getTotalPowderWeight();

    const rate = parseFloat(rateInput.value);
    
    // 粉末重量がある ＆ 率が入力されている場合のみ計算
    if (totalPowder > 0 && !isNaN(rate)) {
        // 加水量 = 粉末重量 * (加水率 / 100)
        const water = totalPowder * (rate / 100);
        // 小数第1位で丸めてセット
        waterInput.value = water.toFixed(1);
    } else if (totalPowder === 0) {
        // 粉末が0の時は計算できないのでクリア（またはプレースホルダを出すなど）
        waterInput.value = '';
    }
}

/**
 * 加水量(ml) が手入力されたとき、加水率(%) を自動逆算する
 * @param {string} type 'min' または 'max'
 */
function onWaterAmountChange(type) {
    const rateInput = document.getElementById(type + 'HydrationInput');
    const waterInput = document.getElementById(type + 'WaterInput');
    const totalPowder = getTotalPowderWeight();

    const water = parseFloat(waterInput.value);

    // 粉末重量がある ＆ 量が入力されている場合のみ計算
    if (totalPowder > 0 && !isNaN(water)) {
        // 加水率 = (加水量 / 粉末重量) * 100
        const rate = (water / totalPowder) * 100;
        // 小数第1位で丸めてセット
        rateInput.value = rate.toFixed(1);
    } else if (totalPowder === 0) {
        rateInput.value = '';
    }
}

/**
 * 材料の選択や数量が変更されたときに全体を再計算する
 * （各行の quantity の oninput や select の onchange でこれを呼ぶ）
 */
function recalculateAllHydration() {
    const rateMin = document.getElementById('minHydrationInput').value;
    const rateMax = document.getElementById('maxHydrationInput').value;
    const waterMin = document.getElementById('minWaterInput').value;
    const waterMax = document.getElementById('maxWaterInput').value;
    
    const totalPowder = getTotalPowderWeight();
    
    // 画面の表示用（親切設計）
    const displayArea = document.getElementById('hydrationDisplay');
    if (displayArea) {
        if (totalPowder > 0) {
            displayArea.innerHTML = `現在の粉末原料合計: <strong>${totalPowder} g</strong>`;
        } else {
            displayArea.innerHTML = '粉末原料と加水量を入力すると、現在の加水率が計算されます。';
        }
    }

    // 優先度：通常は「率(%)」をベースに「量(ml)」を再計算する
    // ただし、どちらを基準にするかは業務の運用次第です。
    // 今回は「率」が入力されていればそれを正として再計算します。
    if (rateMin) {
        onHydrationRateChange('min');
    } else if (waterMin) {
        onWaterAmountChange('min');
    }
    
    if (rateMax) {
        onHydrationRateChange('max');
    } else if (waterMax) {
        onWaterAmountChange('max');
    }
}

function applyStyle(el, state) {
    const s = {
        ok:      { bg: '#d4edda', border: '#c3e6cb', color: '#155724' },
        warn:    { bg: '#f8d7da', border: '#f5c6cb', color: '#721c24' },
        waiting: { bg: '#fff3cd', border: '#ffeeba', color: '#856404' },
        default: { bg: '#f8f9fa', border: '#ddd',    color: '#333'    },
    }[state] || { bg: '#f8f9fa', border: '#ddd', color: '#333' };
    el.style.backgroundColor = s.bg;
    el.style.borderColor     = s.border;
    el.style.color           = s.color;
}

// ページロード時に加水率表示を初期化
document.addEventListener('DOMContentLoaded', calculateRealtimeHydration);
