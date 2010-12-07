/**
 * @fileOverview
 *
 * Core functionality, this file should be loaded first.
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */


/**
 * @namespace The namespace for the whole drawing JavaScript.
 */
var odr = odr || {};

/**
 * @namespace
 * jQuery conflicts with Icefaces, therefore jQuery is assigned to this variable.
 */
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
            'font-size': '12px'
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
            'font-size': '11px'
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

odr.drag = {
    extensionsMultiplier : 3
}

odr.css = {
    url : "resources/css/drawing.css",
    inlineStyle : '@import "resources/css/drawing.css";',
    decisionClass : "decision",
    iterationClass : "iteration",
    disconnectedClass : "disconnected"
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

// the following two variables are used by drawing.js and drawing.menu.js. They are holding
// references to all rectangles and nodes
odr._allRectangles = {};
odr._allAssociations = {};
odr._requestedData = {};



/*
 * ###########################################################################
 *                            Initialization
 */
/**
 * @description
 * Attach a lisnener to the initialization phase. You should only attach listeners to this phase that
 * don't require that the whole drawing facility is completely set up!
 *
 * @param {Function} callback The function which should be called during the initialization phase.
 */
odr.init = function(callback) {
    odr._initFunctions[odr._initFunctions.length] = callback;
}
/**
 * @description
 * Listeners that you add to the ready phase can make use of the whole drawing facility. This generally means
 * that you can start adding nodes or calling methods.
 *
 * @param {Function} callback The function which should be called after the drawing facility is set up.
 */
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
/**
 * @description
 * <p>This method is used to simulate inheritance in JavaScript.</p>
 *
 * <p>Source:
 * <a href="https://developer.mozilla.org/en/JavaScript/Guide/Inheritance_Revisited">
 *     Mozilla Developer Network: Inheritance revisited
 * </a></p>
 *
 * @param {Function} child The sub type which should inherit all methods from supertype.
 * @param {Function} supertype The super type
 */
function extend(child, supertype){
    child.prototype.__proto__ = supertype.prototype;
    child.superClass = supertype.prototype;
}


/**
 * @description
 * <p>We extend the prototype of all functions with the function createDelegate. This method allows
 * us to change the scope of a function to "this".</p>
 *
 * <p>This is useful when attaching listeners to jQuery events like click or mousemove as jQuery normally uses
 * $(this) to reference the source of the event. When using the createDelegate method, this will point to the
 * object that you want to reference with this.</p>
 *
 * <p>Source: <a href="http://stackoverflow.com/questions/520019/controlling-the-value-of-this-in-a-jquery-event">
 *     Stackoverflow
 * </a></p>
 *
 * @param {Object} scope The scope which you want to apply.
 */
Function.prototype.createDelegate = function(scope) {
    var fn = this;
    return function() {
        // Forward to the original function using 'scope' as 'this'.
        return fn.apply(scope, arguments);
    }
}

/**
 * @description
 * This function strips everything from a string that is not a number,
 *
 * @return {String} Only the numbers from the previous string.
 */
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
                odr._svgContainer.css("width", document.documentElement.clientWidth);
            } else {
                odr._svgContainer.css("width", odr.svgContainer.initialSize.width);
            }

            if (odr.svgContainer.initialSize.height == "auto") {
                odr._svgContainer.css("height", document.documentElement.clientHeight);
            } else {
                odr._svgContainer.css("height", odr.svgContainer.initialSize.height);
            }
        }
    });
})






