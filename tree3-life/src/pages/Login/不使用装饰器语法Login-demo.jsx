// import React, {Component} from 'react';
// import {connect} from 'react-redux'
// import Card1 from "@/components/TreeUi/Card/card1";
// // import {Redirect} from 'react-router-dom'
// // import {application} from "../../api/index";
// import './Login.css'
// import {Form, Icon, Input, Button, message} from "antd";
// import {tempStorageUserInfo} from "../../store/states/user";
// import {application} from "../../api";
//
// const {Item} = Form
//
// class Login extends Component {
//     handleSubmit = event => {
//         event.preventDefault();//阻止表单的默认事件
//         this.props.form.validateFields(async (err, values) => {
//             if (!err) {
//                 console.log('Received values of form: ', values);
//
//                 let resp = await application.login(values);
//                 //todo 登录成功，将数据存储到redux中
//                 if (resp && resp.code === 2000) {
//                     resp.data.login = true
//                     this.props.tempStorageUserInfo(resp.data)
//                     message.success('登录成功!', 5)
//                     this.props.history.replace('/admin')
//                     // if (this.props.user.data.login){
//                     //     console.log("xxxxxxx")
//                     //     return <Redirect to={"/dashboard"}/>
//                     // }
//                 }
//                 console.log(resp, '返回值');
//
//             }
//         });
//     };
//
//     render() {
//         const {getFieldDecorator} = this.props.form;
//         return (
//             <Card1 className="login">
//                 <Form onSubmit={this.handleSubmit} className="login-form">
//                     <Item>
//                         {getFieldDecorator('username', {
//                             rules: [{
//                                 required: true,
//                                 message: '请输入用户名！'
//                             }]
//                         })
//                         (
//                             <Input
//                                 prefix={<Icon type="user" style={{color: 'rgba(0,0,0,.25)'}}/>}
//                                 placeholder="Username"
//                             />,
//                         )
//                         }
//                     </Item>
//                     <Item>
//                         {getFieldDecorator('password', {
//                             rules: [{required: true, message: '请输入密码！'}],
//                         })(
//                             <Input
//                                 prefix={<Icon type="lock" style={{color: 'rgba(0,0,0,.25)'}}/>}
//                                 type="password"
//                                 placeholder="Password"
//                             />,
//                         )}
//                     </Item>
//                     <Item>
//                         <Button type="primary" htmlType="submit" className="login-form-button">
//                             登录
//                         </Button>
//                     </Item>
//                 </Form>
//             </Card1>
//         );
//
//     }
// }
//
// const LoginForm = Form.create()(Login);
//
// // export default Login;
// export default connect(state => ({
//     user: state.user
// }), {
//     tempStorageUserInfo
// })(LoginForm);
