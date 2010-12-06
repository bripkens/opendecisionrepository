/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */





/*
 * ###########################################################################
 *                            Entity initialization
 */
odr.init(function() {
    odr.registry = new odr.Registry();
});






/**
 * @class A helper class that implements the <a href="http://martinfowler.com/eaaCatalog/registry.html">Registry pattern</a>.
 * @description Every entity automatically registers itself with the registry so that the entity can be retrieved by it's id.
 */
odr.Registry = function() {
    this._items = {};
}
odr.Registry.prototype = {
    _items : null,
    /**
     * @param {odr.DrawableItem} item The item you whish to add to the Registry. The key by which the item can later on
     * be removed or get is the result of the {@link odr.DrawableItem#id()} call.
     */
    add : function(item) {
        this._items[item.id()] = item;
    },
    /**
     * @param {{String|Number}} itemId
     * @return {odr.DrawableItem}
     */
    get : function(itemId) {
        return this._items[itemId];
    },
    /** @param {{String|Number}} itemId*/
    remove : function(itemId) {
        delete this._items[itemId];
    }
}





/**
 * @constructor
 *
 * @class
 * A useful base class that allows to attach listeners to drawable items
 *
 * @abstract
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

/**
 * @namespace
 * Can be used in combination with {@link odr.Callback#unbind} as this object literal defines
 * the event types.
 */
odr.Callback.types = {
    /** @field */
    draw : "draw",
    /** @field */
    redraw : "redraw",
    /** @field */
    remove : "remove",
    /** @field */
    dragStart : "dragStart",
    /** @field */
    dragging : "dragging",
    /** @field */
    dragEnd : "dragEnd",
    /** @field */
    visibility : "visibility"
}

