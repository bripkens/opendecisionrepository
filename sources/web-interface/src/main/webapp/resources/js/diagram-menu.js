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





/*
 * Enable the bottom menu for the performance mode
 */
odr.bootstrap(function() {
    var out = $(odr.settings.menu.bottom.performance.out);
    var high = $(odr.settings.menu.bottom.performance.high);
    var low = $(odr.settings.menu.bottom.performance.low);
    
    var setCurrent = function(element) {
        out.text(element.children().first().text());
    }

    high.click(function() {
        setCurrent(high);
        odr.user.lowPerformanceMode = false;
        high.addClass(odr.settings.menu.bottom.selectedClass);
        low.removeClass(odr.settings.menu.bottom.selectedClass);
    });

    low.click(function() {
        setCurrent(low);
        odr.user.lowPerformanceMode = true;
        low.addClass(odr.settings.menu.bottom.selectedClass);
        high.removeClass(odr.settings.menu.bottom.selectedClass);
    });
});






/*
 * Enable the bottom menu for the alignment dialog
 */
odr.bootstrap(function() {
    var out = $(odr.settings.menu.bottom.alignment.out);
    var on = $(odr.settings.menu.bottom.alignment.on);
    var off = $(odr.settings.menu.bottom.alignment.off);
    var ask = $(odr.settings.menu.bottom.alignment.ask);

    var setCurrent = function(element) {
        out.text(element.children().first().text());
    }

    on.click(function() {
        setCurrent(on);
        odr.user.automaticallyAlignDragHandles = true;
        on.addClass(odr.settings.menu.bottom.selectedClass);
        off.removeClass(odr.settings.menu.bottom.selectedClass);
        ask.removeClass(odr.settings.menu.bottom.selectedClass);
    });

    off.click(function() {
        setCurrent(off);
        odr.user.automaticallyAlignDragHandles = false;
        off.addClass(odr.settings.menu.bottom.selectedClass);
        on.removeClass(odr.settings.menu.bottom.selectedClass);
        ask.removeClass(odr.settings.menu.bottom.selectedClass);
    });

    ask.click(function() {
        setCurrent(ask);
        odr.user.automaticallyAlignDragHandles = undefined;
        ask.addClass(odr.settings.menu.bottom.selectedClass);
        on.removeClass(odr.settings.menu.bottom.selectedClass);
        off.removeClass(odr.settings.menu.bottom.selectedClass);
    });
});
