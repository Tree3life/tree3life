/**
 * 对聊天组件中 websocket相关处理 的抽取
 *
 * 各种方法的使用要在react组件中使用bind(componetThis)  修改函数内部this的指向
 * 例如：
 * websocket.addEventListener("message", dispatchMessage.bind(this))，
 * 这里实际上绑定是 dispatchMessage.bind(this)的返回值
 *
 * 【func.bind(obj)的背后】
 * 1.将 `函数func` 内部的this 绑定为 obj,
 * 2.func.bind(obj) 的返回值是一个新的函数，
 * 即：const newFunc=func.bind(obj)
 */
import {commandType, contentType} from "@/resources/constant/tree3chat";
import {message} from "antd";
import {nanoid} from "nanoid";
import settings from "@/resources/application";
import beanFactory from "@/resources/factory";
import history from "@/util/history";
import dayjs from "dayjs";


// import settings from "@/resources/application";
//功能区
export const funMenu = {friends: 'friendList', groups: 'groupList', findFriends: 'findFriends'}
export const defaultAvatar = "https://tree3.oss-cn-hangzhou.aliyuncs.com/favor/caticonO.png"
export const serverId = -1;//在用户表中代表服务器对象的id
export const clientId = -999;//在用户表中代表客户端对象的id
/**
 * idleDetectionInterval空闲检测的间隔事件设置为： 5分钟（300秒 ,300000毫秒 ）
 * heartbeatInterval 前端心跳包的发送间隔设置为：2分钟（120000毫秒 ）
 * reconnectIntervalTimeout自动重连的间隔设置为：1分钟（60000毫秒 ）
 */
export const idleDetectionInterval = 300000;
// export const heartbeatInterval = 120000;
export const heartbeatInterval = 120000;
export const reconnectIntervalTimeout = 60000;
export const chatInitInfo = {
    websocket: undefined,
    /**
     * 最新的接收到的消息 的时间戳
     * 每次接收到新消息都应当去主动更新本字段，有助于减少心跳检测的流量
     * （不更新也没什么坏影响）
     * @type {number}
     */
    lastReceiveMsgTime: -1,
    /**
     * 最新的发送的消息 的时间戳
     * 每次发送新消息后都应当去主动更新本字段，有助于减少心跳检测的流量
     * （不更新也没什么坏影响）
     * @type {number}
     */
    lastSendMsgTime: -1,
    /**
     * doDisconnect: true,主动断开连接
     * doDisconnect: false,异常断开连接
     *
     */
    doDisconnect: false,
    selectedMenu: funMenu.friends,//联系人集合:friends/groups
    userId: '',//当前用户的id
    token: undefined,
    currentSession: {//currentSession 当前的聊天对象
        contactor: {historyRefreshed: false,},//聊天对象 群聊Obj或朋友Obj(        historyInfo: [],//与当前聊天对象的 聊天消息
    },


    friendList: [
        {
            id: nanoid(5),
            title: '好友1',//用户名、群名
            avatar: defaultAvatar,//头像
            lastMsg: '最近的一条消息简略',//最近的一条消息简略
            lastTime: '发送时间',//最近消息的发送时间
            countUnread: [],
            historyRefreshed: false,//本次连接过程中是否已经 查询过 双方的聊天记录
            histories: [//聊天历史，按时间的升序进行排列
                {
                    id: '', messageType: 1, state: '',
                    from: '', to: '', content: '',
                    contentType: '', createTime: ''
                },
                {
                    id: '', messageType: 1, state: '',
                    from: '', to: '', content: '',
                    contentType: '', createTime: ''
                }
            ]
        },
        {
            id: nanoid(5),
            title: '好友2',//用户名、群名
            avatar: defaultAvatar,//头像
            lastMsg: '最近的一条消息简略',//最近的一条消息简略
            lastTime: '发送时间',//最近消息的发送时间
            countUnread: [],
            historyRefreshed: false,//本次连接过程中是否已经 查询过 双方的聊天记录
            histories: [//聊天历史，按时间的升序进行排列
                {
                    id: '', messageType: 1, state: '',
                    from: '', to: '', content: '',
                    contentType: '', createTime: ''
                },
                {
                    id: '', messageType: 1, state: '',
                    from: '', to: '', content: '',
                    contentType: '', createTime: ''
                }
            ]
        },
    ],
    groupList: [
        {
            id: nanoid(5),
            // members: [],
            title: '群名1',//用户名、群名
            avatar: defaultAvatar,//头像
            lastMsg: '最近的一条消息简略',//最近的一条消息简略
            lastTime: '发送时间',//最近消息的发送时间
            countUnread: [],
            histories: [//聊天历史，按时间的升序进行排列
                {
                    id: '', command: 1, state: '',
                    from: '', to: '', content: '',
                    contentType: '', createTime: ''
                },
                {
                    id: '', command: 1, state: '',
                    from: '', to: '', content: '',
                    contentType: '', createTime: ''
                }
            ]
        },
        {
            id: nanoid(5),
            // members: [],
            title: '群名2',//用户名、群名
            avatar: defaultAvatar,//头像
            lastMsg: '最近的一条消息简略',//最近的一条消息简略
            lastTime: '发送时间',//最近消息的发送时间
            countUnread: [],
            histories: [//聊天历史，按时间的升序进行排列
                {
                    id: '', command: 1, state: '',
                    from: '', to: '', content: '',
                    contentType: '', createTime: ''
                },
                {
                    id: '', command: 1, state: '',
                    from: '', to: '', content: '',
                    contentType: '', createTime: ''
                }
            ]
        },
    ],
};
/**
 * ping定时任务
 * @type {undefined}
 */
