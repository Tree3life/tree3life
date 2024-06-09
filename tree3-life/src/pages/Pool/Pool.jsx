import './Pool.css'
import React, {Component} from 'react';
import Markdown from 'react-markdown'
class Pool extends Component {
    render() {
        const markdown = `aaaaaaaaaaaaaaaaaaaaaaaa`

        return (
            <>
                <div id={this.constructor.name}>
                    <Markdown>{markdown}</Markdown>
                    {this.constructor.name}
                </div>
            </>

        );
    }
}

export default Pool;