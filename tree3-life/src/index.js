import React from 'react';
import ReactDOM from 'react-dom'
import {Provider} from 'react-redux'
import {BrowserRouter} from "react-router-dom";

import './index.css';
import store, {persistor} from './store'
import {PersistGate} from 'redux-persist/lib/integration/react';

import App from './App';
// import ErrorBoundary from "./components/ErrorBoundary";

/**
 * react版本npm下载量：
 * 17.0.2  11,938,301
 * 18.2.0  13,592,116
 */
ReactDOM.render(
    /* 此处用Provider包裹App的目的：让App所有的后代容器组件都能接收到store */
    <Provider store={store}>
        {/*<ErrorBoundary>//react的错误处理机制*/}
        {/*redux持久化配置*/}
        <PersistGate loading={null} persistor={persistor}>
            <BrowserRouter>
                <App/>
            </BrowserRouter>
        </PersistGate>
        {/*</ErrorBoundary>*/}
    </Provider>,
    document.getElementById('root')
)
