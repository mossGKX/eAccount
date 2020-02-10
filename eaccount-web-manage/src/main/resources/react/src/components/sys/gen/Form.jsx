const { connect } = dva;
const { Form, AutoComplete, Input, InputNumber, Button, Table, Switch,  Radio: { Group } } = antd;
const { Item } = Form;
const { CrudForm, RemoteSelect } = require('../../Widgets.jsx');
const Constants = require("../../Constants.jsx");
import { namespace, baseUrl } from '../../../models/sys/Gen.jsx'

class SysModuleForm extends CrudForm {
    static defaultProps = {
        namespace: namespace
    };
    dealSubmitData(values) {
        const { edit: { record } } = this.props
        if(record) {
            values.attrs = this.editableAttr.getValue();
        }
        return values;
    }
    render() {
        const { form: { getFieldDecorator }, edit: { record }, loading, dispatch } = this.props, { item: { layout, tailLayout }} = Constants.FORM;
        return (
            <div style={{padding: '40px 20px'}}>
                <Form onSubmit={this.handleSubmit.bind(this)}>
                    <Item {...layout} label={<span className="form-title">自动代码{record ? '生成' : '创建'}</span>} colon={false}>
                    </Item>
                    <Item {...layout} label="包名">
                        {getFieldDecorator('packageName', {
                            rules: [{ required: true, message: '请输入包名!' }],
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label="实体名" hasFeedback>
                        {getFieldDecorator('entityName', {
                            rules: [{ required: true, message: '请输入实体名!' }],
                        })(<Input />)}
                    </Item>
                    <Item {...layout} label="uri" hasFeedback>
                        {getFieldDecorator('uri', {
                            rules: [
                                { required: true, message: '请输入uri!' },
                                { pattern: /^[^/].*[^/]$/, message:  '不能使用/符合开头和结尾，正确如：sys/config' }
                            ]
                        })(<Input />)}
                    </Item>
                    {!record ? '' : <Item {...layout} label="类型" hasFeedback>
                        {getFieldDecorator('noGen', {
                            rules: [
                                { required: true, message: '请选择类型!' }
                            ]
                        })(<Group options={[{ label: '保存并生成代码', value: false }, { label: '只保存', value: true } ]} />)}
                    </Item>}
                    {record ? '' : <Item {...layout} label="数据库表">
                        {getFieldDecorator('dataTable', {
                            rules: [{ required: true, message: '请选择数据库表!' }],
                        })(<RemoteSelect url={`${baseUrl}/getAllTables`} />)}
                    </Item>}
                    {record ? <EditableAttr ref={ele => this.editableAttr = ele} dataSource={record.attrs}/> : ''}
                    <Item {...tailLayout}>
                        <Button type="primary" htmlType="submit" disabled={loading} size="large">{record ? '代码生成' : '提交'}</Button>
                        <Button size="large" style={{ marginLeft: 8 }} onClick={ () => dispatch({ type: `${namespace}/closeFormPage`})}>返回</Button>
                    </Item>
                </Form>
            </div>
        );
    }
}

class EditableAttr extends React.Component {
    state = {
        dataSource: this.props.dataSource
    };
    getValue() {
        return this.state.dataSource;
    }
    onChange(value, dataIndex, index) {
        console.log(value);
        const { dataSource } = this.state;
        dataSource[index][dataIndex] = value;
        this.setState({
            dataSource: dataSource
        });
    }
    render() {
        const { dataSource } = this.state, me = this,
            switchProps = { checkedChildren:"是", unCheckedChildren: "否", size: "small" };
        const switchRender = (dataIndex) => {
            return (text, record, index) => {
                return <Switch
                    checkedChildren="是"
                    unCheckedChildren="否"
                    size="small"
                    checked={ text === '1' }
                    onChange={ (checked) => {me.onChange(checked ? '1' : '0', dataIndex, index)} }
                />
            }
        };
        const inputRender = (dataIndex) => {
            return (text, record, index) => {
                return <Input value={text} size="small" onChange={ (e) => {this.onChange(e.target.value, dataIndex, index)} }/>
            }
        };
        return (
            <Table
                style={{ marginBottom: 24 }}
                bordered
                pagination={false}
                dataSource={dataSource}
                columns={[ {
                    title: '表字段',
                    dataIndex: 'jdbcName',
                    width: '15%'
                }, {
                    title: '类变量',
                    dataIndex: 'javaName',
                    width: '15%',
                    render: inputRender('javaName')
                }, {
                    title: '字段描述',
                    dataIndex: 'des',
                    width: '15%',
                    render: inputRender('des')
                }, {
                    title: 'Java类型',
                    dataIndex: 'javaType',
                    width: '20%',
                    render: (text, record, index) =>
                        <AutoComplete
                            value={text}
                            dataSource={autoDataSource}
                            filterOption={(inputValue, option) => option.props.children.toUpperCase().indexOf(inputValue.toUpperCase()) !== -1}
                            onChange={ (value) => {this.onChange(value, 'javaType', index)} }
                        />
                }, {
                    title: '插入?',
                    dataIndex: 'insert',
                    render: switchRender('insert')
                }, {
                    title: '更新?',
                    dataIndex: 'update',
                    render: switchRender('update')
                }, {
                    title: '列表?',
                    dataIndex: 'list',
                    render: switchRender('list')
                }, {
                    title: '查询?',
                    dataIndex: 'query',
                    render: switchRender('query')
                }, {
                    title: '排序',
                    dataIndex: 'weight',
                    width: 100,
                    render: (text, record, index) =>
                        <InputNumber value={text} size="small" onChange={ (value) => {this.onChange(value, 'weight', index)} }/>
                } ]} />
        )
    }
}

const autoDataSource = [ 'String', 'int', 'Integer', 'long', 'Long', 'double', 'Double', 'float', 'Float', 'boolean', 'Boolean' ];

module.exports = connect(state => {
    return { ...state[namespace], loading: state.loading.effects[`${namespace}/save`]  }
})(Form.create()(SysModuleForm));