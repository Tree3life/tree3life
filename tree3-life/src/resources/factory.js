import settings from '@/resources/application'
import {commandType} from "@/resources/constant/tree3chat";

/**
 * 个人信息
 * @param id
 * @param name
 * @param nickName
 * @param avatar
 * @returns {Object}
 */
function creatUser(id, name, nickName, avatar) {
    var user = {};
    user.id = id;
    user.name = name;
    user.nickName = nickName;
    user.avatar = avatar;
    user.friends = [];
    user.groups = [];
    return user;
}

function creatFriend(id, nickName, remarkName) {
    var friend = {};
    friend.id = id;
    friend.nickName = nickName;
    friend.remarkName = remarkName;//备注名
    friend.history = [];//聊天历史
    /**
     friends: [{
     id:undefined,
     history: [//聊天历史
     {}
     ]}
     ],//好友信息
     groups: [],//群聊信息
     */
    return friend;
}

function creatGroup(id, name) {
    var group = {};
    group.id = id;
    group.name = name;
    group.history = [];
    return group;
}

/**
 * 返回一个最基本的消息对象，
 * 有关 具体消息类型 的 拓展字段 需要自行设置
 | ---------------------  | ----------------------------------------------------|
 | sendTime 发件时间 date  | messageType 消息类型  int                              |
 | state 消息状态 int      | deliveryTime 送达/收件时间 date（随消息状态进行改变）(可选)|
 | from 发件人id          | to 收件人id/群id                                      |
 |---------------------  | ----------------------------------------------------|
 |contentType 消息体类型 int| content  消息体 Object  由具体Message类决定是否实现      |
 | --------------------- | ----------------------------------------------------|
 * @param commandType
 * @param from
 * @param to
 * @param content 发送消息的内容
 * @param payload 传递参数
 * @returns {Object}
 */
function createMessage(commandType, from, to, content, payload) {
    var message = {};
    message.createTime = new Date();
    // message.messageType = msgType;
    message.commandType = commandType;
    //默认创建的状态是`未送达`
    message.state = settings.messageStatus.Undelivered;
    // message.deliveryTime = undefined;
    message.from = from;
    message.to = to;
    //默认创建的是text类型
    // message.contentType = settings.contentType.Text;
    message.content = content;
    if (payload) {
        message.payload = payload;
    }
    return message;
}

function createPingMessage(from) {
    return {
        // id: nanoid(6),
        createTime: new Date(), commandType: commandType.Ping, from: from};
}

function createPongMessage(pingId, from) {
    return {id: pingId, createTime: new Date(), commandType: commandType.Pong, from: from};
}

let beanFactory = {
    createMessage,
    creatFriend,
    creatGroup,
    creatUser,
    createPingMessage,
    createPongMessage
};
export default beanFactory
