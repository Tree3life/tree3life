import './index.css'
import React, {PureComponent} from 'react';
import settings from "@/resources/application";
import beanFactory from "@/resources/factory";
import {commandType} from "@/resources/constant/tree3chat";
import {fa, fakerZH_CN as faker} from '@faker-js/faker';
import {auth, user as userApi} from "@/services";
import {
    Avatar,
    Badge,
    Button,
    Card,
    Comment,
    Divider,
    Icon,
    List,
    message, Popover,
    Skeleton,
    Tabs,
} from "antd";
import {nanoid} from "nanoid";
import InfiniteScroll from 'react-infinite-scroll-component';
import dayjs from "dayjs";
import {connect} from "react-redux";
import {withRouter} from "react-router-dom";
import Meta from "antd/es/card/Meta";
import {
    funMenu,
    chatInitInfo,
    dispatchMessage,
    websocketOnOpen,
    websocketOnClose,
    sendMessage,
    defaultAvatar, clientId, startHeartBeat, reconnectInterval, pingInterval,
} from "@/components/Tree3ChatRoom/tree3chatWebsocket";
import {saveInLocalCache} from "@/store/states/cache";
import Search from "antd/es/input/Search";
// import {saveInLocalCache} from "@/store/states/cache";

const {TabPane} = Tabs;


//region主组件内用于的消息总线
const Tree3ChatRoomContext = React.createContext(
    chatInitInfo
);

const {Provider} = Tree3ChatRoomContext

/**
 * 查找好友消息
 */
class FindUser extends React.Component {
    constructor(props, p) {
        super(props, p);
        this.state = {
            matchedUsers: Array.from({length: 20}),
            applyMessages: Array.from({length: 20}),
            hasMoreMatchedUsers: true,
            hasMoreApplyMessages: true,
        }
    }

    fetchMoreApplyMessagesData = () => {
        if (this.state.applyMessages.length >= 500) {
            this.setState({hasMoreApplyMessages: false});
            return;
        }
        // a fake async api call like which sends
        // 20 more records in .5 secs
        setTimeout(() => {
            this.setState({
                applyMessages: this.state.applyMessages.concat(Array.from({length: 20}))
            });
        }, 500);
    };
    searchFriends = async () => {
        let resp = await userApi.searchUsers({username: "张胜男"});
    }
    fetchMoreMatchedUsersData = () => {
        if (this.state.matchedUsers.length >= 500) {
            this.setState({hasMoreMatchedUsers: false});
            return;
        }
        // a fake async api call like which sends
        // 20 more records in .5 secs
        setTimeout(() => {
            this.setState({
                matchedUsers: this.state.matchedUsers.concat(Array.from({length: 20}))
            });
        }, 500);
    };

    render() {
        const style = {
            height: '15%',
            border: "1px solid green",
            // margin: 6,
            padding: 8
        };
        return <>
            <Search
                placeholder={"用户名/群名"}
                onSearch={this.searchFriends}
                style={{width: '100%', margin: '1%'}}
            />
            <div id={"findUserListDom"}>查找展示列表
                <InfiniteScroll
                    dataLength={this.state.matchedUsers.length}
                    next={this.fetchMoreMatchedUsersData}
                    hasMore={this.state.hasMoreMatchedUsers}
                    loader={<h4>Loading...</h4>}
                    height={320}
                    endMessage={
                        <p style={{textAlign: "center"}}>
                            <b>Yay! You have seen it all</b>
                        </p>
                    }
                >
                    {this.state.matchedUsers.map((i, index) => (
                        <div style={style} key={index}>
                            <Popover content={
                                <div>
                                    <p>Content</p>
                                </div>
                            } title="Title">
                                <div style={{display: "flex", flexDirection: 'row', justifyContent: 'space-between'}}>
                                    <p>div - #{index}</p><Button size={"small"}>添加</Button>
                                </div>
                            </Popover>,
                        </div>
                    ))}
                </InfiniteScroll>
            </div>
            <div id={"applyMessagesDom"}>好友添加的通知
                <InfiniteScroll
                    dataLength={this.state.applyMessages.length}
                    next={this.fetchMoreApplyMessagesData}
                    hasMore={this.state.hasMoreApplyMessages}
                    loader={<h4>Loading...</h4>}
                    height={280}
                    endMessage={
                        <p style={{textAlign: "center"}}>
                            <b>Yay! You have seen it all</b>
                        </p>
                    }
                >
                    {this.state.applyMessages.map((i, index) => (
                        <div style={style} key={index}>
                            <Popover content={
                                <div>
                                    <p>Content</p>
                                </div>
                            } title="Title">
                                <div style={{display: "flex", flexDirection: 'row', justifyContent: 'space-between'}}>
                                    <p>div - #{index}</p><Button size={"small"}>同意</Button>
                                </div>
                            </Popover>,
                        </div>
                    ))}
                </InfiniteScroll>
            </div>
        </>;
    }


}