/**
 * @description
 * <p>Make sure that the element which you specified by elementId fit on the canvas. If it doesn't, resize the canvas.</p>
 *
 * <p>If you don't specify a parameter, then make sure that all {@link odr.Shape}s fit on the canvas.</p>
 *
 * @param {String|Number} elementId The id for which you want to make sure that it fits on the canvas.
 */
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
        selector = "." + odr.rectangleSettings["class"] + ", ." + odr.handleSettings["class"] + ", ." + odr.associationSettings.text["class"];
    }

    j(selector).each(function() {
        var currentElement = odr.registry.get(j(this).attr("id"));

        if (currentElement == undefined) {
            currentElement = odr.registry.get(j(this).attr("id").removeNonNumbers());
        }

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
/**
 * @description
 * <p>Round <i>value</i> to a multiple of <i>roundTo</i></p>
 *
 * @param {Number} value The value which you want to round
 * @param {Number} roundTo Round to the nearest multiple of this value
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

/**
 * @description
 * Just list {@link odr._round} but it just rounds up to a multiple of <i>roundTo</i>.
 *
 * @param {Number} value The value which you want to round
 * @param {Number} roundTo Round to the nearest multiple of this value
 * @see odr._round
 */
odr._roundUp = function(value, roundTo) {
    var modResult = value % roundTo;

    if(modResult == 0) {
        return value;
    }

    return value + (roundTo - (modResult));
}

/**
 * @description
 * <p>Snap the position of an {@link odr.Shape} to the grid. The grid dimensions are defined in the
 * {@link odr.grid} settings.</p>
 *
 * <p>The position will be rounded up or down.</p>
 *
 * @param {odr.Shape} element The element which you want to snap to the grid
 */
odr.snapPosition = function(element) {
    if (element.x() != undefined) {
        element.x(odr._round(element.x(), odr.grid.width));
    }

    if (element.y() != undefined) {
        element.y(odr._round(element.y(), odr.grid.height));
    }
}

/**
 * @description
 * <p>Snap the position and size of an {@link odr.Shape} to the grid. The grid dimensions are defined in the
 * {@link odr.grid} settings.</p>
 *
 * <p>The size will only be rounded up.</p>
 *
 * @param {odr.Shape} element The element which you want to snap to the grid
 * @see odr.snapPosition
 */
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
/**
 * @description
 * <p>Enable dragging for an {@link odr.Shape}. This function attaches required listeners.</p>
 *
 * @param {odr.Shape} element The element for which you want to enable dragging
 */
odr.enableDragging = function(element) {
    element.mousedown(odr._dragStart);
    element.mouseup(odr._dragStop);
}

/**
 * @description
 * <p>This function will be called when the user clicks on a node. It is responsible for starting the process
 * of dragging a node.</p>
 *
 * @param {jQuery event} e The jQuery mouse event
 */
odr._dragStart = function(e) {
    if(e.ctrlKey) {
        return false;
    }

    var container = odr._svgContainer.children("svg");

    var containerWidth = container.attr("width") * odr.drag.extensionsMultiplier
    var containerHeight = container.attr("height") * odr.drag.extensionsMultiplier
    container.attr("width", containerWidth);
    container.attr("height", containerHeight);
    container.attr("viewBox", "0 0 " + containerWidth + " " + containerHeight);
    odr._svgContainer.css({
        width :  containerWidth,
        height : containerHeight
    });

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

/**
 * @description
 * <p>This function handles the actual dragging, i.e. when the user clicked on a node and is now moving the mouse.</p>
 *
 * <p>This function also makes sure that the container sizes adapts itself to the position and size of the dragged
 * node. This function also contains the functionality that enables the user to drag a node to the border of the
 * </p>
 *
 * @param {jQuery event} e The jQuery mouse event
 */
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


/**
 * @description
 * <p>When the user releases the mouse button (while dragging), this function is called.</p>
 *
 * <p>
 * This function hides the grid and detaches temporarily bound listeners. In addition, {@link odr.assertContainerSize}
 * is called to make sure that the canvas size is adapted.
 * </p>
 *
 * @param {jQuery event} e The jQuery mouse event
 */
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
/**
 * @description
 * <p>Scale the association to a new level or retrieve the current scale level by calling the method without a
 * parameter.</p>
 *
 * <p>The default scale level is 1. The reduce the scale level by 10% set the scale level to 0.9.</p>
 *
 * @param {Number} [newScale] The new scale level
 * @return {Number} Always the current scale level. When setting a new scale level, the new level is returned.
 */
odr.scale = function(newScale) {
    if (!newScale) {
        return odr._scale.level;
    }

    odr._scale.level = newScale;

    var transformAttribute = "scale(" + odr._scale.level + ")";

    for (var i = 0; i < odr.groups.length; i++) {
        j("#" + odr.groups[i]).attr("transform", transformAttribute);
    }

    odr.assertContainerSize();

    return odr._scale.level;
}










/*
 * ###########################################################################
 *                              Export
 */
/**
 * @description
 * Call this function before exporting. This function will reduce the white borders at the sides of the image.
 */
odr.beforeExport = function() {

    var container = odr._svgContainer.children("svg");

    var x = null;
    var y = null;
    var width = 0;
    var height = 0;


    j("." + odr.rectangleSettings["class"] + ", ." + odr.handleSettings["class"] + ", ." + odr.associationSettings.text["class"]).each(function() {
        var currentElement = odr.registry.get(j(this).attr("id"));

        if (currentElement == undefined) {
            currentElement = odr.registry.get(j(this).attr("id").removeNonNumbers());
        }

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

    container.attr("viewBox", viewBox);
}

/**
 * @description
 * Call this function when the export is done. It will reset the canvas so that everything will look
 */
odr.afterExport = odr.assertContainerSize;














/*
 * ###########################################################################
 *                              Meassure text dimensions
 */
/**
 * @description
 * This function measures the size of a given text with a specified css.
 *
 * @param {String} text The text from which you want to meassure the size
 * @param {Object} css The css which should be used for meassuring
 * @return {Object} An object literal with width and height properties that represent the text dimensions.
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