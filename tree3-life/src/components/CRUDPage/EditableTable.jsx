import '@/containers/System/User/index.less'
import {Button, Col, Form, message, Modal, Popconfirm, Row, Table} from "antd";
import React from "react";


/**
 * 重构前的CRUDPage
 * todo 使用context&组合(children属性)重构此组件-->CRUDPageAttr
 *
 * 一个Context最上层的组件 是最小的复用单元；
 * Context内部的组件不能在Context外部复用；
 * 目前接收的参数说明
 queryForm={{
                    layout: 条件查询表单的布局
                    queryApi: 条件查询的Api
                }}
 tableInfo={{
                    tableColumns: 表格的列配置
                    saveFormLayout: 新增表单的布局配置
                    editRowConfig: 编辑时的行表单布局配置
                    saveApi: 保存接口
                    updateApi: 编辑
                    deleteApi: 删除接口
                    queryApi: 查询接口
                }}
 *
 * @type {ConnectedComponentClass<{contextType?: React.Context<any> | undefined, new<P, S>(props: (Readonly<P> | P)): {state: {confirmDirty: boolean}, render: {(): *, (): React.ReactNode}, componentDidMount?(): void, shouldComponentUpdate?(nextProps: Readonly<{}>, nextState: Readonly<{}>, nextContext: any): boolean, componentWillUnmount?(): void, componentDidCatch?(error: Error, errorInfo: React.ErrorInfo): void, getSnapshotBeforeUpdate?(prevProps: Readonly<{}>, prevState: Readonly<{}>): (any | null), componentDidUpdate?(prevProps: Readonly<{}>, prevState: Readonly<{}>, snapshot?: any): void, componentWillMount?(): void, UNSAFE_componentWillMount?(): void, componentWillReceiveProps?(nextProps: Readonly<{}>, nextContext: any): void, UNSAFE_componentWillReceiveProps?(nextProps: Readonly<{}>, nextContext: any): void, componentWillUpdate?(nextProps: Readonly<{}>, nextState: Readonly<{}>, nextContext: any): void, UNSAFE_componentWillUpdate?(nextProps: Readonly<{}>, nextState: Readonly<{}>, nextContext: any): void, context: unknown, setState<K extends keyof S>(state: (((prevState: Readonly<S>, props: Readonly<P>) => (Pick<S, K> | S | null)) | Pick<S, K> | S | null), callback?: () => void): void, forceUpdate(callback?: () => void): void, readonly props: Readonly<P>, refs: {[p: string]: React.ReactInstance}}, new<P, S>(props: P, context: any): {state: {confirmDirty: boolean}, render: {(): *, (): React.ReactNode}, componentDidMount?(): void, shouldComponentUpdate?(nextProps: Readonly<{}>, nextState: Readonly<{}>, nextContext: any): boolean, componentWillUnmount?(): void, componentDidCatch?(error: Error, errorInfo: React.ErrorInfo): void, getSnapshotBeforeUpdate?(prevProps: Readonly<{}>, prevState: Readonly<{}>): (any | null), componentDidUpdate?(prevProps: Readonly<{}>, prevState: Readonly<{}>, snapshot?: any): void, componentWillMount?(): void, UNSAFE_componentWillMount?(): void, componentWillReceiveProps?(nextProps: Readonly<{}>, nextContext: any): void, UNSAFE_componentWillReceiveProps?(nextProps: Readonly<{}>, nextContext: any): void, componentWillUpdate?(nextProps: Readonly<{}>, nextState: Readonly<{}>, nextContext: any): void, UNSAFE_componentWillUpdate?(nextProps: Readonly<{}>, nextState: Readonly<{}>, nextContext: any): void, context: unknown, setState<K extends keyof S>(state: (((prevState: Readonly<S>, props: Readonly<P>) => (Pick<S, K> | S | null)) | Pick<S, K> | S | null), callback?: () => void): void, forceUpdate(callback?: () => void): void, readonly props: Readonly<P>, refs: {[p: string]: React.ReactInstance}}, prototype: {state: {confirmDirty: boolean}, render: {(): *, (): React.ReactNode}, componentDidMount?(): void, shouldComponentUpdate?(nextProps: Readonly<{}>, nextState: Readonly<{}>, nextContext: any): boolean, componentWillUnmount?(): void, componentDidCatch?(error: Error, errorInfo: React.ErrorInfo): void, getSnapshotBeforeUpdate?(prevProps: Readonly<{}>, prevState: Readonly<{}>): (any | null), componentDidUpdate?(prevProps: Readonly<{}>, prevState: Readonly<{}>, snapshot?: any): void, componentWillMount?(): void, UNSAFE_componentWillMount?(): void, componentWillReceiveProps?(nextProps: Readonly<{}>, nextContext: any): void, UNSAFE_componentWillReceiveProps?(nextProps: Readonly<{}>, nextContext: any): void, componentWillUpdate?(nextProps: Readonly<{}>, nextState: Readonly<{}>, nextContext: any): void, UNSAFE_componentWillUpdate?(nextProps: Readonly<{}>, nextState: Readonly<{}>, nextContext: any): void, context: unknown, setState<K extends keyof S>(state: (((prevState: Readonly<S>, props: Readonly<P>) => (Pick<S, K> | S | null)) | Pick<S, K> | S | null), callback?: () => void): void, forceUpdate(callback?: () => void): void, readonly props: Readonly<P>, refs: {[p: string]: React.ReactInstance}}}, Omit<FormComponentProps<any>, keyof WrappedFormInternalProps>>}
 */
