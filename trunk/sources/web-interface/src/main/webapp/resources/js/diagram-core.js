/**
 * @fileOverview
 *
 * This file contains core functionality that is required by all entities and the menu
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */






/*
 * ###############################################################################################################
 *                                              Visualization set up
 */

/**
 * @description
 * Attach a lisnener to the bootstrap phase. You should only attach listeners to this phase that
 * don't require that the whole drawing facility is completely set up!
 *
 * @param {Function} listener The function which should be called during the initialization phase.
 */
odr.bootstrap = function(listener) {
    odr.vars.bootstrapFunctions[odr.vars.bootstrapFunctions.length] = listener;
}





/**
 * @description
 * Listeners that you add to the ready phase can make use of the whole drawing facility. This generally means
 * that you can start adding nodes or calling methods.
 *
 * @param {Function} callback The function which should be called after the drawing facility is set up.
 */
odr.ready = function(callback) {
    odr.vars.readyFunctions[odr.vars.readyFunctions.length] = callback;
}






/*
 * Start up everything by calling the bootstrap and ready listeners
 */
window.onsvgload = function() {
    // TODO show load animation

    for (var i = 0; i < odr.vars.bootstrapFunctions.length; i++) {
        odr.vars.bootstrapFunctions[i]();
    }

    for (var i = 0; i < odr.vars.readyFunctions.length; i++) {
        odr.vars.readyFunctions[i]();
    }

    // TODO hide load animation
}










/*
 * ###############################################################################################################
 *                                              miscellaneous functionality
 */

/**
 * @description
 * <p>Make sure that the size of the body element is at least the size of the available viewport.</p>
 *
 * <p>This is mandatory for the lasso as otherwise the click event on the body won't be recognized.</p>
 */
odr.assertBodySize = function() {
    var documentWidth = $(document).width();
    var documentHeight = $(document).height();


    var bodyWidth = $("body").width();
    var bodyHeight = $("body").height();

    $("body").width(Math.max(documentWidth, bodyWidth));
    $("body").height(Math.max(documentHeight, bodyHeight));
};
/**
 * @private
 * Just do it on every resize and when it is first loaded
 */
odr.ready(function() {
    odr.assertBodySize();
    $(window).resize(odr.assertBodySize);
});













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
 * <p>Call a method with an array of arguments.</p>
 *
 * @param {params[]} params The arguments that you want to supply to the function.
 */
