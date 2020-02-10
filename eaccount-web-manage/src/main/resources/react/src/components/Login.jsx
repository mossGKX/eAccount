const { connect } = dva;
const { Layout, Form, Icon, Input, Button } = antd;
const { Content } = Layout;
const { Item } = Form;
const { Password, Captcha } = require("./Widgets.jsx");
import { namespace } from '../models/App.jsx'
import { getFormData } from '../utils/FormDataUtils.jsx'
import { rsaEncrypt } from '../utils/StringUtils.jsx'

function handleSubmit(dispatch, validateFields, e) {
    e.preventDefault();
    validateFields((err, values) => {
        if (err)
            return;
        values.password = rsaEncrypt(values.password);// 加密
        dispatch({type: `${namespace}/login`, payload: getFormData(values)});
    });
}

function Login (props) {
    const { loginCaptcha, form, form: { getFieldDecorator, validateFields }, loading, dispatch } = props;
    return (
        <Layout className="login">
            <Content>
                <div className="logo">
                    <img src="resources/image/logo.png" alt="" />
                </div>
                <div className="content">
                    <h3>登录</h3>
                    <Form onSubmit={!loading ? handleSubmit.bind(this, dispatch, validateFields) : undefined} className="login-form">
                        <Item>
                            {getFieldDecorator('username', {
                                rules: [{ required: true, message: '请输入你的用户名!' }],
                            })(
                                <Input size="large" prefix={<Icon type="user" style={{ fontSize: 18 }} />} placeholder="请输入用户名" />
                            )}
                        </Item>
                        <Item>
                            {getFieldDecorator('password', {
                                rules: [{ required: true, message: '请输入你的密码!' }],
                            })(
                                <Password
                                    prefix={<Icon type="lock" style={{fontSize: 18}} />}
                                    size="large" fontSize={18} placeholder="请输入密码" />
                            )}
                        </Item>
                        { loginCaptcha ? <Item>
                            {getFieldDecorator('captcha', {
                                rules: [{ required: true, message: '请输入图片验证码!' }],
                            })(
                                <Captcha
                                    form={form}
                                    namespace={namespace}
                                    img={{ style: { height: 41 } }}
                                    prefix={<Icon type="picture" style={{fontSize: 18}} />}
                                    size="large" fontSize={18} placeholder="请输入图片验证码" />
                            )}
                        </Item> : '' }
                        <Item>
                            <Button type="primary" htmlType="submit" className="login-form-button" loading={loading}>登录</Button>
                        </Item>
                    </Form>
                </div>
            </Content>
        </Layout>
    );
}

module.exports = connect(state => {
    return {
        ...state[namespace],
        loading: state.loading.effects[`${namespace}/login`]
    };
})(Form.create()(Login));