/**
 * 聊天组件
 */
@connect(state => ({
    cache: state.cache
}), {
    saveInLocalCache
})
@withRouter
class Tree3ChatRoom extends PureComponent {
    contactorListRef = React.createRef()

    constructor(props, context) {
        super(props, context);
        this.sendMessageFunc = sendMessage.bind(this);
        this.state = {
            ...chatInitInfo
        }
    }

    componentWillUnmount() {
        // 断开与服务器的连接&清空定时任务
        // if (this.state.websocket) {
        //     this.websocket.terminate()
        // }
        // debug@Rupert：对象(%o)、字符(%s)、数字:(%i、%d、%f)、样式:(%c) (2024/6/14 8:05)
        console.log("componentWillUnmount：")
        if (reconnectInterval) {
            clearInterval(reconnectInterval);
        }
        if (pingInterval) {
            clearInterval(pingInterval);
        }
    }

    componentDidMount() {
        console.log("componentDidMount：")
        this.initWebsocket(settings.websocket_url);
    }

    un


    initWebsocket = (address) => {
        //防止自动重连时，重复建立连接
        if (this.state.websocket) {
            if (reconnectInterval) {
                clearInterval(reconnectInterval)
                console.log("已建立websocket连接：无需重复建立")
            }
            return;
        }
        const {cache: {userInfo: {id: userId}, token}, saveInLocalCache} = this.props;
        //连接websocket
        const websocket = new WebSocket(address);
        //绑定各种回调函数
        if (websocket) {
            //指定连接成功后的回调函数
            // websocket.onopen = this.handleWsOpen(websocket)
            websocket.addEventListener("open", (websocketOnOpen.bind(this))(websocket))
            websocket.addEventListener("message", dispatchMessage.bind(this))
            // websocket.addEventListener("ping", startHeartBeat(websocket, userId))//心跳检测
            websocket.addEventListener("close", websocketOnClose.bind(this))
            saveInLocalCache({
                websocket, setDoDisconnect: (doDisconnect) => {
                    return this.setState({doDisconnect: doDisconnect})
                }
            });
            this.setTree3chatRoom({
                websocket, token, userId
            });
        } else {
            message.warn(settings.str_message.WebSocketFail);
        }
        return websocket;
    }

    setTree3chatSession = (newSession) => {
        this.setState((preState, preProps) => {
            console.log("session更新之前", preState, preProps)
            return {currentSession: {...preState.currentSession, ...newSession}};//变更 state中的tree3chat
        }, () => {
            console.log("session更新之后", this.state.currentSession)
        })
    }


    setTree3chatRoom = (newObj, task) => {
        this.setState((preState, preProps) => {
            return {...preState, ...newObj};//变更 state中的tree3chat
        }, () => {
            if (task) {
                task();
            }
        })
    }

    selectMenu = (key) => {
        console.log(key);
        this.setState({selectedMenu: key});
    }


