/**
 * @fileOverview
 *
 * Menu related functionality that can be changed without affecting the diagram drawing functionality.
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
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
/**
 * @description
 * Attach listeners to all the zoom menu buttons and images
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

/**
 * @description
 * This functions handles scale level changes, i.e. when the user enters something into the zoom level text box
 * and hits enter or presses the button below.
 */
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
 *                            Projects menu
 */
odr.ready(function() {
    j("#save").click(odr._saveAll);
    j("#back").click(function() {
        window.location = j("#externalVarBackUrl").text();
    });
    j("#refresh").click(function() {
        window.location = window.location;
    });
});
/**
 * @description
 * Save the positions of all nodes as well as handles by copying the data into the object that was retrieved
 * from the server.
 */
odr._saveAll = function() {
    odr.setSaveText();
    odr.loadPopupIcon();
    odr.showWaitAnimation();


    for(var i in odr._allRectangles) {
        var currentRectangle = odr._allRectangles[i];
        var value = currentRectangle.value();
        
        value.X = currentRectangle.x();
        value.Y = currentRectangle.y();
        value.Visible = currentRectangle.visible();
    }

    for(var i in odr._allAssociations) {
        var currentAssociation = odr._allAssociations[i];
        var value = currentAssociation.value();

        if (currentAssociation._labelPosition != null) {
            value.LabelX = currentAssociation._labelPosition.x;
            value.LabelY = currentAssociation._labelPosition.y;
        }

        var handles = currentAssociation.handles();

        var handlesForValue = [];

        for (var k = 0; k < handles.length; k++) {
            var currentHandle = handles[k];

            handlesForValue[handlesForValue.length] = {
                X : currentHandle.x(),
                Y : currentHandle.y()
            };
        }

        value.Handles = handlesForValue;
    }

    var targetUrl = j("#externalVarTargetUrl").text();

    var projectIdParameter = j("#externalVarProjectIdParameterName").text();
    var dataParameter = j("#externalVarDataParameterName").text();
    var viewpointParameter = j("#externalVarViewpointParameterName").text();
    
    var parameter = {};
    parameter[projectIdParameter] = j("#externalVarProjectId").text();
    parameter[dataParameter] = odr._createJSONString(odr._requestedData);
    parameter[viewpointParameter] = true;


    j.ajax({
        url : targetUrl,
        data : parameter,
        dataType : "json",
        type : "POST",
        success : function(data, textStatus, XMLHttpRequest) {
            odr.setSuccessText();
            odr.successPopupIcon();
            j(".clickBlocker").hide();
            odr.hideWaitAnimation(4000);
        },
        error : function(data, textStatus, errorThrown) {
            odr.setErrorText();
            odr.errorPopupIcon();
        }
    });
};


/**
 * @description
 * The following function could be replaced by a JavaScript JSON serializer like
 * JSON2 or the jquery json plugin. But for some reason, every quote is escaped.
 *
 * http://groups.google.com/group/google-gson/browse_thread/thread/98249309c8455a91
 *
 * One good thing about this approach is that only the absolutely necessary information is sent to the server.
 *
 * @param {Object} data The original JSON data that should be transformed
 */
odr._createJSONString = function(data) {
    var json = [];

    json.push('{"Id":',
        data.Id,
        ',"Nodes":[');


    for (var i = 0; i < data.Nodes.length; i++) {
        var currentNode = data.Nodes[i];

        json.push('{"Id":',
            currentNode.Id,
            ',"Visible":',
            currentNode.Visible,
            ',"X":',
            currentNode.X,
            ',"Y":',
            currentNode.Y,
            '}'
            );

        if (i+1 != data.Nodes.length) {
            json.push(',');
        }
    }

    json.push('],"Associations":[');

    for(var i = 0; i < data.Associations.length; i++) {
        var currentAssociation = data.Associations[i];

        json.push('{"Id":',
            currentAssociation.Id,
            ',"LabelX":',
            currentAssociation.LabelX,
            ',"LabelY":',
            currentAssociation.LabelY,
            ',"Handles":[');

        for(var k = 0; k < currentAssociation.Handles.length; k++) {
            var currentHandle = currentAssociation.Handles[k];

            json.push('{"X":',
                currentHandle.X,
                ',"Y":',
                currentHandle.Y,
                '}');

            if (k+1 != currentAssociation.Handles.length) {
                json.push(',');
            }
        }

        json.push("]}");

        if (i+1 != data.Associations.length) {
            json.push(',');
        }
    }
    
    json.push(']}');

    return json.join("");
}










/*
 * ###########################################################################
 *            Utility functionality to make use of the menu
 */

odr._nodes = {};

/**
 * @description
 * Use this utility functionality to add nodes. It has several benefits as it will make sure that the node will be
 * added to the nodes menu and it will also add the node to the status menu.
 *
 * @param {String} name The name which should be shown for this node
 * @param {String} stereotype The stereotype that will be visible in the status menu
 * @param {String} stereotypeToShow The stereotype that will be shown above the name of the decision in the node and in the decisions menu
 * @param {Number} x The x position
 * @param {Number} y The y position
 * @param {Boolean} [visible] Whether this node is visible. Default = false
 * @param {Boolean} [round] Whether the node has round corners. Default = true
 * @param {String} [extraClasses] Some extra classes that you want to attach {@link odr.Rectangle#extraClasses}. Default = ""
 * @return {odr.Rectangle} The new node. The node has been painted to the canvas!
 */
