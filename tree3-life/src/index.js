import React from 'react';
import ReactDOM from 'react-dom'
import {Provider} from 'react-redux'
import {Router} from 'react-router-dom'
import history from "@/util/history";

import './index.css';
import store, {persistor} from './store'
import {PersistGate} from 'redux-persist/lib/integration/react';

import App from './App';
// import ErrorBoundary from "./components/ErrorBoundary";


ReactDOM.render(
    /* 此处用Provider包裹App的目的：让App所有的后代容器组件都能接收到store */
    <Provider store={store}>
        {/*<ErrorBoundary>//react的错误处理机制*/}
        {/*redux持久化配置*/}
        <PersistGate loading={null} persistor={persistor}>
            <Router history={history}>
                <App/>
            </Router>
        </PersistGate>
        {/*</ErrorBoundary>*/}
    </Provider>,
    document.getElementById('root')
)
