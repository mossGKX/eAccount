const { connect } = dva;
const { Form, Form: { Item }, Input, Button } = antd;
const { CrudForm } = require('../../Widgets.jsx');
const Constants = require("../../Constants.jsx");
import { namespace } from '../../../models/ec/History.jsx'

class ecHistoryForm extends CrudForm {
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
                    <Item {...layout} label='用户编号' hasFeedback>
                        {getFieldDecorator('userNo', {
                            rules: [
                                { required: true, message: '请输入用户编号!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='金额' hasFeedback>
                        {getFieldDecorator('amount', {
                            rules: [
                                { required: true, message: '请输入金额!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='风险预存期' hasFeedback>
                        {getFieldDecorator('riskDay', {
                            rules: [
                                { required: true, message: '请输入风险预存期!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='修改时间' hasFeedback>
                        {getFieldDecorator('editTime', {
                            rules: [
                                { required: true, message: '请输入修改时间!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='是否允许结算' hasFeedback>
                        {getFieldDecorator('isAllowSett', {
                            rules: [
                                { required: true, message: '请输入是否允许结算!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='备注' hasFeedback>
                        {getFieldDecorator('remark', {
                            rules: [
                                { required: true, message: '请输入备注!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='业务类型' hasFeedback>
                        {getFieldDecorator('trxType', {
                            rules: [
                                { required: true, message: '请输入业务类型!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='请求号' hasFeedback>
                        {getFieldDecorator('requestNo', {
                            rules: [
                                { required: true, message: '请输入请求号!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='银行流水号' hasFeedback>
                        {getFieldDecorator('bankTrxNo', {
                            rules: [
                                { required: true, message: '请输入银行流水号!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='资金变动方向' hasFeedback>
                        {getFieldDecorator('fundDirection', {
                            rules: [
                                { required: true, message: '请输入资金变动方向!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='创建时间' hasFeedback>
                        {getFieldDecorator('createTime', {
                            rules: [
                                { required: true, message: '请输入创建时间!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='账户余额' hasFeedback>
                        {getFieldDecorator('balance', {
                            rules: [
                                { required: true, message: '请输入账户余额!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='是否完成结算' hasFeedback>
                        {getFieldDecorator('isCompleteSett', {
                            rules: [
                                { required: true, message: '请输入是否完成结算!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='版本号' hasFeedback>
                        {getFieldDecorator('version', {
                            rules: [
                                { required: true, message: '请输入版本号!' }
                            ]
                        })(<Input />)}
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
})(Form.create()(ecHistoryForm));