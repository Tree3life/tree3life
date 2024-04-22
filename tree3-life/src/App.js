import {Route, Switch} from 'react-router-dom'
import './App.less';
import 'antd/dist/antd.css';

import './App.css'
import Login from "@/pages/Login/Login";


function App() {
    return (
        <div className="App">
            执笔画清眸
            <Switch>
                {/*<Route path="/login" component={Login}/>*/}
                {/*<Route path="/admin" component={Dashboard}/>*/}
                {/*<Route path="/page" component={DemoCss}/>*/}
                <Route component={Login}/>
            </Switch>
        </div>
    );
}

export default App;
