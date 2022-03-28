"use strict";

Object.defineProperty(exports, "__esModule", {
    value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

var _react = require("react");

var _react2 = _interopRequireDefault(_react);

var useWebkit = "WebkitAppearance" in document.documentElement.style && !window.chrome;

function assign(target, source) {
    for (var k in source) {
        target[k] = source[k];
    }
}

var Item = _react2["default"].createClass({
    displayName: "Item",

    // See Polymer layout attributes
    propTypes: {
        flex: _react2["default"].PropTypes.oneOfType([_react2["default"].PropTypes.bool, _react2["default"].PropTypes.string]),
        layout: _react2["default"].PropTypes.bool,
        wrap: _react2["default"].PropTypes.bool,
        reverse: _react2["default"].PropTypes.bool,
        horizontal: _react2["default"].PropTypes.bool,
        vertical: _react2["default"].PropTypes.bool,
        center: _react2["default"].PropTypes.bool,
        start: _react2["default"].PropTypes.bool,
        end: _react2["default"].PropTypes.bool,
        stretch: _react2["default"].PropTypes.bool,
        startJustified: _react2["default"].PropTypes.bool,
        centerJustified: _react2["default"].PropTypes.bool,
        endJustified: _react2["default"].PropTypes.bool,
        justified: _react2["default"].PropTypes.bool,
        aroundJustified: _react2["default"].PropTypes.bool,
        selfStart: _react2["default"].PropTypes.bool,
        selfCenter: _react2["default"].PropTypes.bool,
        selfEnd: _react2["default"].PropTypes.bool,
        selfStretch: _react2["default"].PropTypes.bool,
        relative: _react2["default"].PropTypes.bool,
        fit: _react2["default"].PropTypes.bool,
        hidden: _react2["default"].PropTypes.bool
    },

    render: function render() {
        var props = this.props;
        var style = props.layout ? { display: useWebkit ? "-webkit-box" : "flex" } : {};
        // flex
        if (typeof props.flex === "string") {
            style.flex = style.WebkitBoxFlex = props.flex;
        } else if (props.flex) {
            style.flex = style.WebkitBoxFlex = 1;
        }
        // flex-wrap
        if (props.wrap) {
            style.flexWrap = style.WebkitFlexWrap = "wrap";
        }
        // flex-direction
        if (props.vertical) {
            style.flexDirection = style.WebkitFlexDirection = props.reverse ? "column-reverse" : "column";
            style.WebkitBoxOrient = "vertical";
        } else {
            style.flexDirection = style.WebkitFlexDirection = props.reverse ? "row-reverse" : "row";
            style.WebkitBoxOrient = "horizontal";
        }
        // align-items
        if (props.center) {
            style.alignItems = style.WebkitBoxAlign = "center";
        } else if (props.start) {
            style.alignItems = "flex-start";
            style.WebkitBoxAlign = "start";
        } else if (props.end) {
            style.alignItems = "flex-end";
            style.WebkitBoxAlign = "end";
        } else if (props.stretch) {
            style.alignItems = style.WebkitBoxAlign = "stretch";
        }
        // justify-content
        if (props.startJustified) {
            style.justifyContent = "flex-start";
            style.WebkitBoxPack = "start";
        } else if (props.centerJustified) {
            style.justifyContent = style.WebkitBoxPack = "center";
        } else if (props.endJustified) {
            style.justifyContent = "flex-end";
            style.WebkitBoxPack = "end";
        } else if (props.justified) {
            style.justifyContent = "space-between";
        } else if (props.aroundJustified) {
            style.justifyContent = "space-around";
        }
        // align-self
        if (props.selfStart) {
            style.alignSelf = style.WebkitAlignSelf = "flex-start";
        } else if (props.selfCenter) {
            style.alignSelf = style.WebkitAlignSelf = "center";
        } else if (props.selfEnd) {
            style.alignSelf = style.WebkitAlignSelf = "flex-end";
        } else if (props.selfStretch) {
            style.alignSelf = style.WebkitAlignSelf = "stretch";
        }
        // other
        if (props.relative) {
            style.position = "relative";
        } else if (props.fit) {
            style.position = "absolute";
            style.top = style.bottom = style.left = style.right = 0;
        }
        if (props.hidden) {
            style.display = "none";
        }

        assign(style, props.style);
        return _react2["default"].createElement(
            "div",
            _extends({}, props, { style: style }),
            props.children
        );
    }
});

var Layout = _react2["default"].createClass({
    displayName: "Layout",

    render: function render() {
        return _react2["default"].createElement(
            Item,
            _extends({ layout: true }, this.props),
            this.props.children
        );
    }
});

var Dialog = _react2["default"].createClass({
    displayName: "Dialog",

    propTypes: {
        style: _react2["default"].PropTypes.object,
        maskStyle: _react2["default"].PropTypes.object,
        className: _react2["default"].PropTypes.string
    },

    getInitialState: function getInitialState() {
        return {
            display: "none",
            opacity: 0,
            marginTop: -50,
            timer: null
        };
    },

    componentDidMount: function componentDidMount() {
        this.refs.mask.getDOMNode().addEventListener("click", this._autoHide);
    },

    componentWillUnmount: function componentWillUnmount() {
        this.refs.mask.getDOMNode().removeEventListener("click", this._autoHide);
    },

    _autoHide: function _autoHide(e) {
        if (e.target === this.refs.mask.getDOMNode()) this.hide();
    },

    show: function show() {
        if (this.state.timer) return;
        var that = this;
        this.state.timer = setInterval(function () {
            if (that.state.opacity < 0.99) {
                that.state.opacity += 0.10;
                that.state.marginTop += 5;
                that.setState({
                    display: useWebkit ? "-webkit-box" : "flex"
                });
            } else {
                clearInterval(that.state.timer);
                that.state.timer = null;
            }
        }, 20);
    },

    hide: function hide() {
        if (this.state.timer) return;
        var that = this;
        this.state.timer = setInterval(function () {
            if (that.state.opacity < 0.01) {
                that.setState({ display: "none" });
                clearInterval(that.state.timer);
                that.state.timer = null;
            } else {
                that.state.opacity -= 0.10;
                that.state.marginTop -= 5;
                that.setState({});
            }
        }, 20);
    },

    render: function render() {
        var maskStyle = {
            position: "fixed",
            top: 0,
            left: 0,
            width: "100%",
            height: "100%",
            background: "rgba(0, 0, 0, 0.6)",
            zIndex: 9
        };

        maskStyle.display = this.state.display;
        maskStyle.opacity = Math.sin(Math.PI * this.state.opacity / 2);
        assign(maskStyle, this.props.maskStyle);

        var dialogStyle = {
            width: "50%",
            background: "white",
            padding: "1em",
            boxShadow: "0 4px 8px #333"
        };

        dialogStyle.marginTop = Math.sin(Math.PI * this.state.marginTop / 100) * 50;
        assign(dialogStyle, this.props.style);

        return _react2["default"].createElement(
            Layout,
            { ref: "mask", center: true, centerJustified: true, style: maskStyle },
            _react2["default"].createElement(
                Item,
                { ref: "dialog", style: dialogStyle, className: this.props.className },
                this.props.children
            )
        );
    }
});

exports["default"] = {
    Layout: Layout,
    Box: Layout,
    Item: Item,
    Dialog: Dialog
};
module.exports = exports["default"];