odr.Callback.prototype = {
    _listeners : null,
    /**
     * @description
     * Attach a listener to the draw event or fire this event by calling this method without parameters.
     * Generally, the event will be fired after the item has been drawn.
     * @param {Function} [listener] The listener you want to attach
     * @param {String|Number} [identifier] Required when you want to attach a listener.
     */
    draw : function(listener, identifier) {
        this.handle(listener, identifier, this._listeners.draw);
    },
    /**
     * @description
     * Attach a listener to the redraw event or fire this event by calling this method without parameters.
     * Generally, the event will be fired after the item has been redrawn.
     * @param {Function} [listener] The listener you want to attach
     * @param {String|Number} [identifier] Required when you want to attach a listener.
     */
    redraw : function(listener, identifier) {
        this.handle(listener, identifier, this._listeners.redraw);
    },
    /**
     * @description
     * Attach a listener to the remove event or fire this event by calling this method without parameters.
     * Generally, the event will be fired after the item has been removed.
     * @param {Function} [listener] The listener you want to attach
     * @param {String|Number} [identifier] Required when you want to attach a listener.
     */
    remove : function(listener, identifier) {
        this.handle(listener, identifier, this._listeners.remove);
    },
    /**
     * @description
     * Attach a listener to the dragStart event or fire this event by calling this method without parameters.
     * Generally, the event will be fired when a user clicks on an item and when dragging is enabled for the item.
     * @param {Function} [listener] The listener you want to attach
     * @param {String|Number} [identifier] Required when you want to attach a listener.
     */
    dragStart : function(listener, identifier) {
        this.handle(listener, identifier, this._listeners.dragStart);
    },
    /**
     * @description
     * Attach a listener to the dragging event or fire this event by calling this method without parameters.
     * Generally, the event will be fired when a user clicks on an item that has dragging enabled and when he then
     * moves the mouse. Please be aware that this event will be called hundreds of times!
     * @param {Function} [listener] The listener you want to attach
     * @param {String|Number} [identifier] Required when you want to attach a listener.
     */
    dragging : function(listener, identifier) {
        this.handle(listener, identifier, this._listeners.dragging);
    },
    /**
     * @description
     * Attach a listener to the dragEndt event or fire this event by calling this method without parameters.
     * Generally, the event will be fired when an item was previously dragged and when the mouse button is now released.
     * @param {Function} [listener] The listener you want to attach
     * @param {String|Number} [identifier] Required when you want to attach a listener.
     */
    dragEnd : function(listener, identifier) {
        this.handle(listener, identifier, this._listeners.dragEnd);
    },
    /**
     * @description
     * Attach a listener to the visibility event or fire this event by calling this method without parameters.
     * Generally, the event will be fired after the {@link odr.DrawableItem#visible()} has been called.
     * @param {Function} [listener] The listener you want to attach
     * @param {String|Number} [identifier] Required when you want to attach a listener.
     */
    visibility : function(listener, identifier) {
        this.handle(listener, identifier, this._listeners.visibility);
    },
    /**
     * @private
     */
    handle : function(listener, identifier, collection) {
        if (listener && identifier) {
            collection[identifier] = listener;
            return;
        }

        for(var i in collection) {
            collection[i]();
        }
    },
    /**
     * @description
     * Unbind an event listener by specifying the event type and the identifier that was used when attaching the listener
     * to the event.
     * @param {String} type The event type
     * @param {String|Number} identifier The identifier which was used.
     * @see odr.Callback.types
     */
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







/**
 * @constructor
 *
 * @extends odr.Callback
 *
 * @class
 * Base class for all entities that can be drawn. A class that inherits from this class states that it can be
 * drawn and that it knows how to draw itself.
 * 
 * @abstract
 * 
 * @description Every entity automatically registers itself with the {@link odr.Registry} so that the entity can be retrieved by it's id.
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
    /**
     * @description
     * Draw the item to the canvas. Every implementation should call the {@link odr.Callback.draw} method afterwards.
     *
     * @param {DOM element|jQuery element} [parent] An optional parent element. In the resulting SVG, the drawn item
     * will be a child of the parent item. Can be used to group elements. If ommited, the SVG root element will be the
     * parent element.
     */
    paint : function(parent) {
        this.draw();
    },
    /**
     * @description
     * Redraw the item. Most items will use an optimized algorithm that only modifies the location of the item.
     * Therefore this method should be called when dragging an item.
     */
    repaint : function() {
        this.redraw();
    },
    /**
     * @description
     * Release all resources, remove the item from the canvas and remove the item from the registry. Also, all
     * listeners should be detached.
     */
    dispose : function() {
        odr.registry.remove(this._id);
        this.remove();
    },
    /**
     * @description
     * You can attach an arbitrary value with each DrawableItem. This value may be everything but the most useful
     * way to use it is to store the data which was used to generate this DrawableItem (like JSON).
     *
     * @param {Object} [value] The value you want to attach or nothing
     * @return {Object|odr.DrawableItem} The value which was set or null if no value was set. If you call this method
     * with a parameter then the method will return the object on which you called the method
     * (<a href="http://martinfowler.com/bliki/FluentInterface.html">Fluent Interface</a>)
     */
    value : function(value) {
        if (value) {
            this._value = value;
            return this;
        }

        return this._value;
    },
    /**
     * @description
     * Define a parent element for this DrawableItem. The parent element can act as a grouping element or container for
     * it's child element. This value can temporarily be overriden by the parent parameter in {@link odr.DrawableItem#paint}.
     *
     * @param {DOM element|jQuery element} [parent] The parent element
     * @return {DOM element|jQuery element|odr.DrawableItem} The parent which was set or null if no parent was set. If
     * you call this method with a parameter then the method will return the object on which you called the method
     * (<a href="http://martinfowler.com/bliki/FluentInterface.html">Fluent Interface</a>)
     */
    parent : function(parent) {
        if (parent) {
            this._parent = parent;
            return this;
        }

        return this._parent;
    },
    /**
     * @return Always returns the automatically generated ID of this DrawableItem
     */
    id : function() {
        return this._id;
    },
    /**
     * @description
     * Show or hide this DrawableItem. When changing the visibility of an iten it will be painted or removed, depending
     * on the new visiblity.
     *
     * Initially each DrawableItem is visible.
     *
     * @param {Boolean} [visible] The new visibility
     * @return {Boolean|odr.DrawableItem} The visibility which was set or null if no parent was set. If you call this
     * method with a parameter then the method will return the object on which you called the method
     * (<a href="http://martinfowler.com/bliki/FluentInterface.html">Fluent Interface</a>).
     */
    visible : function(visible) {
        if (visible != undefined && this._visible != visible) {
            this._visible = visible;
            this.visibility();
            return this;
        }
        
        return this._visible;
    },
    /**
     * @description
     * The extended id is the id which is used in the SVG. Besides the normal id which you can retrieve by calling
     * {@link odr.DrawableItem.id} it also returns some meta information. Mostly this is an id prefix.
     *
     * @return {String} The extended id
     */
    extendedId : function() {
        throw("Abstract method");
    }
}

