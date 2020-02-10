const { connect } = dva;
const { Row, Col } = antd;
const { Plink } = require("../Widgets.jsx");
import { namespace } from '../../models/App.jsx'

function ErrorInvalid (props) {
    const { dispatch } = props;
    return (
        <div className="error-page">
            <Row type="flex" justify="space-around" align="middle" style={{ height: 'calc(100vh - 90px)' }}>
                <Col span={8}>
                    <img style={{ height: 320, maxHeight: 'calc(100vh - 94px)' }} src="resources/image/500.png"/>
                </Col>
                <Col span={6}>
                    <p className="text-center error-state">403</p>
                    <p className="text-center error-text">你的页面数据已过期</p>
                    <p className="text-center">
                        <Plink
                            style={{ fontSize: 18 }}
                            onClick={() => dispatch({ type: `${namespace}/reloadLoginData`, payload: { pathname: '-1' } })}>
                            重新获取
                        </Plink>
                    </p>
                </Col>
            </Row>
        </div>
    );
}

module.exports = connect(state => {
    return { ...state[namespace] };
})(ErrorInvalid);