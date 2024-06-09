/*
	本文件用于汇总所有的 reducer为一个总的reducer
*/
import {user} from "./user";//user reducer
import {cache} from "./cache";//cache reducer
// import {tree3Chat} from "./tree3Chat";//tree3Chat reducer

//汇总所有的reducer变为一个总的reducer
// eslint-disable-next-line import/no-anonymous-default-export
export default {
    cache,//内部缓存
    user,
    // tree3Chat
}
