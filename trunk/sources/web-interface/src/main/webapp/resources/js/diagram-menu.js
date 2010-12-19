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










/**
 * @description
 * Use this utility functionality to add nodes. It has several benefits as it will make sure that the node will be
 * added to the nodes menu and it will also add the node to the status menu.
 *
 * @param {String} name The name which should be shown for this node
 * @param {String} status The status that will be visible in the status menu
 * @param {String} statusToShow The status that will be shown above the name of the node in the node and in the decisions menu
 * @param {Boolean} [visible] Whether this node is visible. Default = false
 * @return {odr.Node} The new node.
 */
odr.addNode = function(name, status, statusToShow, visible) {
    if (visible == undefined) {
        visible = false;
    }

    var node = new odr.Node();
    node.label(name);
    node.status(statusToShow);

    var container = $(document.createElement("div")).
    addClass("decisionSelector").
    appendTo("div.decisions div.overflowDecisions").
    append($(document.createElement("span")).text(name)).
    append($(document.createElement("span")).text(status).addClass("stereotype"));

    var showText = odr.translation.text["menu.nodes.show"];
    var hideText = odr.translation.text["menu.nodes.hide"];

    var currentText, currentClass;

    if (node.visible()) {
        currentText = hideText;
        currentClass = "hide";
    } else {
        currentText = showText;
        currentClass = "show";
    }

    container.html(container.html() + '<input type="button" class="' + currentClass + '" value="' + currentText + '"/>');

    var button = container.children("input");

    button.click(function() {
        if (button.hasClass("hide")) {
            button.removeClass("hide");
            button.addClass("show");
            button.val(showText);

            node.visible(false);
        } else {
            button.removeClass("show");
            button.addClass("hide");
            button.val(hideText);

            node.visible(true);
        }
    });

    node.bind(odr.Drawable.listener.visibilityChanged, function() {
        if (node.visible()) {
            button.removeClass("show");
            button.addClass("hide");
            button.val(hideText);
        } else {
            button.removeClass("hide");
            button.addClass("show");
            button.val(showText);
        }
    }, "nodeMenuListener");




    if (odr.vars.statusGroups[statusToShow] == undefined) {
        odr.vars.statusGroups[statusToShow] = [];
        odr._newStatusGroup(statusToShow, odr.vars.statusGroups[statusToShow]);
    }

    var statusGroup = odr.vars.statusGroups[statusToShow];

    statusGroup.push(node);

    node.bind(odr.Drawable.listener.visibilityChanged, function() {
        var allVisible = true;
        var allInvisible = true;

        for(var i = 1; i < statusGroup.length; i++) {
            if (statusGroup[i].visible()) {
                allInvisible = false;
            } else {
                allVisible = false;
            }
        }

        if (allVisible) {
            statusGroup[0].attr("checked", true);
        } else if (allInvisible) {
            statusGroup[0].removeAttr("checked");
        } else {
            statusGroup[0].removeAttr("checked");
        }
        
    }, "statusMenuListener");
    

    node.visible(visible);

    return node;
}





/**
 * @private
 * @description
 * Create a new status group entry in the menu
 *
 * @param {String} name The name of the group
 * @param {odr.Drawable[]} items Some items which visibility is to be controled
 */
odr._newStatusGroup = function(name, items) {
    var container = $(document.createElement("div")).
    appendTo("div.status div.overflowStatus").
    append($(document.createElement("span")).text(name));

    container.html('<input type="checkbox" checked="true" id="stereotypeCheckbox' + name + '"/>' + container.html());

    var checkbox = container.children("input");

    items[0] = checkbox;

    checkbox.unbind().change(function() {
        var checked = checkbox.is(":checked");

        for(var i = 1; i < items.length; i++) {
            items[i].visible(checked);
        }
    });
}














/*
 * ###############################################################################################################
 *                                              Projects menu
 */
odr.ready(function() {
    $("#save a").click(function() {
        odr._saveAll();
        return false;
    });

    $("#back a").attr("href", odr.settings.request.project.replace("{0}",
        odr.vars.requestParameter[odr.settings.request.parameter.projectId]));

    $("#toRelationshipView a").attr("href", odr.settings.request.visualization.
        replace("{0}", odr.vars.requestParameter[odr.settings.request.parameter.projectId]).
        replace("{1}", odr.settings.request.parameter.relationshipView));

    $("#toChronologicalView a").attr("href", odr.settings.request.visualization.
        replace("{0}", odr.vars.requestParameter[odr.settings.request.parameter.projectId]).
        replace("{1}", odr.settings.request.parameter.chronologicalView));

    $("#toStakeholderView a").attr("href", odr.settings.request.visualization.
        replace("{0}", odr.vars.requestParameter[odr.settings.request.parameter.projectId]).
        replace("{1}", odr.settings.request.parameter.stakeholderView));
            
    $("#refresh a").click(function() {
        window.location = window.location;
        return false;
    });
});







/**
 * @private
 */
