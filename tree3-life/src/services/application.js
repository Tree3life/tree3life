/*认证相关的接口*/

import {post, get} from './index'
import {gatewayPrefix} from '@/resources/application'


export const login = user => post(gatewayPrefix.app + "/login", user)
export const logout = () => get(gatewayPrefix.app + "/logout")