    render() {
        const {cache: {userInfo}} = this.props;
        const {sendMessageFunc} = this
        return (
            <Provider value={
                {
                    ...this.state,
                    setTree3chatRoom: this.setTree3chatRoom,
                    setTree3chatSession: this.setTree3chatSession,
                }

            }
            >
                <div id="tree3ChatRoom">
                    <div id="funcArea">
                        <Tabs
                            tabPosition="left"
                            defaultActiveKey={funMenu.friends} onChange={this.selectMenu}>
                            <TabPane tab="添加好友" key={funMenu.findFriends}>
                                <FindUser></FindUser>
                            </TabPane>
                            <TabPane tab="好友管理" key={funMenu.friends} style={{backgroundColor: "green"}}>

                                <ContactorList ref={this.contactorListRef} sendMessage={sendMessageFunc}/>
                                {/*endregion 好友列表*/}
                            </TabPane>
                            <TabPane tab="群聊管理" key={funMenu.groups}>
                                {/*region 群聊列表*/}
                                <ContactorList sendMessage={sendMessageFunc}/>
                                {/*endregion 群聊列表*/}
                            </TabPane>

                        </Tabs>
                    </div>
                    <div id="mainArea">
                        <ChatMain sendMessage={sendMessageFunc} userInfo={userInfo}/>
                        {/*<img id={'welcomeImg'} src={defaultAvatar}*/}
                        {/*     style={{zIndex:9}}*/}
                        {/*/>*/}
                    </div>
                </div>
            </Provider>
        );
    }
}

/**
 * 联系人/群聊列表
 */
class ContactorList extends PureComponent {
    static contextType = Tree3ChatRoomContext;

    constructor(props, context) {
        super(props, context);
        this.state = {
            // contactorList: this.context[this.context.selectedMenu],
            loading: false,
            hasMore: true,
            allFriends: [],//所有朋友
            allGroups: [],//所有群聊
            doneCacheAllFriends: false,//暂存所有好友（两阶段终止模式）
            doneCacheAllGroups: false
        }
    }

    /**
     * 加载更多联系人信息
     */
    loadMoreContactor = () => {
        let data = this.state[this.context.selectedMenu];
        this.setState({
            loading: true,
        });
        if (data.length > 14) {
            message.warning('Infinite List loaded all');
            this.setState({
                hasMore: false,
                loading: false,
            });
            return;
        }
    }

    /**
     * 根据用户名称/备注进行过滤
     * @param name
     */
    filterFriend = name => {
        // todo@Rupert：发送websocket请求查找好友 (2024/6/1 9:52)

        //Step 1: 从自身的好友进行匹配
        const contactList = this.context[this.context.selectedMenu];
        let currentMenu = this.context.selectedMenu;
        if (currentMenu === funMenu.friends && this.context.friendList.length > 0) {
            if (!this.state.doneCacheAllFriends) {
                this.setState({allFriends: this.context.friendList, doneCacheAllFriends: true})
            }
            if (name === "") {//查询全部好友
                this.context.setTree3chatRoom({friendList: this.state.allFriends})
                return;
            }
            const matchedFriends = this.context.friendList.filter(item => {
                if (!item.title) {
                    return;
                }
                if (item.title.startsWith(name) || item.username.startsWith(name)) {
                    return true;
                }

                return false;
            })
            this.context.setTree3chatRoom({friendList: matchedFriends})

        }

        if (currentMenu === funMenu.groups) {

            //用于 传递"" 进行查询时展示所有的用户
            if (!this.state.doneCacheAllGroups) {
                this.setState({allGroups: this.context.groupList, doneCacheAllGroups: true})
            }
        }
        console.log('找朋友', currentMenu, name, contactList);
    };
    /**
     * 根据用户id，及 群聊/私聊类型 ，查询聊天消息并展示
     * @param id
     * @param menuType
     * @param isRefreshed 是否已经刷新过
     */
    showChatHistory = (current, menuType, isRefreshed) => {
        const {contactor} = this.context.currentSession
        const {id: contactorId} = contactor ? contactor : {id: clientId}

        // fixme@Rupert：防止一直点击同一个用户(仅登录后首次点击时，请求历史信息) (2024/5/29 19:57)
        if (this.context.selectedMenu === menuType && contactorId === current.id) {
            return;
        }

        //用户的聊天历史 是否已经刷新过
        if (!contactor && isRefreshed) {
            return;
        }

        if (menuType === funMenu.friends) {
            //查询私聊信息，
            const {userId} = this.context;
            //判断 本次连接过程中 是否已经查询过聊天记录
            // const friend = this.context.friendList.find(item => item.id === id);
            // const requiredRefresh = friend && !(friend.historyRefreshed);
            console.log("friendId,user,menuType", current, userId, menuType);


            //设置currentSession
            this.context.setTree3chatRoom({currentSession: {contactor: current}}, () => {

                //查询 聊天历史记录消息，并追加到用户的friendList:{friend:{history:[查询到的聊天记录]}}中
                const frame = beanFactory.createMessage(
                    commandType.PrivateQueryHistory,//查询两者的 聊天记录
                    userId,
                    -1,//这是一条发送给系统的消息
                    current.id,//将 聊天对象的id 以消息的内容形式传递
                    current.countUnread//将未读消息的id 传递给后端，实现消息状态的更新
                );
                this.props.sendMessage(frame);
            });

        } else if (menuType === funMenu.groups) {
            //todo 待办 查询群聊消息
            console.log("groupId,menuType", current, menuType);
        } else {
            message.warn("未知的聊天记录类型");
        }
    }

