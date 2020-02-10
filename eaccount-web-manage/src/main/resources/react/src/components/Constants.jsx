/** 常量配置 */
const constants = {
    NET_ERROR: '网络异常',
    OVERRIDE: '请重写该页面',
    DELETE_TEXT: '你确认要删除该数据吗？',
    HTTP: {
        type: {
            json: 'application/json'
        }
    },
    // form表单参数
    FORM: {
        item: {
            layout: {
                labelCol: {
                    xs: { span: 24 },
                    sm: { span: 6 }
                },
                wrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 14 }
                }
            },
            tailLayout: {
                wrapperCol: {
                    xs: {
                        span: 24,
                        offset: 0
                    },
                    sm: {
                        span: 14,
                        offset: 6
                    }
                }
            }
        }
    }
};

// 禁止为对象修改已存在的属性和方法。所有字段均只读。被冻结的对象也是不可扩展和密封的。
Object.freeze(constants);

module.exports = constants;