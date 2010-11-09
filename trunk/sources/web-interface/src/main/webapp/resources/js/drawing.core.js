/*
 * Author: Ben Ripkens <bripkens.dev@gmail.com>
 */




/*
 * ###########################################################################
 *                              Basic settings
 */












/*
 * ###########################################################################
 *                          Global Variables
 * Please do not change anything after this comments
 */

var odr = odr || {};

var j = jQuery.noConflict();

odr._initFunctions = new Array();

odr._readyFunctions = new Array();








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
});




odr.ready(function() {
    rectangle = new odr.Rectangle();

    rectangle.draw(function() {
        console.log("Rectangle is drawn");
    })

    rectangle.redraw(function() {
        console.log("Rectangle is redrawn");
    })

    rectangle.remove(function() {
        console.log("Rectangle is removed");
    })

    rectangle.x(20);
    rectangle.y(20);
    rectangle.width(200);
    rectangle.height(300);

    rectangle.paint();
    rectangle.repaint();
    rectangle.dispose();

    console.log(rectangle.center());
    console.log(rectangle.topLeft());
    console.log(rectangle.bottomRight());
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
