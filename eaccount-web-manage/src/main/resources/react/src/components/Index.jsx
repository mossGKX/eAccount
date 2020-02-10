const { connect } = dva;
const { Spin } = antd;
import { namespace } from '../models/App.jsx'

function Index (props) {
    const { loading, children } = props;
    return(
        <Spin spinning={!!loading}>{children}</Spin>
    );
}

module.exports = connect(state => {
    const effects = state.loading.effects;
    return {
        ...state[namespace],
        loading: effects[`${namespace}/reloadLoginData`]
    };
})(Index);