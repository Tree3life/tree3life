import './demo.css'

import React, {Component} from 'react';

class DemoCss extends Component {
    constructor(props) {
        super(props);
        this.state = {
            content:
                "===" +
                "CSS3中的伪类\n" +
                ":root 选择文档的根元素，等同于 html 元素\n" +
                ":empty 选择没有子元素的元素\n" +
                ":target 选取当前活动的目标元素\n" +
                ":not(selector) 选择除 selector 元素意外的元素\n" +
                ":enabled 选择可用的表单元素\n" +
                ":disabled 选择禁用的表单元素\n" +
                ":checked 选择被选中的表单元素\n" +
                ":nth-child(n) 匹配父元素下指定子元素，在所有子元素中排序第n\n" +
                ":nth-last-child(n) 匹配父元素下指定子元素，在所有子元素中排序第n，从后向前数\n" +
                ":nth-child(odd) 、 :nth-child(even) 、 :nth-child(3n+1)\n" +
                ":first-child 、 :last-child 、 :only-child\n" +
                ":nth-of-type(n) 匹配父元素下指定子元素，在同类子元素中排序第n\n" +
                ":nth-last-of-type(n) 匹配父元素下指定子元素，在同类子元素中排序第n，从后向前数\n" +
                ":nth-of-type(odd) 、 :nth-of-type(even) 、 :nth-of-type(3n+1)\n" +
                ":first-of-type 、 :last-of-type 、 :only-of-type\n" +
                "===" +
                "CSS3中的伪元素\n" +
                "::after 已选中元素的最后一个子元素\n" +
                "::before 已选中元素的第一个子元素\n" +
                "::first-letter 选中某个款级元素的第一行的第一个字母\n" +
                "::first-line 匹配某个块级元素的第一行\n" +
                "::selection 匹配用户划词时的高亮部分"
        }
    }

    render() {
        const {content} = this.state
        let contentItem = content.split('\n').map(
            item => {
                if (item.indexOf('===') === 0) {
                    item = item.substr(3, item.length);
                }
                return <li key={item}>{item}</li>
            }
        )

        return (


            <div style={{backgroundColor: '#986543'}}>
                <div className={'radio-class'}>

                </div>

                <div className="input-ele">
                    姓名：<input />
                    男： <input type={"radio"}/>
                    <input type={"range"}/>
                    女： <input type={"radio"}/>
                    1： <input type={"radio"}/>
                    2： <input type={"radio"}/>
                    3： <input type={"radio"}/>
                </div>
                <div className="back_ground">
                    <ul className="test_ul">
                        {contentItem}
                    </ul>
                </div>
            </div>

        );
    }
}

export default DemoCss;
