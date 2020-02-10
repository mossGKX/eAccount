const { connect } = dva;
const { Form, Input, Button, InputNumber } = antd;
const { Item } = Form;
const { CrudForm } = require('../../Widgets.jsx');
const Constants = require("../../Constants.jsx");
import { namespace } from '../../../models/sys/Organization.jsx'

class SysModuleForm extends CrudForm {
    static defaultProps = {
        namespace: namespace
    };
    render() {
        const { form: { getFieldDecorator }, edit: { title, record }, loading, dispatch } = this.props, { item: { layout, tailLayout }} = Constants.FORM;
        return (
            <div style={{padding: '40px 20px'}}>
                <Form onSubmit={this.handleSubmit.bind(this)}>
                    <Item
                        {...layout}
                        label={<span className="form-title">{title ? title : ('组织' + (record ? '修改' : '录入'))}</span>}
                        colon={false}
                    >
                    </Item>
                    <Item className="none">
                        {getFieldDecorator('pid')(<Input/>)}
                    </Item>
                    <Item {...layout} label="名称" hasFeedback>
                        {getFieldDecorator('name', {
                            rules: [
                                { required: true, message: '请输入名称!' },
                                { min: 1, max: 50, message: '名称长度为1-50!' }
                            ]
                        })(
                            <Input />
                        )}
                    </Item>
                    <Item {...layout} label="描述" hasFeedback>
                        {getFieldDecorator('description', {
                            rules: [
                                { max: 256, message: '描述最大长度为256!' }
                            ]
                        })(
                            <Input />
                        )}
                    </Item>
                    <Item{...layout} label="优先级" hasFeedback>
                        {getFieldDecorator('weight', {
                            rules: [{
                                required: true, message: '请输入优先级!',
                            }],
                        })(
                            <InputNumber min={1} max={99999} defaultValue={1} style={{ width: '100%' }} />
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
})(Form.create()(SysModuleForm));