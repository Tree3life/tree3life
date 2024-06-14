import './Tree3Layout.css'
import React, {Component} from 'react';
import {Avatar, Button, Layout, Modal} from "antd";
import Tree3Header from "./Tree3Header/Tree3Header";
// import Tree3Chat from "@/components/Tree3Chat/Tree3Chat";
import {renderRoutes} from "@/routers/router";
import Tree3ChatRoom from "@/components/Tree3ChatRoom/Tree3ChatRoom";
import {connect} from "react-redux";
import {saveInLocalCache} from "@/store/states/cache";
import beanFactory from "@/resources/factory";
import {commandType} from "@/resources/constant/tree3chat";
import {clientId, serverId} from "@/components/Tree3ChatRoom/tree3chatWebsocket";


@connect(state => ({
    user: state.user,
    cache: state.cache
}), {
    saveInLocalCache
})
class Tree3Layout extends Component {
    constructor(props) {
        super(props);
        this.state = {
            windowWidth: window.innerWidth,
            windowHeight: window.innerHeight,
            chatRoomVisible: false,
            destroyChatRoom: false
        }
    }

    chatRoomModalRef = React.createRef()
    chatRoomRef = React.createRef()


    showModal = () => {
        this.setState({
            chatRoomVisible: true,
        });
    };

    handleLogout = e => {
        const {id: userId} = this.props.user

        if (this.props.cache.websocket && userId && userId !== clientId) {//确认信息有效
            const msg = beanFactory.createMessage(commandType.Logout, userId, serverId);
            // 向服务器发送一条 logout 信息
            this.props.cache.websocket.send(JSON.stringify(msg));
            // message.success('已断开与聊天服务器的连接')
        }
        //声明是主动断开连接的
        this.props.cache.setDoDisconnect(true);
        this.setState({
            //关闭聊天室组件
            chatRoomVisible: false,
            //销毁聊天室组件
            destroyChatRoom: true
        });
    };

    /**
     * 仅关闭聊天室UI，并不销毁聊天室实例
     */
    closeChatRoom = () => {
        this.setState({
            chatRoomVisible: false,
            //销毁聊天室组件
            destroyChatRoom: false
        });
    };

    openShowChatRoom = () => {
        this.setState(
            state => ({
                chatRoomVisible: true,
            }));
    }


    onClose = () => {
        this.setState({
            chatRoomVisible: false,
        });
    };


    handleResize = (e) => {
        this.setState({
            ...this.state, windowWidth: window.innerWidth,
            windowHeight: window.innerHeight
        })
    }

    componentDidMount() {
        //监听窗口大小改变
        window.addEventListener('resize', this.handleResize);
    }

    //移除监听器，防止多个组件之间导致this的指向紊乱
    componentWillUnmount() {
        window.removeEventListener('resize', this.handleResize);
    }

    render() {
        const {Header, Footer, Sider, Content} = Layout;

        let {windowWidth, windowHeight} = this.state;
        const headerStyle = {
            // backgroundColor: "black",
            height: windowHeight * 0.154,
            width: windowWidth,
            marginTop: windowHeight * 0.01,
            paddingLeft: windowWidth * 0.1
        };

        const cache = this.props.cache;
        const {userInfo} = cache;

        return (
            <>
                <Layout id={"container"}>
                    {/*region 聊天室modal*/}
                    <Modal ref={this.chatRoomModalRef}
                           width={this.state.windowWidth * 0.7}
                           maskClosable={true}
                           closable={false}
                           title={
                               <div style={{display: "flex", flexDirection: 'row', justifyContent: 'space-between'}}>
                                   <div>
                                       <Avatar src={userInfo.avatar ? userInfo.avatar : ''} alt={'外星人'}/>
                                       <span>{(userInfo.username ? userInfo.username + '（id=' + userInfo.id + '）的' : '') + "聊天室"}</span>
                                   </div>
                                   <Button key="back" onClick={this.handleLogout}>
                                       退出聊天室
                                   </Button>
                               </div>

                           }
                           visible={this.state.chatRoomVisible}
                           destroyOnClose={this.state.destroyChatRoom}//关闭时销毁内部组件
                           onCancel={this.closeChatRoom}
                           showChatRoomVisible={this.openShowChatRoom}
                           footer={null}
                    >
                        <div style={{
                            width: this.state.windowWidth * 0.676,
                            height: this.state.windowHeight * 0.7,
                            // backgroundColor: "red",
                        }}>
                            {/*<Tree3Chat/>*/}
                            {/*用于异地登录时强制退出*/}
                            <Tree3ChatRoom forceCloseRoom={() => {

                                this.setState({
                                    //关闭聊天室组件
                                    chatRoomVisible: false,
                                    //销毁聊天室组件
                                    destroyChatRoom: true
                                })
                            }}/>
                        </div>
                    </Modal>
                    {/*<Modal ref={currentNode=>{ this.chatRoomNode=currentNode;console.log("nnnnn",this.chatRoomNode);}}*/}
                    {/*endregion*/}


                    <Header id={"container-head"} style={headerStyle}>
                        {/*    头部组件   */}
                        <Tree3Header parentStyle={headerStyle} chatRoomNode={this.chatRoomModalRef}/>
                    </Header>
                    <Layout>

                        <Sider id={"container-sider-left"} theme={"light"} width={windowWidth * 0.24}>
                            {/*中间-左侧*/}
                        </Sider>
                        <Content id={"container-content"}>
                            {/*中间-中间*/}
                            {renderRoutes(this.props.route.routes)}
                        </Content>
                        <Sider id={"container-sider-right"} width={windowWidth * 0.24} theme={"light"}>
                            {/*中间-右侧*/}
                        </Sider>
                    </Layout>
                    <Footer id={"container-footer"}>
                        {/*下面*/}
                        <h1>{windowWidth}</h1>
                        <h1>{windowHeight}</h1>
                    </Footer>
                </Layout>
            </>
        );
    }
}

export default Tree3Layout;