import React, {Component} from 'react';
import {connect} from 'react-redux'
import {message} from "antd";

import './Login.css'
import qqSrc from "@/resources/static/asset/QQ.png"
import wcSrc from "@/resources/static/asset/WeChat.png"

import {application} from "@/services";
import {tempStorageUserInfo} from "@/store/states/user";
import {saveInLocalCache} from "@/store/states/cache";


@connect(state => ({
    user: state.user,
    cache: state.cache
}), {
    tempStorageUserInfo,
    saveInLocalCache
})
class Login extends Component {
    state = {
        username: "",
        password: ""
    }

    collectData = async () => {
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;
        //todo check 用户名密码的合法性
        if (!username || !password) {
            message.warn("用户名/密码不能为空")
        }

        if (password.length < 6) {
            message.warn("密码错误")
        }

        let info = {username, password};

        let resp = await application.login(info);
        console.log("xxxxxxxxxxxxxxx", resp)
        if (resp.bizCode === 2000) {
            message.success(resp.message, 3)
            // resp.data.login = true
            // 将消息暂存至redux和sessionStorage中
            // sessionStorage.setItem("$formOptions",{...resp})
            // this.props.tempStorageUserInfo(resp.data)//保存用户信息
            // this.props.saveInLocalCache({
            //     login: true,//用户的登录状态
            //     roles: resp.data.roles,//用户拥有的角色
            //     pages: resp.data.pages,//用户能够访问的页面
            //     permissions: resp.data.permissions//用户拥有的权限
            // })//保存菜单信息
            // message.success('登录成功!', 3)
            // this.props.history.replace('/admin/sys/user')
        }
        console.log('=============================')
        console.log(info);
        console.log(resp)
        console.log('=============================')
    }

    render() {
        return (
            <div className="box">
                <div className="content">
                    <div className="login-wrapper">
                        <h1>登录</h1>
                        <div className="login-form">
                            <div className="username form-item">
                                <span>用户名</span>
                                <input id="username" type="text" name="username" className="input-item"/>
                            </div>
                            <div className="password form-item">
                                <span>密码</span>
                                <input id="password" type="password" name="password" className="input-item"/>
                            </div>
                            <button className="login-btn" onClick={this.collectData}>登 录</button>
                        </div>
                        <div className="divider">
                            <span className="line"></span>
                            <span className="divider-text">其他方式登录</span>
                            <span className="line"></span>
                        </div>
                        <div className="other-login-wrapper">
                            <div className="other-login-item">
                                <img src={qqSrc} alt=""/>
                            </div>
                            <div className="other-login-item">
                                <img src={wcSrc} alt=""/>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Login;