export let pingInterval = undefined;
/**
 * 自动重连 定时任务
 * @type {undefined}
 */
export let reconnectInterval = undefined;


//region  2.处理 websocket 服务器 发来的消息
/**
 * 处理 websocket 服务器 发来的消息
 * @param event
 */
export function dispatchMessage(event) {
    // const {cache: {userInfo: {id: userId}}} = this.props;
    let msgFrame = event.data;
    let dataObj = JSON.parse(msgFrame);
    // commandType==200
    const {commandType: cmdType} = dataObj
    console.log('收到的socket后端响应数据：', dataObj)
    const {currentSession, friendList: frdList} = this.state
    //接收到消息之后就记录最新的接收时间
    this.setState({lastReceiveMsgTime: dayjs()})
    switch (cmdType) {
        case commandType.Pong:
            console.log("Pong：", dataObj)
            break;
        case commandType.ResponseLogin:
            // to be optimized@Rupert：优化此处的逻辑，优先使用本地缓存的数据 (2024/5/29 11:55)
            //处理 websocket认证后 服务器的响应数据
            const {data: {friends, groups}} = dataObj;

            //将该消息追加到用户对应的历史消息中去
            let friendList = friends.map(item => {
                let {friend, lastHistory} = item;
                return {
                    id: friend.id,
                    title: friend.remarkName ? friend.remarkName : friend.username,//用户名、群名
                    username: friend.username,//用户名、群名
                    remarkName: friend.remarkName,//备注
                    avatar: friend.avatar ? friend.avatar : defaultAvatar,//头像
                    intro: friend.intro ? friend.intro : '简介',
                    lastMsg: lastHistory ? lastHistory.content : '',//最近的一条消息简略
                    lastTime: lastHistory ? lastHistory.createTime : '',//最近消息的发送时间
                    countUnread: item.countUnread ? item.countUnread : [],
                    history: [],
                    historyRefreshed: false
                }
            });

            let groupList = groups.map(item => {
                let {members, lastHistory, groupName, id: groupId} = item;
                return {
                    id: groupId,
                    members,
                    title: groupName,//用户名、群名
                    groupName: groupName,//用户名、群名
                    // remarkName: remarkName,//用户对该群的备注名
                    avatar: item.avatar ? item.avatar : defaultAvatar,//头像
                    lastMsg: lastHistory ? lastHistory.content : '',//最近的一条消息简略
                    lastTime: lastHistory ? lastHistory.createTime : '',//最近消息的发送时间
                    countUnread: [],
                    history: [],
                    historyRefreshed: false
                };
            });

            //处理文本数据
            this.setState((preState, preProps) => {
                return {...preState, ...{friendList, groupList}};//变更 state中的tree3chat
            }, () => {
                if (friendList && friendList.length > 0) {
                    //初始化聊天组件的数据：默认展示第一个 好友的 聊天记录
                    this.contactorListRef.current.showChatHistory(friendList[0], funMenu.friends);
                }
            })
            break;
        case commandType.ResponsePrivateQueryHistory://查询单个用户的聊天记录
            //todo 使用map函数优化
            //更新currentSession 及 friendList的信息
            const newFriendList = frdList.map(item => {
                if (item.id === currentSession.contactor.id) {
                    item.history = dataObj.data;
                    item.historyRefreshed = true;
                    // 取消 未读消息的显示
                    item.countUnread = []
                    //将 该朋友设置为 currentSession
                    this.setTree3chatSession({contactor: item})
                    return item;
                } else {
                    return item;
                }
            });

            this.setTree3chatRoom({friendList: newFriendList})
            break;

        case commandType.PrivateChatText:
            const {from: freId} = dataObj ? dataObj : {};
            if (!dataObj.id) {//实时消息 是先转发 后 往数据库存储的，因此当前id应该是null
                dataObj.id = nanoid(5);
            }

            const newFriends = frdList.map(e => {
                if (e.id === freId) {
                    e.history = [...e.history, dataObj]
                    return e;
                } else {
                    return e;
                }
            })

            // to be optimized@Rupert：优化 考虑此处的优化！！！，这里 有点 小题大作 的 意味。【只需要更新 一个朋友的histor属性，但是却更新了所有的朋友及其属性】
            this.setTree3chatRoom({friendList: newFriends})
            break;
        default:
            break;
    }
}

