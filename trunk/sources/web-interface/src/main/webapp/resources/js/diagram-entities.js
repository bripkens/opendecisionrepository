/**
 * @fileOverview
 *
 * This file contains all drawable items, e.g. nodes
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */





/*
 * ###############################################################################################################
 *                                              Registry
 */
/**
 * @class
 * A helper class that implements the <a href="http://martinfowler.com/eaaCatalog/registry.html">Registry pattern</a>.
 * 
 * @description
 * Every entity automatically registers itself with the registry so that the entity can be retrieved by it's id.
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
     * @param {String|Number} itemId
     * @return {odr.DrawableItem}
     */
    get : function(itemId) {
        return this._items[itemId];
    },
    /**
     * @param {{String|Number}} itemId
     */
    remove : function(itemId) {
        delete this._items[itemId];
    }
}

odr.bootstrap(function() {
    odr.registry = new odr.Registry();
});










/*
 * ###############################################################################################################
 *                                              Classes
 */
/**
 * @constructor
 *
 * @class
 * Base class for all entities that can be drawn. A class that inherits from this class states that it can be
 * drawn and that it knows how to draw itself.
 *
 * @abstract
 *
 * @description
 * Every drawable automatically registers itself with the {@link odr.Registry} so that the entity can be
 * retrieved by it's id.
 */
odr.Drawable = function() {
    this._id = this._idPrefix() + odr.newId();
    odr.registry.add(this);
    this._visible = true;
    this._parent = null;
    this._classes = [];

    this._listener = {};

    for(var listenerType in odr.Drawable.listener) {
        this._listener[odr.Drawable.listener[listenerType]] = {};
    }
}

/**
 * @namespace
 * Can be used in combination with {@link odr.Drawable#bind} and {@link odr.Drawable#unbind} as this object literal
 * defines the event types.
 */
odr.Drawable.listener = {
    /** @field */
    visibilityChanged : "visibilityChanged",
    /** @field */
    parentChanged : "parentChanged",
    /** @field */
    classesChanged : "classesChanged"
}

odr.Drawable.prototype = {
    _id : -1,
    _visible : true,
    /**
     * @private
     * @description
     * A parent for this item. Mostly this will be an html or svg element
     */
    _parent : null,
    /**
     * @private
     * @description
     * CSS classes for this drawable that will be applied upcon paint
     */
    _classes : [],
    _listener : {},



    /**
     * @private
     * @description
     * Get the prefix for the id
     */
    _idPrefix : function() {
        throw("Not implemented.");
    },


    
    /**
     * @description
     * Returns the full id of the drawable
     *
     * @return {Number|String} the id
     */
    id : function() {
        return this._id;
    },



    /**
     * @description
     * Show or hide this DrawableItem.
     *
     * Initially each DrawableItem is visible.
     *
     * You can also retrieve the current value by calling this method without parameters.
     *
     * @param {Boolean} [visible] The new visibility
     * @return {Boolean|odr.Drawable} The visibility which was set. If you call this
     * method with a parameter then the method will return the object on which you called the method.
     */
    visible : function(visible) {
        if (visible != undefined) {

            if (this._visible != visible) {
                this._visible = visible;

                this.fire(odr.Drawable.listener.visibilityChanged, [this]);
            }

            return this;
        }

        return this._visible;
    },







    /**
     * @description
     * Set the parent for this element. The parent element can act as a grouping element or container for
     * it's child elements..
     *
     * You can also retrieve the current value by calling this method without parameters.
     *
     * @param {HtmlElement|SvgElement} [parent] The new parent
     * @return {HtmlElement|SvgElement|odr.Drawable} The parent which was set. If you call this
     * method with a parameter then the method will return the object on which you called the method.
     */
    parent : function(parent) {
        if (parent != undefined) {

            if (this._parent != parent) {
                this._parent = parent;

                this.fire(odr.Drawable.listener.parentChanged, [this]);
            }

            return this;
        }

        return this._parent;
    },






    /**
     * @description
     * Add a class to this drawable
     *
     * @param {String} theClass the class which you want to add
     * @return {odr.Drawable} The object on which you called this method.
     */
    addClass : function(theClass) {

        if (!this.hasClass(theClass)) {
            this._classes[this._classes.length] = theClass;
            this.fire(odr.Drawable.listener.classesChanged, [this]);
        }
        
        return this._parent;
    },





    /**
     * @description
     * Remove a class from this drawable
     *
     * @param {String} theClass the class which you want to remove
     * @return {odr.Drawable} The object on which you called this method.
     */
    removeClass : function(theClass) {
        var index = this._classes.indexOf(theClass);

        if (index != -1) {
            this._classes.splice(index, 1);
            this.fire(odr.Drawable.listener.classesChanged, [this]);
        }
        
        return this._parent;
    },





    /**
     * @description
     * Check whether this drawable has a certain class
     *
     * @param {String} theClass the class for which you want to check the drawable
     * @return {Boolean} True when this drawable has the class, false otherwise.
     */
    hasClass : function(theClass) {
        return this._classes.indexOf(theClass) != -1;
    },





    /**
     * @description
     * Get a string that can be used for the html or svg class attribute.
     *
     * @return {String} All classes seperated by a white space character
     */
    classString : function() {
        return this._classes.join(" ");
    },





    /**
     * @description
     * Bind a listener to the given event type
     *
     * @param {String} type The type of listener that you want to bind to
     * @param {Function} listener The listener function
     * @param {Object} [identification] The identification that is used for binding the listener. There is only one
     * listener pre event type and identification!
     * @return {odr.Drawable} The object on which you called this function
     */
    bind : function(type, listener, identification) {
        var listenerCollection = this._listener[type];

        if (listenerCollection == undefined) {
            throw ("The specified listener type " + type + " is not valid.");
        }

        if (identification == undefined) {
            identification = listener;
        }

        listenerCollection[identification] = listener;

        return this;
    },





    /**
     * @description
     * Unbind a listener from the given event type
     *
     * @param {String} type The type of listener that you want to unbind from
     * @param {Object} identification The identification object that was used when the listener was bound.
     * You can pass the listener function as an identification when you didn't specified an identification when you
     * bound the listener.
     * @return {odr.Drawable} The object on which you called this function
     */
    unbind : function(type, identification) {
        var listenerCollection = this._listener[type];

        if (listenerCollection == undefined) {
            throw ("The specified listener type " + type + " is not valid.");
        }


        delete listenerCollection[identification];

        return this;
    },






    /**
     * @description
     * Fire an event with the given parameters
     *
     * @param {String} type The type of listener that you want to fire
     * @param {Object[]} [params] An array of objects that you want to pass to the listener as parameters
     * @return {odr.Drawable} The object on which you called this function
     */
    fire : function(type, params) {
        var listenerCollection = this._listener[type];

        if (listenerCollection == undefined) {
            throw ("The specified listener type " + type + " is not valid.");
        }

        if (params == undefined) {
            params = [];
        }

        for(var listener in listenerCollection) {
            listenerCollection[listener].callWithParams(params);
        }

        return this;
    }
}














/**
 * @constructor
 *
 * @extends odr.Drawable
 *
 * @class
 * Base class for all drawable items that cover an area, can therefore be clicked and need to be taken into account
 * when the canvas is resized.
 *
 * @abstract
 */
odr.Shape = function() {
    odr.Drawable.call(this);

    this._x = 0;
    this._y = 0;
    this._width = 0;
    this._height = 0;
    this._marked = false;
    this._invisibleMarkedPossible = false;

    for(var listenerType in odr.Shape.listener) {
        this._listener[odr.Shape.listener[listenerType]] = {};
    }

    odr.Shape.superClass.bind.call(this,
        odr.Shape.listener.sizeChanged,
        odr.assertSvgSize,
        "_shape_" + this.id());

    odr.Shape.superClass.bind.call(this,
        odr.Shape.listener.positionChanged,
        odr.assertSvgSize,
        "_shape_" + this.id());

    odr.vars.shapesThatDetermineCanvasSize[this.id()] = this;
}

