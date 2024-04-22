// import React, {Component} from 'react';
// import {connect} from 'react-redux'
// import {Button, Form, Input} from 'antd';
// import Card1 from "@/components/TreeUi/Card/card1";
// // import {Redirect} from 'react-router-dom'
// import {application} from "../../api/index";
// import './Login.css'
// import {Form, Icon, Input, Button, Checkbox, message} from "antd";
// import {tempStorageUserInfo} from "../../store/states/user";
//
// class Login extends Component {
//     render() {
//         //
//         // const inputStyle={ border: "none",background:"#e8e8e8",padding:"1rem",borderRadius:"1rem",boxShadow:'20px 20px 60px #c5c5c5,\n' +
//         //         '      -20px -20px 60px #ffffff',transaction:"0.3s"}
//         // const inputFocus={outlineColor:"#e8e8e8",background:"#e8e8e8",boxShadow: "inset 20px 20px 60px #c5c5c5,\n" +
//         //         "      inset -20px -20px 60px #ffffff",transaction: "0.3s"}
//
//         const onFinish = async (form) => {
//             try {
//                 let resp = await application.login(form);
//                 resp.data.login = true
//                 this.props.tempStorageUserInfo(resp.data)
//                 message.success('登录成功!', 5)
//                 this.props.history.replace('/admin')
//                 // if (this.props.user.data.login){
//                 //     console.log("xxxxxxx")
//                 //     return <Redirect to={"/dashboard"}/>
//                 // }
//                 console.log(resp, '返回值');
//
//                 console.log('ttttttttt', this.props)
//             } catch (e) {
//                 message.warn(e, 5)
//             }
//         };
//
//         const onFinishFailed = (errorInfo) => {
//             console.log(errorInfo);
//             message.warn("数据不合法！", 5)
//         };
//         return (
//             <Card1 className="login">
//                 <Form name="basic" labelCol={{span: 4, offset: 1}} wrapperCol={{span: 16}}
//                       initialValues={{remember: true,}}
//                       onFinish={onFinish} onFinishFailed={onFinishFailed} autoComplete="off"
//                 >
//                     <Form.Item label="用户名" name="username" rules={[{required: true, message: '请输入用户名!',},]}>
//                         <Input/>
//                     </Form.Item>
//
//                     <Form.Item label="密     码" name="password" rules={[{required: true, message: '请输入密码!',},]}>
//                         <Input.Password/>
//                     </Form.Item>
//
//
//                     <Form.Item wrapperCol={{offset: 8, span: 16,}}>
//                         <Button type="primary" shape="round" size={"large"} htmlType="submit">登录</Button>
//                     </Form.Item>
//                     {JSON.stringify(this.props.user)}
//                 </Form>
//             </Card1>
//         );
//     }
// }
//
// // export default Login;
// export default connect(state => ({
//     user: state.user
// }), {
//     tempStorageUserInfo
// })(Login);
