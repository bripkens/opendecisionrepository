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
odr.groups = ["package", "lines", "associations", "handles", "nodes", "texts"]

odr.defsSettings = {
    id : "definitions"
}

odr.rectangleSettings = {
    idPrefix : "node",
    "class" : "node",
    group : "nodes",
    rx : 10,
    ry : 10,
    background : {
        idPrefix : "nodeBackground",
        "class" : "nodeBackground"
    },
    text : {
        idPrefix : "nodeText",
        "class" : "nodeText",
        measureCSS : {
            position: 'absolute',
            left: -1000,
            top: -1000,
            visibility : "hidden",
            "white-space" : "nowrap",
            'font-family' : '"Verdana", sans-serif',
            'font-size': '12px'
        },
        stereotypePadding : 0
    },
    stereotype : {
        idPrefix : "nodeStereotype",
        "class" : "nodeStereotype",
        measureCSS : {
            position: 'absolute',
            left: -1000,
            top: -1000,
            visibility : "hidden",
            "white-space" : "nowrap",
            'font-family' : '"Verdana", sans-serif',
            'font-size': '11px'
        }
    },
    overlay : {
        idPrefix : "nodeOverlay",
        "class" : "nodeOverlay"
    },
    padding: {
        top : 15,
        right : 5,
        bottom : -10,
        left : 15
    }
}

odr.textSettings = {
    idPrefix : "text",
    "class" : "text",
    group : "texts"
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
    group : "associations",
    text : {
        idPrefix : "relationshipText",
        "class" : "relationshipText",
        measureCSS : {
            position: 'absolute',
            left: -1000,
            top: -1000,
            visibility : "hidden",
            "white-space" : "nowrap",
            'font-family' : '"Verdana", sans-serif',
            'font-size': '12px'
        }
    },
    arrow : {
        idPrefix : "arrow",
        color : "black"
    },
    helper : {
        id : "associationHelper",
        "class" : "associationHelper"
    }
}

