/*认证相关的接口*/

import {post, get} from './index'
import {gatewayPrefix} from '@/resources/application'
// import {message} from "antd";
const prefix = gatewayPrefix.users + "/users";
export const searchUsers = params => {

    return get(prefix + "/list", params);
}
