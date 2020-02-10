const { connect } = dva;
const { Form, Form: { Item }, Input, Button, Radio: { Group } } = antd;
const { CrudForm, Password } = require('../../Widgets.jsx');
const Constants = require("../../Constants.jsx");
import { namespace } from '../../../models/ec/User.jsx'

class ecUserForm extends CrudForm {
    static defaultProps = {
        namespace
    };
    render() {
        const { form: { getFieldDecorator }, edit: { record }, loading, dispatch } = this.props, { item: { layout, tailLayout }} = Constants.FORM;
        return (
            <div style={{padding: '40px 20px'}}>
                <Form onSubmit={this.handleSubmit.bind(this)}>
                    <Item {...layout} colon={false}
                        label={<span className="form-title">数据{record ? '修改' : '录入'}</span>} />
                    <Item className="none">
                        {getFieldDecorator('key')(<Input />)}
                    </Item>
                    <Item {...layout} label='登录名' hasFeedback={!record}>
                        {getFieldDecorator('loginName', {
                            rules: [
                                { required: true, message: '请输入登录名!' }
                            ]
                        })(<Input disabled={record} />)}
                    </Item>
                    <Item {...layout} label='用户姓名' hasFeedback>
                        {getFieldDecorator('userName', {
                            rules: [
                                { required: true, message: '请输入用户姓名!' }
                            ]
                        })(<Input />)}
                    </Item>
                    {record ? <div></div> :
                        <Item {...layout} label='登录密码'>
                            {getFieldDecorator('password', {
                                rules: [
                                    { required: true, message: '请输入登录密码!' },
                                    {pattern: /^[a-zA-Z0-9`~!@#$%^&*()\-_=+{}\[\]:"|;'\\<>?,./]{6,32}$/, message: '密码长度为6-32的数字/字母/常用符号'}
                                ]
                            })(<Password />)}
                        </Item>}
                    {record ? <div></div> :
                        <Item {...layout} label='支付密码'>
                        {getFieldDecorator('payPwd', {
                            rules: [
                                { required: true, message: '请输入支付密码!' },
                                {pattern: /^[a-zA-Z0-9`~!@#$%^&*()\-_=+{}\[\]:"|;'\\<>?,./]{6,32}$/, message: '密码长度为6-32的数字/字母/常用符号'}
                            ]
                        })(<Password />)}
                        </Item>}
                    <Item {...layout} label="用户类型">
                        {getFieldDecorator('userType', {
                            initialValue: 'user',
                            rules: [{
                                required: true, message: '请选择用户类型!'
                            }]
                        })(
                            <Group disabled={record ? "true": "false"} options={[{ label: '商户', value: 'merchant' }, { label: '资方', value: 'employer' } ]} />
                        )}
                    </Item>
                    <Item {...tailLayout}>
                        <Button type="primary" htmlType="submit" disabled={loading} size="large">提交</Button>
                        <Button size="large" style={{ marginLeft: 8 }} onClick={ () => dispatch({ type: `${namespace}/closeFormPage`})}>返回</Button>
                    </Item>
                </Form>
            </div>
        );
    }
}

module.exports = connect(state => {
return { ...state[namespace], loading: state.loading.effects[`${namespace}/save`] }
})(Form.create()(ecUserForm));