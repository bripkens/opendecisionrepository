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
    this._stereotype = null;
}

odr.Rectangle.prototype = {
    _stereotype : null,
    stereotype : function(stereotype) {
        if (stereotype) {
            this._stereotype = stereotype;
            this.calculateDimensions();
            return this;
        }

        return this._stereotype;
    },
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

        var group = odr._svg.group(parent, this.extendedId(), {
            "class" : odr.rectangleSettings["class"],
            "id" : this.extendedId()
        })

        odr._svg.rect(group, this.x(), this.y(), this.width(),
            this.height(), odr.rectangleSettings.rx, odr.rectangleSettings.ry, {
                "class" : odr.rectangleSettings.background["class"],
                "id" : this.backgroundId()
            });

        var text = this.label();
        var stereotype = this.stereotype();
        if (stereotype) {
            var stereotypeDimensions = odr.meassureTextDimensions(this.stereotype(), odr.rectangleSettings.stereotype.measureCSS);

            var textSpans = odr._svg.createText().span(stereotype, {
                dx : this.width() / 2 - stereotypeDimensions.width / 2,
                dy : odr.rectangleSettings.padding.top
            });

            parent = j("#" + odr.textSettings.group);

            odr._svg.text(group, this.x(), this.y(), textSpans, {
                "class" : odr.rectangleSettings.stereotype["class"],
                "id" : this.stereotypeId()
            });
        }

        if (text) {

            var labelDimensions = odr.meassureTextDimensions(this.label(), odr.rectangleSettings.text.measureCSS);

            var stereotypeOffset = 0;

            if (stereotype) {
                stereotypeOffset = odr.meassureTextDimensions(this.stereotype(),
                    odr.rectangleSettings.stereotype.measureCSS).height + odr.rectangleSettings.text.stereotypePadding;
            }

            var textSpans = odr._svg.createText().span(text, {
                dx : this.width() / 2 - labelDimensions.width / 2,
                dy : odr.rectangleSettings.padding.top + stereotypeOffset
            });

            parent = j("#" + odr.textSettings.group);

            odr._svg.text(group, this.x(), this.y(), textSpans, {
                "class" : odr.rectangleSettings.text["class"],
                "id" : this.textId()
            });
        }

        odr._svg.rect(group, this.x(), this.y(), this.width(),
            this.height(), odr.rectangleSettings.rx, odr.rectangleSettings.ry, {
                "class" : odr.rectangleSettings.overlay["class"],
                "id" : this.overlayId()
            });

        var element = j("#" + this.overlayId());
        element.click(this._click.createDelegate(this));
        odr.enableDragging(element);
        this.dragging(this._dragging.createDelegate(this), this.extendedId());
        this.dragEnd(this._dragEnd.createDelegate(this), this.extendedId());

        odr.assertContainerSize(this.extendedId());

        odr.Rectangle.superClass.draw.call(this);
    },
    repaint : function() {
        var backgroundElement = j("#" + this.backgroundId());
        var textElement = j("#" + this.textId());
        var stereotypeElement = j("#" + this.stereotypeId());
        var overlayElement = j("#" + this.overlayId());
        
        if (!this.visible()) {
            j("#" + this.extendedId()).remove();
            return;
        } else if (j("#" + this.extendedId()).size() == 0) {
            this.paint();
            return;
        }

        backgroundElement.attr("x", this.x());
        backgroundElement.attr("y", this.y());
        overlayElement.attr("x", this.x());
        overlayElement.attr("y", this.y());
        textElement.attr("x", this.x());
        textElement.attr("y", this.y());
        stereotypeElement.attr("x", this.x());
        stereotypeElement.attr("y", this.y());

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
    _click : function(e) {
        if(e.ctrlKey) {
            this.visible(false);

            return false;
        }

        return true;
    },
    dispose : function() {
        odr.Rectangle.superClass.remove.call(this);
    },
    extendedId : function() {
        return odr.rectangleSettings.idPrefix + this.id();
    },
    backgroundId : function() {
        return odr.rectangleSettings.background.idPrefix + this.id();
    },
    textId : function() {
        return odr.rectangleSettings.text.idPrefix + this.id();
    },
    overlayId : function() {
        return odr.rectangleSettings.overlay.idPrefix + this.id();
    },
    stereotypeId : function() {
        return odr.rectangleSettings.stereotype.idPrefix + this.id();
    },
    label : function(label) {
        var result = odr.Rectangle.superClass.label.call(this, label);

        if (!label) {
            return result;
        }

        this.calculateDimensions();

        return result;
    },
    calculateDimensions : function() {
        var labelDimensions;
        if (this.label() == null) {
            labelDimensions = {
                width : 0,
                height : 0
            };
        } else {
            labelDimensions = odr.meassureTextDimensions(this.label(), odr.rectangleSettings.text.measureCSS);
        }

        var stereoTypeDimensions;

        if (this.stereotype() == null) {
            stereoTypeDimensions = {
                width : 0,
                height : 0
            };
        } else {
            stereoTypeDimensions = odr.meassureTextDimensions(this.stereotype(), odr.rectangleSettings.stereotype.measureCSS);
            stereoTypeDimensions.height += odr.rectangleSettings.text.stereotypePadding;
        }

        var x = Math.max(labelDimensions.width, stereoTypeDimensions.width);
        var y = labelDimensions.height + stereoTypeDimensions.height;

        this.width(x + odr.rectangleSettings.padding.left + odr.rectangleSettings.padding.right);
        this.height(y + odr.rectangleSettings.padding.top + odr.rectangleSettings.padding.bottom);
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
        element.click(this._click.createDelegate(this));
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
    _click : function(e) {
        if(e.ctrlKey) {
            this.parent().removeHandle(this.id());
            this.dispose();
            this.parent().paint();

            return false;
        }

        return true;
    },
    dispose : function() {
        j("#" + this.extendedId()).remove();
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
    this._arrow = false;
    this._lastParent = null;
}

odr.Line.prototype = {
    _start : null,
    _end : null,
    _arrow : false,
    _lastParent : null,
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
        this._lastParent = parent;
        j("#" + this.extendedId()).remove();

        var settings = {
            "class" : odr.lineSettings["class"],
            "id" : this.extendedId()
        };

        if (this._arrow) {
            this.drawArrow(parent);
            settings["marker-end"] = "url(#" + odr.associationSettings.arrow.idPrefix + this.parent().id() + ")"
        }

        odr._svg.line(parent,
            this._start.center().x,
            this._start.center().y,
            this._end.center().x,
            this._end.center().y, settings);

        var element = j("#" + this.extendedId());
        element.click(this._click.createDelegate(this));

        odr.Line.superClass.draw.call(this);
    },
    drawArrow : function(parent) {
        j("#" + odr.associationSettings.arrow.idPrefix + this.parent().id()).remove();
        
        var centerTarget = this._end.center();
        var centerSource = this._start.center();
        var deltaX = Math.abs(Math.abs(centerTarget.x) - Math.abs(centerSource.x));
        var deltaY = Math.abs(Math.abs(centerTarget.y) - Math.abs(centerSource.y));

        // check from which side the line will hit the node
        var left = centerSource.x < centerTarget.x;
        var top = centerSource.y < centerTarget.y;
        var tanSideAngle = (this._end.height() / 2) / (this._end.width() / 2);
        var reachableY = tanSideAngle * deltaX;
        var hitsSide = deltaY < reachableY;

        
        // calculate the point
        var offsetX;
        var offsetY;

        if (hitsSide) {
            var tanAngleBetweenStartAndEnd = deltaY / deltaX;

            offsetX = (this._end.width() / 2);
            if (left) {
                offsetX *= -1;
            }

            offsetY = offsetX * tanAngleBetweenStartAndEnd;

            if (!top && left) {
                offsetY *= -1;
            } else if (top && !left) {
                offsetY *= -1;
            }
        } else {
            var tanAngleBetweenStartAndEnd = deltaX / deltaY;

            offsetY = (this._end.height() / 2);
            if (top) {
                offsetY *= -1;
            }

            offsetX = offsetY * tanAngleBetweenStartAndEnd;

            if (top && !left) {
                offsetX *= -1;
            } else if (!top && left) {
                offsetX *= -1;
            }
        }

        var offsetDiagonal = Math.sqrt(Math.pow(offsetX, 2) + Math.pow(offsetY, 2));

        var defs = j("#" + odr.defsSettings.id);

        var marker = odr._svg.marker(defs, odr.associationSettings.arrow.idPrefix + this.parent().id(),
            offsetDiagonal  + 10, 5, 10, 10, {
                fill : odr.associationSettings.arrow.color,
                orient : "auto",
                viewBox : "0 0 10 10"
            });

        odr._svg.path(marker, odr._svg.createPath().move(0,0).line(10,5).line(0,10).close());

    },
    repaint : function() {
        this.paint(this._lastParent);
    },
    endpointVisibilityChanged : function() {
        if (!this._start.visible() || !this._end.visible()) {
            this.visible(false);
        }
    },
    _click : function(e) {
        var handle = new odr.Handle();
        handle.x(e.pageX  * (1 / odr._scale.level));
        handle.y(e.pageY  * (1 / odr._scale.level));
        handle.paint();

        this.parent().addHandleAfter(this._start, handle);
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
    center : function() {
        var start = this._start.center();
        var end = this._end.center();

        return {
            x : (start.x + end.x) / 2,
            y : (start.y + end.y) / 2
        };
    },
    extendedId : function() {
        return odr.lineSettings.idPrefix + this.id();
    },
    arrow : function(arrow) {
        if (arrow != undefined) {
            this._arrow = arrow;
            return this;
        }

        return this._arrow;
    }
}

extend(odr.Line, odr.DrawableItem);






















/*
 * ###########################################################################
 *                              Association class
 */
odr.Association = function() {
    odr.DrawableItem.call(this);
    this._source = null;
    this._target = null;
    this._label = null;
    this._handles = [];
    this._lines = [];
    this._labelPosition = null;
    this._centerPosition = null;
}

odr.Association.prototype = {
    _source : null,
    _target : null,
    _label : null,
    _lines : [],
    _handles : [],
    _labelPosition : null,
    _centerPosition : null,
    source : function(source) {
        if (source) {
            if (this._source) {
                this._source.unbind(odr.Callback.types.redraw, this.extendedId());
                this._source.unbind(odr.Callback.types.visibility, this.extendedId());
                this._source.unbind(odr.Callback.types.dragEnd, this.extendedId());
                this._source.parent(undefined);
            }

            this._source = source;
            this._source.parent(this);
            this._source.redraw(this.repaint.createDelegate(this), this.extendedId());
            this._source.dragEnd(this.optimizePath.createDelegate(this), this.extendedId());
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
                this._target.unbind(odr.Callback.types.dragEnd, this.extendedId());
                this._target.parent(undefined);
            }

            this._target = target;
            this._target.parent(this);
            this._target.redraw(this.repaint.createDelegate(this), this.extendedId());
            this._target.dragEnd(this.optimizePath.createDelegate(this), this.extendedId());
            this._target.visibility(this.endpointVisibilityChanged.createDelegate(this), this.extendedId());

            return this;
        }

        return this._target;
    },
    addHandle : function(handle) {
        this._handles[this._handles.length] = handle;
        handle.parent(this);
        handle.dragEnd(this.optimizePath.createDelegate(this), this.extendedId());
    },
    removeHandle : function(handleId) {
        for(var i = 0; i < this._handles.length; i++) {
            if (this._handles[i].id() == handleId) {
                this._handles[i].dispose();
                this._handles[i].unbind(odr.Callback.types.dragEnd, this.extendedId());
                this._handles.splice(i, 1);
            }
        }
    },
    addHandleAfter : function(element, handle) {
        handle.parent(this);

        if (this.source().id() == element.id()) {
            this._handles.splice(0, 0, handle);
            handle.dragEnd(this.optimizePath.createDelegate(this), this.extendedId());
            this.paint();
            return;
        }

        for(var i = 0; i < this._handles.length; i++) {
            if (this._handles[i].id() == element.id()) {
                handle.dragEnd(this.optimizePath.createDelegate(this), this.extendedId());
                if (i+1 == this._handles.length) {
                    this._handles[this._handles.length] = handle;
                } else {
                    this._handles.splice(i+1, 0, handle);
                }
                this.paint();
                return;
            }
        }
    },
    handles : function() {
        return this._handles;
    },
    optimizePath : function() {
        var firstX = this._source.center().x;
        var firstY = this._source.center().y;
        var secondX = undefined;
        var secondY = undefined;
        var thirdX = undefined;
        var thirdY = undefined;

        var nodeRemoved = false;

        for(var i = 0; i < this._handles.length; i++) {
            if (secondX == undefined) {
                secondX = this._handles[i].center().x;
                secondY = this._handles[i].center().y;
                continue;
            }

            thirdX = this._handles[i].center().x;
            thirdY = this._handles[i].center().y;


            if ((firstX == secondX && secondX == thirdX) || (firstY == secondY && secondY == thirdY)) {
                nodeRemoved = true;
                this.removeHandle(this._handles[i-1].id());
            }

            firstX = secondX;
            firstY = secondY;
            secondX = thirdX;
            secondY = thirdY;
        }

        if (secondX == undefined) {
            this.calculateCenter();
            return;
        }

        thirdX = this._target.center().x;
        thirdY = this._target.center().y;

        if ((firstX == secondX && secondX == thirdX) || (firstY == secondY && secondY == thirdY)) {
            nodeRemoved = true;
            this.removeHandle(this._handles[this._handles.length - 1].id());
        }

        
        if (nodeRemoved) {
            this.paint();
        } else {
            this.calculateCenter();
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
            this._handles[i].paint();

            element = this._handles[i];
        }

        line = new odr.Line();
        line.parent(this);
        line.start(element);
        line.end(this._target);
        line.arrow(true);
        this._lines[this._lines.length] = line;
        line.paint(associationGroup);

        this.setAllHandlesVisible(true);
        this.calculateCenter();
        this.drawAssociationLabel(associationGroup);

        odr.Association.superClass.draw.call(this);
    },
    calculateCenter : function() {
        if (this._handles.length == 0) {
            var start = this._source.center();
            var end = this._target.center();

            this._centerPosition = {
                x : (start.x + end.x) / 2,
                y : (start.y + end.y) / 2
            };
        } else if (this._handles.length % 2 != 0) {
            this._centerPosition = this._handles[parseInt(this._handles.length / 2)].center();
        } else {
            var center = parseInt(this._handles.length / 2);
            var start = this._handles[center - 1].center();
            var end = this._handles[center].center();

            this._centerPosition = {
                x : (start.x + end.x) / 2,
                y : (start.y + end.y) / 2
            };
        }
    },
    drawAssociationLabel : function(parent) {
        var point;
        var label = this.label();

        if (!label) {
            return;
        }

        this.calculateCenter();

        point = this._centerPosition;

        if (this._labelPosition != null) {
            point = this._labelPosition;
        } else {
            this._labelPosition = point;
        }

        
        odr._svg.text(parent, point.x, point.y, label, {
            "class" : odr.associationSettings.text["class"],
            "id" : odr.associationSettings.text.idPrefix + this.id()
        });
        
        var textDimensions = odr.meassureTextDimensions(label, odr.associationSettings.text.measureCSS);
        var self = this;
        var htmlElement = j("#" + odr.associationSettings.text.idPrefix + this.id());
        var element = {
            self : self,
            id : function() {
                return odr.associationSettings.text.idPrefix + self.id();
            },
            topLeft : function() {
                return {
                    x : this.x(),
                    y : this.y()
                }
            },
            bottomRight : function() {
                return {
                    x : this.x() + textDimensions.width,
                    y : this.y() + textDimensions.height
                }
            },
            extendedId : function() {
                return this.id();
            },
            x : function(x) {
                if (x) {
                    self._labelPosition.x = x;

                    htmlElement.attr("x", x);

                    return this;
                }

                return self._labelPosition.x;
            },
            y : function(y) {
                if (y) {
                    self._labelPosition.y = y;

                    htmlElement.attr("y", y);

                    return this;
                }

                return self._labelPosition.y;
            },
            dragStart : function() {

            },
            dragging : function() {
                var helpLine = j("#" + odr.associationSettings.helper.id);

                self.calculateCenter();

                helpLine.attr("x1", self._labelPosition.x);
                helpLine.attr("y1", self._labelPosition.y);
                helpLine.attr("x2", self._centerPosition.x);
                helpLine.attr("y2", self._centerPosition.y);
            },
            dragEnd : function() {
                
            },
            enter : function(e) {
                var lineGroup = j("#" + odr.lineSettings.group);

                

                odr._svg.line(lineGroup, this._labelPosition.x, this._labelPosition.y,
                    this._centerPosition.x, this._centerPosition.y, {
                        id : odr.associationSettings.helper.id,
                        "class" : odr.associationSettings.helper["class"]
                    });
            },
            out : function(e) {
                j("#" + odr.associationSettings.helper.id).remove();
            }
        };

        odr.registry.add(element);

        htmlElement.mouseenter(element.enter.createDelegate(this));

        htmlElement.mouseleave(element.out);

        odr.registry.add(element);

        odr.enableDragging(htmlElement);
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