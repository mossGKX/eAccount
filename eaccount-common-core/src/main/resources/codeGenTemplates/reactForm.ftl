const { connect } = dva;
const { Form, Form: { Item }, Input, Button } = antd;
const { CrudForm } = require('${react.path}/Widgets.jsx');
const Constants = require("${react.path}/Constants.jsx");
import { namespace } from '${react.path}/../models/${react.modelPath}.jsx'

class ${react.namespace}Form extends CrudForm {
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
            <#list insertAndUpdate as attr>
                <#if attr.javaName != "id">
                    <#if attr.insert == "1" && attr.update == "1">
                    <Item {...layout} label='${attr.des}' hasFeedback>
                        {getFieldDecorator('${attr.javaName}', {
                            rules: [
                                { required: true, message: '请输入${attr.des}!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <#else>
                    {
                        <#if attr.update == "1">!</#if>record ? '' :
                        <Item {...layout} label='${attr.des}' hasFeedback>
                            {getFieldDecorator('${attr.javaName}', {
                            rules: [
                                { required: true, message: '请输入${attr.des}!' }
                            ]
                            })(<Input />)}
                        </Item>
                    }
                    </#if>
                </#if>
            </#list>
                    <Item {...tailLayout}>
                        <Button type="primary" htmlType="submit" disabled={loading} size="large">提交</Button>
                        <Button size="large" style={{ marginLeft: 8 }} onClick={ () => dispatch({ type: `${_$}{namespace}/closeFormPage`})}>返回</Button>
                    </Item>
                </Form>
            </div>
        );
    }
}

module.exports = connect(state => {
return { ...state[namespace], loading: state.loading.effects[`${_$}{namespace}/save`] }
})(Form.create()(${react.namespace}Form));