odr.lineSettings = {
    idPrefix : "line",
    "class" : "line",
    group : "lines"
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

odr.scroll = {
    speed : {
        up : -50,
        down : 50,
        left : -50,
        right : 50
    },
    margin : {
        up : 100,
        down : 100,
        left : 50,
        right : 50
    }
}

odr.css = {
    url : "resources/css/drawing.css",
    inlineStyle : '@import "resources/css/drawing.css";'
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

            odr._svg.defs(odr.defsSettings.id);

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
    if (elementId == -1) {
        return;
    }


    var container = odr._svgContainer.children("svg");

    var containerWidth = 0;
    var containerHeight = 0;

    var selector = undefined;

    if (elementId) {
        selector = "#" + elementId;
        containerWidth = container.attr("width") - odr.svgContainer.padding.x;
        containerHeight = container.attr("height") - odr.svgContainer.padding.y;
    } else {
        selector = "." + odr.rectangleSettings["class"] + ", ." + odr.handleSettings["class"];
    }

    j(selector).each(function() {
        var currentElement = odr.registry.get(j(this).attr("id").removeNonNumbers());
        var lowerRightCorner = currentElement.bottomRight();

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
    container.attr("viewBox", "0 0 " + containerWidth + " " + containerHeight);
    var parent = odr._svgContainer;
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
    var modResult = value % roundTo;

    if(modResult == 0) {
        return value;
    } else if (modResult >= (roundTo / 2)) {
        return value + (roundTo - modResult);
    } else {
        return value - modResult;
    }
}


odr._roundUp = function(value, roundTo) {
    var modResult = value % roundTo;

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

    var button = e.button;
    odr._dragging.elementToDrag[button] = j(this)
    odr._dragging.previousEvent[button] = e;
    
    j("body").mousemove(odr._drag);
    j("body").addClass(odr.grid["class"]);

    if ((odr._dragging.itemToDrag[button] = odr.registry.get(j(this).attr("id"))) == null) {
        odr._dragging.itemToDrag[button] = odr.registry.get(j(this).attr("id").removeNonNumbers());
    }
    
    odr._dragging.itemToDrag[button].dragStart();

    return false;
}

odr._drag = function(e) {
    var button = e.button;
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


    var viewportWidth = j(window).width();
    var viewportHeight = j(window).height();
    var scrollLeft = j(window).scrollLeft();
    var scrollTop = j(window).scrollTop();
    var lowerRightCorner = odr._dragging.itemToDrag[button].bottomRight();
    var topLeftCorner = odr._dragging.itemToDrag[button].topLeft();

    var scrollX = 0;
    var scrollY = 0;

    var doScrollRight = (lowerRightCorner.x * odr._scale.level) - scrollLeft > viewportWidth - odr.scroll.margin.right;
    var doScrollLeft = (topLeftCorner.x * odr._scale.level) - scrollLeft < odr.scroll.margin.left;
    var doScrollDown = (lowerRightCorner.y * odr._scale.level) > viewportHeight - odr.scroll.margin.down - scrollTop;
    var doScrollUp = (topLeftCorner.y * odr._scale.level) < scrollTop + odr.scroll.margin.up;

    if (doScrollRight) {
        scrollX = odr.scroll.speed.right;
    } else if (doScrollLeft) {
        scrollX = odr.scroll.speed.left;
    }

    if (doScrollUp) {
        scrollY = odr.scroll.speed.up;
    } else if (doScrollDown) {
        scrollY = odr.scroll.speed.down;
    }

    window.scrollBy(scrollX, scrollY);

    return false;
}



odr._dragStop = function(e) {
    var button = e.button;

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













/*
 * ###########################################################################
 *                              Zooming
 */
odr.scale = function(newScale) {
    if (!newScale) {
        return odr._scale.level;
    }

    odr._scale.level = newScale;

    var transformAttribute = "scale(" + odr._scale.level + ")";

    for (var i = 0; i < odr.groups.length; i++) {
        odr._svg.group(odr.groups[i]);
        j("#" + odr.groups[i]).attr("transform", transformAttribute);
    }

    odr.assertContainerSize();

    return odr._scale.level;
}










/*
 * ###########################################################################
 *                              Export
 */
odr.beforeExport = function() {

    var container = odr._svgContainer.children("svg");

    var x = null;
    var y = null;
    var width = 0;
    var height = 0;


    j("." + odr.rectangleSettings["class"] + ", ." + odr.handleSettings["class"]).each(function() {
        var currentElement = odr.registry.get(j(this).attr("id").removeNonNumbers());

        var lowerRightCorner = currentElement.bottomRight();
        lowerRightCorner.x *= odr._scale.level;
        lowerRightCorner.y *= odr._scale.level;

        var topLeftCorner = currentElement.topLeft();
        topLeftCorner.x *= odr._scale.level;
        topLeftCorner.y *= odr._scale.level;

        if (lowerRightCorner.x > width) {
            width = lowerRightCorner.x;
        }

        if (lowerRightCorner.y > height) {
            height = lowerRightCorner.y;
        }

        if (x == null || topLeftCorner.x < x) {
            x = topLeftCorner.x;
        }

        if (y == null || topLeftCorner.y < y) {
            y = topLeftCorner.y;
        }
    });

    width += odr.svgContainer.padding.x
    height += odr.svgContainer.padding.y;

    x -= odr.svgContainer.padding.x
    y -= odr.svgContainer.padding.y;

    container.attr("width", width - x);
    container.attr("height", height - y);

    var viewBox = x + " " + y + " " + (parseInt(width) - parseInt(x)) + " " + (parseInt(height) - parseInt(y));

    container.attr("viewBox", viewBox)
}


odr.afterExport = odr.assertContainerSize;














/*
 * ###########################################################################
 *                              Meassure text dimensions
 */
odr.meassureTextDimensions = function(text, css) {
    var div = document.createElement('div');
    document.body.appendChild(div);
    j(div).css(css);

    j(div).text(text);

    var result = {
        height: j(div).outerHeight(),
        width: j(div).outerWidth()
    }

    j(div).remove();

    return result;
}