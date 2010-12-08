/**
 * @fileOverview
 *
 * This file contains definitions, settings and variable declarations
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */

/**
 * @namespace The namespace for the whole diagram JavaScript.
 */
var odr = odr || {};





/*
 * ###############################################################################################################
 *                                              Settings
 */

/**
 * @namespace
 * ODR settings
 */
odr.settings = {
    lowPerformanceMode : false,
    menu : {
        top : {
            "class" : "topMenu",
            expandedHeight : function() {
                return "-15px";
            },
            collapsedHeight : function(element) {
                return (element.height() + 11) * -1;
            },
            animationDuration : 500
        }
    },
    dragging : {
        jQueryUiSettings : {
            grid : [10, 10],
            snap : true,
            scroll: true,
            stack : ".node"
        }
    },
    resizing : {
        jQueryUiSettings : {
            grid : 10
        }
    },
    node : {
        idPrefix : "node",
        "class" : "node",
        markedClass : "marked",
        container : "nodeGroup",
        labelMeasureCss : {
            position: 'absolute',
            left: -1000,
            top: -1000,
            visibility : "hidden",
            "white-space" : "nowrap",
            "font-family" : "'lucida grande', tahoma, verdana, arial, sans-serif",
            "font-size" : "13px",
            "font-weight" : "bold"
        },
        statusMeasureCss : {
            position: 'absolute',
            left: -1000,
            top: -1000,
            visibility : "hidden",
            "white-space" : "nowrap",
            "font-family" : "'lucida grande', tahoma, verdana, arial, sans-serif",
            "font-size" : "12px"
        },
        textPadding : {
            x : 20,
            y : 10
        },
        size : {
            min : {
                width: 70,
                height : 50
            },
            multipleOf : {
                width : 10,
                height : 10
            }
        },
        infoIcon : {
            text : "Click here to retrieve additional information about this node.",
            "class" : "info vtip"
        },
        hideIcon : {
            text : "Click here to hide this node.",
            "class" : "hide vtip"
        }
    }
};







/*
 * ###############################################################################################################
 *                                              Variable declarations
 */

/**
 * @private
 */
odr.vars = {
    idCounter : 0,
    registry : null,    // Will be replaced during bootstrap with an instance of odr.Registry
    bootstrapFunctions : [],
    readyFunctions : []
}