
class CrudForm extends React.Component {
    componentDidMount() {
        const { form: { setFieldsValue }, edit: { record } } = this.props;
        setFieldsValue(record ? record : {});
    }
    handleSubmit(e) {
        this.handleSubmitUrl(undefined, e);
    }
    handleSubmitUrl(url, e) {
        e.preventDefault();
        const { form: { validateFields }, edit: { record }, dispatch, namespace } = this.props;
        validateFields((err, values) => {
            if (err) {
                return;
            }
            values.key = record ? record.key : undefined;
            values.version = record ? record.version : undefined;
            values = this.dealSubmitData(values);
            dispatch(({ type: `${namespace}/save`, payload: { url: url || (values.key ? 'update' : 'save'), values }}));
        });
    }
    dealSubmitData(values) {
        // 子组件需要的时候实现
        return values;
    }
}

module.exports = CrudForm;