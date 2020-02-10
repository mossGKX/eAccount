import ModelUtils from '../../utils/ModelUtils.jsx'

export const namespace = 'sysResources';
export const baseUrl = 'sys/resources';

export default {
    ...ModelUtils(baseUrl, namespace)
};