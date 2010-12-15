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
    this._json = null;
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
     * JSON data which was used when creating this object
     */
    _json : null,
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
        if (odr.settings.lowPerformanceMode && e.type != "dragstop") {
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
        if (odr.settings.lowPerformanceMode && e.type != "resizestop") {
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

        this.size($(this._element).width(), $(this._element).height());
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
    containmentChanged : "containmentChanged"
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



        // activate drag / drop and resizing
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
            this.addClass(odr.settings.node.markedClass);
        } else {
            this.removeClass(odr.settings.node.markedClass);
        }
    },







    /**
     * @private
     * @description
     * Will be called when the handle is dragged in the user interface through jQuery ui
     */
    _positionChangedThroughUi : function(e) {
        if (odr.settings.lowPerformanceMode && e.type != "dragstop") {
            return;
        }

        var uiPosition = $(this._element).position();
        var entityPosition = this.position();

        this.position(uiPosition.left, uiPosition.top);
        this._relativeX = uiPosition.left - this._containment.x();
        this._relativeY = uiPosition.top - this._containment.y();

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

    this._sourceHandle = new odr.Handle().visible(true);
    this._targetHandle = new odr.Handle().visible(true);

    this._handles = [];

    this.addClass(odr.settings.association["class"]);

    for(var listenerType in odr.Association.listener) {
        this._listener[odr.Association.listener[listenerType]] = {};
    }

    this._paint();


//    odr.Node.superClass.bind.call(this,
//        odr.Drawable.listener.visibilityChanged,
//        this._visibilityChanged.createDelegate(this),
//        "_node_" + this.id());
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
    targetChanged : "targetChanged"
}

odr.Association.prototype = {
    _element : null,

    _source : null,
    _sourceHandle : null,
    _target : null,
    _targetHandle : null,
    _handles : [],







    /**
     * @private
     */
    _idPrefix : function() {
        return odr.settings.association.idPrefix;
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
                this._source = source;

                odr.Association.superClass.fire.call(this, odr.Association.listener.sourceChanged, [this]);
                
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
                this._target = target;

                odr.Association.superClass.fire.call(this, odr.Association.listener.targetChanged, [this]);
                
                this._targetHandle.containment(target);
            }

            return this;
        }

        return this._target;
    },





    


    /**
     * @private
     */
    _paint : function() {
        this._element = document.createElementNS(svgns, "g");
        this._element.setAttribute("id", this.id());
        this._element.setAttribute("class", this.classString());

        // add the node div to the parent
        var parent = this.parent();

        if (parent == undefined) {
            parent = document.getElementById(odr.settings.association.container);
        }

        parent.appendChild(this._element);
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

    this._hover = false;

    this.addClass(odr.settings.line["class"]);

    for(var listenerType in odr.Line.listener) {
        this._listener[odr.Line.listener[listenerType]] = {};
    }

    this._paint();

    this.bind(odr.Drawable.listener.visibilityChanged, this._visibilityChanged.createDelegate(this), this.id());
    this.bind(odr.Drawable.listener.classesChanged, this._classesChanged.createDelegate(this), this.id());
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

        // add the node div to the parent
        var parent = this.parent();

        if (parent == undefined) {
            parent = document.getElementById(odr.settings.line.container);
        }

        parent.appendChild(this._element);

        this._element.addEventListener("click", function() {
            this.fire(odr.Line.listener.click, [this]);
        }.createDelegate(this));

        this._element.addEventListener("mouseover", function() {
            if (!this._hover) {
                this.fire(odr.Line.listener.mousein, [this]);
                this._hover = true;
            }
            this.fire(odr.Line.listener.mouseover, [this]);
        }.createDelegate(this));

        this._element.addEventListener("mouseout", function() {
            this.fire(odr.Line.listener.mouseout, [this]);
            this._hover = false;
        }.createDelegate(this));

        root.unsuspendRedraw(suspendID);
    },







    /**
     * @private
     */
    _paintArrow : function() {
        
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
        }
    },






    /**
     * @private
     */
    _classesChanged : function() {
        this._element.setAttribute("class", this.classString());
    }
};

extend(odr.Line, odr.Drawable);