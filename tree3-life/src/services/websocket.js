// //消息对象
// export const frame = {
//     "from": "",
//     "to": "",
//     "message": "",
//     "type": "",
//     "id": '',
// }
//
// // todo使用promise封装websocket连接
//
// /**
//  * 当前socket对象
//  * 建立webscoket连接之后进行赋值
//  * @type {undefined}
//  */
// // export let currentSocket = new Promise((resolve, reject) => {
// //
// //     resolve(()=>{return 'aaaabbbbbbb'})
// //     reject(()=>{return 'eeeeeeeeeeee'})
// // })
// // export let currentSocket = ((params) => {
// //         let socket = undefined
// //         try {
// //             socket = new WebSocket('ws://127.0.0.1:8088/webSocket/aaa')
// //         } catch (e) {
// //             //todo 消息提示连接失败
// //             console.log(e,'是啵啵啵啵啵啵啵啵啵啵啵啵')
// //             return socket
// //         }
// //     }
// // )()
//
// // function currentSocket(url,msg){
// // export function currentSocket(url){
// //     return new Promise((resolve, reject)=>{
// //         const socket = new WebSocket(url)
// //         socket.onopen = () => {
// //             // socket.send(JSON.stringify(msg))
// //             setTimeout(()=>{ //当服务未回应时，判断socket状态，若不为1表示失败，做链接失败处理
// //                 if(socket.readyState!=1){
// //                     reject('连急急急急急急error');
// //                 }
// //             },1500)
// //         }
// //         socket.onerror = (e) => { //websocket链接失败
// //             reject('socket.onerror error');
// //         }
// //         socket.onmessage = (e) => { //websocket链接成功
// //             resolve('socket.onmessage  success');
// //         }
// //     })
// // }
//
//
// // new WebSocket('ws://127.0.0.1:8088/webSocket/aaa')
//
//
// // async  (username)=>{ await }
//
//
// // 与websocket服务器创建连接
// export function createWebSocket() {
//     // 注意这里的端口号是后端服务的端口号，后面的是后端正常请求的路径，ziyuan是我的项目名，最后面的是我放在cookie中的当前登陆用户
//     let websocket = new WebSocket('ws://127.0.0.1:8088/webSocket/用户名')
//     console.log('ws://127.0.0.1:8088/webSocket/')
//     // 连接成功时
//     websocket.onopen = () => {
//         console.log("连接成功")
//         // websocket.send('hello')
//     }
//     websocket.onmessage = event => {
//         // 后端发送的消息在event.data中
//         console.log(event.data)
//     }
//     websocket.onclose = function () {
//         console.log('关闭了')
//     }
//     // 路由跳转时结束websocket链接
//     // this.$router.afterEach(function () {
//     //     websocket.close()//断开websocket链接
//     // })
//     // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常
//     window.onbeforeunload = function () {
//         websocket.close()
//     }
// }
//
//
//
