import ModelUtils from '${react.path}/utils/ModelUtils.jsx'

export const namespace =  '${react.namespace}';
export const baseUrl =  '${code.uri}';

const model = ModelUtils(baseUrl, namespace);

export default {
    ...model
};