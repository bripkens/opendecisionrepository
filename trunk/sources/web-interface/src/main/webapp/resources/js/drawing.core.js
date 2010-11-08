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





/*
 * ###########################################################################
 *                              DrawableItem class
 */

odr.DrawableItem = function() {
    odr.Callback.call(this);
    this.id = odr.DrawableItem.idCounter++;
}
odr.DrawableItem.idCounter = 0;
odr.DrawableItem.prototype = {
    id : -1
}
extend(odr.DrawableItem, odr.Callback);






/*
 * ###########################################################################
 *                              Registry class
 */
odr.Registry = function() {
    this.items = new Array();
}
odr.Registry.prototype = {
    items : null,
    add : function(item) {
        this.items[item.id()] = item;
    },
    get : function(itemId) {
        return this.items[itemId];
    },
    remove : function(itemId) {
        alert("Not implemented: odr.Registry.remove");
    }
}