odr._saveAll = function() {
    var popupId = odr.popup.showSave();

    for(var e in odr.vars.allDecisionNodes) {
        odr._saveNodeData(odr.vars.allDecisionNodes[e]);
    }
    
    for(var e in odr.vars.allIterationNodes) {
        odr._saveNodeData(odr.vars.allIterationNodes[e]);
    }

    for(var i in odr.vars.allAssociations) {
        var currentAssociation = odr.vars.allAssociations[i];
        var json = currentAssociation.json;

        var labelPosition = currentAssociation.labelPosition();


        json.LabelX = labelPosition.x;
        json.LabelY = labelPosition.y;

        var handles = currentAssociation.handles();

        var handlesForValue = [];

        for (var k = 0; k < handles.length; k++) {
            var currentHandle = handles[k];

            handlesForValue.push({
                X : currentHandle.x(),
                Y : currentHandle.y()
            });
        }

        json.Handles = handlesForValue;
    }

    var parameter = {};
    parameter[odr.settings.request.parameter.projectId] = odr.vars.requestParameter[odr.settings.request.parameter.projectId];
    parameter[odr.settings.request.parameter.data] = odr._createJSONString(odr.vars.json);
    parameter[odr.vars.requestedViewpoint] = true;

    $.ajax({
        url : odr.settings.request.dataReceiver,
        data : parameter,
        dataType : "json",
        type : "POST",
        success : function(data, textStatus, XMLHttpRequest) {
            odr.popup.close(popupId);
        },
        error : function(data, textStatus, errorThrown) {
            odr.popup.showError("Could not save visualization: " + textStatus);
        }
    });
};







/**
 * @private
 */
odr._saveNodeData = function(node) {
    var json = node.json;

    json.X = node.x();
    json.Y = node.y();
    json.Width = node.width();
    json.Height = node.height();
    json.Visible = node.visible();
}






/**
 * @private
 *
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
            ',"Width":',
            currentNode.Width,
            ',"Height":',
            currentNode.Height,
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
 * ###############################################################################################################
 *                                              Additional information button
 */
/**
 * @description
 * Show details about the given node
 *
 * @param {odr.Node} node The node for which additional information should be shown
 */
odr.showAdditionalDecisionDetails = function(node) {
    var json = node.json;

    var url = odr.settings.request.decisionDetails.
    replace("{0}", odr.vars.requestParameter[odr.settings.request.parameter.projectId]).
    replace("{1}", json.Version.Decision.Id).
    replace("{2}", json.Version.Id);

    window.open(url);
}





/**
 * @description
 * Show details about the given node
 *
 * @param {odr.Node} node The node for which additional information should be shown
 */
odr.showAdditionalIterationDetails = function(node) {
    var json = node.json;

    var url = odr.settings.request.iterationDetails.
    replace("{0}", odr.vars.requestParameter[odr.settings.request.parameter.projectId]).
    replace("{1}", json.Iteration.Id);

    window.open(url);
}














/*
 * ###############################################################################################################
 *                                              Exporting
 */
odr.ready(function() {
    $("div.export ul li").click(function() {
        var format = $(this).attr("class");

        var nodes = [];

        var minX = Number.MAX_VALUE, maxX = Number.MIN_VALUE, minY = Number.MAX_VALUE, maxY = Number.MIN_VALUE;

        for(var key in odr.vars.shapesThatDetermineCanvasSize) {
            var shape = odr.vars.shapesThatDetermineCanvasSize[key];

            if (!shape.visible() || (shape instanceof odr.Label && shape.label() == "")) {
                continue;
            }

            var topLeft = shape.topLeft();
            var bottomRight = shape.bottomRight();

            minX = Math.min(topLeft.x, minX);
            maxX = Math.max(bottomRight.x, maxX);

            minY = Math.min(topLeft.y, minY);
            maxY = Math.max(bottomRight.y, maxY);
        }

        minX -= odr.settings["export"].padding.left;
        maxX += odr.settings["export"].padding.right;
        minY -= odr.settings["export"].padding.top;
        maxY += odr.settings["export"].padding.bottom;

        var viewBox = ['viewBox="',
        minX,
        " ",
        minY,
        " ",
        maxX - minX,
        " ",
        maxY - minY,
        '"'].join("");

        for(var e in odr.vars.allDecisionNodes) {
            nodes.push(odr.vars.allDecisionNodes[e].svgRepresentation());
        }

        for(var e in odr.vars.allIterationNodes) {
            nodes.push(odr.vars.allIterationNodes[e].svgRepresentation());
        }

        for(var e in odr.vars.allAssociations) {
            nodes.push(odr.vars.allAssociations[e].svgRepresentation());
        }

        var lines = $("#" + odr.settings.svg.container).html();

        var completeSvg = lines.substring(0, lines.search("</svg>")) + nodes.join("") + lines.substring(lines.search("</svg>"));

        // the viewBox attribute needs to changed in order to cut off the sides of the svg
        completeSvg = completeSvg.replace(/viewBox=["']{1}[0-9]+ [0-9]+ [0-9]+ [0-9]+["']{1}/, viewBox);
        completeSvg = completeSvg.replace(/width=["']{1}[0-9]+["']{1}/, 'width="' + (maxX - minX) + '"');
        completeSvg = completeSvg.replace(/height=["']{1}[0-9]+["']{1}/, 'height="' + (maxY - minY) + '"');

        var form = $("div.export form");

        var dataInput = form.children("input#data");
        dataInput.val(completeSvg);

        var formatInput = form.children("input#format");
        formatInput.val(format);

        form.submit();
    });
});