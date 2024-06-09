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


// import settings from "@/resources/application";
//功能区
export const funMenu = {friends: 'friendList', groups: 'groupList',findFriends:'findFriends'}
export const defaultAvatar = "https://tree3.oss-cn-hangzhou.aliyuncs.com/favor/caticonO.png"
export const serverId = -1;//在用户表中代表服务器对象的id
export const clientId = -999;//在用户表中代表客户端对象的id
export const chatInitInfo = {
    websocket: undefined,
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
            countUnread:[],
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
            countUnread:[],
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
            countUnread:[],
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
            countUnread:[],
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

    switch (cmdType) {
        case commandType.ResponseLogin:
            // to be optimized@Rupert：优化此处的逻辑，优先使用本地缓存的数据 (2024/5/29 11:55)

            //处理 websocket认证后 服务器的响应数据
            const {data: {friends, groups}} = dataObj;

            //将该消息追加到用户对应的历史消息中去
            let friendList = friends.map(item => {
                let {friend, lastHistory} = item;
                return {
                    id: friend.id,
                    title: friend.remarkName?friend.remarkName:friend.username,//用户名、群名
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
                    countUnread:[],
                    history: [],
                    historyRefreshed: false
                };
            });
            console.log('=============================friendList', friendList);
            console.log('=============================groupList', groupList);

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
            console.log('收到的私聊信息', dataObj)
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

            //todo 优化 考虑此处的优化！！！，这里 有点 小题大作 的 意味。【只需要更新 一个朋友的histor属性，但是却更新了所有的朋友及其属性】
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

        //建立连接成功之后，发送注册消息，通知服务器将用户名和channel进行绑定
        //将event和websocket传给 处理消息的函数
        if (!websocket) {
            message.warn(settings.str_message.WebSocketFail);
            return;
        }


        websocket.send(JSON.stringify(frame));
    }
}

//endregion  1.处理 websocket 服务器 发来的消息

//region  3.处理 websocket 断开事件
export function websocketOnClose(event) {
    message.warn(settings.str_message.WebSocketOnClose);
}

//endregion  3.处理 websocket 断开事件

//region 4 向websocket服务器发送消息
export function sendMessage(messageObj) {
    console.log('收到的的消息对象：', messageObj)

    //默认是text类型
    let msgType = messageObj.contentType ? messageObj.contentType : contentType.Text;

    const {websocket} = this.state
    if (!websocket) {
        message.warn(settings.str_message.WebSocketFail);
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
