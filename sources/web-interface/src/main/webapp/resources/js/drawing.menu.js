/*
 * Author: Ben Ripkens <bripkens.dev@gmail.com>
 */


/*
 * ###########################################################################
 *                              Menu settings
 */
odr.smallMenu = "smallMenu";
odr.smallMenuExpandedTop = "-15px";
odr.smallMenuCollapsedTop = function(height) {
    return (height + 11) * -1;
}
odr.smallMenuAnimationDuration = 500;

odr.zoomMenu = {
    inputButton : "inputSetScale",
    inputField : "inputScale",
    errorClass : "error",
    inButton : "zoomIn",
    outButton : "zoomOut"
}



/*
 * ###########################################################################
 *                            Menu initialization
 */
odr.init(function() {
    j("." + odr.smallMenu).mouseenter(function() {
        j(this).stop(true, false).animate({
            "top" : odr.smallMenuExpandedTop
        }, odr.smallMenuAnimationDuration);
    });

    j("." + odr.smallMenu).mouseleave(function() {
        j(this).stop(true, false).animate({
            "top" : odr.smallMenuCollapsedTop(j(this).height())
        }, odr.smallMenuAnimationDuration);
    });
});







/*
 * ###########################################################################
 *                            Zoom menu
 */
odr.prepareZoom = function() {
    j("#" + odr.zoomMenu.inputButton).click(odr.handleScaleInput);

    j("#" + odr.zoomMenu.inButton).click(function() {
        odr.scale(odr.scale() + 0.1);
        j("#" + odr.zoomMenu.inputField).val(Math.round(odr.scale() * 100));
    });

    j("#" + odr.zoomMenu.outButton).click(function() {
        odr.scale(odr.scale() - 0.1);
        j("#" + odr.zoomMenu.inputField).val(Math.round(odr.scale() * 100));
    });

    j("#" + odr.zoomMenu.inputField).keyup(function(e) {
        if(e.keyCode == 13) {
            odr.handleScaleInput();
        }
    });

    j("#" + odr.zoomMenu.inputField).val(odr.scale() * 100);
}

odr.handleScaleInput = function() {
    var scaleInput = j("#" + odr.zoomMenu.inputField);

    var newScale = scaleInput.val();

    newScale = parseFloat(newScale);

    if (isNaN(newScale)) {
        scaleInput.addClass(odr.zoomMenu.errorClass);
        return;
    }

    scaleInput.removeClass(odr.zoomMenu.errorClass);

    odr.scale(newScale / 100);
}


odr.ready(odr.prepareZoom);







/*
 * ###########################################################################
 *                            Export menu
 */
odr.ready(function() {
    j("div.export ul li").click(function() {

        odr.beforeExport();

        var format = j(this).attr("class");

        j.get(odr.css.url, function(cssStyle) {
            var form = j("div.export form");

            var dataInput = form.children("input#data");
            dataInput.val(j("#" + odr.svgContainer.id).html().replace(odr.css.inlineStyle, cssStyle));

            var formatInput = form.children("input#format");
            formatInput.val(format);

            form.submit();

            odr.afterExport();
        });

    })
});






/*
 * ###########################################################################
 *                            Export menu
 */
odr.addNode = function(name, x, y, visible) {
    if (visible == undefined) {
        visible = false;
    }

    var rectangle = new odr.Rectangle();
    rectangle.x(x);
    rectangle.y(y);
    rectangle.label(name);
    rectangle.paint();


    var container = j(document.createElement("div")).
    addClass("decisionSelector").
    appendTo("div.decisions div.overflow").
    append(j(document.createElement("span")).text(name));

    container.html(container.html() + '<input type="button" class="hide" value="Hide"/>');

    var button = container.children("input");

    button.click(function() {
        if (button.hasClass("hide")) {
            button.removeClass("hide");
            button.addClass("show");
            button.val("Show");

            rectangle.visible(false);
        } else {
            button.removeClass("show");
            button.addClass("hide");
            button.val("Hide");

            rectangle.visible(true);
        }
    });

    rectangle.visibility(function() {
        if (rectangle.visible()) {
            button.removeClass("show");
            button.addClass("hide");
            button.val("Hide");
        } else {
            button.removeClass("hide");
            button.addClass("show");
            button.val("Show");
        }
    }, "menuListener");

    rectangle.visible(visible);

    return rectangle;
}

