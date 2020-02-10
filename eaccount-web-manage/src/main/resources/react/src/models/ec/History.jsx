import ModelUtils from '../../utils/ModelUtils.jsx'

export const namespace =  'ecHistory';
export const baseUrl =  'ec/history';

const model = ModelUtils(baseUrl, namespace);

export default {
    ...model
};