Function.prototype.callWithParams = function(params) {
    params.unshift(null);
    this.apply(this, Array.prototype.slice.call(params, 1));
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














/**
 * @description
 * Retrieve all elements that have a specific class. This function is used in combination with svg.
 *
 * @param {Object} namespace The svgweb namespace that will be used. In most cases it will be svgns
 * (not a string but instead a global).
 * @param {String} theClass the class that the element should have
 * @param {String} [tagName] The elements tag. This can be used to increase the performance
 * @return {Object[]} Elements from the namespace.
 */
document.getElementsByClassNS= function(namespace, theClass, tagName) {

    if (tagName == undefined) {
        tagName = "*";
    }

    var allElements = document.getElementsByTagNameNS(namespace, tagName);

    var result = [];

    for (i = 0; i < allElements.length; i++) {

        var currentElement = allElements[i];
        var classesOfCurrentElement = currentElement.className.baseVal.split(" ");

        for(j = 0; j < classesOfCurrentElement.length; j++) {
            if (classesOfCurrentElement[j] == theClass) {
                result[result.length] = currentElement;
                break;
            }
        }
    }

    return result;
}













/**
 * @description
 * This function measures the size of a given text with a specified css.
 *
 * @param {String} text The text from which you want to meassure the size
 * @param {Object} css The css which should be used for meassuring
 * @return {Object} An object with width and height properties that represent the text dimensions.
 */
odr.meassureTextDimensions = function(text, css) {
    var div = document.createElement('div');
    document.body.appendChild(div);
    $(div).css(css);

    $(div).text(text);

    var result = {
        height: $(div).outerHeight(),
        width: $(div).outerWidth()
    }

    $(div).remove();

    return result;
}









/**
 * @description
 * <p>Round <i>value</i> to a multiple of <i>roundTo</i></p>
 *
 * @param {Number} value The value which you want to round
 * @param {Number} roundTo Round to the nearest multiple of this value
 */
odr.round = function(value, roundTo) {
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
odr.roundUp = function(value, roundTo) {
    var modResult = value % roundTo;

    if(modResult == 0) {
        return value;
    }

    return value + (roundTo - (modResult));
}









/**
 * @description
 * This function always returns a new unique id, i.e. a number. The first id is 0.
 *
 * @return {Number} a unique id
 */
odr.newId = function() {
    return odr.vars.idCounter++;
}






/**
 * @description
 * <p>Move all marked shapes</p>
 *
 * <p>odr.vars.markedElements is used for performance reasons.</p>
 *
 * @param {Number} x The number of pixel that each marked element should be moved in horizontal direction
 * @param {Number} y The number of pixel that each marked element should be moved in vertical direction
 * @param {odr.Drawable} exclude Exclude this item from moving. Normally this should be that item that is dragged
 * as otherwise it would be moved twice.
 */
odr.moveMarkedShapes = function(x, y, exclude) {
    var root = odr.canvas();

    // wait with redrawing the SVG
    var suspendID = root.suspendRedraw(5000);

    for(var key in odr.vars.markedElements) {
        var element = odr.vars.markedElements[key];

        if (element != exclude) {
            element.move(x, y);
        }
    }

    // enable redrawing
    root.unsuspendRedraw(suspendID);
}










/**
 * @description
 * <p>Create a new arrow with default attributes and add it to the definitions of the svg.</p>
 *
 * @return {Element} The created element
 */
odr.arrow = function(id) {
    var marker = document.createElementNS(svgns, "marker");

    if (id != undefined) {
        marker.id = id;
    }

    for(var attributeName in odr.settings.line.arrow.attributes) {
        marker.setAttribute(attributeName, odr.settings.line.arrow.attributes[attributeName]);
    }

    marker.className = odr.settings.line.arrow["class"];

    var path = document.createElementNS(svgns, "path");
    path.setAttribute("d", "M0,0L10,5L0,10z");
    marker.appendChild(path);

    var defs = document.getElementsByTagNameNS(svgns, "defs")[0];
    defs.appendChild(marker);

    return marker;
};


















/*
 * ###############################################################################################################
 *                                        Load translations
 */
odr.bootstrap(function() {
    // receive translation from the server
    $.ajax({
        url : odr.settings.request.translation,
        dataType : "json",
        error : function(data, textStatus, errorThrown) {
        // TODO Show an error popup
        },
        success : function(data) {
            // store translation
            odr.translation.locale = data.Locale;
            odr.translation.text = data.Translations;

            document.title = odr.translation.text["title"];

            // translate static button values
            $("input." + odr.translation["class"]).each(function() {
                var translation = odr.translation.text[$(this).val()];
                $(this).val(translation);
                $(this).removeClass(odr.translation["class"]);
            });

            // translate static alternative image texts
            $("img." + odr.translation["class"]).each(function() {
                var translation = odr.translation.text[$(this).attr("alt")];
                $(this).attr("alt", translation);
                $(this).removeClass(odr.translation["class"]);
            });

            // translate static html text elements
            $("." + odr.translation["class"]).each(function() {
                var translation = odr.translation.text[$(this).text()];
                $(this).text(translation);
                $(this).removeClass(odr.translation["class"]);
            });
            
        }
    });
});










/*
 * ###############################################################################################################
 *                                        SVG canvas
 */
/**
 * @description
 * Get the SVG canvas
 *
 * @return {Object} The SVG canvas DOM element
 */
odr.canvas = function() {
    return document.getElementsByTagNameNS(svgns, 'svg')[0];
}







/**
 * @description
 * Make sure that the size of the SVG canvas is big enough for all lines
 */
odr.assertSvgSize = function() {
    var root = odr.canvas();

    var width = 0, height = 0;
    
    for(var key in odr.vars.shapesThatDetermineCanvasSize) {
        var shape = odr.vars.shapesThatDetermineCanvasSize[key];
        var bottomRight = shape.bottomRight();

        if (shape.visible()) {
            width = Math.max(width, bottomRight.x);
            height = Math.max(height, bottomRight.y);
        }
    }

    width += odr.settings.svg.padding.right;
    height += odr.settings.svg.padding.bottom;

    // wait with redrawing the SVG
    var suspendID = root.suspendRedraw(5000);

    root.setAttribute("width", width);
    root.setAttribute("height", height);
    root.setAttribute("viewBox", "0 0 " + width + " " + height);

    // enable redrawing
    root.unsuspendRedraw(suspendID);
};









/*
 * ###############################################################################################################
 *                             Selection of multiple nodes / lasso
 */
/**
 * @private
 * Allow to select more then one element by creating a lasso that can span over multiple elements
 */
odr.ready(function() {
    $("body").mousedown(odr._selectionStart);
});







/**
 * @description
 * This function will be called when the user clicks on the body element.
 *
 * @param {jQueryEvent} e The jQuery event
 */
odr._selectionStart = function(e) {
    if (!$(e.target).is("body, svg")) {
        return;
    }

    odr.vars.lasso = $("#" + odr.settings.lasso.id);

    odr.vars.lasso.show();
    odr.vars.lasso.initialX = e.pageX;
    odr.vars.lasso.initialY = e.pageY;
    odr.vars.lasso.css("left", e.pageX);
    odr.vars.lasso.css("top", e.pageY);
    odr.vars.lasso.css("width", 0);
    odr.vars.lasso.css("height", 0);

    if (!e.ctrlKey) {
        odr.clearSelectedElements();
    }

    $("body").mousemove(odr._selectionResize);
    $("body").mouseup(odr._selectionEnd);

    // returning false as otherwise the browser will try to select the content of the document
    return false;
};






/**
 * @private
 *
 * @description
 * This function will be called when the user clicked on the body or svg element and moved the mouse
 *
 * @param {jQueryEvent} e The jQuery event
 */
odr._selectionResize = function(e) {
    var x1 = odr.vars.lasso.initialX;
    var x2 = e.pageX;

    var y1 = odr.vars.lasso.initialY;
    var y2 = e.pageY;

    var minX = Math.min(x1, x2);
    var minY = Math.min(y1, y2);

    odr.vars.lasso.css("left", minX);
    odr.vars.lasso.css("top", minY);
    odr.vars.lasso.css("width", Math.max(x1, x2) - minX);
    odr.vars.lasso.css("height", Math.max(y1, y2) - minY);

    // returning false as otherwise the browser will try to select the content of the document
    return false;
};







/**
 * @private
 *
 * @description
 * This function will be called when the user releases the mouse and previously clicked on the body or svg element.
 *
 * @param {jQueryEvent} e The jQuery event
 */
odr._selectionEnd = function(e) {
    $("body").unbind("mousemove", odr._selectionResize);
    $("body").unbind("mouseup", odr._selectionEnd);
    
    odr.vars.lasso.hide();

    var left = parseInt(odr.vars.lasso.css("left").removeNonNumbers());
    var top = parseInt(odr.vars.lasso.css("top").removeNonNumbers());
    var width = odr.vars.lasso.width();
    var height = odr.vars.lasso.height();

    odr.selectElements(left, top, left + width, top + height);

    // returning false as otherwise the browser will try to select the content of the document
    return false;
};








/**
 * @description
 * Deselect all selected elements
 */
odr.clearSelectedElements = function() {
    for(var key in odr.vars.markedElements) {
        odr.vars.markedElements[key].marked(false);
    }
};







/**
 * @description
 * Select all elements in odr.vars.shapesThatDetermineCanvasSize that fall into the given region.
 *
 * @param {Number} minX The minimum x coordinate
 * @param {Number} minY The minimum y coordinate
 * @param {Number} maxX The maximal x coordinate
 * @param {Number} maxY The maximal y coordinate
 */
odr.selectElements = function(minX, minY, maxX, maxY) {
    for(var key in odr.vars.shapesThatDetermineCanvasSize) {
        var element = odr.vars.shapesThatDetermineCanvasSize[key];

        var topLeft = element.topLeft();
        var bottomRight = element.bottomRight();

        if (topLeft.x >= minX && topLeft.y >= minY && bottomRight.x <= maxX && bottomRight.y <= maxY) {
            element.marked(!element.marked());
        }
    }
};