const { Form, Row, Col, Button, Icon, Tooltip } = antd;
const FormItem = Form.Item;
import { isArray } from '../../utils/CheckUtils.jsx';

const BaseSearchForm = React.createClass( {
    getInitialState() {
        return {
            expand: false,
            close: false,
            closeNum: 2
        };
    },
    componentDidMount() {
        this.props.form.setFieldsValue(this.props.searchData || {});
    },
    handleSearch(e) {
        e.preventDefault();
        const values = this.props.form.getFieldsValue();
        this.props.handleSearch ? this.props.handleSearch(values) : '';
    },
    handleReset() {
        this.props.form.resetFields();
    },
    toggle(type) {
        const state = {};
        state[type] = !this.state[type];
        this.setState(state);
    },
    // To generate mock Form.Item
    getFields() {
        const count = this.state.expand ? 10 : this.state.closeNum;
        const { getFieldDecorator } = this.props.form;
        const formItemLayout = {
            labelCol: { span: 5 },
            wrapperCol: { span: 19 },
        };
        const children = [];
        let fields = this.props.children;

        if(!isArray(fields)) {
            fields = [fields];
        }
        fields.forEach((field, index) => {
            children.push(
                <Col span={8} key={index} style={{ display: index < count ? 'block' : 'none' }}>
                    <FormItem {...formItemLayout} label={field.props.label}>
                        {getFieldDecorator(field.props.name)(
                            field
                        )}
                    </FormItem>
                </Col>
            );
        });
        return children;
    },
    render() {
        const { expand, closeNum, close } = this.state;
        const { children, loading, searchDataLabel } = this.props;
        const childrenLength = isArray(children) ? children.length : 1;
        const showLength = closeNum > childrenLength || expand ? childrenLength : closeNum;
        const search = <span>
                        <Button type='primary' htmlType='submit' disabled={loading}>{searchDataLabel || '搜索'}</Button>
                        {
                            childrenLength > closeNum ?
                                <a style={{ marginLeft: 8, fontSize: 12 }} onClick={this.toggle.bind(this, 'expand')}>
                                    {expand ? '收起' : '展开'} <Icon type={expand ? 'up' : 'down'} />
                                </a>
                                : ''
                        }
                    </span>;
        return (
            <div>
                <Tooltip placement="bottomRight" title={close ? '展示查询面板' : '隐藏查询面板'}>
                    <Icon type={close ? 'arrows-alt' : 'shrink'} onClick={this.toggle.bind(this, 'close')}
                          style={{ float: 'right', margin: close || expand ? 5 : 10, cursor: 'pointer' }}
                    />
                </Tooltip>
                <Form
                    className={close ? 'ant-advanced-search-form none' : 'ant-advanced-search-form'}
                    onSubmit={loading ? undefined : this.handleSearch}
                >
                    <Row gutter={40}>
                        {this.getFields()}
                        {showLength > 2 ? '' : search}
                    </Row>
                    {showLength > 2 ?
                        <Row style={{ 'padding-bottom': 14 }}>
                            <Col span={24} style={{ textAlign: 'right' }}>{search}</Col>
                        </Row>
                        : ''}
                </Form>
            </div>
        );
    }
});

module.exports = Form.create()(BaseSearchForm);