/**
 * @namespace
 * Can be used in combination with {@link odr.Drawable#bind} and {@link odr.Drawable#unbind} as this object literal
 * defines the event types.
 */
odr.Shape.listener = {
    /** @field */
    positionChanged : "positionChanged",
    /** @field */
    sizeChanged : "sizeChanged",
    /** @field */
    markedChanged : "markedChanged"
}

odr.Shape.prototype = {
    _x : 0,
    _y : 0,
    _width : 0,
    _height : 0,
    /**
     * @private
     * @description
     * Whether this drawable is marked
     */
    _marked : false,
    _invisibleMarkedPossible : false,
    







    /**
     * @description
     * Set whether or not this element is marked. The initial value is false.
     *
     * You can also retrieve the current value by calling this method without parameters.
     *
     * @param {Boolean} [marked] The new marked value
     * @return {Boolean|odr.Drawable} The marked value which was set. If you call this
     * method with a parameter then the method will return the object on which you called the method.
     */
    marked : function(marked) {
        if (marked != undefined) {

            if (this._marked != marked) {
                if (!this.visible() && !this._invisibleMarkedPossible) {
                    marked = false;
                }

                this._marked = marked;

                if (marked) {
                    odr.vars.markedElements[this.id()] = this;
                } else {
                    delete odr.vars.markedElements[this.id()];
                }

                this.fire(odr.Shape.listener.markedChanged, [this]);
            }

            return this;
        }

        return this._marked;
    },





    
    /**
     * @description
     * You can also retrieve the current value by calling this method without parameters.
     *
     * @param {Number} [x] The new x coordinate
     * @return {Number|odr.Shape} The x coordinate which was set. If you call this method with a parameter then
     * the method will return the object on which you called the method.
     */
    x : function(x) {
        if (x != undefined) {
            if (this._x != x) {

                this._x = x;
                odr.Shape.superClass.fire.call(this, odr.Shape.listener.positionChanged, [this]);
            }

            return this;
        }

        return this._x;
    },





    /**
     * @description
     * You can also retrieve the current value by calling this method without parameters.
     *
     * @param {Number} [y] The new y coordinate
     * @return {Number|odr.Shape} The y coordinate which was set. If you call this method with a parameter then
     * the method will return the object on which you called the method.
     */
    y : function(y) {
        if (y != undefined) {
            if (this._y != y) {

                this._y = y;
                odr.Shape.superClass.fire.call(this, odr.Shape.listener.positionChanged, [this]);
            }

            return this;
        }

        return this._y;
    },






    /**
     * Update the whole position of a shape or retrieve the position. This method is to be prefered over x().y()
     * as it will only inform the listeners one time about the position changed. Therefore it can improve the
     * performance.
     *
     * @param {Number} [x] The new x coordinate
     * @param {Number} [y] The new y coordinate
     * @return {Object|odr.Shape} Returns odr.Shape when you pass an x and y parameter. If you call this method
     * without a parameter it will return the position as an object with x and y properties.
     */
    position : function(x, y) {
        if (x != undefined && y != undefined) {
            var positionChanged = false;

            if (this._x != x) {
                this._x = x;
                positionChanged = true;
            }

            if (this._y != y) {
                this._y = y;
                positionChanged = true;
            }

            if (positionChanged) {
                odr.Shape.superClass.fire.call(this, odr.Shape.listener.positionChanged, [this]);
            }

            return this;
        }

        return {
            x : this._x,
            y : this._y
        };
    },







    /**
     * @description
     * Move the shape
     *
     * @param {Number} [x] The number of pixels which you want to move the shape on the x axis
     * @param {Number} [y] The number of pixels which you want to move the shape on the y axis
     * @return {odr.Shape} Returns the object of which you called the method.
     */
    move : function(x, y) {
        var currentPosition = this.position();

        this.position(currentPosition.x + x, currentPosition.y + y);
    },





    /**
     * @description
     * You can also retrieve the current value by calling this method without parameters.
     *
     * @param {Number} [width] The new width
     * @return {Number|odr.Shape} The width which was set. If you call this method with a parameter then the
     * method will return the object on which you called the method.
     */
    width : function(width) {
        if (width != undefined) {
            if (this._width != width) {

                this._width = width;
                odr.Shape.superClass.fire.call(this, odr.Shape.listener.sizeChanged, [this]);
            }

            return this;
        }

        return this._width;
    },







    /**
     * @description
     * You can also retrieve the current value by calling this method without parameters.
     *
     * @param {Number} [height] The new height
     * @return {Number|odr.DrawableItem} The height which was set. If you call this method with a parameter then the
     * method will return the object on which you called the method.
     */
    height : function(height) {
        if (height != undefined) {
            if (this._height != height) {

                this._height = height;
                odr.Shape.superClass.fire.call(this, odr.Shape.listener.sizeChanged, [this]);
            }

            return this;
        }

        return this._height;
    },






    /**
     * Update the whole size of a shape or retrieve the size. This method is to be prefered over width()height()
     * as it will only inform the listeners one time about the size changed. Therefore it can improve the
     * performance.
     *
     * @param {Number} [width] The new width coordinate
     * @param {Number} [height] The new height coordinate
     * @return {Object|odr.Shape} Returns odr.Shape when you pass a width and height parameter. If you call this method
     * without a parameter it will return the size as an object with width and height properties.
     */
    size : function(width, height) {
        if (width != undefined && height != undefined) {
            var sizeChanged = false;

            if (this._width != width) {
                this._width = width;
                sizeChanged = true;
            }

            if (this._height != height) {
                this._height = height;
                sizeChanged = true;
            }

            if (sizeChanged) {
                odr.Shape.superClass.fire.call(this, odr.Shape.listener.sizeChanged, [this]);
            }

            return this;
        }

        return {
            width : this._width,
            height : this._height
        };
    },











    /**
     * @description
     * Retrieve the top left coordinate of this element.
     *
     * @return {Object} An object with x and y properties.
     */
    topLeft : function() {
        return {
            x : this.x(),
            y : this.y()
        };
    },








    /**
     * @description
     * Retrieve the bottom right coordinate of this element.
     *
     * @return {Object} An object with x and y properties.
     */
    bottomRight : function() {
        return {
            x : this.x() + this.width(),
            y : this.y() + this.height()
        };
    },










    /**
     * @description
     * Retrieve the center coordinate of this element.
     *
     * @return {Object} An object with x and y properties.
     */
    center : function() {
        return {
            x : this.x() + (this.width() / 2),
            y : this.y() + (this.height() / 2)
        };
    },








    /**
     * @description
     * Check whether the given coordinates lie in the shape
     *
     * @param {Number} x
     * @param {Number} y
     * @return {Boolean} True when the coordinates lie in the shape, false otherwise
     */
    isInside : function(x, y) {
        return x >= this.x() && y >= this.y() && x <= (this.x() + this.width()) && y <= (this.y() + this.height());
    },







    /**
     * @private
     */
    visible : function(visible) {
        var result = odr.Shape.superClass.visible.call(this, visible);

        if (!this._invisibleMarkedPossible && visible != undefined && !visible && this.marked()) {
            this.marked(false);
        }

        return result;
    }
}

extend(odr.Shape, odr.Drawable);

















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
    this._label = "";

    for(var listenerType in odr.Endpoint.listener) {
        this._listener[odr.Endpoint.listener[listenerType]] = {};
    }
}

/**
 * @namespace
 * Can be used in combination with {@link odr.Drawable#bind} and {@link odr.Drawable#unbind} as this object literal
 * defines the event types.
 */
odr.Endpoint.listener = {
    /** @field */
    labelChanged : "labelChanged"
}