extend(odr.DrawableItem, odr.Callback);







/**
 * @constructor
 *
 * @extends odr.DrawableItem
 *
 * @class
 * Base class for all drawable items that cover an area, can therefore be clicked and need to be taken into account
 * when the canvas is resized.
 *
 * @abstract
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
    /**
     * @description
     * Retrieve the center position of the shape. The center is used for associations as they always target the center
     * of a shape.
     * @return {Object literal} The center of the shape. The object literal has x and y properties.
     */
    center : function() {
        throw("Abstract method");
    },
    /**
     * @description
     * Retrieve the position of the top left corner of the shape. This information is required when changing the size
     * of the canvas.
     * @return {Object literal} Top left corner of the shape. The object literal has x and y properties.
     */
    topLeft : function() {
        throw("Abstract method");
    },
    /**
     * @description
     * Retrieve the position of the bottom right corner of the shape. This information is required when changing the size
     * of the canvas.
     * @return {Object literal} Bottom right corner of the shape. The object literal has x and y properties.
     */
    bottomRight : function() {
        throw("Abstract method");
    },
    /**
     * @param {Number} [x] The new x coordinate
     * @return {Number|odr.DrawableItem} The x coordinate which was set. If you call this method with a parameter then
     * the method will return the object on which you called the method
     * (<a href="http://martinfowler.com/bliki/FluentInterface.html">Fluent Interface</a>)
     */
    x : function(x) {
        if (x) {
            this._x = x;
            return this;
        }

        return this._x;
    },
    /**
     * @param {Number} [y] The new y coordinate
     * @return {Number|odr.DrawableItem} The y coordinate which was set. If you call this method with a parameter then
     * the method will return the object on which you called the method
     * (<a href="http://martinfowler.com/bliki/FluentInterface.html">Fluent Interface</a>)
     */
    y : function(y) {
        if (y) {
            this._y = y;
            return this;
        }

        return this._y;
    },
    /**
     * @param {Number} [width] The new width
     * @return {Number|odr.DrawableItem} The width which was set. If you call this method with a parameter then the
     * method will return the object on which you called the method
     * (<a href="http://martinfowler.com/bliki/FluentInterface.html">Fluent Interface</a>)
     */
    width : function(width) {
        if (width) {
            this._width = width;
            return this;
        }

        return this._width;
    },
    /**
     * @param {Number} [height] The new height
     * @return {Number|odr.DrawableItem} The height which was set. If you call this method with a parameter then the
     * method will return the object on which you called the method
     * (<a href="http://martinfowler.com/bliki/FluentInterface.html">Fluent Interface</a>)
     */
    height : function(height) {
        if (height) {
            this._height = height;
            return this;
        }

        return this._height;
    }
}

extend(odr.Shape, odr.DrawableItem);








/**
 * @constructor
 *
 * @extends odr.Shape
 *
 * @class
 * An endpoint of an association.
 *
 * @abstract
 */
odr.Endpoint = function() {
    odr.Shape.call(this);
    this._label = null;
}

