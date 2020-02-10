import ModelUtils from '../../../utils/ModelUtils.jsx'

export const namespace =  'ecUserPayInfo';
export const baseUrl =  'ec/user/payInfo';

const model = ModelUtils(baseUrl, namespace);

export default {
    ...model
};