odr.Endpoint.prototype = {
    _label : "",





    
    /**
     * @description
     * You can also retrieve the current value by calling this method without parameters.
     *
     * @param {String|Number} [label] The new label
     * @return {String|Number|odr.Endpoint} The label which was set or null if no label was set.
     * If you call this method with a parameter then the method will return the object on which you called the
     * method.
     */
    label : function(label) {
        if(label != undefined) {
            if (this._label != label) {
                this._label = label;
                odr.Endpoint.superClass.fire.call(this, odr.Endpoint.listener.labelChanged, [this]);
            }

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
 * A node is an endpoint of an association and a graphical representation of a decision, version or iteration
 */
odr.Node = function() {
    odr.Endpoint.call(this);
    this._status = "";
    this._element = null;
    this._statusElement = null;
    this._labelElement = null;
    this._minSize = {
        width : 0,
        height : 0
    };
    this._json = null;

    for(var listenerType in odr.Node.listener) {
        this._listener[odr.Node.listener[listenerType]] = {};
    }

    this.addClass(odr.settings.node["class"]);
    this._paint();

    odr.Node.superClass.bind.call(this,
        odr.Drawable.listener.visibilityChanged,
        this._visibilityChanged.createDelegate(this),
        "_node_" + this.id());

    odr.Node.superClass.bind.call(this,
        odr.Endpoint.listener.labelChanged,
        this._labelChanged.createDelegate(this),
        "_node_" + this.id());

    odr.Node.superClass.bind.call(this,
        odr.Node.listener.statusChanged,
        this._statusChanged.createDelegate(this),
        "_node_" + this.id());

    odr.Node.superClass.bind.call(this,
        odr.Drawable.listener.classesChanged,
        this._classesChanged.createDelegate(this),
        "_node_" + this.id());

    odr.Node.superClass.bind.call(this,
        odr.Drawable.listener.parentChanged,
        this._parentChanged.createDelegate(this),
        "_node_" + this.id());

    odr.Node.superClass.bind.call(this,
        odr.Shape.listener.markedChanged,
        this._markedChanged.createDelegate(this),
        "_node_" + this.id());

    odr.Node.superClass.bind.call(this,
        odr.Shape.listener.sizeChanged,
        this._sizeChanged.createDelegate(this),
        "_node_" + this.id());

    odr.Node.superClass.bind.call(this,
        odr.Shape.listener.positionChanged,
        this._positionChanged.createDelegate(this),
        "_node_" + this.id());
}

/**
 * @namespace
 * Can be used in combination with {@link odr.Drawable#bind} and {@link odr.Drawable#unbind} as this object literal
 * defines the event types.
 */
odr.Node.listener = {
    /** @field */
    statusChanged : "statusChanged"
}

odr.Node.prototype = {
    _status : "",
    _element : null,
    _statusElement : null,
    _labelElement : null,
    _minSize : {
        width : 0,
        height : 0
    },
    _json : null,



    /**
     * @private
     */
    _idPrefix : function() {
        return odr.settings.node.idPrefix;
    },




    /**
     * @description
     * Set or get the status for this node. The status is the text which is displayed above the label.
     *
     * @param {String} [status] The status which you want to set
     * @return {String|odr.Node} When no parameter is provided the status will be returned. If a parameter has been
     * provided the object ob which you called the method will be returned.
     */
    status : function(status) {
        if (status != undefined) {
            if (this._status != status) {
                this._status = status;
                odr.Node.superClass.fire.call(this, odr.Node.listener.statusChanged, [this]);
            }

            return this;
        }

        return this._status;
    },




    /**
     * @private
     */
    _paint : function() {
        // prepare the group div
        this._element = document.createElement("div");
        this._element.id = this.id();
        this._element.className = this.classString();
        this._element.style.position = "absolute";
        this._element.style.width = this.width() + "px";
        this._element.style.height = this.height() + "px";
        this._element.style.left = this.x() + "px";
        this._element.style.top = this.y() + "px";




        // label for the status / stereotype
        this._statusElement = document.createElement("span");
        this._element.appendChild(this._statusElement);




        // label for the label...
        this._labelElement = document.createElement("h3");
        this._element.appendChild(this._labelElement);
        



        // info icon
        var info = document.createElement("div");
        info.className = odr.settings.node.infoIcon["class"];
        info.title = odr.settings.node.infoIcon.text;
        this._element.appendChild(info);
        vtip($(info));







        // resize icon
        var resize = document.createElement("div");
        resize.className = odr.settings.node.resizeIcon["class"];
        resize.title = odr.settings.node.resizeIcon.text
        this._element.appendChild(resize);
        $(resize).click(function() {
            this.size(this._minSize.width, this._minSize.height);
        }.createDelegate(this));
        vtip($(resize));







        // hide icon
        var hide = document.createElement("div");
        hide.className = odr.settings.node.hideIcon["class"];
        hide.title = odr.settings.node.hideIcon.text
        this._element.appendChild(hide);
        $(hide).click(function() {
            this.visible(false);
        }.createDelegate(this));
        vtip($(hide));



        // add the group div to the parent
        var parent = this.parent();

        if (parent == undefined) {
            parent = document.getElementById(odr.settings.node.container);
        }

        parent.appendChild(this._element);



        // activate drag / drop and resizing
        // for some reason, $(this.__element).draggable(...) is not working, 'hence the node will be retrieved
        // using standard jQuery
        var jQueryNode = $("#" +this.id());
        jQueryNode.draggable(odr.settings.node.jQueryUiDraggingSettings);
        jQueryNode.resizable(odr.settings.node.jQueryUiResizingSettings);



        // attach listeners to the draggable events
        jQueryNode.bind("dragstart", this._positionChangedThroughUi.createDelegate(this));
        jQueryNode.bind("drag", this._positionChangedThroughUi.createDelegate(this));
        jQueryNode.bind("dragstop", this._positionChangedThroughUi.createDelegate(this));



        // attach listeners to the resize events
        jQueryNode.bind("resizestart", this._sizeChangedThroughUi.createDelegate(this));
        jQueryNode.bind("resize", this._sizeChangedThroughUi.createDelegate(this));
        jQueryNode.bind("resizestop", this._sizeChangedThroughUi.createDelegate(this));




        // attach a listener to the click event
        jQueryNode.bind("click", this._click.createDelegate(this));
    },



    /**
     * @private
     */
    _click : function(e) {
        if(e.ctrlKey) {
            this.marked(!this.marked());
        }
    },





    /**
     * @private
     */
    _visibilityChanged : function() {
        var display = null;

        if (this.visible()) {
            display = "block";
        } else {
            display = "none";
        }

        this._element.style.display = display;
    },





    /**
     * @private
     */
    _labelChanged : function() {
        $(this._labelElement).text(this.label());
        
        this._setMinSize();
    },




    /**
     * @private
     * @description
     * Will be called when {@link odr.Node.status} is called with a new value
     */
    _statusChanged : function() {
        $(this._statusElement).text(this.status());

        this._setMinSize();
    },





    /**
     * @private
     * @description
     * Will be called when the node is dragged in the user interface through jQuery ui
     */
    _positionChangedThroughUi : function(e) {
        if (odr.user.lowPerformanceMode && e.type != "dragstop") {
            return;
        }

        var uiPosition = $(this._element).position();
        var entityPosition = this.position();
        
        this.position(uiPosition.left, uiPosition.top);

        odr.moveMarkedShapes(uiPosition.left - entityPosition.x, uiPosition.top - entityPosition.y, this);
    },





    /**
     * @private
     * @description
     * Will be called when {@link odr.Shape.x} or {@link odr.Shape.y} is called with a new value
     */
    _positionChanged : function() {
        this._element.style.left = this.x() + "px";
        this._element.style.top = this.y() + "px";
    },





    /**
     * @private
     * @description
     * Will be called when the node is resized in the user interface through jQuery ui
     */
    _sizeChangedThroughUi : function(e) {
        if (odr.user.lowPerformanceMode && e.type != "resizestop") {
            return;
        }

        this.size($(this._element).width(), $(this._element).height());
    },




    /**
     * @private
     */
    _sizeChanged : function() {
        this._element.style.width = this.width() + "px";
        this._element.style.height = this.height() + "px";
    },




    /**
     * @private
     */
    _classesChanged : function() {
        this._element.className = this.classString();
    },





    /**
     * @private
     */
    _parentChanged : function() {
        // Inserting the DOM element a second time will automatically remove it from
        // it's previous position.
        this.parent().appendChild(this._element);
    },





    /**
     * @private
     */
    _markedChanged : function() {
        if (this.marked()) {
            this.addClass(odr.settings.node.markedClass);
        } else {
            this.removeClass(odr.settings.node.markedClass);
        }
    },





    /**
     * @private
     * @description
     * Set the minimum size for the node. The minimum size is calculated and depends on the dimensions
     * of the status and label text.
     */
    _setMinSize : function() {
        var labelDimensions = odr.meassureTextDimensions(this.label(), odr.settings.node.labelMeasureCss);
        var statusDimensions = odr.meassureTextDimensions(this.status(), odr.settings.node.statusMeasureCss);

        this._minSize.width = Math.max(labelDimensions.width, statusDimensions.width) + odr.settings.node.textPadding.x;
        this._minSize.height = labelDimensions.height + statusDimensions.height + odr.settings.node.textPadding.y;

        this._minSize.width = odr.roundUp(Math.max(this._minSize.width, odr.settings.node.size.min.width), odr.settings.node.size.multipleOf.width);
        this._minSize.height = odr.roundUp(Math.max( this._minSize.height, odr.settings.node.size.min.height), odr.settings.node.size.multipleOf.height);

        // removed as min-width may not apply when text is to be broken up into several lines
        //        this._element.style["min-width"] = minWidth + "px";
        this._element.style["min-height"] =  this._minSize.height + "px";

        var previousSize = this.size();

        if (previousSize.width == 0 || previousSize.height == 0) {
            this._element.style.width = this._minSize.width + "px";
            this._element.style.height =  this._minSize.height + "px";
        }

        this.size(odr.roundUp($(this._element).width(), odr.settings.node.size.multipleOf.width),
            odr.roundUp($(this._element).height(), odr.settings.node.size.multipleOf.height));
    }
}

extend(odr.Node, odr.Endpoint);

















/**
 * @constructor
 *
 * @extends odr.Shape
 *
 * @class
 * A handle can be used to create angular lines.
 */
odr.Handle = function() {
    odr.Shape.call(this);
    this._element = null;
    this._containment = null;
    this._relativeX = 0;
    this._relativeY = 0;

    this._invisibleMarkedPossible = true;

    for(var listenerType in odr.Handle.listener) {
        this._listener[odr.Handle.listener[listenerType]] = {};
    }

    this.size(odr.settings.handle.size.width, odr.settings.handle.size.height);

    this.addClass(odr.settings.handle["class"]);
    this._paint();

    odr.Handle.superClass.bind.call(this,
        odr.Drawable.listener.visibilityChanged,
        this._visibilityChanged.createDelegate(this),
        this.id());

    odr.Handle.superClass.bind.call(this,
        odr.Drawable.listener.classesChanged,
        this._classesChanged.createDelegate(this),
        this.id());

    odr.Handle.superClass.bind.call(this,
        odr.Drawable.listener.parentChanged,
        this._parentChanged.createDelegate(this),
        this.id());

    odr.Handle.superClass.bind.call(this,
        odr.Shape.listener.markedChanged,
        this._markedChanged.createDelegate(this),
        this.id());

    odr.Handle.superClass.bind.call(this,
        odr.Shape.listener.positionChanged,
        this._positionChanged.createDelegate(this),
        this.id());

};

/**
 * @namespace
 * Can be used in combination with {@link odr.Drawable#bind} and {@link odr.Drawable#unbind} as this object literal
 * defines the event types.
 */
odr.Handle.listener = {
    /** @field */
    containmentChanged : "containmentChanged",
    /** @field */
    click : "click",
    /** @field */
    dragStop : "dragStop"
}

odr.Handle.prototype = {
    _element : null,
    _containment : null,
    _relativeX : 0,
    _relativeY : 0,






    /**
     * @private
     */
    _idPrefix : function() {
        return odr.settings.handle.idPrefix;
    },








    /**
     * @description
     * Restrain the movement of the handle to this element.
     *
     * @param {odr.Shape} [containment] The shape which you want to use to constrain the movement of the drag handle to.
     * @return {odr.Handle|odr.Shape} The object on which you called the method if you provide a parameter or the current
     * containment if you provide no parameter.
     */
    containment : function(containment) {
        if (containment != undefined) {
            if (containment != this._containment) {
                if (this._containment != null) {
                    this._containment.unbind(odr.Shape.listener.positionChanged, this.id());
                    this._containment.unbind(odr.Shape.listener.sizeChanged, this.id());
                }

                this._containment = containment;

                if (this._containment != null) {
                    $(this._element).draggable("option", "containment", "#" + this._containment.id());
                    $(this._element).draggable("option", "grid", false);
                    this.position(this._containment.x(), this._containment.y());
                } else {
                    $(this._element).draggable("option", "containment", false);
                    $(this._element).draggable("option", "grid", odr.settings.grid);
                }

                odr.Handle.superClass.fire.call(this, odr.Handle.listener.containmentChanged, [this]);
                
                this._containment.bind(odr.Shape.listener.positionChanged,
                    this._containmentPositionChanged.createDelegate(this),
                    this.id());

                this._containment.bind(odr.Shape.listener.sizeChanged,
                    this._containmentSizeChanged.createDelegate(this),
                    this.id());
            }

            return this;
        }

        return this._containment;
    },









    /**
     * @private
     */
    _paint : function() {
        // prepare node div
        this._element = document.createElement("div");
        this._element.id = this.id();
        this._element.className = this.classString();
        this._element.style.position = "absolute";
        this._element.style.width = this.width() + "px";
        this._element.style.height = this.height() + "px";
        this._element.style.left = this.x() + "px";
        this._element.style.top = this.y() + "px";




        // add the node div to the parent
        var parent = this.parent();

        if (parent == undefined) {
            parent = document.getElementById(odr.settings.handle.container);
        }

        parent.appendChild(this._element);



        // activate drag / drop
        // for some reason, $(this.__element).draggable(...) is not working, 'hence the node will be retrieved
        // using standard jQuery
        var jQueryHandle = $("#" + this.id());
        jQueryHandle.draggable(odr.settings.handle.jQueryUiDraggingSettings);





        // attach listeners to the draggable events
        jQueryHandle.bind("dragstart", this._positionChangedThroughUi.createDelegate(this));
        jQueryHandle.bind("drag", this._positionChangedThroughUi.createDelegate(this));
        jQueryHandle.bind("dragstop", this._positionChangedThroughUi.createDelegate(this));



        
        // attach a listener to the click event
        jQueryHandle.bind("click", this._click.createDelegate(this));
    },













    /**
     * @private
     */
    _click : function(e) {
        if(e.ctrlKey) {
            this.marked(!this.marked());
        } else {
            this.fire(odr.Handle.listener.click, [this, e]);
        }

        return false;
    },








    /**
     * @private
     */
    _visibilityChanged : function() {
        var display = null;

        if (this.visible()) {
            display = "block";
        } else {
            display = "none";
        }

        this._element.style.display = display;
    },






    /**
     * @private
     */
    _classesChanged : function() {
        this._element.className = this.classString();
    },






    /**
     * @private
     */
    _parentChanged : function() {
        // Inserting the DOM element a second time will automatically remove it from
        // it's previous position.
        this.parent().appendChild(this._element);
    },







    /**
     * @private
     */
    _markedChanged : function() {
        if (this.marked()) {
            this.addClass(odr.settings.handle.markedClass);
        } else {
            this.removeClass(odr.settings.handle.markedClass);
        }
    },







    /**
     * @private
     * @description
     * Will be called when the handle is dragged in the user interface through jQuery ui
     */
    _positionChangedThroughUi : function(e) {
        if (odr.user.lowPerformanceMode && e.type != "dragstop") {
            return;
        }

        var uiPosition = $(this._element).position();
        var entityPosition = this.position();

        this.position(uiPosition.left, uiPosition.top);
        
        if (this._containment != null) {
            this._relativeX = uiPosition.left - this._containment.x();
            this._relativeY = uiPosition.top - this._containment.y();
        }
        
        odr.moveMarkedShapes(uiPosition.left - entityPosition.x, uiPosition.top - entityPosition.y, this);

        if (e.type == "dragstop") {
            this.fire(odr.Handle.listener.dragStop, [this]);
        }
    },






    /**
     * @private
     * @description
     * Will be called when {@link odr.Shape.x} or {@link odr.Shape.y} is called with a new value
     */
    _positionChanged : function() {
        this._element.style.left = this.x() + "px";
        this._element.style.top = this.y() + "px";
    },











    /**
     * @private
     */
    _containmentPositionChanged : function() {
        this.position(this._containment.x() + this._relativeX, this._containment.y() + this._relativeY);
    },







    /**
     * @private
     */
    _containmentSizeChanged : function() {
        if (!this._containment.isInside(this._containment.x() + this._relativeX,
            this._containment.y() + this._relativeY)) {
            this.position(this._containment.x(), this._containment.y());
            this._relativeX = this._relativeY = 0;
        }
    },









    /**
     * @private
     */
    marked : function(marked) {
        if (this._containment != null) {
            marked = undefined;
        }

        return odr.Handle.superClass.marked.call(this, marked);
    },









    /**
     * @description
     * <p>Remove this handle. The handle unbinds itself from all listeners and removes all html markup that is associated
     * with this object.</p>
     * <p>This action can't be undone.</p>
     */
    remove : function() {
        if (this._containment != null) {
            this._containment.unbind(odr.Shape.listener.positionChanged, this.id());
            this._containment.unbind(odr.Shape.listener.sizeChanged, this.id());
        }

        $("#" + this.id()).draggable("destroy");
        $("#" + this.id()).remove();
        odr.registry.remove(this.id());
    }
};

extend(odr.Handle, odr.Shape);























/**
 * @constructor
 *
 * @extends odr.Drawable
 *
 * @class
 * An association represents a connection between two {@link odr.Endpoint}s.
 */
odr.Association = function() {
    odr.Drawable.call(this);

    this._element = null;

    this._source = null;
    this._target = null;

    this._sourceHandle = new odr.Handle().
    visible(false).
    bind(odr.Handle.listener.dragStop, this._handleDragStop.createDelegate(this), this.id());

    this._targetHandle = new odr.Handle().
    visible(false).
    bind(odr.Handle.listener.dragStop, this._handleDragStop.createDelegate(this), this.id());

    this._label = new odr.Label().bind(odr.Label.listener.mousein, this._labelMouseIn.createDelegate(this), this.id())
    .bind(odr.Label.listener.mouseout, this._labelMouseOut.createDelegate(this), this.id());
    
    this._labelLines = [];
    this._labelLines[0] = new odr.Line().source(this._sourceHandle).target(this._label).visible(false).color("grey");
    this._labelLines[1] = new odr.Line().source(this._targetHandle).target(this._label).visible(false).color("grey");


    this._handles = [];
    this._lines = [];

    this._forceHandleVisible = true;

    this.addClass(odr.settings.association["class"]);

    for(var listenerType in odr.Association.listener) {
        this._listener[odr.Association.listener[listenerType]] = {};
    }

    this._paint();

    this.bind(odr.Association.listener.handleChanged, this._handleChanged.createDelegate(this), this.id());
    this.bind(odr.Drawable.listener.visibilityChanged, this._visibilityChanged.createDelegate(this), this.id());
};

/**
 * @namespace
 * Can be used in combination with {@link odr.Drawable#bind} and {@link odr.Drawable#unbind} as this object literal
 * defines the event types.
 */
odr.Association.listener = {
    /** @field */
    sourceChanged : "sourceChanged",
    /** @field */
    targetChanged : "targetChanged",
    /** @field */
    handleChanged : "handleChanged"
}

odr.Association.prototype = {
    _element : null,

    _source : null,
    _sourceHandle : null,
    _target : null,
    _targetHandle : null,
    _handles : [],
    _lines : [],
    _forceHandleVisible : true,
    _label : null,
    _labelLines : [],







    /**
     * @private
     */
    _idPrefix : function() {
        return odr.settings.association.idPrefix;
    },










    /**
     * @description
     * You can also retrieve the current value by calling this method without parameters.
     *
     * @param {String|Number} [label] The new label
     * @return {String|Number|odr.Association} The label which was set or null if no label was set.
     * If you call this method with a parameter then the method will return the object on which you called the
     * method.
     */
    label : function(label) {
        if (label != undefined) {
            this._label.label(label);
            
            return this;
        }

        return this._label.label();
    },











    /**
     * @description
     * You can also retrieve the current value by calling this method without parameters.
     *
     * @param {Number} [x] The new x coordinate
     * @param {Number} [y] The new y coordinate
     * @return {Object|odr.Association} The current position as an object with x and y properties if you supply no or
     * only one parameter. If you pass in both parameter, then the object on which you called the method is returned.
     * method.
     */
    labelPosition : function(x, y) {
        if (x != undefined && y != undefined) {
            this._label.position(x, y);

            return this;
        }

        return this._label.position();
    },









    /**
     * @description
     * Set a new endpoint for this association or retrieve the endpoint. This endpoint represents the source
     * of the association
     *
     * @param {odr.Endpoint} [source] The new souce or nothing if you want to retrieve the current source
     * @return {odr.Association|odr.Endpoint} The current source if you call this method without any parameter or the
     * object on which you called this method if you supply a parameter.
     */
    source : function(source) {
        if (source != undefined) {
            if (source != this._source) {
                if (this._source != null) {
                    this._source.unbind(odr.Drawable.listener.visibilityChanged, this.id());
                }

                this._source = source;

                this._source.bind(odr.Drawable.listener.visibilityChanged,
                    this._endpointVisibilityChanged.createDelegate(this),
                    this.id());

                this.fire(odr.Association.listener.sourceChanged, [this]);
                
                this._sourceHandle.containment(source);
            }

            return this;
        }

        return this._source;
    },







    /**
     * @description
     * Set a new endpoint for this association or retrieve the endpoint. This endpoint represents the target
     * of the association
     *
     * @param {odr.Endpoint} [target] The new target or nothing if you want to retrieve the current target
     * @return {odr.Association|odr.Endpoint} The current target if you call this method without any parameter or the
     * object on which you called this method if you supply a parameter.
     */
    target : function(target) {
        if (target != undefined) {
            if (target != this._target) {
                if (this._target != null) {
                    this._target.unbind(odr.Drawable.listener.visibilityChanged, this.id());
                }

                this._target = target;

                this._target.bind(odr.Drawable.listener.visibilityChanged,
                    this._endpointVisibilityChanged.createDelegate(this),
                    this.id());

                this.fire(odr.Association.listener.targetChanged, [this]);
                
                this._targetHandle.containment(target);
            }

            return this;
        }

        return this._target;
    },











    /**
     * @description
     * Add a handle to the association. The handle will be added to the end of the association
     *
     * @param {odr.Handle} handle The handle which you want to add
     * @return {odr.Association} The object on which you called this method.
     */
    addHandleToEnd : function(handle) {
        this.removeHandle(handle, false);

        this._handles.push(handle);

        handle.bind(odr.Handle.listener.click, this._handleClick.createDelegate(this), this.id());
        handle.bind(odr.Handle.listener.dragStop, this._handleDragStop.createDelegate(this), this.id());
        this.fire(odr.Association.listener.handleChanged, [this]);

        return this;
    },










    /**
     * @description
     * Add a handle to the association. The handle will be added to the beginning of the association
     *
     * @param {odr.Handle} handle The handle which you want to add
     * @return {odr.Association} The object on which you called this method.
     */
    addHandleToBeginning : function(handle) {
        this.removeHandle(handle, false);

        this._handles.unshift(handle);

        handle.bind(odr.Handle.listener.click, this._handleClick.createDelegate(this), this.id());
        handle.bind(odr.Handle.listener.dragStop, this._handleDragStop.createDelegate(this), this.id());
        this.fire(odr.Association.listener.handleChanged, [this]);

        return this;
    },









    /**
     * @description
     * Add a handle before another handle.
     *
     * @param {odr.Handle} after The handle which should be before the new handle after insertion
     * @param {odr.Handle} newHandle The new handle
     * @return {odr.Association} The object on which you called this method.
     */
    addHandleAfter : function(after, newHandle) {
        this.removeHandle(newHandle, false);

        var index = this._handles.indexOf(after);

        if (index == -1) {
            throw("The handle is not part of the association");
        }

        this._handles.splice(index+1, 0, newHandle);

        newHandle.bind(odr.Handle.listener.click, this._handleClick.createDelegate(this), this.id());
        newHandle.bind(odr.Handle.listener.dragStop, this._handleDragStop.createDelegate(this), this.id());
        this.fire(odr.Association.listener.handleChanged, [this]);

        return this;
    },









    /**
     * @description
     * Remove the given handle from the association
     *
     * @param {odr.Handle} handle The handle which you want to add
     * @param {Boolean} [fire] Whether the handle changed event should be fired. This should almost always be true.
     * Default is true.
     * @return {odr.Association} The object on which you called this method.
     */
    removeHandle : function(handle, fire) {
        var index = this._handles.indexOf(handle);

        if (index != -1) {

            this._handles[index].unbind(odr.Handle.listener.click, this.id());
            this._handles[index].unbind(odr.Handle.listener.dragStop, this.id());

            this._handles.splice(index, 1);

            if (fire != false) {
                this.fire(odr.Association.listener.handleChanged, [this]);
            }
        }

        return this;
    },








    /**
     * @private
     */
    _handleClick : function(handle, e) {
        if (e.shiftKey) {
            this.removeHandle(handle);
            handle.remove();
        }
    },






    /**
     * @private
     */
    _paint : function() {
        var root = odr.canvas();
        var suspendID = root.suspendRedraw(5000);

        this._element = document.createElementNS(svgns, "g");
        this._element.setAttribute("id", this.id());
        this._element.setAttribute("class", this.classString());

        // add the node div to the parent
        var parent = this.parent();

        if (parent == undefined) {
            parent = document.getElementById(odr.settings.association.container);
        }

        parent.appendChild(this._element);

        this._repaint();

        root.unsuspendRedraw(suspendID);
    },
    
    




    _repaint : function() {
        var root = odr.canvas();
        var suspendID = root.suspendRedraw(5000);

        // remove all lines
        for(var i = 0; i < this._lines.length; i++) {
            this._lines[i].remove();
        }

        this._lines = [];

        // create the new lines
        var currentNode = this._sourceHandle;

        for(var i = 0; i < this._handles.length; i++) {
            var currentHandle = this._handles[i];

            var line = new odr.Line().source(currentNode).target(currentHandle);
            this._lines.push(line);

            currentNode = currentHandle;
        }

        var line = new odr.Line().source(currentNode).target(this._targetHandle).arrow(true);
        this._lines.push(line);


        for(var i = 0; i < this._lines.length; i++) {
            var line = this._lines[i];

            line.bind(odr.Line.listener.click, this._lineMouseClick.createDelegate(this), this.id());
            line.bind(odr.Line.listener.mousein, this._lineMouseIn.createDelegate(this), this.id());
            line.bind(odr.Line.listener.mouseout, this._lineMouseOut.createDelegate(this), this.id());
        }

        this._setAllHandles(this._forceHandleVisible);

        root.unsuspendRedraw(suspendID);
    },

    




    /**
     * @private
     */
    _lineMouseClick : function(source, e) {
        if (e.ctrlKey) {
            this._forceHandleVisible = !this._forceHandleVisible;
            this._setAllHandles(this._forceHandleVisible);
            return;
        }

        var handle = new odr.Handle();
        handle.position(odr.round(e.pageX, odr.settings.grid[0]), odr.round(e.pageY, odr.settings.grid[0]));

        if (source.source() == this._sourceHandle) {
            this.addHandleToBeginning(handle);
        } else {
            this.addHandleAfter(source.source(), handle);
        }
    },






    /**
     * @private
     */
    _lineMouseIn : function() {
        var root = odr.canvas();
        var suspendID = root.suspendRedraw(5000);

        this._setAllHandles(true);

        for(var i = 0; i < this._lines.length; i++) {
            this._lines[i].highlight();
        }

        this._setLabelLineVisible(true);

        root.unsuspendRedraw(suspendID);
    },





    /**
     * @private
     */
    _lineMouseOut : function() {
        var root = odr.canvas();
        var suspendID = root.suspendRedraw(5000);

        if (!this._forceHandleVisible) {
            this._setAllHandles(false);
        }

        for(var i = 0; i < this._lines.length; i++) {
            this._lines[i].stopHighlight();
        }

        this._setLabelLineVisible(false);

        root.unsuspendRedraw(suspendID);
    },







    /**
     * @private
     */
    _labelMouseIn : function() {
        var root = odr.canvas();
        var suspendID = root.suspendRedraw(5000);

        this._setLabelLineVisible(true);

        root.unsuspendRedraw(suspendID);
    },







    /**
     * @private
     */
    _labelMouseOut : function() {
        var root = odr.canvas();
        var suspendID = root.suspendRedraw(5000);

        this._setLabelLineVisible(false);

        root.unsuspendRedraw(suspendID);
    },




    /**
     * @private
     */
    _setLabelLineVisible : function(visible) {
        for(var i = 0; i < this._labelLines.length; i++) {
            this._labelLines[i].visible(visible);
        }
    },





    /**
     * @private
     */
    _handleDragStop : function(handle) {
        var otherHandle;
        var containment;
        if (handle == this._sourceHandle) {

            if (this._handles.length != 0) {
                otherHandle = this._handles[0];
            } else {
                otherHandle = this._targetHandle;
            }
            containment = this._source;
        } else if (handle == this._targetHandle) {
            if (this._handles.length != 0) {
                otherHandle = this._handles[this._handles.length - 1];
            } else {
                otherHandle = this._sourceHandle;
            }

            containment = this._target;
        } else {
            this._optimizePath();
            return;
        }

        var currentX = handle.x();
        var otherX = otherHandle.x();
        var topLeft = containment.topLeft();
        var bottomRight = containment.bottomRight();

        if (otherX >= topLeft.x && otherX <= bottomRight.x && this._inTreshhold(otherX, currentX) && otherX != currentX) {
            odr.alignmentHelper(function() {
                handle.x(otherX);
                this._optimizePath();
            }.createDelegate(this));
            return;
        }

        var currentY = handle.y();
        var otherY = otherHandle.y();

        if (otherY >= topLeft.y && otherY <= bottomRight.y && this._inTreshhold(otherY, currentY) && otherY != currentY) {
            odr.alignmentHelper(function() {
                handle.y(otherY);
                this._optimizePath();
            }.createDelegate(this));
            return;
        }
        
    },






    /**
     * @private
     */
    _inTreshhold : function(coordinate1, coordinate2) {
        coordinate1 = Math.abs(coordinate1);
        coordinate2 = Math.abs(coordinate2);

        return Math.max(coordinate1, coordinate2) - Math.min(coordinate1, coordinate2) < odr.settings.handle.alignmentTreshhold;
    },

    
    


  






    /**
     * @private
     */
    _setAllHandles : function(visible) {
        this._sourceHandle.visible(visible);
        this._targetHandle.visible(visible);

        for(var i = 0; i < this._handles.length; i++) {
            this._handles[i].visible(visible);
        }
    },









    /**
     * @private
     */
    _optimizePath : function() {
        var root = odr.canvas();
        var suspendID = root.suspendRedraw(5000);

        if (this._handles.length == 0) {
            return;
        }

        var firstHandle = this._sourceHandle;
        var secondHandle = this._handles[0];
        var thirdHandle = null;

        for(var i = 1; i < this._handles.length; i++) {
            thirdHandle = this._handles[i];

            if ((firstHandle.x() == secondHandle.x() && secondHandle.x() == thirdHandle.x()) ||
                (firstHandle.y() == secondHandle.y() && secondHandle.y() == thirdHandle.y())) {
                this.removeHandle(secondHandle);
                secondHandle.remove();
                this._optimizePath();
                return;
            }

            // ...
            firstHandle = secondHandle;
            secondHandle = thirdHandle;
        }

        thirdHandle = this._targetHandle;

        if ((firstHandle.x() == secondHandle.x() && secondHandle.x() == thirdHandle.x()) ||
            (firstHandle.y() == secondHandle.y() && secondHandle.y() == thirdHandle.y())) {
            this.removeHandle(secondHandle);
            secondHandle.remove();
        }

        root.unsuspendRedraw(suspendID);
    },








    /**
     * @private
     */
    _visibilityChanged : function() {
        var root = odr.canvas();
        var suspendID = root.suspendRedraw(5000);

        var visible = this.visible();

        this._label.visible(visible);

        for(var i = 0; i < this._lines.length; i++) {
            this._lines[i].visible(visible);
        }

        if (!visible) {
            this._forceHandleVisible = false;
            this._setAllHandles(false);
        } else {
            this._sourceHandle.visible(true);
            this._targetHandle.visible(true);
        }

        root.unsuspendRedraw(suspendID);
    },




    
    
    /**
     * @private
     */
    _endpointVisibilityChanged : function() {
        var shouldBeVisible = this._source.visible() && this._target.visible();

        this.visible(shouldBeVisible);
    },






    /**
     * @private
     */
    _handleChanged : function() {
        this._repaint();
    }

};

extend(odr.Association, odr.Drawable);















/**
 * @constructor
 *
 * @extends odr.Drawable
 *
 * @class
 * An abstract line between two {@link odr.Shape}s
 */
odr.Line = function() {
    odr.Drawable.call(this);

    this._element = null;

    this._source = null;
    this._target = null;

    this._arrowId = odr.settings.line.arrow.idPrefix + odr.newId();
    this._arrow = false;

    this._hover = false;

    this.addClass(odr.settings.line["class"]);

    this._color = null;

    for(var listenerType in odr.Line.listener) {
        this._listener[odr.Line.listener[listenerType]] = {};
    }

    this._paint();

    this.bind(odr.Drawable.listener.visibilityChanged, this._visibilityChanged.createDelegate(this), this.id());
    this.bind(odr.Drawable.listener.classesChanged, this._classesChanged.createDelegate(this), this.id());
//    this.bind(odr.Line.listener.mousein, this._mousein.createDelegate(this), this.id());
//    this.bind(odr.Line.listener.mouseout, this._mouseout.createDelegate(this), this.id());
};

/**
 * @namespace
 * Can be used in combination with {@link odr.Drawable#bind} and {@link odr.Drawable#unbind} as this object literal
 * defines the event types.
 */
odr.Line.listener = {
    /** @field */
    sourceChanged : "sourceChanged",
    /** @field */
    targetChanged : "targetChanged",
    /** @field */
    click : "click",
    /** @field */
    mouseover : "mouseover",
    /** @field */
    mousein : "mousein",
    /** @field */
    mouseout : "mouseout"
}

odr.Line.prototype = {
    _element : null,
    _source : null,
    _target : null,
    _hover : false,
    _arrowId : null,
    _arrowElement : null,
    _arrow : false,
    _color : null,





    /**
     * @private
     */
    _idPrefix : function() {
        return odr.settings.line.idPrefix;
    },






    /**
     * @description
     * Set a new source for this line or retrieve the source. This shape represents the source
     * of the line
     *
     * @param {odr.Shape} [source] The new souce or nothing if you want to retrieve the current source
     * @return {odr.Line|odr.Shape} The current source if you call this method without any parameter or the
     * object on which you called this method if you supply a parameter.
     */
    source : function(source) {
        if (source != undefined) {
            if (source != this._source) {
                if (this._source != null) {
                    this._source.unbind(odr.Shape.listener.positionChanged, this.id());
                }

                this._source = source;

                var center = this._source.center();
                this._element.setAttribute("x1", center.x);
                this._element.setAttribute("y1", center.y);

                this._source.bind(odr.Shape.listener.positionChanged,
                    this._endpointPositionChanged.createDelegate(this),
                    this.id());

                this.fire.call(this, odr.Line.listener.sourceChanged, [this]);
            }

            return this;
        }

        return this._source;
    },







    /**
     * @description
     * Set a new target for this line or retrieve the target. This shape represents the target
     * of the line
     *
     * @param {odr.Shape} [target] The new target or nothing if you want to retrieve the current target
     * @return {odr.Line|odr.Shape} The current target if you call this method without any parameter or the
     * object on which you called this method if you supply a parameter.
     */
    target : function(target) {
        if (target != undefined) {
            if (target != this._target) {
                if (this._target != null) {
                    this._target.unbind(odr.Shape.listener.positionChanged, this.id());
                }

                this._target = target;

                var center = this._target.center();
                this._element.setAttribute("x2", center.x);
                this._element.setAttribute("y2", center.y);

                this._target.bind(odr.Shape.listener.positionChanged,
                    this._endpointPositionChanged.createDelegate(this),
                    this.id());

                this.fire.call(this, odr.Line.listener.targetChanged, [this]);
            }

            return this;
        }

        return this._target;
    },










    /**
     * @description
     * Change whether an arrow is shown or not.
     *
     * @param {Boolean} [arrow] True to show an error, false to hide it.
     * @return {Boolean|odr.Line} Whether an arrow or is shown or not is returned when you call this method without
     * a parameter. If you set a new value, the object on which you called this method is returned.
     */
    arrow : function(arrow) {
        if (arrow != undefined) {
            if (arrow != this._arrow) {
                this._arrow = arrow;

                if (this._arrow) {
                    this._element.setAttribute("marker-end", "url(#" + this._arrowId + ")");
                } else {
                    this._element.removeAttribute("marker-end");
                }
            }

            return this;
        }

        return this._arrow;
    },









    /**
     * @private
     */
    _paint : function() {
        var root = odr.canvas();
        var suspendID = root.suspendRedraw(5000);

        this._element = document.createElementNS(svgns, "line");
        this._element.setAttribute("id", this.id());
        this._element.setAttribute("class", this.classString());

        if (this._source != null) {
            var sourceCenter = this._source.center();
            this._element.setAttribute("x1", sourceCenter.x);
            this._element.setAttribute("y1", sourceCenter.y);
        }

        if (this._target != null) {
            var targetCenter = this._target.center();
            this._element.setAttribute("x2", targetCenter.x);
            this._element.setAttribute("y2", targetCenter.y);
        }

        for(var attributeName in odr.settings.line.attributes) {
            this._element.setAttribute(attributeName, odr.settings.line.attributes[attributeName]);
        }

        if (this._color != null) {
            this._element.setAttribute("stroke", this._color);
        }





        // add the node div to the parent
        var parent = this.parent();

        if (parent == undefined) {
            parent = document.getElementById(odr.settings.line.container);
        }

        parent.appendChild(this._element);





        // draw the arrow
        this._arrowElement = odr.arrow(this._arrowId);

        if (this._arrow) {
            this._element.setAttribute("marker-end", "url(#" + this._arrowId + ")");
        }







        // attach event listener to the svg element and fire the appropriate listeners of this class
        this._element.addEventListener("click", function(e) {
            this.fire(odr.Line.listener.click, [this, e]);
            return false;
        }.createDelegate(this), false);

        this._element.addEventListener("mouseover", function() {
            if (!this._hover) {
                this.fire(odr.Line.listener.mousein, [this]);
                this._hover = true;
            }
            this.fire(odr.Line.listener.mouseover, [this]);
        }.createDelegate(this), false);

        this._element.addEventListener("mouseout", function() {
            this.fire(odr.Line.listener.mouseout, [this]);
            this._hover = false;
        }.createDelegate(this), false);





        // draw
        root.unsuspendRedraw(suspendID);
    },









    /**
     * @private
     */
    _endpointPositionChanged : function() {
        var root = odr.canvas();
        var suspendID = root.suspendRedraw(5000);

        if (this._source != null) {
            var sourceCenter = this._source.center();
            this._element.setAttribute("x1", sourceCenter.x);
            this._element.setAttribute("y1", sourceCenter.y);
        }

        if (this._target != null) {
            var targetCenter = this._target.center();
            this._element.setAttribute("x2", targetCenter.x);
            this._element.setAttribute("y2", targetCenter.y);
        }

        root.unsuspendRedraw(suspendID);
    },







    /**
     * @private
     */
    _visibilityChanged : function() {
        if (this.visible()) {
            this._paint();
        } else {
            this._element.parentNode.removeChild(this._element);
            this._arrowElement.parentNode.removeChild(this._arrowElement);
        }
    },






    /**
     * @private
     */
    _classesChanged : function() {
        this._element.setAttribute("class", this.classString());
    },











    /**
     * @description
     * Set a new color for this line
     *
     * @param {String} [color] The new color
     * @return {String|odr.Line} The object on which you called this method or the current color if you supply
     * no parameter
     */
    color : function(color) {
        if (color != undefined) {
            if (this._color != color) {
                this._color = color;

                if (this._color != null) {
                    this._element.setAttribute("stroke", this._color);
                }

                return this;
            }
        }

        return this._color;
    },










    /**
     * @description
     * Highlight the line
     *
     * @return {odr.Line} The object on which you called this method
     */
    highlight : function() {
        var root = odr.canvas();
        var suspendID = root.suspendRedraw(5000);

        for(var attributeName in odr.settings.line.hoverAttributes) {
            this._element.setAttribute(attributeName, odr.settings.line.hoverAttributes[attributeName]);
        }

        for(var attributeName in odr.settings.line.arrow.hoverAttributes) {
            this._arrowElement.setAttribute(attributeName, odr.settings.line.arrow.hoverAttributes[attributeName]);
        }

        root.unsuspendRedraw(suspendID);

        return this;
    },









    /**
     * @description
     * Stop highlighting the line
     *
     * @return {odr.Line} The object on which you called this method
     */
    stopHighlight : function() {
        var root = odr.canvas();
        var suspendID = root.suspendRedraw(5000);

        for(var attributeName in odr.settings.line.attributes) {
            this._element.setAttribute(attributeName, odr.settings.line.attributes[attributeName]);
        }

        for(var attributeName in odr.settings.line.arrow.attributes) {
            this._arrowElement.setAttribute(attributeName, odr.settings.line.arrow.attributes[attributeName]);
        }

        root.unsuspendRedraw(suspendID);

        return this;
    },






    /**
     * @description
     * Remove this object, i.e. unbind all listeners, remove the entry from the registry and remove it from the SVG.
     * This object can't be undone.
     */
    remove : function() {
        if (this.visible()) {
            this._element.parentNode.removeChild(this._element);
            this._arrowElement.parentNode.removeChild(this._arrowElement);
        }

        if (this._source != null) {
            this._source.unbind(odr.Shape.listener.positionChanged, this.id());
        }

        if (this._target != null) {
            this._target.unbind(odr.Shape.listener.positionChanged, this.id());
        }

        odr.registry.remove(this.id());
    }

};

extend(odr.Line, odr.Drawable);


























odr.Label = function() {
    odr.Shape.call(this);

    this._element = null;
    this._label = "";

    this.addClass(odr.settings.label["class"]);

    for(var listenerType in odr.Label.listener) {
        this._listener[odr.Label.listener[listenerType]] = {};
    }

    this._paint();

    this.bind(odr.Drawable.listener.visibilityChanged,
        this._visibilityChanged.createDelegate(this),
        this.id());

    this.bind(odr.Drawable.listener.classesChanged,
        this._classesChanged.createDelegate(this),
        this.id());

    this.bind(odr.Drawable.listener.parentChanged,
        this._parentChanged.createDelegate(this),
        this.id());

    this.bind(odr.Shape.listener.markedChanged,
        this._markedChanged.createDelegate(this),
        this.id());

    this.bind(odr.Shape.listener.positionChanged,
        this._positionChanged.createDelegate(this),
        this.id());
};

/**
 * @namespace
 * Can be used in combination with {@link odr.Drawable#bind} and {@link odr.Drawable#unbind} as this object literal
 * defines the event types.
 */
odr.Label.listener = {
    /** @field */
    labelChanged : "labelChanged",
    /** @field */
    click : "click",
    /** @field */
    mousein : "mousein",
    /** @field */
    mouseout : "mouseout"
}

odr.Label.prototype = {
    _element : null,
    _label : "",




    /**
     * @description
     * You can also retrieve the current value by calling this method without parameters.
     *
     * @param {String|Number} [label] The new label
     * @return {String|Number|odr.Label} The label which was set or null if no label was set.
     * If you call this method with a parameter then the method will return the object on which you called the
     * method.
     */
    label : function(label) {
        if (label != undefined) {
            if (label != this._label) {
                if (label == null) {
                    label = "";
                }

                this._label = label;

                $(this._element).text(label);

                this.fire(odr.Label.listener.labelChanged, [this]);
            }

            return this;
        }


        return this._label;
    },








    /**
     * @private
     */
    _idPrefix : function() {
        return odr.settings.label.idPrefix;
    },








    /**
     * @private
     */
    _paint : function() {
        // prepare node div
        this._element = document.createElement("div");
        this._element.id = this.id();
        this._element.className = this.classString();
        this._element.style.position = "absolute";
        this._element.style.left = this.x() + "px";
        this._element.style.top = this.y() + "px";
        $(this._element).text(this._label);



        // add the node div to the parent
        var parent = this.parent();

        if (parent == undefined) {
            parent = document.getElementById(odr.settings.label.container);
        }

        parent.appendChild(this._element);



        // activate drag / drop
        // for some reason, $(this.__element).draggable(...) is not working, 'hence the node will be retrieved
        // using standard jQuery
        var jQueryHandle = $("#" + this.id());
        jQueryHandle.draggable(odr.settings.handle.jQueryUiDraggingSettings);





        // attach listeners to the draggable events
        jQueryHandle.bind("dragstart", this._positionChangedThroughUi.createDelegate(this));
        jQueryHandle.bind("drag", this._positionChangedThroughUi.createDelegate(this));
        jQueryHandle.bind("dragstop", this._positionChangedThroughUi.createDelegate(this));




        // attach additional listener
        jQueryHandle.bind("click", this._click.createDelegate(this));

        jQueryHandle.bind("mouseenter", function() {
            this.fire(odr.Label.listener.mousein, [this]);
        }.createDelegate(this));

        jQueryHandle.bind("mouseleave", function() {
            this.fire(odr.Label.listener.mouseout, [this]);
        }.createDelegate(this));
    },










    /**
     * @private
     * @override
     */
    center : function() {
        return {
            x : this.x() + ($(this._element).width() / 2),
            y : this.y() + ($(this._element).height() / 2)
        };
    },











    /**
     * @private
     */
    _positionChangedThroughUi : function(e) {
        if (odr.user.lowPerformanceMode && e.type != "dragstop") {
            return;
        }

        var uiPosition = $(this._element).position();
        var entityPosition = this.position();

        this.position(uiPosition.left, uiPosition.top);

        odr.moveMarkedShapes(uiPosition.left - entityPosition.x, uiPosition.top - entityPosition.y, this);
    },











    /**
     * @private
     */
    _positionChanged : function() {
        this._element.style.left = this.x() + "px";
        this._element.style.top = this.y() + "px";
    },


    






    /**
     * @private
     */
    _click : function(e) {
        if(e.ctrlKey) {
            this.marked(!this.marked());
        } else {
            this.fire(odr.Label.listener.click, [this]);
        }
    },








    /**
     * @private
     */
    _visibilityChanged : function() {
        var display = null;

        if (this.visible()) {
            display = "block";
        } else {
            display = "none";
        }

        this._element.style.display = display;
    },






    /**
     * @private
     */
    _classesChanged : function() {
        this._element.className = this.classString();
    },






    /**
     * @private
     */
    _parentChanged : function() {
        // Inserting the DOM element a second time will automatically remove it from
        // it's previous position.
        this.parent().appendChild(this._element);
    },







    /**
     * @private
     */
    _markedChanged : function() {
        if (this.marked()) {
            this.addClass(odr.settings.label.markedClass);
        } else {
            this.removeClass(odr.settings.label.markedClass);
        }
    }
};

extend(odr.Label, odr.Shape);