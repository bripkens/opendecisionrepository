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
 *                            Projects menu
 */
odr.ready(function() {
    j("#save").click(odr._saveAll);
});
odr._saveAll = function() {
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

        console.log(odr._requestedData);

    var targetUrl = j("#externalVarTargetUrl").text();
    //    console.log(JSON.stringify(odr._requestedData, 4))
    //    console.log(foo);
    console.log(odr._createJSONString(odr._requestedData));
    j.ajax({
        url : targetUrl,
        data : {
            "data" : odr._createJSONString(odr._requestedData)
        },
        dataType : "json",
        type : "POST",
        success : function(data, textStatus, XMLHttpRequest) {
            alert("Success: " + data + "; Status: " + textStatus);
        },
        error : function(data, textStatus, errorThrown) {
            alert("Error: " + data + "; Status: " + textStatus);
        }
    });
};


/*
 * The following function could be replaced by a JavaScript JSON serializer like
 * JSON2 or the jquery json plugin. But for some reason, every quote is escaped.
 *
 * http://groups.google.com/group/google-gson/browse_thread/thread/98249309c8455a91
 *
 * One good thing about this approach is that only the absolutely necessary information is sent to the server.
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

odr.addNode = function(name, stereotype, x, y, visible) {
    if (visible == undefined) {
        visible = false;
    }

    

    var rectangle = new odr.Rectangle();
    rectangle.x(x);
    rectangle.y(y);
    rectangle.label(name);
    rectangle.stereotype(stereotype);
    rectangle.paint();


    var container = j(document.createElement("div")).
    addClass("decisionSelector").
    appendTo("div.decisions div.overflowDecisions").
    append(j(document.createElement("span")).text(name));

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