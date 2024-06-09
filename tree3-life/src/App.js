import React, {PureComponent} from 'react'
import './App.less';
import 'antd/dist/antd.css';
import './App.css'

import {renderRoutes, routes} from "@/routers/router";

//PureComponent（重写了shouldComponentUpdate()——仅进行 浅层次的 比较）：仅重新渲染 state 发送了改变的 组件。
class App extends PureComponent {

    render() {
        return (
            <div className="App">
                {/* 这个方法，每次有子路由时都需要使用，会传当前路由的子路由，可以说是按需加载，
       实时编译的，而不是一次性吧所有路由都渲染出来 */}
                {/*其他页面调用时：{renderRoutes(this.props.route.routes)}*/}
                {renderRoutes(routes)}
            </div>
        );
    }
}

export default App;