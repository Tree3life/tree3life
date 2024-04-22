import {Route, Switch} from 'react-router-dom'
import './App.less';
import 'antd/dist/antd.css';

import './App.css'
// import Demo from "@/components/Demo/Demo";
import Login from "@/pages/Login/Login";
// import Dashboard from "./containers/Dashboard";
// import Login from "./containers/Login/Login";
// import DemoCss from "./components/CssPage/demo/DemoCss";

function App() {
    return (
        <div className="App">
            执笔画清眸
            <Switch>
                {/*<Route path="/login" component={Login}/>*/}
                {/*<Route path="/admin" component={Dashboard}/>*/}
                {/*<Route path="/page" component={DemoCss}/>*/}
                <Route component={Login}/>
                {/*<Route component={Demo}/>*/}
            </Switch>
        </div>
    );
}

export default App;
