/**
 * 本文件是reducer和action的结合
 */
import ACTION from '../type'

/**
 * reducer：person
 * @type {[{name: string, id: string, age: number}]}
 */
const initState = {}

export function user(preState = initState, action) {
    const {type, data} = action
    switch (type) {
        case ACTION.tempStorageUserInfo: //暂存登录成功的用户信息
            //preState.unshift(data) //此处不可以这样写，这样会导致preState被改写，personReducer就不是纯函数了。
            return {...preState, ...data}
        default:
            return preState
    }
}


/**
 * 以下是 所有和 reducer：person 相关的action
 * 即用于改变状态的方法
 * @param data
 * @returns {{data, type: string}}
 */

export const tempStorageUserInfo = data => ({type: ACTION.tempStorageUserInfo, data})


