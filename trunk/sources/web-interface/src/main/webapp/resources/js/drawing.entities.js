/*
 * Author: Ben Ripkens <bripkens.dev@gmail.com>
 */





/*
 * ###########################################################################
 *                            Entity initialization
 */
odr.init(function() {
    odr.registry = new odr.Registry();
});






/*
 * ###########################################################################
 *                              Registry class
 */
odr.Registry = function() {
    this._items = new Object();
}
odr.Registry.prototype = {
    _items : null,
    add : function(item) {
        this._items[item.id()] = item;
    },
    get : function(itemId) {
        return this._items[itemId];
    },
    remove : function(itemId) {
        delete this._items[itemId];
    }
}





/*
 * ###########################################################################
 *                              Callback class
 */

odr.Callback = function() {
    this._listeners = {
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
        this.handle(listener, this._listeners.draw);
    },
    redraw : function(listener) {
        this.handle(listener, this._listeners.redraw);
    },
    remove : function(listener) {
        this.handle(listener, this._listeners.remove);
    },
    dragStart : function(listener) {
        this.handle(listener, this._listeners.dragStart);
    },
    dragging : function(listener) {
        this.handle(listener, this._listeners.dragging);
    },
    dragEnd : function(listener) {
        this.handle(listener, this._listeners.dragEnd);
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
    this._id = odr.DrawableItem.idCounter++;

    odr.registry.add(this);

    this._value = null;
    this._parent = null;
}

odr.DrawableItem.idCounter = 0;

odr.DrawableItem.prototype = {
    _id : -1,
    _value : null,
    _parent : null,
    paint : function() {
        this.draw();
    },
    repaint : function() {
        this.redraw();
    },
    dispose : function() {
        odr.registry.remove(this._id);
        this.remove();
    },
    value : function(value) {
        if (value) {
            this._value = value;
            return this;
        }

        return this._value;
    },
    parent : function(parent) {
        if (parent) {
            this._parent = parent;
            return this;
        }

        return this._parent;
    },
    id : function() {
        return this._id;
    }
}

extend(odr.DrawableItem, odr.Callback);







/*
 * ###########################################################################
 *                              Shape class
 */
odr.Shape = function() {
    odr.DrawableItem.call(this);
    this._x = -1;
    this._y = -1;
    this._width = -1;
    this._height = -1;
}

odr.Shape.prototype = {
    _x : -1,
    _y : -1,
    _width : -1,
    _height : -1,
    center : function() {
        throw("Abstract method");
    },
    topLeft : function() {
        throw("Abstract method");
    },
    bottomRight : function() {
        throw("Abstract method");
    },
    x : function(x) {
        if (x) {
            this._x = x;
            return this;
        }

        return this._x;
    },
    y : function(y) {
        if (y) {
            this._y = y;
            return this;
        }

        return this._y;
    },
    width : function(width) {
        if (width) {
            this._width = width;
            return this;
        }

        return this._width;
    },
    height : function(height) {
        if (height) {
            this._height = height;
            return this;
        }

        return this._height;
    }
}

extend(odr.Shape, odr.DrawableItem);








/*
 * ###########################################################################
 *                              Endpoint class
 */
odr.Endpoint = function() {
    odr.Shape.call(this);
    this._label = null;
}

odr.Endpoint.prototype = {
    _label : null,
    label : function(label) {
        if(label) {
            this._label = label;
            return this;
        }

        return this._label;
    }
}

extend(odr.Endpoint, odr.Shape);







/*
 * ###########################################################################
 *                              Node class
 */
odr.Rectangle = function() {
    odr.Endpoint.call(this);
}

odr.Rectangle.prototype = {
    center : function() {
        return {
            x : this.x() + this.width() / 2,
            y : this.y() + this.height() / 2
        };
    },
    topLeft : function() {
        return {
            x : this.x(),
            y : this.y()
        }
    },
    bottomRight : function() {
        return {
            x : this.x() + this.width(),
            y : this.y() + this.height()
        }
    },
    paint : function() {
        odr.Rectangle.superClass.draw.call(this);
    },
    repaint : function() {
        odr.Rectangle.superClass.redraw.call(this);
    },
    dispose : function() {
        odr.Rectangle.superClass.remove.call(this);
    }
}

extend(odr.Rectangle, odr.Endpoint);