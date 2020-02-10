import ModelUtils from '../../utils/ModelUtils.jsx'

export const namespace =  'sysConfig';
export const baseUrl =  'sys/config';

const model = ModelUtils(baseUrl, namespace);

export default {
    ...model
};