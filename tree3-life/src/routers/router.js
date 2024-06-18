//todo 将子级  routes 修改为 children
import React, {Suspense, lazy} from 'react'
import {Route, Switch, Redirect} from 'react-router-dom'
import {matchPath, Router} from "react-router"

// exact属性为true时路径中的hash值必须和path完全一致才渲染对应的组件，如果为false则'/'也可以匹配'/login';
// （即如果strict属性为false，则末尾是否包含反斜杠结尾不影响匹配结果）
// strict属性主要就是匹配反斜杠，规定是否匹配末尾包含反斜杠的路径，如果strict为true，则如果path中不包含反斜杠结尾，
// 则它也不能匹配包含反斜杠结尾的路径，这个需要和exact结合使用

/**
 * todo 优化 将routes 从数组形式 改写为 对象形式(数组类型的取用的时候不方便,不方便方便编程式路由的维护使用)
 * 推荐：this.props.history.push(pageConfig.login.path)
 * 避免： this.props.history.push('/login')
 * @type {{}}
 */
// const pageConfig = {
//     //路径：该路径的配置对象
//     pool: {path: "/pool", component: lazy(() => import('@/pages/Pool/Pool'))},
//     login: {path: '/login', component: lazy(() => import('@/pages/Login/Login'))},
//     home: {
//         path: '/home',
//         children: {
//             blogs: {
//                 // path: '/blog'      `/`:代表根路径，添加时表示从根路径开始匹配，不添加表示 向当前路径后面追加以下path
//                 path: '/home/blogs',
//                 component: lazy(() => import('../pages/Tree3Layout/Blog/Blog')),
//                 meta: {title: '测试titlie'},
//             },
//             login: {
//                 path: '/home/login',
//                 name: 'TeacherTextbook',
//                 component: lazy(() => import('../pages/Login/Login')),
//                 meta: {title: '选课'},
//             },
//             load: {
//                 path: '/home/load',
//                 name: 'loading',
//                 component: lazy(() => import('../components/Loading/Loading')),
//                 meta: {title: 'testLoading'},
//             },
//         }
//     },
// };
//1、通过React的lazy函数配合import()函数动态加载路由组件 ===》 路由组件代码会被分开打包
// 使用React自带的 Suspense,lazy实现懒加载

const routes = [
    {path: "/pool", component: lazy(() => import('@/pages/Pool/Pool'))},
    {path: '/login', component: lazy(() => import('@/pages/Login/Login'))},
    {
        path: '/home', component: lazy(() => import('@/pages/Tree3Layout/Tree3Layout')),
        routes: [
            {
                // path: '/blog'      `/`:代表根路径，添加时表示从根路径开始匹配，不添加表示 向当前路径后面追加以下path
                path: '/home/blogs',
                component: lazy(() => import('../pages/Tree3Layout/Blog/Blog')),
                meta: {title: '测试titlie'},
            },
            {
                path: '/home/login',
                name: 'TeacherTextbook',
                component: lazy(() => import('../pages/Login/Login')),
                meta: {title: '选课'},
            },
            {
                path: '/home/load',
                name: 'loading',
                component: lazy(() => import('../components/Loading/Loading')),
                meta: {title: 'testLoading'},
            },
            {
                path: '/home/pay',
                name: '聚合支付',
                component: lazy(() => import('../components/LeftMenuDemo')),
                meta: {title: 'testPay'},
            },
        ]
    },
    //else
    {path: '/', render: () => <Redirect to={"/home/blogs"}/>},

    // {path: "/", exact: true, render: () => <Redirect to={"/login/password"}/>},
    // {path: "/teacher", exact: true, render: () => <Redirect to={"/teacher/textbook"}/>},
    // {
    //     path: '/teacher',
    //     name: 'Teacher',
    //     component: lazy(() => import('pages/layout/layout.jsx')),
    //     meta: {title: '教师端'},
    //     routes: [
    //         {
    //             path: '/teacher/textbook',
    //             name: 'TeacherTextbook',
    //             component: lazy(() => import('pages/teacher/textbook/textbook.jsx')),
    //             meta: {title: '选课'},
    //             routes: [
    //                 {
    //                     path: '/teacher/textbook/index',
    //                     name: 'TeacherTextbookIndex',
    //                     component: lazy(() => import('pages/test.jsx')),
    //                     meta: {title: '测试啊'},
    //                 }
    //             ]
    //         },
    //         {
    //             path: '/teacher/in-class/:id/:name',
    //             name: 'TeacherInclass',
    //             component: lazy(() => import('pages/teacher/in-class/in-class.jsx')),
    //             meta: {title: '上课'}
    //         }
    //     ]
    // }
]


//todo 重写 以实现 路由守卫 const permissions = 'user'//登录接口获取的当前用户的角色 ；const requiresAuth = true //是否已经登录
// 实现react-router-config里的renderRoutes方法
function renderRoutes(routes, extraProps = {}, switchProps = {}) {
    return routes ? (
        <Suspense fallback={
            //加载页。。。
            <div>拼命加载中。。。</div>
        }>
            <Switch {...switchProps}>
                {routes.map((route, i) => (
                    <Route
                        key={route.key || i}
                        path={route.path}
                        exact={route.exact}
                        strict={route.strict}
                        render={props =>
                            route.render ? (
                                route.render({...props, ...extraProps, route: route})
                            ) : (
                                <route.component {...props} {...extraProps} route={route}/>
                            )
                        }
                    />
                ))}
            </Switch>
        </Suspense>
    ) : null;
}

// 实现react-router-config里的matchRoutes方法
function matchRoutes(routes, pathname, /*not public API*/ branch = []) {
    routes.some(route => {
        const match = route.path
            ? matchPath(pathname, route)
            : branch.length
                ? branch[branch.length - 1].match // use parent match
                : Router.computeRootMatch(pathname); // use default "root" match

        if (match) {
            branch.push({route, match});

            if (route.routes) {
                matchRoutes(route.routes, pathname, branch);
            }
        }
        return match;
    });
    return branch;
}

export {routes, renderRoutes, matchRoutes}
