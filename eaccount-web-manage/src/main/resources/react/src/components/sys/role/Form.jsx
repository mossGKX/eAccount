const { connect } = dva;
const { Form, Input, Button } = antd;
const { Item } = Form;
const { CrudForm } = require('../../Widgets.jsx');
const { FORM: { item: { layout, tailLayout }}} = require("../../Constants.jsx");
import { namespace } from '../../../models/sys/Role.jsx'

class SysModuleForm extends CrudForm {
    static defaultProps = {
        namespace: namespace
    };
    render() {
        const { form: { getFieldDecorator }, edit: { record }, loading, dispatch } = this.props;
        return (
            <div style={{padding: '40px 20px'}}>
                <Form onSubmit={this.handleSubmit.bind(this)}>
                    <Item
                        {...layout}
                        label={<span className="form-title">系统角色{record ? '修改' : '录入'}</span>}
                        colon={false}
                    >
                    </Item>
                    <Item
                        {...layout}
                        label="角色名称"
                        hasFeedback
                    >
                        {getFieldDecorator('name', {
                            rules: [ {
                                required: true, message: '请输入角色名称!',
                            }, {
                                max: 36, message: '角色名称最大长度为36!',
                            } ],
                        })(
                            <Input />
                        )}
                    </Item>
                    <Item
                        {...layout}
                        label="说明"
                        hasFeedback
                    >
                        {getFieldDecorator('description', {
                            rules: [ {
                                max: 255, message: '说明最大长度为255!',
                            } ],
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
    return { ...state[namespace], loading: state.loading.effects[`${namespace}/save`] }
})(Form.create()(SysModuleForm));