odr.Endpoint.prototype = {
    _label : null,
    /**
     * @param {String|Number} [label] The new label
     * @return {String|Number|odr.DrawableItem} The label which was set or null if no label was set.
     * If you call this method with a parameter then the method will return the object on which you called the
     * method (<a href="http://martinfowler.com/bliki/FluentInterface.html">Fluent Interface</a>)
     */
    label : function(label) {
        if(label) {
            this._label = label;

            return this;
        }
        return this._label;
    }
}

extend(odr.Endpoint, odr.Shape);









/**
 * @constructor
 *
 * @extends odr.Endpoint
 *
 * @class
 * The rectangle class can be used represent decisions and versions
 */
odr.Rectangle = function() {
    odr.Endpoint.call(this);
    this._stereotype = null;
    this._round = true;
    this._extraClasses = "";
}

odr.Rectangle.prototype = {
    _stereotype : null,
    _round : true,
    _extraClasses : "",
    extraClasses : function(extraClasses) {
        if (extraClasses != undefined) {
            this._extraClasses = extraClasses;
            return this;
        }

        return this._extraClasses;
    },
    /** @private */
    round : function(round) {
        if (round != undefined) {
            this._round = round;
            return this;
        }

        return this._round;
    },
    stereotype : function(stereotype) {
        if (stereotype) {
            this._stereotype = stereotype;
            this.calculateDimensions();
            return this;
        }

        return this._stereotype;
    },
    /** @private */
    center : function() {
        return {
            x : this.x() + this.width() / 2,
            y : this.y() + this.height() / 2
        };
    },
    /** @private */
    topLeft : function() {
        return {
            x : this.x(),
            y : this.y()
        }
    },
    /** @private */
    bottomRight : function() {
        return {
            x : this.x() + this.width(),
            y : this.y() + this.height()
        }
    },
    /** @private */
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

        var rx = 0, ry = 0;

        if (this._round) {
            rx = odr.rectangleSettings.rx;
            ry = odr.rectangleSettings.ry;
        }

        odr._svg.rect(group, this.x(), this.y(), this.width(),
            this.height(), rx, ry, {
                "class" : odr.rectangleSettings.background["class"] + " " + this._extraClasses,
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
            this.height(), rx, ry, {
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
    /** @private */
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
    /** @private */
    visible : function(visible) {
        var previousVisibility = odr.Rectangle.superClass.visible.call(this);
        var returnValue = odr.Rectangle.superClass.visible.call(this, visible);
        var newVisibility = odr.Rectangle.superClass.visible.call(this);

        if (previousVisibility != newVisibility) {
            this.repaint();
        }

        return returnValue;
    },
    /** @private */
    _dragging : function() {
        this.repaint();
    },
    /** @private */
    _dragEnd : function() {
        odr.snapPosition(this);
        this.repaint();
    },
    /** @private */
    _click : function(e) {
        if(e.ctrlKey) {
            this.visible(false);

            return false;
        }

        return true;
    },
    /** @private */
    dispose : function() {
        odr.Rectangle.superClass.remove.call(this);
    },
    /** @private */
    extendedId : function() {
        return odr.rectangleSettings.idPrefix + this.id();
    },
    /** @private */
    backgroundId : function() {
        return odr.rectangleSettings.background.idPrefix + this.id();
    },
    /** @private */
    textId : function() {
        return odr.rectangleSettings.text.idPrefix + this.id();
    },
    /** @private */
    overlayId : function() {
        return odr.rectangleSettings.overlay.idPrefix + this.id();
    },
    /** @private */
    stereotypeId : function() {
        return odr.rectangleSettings.stereotype.idPrefix + this.id();
    },
    /** @private */
    label : function(label) {
        var result = odr.Rectangle.superClass.label.call(this, label);

        if (!label) {
            return result;
        }

        this.calculateDimensions();

        return result;
    },
    /** @private */
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









/**
 * @constructor
 *
 * @extends odr.Shape
 *
 * @class
 * A drag handle for associations
 */
odr.Handle = function() {
    odr.Shape.call(this);
}

odr.Handle.prototype = {
    /** @private */
    center : function() {
        return {
            x : this.x(),
            y : this.y()
        };
    },
    /** @private */
    topLeft : function() {
        return {
            x : this.x() - (this.width() / 2),
            y : this.y() - (this.height() / 2)
        }
    },
    /** @private */
    bottomRight : function() {
        return {
            x : this.x() + (this.width() / 2),
            y : this.y() + (this.height() / 2)
        }
    },
    /** @private */
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
    /** @private */
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
    /** @private */
    visible : function(visible) {
        var previousVisibility = odr.Handle.superClass.visible.call(this);
        var returnValue = odr.Handle.superClass.visible.call(this, visible);
        var newVisibility = odr.Handle.superClass.visible.call(this);

        if (previousVisibility != newVisibility) {
            this.repaint();
        }

        return returnValue;
    },
    /** @private */
    _dragging : function() {
        this.repaint();
    },
    /** @private */
    _dragEnd : function() {
        odr.snapPosition(this);
        this.repaint();
    },
    /** @private */
    _click : function(e) {
        if(e.ctrlKey) {
            this.parent().removeHandle(this.id());
            this.dispose();
            this.parent().paint();

            return false;
        }

        return true;
    },
    /** @private */
    dispose : function() {
        j("#" + this.extendedId()).remove();
        odr.Handle.superClass.remove.call(this);
    },
    /** @private */
    extendedId : function() {
        return odr.handleSettings.idPrefix + this.id();
    }
}

extend(odr.Handle, odr.Shape);








/**
 * @constructor
 *
 * @extends odr.DrawableItem
 *
 * @class
 * A simple line that can be drawn between two Shapes
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
    /** @private */
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
    /** @private */
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
    /** @private */
    repaint : function() {
        this.paint(this._lastParent);
    },
    /** @private */
    endpointVisibilityChanged : function() {
        if (!this._start.visible() || !this._end.visible()) {
            this.visible(false);
        }
    },
    /** @private */
    _click : function(e) {
        var handle = new odr.Handle();
        handle.x(e.pageX  * (1 / odr._scale.level));
        handle.y(e.pageY  * (1 / odr._scale.level));
        handle.paint();

        this.parent().addHandleAfter(this._start, handle);
    },
    /** @private */
    dispose : function() {
        this._start.unbind(odr.Callback.types.redraw, this.extendedId());
        this._start.unbind(odr.Callback.types.visibility, this.extendedId());
        this._end.unbind(odr.Callback.types.redraw, this.extendedId());
        this._end.unbind(odr.Callback.types.visibility, this.extendedId());

        j("#" + this.extendedId()).remove();

        this.visible(false);

        odr.Line.superClass.dispose.call(this);
    },
    /** @private */
    center : function() {
        var start = this._start.center();
        var end = this._end.center();

        return {
            x : (start.x + end.x) / 2,
            y : (start.y + end.y) / 2
        };
    },
    /** @private */
    extendedId : function() {
        return odr.lineSettings.idPrefix + this.id();
    },
    /** @private */
    arrow : function(arrow) {
        if (arrow != undefined) {
            this._arrow = arrow;
            return this;
        }

        return this._arrow;
    }
}

extend(odr.Line, odr.DrawableItem);






















/**
 * @constructor
 *
 * @extends odr.DrawableItem
 *
 * @class
 * An association between two endpoints.
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
    /** @private */
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
    /** @private */
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
    /** @private */
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
    /** @private */
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
    /** @private */
    setAllHandlesVisible : function(visible) {
        for(var i = 0; i < this._handles.length; i++) {
            this._handles[i].visible(visible);
        }
    },
    /** @private */
    removeAllLines : function() {
        for(var i = 0; i < this._lines; i++) {
            this._lines[i].dispose();
        }
        this._lines = [];
    },
    /** @private */
    endpointVisibilityChanged : function() {
        this.repaint();
    },
    /** @private */
    dispose : function() {
        odr.Association.superClass.remove.call(this);
    },
    /** @private */
    extendedId : function() {
        return odr.associationSettings.idPrefix + this.id();
    }
}

extend(odr.Association, odr.DrawableItem);