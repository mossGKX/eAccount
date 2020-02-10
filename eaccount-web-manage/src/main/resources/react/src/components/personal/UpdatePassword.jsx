const { connect } = dva;
const { Form, Form: { Item }, Input, Button } = antd;
const { Password } = require('../Widgets.jsx');
const { FORM: { item: { layout, tailLayout } } } = require("../Constants.jsx");
import { rsaEncrypt } from '../../utils/StringUtils.jsx'
import { getFormData } from '../../utils/FormDataUtils.jsx'
import { namespace } from '../../models/Personal.jsx'

function handleSubmit(validateFields, dispatch, e) {
    e.preventDefault();
    validateFields((err, values) => {
        if (err) {
            return;
        }
        values.oldPassword = rsaEncrypt(values.oldPassword);
        values.password = rsaEncrypt(values.password);
        values.confirmPassword = rsaEncrypt(values.confirmPassword);
        dispatch(({ type: `${namespace}/updatePassword`, payload: { values: getFormData(values) } }));
    });
}

class UpdatePassword extends React.Component {
    render() {
        const { form: { getFieldDecorator, getFieldValue, validateFields }, loading, username, dispatch } = this.props;
        return (
            <div style={{padding: '40px 20px'}}>
                <Form onSubmit={handleSubmit.bind(this, validateFields, dispatch)}>
                    <Item
                        {...layout}
                        label={<span className="form-title">密码修改</span>}
                        colon={false}
                    >
                    </Item>
                    <Item
                        {...layout}
                        label="用户名"
                    >
                        <Input disabled value={username} />
                    </Item>
                    <Item
                        {...layout}
                        label="原密码"
                    >
                        {getFieldDecorator('oldPassword', {
                            rules: [ {
                                required: true, message: '请输入原密码!'
                            } ]
                        })(
                            <Password />
                        )}
                    </Item>
                    <Item
                        {...layout}
                        label="新密码"
                    >
                        {getFieldDecorator('password', {
                            rules: [ {
                                required: true, message: '请输入新密码!'
                            }, {
                                pattern: /^[a-zA-Z0-9`~!@#$%^&*()\-_=+{}\[\]:"|;'\\<>?,./]{6,32}$/, message: '密码长度为6-32的数字/字母/常用符号'
                            }, {
                                validator: (rule, value, callback) => {
                                    value && getFieldValue('confirmPassword') ? validateFields(['confirmPassword'], { force: true }) : '';
                                    callback();
                                }
                            } ]
                        })(
                            <Password />
                        )}
                    </Item>
                    <Item
                        {...layout}
                        label="确认密码"
                    >
                        {getFieldDecorator('confirmPassword', {
                            rules: [ {
                                required: true, message: '请输入确认密码!'
                            }, {
                                validator: (rule, value, callback) => {
                                    value && value !== getFieldValue('password') ? callback('两次密码输入不正确!') : callback()
                                }
                            } ]
                        })(
                            <Password />
                        )}
                    </Item>
                    <Item {...tailLayout}>
                        <Button type="primary" htmlType="submit" disabled={loading} size="large">提交</Button>
                    </Item>
                </Form>
            </div>
        );
    }
}

module.exports = connect(state => {
    return { ...state[namespace], loading: state.loading.effects[`${namespace}/updatePassword`], username: state.app.username  }
})(Form.create()(UpdatePassword));