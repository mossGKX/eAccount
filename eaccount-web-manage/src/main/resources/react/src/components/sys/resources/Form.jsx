const { connect } = dva;
const { Form, Input, Button } = antd;
const { Item } = Form;
const { CrudForm } = require('../../Widgets.jsx');
const Constants = require("../../Constants.jsx");
import { namespace } from '../../../models/sys/Resources.jsx'

class SysModuleForm extends CrudForm {
    static defaultProps = {
        namespace: namespace
    };
    render() {
        const { form: { getFieldDecorator }, edit: { record }, loading, dispatch } = this.props, { item: { layout, tailLayout }} = Constants.FORM;
        return (
            <div style={{padding: '40px 20px'}}>
                <Form onSubmit={this.handleSubmit.bind(this)}>
                    <Item
                        {...layout}
                        label={<span className="form-title">系统资源{record ? '修改' : '录入'}</span>}
                        colon={false}
                    >
                    </Item>
                    <Item
                        {...layout}
                        label="模块"
                    >
                        {getFieldDecorator('groupName', {
                            rules: [{
                                required: true, message: '请选择模块!',
                                rules: [{ max: 255, message: '模块最大长度为255!' }]
                            }],
                        })(
                            <Input />
                        )}
                    </Item>
                    <Item
                        {...layout}
                        label="资源名称"
                        hasFeedback
                    >
                        {getFieldDecorator('name', {
                            rules: [{
                                required: true, message: '请输入资源名称!',
                                rules: [{ max: 255, message: '资源名称最大长度为255!' }]
                            }],
                        })(
                            <Input />
                        )}
                    </Item>
                    <Item
                        {...layout}
                        label="资源地址"
                        hasFeedback
                    >
                        {getFieldDecorator('url', {
                            rules: [{
                                required: true, message: '请输入资源地址!',
                                rules: [{ max: 255, message: '资源地址最大长度为255!' }]
                            }],
                        })(
                            <Input />
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
    return { ...state[namespace], loading: state.loading.effects[`${namespace}/save`]  }
})(Form.create()(SysModuleForm));