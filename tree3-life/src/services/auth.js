/*认证相关的接口*/

import {post, get} from './index'
// import {gatewayPrefix} from '@/resources/application'
import {gatewayPrefix} from '@/resources/application'
// import {message} from "antd";

export const login = user => {
// export const login = async user => {
    // const resp = await post(gatewayPrefix.auth + "/login", user);
    // message.success(resp['message']);
    // return resp.data;
    return post(gatewayPrefix.auth + "/login", user);
}
export const logout = () => get(gatewayPrefix.auth + "/logout")
