/*
 * Author: Ben Ripkens <bripkens.dev@gmail.com>
 */


/*
 * ###########################################################################
 *                              Callback class
 */

odr.Callback = function() {
    this.listeners = {
        draw : new Array(),
        redraw : new Array(),
        remove : new Array(),
        dragStart : new Array(),
        dragging : new Array(),
        dragEnd : new Array()
    };
}
odr.Callback.prototype = {
    listeners : null,
    draw : function(listener) {
        this.handle(listener, this.listeners.draw);
    },
    redraw : function(listener) {
        this.handle(listener, this.listeners.redraw);
    },
    remove : function(listener) {
        this.handle(listener, this.listeners.remove);
    },
    dragStart : function(listener) {
        this.handle(listener, this.listeners.dragStart);
    },
    dragging : function(listener) {
        this.handle(listener, this.listeners.dragging);
    },
    dragEnd : function(listener) {
        this.handle(listener, this.listeners.dragEnd);
    },
    handle : function(listener, collection) {
        if (listener) {
            collection[collection.length] = listener;
            return;
        }

        for(var i in collection) {
            collection[i]();
        }
    }
}