    render() {
        const contactList = this.context[this.context.selectedMenu];
        return (
            <div id="scrollableDivContactList" style={{
                // height: 400,
                overflow: 'auto',
                // padding: '0 16px',
                // border: '1px solid rgba(140, 140, 140, 0.35)',
            }}>
                {/*region 好友列表*/}
                <Search
                    placeholder={(this.context.selectedMenu === funMenu.friends ? "用户名" : (this.context.selectedMenu === funMenu.groups ? "群名" : '')) + "/备注"}
                    onSearch={() => {
                        console.log("查找朋友：")
                    }}
                    // onSearch={this.filterFriend}
                    style={{width: '100%', marginTop: '1%', marginBottom: '1%'}}
                />
                {/*region InfiniteScroll*/}
                <InfiniteScroll
                    dataLength={100}
                    hasMore={false}
                    next={() => {
                    }}
                    loader={<Skeleton avatar paragraph={{rows: 1,}}
                        // active
                    />}
                    // endMessage={<Divider plain>It is all, nothing more 🤐</Divider>}
                    scrollableTarget="scrollableDivContactList"
                >
                    {/*region List */}
                    <List
                        dataSource={contactList}
                        renderItem={
                            item => (
                                <List.Item key={item.id} onClick={() => {
                                    this.showChatHistory(item, this.context.selectedMenu, item.historyRefreshed)
                                }}>
                                    <List.Item.Meta
                                        avatar={
                                            // <a href="#">
                                            // to be optimized@Rupert：(2024/5/28 10:13)
                                            //  未读逻辑：(异常退出时未读消息的发送逻辑)
                                            //  Step 1: 退出聊天室时 发送 一条退出消息
                                            //  Step 2: 以该消息的时间点为准，将所有后续收到的信息 统计 为未读的消息
                                            <Badge count={item.countUnread ? item.countUnread.length : 0}>
                                                <span className="head-example"/>
                                                <Avatar src={item.avatar}/>
                                            </Badge>
                                            // </a>
                                        }
                                        title={item.title + ':' + item.id}
                                        description={item.lastMsg ? (item.lastMsg.length >= 10 ? (item.lastMsg.slice(0, 9) + '...') : item.lastMsg) : 'item.lastMsg.length'}
                                    />
                                    <div>
                                        <span>
                                            {dayjs(item.lastTime).format('MM/DD')}
                                        </span>
                                        <br/>
                                        <span> {dayjs(item.lastTime).format('HH:mm')}</span>
                                    </div>
                                </List.Item>
                            )
                        }
                    />
                    {/*endregion List*/}
                </InfiniteScroll>
                {/*endregion InfiniteScroll*/}
            </div>
        )
    }
}

/**
 * 聊天框主体、头部、输入框、右侧消息展示窗口
 * currentSession:
 {//currentSession 当前的聊天对象
 *
 *
    contactor: {
        id, title
    :
        '用户名', avatar, lastTime, lastMsg, history[], countUnread
    }
,//聊天对象 群聊Obj或朋友Obj(        historyInfo: [],//与当前聊天对象的 聊天消息
 *
}
 ,
 */
