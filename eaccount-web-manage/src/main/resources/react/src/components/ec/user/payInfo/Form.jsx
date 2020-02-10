const { connect } = dva;
const { Form, Form: { Item }, Input, Button } = antd;
const { CrudForm } = require('../../../Widgets.jsx');
const Constants = require("../../../Constants.jsx");
import { namespace } from '../../../../models/ec/User/PayInfo.jsx'

class ecUserPayInfoForm extends CrudForm {
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
                    <Item {...layout} label='备注' hasFeedback>
                        {getFieldDecorator('remark', {
                            rules: [
                                { required: true, message: '请输入备注!' }
                            ]
                        })(<Input />)}
                    </Item>
                    {
                        record ? '' :
                        <Item {...layout} label='创建时间' hasFeedback>
                            {getFieldDecorator('createTime', {
                            rules: [
                                { required: true, message: '请输入创建时间!' }
                            ]
                            })(<Input />)}
                        </Item>
                    }
                    <Item {...layout} label='支付通道名称' hasFeedback>
                        {getFieldDecorator('payWayName', {
                            rules: [
                                { required: true, message: '请输入支付通道名称!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='用户编号' hasFeedback>
                        {getFieldDecorator('userNo', {
                            rules: [
                                { required: true, message: '请输入用户编号!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='状态' hasFeedback>
                        {getFieldDecorator('status', {
                            rules: [
                                { required: true, message: '请输入状态!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='通道编号' hasFeedback>
                        {getFieldDecorator('payWayCode', {
                            rules: [
                                { required: true, message: '请输入通道编号!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='商户key' hasFeedback>
                        {getFieldDecorator('partnerKey', {
                            rules: [
                                { required: true, message: '请输入商户key!' }
                            ]
                        })(<Input />)}
                    </Item>
                    {
                        record ? '' :
                        <Item {...layout} label='用户姓名' hasFeedback>
                            {getFieldDecorator('userName', {
                            rules: [
                                { required: true, message: '请输入用户姓名!' }
                            ]
                            })(<Input />)}
                        </Item>
                    }
                    <Item {...layout} label='支付商户secret' hasFeedback>
                        {getFieldDecorator('appSectet', {
                            rules: [
                                { required: true, message: '请输入支付商户secret!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='支付商户ID' hasFeedback>
                        {getFieldDecorator('merchantId', {
                            rules: [
                                { required: true, message: '请输入支付商户ID!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='支付私钥' hasFeedback>
                        {getFieldDecorator('rsaPrivateKey', {
                            rules: [
                                { required: true, message: '请输入支付私钥!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='支付公钥' hasFeedback>
                        {getFieldDecorator('rsaPublicKey', {
                            rules: [
                                { required: true, message: '请输入支付公钥!' }
                            ]
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label='支付appid' hasFeedback>
                        {getFieldDecorator('appId', {
                            rules: [
                                { required: true, message: '请输入支付appid!' }
                            ]
                        })(<Input />)}
                    </Item>
                    {
                        !record ? '' :
                        <Item {...layout} label='修改时间' hasFeedback>
                            {getFieldDecorator('editTime', {
                            rules: [
                                { required: true, message: '请输入修改时间!' }
                            ]
                            })(<Input />)}
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
})(Form.create()(ecUserPayInfoForm));