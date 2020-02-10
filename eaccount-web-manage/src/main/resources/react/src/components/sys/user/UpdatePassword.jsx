const { connect } = dva;
const { Form, Form: { Item }, Input, Button } = antd;
const { CrudForm, Password } = require('../../Widgets.jsx');
const { FORM: { item: { layout, tailLayout } } } = require("../../Constants.jsx");
import { rsaEncrypt } from '../../../utils/StringUtils.jsx'
import { namespace } from '../../../models/sys/User.jsx'

class SysUserForm extends CrudForm {
    static defaultProps = {
        namespace: namespace
    };
    dealSubmitData(values) {
        values = { ...values };
        delete(values.account);
        values.password = rsaEncrypt(values.password);
        return values;
    }
    render() {
        const { form: { getFieldDecorator, getFieldValue, validateFields }, loading } = this.props;
        return (
            <div style={{padding: '40px 20px'}}>
                <Form onSubmit={this.handleSubmitUrl.bind(this, 'updatePassword')}>
                    <Item
                        {...layout}
                        label={<span className="form-title">修改密码</span>}
                        colon={false}
                    >
                    </Item>
                    <Item className="none">
                        {getFieldDecorator('id')(
                            <Input />
                        )}
                    </Item>
                    <Item
                        {...layout}
                        label="登录名"
                    >
                        {getFieldDecorator('account.account')(
                            <Input disabled />
                        )}
                    </Item>
                    <Item
                        {...layout}
                        label="新密码"
                    >
                        {getFieldDecorator('password', {
                            rules: [ {
                                required: true, message: '请输入新密码'
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
                                required: true, message: '请输入确认密码'
                            }, {
                                validator: (rule, value, callback) => {
                                    value && value !== getFieldValue('password') ?
                                        callback('两次密码输入不正确') : callback()
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
    return { ...state[namespace], loading: state.loading.effects[`${namespace}/save`] }
})(Form.create()(SysUserForm));