class ChatMain extends React.Component {
    static contextType = Tree3ChatRoomContext;
    textAreaRef = React.createRef();


    constructor(props, context) {
        super(props, context);
        this.state = {
            chatHistory: this.context.currentSession.contactor ? this.context.currentSession.contactor.history : [],
            loading: false,
            hasMore: true,
        };
    }

    /**
     * 删除好友
     */
    deleteFriend = () => {
        console.log("删除好友：")
    }

    /**
     * 编辑好友的备注
     * @param current
     * @returns {(function(): void)|*}
     */
    handleSendMessage = (current) => {
        return () => {
            let text = this.textAreaRef.current.value.trim()
            if (!this.context.websocket) {
                message.warn("与聊天服务器的连接已断开！");
            }
            if (!text) {
                return;
            }

            let {history, id} = current
            const msg = beanFactory.createMessage(commandType.PrivateChatText, this.context.userId, id, text);
            msg.id = nanoid(11);//向服务端发送消息之前前要将id去掉
            if (!history) {
                history = [];
            }
            //
            history.push(msg);
            //维护 currentSession,contactor状态
            const newContactor = {...current, history}
            this.context.setTree3chatSession({contactor: newContactor})

            if (this.context.friendList) {
                let currentSessionIndex = this.context
                    .friendList
                    .findIndex((element) => {
                        console.log(element.id, id)
                        return element.id === id
                    });
                if (currentSessionIndex >= 0) {
                    let {friendList} = this.context
                    friendList[currentSessionIndex] = newContactor
                    console.log('friendList', this.context)
                    // this.context.setTree3chatRoom({friendList})
                }
            }

            //向服务器发送聊天消息，由后端生成ID 并存储
            msg.id = null;//初次发送时前端使用的是临时id
            this.props.sendMessage(msg)
            // fixme 心跳流量控制@Rupert：这里修改后的 lastSendMsgTime的最新值，
            //  无法被心跳pingInterval中的elementThis.state.lastSendMsgTime
            //  读取到
            this.setState({lastSendMsgTime: dayjs()}, () => {
                console.log("修改lastSendMsgTime：", this.state.lastSendMsgTime)
            })
            this.textAreaRef.current.value = ''
        }
    }
    editFriendInfo = () => {
        // debug@Rupert：对象(%o)、字符(%s)、数字:(%i、%d、%f)、样式:(%c) (2024/6/1 9:36)
        console.log("编辑好友备注：")
    }


    /**
     * 加载更多的历史记录
     */
    loadMoreHistory = (a, b, c) => {
        // debug@Rupert：JS对象(%O)、DOM对象(%o)、字符(%s)、数字:(%i、%d、%f)、样式:(%c) (2024/5/26 15:59)
        console.log("%c加载更多的历史记录：", a, b, c)
        console.log(faker.person.fullName());

        //联系人的聊天记录
        if (this.context.currentSession.contactor.history.length >= 500) {
            this.setState({hasMore: false});
            return;
        }

        setTimeout(() => {
            const {contactor} = this.context.currentSession;
            // contactor.history.concat(Array.from({length: 20}));
            //todo 加载更多聊天历史
            contactor.history.concat(contactor.history);
            this.setState({
                // items: this.state.items.concat(Array.from({length: 20}))
                currentSession: {contactor: {...contactor}}
            });
        }, 500);
    }

