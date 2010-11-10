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
    this._items = {};
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
        draw : {},
        redraw : {},
        remove : {},
        dragStart : {},
        dragging : {},
        dragEnd : {},
        visibility : {}
    };
}

odr.Callback.types = {
    draw : "draw",
    redraw : "redraw",
    remove : "remove",
    dragStart : "dragStart",
    dragging : "dragging",
    dragEnd : "dragEnd",
    visibility : "visibility"
}

odr.Callback.prototype = {
    _listeners : null,
    draw : function(listener, identifier) {
        this.handle(listener, identifier, this._listeners.draw);
    },
    redraw : function(listener, identifier) {
        this.handle(listener, identifier, this._listeners.redraw);
    },
    remove : function(listener, identifier) {
        this.handle(listener, identifier, this._listeners.remove);
    },
    dragStart : function(listener, identifier) {
        this.handle(listener, identifier, this._listeners.dragStart);
    },
    dragging : function(listener, identifier) {
        this.handle(listener, identifier, this._listeners.dragging);
    },
    dragEnd : function(listener, identifier) {
        this.handle(listener, identifier, this._listeners.dragEnd);
    },
    visibility : function(listener, identifier) {
        this.handle(listener, identifier, this._listeners.visibility);
    },
    handle : function(listener, identifier, collection) {
        if (listener && identifier) {
            collection[identifier] = listener;
            return;
        }

        for(var i in collection) {
            collection[i]();
        }
    },
    unbind : function(type, identifier) {
        var collection = undefined;

        if (type == odr.Callback.types.draw) {
            collection = this._listeners.draw;
        } else if (type == odr.Callback.types.redraw) {
            collection = this._listeners.redraw;
        } else if (type == odr.Callback.types.remove) {
            collection = this._listeners.remove;
        } else if (type == odr.Callback.types.dragStart) {
            collection = this._listeners.dragStart;
        } else if (type == odr.Callback.types.dragging) {
            collection = this._listeners.dragging;
        } else if (type == odr.Callback.types.dragEnd) {
            collection = this._listeners.dragEnd;
        } else if (type == odr.Callback.types.visibility) {
            collection = this._listeners.visibility;
        } else {
            throw("Listener type is not defined");
        }

        delete collection[identifier];
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
    this._visible = true;
}

odr.DrawableItem.idCounter = 0;

odr.DrawableItem.prototype = {
    _id : -1,
    _value : null,
    _parent : null,
    _visible : true,
    paint : function(parent) {
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
    },
    visible : function(visible) {
        if (visible != undefined && this._visible != visible) {
            this._visible = visible;
            this.visibility();
            return this;
        }
        
        return this._visible;
    },
    extendedId : function() {
        throw("Abstract method");
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
 *                              Rectangle class
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
    paint : function(parent) {
        if (!this.visible()) {
            return;
        }

        odr.snap(this);

        j("#" + this.extendedId()).remove();

        if (!parent) {
            parent = j("#" + odr.rectangleSettings.group);
        }

        odr._svg.rect(parent, this.x(), this.y(), this.width(),
            this.height(), odr.rectangleSettings.rx, odr.rectangleSettings.ry, {
                "class" : odr.rectangleSettings["class"],
                "id" : this.extendedId()
            });

        var element = j("#" + this.extendedId());
        odr.enableDragging(element);
        this.dragging(this._dragging.createDelegate(this), this.extendedId());
        this.dragEnd(this._dragEnd.createDelegate(this), this.extendedId());

        odr.assertContainerSize(this.extendedId());

        odr.Rectangle.superClass.draw.call(this);
    },
    repaint : function() {
        var element = j("#" + this.extendedId());

        if (!this.visible()) {
            element.remove();
            return;
        } else if (element.size() == 0) {
            this.paint();
            return;
        }

        element.attr("width", this.width());
        element.attr("height", this.height());
        element.attr("x", this.x());
        element.attr("y", this.y());

        odr.assertContainerSize(this.extendedId());

        odr.Rectangle.superClass.redraw.call(this);
    },
    visible : function(visible) {
        var previousVisibility = odr.Rectangle.superClass.visible.call(this);
        var returnValue = odr.Rectangle.superClass.visible.call(this, visible);
        var newVisibility = odr.Rectangle.superClass.visible.call(this);

        if (previousVisibility != newVisibility) {
            this.repaint();
        }

        return returnValue;
    },
    _dragging : function() {
        this.repaint();
    },
    _dragEnd : function() {
        odr.snapPosition(this);
        this.repaint();
    },
    dispose : function() {
        odr.Rectangle.superClass.remove.call(this);
    },
    extendedId : function() {
        return odr.rectangleSettings.idPrefix + this.id();
    }
}

extend(odr.Rectangle, odr.Endpoint);









/*
 * ###########################################################################
 *                              Handle class
 */
odr.Handle = function() {
    odr.Shape.call(this);
}

odr.Handle.prototype = {
    center : function() {
        return {
            x : this.x(),
            y : this.y()
        };
    },
    topLeft : function() {
        return {
            x : this.x() - (this.width() / 2),
            y : this.y() - (this.height() / 2)
        }
    },
    bottomRight : function() {
        return {
            x : this.x() + (this.width() / 2),
            y : this.y() + (this.height() / 2)
        }
    },
    paint : function(parent) {
        if (!this.visible()) {
            return;
        }

        odr.snap(this);

        j("#" + this.extendedId()).remove();

        if (!parent) {
            parent = j("#" + odr.handleSettings.group);
        }

        odr._svg.circle(parent, this.x(), this.y(), odr.handleSettings.radius, {
            "class" : odr.handleSettings["class"],
            "id" : this.extendedId()
        });


        var element = j("#" + this.extendedId());
        odr.enableDragging(element);
        this.dragging(this._dragging.createDelegate(this), this.extendedId());
        this.dragEnd(this._dragEnd.createDelegate(this), this.extendedId());

        odr.assertContainerSize(this.extendedId());

        odr.Handle.superClass.draw.call(this);
    },
    repaint : function() {
        var element = j("#" + this.extendedId());

        if (!this.visible()) {
            element.remove();
            return;
        } else if (element.size() == 0) {
            this.paint();
            return;
        }

        element.attr("cx", this.x());
        element.attr("cy", this.y());

        odr.assertContainerSize(this.extendedId());

        odr.Handle.superClass.redraw.call(this);
    },
    visible : function(visible) {
        var previousVisibility = odr.Handle.superClass.visible.call(this);
        var returnValue = odr.Handle.superClass.visible.call(this, visible);
        var newVisibility = odr.Handle.superClass.visible.call(this);

        if (previousVisibility != newVisibility) {
            this.repaint();
        }

        return returnValue;
    },
    _dragging : function() {
        this.repaint();
    },
    _dragEnd : function() {
        odr.snapPosition(this);
        this.repaint();
    },
    dispose : function() {
        odr.Handle.superClass.remove.call(this);
    },
    extendedId : function() {
        return odr.handleSettings.idPrefix + this.id();
    }
}

extend(odr.Handle, odr.Shape);








/*
 * ###########################################################################
 *                              Line class
 */
odr.Line = function() {
    odr.DrawableItem.call(this);
    this._start = null;
    this._end = null;
}

odr.Line.prototype = {
    _start : null,
    _end : null,
    start : function(start) {
        if (start) {
            if (this._start) {
                this._start.unbind(odr.Callback.types.redraw, this.extendedId());
                this._start.unbind(odr.Callback.types.visibility, this.extendedId());
            }

            this._start = start;

            this._start.redraw(this.repaint.createDelegate(this), this.extendedId());
            this._start.visibility(this.endpointVisibilityChanged.createDelegate(this), this.extendedId());
            
            return this;
        }

        return this._start;
    },
    end : function(end) {
        if (end) {
            if (this._end) {
                this._end.unbind(odr.Callback.types.redraw, this.extendedId());
                this._end.unbind(odr.Callback.types.visibility, this.extendedId());
            }

            this._end = end;

            this._end.redraw(this.repaint.createDelegate(this), this.extendedId());
            this._end.visibility(this.endpointVisibilityChanged.createDelegate(this), this.extendedId());

            return this;
        }

        return this._end;
    },
    paint : function(parent) {
        j("#" + this.extendedId()).remove();


        odr._svg.line(parent,
            this._start.center().x,
            this._start.center().y,
            this._end.center().x,
            this._end.center().y,
            {
                "class" : odr.lineSettings["class"],
                "id" : this.extendedId()
            });

        odr.Line.superClass.draw.call(this);
    },
    repaint : function() {
        var element = j("#" + this.extendedId());

        element.attr("x1", this._start.center().x);
        element.attr("y1", this._start.center().y);
        element.attr("x2", this._end.center().x);
        element.attr("y2", this._end.center().y);

        odr.Line.superClass.redraw.call(this);
    },
    endpointVisibilityChanged : function() {
        if (!this._start.visible() || !this._end.visible()) {
            this.visible(false);
        }
    },
    dispose : function() {
        this._start.unbind(odr.Callback.types.redraw, this.extendedId());
        this._start.unbind(odr.Callback.types.visibility, this.extendedId());
        this._end.unbind(odr.Callback.types.redraw, this.extendedId());
        this._end.unbind(odr.Callback.types.visibility, this.extendedId());

        j("#" + this.extendedId()).remove();

        this.visible(false);

        odr.Line.superClass.dispose.call(this);
    },
    extendedId : function() {
        return odr.lineSettings.idPrefix + this.id();
    }
}

extend(odr.Line, odr.DrawableItem);






















/*
 * ###########################################################################
 *                              Line class
 */
odr.Association = function() {
    odr.DrawableItem.call(this);
    this._source = null;
    this._target = null;
    this._label = null;
    this._handles = [];
    this._lines = [];
}

odr.Association.prototype = {
    _source : null,
    _target : null,
    _label : null,
    _lines : [],
    source : function(source) {
        if (source) {
            if (this._source) {
                this._source.unbind(odr.Callback.types.redraw, this.extendedId());
                this._source.unbind(odr.Callback.types.visibility, this.extendedId());
            }

            this._source = source;

            this._source.redraw(this.repaint.createDelegate(this), this.extendedId());
            this._source.visibility(this.endpointVisibilityChanged.createDelegate(this), this.extendedId());

            return this;
        }

        return this._source;
    },
    target : function(target) {
        if (target) {
            if (this._target) {
                this._target.unbind(odr.Callback.types.redraw, this.extendedId());
                this._target.unbind(odr.Callback.types.visibility, this.extendedId());
            }

            this._target = target;

            this._target.redraw(this.repaint.createDelegate(this), this.extendedId());
            this._target.visibility(this.endpointVisibilityChanged.createDelegate(this), this.extendedId());

            return this;
        }

        return this._target;
    },
    addHandle : function(handle) {
        this._handles[this._handles.length] = handle;
        handle.parent(this);
    },
    removeHandle : function(handleId) {
        for(var i = 0; i < this._handles.length; i++) {
            if (this._handles[i].id() == handleId) {
                this._handles.splice(i, 1);
            }
        }
    },
    label : function(label) {
        if (label) {
            this._label = label;
            return this;
        }

        return this._label;
    },
    paint : function(parent) {
        if (!this.visible() || !this._source.visible() || !this._target.visible()) {
            return;
        }


        j("#" + this.extendedId()).remove();
        this.removeAllLines();

        if (!parent && !this.parent()) {
            parent = j("#" + odr.associationSettings.group);
        } else if (!parent) {
            parent = this.parent();
        }

        var associationGroup = odr._svg.group(parent, this.extendedId(), {
            "class" : odr.associationSettings["class"]
        });

        var element = this._source;

        for (var i = 0; i < this._handles.length; i++) {
            var line = new odr.Line();
            line.parent(this);
            line.start(element);
            line.end(this._handles[i]);
            this._lines[this._lines.length] = line;
            line.paint(associationGroup);

            element = this._handles[i];
        }

        line = new odr.Line();
        line.start(element);
        line.end(this._target);
        this._lines[this._lines.length] = line;
        line.paint(associationGroup);

        this.setAllHandlesVisible(true);

        odr.Association.superClass.draw.call(this);
    },
    repaint : function() {
        var element = j("#" + this.extendedId());

        if (!this.visible() || !this.source().visible() || !this.target().visible()) {
            this.removeAllLines();
            element.remove();
            this.setAllHandlesVisible(false);
            return;
        } else if (element.size() == 0) {
            this.paint();
            return;
        }

        this.draw();

        odr.Association.superClass.redraw.call(this);
    },
    setAllHandlesVisible : function(visible) {
        for(var i = 0; i < this._handles.length; i++) {
            this._handles[i].visible(visible);
        }
    },
    removeAllLines : function() {
        for(var i = 0; i < this._lines; i++) {
            this._lines[i].dispose();
        }
        this._lines = [];
    },
    endpointVisibilityChanged : function() {
        this.repaint();
    },
    dispose : function() {
        odr.Association.superClass.remove.call(this);
    },
    extendedId : function() {
        return odr.associationSettings.idPrefix + this.id();
    }
}

extend(odr.Association, odr.DrawableItem);