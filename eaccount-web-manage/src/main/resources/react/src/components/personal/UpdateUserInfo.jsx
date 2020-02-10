const { connect } = dva;
const { Form, Form: { Item }, Input, Button, Spin, Radio: { Group } } = antd;
const { FORM: { item: { layout, tailLayout } } } = require("../Constants.jsx");
import { namespace } from '../../models/Personal.jsx'

function handleSubmit(validateFields, dispatch, e) {
    e.preventDefault();
    validateFields((err, values) => {
        if (err) {
            return;
        }
        dispatch(({ type: `${namespace}/updateUserInfo`, payload: { values } }));
    });
}

class UpdateUserInfo extends React.Component {
    render() {
        const { form: { getFieldDecorator, validateFields }, loading, loadingInfo, dispatch,
            userInfo: { name, account, telephone, gender, mobilephone, email } } = this.props;
        return (
            <div style={{padding: '40px 20px'}}>
                <Spin spinning={!!loadingInfo} delay={500}>
                    <Form onSubmit={handleSubmit.bind(this, validateFields, dispatch)}>
                        <Item
                            {...layout}
                            label={<span className="form-title">个人信息</span>}
                            colon={false}
                        >
                        </Item>
                        <Item {...layout} label="用户名">
                            <Input disabled value={account ? account.account : ''} />
                        </Item>
                        <Item {...layout} label="姓名" hasFeedback>
                            {getFieldDecorator('name', {
                                initialValue: name,
                                rules: [{
                                    required: true, message: '请输入姓名!'
                                }, {
                                    pattern: /^[\u4e00-\u9fa5]{2,4}$/, message: '姓名只允许中文，且长度为2-4!'
                                }]
                            })(<Input />)}
                        </Item>
                        <Item {...layout} label="手机" hasFeedback>
                            {getFieldDecorator('mobilephone', {
                                initialValue: mobilephone,
                                rules: [
                                    {pattern: /^1[3|4|5|7|8|9]\d{9}$/, message: '手机格式不正确'}
                                ]
                            })(<Input />)}
                        </Item>
                        <Item {...layout} label="邮箱" hasFeedback>
                            {getFieldDecorator('email', {
                                initialValue: email,
                                rules: [
                                    {max: 32, message: '邮箱长度最大为32'},
                                    {pattern: /^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$/, message: '邮箱格式不正确'}
                                ]
                            })(<Input />)}
                        </Item>
                        <Item {...layout} label="固定电话" hasFeedback>
                            {getFieldDecorator('telephone', {
                                initialValue: telephone,
                                rules: [
                                    {pattern: /^(0\d{2,3}[-])?\d{7,8}/, message: '请输入正确的固定电话'}
                                ]
                            })(<Input />)}
                        </Item>
                        <Item {...layout} label="性别">
                            {getFieldDecorator('gender', {
                                initialValue: gender,
                                rules: [{
                                    required: true, message: '请选择性别!'
                                }]
                            })(
                                <Group options={[ { label: '男', value: '1' }, { label: '女', value: '0' } ]} />
                            )}
                        </Item>
                        <Item {...tailLayout}>
                            <Button type="primary" htmlType="submit" disabled={loading} size="large">提交</Button>
                        </Item>
                    </Form>
                </Spin>
            </div>
        );
    }
}

module.exports = connect(state => {
    const effects = state.loading.effects;
    return {
        ...state[namespace],
        loading: effects[`${namespace}/updateUserInfo`],
        loadingInfo: effects[`${namespace}/getUserInfo`]
    }
})(Form.create()(UpdateUserInfo));