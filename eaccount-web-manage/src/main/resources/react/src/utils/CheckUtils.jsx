/**
 * Returns `true` if the passed value is a number. Returns `false` for non-finite numbers.
 * @param {Object} value The value to test.
 * @return {Boolean}
 */
export function isNumber(value) {
    return typeof value === 'number' && isFinite(value);
}
/**
 * Validates that a value is numeric.
 * @param {Object} value Examples: 1, '1', '2.34'
 * @return {Boolean} True if numeric, false otherwise
 */
export function isNumeric(value) {
    return !isNaN(parseFloat(value)) && isFinite(value);
}
/**
 * Returns `true `if the passed value is a string.
 * @param {Object} value The value to test.
 * @return {Boolean}
 */
export function isString(value) {
    return typeof value === 'string';
}
/**
 * Returns `true` if the passed value is a boolean.
 *
 * @param {Object} value The value to test.
 * @return {Boolean}
 */
export function isBoolean(value) {
    return typeof value === 'boolean';
}
/**
 * Returns `true` if the passed value is a JavaScript Array, `false` otherwise.
 *
 * @param {Object} value The target to test.
 * @return {Boolean}
 * @method
 */
export function isArray(value) {
    return ('isArray' in Array) ? Array.isArray(value) : (toString.call(value) === '[object Array]');
}
/**
 * Returns `true` if the passed value is a JavaScript Date object, `false` otherwise.
 * @param {Object} value The object to test.
 * @return {Boolean}
 */
export function isDate(value) {
    return toString.call(value) === '[object Date]';
}
/**
 * Returns true if the passed value is empty, false otherwise. The value is deemed to be empty if it is either:
 *
 * - `null`
 * - `undefined`
 * - a zero-length array
 * - a zero-length string (Unless the `allowEmptyString` parameter is set to `true`)
 *
 * @param {Object} value The value to test.
 * @param {Boolean} [allowEmptyString=false] `true` to allow empty strings.
 * @return {Boolean}
 */
export function isEmpty(value, allowEmptyString) {
    return (value === null) || (value === undefined) || (!allowEmptyString ? value === '' : false) || (isArray(value) && value.length === 0);
}
export function arrayHasKey(array, key) {
    if (array === null || array === undefined) {
        return false;
    }
    for (let i = 0; i < array.length; i++) {
        if (array[i].key === key) {
            return true;
        }
    }
    return false;
}