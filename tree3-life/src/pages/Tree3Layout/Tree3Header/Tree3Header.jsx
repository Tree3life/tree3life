import './Tree3Header.css'
import React, {Component} from 'react';
import {withRouter} from 'react-router-dom'

import {Menu, Icon, Row, Col, Button, Avatar} from 'antd';
import {Input} from 'antd';

// const {SubMenu} = Menu;
const {Search} = Input;

@withRouter
class Tree3Header extends Component {
    state = {
        current: 'chatroomMenu',
    };


    handleClick = e => {
        this.setState({
            current: e.key,
        });
        switch (e.key) {
            case 'chatroomMenu'://打开聊天室
                //拆包
                const {chatRoomNode} = this.props
                chatRoomNode.current.props.showChatRoomVisible();
                return;
            default  :
                console.log('default........')
                return;
        }


    };

    render() {
        const {parentStyle} = this.props
        const menuStyle = {
            // backgroundColor: "red",
            height: parentStyle.height * 0.4,
            width: parentStyle.width * 0.8,
        };
        const searchDivStyle = {
            paddingTop: parentStyle.height * 0.5 * 0.15,
            height: parentStyle.height * 0.57,
            width: parentStyle.width * 0.8
        };
        return (
            <>
                {/* redux 中监听属性 窗口的宽度、高度*/}
                {/*region 导航栏*/}
                <Menu id={"header-menu"} onClick={this.handleClick} style={menuStyle}
                      selectedKeys={[this.state.current]}
                      mode="horizontal">

                    <Menu.Item disabled={true} key="tree3-head-logo">
                        <Avatar src={"http://md.tree3.life/favor/favicon.svg"}  size="large" icon="user" />
                    </Menu.Item>

                    <Menu.Item key="mail">
                        <Icon type="mail"/>
                        博客检索
                    </Menu.Item>
                    <Menu.Item key="chatroomMenu">
                        <Icon type="usergroup-add"/>
                        聊天室
                    </Menu.Item>
                    {/*<Menu.Item key="app" disabled>*/}
                    {/*    <Icon type="appstore"/>*/}
                    {/*    Navigation Two*/}
                    {/*</Menu.Item>*/}

                    {/*<SubMenu*/}
                    {/*    title={*/}
                    {/*        <span className="submenu-title-wrapper"> <Icon type="setting"/>*/}
                    {/*        Navigation Three - Submenu*/}
                    {/*    </span>*/}
                    {/*    }>*/}
                    {/*    <Menu.ItemGroup title="Item 1">*/}
                    {/*        <Menu.Item key="setting:1">Option 1</Menu.Item>*/}
                    {/*        <Menu.Item key="setting:2">Option 2</Menu.Item>*/}
                    {/*    </Menu.ItemGroup>*/}
                    {/*    <Menu.ItemGroup title="Item 2">*/}
                    {/*        <Menu.Item key="setting:3">Option 3</Menu.Item>*/}
                    {/*        <Menu.Item key="setting:4">Option 4</Menu.Item>*/}
                    {/*    </Menu.ItemGroup>*/}
                    {/*</SubMenu>*/}

                    <Menu.Item key="alipay">
                        <a href="https://ant.design" target="_blank" rel="noopener noreferrer">
                            聚合支付
                        </a>
                    </Menu.Item>
                </Menu>
                {/*endregion 导航栏*/}

                {/*region 搜索框*/}
                <div style={searchDivStyle}>
                    <Row type="flex" justify="space-around" align="top">
                        <Col span={3} style={{backgroundColor: "white"}}>
                            <h2>Tree 3 Life</h2>
                        </Col>
                        <Col span={18}>
                            <Search
                                style={{paddingTop: parentStyle.height * 0.5 * 0.15}}
                                placeholder="input search text"
                                enterButton="Search"
                                size="large"
                                onSearch={value => console.log(value)}
                            />
                        </Col>
                        <Col span={2}>
                            {/*<NavLink*/}
                            {/*    to="login"*/}
                            {/*    className={({ isActive }) => {*/}
                            {/*        return isActive ? 'base one' : 'base'*/}
                            {/*    }}*/}
                            {/*>登录</NavLink>*/}
                            <Button block={true} size={"large"} onClick={() => {
                                this.props.history.push("/login")
                            }}>
                                登录
                            </Button>
                        </Col>
                        <Col span={1}>
                            <Button type="link" block size={"large"}>
                                注册
                            </Button></Col>
                    </Row>
                </div>

                {/*endregion 搜索框*/}


            </>
        );
    }
}

export default Tree3Header;