odr.addNode = function(name, stereotype, stereotypeToShow, x, y, visible, round, extraClasses) {
    if (visible == undefined) {
        visible = false;
    }

    if (round == undefined) {
        round = true;
    }

    if (extraClasses == undefined) {
        extraClasses = "";
    }

    var rectangle = new odr.Rectangle();
    rectangle.x(x);
    rectangle.y(y);
    rectangle.label(name);
    rectangle.stereotype(stereotypeToShow);
    rectangle.round(round);
    rectangle.extraClasses(extraClasses);
    rectangle.paint();


    var container = j(document.createElement("div")).
    addClass("decisionSelector").
    appendTo("div.decisions div.overflowDecisions").
    append(j(document.createElement("span")).text(name)).
    append(j(document.createElement("span")).text(stereotype).addClass("stereotype"));

    var showText = j("span#decisionShowButtonText").text();
    var hideText = j("span#decisionHideButtonText").text();

    container.html(container.html() + '<input type="button" class="hide" value="' + hideText + '"/>');

    var button = container.children("input");

    button.click(function() {
        if (button.hasClass("hide")) {
            button.removeClass("hide");
            button.addClass("show");
            button.val(showText);

            rectangle.visible(false);
        } else {
            button.removeClass("show");
            button.addClass("hide");
            button.val(hideText);

            rectangle.visible(true);

            odr.assertContainerSize();
        }
    });

    rectangle.visibility(function() {
        if (rectangle.visible()) {
            button.removeClass("show");
            button.addClass("hide");
            button.val(hideText);
        } else {
            button.removeClass("hide");
            button.addClass("show");
            button.val(showText);
        }
    }, "menuListener");







    if (odr._nodes[stereotype] == undefined) {
        odr._nodes[stereotype] = [];
        odr._newStereotype(stereotype, odr._nodes[stereotype]);
    }

    var items = odr._nodes[stereotype];

    items[items.length] = rectangle;

    rectangle.visibility(function() {
        var allVisible = true;
        var allInvisible = true;

        for(var i = 1; i < items.length; i++) {
            if (items[i].visible()) {
                allInvisible = false;
            } else {
                allVisible = false;
            }
        }

        if (allVisible) {
            items[0].attr("checked", "checked");
            items[0].attr("checked", true);
            items[0].get().checked = true;
            
        } else if (allInvisible) {
            items[0].attr("checked", false);
            items[0].removeAttr("checked");
            items[0].get().checked = false;
        }
    }, "stereotypeListener");

    rectangle.visible(visible);

    return rectangle;
}


odr._newStereotype = function(name, items) {
    var container = j(document.createElement("div")).
    appendTo("div.status div.overflowStatus").
    append(j(document.createElement("span")).text(name));

    container.html('<input type="checkbox" checked="true" id="stereotypeCheckbox' + name + '"/>' + container.html());

    var checkbox = container.children("input");

    items[0] = checkbox;

    checkbox.change(function() {

        var checked = checkbox.is(":checked");
        
        for(var i = 1; i < items.length; i++) {
            items[i].visible(checked);
        }
    });
}










/*
 * ###########################################################################
 *                         Loading animation and status
 */
/**
 * @description
 * Show the wait animation popup (actually this popup can also be used to represent errors or the success status).
 *
 * @param {String|Number} [animationSpeed] Either "fast" or "slow" for 200 respectively 600 milliseconds or
 * the time in milliseconds. Default speed is 200 milliseconds.
 *
 */
odr.showWaitAnimation = function(animationSpeed) {
    if (animationSpeed == undefined) {
        animationSpeed = "fast";
    }

    var windowWidth = j(document).width();
    var windowHeight = j(document).height();
    var popupHeight = j(".loader").height();
    var popupWidth = j(".loader").width();

    j(".clickBlocker").css({
        "height": windowHeight,
        "width": windowWidth
    });

    j(".loader").css({
        "top": windowHeight/2-popupHeight/2,
        "left": windowWidth/2-popupWidth/2
    });

    j(".clickBlocker").fadeIn(animationSpeed);
    j(".loader").fadeIn(animationSpeed);
}

/**
 * @description
 * Hide the small popup
 *
 * @param {String|Number} [animationSpeed] Either "fast" or "slow" for 200 respectively 600 milliseconds or
 * the time in milliseconds. Default speed is 200 milliseconds.
 */
odr.hideWaitAnimation = function(animationSpeed) {
    if (animationSpeed == undefined) {
        animationSpeed = "fast";
    }

    j(".loader").fadeOut(animationSpeed);
    j(".clickBlocker").fadeOut(animationSpeed);
}
/**
 * @description
 * Set an icon for the small popup
 *
 * @param {String} url The url to the icon including filename and filename extension
 */
odr.popupIcon = function(url) {
    j(".loader").css("background-image", "url(" + url + ")");
}
/**
 * @description
 * Set the default load icon
 */
odr.loadPopupIcon = function() {
    odr.popupIcon("resources/images/ajax-loader-circle.gif");
}
/**
 * @description
 * Set the default error icon
 */
odr.errorPopupIcon = function() {
    odr.popupIcon("resources/images/error-big.png");
}
/**
 * @description
 * Set the default success icon
 */
odr.successPopupIcon = function() {
    odr.popupIcon("resources/images/success-big.png");
}
/**
 * @description
 * Set the text that is shown below the icon in the popup
 *
 * @param {String} text The text which you want to show
 */
odr.popupText = function(text) {
    j(".loader").children("span").text(text);
}
/**
 * @description
 * Set the default success text
 */
odr.setSuccessText = function() {
    odr.popupText(j("#successText").text());
}
/**
 * @description
 * Set the default error text
 */
odr.setErrorText = function() {
    odr.popupText(j("#errorText").text());
}
/**
 * @description
 * Set the default saving text
 */
odr.setSaveText = function() {
    odr.popupText(j("#savingText").text());
}
/**
 * @description
 * Set the default loading text
 */
odr.setLoadingText = function() {
    odr.popupText(j("#loadingText").text());
}
