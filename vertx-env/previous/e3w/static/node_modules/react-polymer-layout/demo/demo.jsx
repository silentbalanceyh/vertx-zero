import React from "react";
import {Dialog} from "../index.jsx";

const Button = React.createClass({
    handleClick() {
        this.refs.dialog.show();
    },

    render() {
        return (
            <div>
                <button onClick={this.handleClick}>Dialog</button>
                <Dialog ref="dialog" style={{textAlign: "center"}}>Hello, world</Dialog>
            </div>
        );
    }
});

React.render(<Button />, document.body);