//endregion 2.处理 websocket 服务器 发来的消息


//region  1.在与 websocket 服务器  成功建立连接之后 使用token进行认证
export function websocketOnOpen(websocket) {
    return (event) => {
        const {cache: {userInfo: {id: userId}, token}} = this.props;

        console.log(settings.str_message.WebSocketSuccess)
        //token 不存在时提示，重新登录
        if (!token) {
            message.warn(settings.str_message.NONE_TOKEN);
            //todo 关闭弹窗 并跳转到登录页面
            history.replace('/login');
            return;
        }
        //构造并发送消息
        const frame = beanFactory.createMessage(
            commandType.Login,
            userId, serverId,
            token
        );

        if (!websocket) {
            message.warn(settings.str_message.WebSocketFail);
            return;
        }


        //建立连接成功之后，发送注册消息，通知服务器将用户名和channel进行绑定
        //将event和websocket传给 处理消息的函数
        websocket.send(JSON.stringify(frame));

        //初始化 最新的消息时间
        const now = dayjs();
        this.setState({lastReceiveMsgTime: now, lastSendMsgTime: now}, () => {
            //开启心跳检测
            startHeartBeat(websocket, userId, this)();
        })
    }
}

//endregion  1.处理 websocket 服务器 发来的消息

//region  3.处理 websocket 断开事件
export function websocketOnClose(event) {
    //非主动退出，自动重连
    if (!this.state.doDisconnect) {
        const element = this;
        reConnect(element)();
        return;
    }

    //主动退出
    //1. 清除旧的连接
    // to be optimized@Rupert：这里表明 本项目中的websocket 设计上存在不合理的地方，
    //  后续需要对state/redux-cache/context 设计进行优化 (2024/6/13 16:25)
    //1.1 清除state&context中的websocket对象
    this.setState({websocket: undefined});
    //1.2 清除redux-cache中的websocket对象
    this.props.saveInLocalCache({websocket: undefined});
    //停止心跳检测
    clearInterval(pingInterval);
    message.warn(settings.str_message.WebSocketOnClose);
    return;

}

//endregion  3.处理 websocket 断开事件

