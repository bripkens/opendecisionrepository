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
    request : {
        parameter : {
            projectId : "id",
            relationshipView : "relationship",
            chronologicalView : "chronological",
            stakeholderView : "stakeholder"
        },
        translation : "ViewpointTextProvider",
        dataProvider : "ViewpointDataProvider",
        dataReceiver : "ViewpointDataReceiver",
        project : "projectDetails.html?id={0}",
        projects : "projects.html"
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
        },
        bottom : {
            selectedClass : "selected",
            performance : {
                high : "#performanceHigh",
                low : "#performanceLow",
                out : "#performanceOut"
            },
            alignment : {
                on : "#autoAlignmentOn",
                off : "#autoAlignmentOff",
                ask : "#autoAlignmentAsk",
                out : "#autoAlignmentOut"
            }
        }
    },
    popup : {
        temporaryText : {
            refresh : "Refresh",
            back : "Abort and back to project",
            title : "Loading",
            text : "Loading, please wait.."
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
        },
        resizeIcon : {
            text : "Click here to reduce the node size to the bare minimum.",
            "class" : "resize vtip"
        }
    },
    handle : {
        idPrefix : "handle",
        "class" : "handle",
        markedClass : "marked",
        container : "handleGroup",
        jQueryUiDraggingSettings : {
            grid : [10, 10],
            scroll: true,
            cursor : "move"
        },
        size : {
            width : 6,
            height : 6
        },
        alignmentTreshhold : 40
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
        hoverAttributes : {
            stroke : "darkred",
            "stroke-width" : 4
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
            },
            hoverAttributes : {
                markerWidth : 4,
                fill : "darkred"
            }
        }
    },
    label : {
        idPrefix : "label",
        "class" : "label",
        container : "labelGroup",
        markedClass : "marked"
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
    "class" : "translate",
    titleClass : "translateTitle"
};












/*
 * ###############################################################################################################
 *                                      User preferences
 */
odr.user = {
    lowPerformanceMode : false,
    automaticallyAlignDragHandles : undefined
}







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
    lasso : null,
    requestParameter : null
};