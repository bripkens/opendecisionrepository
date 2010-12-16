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
    request : {
        translation : "ViewpointTextProvider"
    },
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
    grid : [10, 10],
    node : {
        idPrefix : "node",
        "class" : "node",
        markedClass : "marked",
        container : "nodeGroup",
        jQueryUiDraggingSettings : {
            grid : [10, 10],
            scroll: true,
            cursor : "move"
        },
        jQueryUiResizingSettings : {
            grid : 10
        },
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
                width : 6,
                height : 6
            }
        },
        infoIcon : {
            text : "Click here to retrieve additional information about this node.",
            "class" : "info vtip"
        },
        hideIcon : {
            text : "Click here to hide this node.",
            "class" : "hide vtip"
        },
        resizeIcon : {
            text : "Click here to reduce the node size to the bare minimum.",
            "class" : "resize vtip"
        }
    },
    handle : {
        idPrefix : "handle",
        "class" : "handle",
        container : "handleGroup",
        jQueryUiDraggingSettings : {
            grid : [10, 10],
            scroll: true,
            cursor : "move"
        },
        size : {
            width : 6,
            height : 6
        }
    },
    association : {
        idPrefix : "association",
        "class" : "association",
        container : "associationGroup"
    },
    line : {
        idPrefix : "line",
        "class" : "line",
        container : "lineGroup",
        attributes : {
            "stroke" : "black",
            "stroke-width" : "1"
        },
        arrow : {
            idPrefix : "arrow",
            "class" : "arrow",
            attributes : {
                refX : 10,
                refY : 5,
                markerWidth : 10,
                markerHeight : 10,
                orient : "auto",
                fill : "black",
                viewBox : "0 0 10 10"
            }
        }
    },
    svg : {
        id : "canvas",
        redrawTimeout : 5000,
        padding : {
            top : 10,
            right : 10,
            bottom : 10,
            left : 10
        }
    },
    lasso : {
        id : "lasso"
    }
};







/*
 * ###############################################################################################################
 *                             Translations will be loaded into this var
 */
odr.translation = {
    locale : null,
    text : null,
    "class" : "translate"
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
    readyFunctions : [],
    /**
     * All elements in this var need to considered when resizing the SVG canvas.
     *
     * They must all supply a topLeft, bottomRight and visible function
     */
    shapesThatDetermineCanvasSize : {},
    /**
     * A list of all marked elements. This variable is used for performance reasons
     */
    markedElements : {},
    lasso : null
};