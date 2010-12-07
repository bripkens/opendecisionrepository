/**
 * @fileOverview
 *
 * This file contains definitions, settings and variable declarations
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */

/**
 * @namespace The namespace for the whole diagram JavaScript.
 */
var odr = odr || {};





/*
 * ###############################################################################################################
 *                                              Settings
 */

/**
 * @namespace
 * ODR settings
 */
odr.settings = {
    menu : {
        top : {
            "class" : "topMenu",
            expandedHeight : function() {
                return "-15px";
            },
            collapsedHeight : function(element) {
                return (element.height() + 11) * -1;
            },
            animationDuration : 500
        }
    },
    dragging : {
        jQueryUiSettings : {
            grid : [10, 10],
            snap : true,
            scroll: true,
            stack : ".node"
        }
    },
    resizing : {
        jQueryUiSettings : {
            grid : 10
        }
    }
};







/*
 * ###############################################################################################################
 *                                              Variable declarations
 */

/**
 * @private
 */
odr.vars = {
    bootstrapFunctions : [],
    readyFunctions : []
}