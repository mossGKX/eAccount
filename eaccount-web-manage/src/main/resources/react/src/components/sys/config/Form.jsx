const { connect } = dva;
const { Form, Form: { Item }, Input, Button, Radio: { Group } } = antd;
const { CrudForm } = require('../../Widgets.jsx');
const Constants = require("../../Constants.jsx");
import { namespace } from '../../../models/sys/Config.jsx'

class sysConfigForm extends CrudForm {
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
                    <Item {...layout} label='参数组' hasFeedback>
                        {getFieldDecorator('groupName', {
                            rules: [
                                { required: true, message: '请输入参数组!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='参数名称' hasFeedback>
                        {getFieldDecorator('name', {
                            rules: [
                                { required: true, message: '请输入参数名称!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='参数值' hasFeedback>
                        {getFieldDecorator('value', {
                            rules: [
                                { required: true, message: '请输入参数值!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='描述' hasFeedback>
                        {getFieldDecorator('des', {
                            rules: [
                                { required: true, message: '请输入描述!' }
                            ]
                        })(<Input />)}
                    </Item>
                    {
                        !record ? '' :
                        <Item {...layout} label='状态' hasFeedback>
                            {getFieldDecorator('status', {
                            rules: [
                                { required: true, message: '请选择状态!' }
                            ]
                            })(<Group options={[{ label: '正常', value: '0' }, { label: '禁用', value: '1' } ]} />)}
                        </Item>
                    }
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
})(Form.create()(sysConfigForm));