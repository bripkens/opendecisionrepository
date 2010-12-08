/**
 * @fileOverview
 *
 * This file contains all drawable items, e.g. nodes
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */


/*
 * ###############################################################################################################
 *                                              Set up
 */
odr.bootstrap(function() {
    odr.registry = new odr.Registry();
});













/*
 * ###############################################################################################################
 *                                              Shared functionality
 */
/**
 * @description
 * This function always returns a new unique id, i.e. a number. The first id is 0.
 *
 * @return {Number} a unique id
 */
odr.newId = function() {
    return odr.vars.idCounter++;
}










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
     * @param {{String|Number}} itemId
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
 * @description Every entity automatically registers itself with the {@link odr.Registry} so that the entity can be retrieved by it's id.
 */
odr.Drawable = function() {
    this._id = odr.newId() + this._idPrefix();
    this._visible = true;
    this._marked = false;
    this._parent = null;
    this._json = null;
    this._classes = [];
    this._listener = {
        paint : {},
        visibilityChanged : {}
    };
}

/**
 * @namespace
 * Can be used in combination with {@link odr.Drawable#bind} and {@link odr.Drawable#unbind} as this object literal
 * defines the event types.
 */
odr.Drawable.listener = {
    /** @field */
    paint : "paint",
    /** @field */
    visibilityChanged : "visibilityChanged"
}

odr.Drawable.prototype = {
    _id : -1,
    _visible : true,
    /**
     * @private
     * @description
     * Whether this drawable is marked
     */
    _marked : false,
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
     * Show or hide this DrawableItem. When changing the visibility of an iten it will be painted or removed, depending
     * on the new visiblity.
     *
     * Initially each DrawableItem is visible.
     *
     * You can also retrieve the current value by calling this method without parameters.
     *
     * @param {Boolean} [visible] The new visibility
     * @return {Boolean|odr.Drawable} The visibility which was set or null if no parent was set. If you call this
     * method with a parameter then the method will return the object on which you called the method
     * (<a href="http://martinfowler.com/bliki/FluentInterface.html">Fluent Interface</a>).
     */
    visible : function(visible) {
        if (visible != undefined) {
            this._visible = visible;

            return this;
        }

        return this._visible;
    },




    
    bind : function(type, listener, identification) {
        var listenerCollection = this._listener[type];

        if (listenerCollection == undefined) {
            throw ("The specified listener type " + type + "is not valid.");
        }

        if (identification == undefined) {
            identification = listener;
        }

        listenerCollection[identification] = listener;
    },





    unbind : function(type, identification) {
        var listenerCollection = this._listener[type];

        if (listenerCollection == undefined) {
            throw ("The specified listener type " + type + "is not valid.");
        }


        delete listenerCollection[identification];
    },







    fire : function(type, params) {
        var listenerCollection = this._listener[type];

        if (listenerCollection == undefined) {
            throw ("The specified listener type " + type + "is not valid.");
        }

        for(var listener in listenerCollection) {
            listenerCollection[listener].callWithParams(params);
        }
    }
}











odr.Shape = function() {
    odr.Drawable.call(this);

    this._listener.positionChanged = {};
}

odr.Shape.listener = {
    /** @field */
    positionChanged : "positionChanged"
}

odr.Shape.prototype = {
    /**
     * @private
     */
    _idPrefix : function() {
        return "fooPrefix";
    },



    x : function(x) {
        odr.Shape.superClass.fire.call(this, odr.Shape.listener.positionChanged, [this]);
    }
}

extend(odr.Shape, odr.Drawable);