//region 4 向websocket服务器发送消息
export function sendMessage(messageObj) {
    console.log('收到的的消息对象：', messageObj)

    //默认是text类型
    let msgType = messageObj.contentType ? messageObj.contentType : contentType.Text;

    const {websocket} = this.state
    if (!websocket) {
        message.warn(settings.str_message.WebSocketOnDisconnect);
        return;
    }

    switch (msgType) {
        case  contentType.Text:
            console.log('发送的消息对象：', messageObj)
            websocket.send(JSON.stringify(messageObj));
            break;
        default:
            message.warn(settings.str_message.WebSocketUnsupportedMessageTypes);
            break;
    }
}

//endregion 4 向websocket服务器发送消息


//region 5 心跳检测
/**
 * 开启心跳检测
 * 1. 客户端建立WebSocket连接。
 * 2. 客户端向服务器发送心跳数据包，服务器接收并返回一个表示接收到心跳数据包的响应。
 * 3. 当服务器没有及时接收到客户端发送的心跳数据包时，服务器会 发送一个关闭连接的请求。
 * 4. 服务器定时向客户端发送心跳数据包，客户端接收并返回一个表示接收到心跳数据包的响应。
 * 5. 当客户端没有及时接收到服务器发送的心跳数据包时，客户端会重新连接WebSocket
 * @param websocket
 * @returns {(function(*): void)|*}
 */
export function startHeartBeat(ws, userId, elementThis) {
    return () => {
        clearInterval(pingInterval);
        //周期性发送心跳包
        pingInterval = setInterval(() => {
            const now = dayjs()
            if (ws.readyState == WebSocket.CLOSING || ws.readyState == WebSocket.CLOSED) {
                console.log("聊天服务器已断开....)");
                clearInterval(pingInterval);
                return;
            }

            // 减小流量/不必要的ping ，检查最近的一条消息的发送时间，如果间隔较小，就不发送心跳包
            // fixme 心跳流量控制@Rupert：检查最近的一条消息的发送时间，如果间隔较小，就不发送心跳包 (2024/6/13 18:52)
            let nextSendMsgTime = dayjs(elementThis.state.lastSendMsgTime).add(heartbeatInterval / 3, 'millisecond')
            let timeFlag = dayjs(now).isBefore(nextSendMsgTime);
            if (timeFlag) {
                console.log("未发送的pinggggg：{}", elementThis.state.lastSendMsgTime, now, nextSendMsgTime)
                return;
            }
            let ping = beanFactory.createPingMessage(userId);
            // console.log("发送的pinggggg：{}", elementThis.state.lastSendMsgTime, now, nextSendMsgTime)
            ws.send(JSON.stringify(ping))
            elementThis.setState({lastSendMsgTime: now});
        }, heartbeatInterval);
    }
}

//endregion 5 心跳检测

//region 6 重连尝试
/**
 * 重连机制
 * 1. 前端`监听WebSocket的onclose()事件`，重新创建WebSocket连接。
 2. 使用WebSocket插件或库，例如Sockjs、Stompjs等。
 3. 使用心跳机制检测WebSocket连接状态，自动重连。
 4. 使用断线重连插件或库，例如ReconnectingWebSocket等。
 * @param data
 */
export function reConnect(elementThis) {
    return () => {
        //1. fixme@Rupert： 如果不是主动断开 && 最后一次消息的接收时间/发送时间+心跳间隔时间 < now() ，则判定为已经和服务器断开连接；
        if (!elementThis.state.doDisconnect
            // && (
            //     ((elementThis.state.lastSendMsgTime + heartbeatInterval) < now())//最后一次发送消息 超时（说明Ping出现了问题）
            //     || ((elementThis.state.lastReceiveMsgTime + heartbeatInterval) < now())//最后一次接收消息 超时（Pong出现了问题）
            // )
        ) {
            //3. 主动重连服务器
            reconnectInterval = setInterval(() => {
                elementThis.initWebsocket(settings.websocket_url);
            }, reconnectIntervalTimeout);
        }
    }
}

//endregion 6 重连尝试



