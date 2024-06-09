import {get, post, put, del, patch, proGet} from './http.js'
import * as sysRole from './sysRole'
import * as websocket from './websocket'
import * as auth from "./auth";
import * as user from "./user";

/**
 * 此文件用于注册后端请求
 * 步骤：
 * step1:引入新增的api文件；
 * step2:暴露对象  ；
 *
 * 范例：
 * step1：import http from '@/service/http'
 * step2：将http对象注册到export default{}中
 *
 *
 * 养成习惯：
 * 对于路径字符串的拼接，每个部分只负责前面的`/`，不负责后面的（管前不管后--管钱不管厚）。
 * 推荐："http://localhost:9200/archives/aaa"="http://localhost:9200"+"/archives"+"/aaa"
 * 不推荐："http://localhost:9200/archives/aaa"="http://localhost:9200/"+"archives/"+"aaa"（或其它形式）
 *
 *
 */


/**
 * 统一暴露所有的业务请求接口
 * 使用示例：页面中调用user 对象的getUser()方法
 *       step1:import {user} from "../xxx/services";；
 *       step2: let respData= await user.getUser(2);//注：await 关键字要在 async关键字修饰的方法内部使用 async() => {await api()}
 * 注意事项：
 *       1.此处若使用 export default 会导致无法在页面 中 无法解构引入
 */
export {
    get, post, put, del, patch, proGet,//基础的http请求
    websocket,
    user,
    auth,
    sysRole,
}


/**
 * 接口调用案例
 import {user} from "../../services";
 sendRequest = async () => {
    //调用接口
    let serviceResp = await user.getUser(2);
    let serviceResps = await user.getUserList();
    console.log(serviceResp)
    console.log(serviceResps)
}
 */
