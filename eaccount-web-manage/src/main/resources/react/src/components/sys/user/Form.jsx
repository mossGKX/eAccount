const { connect } = dva;
const { Form, Form: { Item }, Input, Button, Radio: { Group } } = antd;
const { CrudForm, RemoteSelect, RemoteTreeSelect } = require('../../Widgets.jsx');
const { FORM: { item: { layout, tailLayout } } } = require("../../Constants.jsx");
import { namespace } from '../../../models/sys/User.jsx'
import { baseUrl as roleUrl } from '../../../models/sys/Role.jsx'
import { baseUrl as orgUrl } from '../../../models/sys/Organization.jsx'

class SysUserForm extends CrudForm {
    static defaultProps = {
        namespace: namespace
    };
    render() {
        const { form: { getFieldDecorator }, edit: { record, userId }, loading, dispatch } = this.props;
        return (
            <div style={{padding: '40px 20px'}}>
                <Form onSubmit={this.handleSubmit.bind(this)}>
                    <Item {...layout} label={<span className="form-title">系统用户{userId ? '修改' : '录入'}</span>} colon={false} />
                    <Item {...layout} label="登录名" hasFeedback>
                        {getFieldDecorator('account.account', {
                            rules: [{
                                required: true, message: '请输入登录名'
                            }, {
                                min: 3, max: 32, message: '登录名长度为3-32'
                            }]
                        })(
                            <Input />
                        )}
                    </Item>
                    <Item {...layout} label="用户姓名" hasFeedback>
                        {getFieldDecorator('name', {
                            rules: [{
                                required: true, message: '请输入用户姓名'
                            }, {
                                pattern: /^[\u4e00-\u9fa5]{2,4}$/, message: '用户姓名只允许中文，且长度为2-4'
                            }]
                        })(
                            <Input />
                        )}
                    </Item>
                    <Item {...layout} label="固定电话" hasFeedback>
                        {getFieldDecorator('telephone', {
                            rules: [
                                //{required: true, message: '请输入固定电话'},
                                {pattern: /^(0\d{2,3}[-])?\d{7,8}/, message: '请输入正确的固定电话'}
                            ]
                        })(
                            <Input />
                        )}
                    </Item>
                    <Item {...layout} label="手机" hasFeedback>
                        {getFieldDecorator('mobilephone', {
                            rules: [
                                {pattern: /^1[3|4|5|7|8|9]\d{9}$/, message: '手机格式不正确'}
                            ]
                        })(
                            <Input />
                        )}
                    </Item>
                    <Item {...layout} label="邮箱" hasFeedback>
                        {getFieldDecorator('email', {
                            rules: [
                                {max: 32, message: '邮箱长度最大为32'},
                                {pattern: /^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$/, message: '邮箱格式不正确'}
                            ]
                        })(
                            <Input />
                        )}
                    </Item>
                    <Item {...layout} label="性别">
                        {getFieldDecorator('gender', {
                            initialValue: '1',
                            rules: [{
                                required: true, message: '请选择性别'
                            }]
                        })(
                            <Group options={[ { label: '男', value: '1' }, { label: '女', value: '0' } ]} />
                        )}
                    </Item>
                    <Item {...layout} label="角色">
                        {getFieldDecorator('account.role.key', {
                            rules: [{ required: true, message: '请选择角色!' }],
                        })(
                            <RemoteSelect url={`${roleUrl}/listAll`} />
                        )}
                    </Item>
                    {
                        record ?
                            <Item {...layout} label="账号状态" >
                                {getFieldDecorator('account.status', {
                                    rules: [{ required: true, message: '请选择账号状态!' }]
                                })(
                                    <Group options={[{ label: '正常', value: 0 }, { label: '禁用', value: 1 } ]} />
                                )}
                            </Item>
                            : ''
                    }
                    <Item {...layout} label="组织">
                        {getFieldDecorator('org.key', {
                            rules: [{ required: true, message: '请选择组织!' }],
                        })(
                            <RemoteTreeSelect url={`${orgUrl}/listAll`} />
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
})(Form.create()(SysUserForm));