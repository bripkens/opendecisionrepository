/*
 * Author: Ben Ripkens <bripkens.dev@gmail.com>
 */


/*
 * Don't change the following two lines
 */
var odr = odr || {};
var j = jQuery.noConflict();




/*
 * ###########################################################################
 *                              Basic settings
 */
odr.groups = ["package", "lines", "handles", "nodes"]

odr.rectangleSettings = {
    idPrefix : "node",
    "class" : "node",
    group : "nodes",
    rx : 10,
    ry : 10
}

odr.handleSettings = {
    idPrefix : "dragHandle",
    "class" : "dragHandle",
    group : "handles",
    radius : 6
}

odr.associationSettings = {
    idPrefix : "relationship",
    "class" : "relationship"
}

odr.lineSettings = {
    idPrefix : "line",
    "class" : "line",
    "group" : "lines"
}

odr.gridSettings = {
    "class" : "grid",
    width : 10,
    height : 10
}

odr.svgContainer = {
    id : "svgTarget",
    initialSize : {
        width : "auto",
        height : "auto"
    },
    padding : {
        x : 10,
        y : 10
    }
}

odr.css = {
    url : "resources/css/graph.css",
    inlineStyle : '@import "resources/css/graph.css";'
}





/*
 * ###########################################################################
 *                          Global Variables
 * Please do not change anything after this comments
 */

odr._svgContainer = undefined;
odr._svg = undefined;
odr._initFunctions = [];
odr._readyFunctions = [];
odr._scale = {
    level : 1
}







/*
 * ###########################################################################
 *                            Initialization
 */
odr.init = function(callback) {
    odr._initFunctions[odr._initFunctions.length] = callback;
}


odr.ready = function(callback) {
    odr._readyFunctions[odr._readyFunctions.length] = callback;
}


j(document).ready(function() {
    for (var i = 0; i < odr._initFunctions.length; i++) {
        odr._initFunctions[i]();
    }

    for (var i = 0; i < odr._readyFunctions.length; i++) {
        odr._readyFunctions[i]();
    }
})







/*
 * ###########################################################################
 *                              Utility functionality
 */
function extend(child, supertype){
    child.prototype.__proto__ = supertype.prototype;
    child.superClass = supertype.prototype;
}

Function.prototype.createDelegate = function(scope) {
    var fn = this;
    return function() {
        // Forward to the original function using 'scope' as 'this'.
        return fn.apply(scope, arguments);
    }
}

String.prototype.removeNonNumbers = function() {
    var result = new String(this);
    result = result.replace(/[^0-9]/g, '');

    return result;
}





/*
 * ###########################################################################
 *                              SVG
 */
odr.init(function() {
    odr._svgContainer = j("#" + odr.svgContainer.id);
    odr._svgContainer.svg({
        onLoad: function(svg) {
            odr._svg = svg;

            odr._svg.style(odr.css.inlineStyle);

            for (var i = 0; i < odr.groups.length; i++) {
                odr._svg.group(odr.groups[i]);
            }

            if (odr.svgContainer.initialSize.width == "auto") {
                odr._svgContainer.attr("width", document.documentElement.clientWidth);
            } else {
                odr._svgContainer.attr("width", odr.svgContainer.initialSize.width);
            }

            if (odr.svgContainer.initialSize.height == "auto") {
                odr._svgContainer.attr("height", document.documentElement.clientHeight);
            } else {
                odr._svgContainer.attr("height", odr.svgContainer.initialSize.height);
            }
        }
    });
})







odr.assertContainerSize = function(elementId) {

    container = odr._svgContainer.children("svg");

    containerWidth = 0;
    containerHeight = 0;

    selector = undefined;

    if (elementId) {
        selector = "#" + elementId;
        containerWidth = container.attr("width") - odr.svgContainer.padding.x;
        containerHeight = container.attr("height") - odr.svgContainer.padding.y;
    } else {
        selector = "." + odr.rectangleSettings["class"] + ", ." + odr.handleSettings["class"];
    }

    j(selector).each(function() {
        currentElement = odr.registry.get(j(this).attr("id").removeNonNumbers());
        lowerRightCorner = currentElement.bottomRight();

        lowerRightCorner.x *= odr._scale.level;
        lowerRightCorner.y *= odr._scale.level;

        if (lowerRightCorner.x > containerWidth) {
            containerWidth = lowerRightCorner.x;
        }

        if (lowerRightCorner.y > containerHeight) {
            containerHeight = lowerRightCorner.y;
        }
    });

    containerWidth += odr.svgContainer.padding.x
    containerHeight += odr.svgContainer.padding.y;

    container.attr("width", containerWidth);
    container.attr("height", containerHeight);
    parent = odr._svgContainer;
    parent.css({
        width :  containerWidth,
        height : containerHeight
    });
}