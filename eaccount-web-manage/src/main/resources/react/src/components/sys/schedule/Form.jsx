const { connect } = dva;
const { Form, Input, Button } = antd;
const { CrudForm } = require('../../Widgets.jsx');
const Constants = require("../../Constants.jsx");
import { namespace } from '../../../models/sys/Schedule.jsx';

const FormItem = Form.Item;
const { TextArea } = Input;

class SysPermissionForm extends CrudForm {
    static defaultProps = {
        namespace
    };
    render() {
        const { form: { getFieldDecorator }, edit: { record, title }, loading, dispatch } = this.props, { item: { layout, tailLayout }} = Constants.FORM;
        return (
            <div style={{padding: '40px 20px'}}>
                <Form onSubmit={this.handleSubmit.bind(this)}>
                    <FormItem
                        {...layout}
                        label={<span className="form-title">{title ? title : ('定时任务' + (record ? '修改' : '录入'))}</span>}
                        colon={false}
                    >
                    </FormItem>
                    <FormItem className="none">
                        {getFieldDecorator('key')(<Input />)}
                    </FormItem>
                    <FormItem {...layout} label="任务码" hasFeedback>
                        {getFieldDecorator('taskCode', {
                            rules: [
                                { required: true, whitespace: true, message: '请输入任务码' },
                                { max: 100, message: '任务码长度不能超过100' },
                                { pattern: /^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$/, message: '任务码为字母、数字、下划线组合，且不能以下划线开头或结尾'}
                            ]
                        })(
                            <Input />
                        )}
                    </FormItem>
                    <FormItem {...layout} label="任务名称" hasFeedback>
                        {getFieldDecorator('taskName', {
                            rules: [
                                { required: true, whitespace: true, message: '请输入任务名称' },
                                { max: 100, message: '任务名称长度不能超过100' }
                            ]
                        })(
                            <Input />
                        )}
                    </FormItem>
                    <FormItem {...layout} label="定时配置" hasFeedback>
                        {getFieldDecorator('taskConf', {
                            rules: [
                                { required: true, whitespace: true, message: '请输入定时配置' },
                                { max: 100, message: '定时配置长度不能超过100' }
                            ]
                        })(
                            <Input />
                        )}
                    </FormItem>
                    <FormItem {...layout} label="执行地址" hasFeedback>
                        {getFieldDecorator('taskClass', {
                            rules: [
                                { required: true, whitespace: true, message: '请输入执行地址'},
                                { max: 100, message: '执行地址长度不能超过100' },
                                { pattern: /^[a-zA-Z0-9.]+$/, message: '执行地址不能为特殊字符或汉字'}
                            ]
                        })(<Input />)}
                    </FormItem>
                    <FormItem {...layout} label="指定IP" hasFeedback>
                        {getFieldDecorator('taskServerIp', {
                            rules: [
                                { required: true, message: '请输入指定IP'},
                                { max: 100, message: '指定IP长度不能超过100' },
                                { pattern: /^[0-9\.]+$/, message: 'IP格式错误' }
                            ]
                        })(<Input />)}
                    </FormItem>
                    <FormItem {...layout} label="备注" hasFeedback>
                        {getFieldDecorator('remark', {
                            rules: [
                                { required: true, message: '请输入备注'},
                                { max: 250, message: '备注长度不能超过250' }
                            ]
                        })(<TextArea autosize={{ minRows: 4, maxRows: 6 }}/>)}
                    </FormItem>
                    <FormItem {...tailLayout}>
                        <Button type="primary" htmlType="submit" loading={loading} size="large">提交</Button>
                        <Button size="large" style={{ marginLeft: 8 }} onClick={ () => dispatch({ type: `${namespace}/closeFormPage`})}>返回</Button>
                    </FormItem>
                </Form>
            </div>
        );
    }
}

module.exports = connect(state => {
    return { ...state[namespace], loading: state.loading.effects[`${namespace}/save`] }
})(Form.create()(SysPermissionForm));