//region 注册用户的弹框
const UserInfoForm = Form.create({name: 'form_in_modal'})(
    class extends React.Component {
        state = {
            confirmDirty: false,
        };

        render() {
            console.log('=}}}}}}}}}}}}}}', this.props.tableInfo)
            const {tableInfo: {saveFormLayout}} = this.props
            const {visible, onCancel, onCreate, form} = this.props;
            return (
                <Modal title="用户信息" visible={visible} onCancel={onCancel} onOk={onCreate} okText="确认" cancelText="取消">
                    <Form layout="vertical">
                        {/*todo 1使用getFileds类似方式做活表格样式 */}
                        {saveFormLayout ? saveFormLayout(form) : ''}
                    </Form>
                </Modal>
            );
        }
    },
);

//endregion 注册用户的弹框

//region 查询表单
@Form.create()
class QueryForm extends React.Component {

    handleSearch = e => {
        const {qyeryForm: {queryApi}} = this.props
        e.preventDefault();
        this.props.form.validateFields(async (err, values) => {
            try {
                let resp = await queryApi(values);
                this.props.updateTableData(resp.data.map(ele => {
                    return {key: ele.id, ...ele}
                }))
            } catch (error) {
                message.warn(error, 3)
            }
        });
    };

    render() {
        const {getFieldDecorator} = this.props.form;
        const {qyeryForm: {layout}} = this.props
        return <Form className="ant-advanced-search-form" onSubmit={this.handleSearch}>
            {/*todo 2 使用getFileds类似的方式做活表格，或尝试是否可以通过继承实现*/}
            <Row gutter={24}>
                {layout ? layout(getFieldDecorator) : ''}
                <Col span={5} style={{textAlign: 'left', paddingTop: '43px'}}>
                    <Button type="primary" htmlType="submit">查询</Button>
                    <Button style={{marginLeft: 8}} onClick={() => {
                        this.props.form.resetFields()
                    }}>重置</Button>
                </Col> </Row>
        </Form>
    }
}

//endregion 查询表单


