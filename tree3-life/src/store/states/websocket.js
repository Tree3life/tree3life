/**
 * 本文件用于缓存从后台请求过来的数据，
 * 1.要求：
 * i： catch中缓存的数据，只能进行读操作，不允许进行写操作；
 *          （与其它平级reducer文件的区别：
 *           a：对象不同 -->     其它reducer是共享组件间的数据，本文件的reducer是保存服务器共享出来的数据）
 *           b：读写性质不同 -->  其它reducer可以进行写操作，但是本文件不允许进行写操作）
 * ii：catch 要以json对象形式存储 后端初次请求后端响应回来的数据；这里的`写操作`与上述的`写操作`是不同的;
 *
 * 2.缓存策略或者说缓存原则（即哪些数据是需要、应当、甚至是必须要被缓存的）：
 * i. 在前端被多次重复使用，并且每次调用都需要发送请求从后端获取；
 * ii.后端响应的数据不随请求次数的增加而改变；
 *
 * 3.意义：减少对后端的 无意义的请求
 * @param preState
 * @param data
 * @returns {{}|*}
 */
import ACTION from "../type";
import settings from '../../resources/application'
// import {message} from "antd";

const initState = undefined

/**
 * cache 本项目内部的缓存区
 * @param preState
 * @param data
 * @returns {{}|*}
 */
export function websocket(preState = initState, action) {
    console.log("连接：websocket")
    //         let aa =new WebSocket('ws://127.0.0.1:8088/webSocket/aaa')
    const {type, data} = action
    /**
     * 猜测: reducer中的switch case /else 等分支最后会被汇总到一起进行判断
     */
    if (type === ACTION.saveInLocalCache) {
        // 获取data中的所有对象，并存入到catch中
        return {...preState, ...data}
    } else {
        return preState
    }
}

export const connectServer = (data) => {
    let websocket = new WebSocket(settings.websocket_url)
    websocket.onopen = () => {
        console.log('连接成功')
        //连接成功之后，应当将个人信息发送给服务器，服务器根据个人信息将channel进行会话管理
        //type：用于区分消息类型
        //发送消息
        let registframe = {
            "from": "",
            "to": "",
            "message": "",
            "type": 99,//99是约定好的注册channel消息类型
            "id": Math.round(Math.random() * 10000)
        }
        //建立连接成功之后，发送注册消息，通知服务器将用户名和channel进行绑定
        registframe.from = "张三丰"
        websocket.send(JSON.stringify(registframe));
    }
    websocket.onmessage = event => {
        // 后端发送的消息在event.data中
        // console.log(event.data)
        let msg = JSON.parse(event.data)
        console.log('收到数据：', msg, event);
    }
    websocket.onclose = function () {
        console.log('关闭方法执行')
    }
}

/**
 * 向后端发送消息
 * @param data
 * @returns {{data, type: symbol}}
 */
// export const sendMessage = data => ({type: ACTION.sendMessage, data})
export const sendMessage = (data, payload) => (() => {


    return (dispatch) => {
        dispatch({
            type: ACTION.sendMessage, data
        });
    }
})

/**
 * 解析后端接收到的消息
 * @param data
 * @returns {{data, type: string}}
 */
export const parseMessage = data => ({type: ACTION.parseMessage, data})
