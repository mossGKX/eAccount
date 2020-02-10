const { connect } = dva;
const { Form, Form: { Item }, Input, Button, InputNumber } = antd;
const { CrudForm } = require('../../Widgets.jsx');
const Constants = require("../../Constants.jsx");
import { namespace } from '../../../models/sys/Menu.jsx'

class SysModuleForm extends CrudForm {
    static defaultProps = {
        namespace: namespace
    };
    render() {
        const { form: { getFieldDecorator }, edit: { record, title }, loading, dispatch } = this.props, { item: { layout, tailLayout }} = Constants.FORM;
        return (
            <div style={{padding: '40px 20px'}}>
                <Form onSubmit={this.handleSubmit.bind(this)}>
                    <Item
                        {...layout}
                        label={<span className="form-title">{title ? title : ('系统菜单' + (record ? '修改' : '录入'))}</span>}
                        colon={false}
                    >
                    </Item>
                    <Item className="none">
                        {getFieldDecorator('pid')(<Input />)}
                    </Item>
                    <Item {...layout} label="菜单文本" hasFeedback>
                        {getFieldDecorator('label', {
                            rules: [
                                { required: true, message: '请输入菜单文本!' },
                                { min: 2, max: 50, message: '菜单文本长度为2-50!' }
                            ]
                        })(
                            <Input />
                        )}
                    </Item>
                    <Item {...layout} label="菜单图标" hasFeedback>
                        {getFieldDecorator('icon', {
                            rules: [{ max: 50, message: '菜单图标最大长度为50!' }]
                        })(
                            <Input />
                        )}
                    </Item>
                    <Item {...layout} label="uri" hasFeedback>
                        {getFieldDecorator('path', {
                            rules: [{ max: 50, message: 'uri最大长度为255!' }]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label="权限标识" hasFeedback>
                        {getFieldDecorator('sn', {
                            rules: [{ max: 255, message: '权限标识最大长度为255!' }]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label="排序号" hasFeedback>
                        {getFieldDecorator('weight', {
                            rules: [{ required: true, message: '请输入排序号!' }]
                        })(<InputNumber min={1} max={99999} defaultValue={1} style={{ width: '100%' }} />)}
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
    return { ...state[namespace], loading: state.loading.effects[`${namespace}/save`]  }
})(Form.create()(SysModuleForm));