//region 表单 行可编辑
@Form.create()
class EditableTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            editingKey: '',
            createFormVisible: false,
        };
        const {tableInfo: {tableColumns}} = this.props

        this.columns = [];
        //加载自定义配置的列
        tableColumns(this.columns)

        //配置操作列
        this.columns.push({
            title: '操作',
            render: (text, record) => {
                const {editingKey} = this.state;
                const editable = record.key === this.state.editingKey;
                return editable ? (
                    <span>
                            <EditableContext.Consumer>{({propsForm}) => (
                                <span onClick={() => this.update(propsForm, record.key)}
                                      style={{marginRight: 8, color: 'skyblue'}}>
                                    保存
                                </span>
                            )}
                            </EditableContext.Consumer>
                            <Popconfirm title="不保存?" okText={'是'} cancelText={'否'}
                                        onConfirm={() => this.cancel(record.key)}>
                              <span style={{color: 'skyblue'}}>取消</span>
                            </Popconfirm>
                        </span>
                ) : (
                    <>
                            <span style={{color: 'skyblue'}} disabled={editingKey !== ''}
                                  onClick={() => this.edit(record.key)}>
                            编辑
                            </span>
                        &nbsp;&nbsp;
                        <Popconfirm title="真的要删除吗?" okText={'是'} cancelText={'否'}
                                    onConfirm={() => this.delete(record.id, record)}>
                            <span style={{color: 'skyblue'}}>删除</span>
                        </Popconfirm>
                    </>
                );
            },
        },)
    }

    componentDidMount() {
        this.initUsers()
    }


    cancel = () => {
        this.setState({editingKey: ''});
    };


    initUsers = async () => {
        const {tableInfo: {queryApi}} = this.props
        try {
            let resp = await queryApi()
            let mapData = resp.data.map(ele => {
                return {key: ele.id, ...ele}
            })
            this.setState({"data": mapData});
        } catch (error) {
            message.warn("请求失败：" + error.data.errmsg, 3)
        }
    }


    add = () => {
        const {tableInfo: {saveApi}} = this.props
        const {form} = this.formRef.props;
        form.validateFields(async (err, values) => {
            if (err) {
                return;
            }
            try {
                let resp = await saveApi(values)
                this.state.data.unshift({key: resp.data.id, ...resp.data})
                message.warn('添加成功', 1)
                form.resetFields();
                this.setState({createFormVisible: false});
            } catch (error) {
                message.warn('添加失败：' + error, 3)
            }
        });
    }

    //删除
    delete = async (id, index) => {
        const {tableInfo: {deleteApi}} = this.props
        try {
            let resp = await deleteApi({id: id})
            this.setState({'data': this.state.data.filter(ele => ele.key !== id)})
            message.success("删除成功", 1)
        } catch (error) {
            message.warn("删除失败：" + error, 3)
        }

    }

    update(form, key) {
        const {tableInfo: {updateApi}} = this.props

        form.validateFields(async (error, row) => {
            if (error) {
                return;
            }
            const newData = [...this.state.data];
            const index = newData.findIndex(item => key === item.key);
            if (index > -1) {
                const item = newData[index];
                let paramObj = {...item, ...row}//构造要保存的最新对象
                try {
                    let resp = await updateApi(paramObj)
                    newData.splice(index, 1, paramObj);
                    this.setState({data: newData, editingKey: ''});
                    message.success("编辑成功", 1)
                } catch (error) {
                    message.warn("编辑失败：" + error, 3)
                }

            } else {
                newData.push(row);
                this.setState({data: newData, editingKey: ''});
            }
        });
    }

    edit(key) {
        this.setState({editingKey: key});
    }

    updateTableData = (data) => {
        this.setState({data: data})
    }

    render() {

        const components = {
            body: {
                // wrapper: BodyWrapper,
                cell: EditableCell,
            },
        };


        //todo 再次优化表格的列
        const columns = this.columns.map(col => {
            if (!col.editable) {
                return col;
            }
            return {
                ...col,
                onCell: record => ({
                    record,
                    // inputType: col.dataIndex === 'age' ? 'number' : 'text',
                    inputType: 'text',
                    dataIndex: col.dataIndex,
                    title: col.title,
                    editing: record.key === this.state.editingKey,
                }),
            };
        });

        return (
            <EditableContext.Provider value={{propsForm: this.props.form, tableInfo: this.props.tableInfo}}>
                {console.log('EditableContext.Provider====================>', this.props.form)}
                {/*条件查询*/}
                <QueryForm qyeryForm={this.props.queryForm} updateTableData={this.updateTableData}/>
                <Table
                    components={components}
                    bordered
                    title={() => {
                        return <div>
                            <Button type="primary" onClick={() => {
                                this.setState({createFormVisible: true});
                            }}>
                                添加
                            </Button>
                            <UserInfoForm
                                tableInfo={this.props.tableInfo}
                                wrappedComponentRef={formRef => {
                                    this.formRef = formRef;
                                }}
                                visible={this.state.createFormVisible}
                                onCancel={() => {
                                    this.setState({createFormVisible: false});
                                }}
                                onCreate={this.add}
                            />
                        </div>
                    }}
                    dataSource={this.state.data}
                    columns={columns}
                    rowClassName="editable-row"
                    pagination={{
                        onChange: this.cancel,
                    }}
                />
            </EditableContext.Provider>
        );
    }
}

//endregion 表单 行可编辑


//region可编辑表格Cell
const EditableContext = React.createContext();

class EditableCell extends React.Component {
    componentDidMount() {
    }

    getInput = (cellProps, {editRowConfig}) => {
        return editRowConfig(this.props)
    };

    renderCell = ({propsForm, tableInfo}) => {
        const {getFieldDecorator} = propsForm

        const {
            editing, dataIndex, title, inputType, record, index, children, ...restProps
        } = this.props;

        return (
            <td {...restProps}>
                {editing ? (
                    <Form.Item style={{margin: 0}}>
                        {getFieldDecorator(dataIndex, {
                            rules: [
                                {
                                    required: ((dataIndex === 'username') || (dataIndex === 'name') || (dataIndex === 'locked')),
                                    message: `请输入 ${title}!`,
                                },
                            ],
                            initialValue: record[dataIndex],
                        })(this.getInput(this.props, tableInfo))}
                    </Form.Item>
                ) : (
                    children
                )}
            </td>
        );
    };

    render() {
        return <EditableContext.Consumer>{this.renderCell}</EditableContext.Consumer>;
    }
}

//endregion可编辑表格Cell


export default EditableTable