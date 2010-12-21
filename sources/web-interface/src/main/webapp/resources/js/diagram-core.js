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
    odr.popup.prepare();

    var popupId1 = odr.popup.show(odr.settings.popup.temporaryText.title,
        odr.settings.popup.temporaryText.text,
        "resources/images/ajax-loader-circle.gif",
        "Loading icon",
        false);

    // Set timeout is used since we need this function call to end quickly in order to show
    // the loading animation. Without returning the function call, the popup won't be shown.
    setTimeout(function() {
        for (var i = 0; i < odr.vars.bootstrapFunctions.length; i++) {
            odr.vars.bootstrapFunctions[i]();
        }

        if (!odr.popup.close(popupId1)) {
            return;
        }

        var popupId2 = odr.popup.translate();

        // see above
        setTimeout(function() {
            for (var i = 0; i < odr.vars.readyFunctions.length; i++) {
                odr.vars.readyFunctions[i]();
            }

            odr.popup.close(popupId2);
            odr.showStatus(odr.translation.text["loader.text.load.success"]);
        }, 0);
    }, 0);
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

    marker.setAttribute("class", odr.settings.line.arrow["class"])

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
        async : false,
        timeout : 2000,
        error : function(data, textStatus, errorThrown) {
            odr.popup.show("Error",
                "An error occured while receiving the localized content from the server.",
                "resources/images/error-big.png",
                odr.translation.text["popup.error.icon.alt"],
                false);
        },
        success : function(data) {
            // store translation
            odr.translation.locale = data.Locale;
            odr.translation.text = data.Translations;

            document.title = odr.translation.text["title"];

            // translate titles
            $("." + odr.translation.titleClass).each(function() {
                var translation = odr.translation.text[$(this).attr("title")];
                $(this).attr("title", translation);
                $(this).removeClass(odr.translation.titleClass);
            });

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

        if (shape instanceof odr.Handle || shape.visible()) {
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
    
    var left = parseInt(odr.vars.lasso.css("left").removeNonNumbers());
    var top = parseInt(odr.vars.lasso.css("top").removeNonNumbers());
    var width = odr.vars.lasso.width();
    var height = odr.vars.lasso.height();

    odr.selectElements(left, top, left + width, top + height);

    odr.vars.lasso.hide();

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
















/*
 * ###############################################################################################################
 *                                          Drag handle alignment
 */
odr.ready(function() {
    $("#alignmentPopup").dialog({
        autoOpen: false,
        height: 170,
        width: 330,
        modal: true,
        zIndex : 5050,
        resizable : false,
        close: function() {
            $(this).find("#alignmentPopup-rememberSettings").removeAttr("checked");
        }
    });
});





/**
 * @description
 * Open a dialog box that checks whether the user wants the system to automatically align the drag handles for him.
 * This dialog won't be shown if the user selected that his action should be remembered.
 *
 * @param {Function} helpAction This function will be called when the user decideds that he wants help
 * @param {Function} cancelAction This function will be called when the user decideds that he wants NO help
 */
odr.alignmentHelper = function(helpAction, cancelAction) {
    if (odr.user.automaticallyAlignDragHandles == true) {
        helpAction();
        return;
    } else if (odr.user.automaticallyAlignDragHandles == false) {
        if (cancelAction != undefined) {
            cancelAction();
        }
        return;
    }


    var buttons = {};

    var out = $("#alignmentMenu");


    buttons[odr.translation.text["alignment.do"]] = function() {
        if ($("#alignmentPopup-rememberSettings").is(":checked")) {
            odr.user.automaticallyAlignDragHandles = true;

            out.addClass("on");
            out.removeClass("off");
            out.removeClass("questionmark");
        }

        helpAction();
        
        $(this).dialog("close");
    };

    buttons[odr.translation.text["alignment.cancel"]] = function() {
        if ($("#alignmentPopup-rememberSettings").is(":checked")) {
            odr.user.automaticallyAlignDragHandles = false;
            
            out.removeClass("on");
            out.addClass("off");
            out.removeClass("questionmark");
        }

        if (cancelAction != undefined) {
            cancelAction();
        }

        $(this).dialog("close");
    };

    $("#alignmentPopup").dialog("option", "buttons", buttons);
    $("#alignmentPopup").dialog("open");
}














/*
 * ###############################################################################################################
 *                                              Popup
 */
/**
 * @namespace
 * @description
 * All popup related functionality is encapsulated within this object
 */
odr.popup = {};







/**
 * @description
 * Prepare the popup from beeing shown. This method is called during initialization of the visualization. It uses
 * temporary button labels.
 */
odr.popup.prepare = function() {
    odr.vars.popupId = 0;

    var buttons = {};

    buttons[odr.settings.popup.temporaryText.back] = function() {
        var projectId = odr.vars.requestParameter[odr.settings.request.parameter.projectId];

        if (isNaN(projectId)) {
            alert("Can't redirect to the project details page since an invalid project id was supplied. Redirecting " +
                "to project overview instead.");
            window.location = odr.settings.request.projects;
            return;
        }
        
        window.location = odr.settings.request.project.replace("{0}",
            odr.vars.requestParameter[odr.settings.request.parameter.projectId]);
    };

    buttons[odr.settings.popup.temporaryText.refresh] = function() {
        location.reload(true);
    };

    $("#iconPopup").dialog({
        autoOpen: false,
        height: 220,
        width: 330,
        modal: true,
        zIndex : 5050,
        resizable : false,
        buttons : buttons,
        title : odr.settings.popup.temporaryText.title
    });
};







/**
 * @description
 * Translate the temporary values within the popup. This method will also be called during initialization but after
 * the translation has been loaded from the server
 */
odr.popup.translate = function() {
    var popup = $("#iconPopup");

    var previousButtons = popup.dialog("option", "buttons");
    var newButtons = {};

    newButtons[odr.translation.text["popup.to.Project"]] = previousButtons[odr.settings.popup.temporaryText.back];
    newButtons[odr.translation.text["popup.refresh"]] = previousButtons[odr.settings.popup.temporaryText.refresh];

    popup.dialog("option", "buttons", newButtons);

    return odr.popup.showLoad();
};









/**
 * @description
 * Show a popup
 *
 * @param {String} title The title of the popup
 * @param {String} text The message that will be presented within the popup
 * @param {String} image The url to the image
 * @param {String} imageAlt The alternative image text
 * @param {Boolean} [closeable] Whether this dialog is closeable. Default is true
 * @return {Number} A popupId number that can be supplied to {@link odr.popup.close} to close the popup.
 */
odr.popup.show = function(title, text, image, imageAlt, closeable) {
    odr.popup.close();

    var popup = $("#iconPopup");

    popup.dialog("option", "title", title);

    if (closeable == undefined) {
        closeable = true;
    }

    popup.dialog("option", "closeOnEscape", closeable);

    var closeIcon = popup.prev().find("a");

    if (closeable) {
        closeIcon.show();
    } else {
        closeIcon.hide();
    }

    popup.find("p").first().text(text);
    var img = popup.find("img").first();
    img.attr("src", image);
    img.attr("alt", imageAlt);

    odr.popup.close();
    popup.dialog("open");

    odr.vars.popupId++;

    return odr.vars.popupId;
}









/**
 * @description
 * Close the popup
 *
 * @param {Number} [popupId] The id which you retrieved when you opened the popup. By supplying this id you can
 * ensure that the popup will only be closed when it was the last one which was opened. When you don't supply an id,
 * then the popup will be closed what so ever.
 */
odr.popup.close = function(popupId) {
    if (popupId == undefined || popupId == odr.vars.popupId) {
        $("#iconPopup").dialog("close");
        return true;
    }

    return false;
}







/**
 * @description
 * Show the load popup. This will use the default translations and icon for the loading dialog.
 *
 * @return {Number} A popupId number that can be supplied to {@link odr.popup.close} to close the popup.
 */
odr.popup.showLoad = function() {
    return odr.popup.show(odr.translation.text["popup.load.title"],
        odr.translation.text["popup.load.text"],
        "resources/images/ajax-loader-circle.gif",
        odr.translation.text["popup.load.icon.alt"],
        false);
}










/**
 * @description
 * Show the error popup. This will use the default translations and icon for the error dialog.
 *
 * The error message won't be presented directly to the user but instead will be placed on the console.
 *
 * @param {String} [errorMessage] A message that will be logged on the JavaScript console.
 * @param {Boolean} [closeable] Whether the user should get the possibility to close the dialog. Default: false
 * @return {Number} A popupId number that can be supplied to {@link odr.popup.close} to close the popup.
 */
odr.popup.showError = function(errorMessage, closeable) {
    if (console != undefined && errorMessage != undefined) {
        console.log("############### - Error through odr.popup.showError - ##################");
        console.log(errorMessage);
        console.log("########################################################################");
    }

    if (closeable == undefined) {
        closeable = false;
    }

    return odr.popup.show(odr.translation.text["popup.error.title"],
        odr.translation.text["popup.error.text"],
        "resources/images/error-big.png",
        odr.translation.text["popup.error.icon.alt"],
        closeable);
}










/**
 * @description
 * Show the save popup. This will use the default translations and icon for the save dialog.
 * @return {Number} A popupId number that can be supplied to {@link odr.popup.close} to close the popup.
 */
odr.popup.showSave = function() {
    return odr.popup.show(odr.translation.text["popup.save.title"],
        odr.translation.text["popup.save.text"],
        "resources/images/ajax-loader-circle.gif",
        odr.translation.text["popup.save.icon.alt"],
        false);
}













/*
 * ###############################################################################################################
 *                                              Request parameter
 */
/**
 * @description
 * Taken from http://snipplr.com/view/799/get-url-variables/
 *
 * @return {Object} An object that contains all request parameters in the form of "parameterName : value".
 */
odr.getUrlVars = function() {
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}




odr.bootstrap(function() {
    odr.vars.requestParameter = odr.getUrlVars();
});











/*
 * ###############################################################################################################
 *                                              Maintaining the session
 */
/**
 * @description
 * This method is used to keep up the session. It could happen that a edits / views a visualization for longer then
 * the session timeout length. Therefore the server is "pinged" continuously to maintain the session.
 *
 * @param {Boolean} continuous Whether the ping should only be done once or multiple times.
 */
odr.maintainSession = function(continuous) {
    if (continuous == undefined) {
        continuous = true;
    }

    $.get(odr.settings.request.translation);
    $.ajax({
        url: odr.settings.request.sessionMaintaining,
        error: function(request, status, error) {
            odr.popup.showError("Could not maintain session: " + status, true);
        },
        success : function() {
            if (continuous) {
                setTimeout(odr.maintainSession, odr.settings.sessionMaintaining);
            }
        }
    });
};

odr.ready(odr.maintainSession);











/*
 * ###############################################################################################################
 *                                        Related decisions dialog
 */
odr.ready(function() {
    var buttons = {};

    buttons[odr.translation.text["related.nodes.popup.show"]] = function() {
        odr._changeVisibilityBasedOnRelationship($(this), true);
        $(this).dialog("close");
    };

    buttons[odr.translation.text["related.nodes.popup.hide"]] = function() {
        odr._changeVisibilityBasedOnRelationship($(this), false);
        $(this).dialog("close");
    };

    buttons[odr.translation.text["related.nodes.popup.cancel"]] = function() {
        $(this).dialog("close");
    };

    $("#relatedNodesPopup").dialog({
        autoOpen: false,
        height: 330,
        width: 400,
        modal: true,
        zIndex : 5050,
        buttons : buttons,
        close : function() {
            $(this).find("select > option").removeAttr("selected").first().attr("selected", true);
            $(this).find('input[name="relatedNodesPopup-visibility-others"]').removeAttr("checked").first().attr("checked", true);
            
            $(this).find("#relatedNodesPopup-followOutgoing").attr("checked", true);
            $(this).find("#relatedNodesPopup-followIncoming").attr("checked", true);
        }
    });
});







/**
 * @description
 * This function is used to show and hide nodes based on their interrelationships
 *
 * @param {jQueryHtmlElement} dialog The dialog window upon which you want to react.
 * @param {Boolean} relatedVisible Whether related nodes should be visible
 */
odr._changeVisibilityBasedOnRelationship = function(dialog, relatedVisible) {
    var depth = parseInt(dialog.find("select > option:selected").val());
    var otherVisibility = dialog.find('input[name="relatedNodesPopup-visibility-others"]:checked').val();

    var followOutgoing = dialog.find("#relatedNodesPopup-followOutgoing").is(":checked");
    var followIncoming = dialog.find("#relatedNodesPopup-followIncoming").is(":checked");

    if (otherVisibility == "show") {
        for(var e in odr.vars.allDecisionNodes) {
            odr.vars.allDecisionNodes[e].visible(true);
        }

        for(var e in odr.vars.allIterationNodes) {
            odr.vars.allIterationNodes[e].visible(true);
        }
    } else if (otherVisibility == "hide") {
        for(var e in odr.vars.allDecisionNodes) {
            odr.vars.allDecisionNodes[e].visible(false);
        }

        for(var e in odr.vars.allIterationNodes) {
            odr.vars.allIterationNodes[e].visible(false);
        }
    }

    var node = odr.vars.relatedNode;

    var root = odr.canvas();
    var suspendID = root.suspendRedraw(5000);

    odr.setRelatedVisible(node, relatedVisible, depth, followOutgoing, followIncoming);

    root.unsuspendRedraw(suspendID);
}














/**
 * @description
 * Change the visibility of this node and all it's related nodes to the new visibility.
 *
 * @param {odr.Node} node The node from which you want to start
 * @param {Boolean} visible Whether related nodes and the node from parameter 1 shall be visible
 * @param {Number} depth How many steps away should be taken into account
 * @param {Boolean} followOutgoing Whether to take outgoing relationships into account
 * @param {Boolean} followIncoming Whether to take incoming relationships into account
 */
odr.setRelatedVisible = function(node, visible, depth, followOutgoing, followIncoming) {
    node.visible(visible);

    if (depth != 0) {
        for(var e in odr.vars.allAssociations) {
            var association = odr.vars.allAssociations[e];

            if (followOutgoing && association.source() == node) {
                odr.setRelatedVisible(association.target(), visible, depth - 1, followOutgoing, followIncoming);
            } else if (followIncoming && association.target() == node) {
                odr.setRelatedVisible(association.source(), visible, depth - 1, followOutgoing, followIncoming);
            }
        }
    }
};







/**
 * @description
 * Show the related nodes dialog for the given node
 *
 * @param {odr.Node} node The node for which you want to show the dialog
 */
odr.showRelatedNodesDialog = function(node) {
    odr.vars.relatedNode = node;

    $("#relatedNodesPopup").dialog("open");
};