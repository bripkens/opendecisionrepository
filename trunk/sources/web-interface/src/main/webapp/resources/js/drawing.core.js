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
odr.groups = ["package", "lines", "associations", "handles", "nodes"]

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
    "class" : "relationship",
    group : "associations"
}

odr.lineSettings = {
    idPrefix : "line",
    "class" : "line",
    "group" : "lines"
}

odr.grid = {
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

odr._dragging = {
    previousEvent : [],
    elementToDrag : [],
    itemToDrag : []
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


















/*
 * ###########################################################################
 *                              Snapping
 */
odr._round = function(value, roundTo) {
    modResult = value % roundTo;

    if(modResult == 0) {
        return value;
    } else if (modResult >= (roundTo / 2)) {
        return value + (roundTo - modResult);
    } else {
        return value - modResult;
    }
}


odr._roundUp = function(value, roundTo) {
    modResult = value % roundTo;

    if(modResult == 0) {
        return value;
    }

    return value + (roundTo - (modResult));
}

odr.snapPosition = function(element) {
    if (element.x() != undefined) {
        element.x(odr._round(element.x(), odr.grid.width));
    }

    if (element.y() != undefined) {
        element.y(odr._round(element.y(), odr.grid.height));
    }
}

odr.snap = function(element) {
    odr.snapPosition(element);

    if (element.width() != undefined) {
        element.width(odr._roundUp(element.width(), odr.grid.width * 2));
    }

    if (element.height() != undefined) {
        element.height(odr._roundUp(element.height(), odr.grid.height * 2));
    }
}











/*
 * ###########################################################################
 *                              Dragging
 */
odr.enableDragging = function(element) {
    element.mousedown(odr._dragStart);
    element.mouseup(odr._dragStop);
}

odr._dragStart = function(e) {
    if(e.ctrlKey) {
        return false;
    }

    button = e.button;
    odr._dragging.elementToDrag[button] = j(this)
    odr._dragging.previousEvent[button] = e;
    
    j("body").mousemove(odr._drag);
    j("body").addClass(odr.grid["class"]);

    odr._dragging.itemToDrag[button] = odr.registry.get(j(this).attr("id").removeNonNumbers());
    odr._dragging.itemToDrag[button].dragStart();

    return false;
}

odr._drag = function(e) {
    button = e.button;
    if (odr._dragging.previousEvent[button] == undefined) {
        j("body").unbind("mousemove");
        return false;
    }

    var oldX = odr._dragging.itemToDrag[button].x();
    var oldY = odr._dragging.itemToDrag[button].y();

    var deltaX = (e.pageX - odr._dragging.previousEvent[button].pageX) * (1 / odr._scale.level);
    var deltaY = (e.pageY - odr._dragging.previousEvent[button].pageY) * (1 / odr._scale.level);

    odr._dragging.itemToDrag[button].x(oldX + deltaX);
    odr._dragging.itemToDrag[button].y(oldY + deltaY);
    odr._dragging.previousEvent[button] = e;

    odr._dragging.itemToDrag[button].dragging();

    odr.assertContainerSize(odr._dragging.itemToDrag[button].extendedId());


//    viewportWidth = j(window).width();
//    viewportHeight = j(window).height();
//    scrollLeft = j(window).scrollLeft();
//    scrollTop = j(window).scrollTop();
//    lowerRightCorner = odr.itemToDrag[button].lowerRightCorner();
//    topLeftCorner = odr.itemToDrag[button].topLeftCorner();
//
//    scrollX = 0;
//    scrollY = 0;
//
//    doScrollRight = lowerRightCorner.x - scrollLeft > viewportWidth - odr.viewportScrollMargin.x;
//    doScrollLeft = topLeftCorner.x - scrollLeft < odr.viewportScrollMargin.x;
//    doScrollDown = lowerRightCorner.y > viewportHeight - odr.viewportScrollMargin.y - scrollTop;
//    doScrollUp = topLeftCorner.y < scrollTop + odr.viewportScrollMargin.y;
//
//    if (doScrollRight) {
//        scrollX = odr.scrollSpeed.right;
//    } else if (doScrollLeft) {
//        scrollX = odr.scrollSpeed.left;
//    }
//
//    if (doScrollUp) {
//        scrollY = odr.scrollSpeed.up;
//    } else if (doScrollDown) {
//        scrollY = odr.scrollSpeed.down;
//    }
//
//    window.scrollBy(scrollX, scrollY);

    return false;
}



odr._dragStop = function(e) {
    button = e.button;

    if (odr._dragging.itemToDrag[button] == undefined || e.ctrlKey) {
        return false;
    }

    j("body").removeClass(odr.grid["class"]);

    j("body").unbind("mousemove");
    odr._dragging.previousEvent[button] = undefined;

    odr._dragging.itemToDrag[button].dragEnd();

    odr.assertContainerSize();

    return false;
}