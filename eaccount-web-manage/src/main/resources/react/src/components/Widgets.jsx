/** 控件 */
const widgets = {
    BaseSearchForm: require("./widget/BaseSearchForm.jsx"),
    CrudForm: require("./widget/CrudForm.jsx"),
    Password: require("./widget/Password.jsx"),
    Plink: require("./widget/Plink.jsx"),
    RemoteSelect: require("./widget/RemoteSelect.jsx"),
    RemoteTreeSelect: require("./widget/RemoteTreeSelect.jsx"),
    SelectTreeTable: require("./widget/SelectTreeTable.jsx"),
    Captcha: require("./widget/Captcha.jsx")
};

// 禁止为对象修改已存在的属性和方法。所有字段均只读。被冻结的对象也是不可扩展和密封的。
Object.freeze(widgets);

module.exports = widgets;