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

    // 入力値をリセット
    newRow.querySelectorAll('input, select').forEach(el => {
        if (el.tagName === 'SELECT') {
            el.selectedIndex = 0; // 修正: selectIndex → selectedIndex
        } else if (el.type !== 'hidden') {
            el.value = '';
        }
    });

    // 単位表示のクリア
    const unitDisplay = newRow.querySelector('.unit-display');
    if (unitDisplay) unitDisplay.textContent = '';

    // 産地セレクトボックスを「未設定」のみにリセット
    const originSelect = newRow.querySelector('.origin-select');
    if (originSelect) {
        originSelect.innerHTML = '<option value="">未設定</option>';
    }

    tbody.appendChild(newRow);
    updateIndices();
    calculateRealtimeHydration();
}

function removeRow(btn) {
    // バグ修正: '.recipe-row' に統一（HTMLのクラス名と一致させる）
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
    // バグ修正: '.recipe-row' に統一
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
// 材料選択時: 単位 + 産地を更新
// ────────────────────────────────────────────

/**
 * 材料セレクトボックス変更時に呼ばれる
 * - 単位ラベルを更新
 * - 産地セレクトボックスをDBデータで更新
 */
function updateUnitAndOrigin(selectElement) {
    const selectedOption = selectElement.options[selectElement.selectedIndex];

    // 単位の更新
    const unit = selectedOption.getAttribute('data-unit') || '';
    const unitDisplay = selectElement.closest('tr').querySelector('.unit-display');
    if (unitDisplay) unitDisplay.textContent = unit;

    // 産地の更新
    const materialId = selectElement.value;
    const originSelect = selectElement.closest('tr').querySelector('.origin-select');
    if (!originSelect) return;

    // まず「未設定」のみにリセット
    originSelect.innerHTML = '<option value="">未設定</option>';

    // ORIGIN_MAP に産地データがあれば追加
    if (materialId && ORIGIN_MAP[materialId] && ORIGIN_MAP[materialId].length > 0) {
        ORIGIN_MAP[materialId].forEach(origin => {
            const opt = document.createElement('option');
            opt.value = origin;
            opt.textContent = origin;
            originSelect.appendChild(opt);
        });
    }
    // 産地が1件だけなら自動選択する（任意）
    if (originSelect.options.length === 2) {
        originSelect.selectedIndex = 1;
    }

    calculateRealtimeHydration();
}

// ────────────────────────────────────────────
// リアルタイム加水率計算（目安範囲を統合表示）
// ────────────────────────────────────────────

function calculateRealtimeHydration() {
    let totalPowder = 0;

    const rows = document.querySelectorAll('#recipeTableBody .recipe-row');
    rows.forEach(row => {
        const select = row.querySelector('.material-select');
        const quantityInput = row.querySelector('input[name*=".quantity"]');

        if (select && select.selectedIndex > 0 && quantityInput && quantityInput.value) {
            const option = select.options[select.selectedIndex];
            const isPowder = option.getAttribute('data-is-powder') === 'true';
            const quantity = parseFloat(quantityInput.value) || 0;

            if (isPowder) {
                totalPowder += quantity;
            }
        }
    });

    const waterInput = document.getElementById('waterInput');
    const waterAmount = parseFloat(waterInput ? waterInput.value : 0) || 0;
    const displayDiv = document.getElementById('hydrationDisplay');
    if (!displayDiv) return;

    // 目安範囲の計算（Thymeleafから渡された加水率上下限を使用）
    const rangeHtml = (totalPowder > 0 && HYDRATION_MIN > 0 && HYDRATION_MAX > 0)
        ? buildRangeHtml(totalPowder)
        : '';

    if (totalPowder > 0 && waterAmount > 0) {
        // 現在の加水率 + 目安範囲を表示
        const hydrationRate = ((waterAmount / totalPowder) * 100).toFixed(1);
        const withinRange = isWithinRange(parseFloat(hydrationRate));

        displayDiv.innerHTML =
            `<div>【現在の加水率】 <strong>${hydrationRate}%</strong>` +
            ` （粉体合計: ${totalPowder}g ／ 加水量: ${waterAmount}ml）` +
            (withinRange
                ? ' <span style="color:#155724;">✔ 目安範囲内</span>'
                : ' <span style="color:#721c24;">⚠ 目安範囲外</span>') +
            `</div>${rangeHtml}`;

        applyStyle(displayDiv, withinRange ? 'ok' : 'warn');

    } else if (totalPowder > 0) {
        displayDiv.innerHTML =
            `<div>粉体合計: <strong>${totalPowder}g</strong> ／ 加水量を入力してください</div>` +
            rangeHtml;
        applyStyle(displayDiv, 'input-waiting');

    } else {
        displayDiv.innerHTML = '粉末原料と加水量を入力すると、現在の加水率が計算されます。';
        applyStyle(displayDiv, 'default');
    }
}

/**
 * 目安範囲の HTML を生成
 */
function buildRangeHtml(totalPowder) {
    const minWater = Math.round(totalPowder * HYDRATION_MIN / 100);
    const maxWater = Math.round(totalPowder * HYDRATION_MAX / 100);
    return `<div style="margin-top:6px; font-size:0.9em;">` +
        `【加水量の目安】 ${HYDRATION_MIN}%～${HYDRATION_MAX}%： ` +
        `<strong>${minWater}ml ～ ${maxWater}ml</strong></div>`;
}

/**
 * 加水率が目安範囲内かどうか
 */
function isWithinRange(rate) {
    if (!HYDRATION_MIN || !HYDRATION_MAX) return true; // 範囲未設定時は判定しない
    return rate >= HYDRATION_MIN && rate <= HYDRATION_MAX;
}

/**
 * hydrationDisplay のスタイルを状態に応じて切り替え
 */
function applyStyle(el, state) {
    const styles = {
        ok:            { bg: '#d4edda', border: '#c3e6cb', color: '#155724' },
        warn:          { bg: '#f8d7da', border: '#f5c6cb', color: '#721c24' },
        'input-waiting': { bg: '#fff3cd', border: '#ffeeba', color: '#856404' },
        default:       { bg: '#f8f9fa', border: '#ddd',    color: '#333'    },
    };
    const s = styles[state] || styles.default;
    el.style.backgroundColor = s.bg;
    el.style.borderColor = s.border;
    el.style.color = s.color;
}