import React, {Component} from 'react';
import {connect} from 'react-redux'
import {message} from "antd";

import './Login.css'
// import qqSrc from "@/resources/static/asset/QQ.png"
// import wcSrc from "@/resources/static/asset/WeChat.png"

import {auth} from "@/services";
import {saveInLocalCache} from "@/store/states/cache";
import {debounce} from "@/util/throttleAndDebounce";
import settings from "@/resources/application";
import {storageUserInfo} from "@/store/states/user";


@connect(state => ({
    user: state.user,
    cache: state.cache
}), {
    saveInLocalCache,
    storageUserInfo
})
class Login extends Component {
    state = {
        username: "",
        password: ""
    }

    collectData = () => {
        debounce(async () => {
            const username = document.getElementById("username").value;
            const password = document.getElementById("password").value;
            //todo check 用户名密码的合法性
            if (!username || !password) {
                message.warn("用户名/密码不能为空")
                return;
            }

            if (password.length < 6) {
                message.warn("密码错误")
                return;
            }

            let info = {username, password};
            let resp = await auth.login(info);
            const {data: userInfo, message: msg} = resp;

            const {cache} = this.props;
            console.log("cache", cache)
            // 将信息暂存至redux 中

            sessionStorage.setItem(settings.token_key, userInfo.password)//token
            this.props.saveInLocalCache({//异步的更新
                "tree3life_cache": "只能读,不能写xxxxxxx",
                login: true,//用户的登录状态
                token: userInfo.password,
                // to be optimized@Rupert：将userInfo从cache中进行去除 (2024/5/28 18:34)
                userInfo: {...cache.userInfo, ...userInfo},
                // roles: resp.data.roles,//用户拥有的角色
                // pages: resp.data.pages,//用户能够访问的页面
                // permissions: resp.data.permissions//用户拥有的权限
            })//保存菜单信息

            //将用户相关的信息进行保存
            this.props.storageUserInfo( {...userInfo, token: userInfo.password})
            message.success(msg)
            this.props.history.replace('/home/blogs')//router5版本语法
        }, 500);
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
                        {/*todo 实现qq、微信登录*/}
                        {/*<div className="divider">*/}
                        {/*    <span className="line"></span>*/}
                        {/*    <span className="divider-text">其他方式登录</span>*/}
                        {/*    <span className="line"></span>*/}
                        {/*</div>*/}
                        {/*<div className="other-login-wrapper">*/}
                        {/*    <div className="other-login-item">*/}
                        {/*        <img src={qqSrc} alt=""/>*/}
                        {/*    </div>*/}
                        {/*    <div className="other-login-item">*/}
                        {/*        <img src={wcSrc} alt=""/>*/}

                        {/*    </div>*/}
                        {/*</div>*/}
                    </div>
                </div>
            </div>
        );
    }
}

export default Login;
