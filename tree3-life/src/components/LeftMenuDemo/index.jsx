import React from 'react';

import {Menu, Item, useContextMenu} from 'react-contexify';
import 'react-contexify/dist/ReactContexify.css';


const LeftMenuDemo = () => {
    const MENU_ID = 'rightMenu';
    const {show} = useContextMenu({
        id: MENU_ID,
    });


    function handleItemClick({triggerEvent, event, props, data}) {
        // retrieve the id of the Item or any other dom attribute
        const id = event.currentTarget.id; // equal to "item-id"

        // access the props and the data
        console.log(id, props, data);

        // access the coordinate of the mouse when the menu has been displayed
        const {clientX, clientY} = triggerEvent;
        console.log(clientX, clientY)
    }


    return (
        <div id="Index">
            右键菜单demo
            <div style={{width: '100%', height: '100vh'}} onContextMenu={show}>
            </div>
            <Menu id={MENU_ID} animation="fade">
                <Item id="item-id1" data={{name: 'value1'}} props={12} onClick={handleItemClick}>这是通过鼠标右键点击展示的1</Item>
                <Item id="item-id2" data={{age: 'value2'}} onClick={handleItemClick}>这是通过鼠标右键点击展示的2</Item>
                <Item id="item-id3" data={{psw: 'value3'}} onClick={handleItemClick}>这是通过鼠标右键点击展示的3</Item>
            </Menu>
        </div>
    );
}

export default LeftMenuDemo;