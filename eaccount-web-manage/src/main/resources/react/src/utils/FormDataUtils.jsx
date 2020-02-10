export function getFormData(obj) {
    const form_data = new FormData();
    Object.keys(obj).forEach(key => {
        form_data.append(key, obj[key]);
    });
    return form_data;
}