    render() {
        const {
            currentSession: {
                contactor
            }
        } = this.context;

        const {//contactor当前联系人的信息
            id: contactId,//当前联系人的id
            title,//用户名
            avatar,//头像
            intro,
            // countUnread//未读消息的统计
        } = contactor ? contactor : {};
        const {userInfo} = this.props;
        return <>
            <div id="mainHeader">
                <span style={{fontSize: '30px', fontWeight: 'bold'}}>{title ? title : ''}</span> <span>功能</span></div>
            <div id="bodyWindow">
                <div id="historyWindow" style={
                    {//翻转  滚动条InfiniteScroll的关键
                        display: 'flex',
                        flexDirection: 'column-reverse',
                    }
                }>
                    {/*<div id="scrollableDivHistory" >*/}
                    <InfiniteScroll
                        dataLength={this.context.currentSession.contactor.history ? this.context.currentSession.contactor.history.length : 0}
                        //发送请求，返回值 null或undefined 时 false
                        next={this.loadMoreHistory}
                        hasMore={this.state.hasMore}
                        loader={<span className='loadingSpan'>Loading...</span>}
                        endMessage={
                            <p style={{textAlign: "center"}}>
                                <b>Yay! You have seen it all</b>
                            </p>
                        }
                        scrollThreshold="1000px"
                        //颠倒
                        style={{
                            display: 'flex',
                            flexDirection: 'column-reverse'
                        }} //To put endMessage and loader to the top.
                        inverse={true} //
                        scrollableTarget="historyWindow"
                    >
                        <List
                            dataSource={
                                this.context.currentSession.contactor.history ? this.context.currentSession.contactor.history.map(ele => {
                                        /**
                                         * 1.如果
                                         */
                                        if (ele.from === contactId) {//正在聊天的对象 的id 用户名/备注、头像、
                                            ele.avatar = avatar ? avatar : ''
                                            ele.username = title ? title : ''
                                        } else if (ele.from === userInfo.id) {//当前登录用户
                                            ele.username = userInfo.username ? userInfo.username : '昵称'
                                            ele.avatar = userInfo.avatar ? userInfo.avatar : defaultAvatar
                                        } else {
                                            message.error("消息串门了");
                                        }
                                        return ele;
                                    }
                                ) : []
                            }
                            renderItem={item => {
                                //avatar: "https://tree3.oss-cn-hangzhou.aliyuncs.com/favor/caticonO.png"
                                // command : 11
                                // content:  "上官2-->aaa1"
                                // createTime: "2024-05-24 20:09:41"
                                // deleted: false
                                // from:  2
                                // id : 44
                                // state: 0
                                // to : 1
                                // username: "aaa"
                                return (
                                    <List.Item key={item.id}>
                                        <div
                                            className={item.from === userInfo.id ? "userListItem" : 'contactorObjListItem'}>
                                            <Comment
                                                author={<span>{item.username + ':' + item.from}</span>}
                                                avatar={
                                                    <Avatar src={item.avatar} alt="头像丢失"/>
                                                }
                                                content={
                                                    <p>
                                                        {item.content}
                                                    </p>
                                                }
                                                datetime={
                                                    <span>{dayjs(item.createTime ? item.createTime : '').format("YYYY/MM/DD HH:mm")}</span>
                                                }
                                            />
                                        </div>
                                    </List.Item>
                                )
                            }}
                        />
                    </InfiniteScroll>
                    {/*</div>*/}
                </div>

                <div id="funcWindow">表情、文件</div>
                <textarea rows={6} placeholder={"输入..."} ref={this.textAreaRef} id="inputWindow">

               </textarea>
                {/*<input placeholder={"输入框aaaaaaaaaa"} type={"text"}/>*/}
                <Button size={"small"}
                        style={{position: "absolute", zIndex: "2", right: "8px", bottom: "10px"}}
                        onClick={this.handleSendMessage(contactor)}
                >发送
                </Button>
            </div>
            <div id="infoWindow">
                <Card className={'friendInfo'}
                    // style={{ width: 300 }}
                      cover={
                          <img src={avatar} alt={'图片没了'} className={"infoPicture"}/>
                      }
                      actions={[
                          <Icon type="setting" key="setting"/>,
                          <Icon type="edit" key="edit" onClick={this.editFriendInfo}/>,
                          <Icon type="delete" key="delete" onClick={this.deleteFriend}/>,
                      ]}
                >
                    <Meta
                        // avatar={<Avatar src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png" />}
                        // avatar={<Avatar src={avatar}/>}
                        title={title}
                        description={intro ? intro : ''}
                    />
                </Card>
            </div>
        </>;
    }
}

export default Tree3ChatRoom;