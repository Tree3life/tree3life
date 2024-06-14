/**
 * 本文件是reducer和action creator的结合
 */
import ACTION from '../actiontypes'
import {clientId} from "@/components/Tree3ChatRoom/tree3chatWebsocket";

/**
 * reducer：person
 * @type {[{token,name: string, id: string, age: number}]}
 */
const initState = {
    token: "",
    id: clientId,
    name: '姓名',
    username: '',
    avatar: '',
    age: '',
    createTime: '',
    deleted: false,
    gender: '性别',
    locked: true,
}

//reducer，reducer本质是一个纯函数
export function user(preState = initState, action) {
    const {type, data} = action
    switch (type) {
        case ACTION.storageUserInfo: //暂存登录成功的用户信息
            //preState.unshift(data) //此处不可以这样写，这样会导致preState被改写，personReducer就不是纯函数了。
            return {...preState, ...data}
        default:
            return preState
    }
}

//region action creators
/**
 * 以下是 所有和 reducer：person 相关的 action
 * 即用于改变状态的方法
 * @param data
 * @returns {{data, type: string}}
 */
export const storageUserInfo = data => {
    return {type: ACTION.storageUserInfo, data}
}


//endregion action creators
