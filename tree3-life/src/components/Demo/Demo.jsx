import './Demo.css'

import React, {Component} from 'react';
import {connect} from 'react-redux'
import Card1 from "@/components/TreeUi/Card/card1";
// import {Redirect} from 'react-router-dom'
import {Form, Icon, Input, Button, message} from "antd";
import {tempStorageUserInfo} from "@/store/states/user";
import {application} from "@/services";
import {saveInLocalCache} from "@/store/states/cache";

const {Item} = Form

/**
 * 使用装饰器语法
 */
@connect(state => ({
    user: state.user,
    cache: state.cache
}), {
    tempStorageUserInfo,
    saveInLocalCache
})
@Form.create()
class Login extends Component {
    doTest=event=>{
        let resp=application.logout()
        console.log(resp);
    }

    handleSubmit = event => {
        event.preventDefault();//阻止表单的默认事件
        this.props.form.validateFields(async (err, values) => {
            if (!err) {
                try {
                    let resp = await application.login(values);
                    resp.data.login = true
                    this.props.tempStorageUserInfo(resp.data)//保存用户信息
                    console.log(resp)
                    // this.props.saveInLocalCache({
                    //     login: true,//用户的登录状态
                    //     roles: resp.data.roles,//用户拥有的角色
                    //     pages: resp.data.pages,//用户能够访问的页面
                    //     permissions: resp.data.permissions//用户拥有的权限
                    // })//保存路由信息

                    //切换路由
                    // this.props.history.replace('/admin')

                    message.success('登录成功!', 3)
                    // if (this.props.user.data.login){
                    //     console.log("xxxxxxx")
                    //     return <Redirect to={"/dashboard"}/>
                    // }
                } catch (e) {
                    console.log('Demo.jsx：', e)
                    message.warn(e, 3)
                }
            }
        });
    };

    render() {
        const {getFieldDecorator} = this.props.form;
        return (
            <Card1 className="login">
                <Button type="danger" onClick={this.doTest}>aaaa</Button>

                <Form onSubmit={this.handleSubmit} className="login-form">
                    <Item>
                        {getFieldDecorator('username', {
                            rules: [{
                                required: true,
                                message: '请输入用户名！'
                            }]
                        })
                        (
                            <Input
                                prefix={<Icon type="user" style={{color: 'rgba(0,0,0,.25)'}}/>}
                                placeholder="Username"
                            />,
                        )
                        }
                    </Item>
                    <Item>
                        {getFieldDecorator('password', {
                            rules: [{required: true, message: '请输入密码！'}],
                        })(
                            <Input
                                prefix={<Icon type="lock" style={{color: 'rgba(0,0,0,.25)'}}/>}
                                type="password"
                                placeholder="Password"
                            />,
                        )}
                    </Item>
                    <Item>
                        <Button type="primary" htmlType="submit" className="login-form-button">
                            登录
                        </Button>
                    </Item>
                </Form>
            </Card1>
        );

    }
}

//
export default Login;
