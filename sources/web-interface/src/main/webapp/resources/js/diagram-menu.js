/**
 * @fileOverview
 *
 * This file contains menu related functionality.
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */




/*
 * Enable the animation of the small menu on the top of the page.
 */
odr.bootstrap(function() {
    $("." + odr.settings.menu.top["class"]).mouseenter(function() {
        $(this).stop(true, false).animate({
            "top" : odr.settings.menu.top.expandedHeight($(this))
        }, odr.settings.menu.top.animationDuration);
    });

    $("." + odr.settings.menu.top["class"]).mouseleave(function() {
        $(this).stop(true, false).animate({
            "top" : odr.settings.menu.top.collapsedHeight($(this))
        }, odr.settings.menu.top.animationDuration);
    });
});