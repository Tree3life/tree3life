// /**
//  * 维护一个可以被全局共享的 websocket对象
//  * ！！其实聊天相关的状态共享使用  React.createContext()在聊天组件内进行信息共享更合适，
//  * 那样聊天组件的独立性更强，耦合性更低。（todo 后续优化）
//  * @param preState
//  * @param data
//  * @returns {{}|*}
//  */
// import ACTION from "../actiontypes";
// import settings from '@/resources/application'
// import {message} from "antd";
//
// const initState = {
//     token: undefined,
//     websocketServer: undefined,
//     user: {id: undefined, name: '', nickName: '', avatar: ''},//个人信息
//     friends: [{
//         id: undefined,
//         history: [//聊天历史
//             {}
//         ]
//     }
//     ],//好友信息
//     groups: [],//群聊信息
// }
//
// /**
//  * initWebsocketServer: Symbol('初始化连接websocket服务器:connect、设置各种回调函数——(onopen，onmessage，onclose，onerror)'),
//  * sendMessageText: Symbol('发送websocket消息'),
//  * sendMessageBlob: Symbol('发送二进制数据ArrayBuffer消息'),
//  * sendMessageArrayBuffer: Symbol('发送websocket消息'),
//  * parseMessage: Symbol('解析websocket消息'),
//  * destroyWebsocketServer:Symbol("销毁与websocket服务器建立的连接对象"),
//  * @param preState
//  * @param action
//  * @returns {{}|*}
//  */
//
// //reducer
// export function tree3Chat(preState = initState, action) {
//     const {type, data} = action
//     /**
//      * 猜测: reducer中的switch case /else 等分支最后会被汇总到一起进行判断
//      */
//     switch (type) {
//         case ACTION.initWebsocketServer:
//             // 获取data中的所有对象，并存入到catch中
//             return {...preState, websocketServer: data};
//         case ACTION.destroyWebsocketServer:
//             //销毁该websocket对象
//             return {...preState, websocketServer: null};
//         default:
//             return preState;
//     }
// }
//
// /**
//  * 异步的redux
//  * 1.与websocket服务器建立连接，
//  * 2.连接成功后 将token作为凭证发送给服务器，服务器将channel与token进行绑定
//  * @param token
//  */
// export const initWebsocketServer = (token) => {
//     return (dispatch) => {//在此处进行异步任务的处理
//         //连接websocket
//         let websocket = new WebSocket(settings.websocket_url)
//         //绑定各种回调函数
//         if (websocket) {
//             //指定连接成功后的回调函数
//             websocket.onopen = () => {
//                 console.log(settings.str_message.WebSocketSuccess)
//                 //构造并发送消息
//                 let frame = {
//                     //考虑消息的id是由前端生成较好还是后端生成较好
//                     "id": Math.round(Math.random() * 10000),
//                     "sendTime": 1, "msgType": 1,
//                     "cmd": 133, "state": 3,
//                     //当前登录的用户  system
//                     "from": 4, "to": -1,
//                     "body": token
//                 }
//                 //建立连接成功之后，发送注册消息，通知服务器将用户名和channel进行绑定
//                 websocket.send(JSON.stringify(frame));
//             };
//
//             //指定收到消息后的回调函数
//             websocket.onmessage = event => {
//                 //将event和websocket传给 处理消息的函数
//                 dispatch(parseMessage(event, websocket));
//             };
//             websocket.onclose = (event) => {
//                 message.success(settings.str_message.WebSocketOnClose);
//             }
//         } else {
//             message.warn(settings.str_message.WebSocketFail);
//         }
//
//
//         //派发任务
//         dispatch({
//             type: ACTION.initWebsocketServer, websocket
//         });
//     }
//
// }
//
//
// /**
//  * 释放websocket连接
//  * @param data
//  * @returns {{data, type: symbol}}
//  */
// export const destroyWebsocketServer = (data, payload) => (() => {
//
//
//     return (dispatch) => {
//         dispatch({
//             type: ACTION.destroyWebsocketServer, data
//         });
//     }
// })
//
//
// /**
//  * 向后端发送文本消息
//  * @param data
//  * @returns {{data, type: symbol}}
//  */
// // export const sendMessage = data => ({type: ACTION.sendMessage, data})
// export const sendMessageText = (data) => (() => {
//     return (dispatch) => {
//         dispatch({
//             type: ACTION.sendMessageText, data
//         });
//     }
// })
//
// /**
//  * 像后端发送Blob数据
//  * @param data
//  * @param payload
//  * @returns {function(): function(*): void}
//  */
// export const sendMessageBlob = (data, payload) => (() => {
//
//     return (dispatch) => {
//         dispatch({
//             type: ACTION.sendMessageBlob, data
//         });
//     }
// })
//
// /**
//  * 像后端发送ArrayBuffer数据
//  * @param data
//  * @param payload
//  * @returns {function(): function(*): void}
//  */
// export const sendMessageArrayBuffer = (data, payload) => (() => {
//
//     //note1.bufferedAmount属性，表示还有多少字节的二进制数据没有发送出去。它可以用来判断发送是否结束
//     // var data = new ArrayBuffer(10000000);
//     // socket.send(data);
//     //
//     // if (socket.bufferedAmount === 0) {
//     //     // 发送完毕
//     // } else {
//     //     // 发送还没结束
//     // }
//
//     return (dispatch) => {
//         dispatch({
//             type: ACTION.sendMessageArrayBuffer, data
//         });
//     }
// })
//
//
// /**
//  * 解析后端接收到的消息
//  * @param data
//  * @returns {{data, type: string}}
//  */
// export const parseMessage = (event, websocket) => {
//     let msgFrame = event.data;
//     console.log("收到的消息数据的类型ws.binaryType", websocket.binaryType)
//     console.log('收到的消息数据：', msgFrame);
//
//     //处理文本数据
//     if (typeof msgFrame === String) {
//         try {
//             let msg = JSON.parse(msgFrame);
//             //todo 将该消息追加到用户对应的历史消息中去
//             const history = [];
//
//             console.log("Received data string：", msg,history);
//         } catch (e) {
//             console.error(settings.str_message.WebSocketParseMessageError, msgFrame)
//             message.error(settings.str_message.WebSocketParseMessageError)
//         }
//     }
//     //todo 处理二进制数据
//     if (msgFrame instanceof ArrayBuffer) {
//         var buffer = msgFrame;
//         console.log("Received arraybuffer",buffer);
//     }
//     return (dispatch) => {
//         dispatch({type: ACTION.parseMessage, msgFrame})
//     }
// }
