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
    for (var i = 0; i < odr.vars.bootstrapFunctions.length; i++) {
        odr.vars.bootstrapFunctions[i]();
    }

    for (var i = 0; i < odr.vars.readyFunctions.length; i++) {
        odr.vars.readyFunctions[i]();
    }
}










/*
 * ###############################################################################################################
 *                                              Utility functionality
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