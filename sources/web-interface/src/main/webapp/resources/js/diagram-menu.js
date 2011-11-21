/**
 * @fileOverview
 *
 * This file contains menu related functionality.
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */



/*
 * ###############################################################################################################
 *                                              Top menu show and hide
 */
odr.bootstrap(function() {
    $(".topMenu > li").click(function() {
        odr.vars.topMenuOpen = !odr.vars.topMenuOpen;

        if (odr.vars.topMenuOpen) {
            $(".topMenuClickBlocker").show();
        } else {
            $(".topMenuClickBlocker").hide();
        }

        $(this).children("ul").toggle();
        
        return false;
    });

    $(".topMenu").children("li").mouseenter(function() {
        

        if (odr.vars.topMenuOpen) {
            var outer = this;

            $(".topMenu").children("li").each(function() {
                if (this != outer) {
                    $(this).children("ul").hide();
                }
            });

            $(this).children("ul").show();
        }
        return false;
    });

    $(".topMenuClickBlocker").click(function() {
        $(".topMenu").children("li").each(function() {
            $(this).children("ul").hide();
        });

        $(this).hide();

        odr.vars.topMenuOpen = false;
    });

    var setTopMenuClickBlockerToMaxSize = function() {
        var documentWidth = $(document).width();
        var documentHeight = $(document).height();


        var bodyWidth = $("body").width();
        var bodyHeight = $("body").height();

        $(".topMenuClickBlocker").width(Math.max(documentWidth, bodyWidth));
        $(".topMenuClickBlocker").height(Math.max(documentHeight, bodyHeight));
    }

    $(".topMenu > li li").click(function() {
        return false;
    });

    $(window).resize(setTopMenuClickBlockerToMaxSize);
    setTopMenuClickBlockerToMaxSize();
});












/*
 * ###############################################################################################################
 *                                              Overflow lists
 */
odr.bootstrap(function() {
    $(".overflowList").each(function() {
        var list = $(this);

        $(".nodes > .overflowList > .scrollup").click();

        list.children(".scrollup").first().click(function() {
            var maxEntries = parseInt(list.attr("maxEntries"));
            var currentEntry = parseInt(list.attr("currentEntry"));
            var clickDelta = parseInt(list.attr("clickDelta"));
            var elements = list.children("li").not(".scrolldown, .scrollup");

            currentEntry -= clickDelta;

            if (currentEntry <= 0) {
                currentEntry =  0;
                $(this).hide();
            }

            if (elements.length > (currentEntry + maxEntries)) {
                list.children().last().show();
            }

            if (currentEntry == 0) {
                elements.hide().filter(":lt(" + maxEntries + ")").show();
            } else {
                elements.hide().filter(":gt(" + (currentEntry - 1) + "):lt(" + maxEntries + ")").show();
            }

            

            list.attr("currentEntry", currentEntry);

            return false;
        });

        list.children(".scrolldown").hide().first().click(function() {
            var maxEntries = parseInt(list.attr("maxEntries"));
            var currentEntry = parseInt(list.attr("currentEntry"));
            var clickDelta = parseInt(list.attr("clickDelta"));
            var elements = list.children("li").not(".scrolldown, .scrollup");

            var maxForCurrentEntry = elements.length - maxEntries - 1;

            currentEntry += clickDelta;

            if (currentEntry >= maxForCurrentEntry) {
                currentEntry = maxForCurrentEntry;
                $(this).hide();
            }

            list.children().first().show();

            elements.hide().filter(":gt(" + currentEntry + "):lt(" + maxEntries + ")").show();

            list.attr("currentEntry", currentEntry);

            return false;
        });
    });
});














/*
 * Enable the performance mode menu
 */
odr.bootstrap(function() {
    var out = $("#performanceMenu");
    var quality = out.find("div > ul > li.quality");
    var speed = out.find("div > ul > li.speed");

    quality.click(function() {
        out.removeClass("speed");
        out.addClass("quality");

        odr.user.lowPerformanceMode = false;
    });

    speed.click(function() {
        out.removeClass("quality");
        out.addClass("speed");

        odr.user.lowPerformanceMode = true;
    });
});






/*
 * Enable the auto alignment menu
 */
odr.bootstrap(function() {
    var out = $("#alignmentMenu");
    var on = out.find("div > ul > li.on");
    var off = out.find("div > ul > li.off");
    var ask = out.find("div > ul > li.questionmark");


    on.click(function() {
        out.addClass("on");
        out.removeClass("off");
        out.removeClass("questionmark");

        odr.user.automaticallyAlignDragHandles = true;
    });

    off.click(function() {
        out.removeClass("on");
        out.addClass("off");
        out.removeClass("questionmark");

        odr.user.automaticallyAlignDragHandles = false;
    });

    ask.click(function() {
        out.removeClass("on");
        out.removeClass("off");
        out.addClass("questionmark");

        odr.user.automaticallyAlignDragHandles = undefined;
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


    odr._newNodeMenuEntry(node, name, status, visible);


    if (odr.vars.statusGroups[statusToShow] == undefined) {
        odr.vars.statusGroups[statusToShow] = [];
        odr._newStatusGroup(statusToShow, odr.vars.statusGroups[statusToShow]);
    }

    var statusGroup = odr.vars.statusGroups[statusToShow];

    statusGroup.push(node);

    node.bind(odr.Drawable.listener.visibilityChanged, function() {
        var allVisible = true, allInvisible = true;

        for(var i = 1; i < statusGroup.length; i++) {
            var visible = statusGroup[i].visible();

            if (visible) {
                allInvisible = false;
            } else {
                allVisible = false;
            }
        }

        if (allVisible) {
            statusGroup[0].removeClass("invisible");
            statusGroup[0].removeClass("partyVisible");
            statusGroup[0].addClass("visible");
        } else if (allInvisible) {
            statusGroup[0].addClass("invisible");
            statusGroup[0].removeClass("partyVisible");
            statusGroup[0].removeClass("visible");
        } else {
            statusGroup[0].removeClass("invisible");
            statusGroup[0].addClass("partyVisible");
            statusGroup[0].removeClass("visible");
        }

    }, "statusMenuListener");
    

    node.visible(visible);

    return node;
}






odr._newNodeMenuEntry = function(node, label, status, initialVisibility) {
    var showText = odr.translation.text["menu.nodes.show"];
    var hideText = odr.translation.text["menu.nodes.hide"];


    var activeClass = "visible";

    if (!initialVisibility) {
        activeClass = "invisible";
    }

    var li = $(document.createElement("li")).
    addClass(activeClass).
    attr("id", "menu" + node.id()).
    append($(document.createElement("span")).text(label)).
    append($(document.createElement("span")).text(status).addClass("statusText"));

    li.click(function() {
        return false;
    });

    var childs = $(document.createElement("div")).addClass("childMarker");
    li.append(childs);
    var ul = $(document.createElement("ul"));
    childs.append(ul);

    var visibleButton = $(document.createElement("li")).
    addClass("visible").
    append($(document.createElement("span")).text(showText));
    ul.append(visibleButton);

    var invisibleButton = $(document.createElement("li")).
    addClass("invisible").
    append($(document.createElement("span")).text(hideText));
    ul.append(invisibleButton);

    var detailsButton = $(document.createElement("li")).
    addClass("details").
    append($(document.createElement("span")).text(odr.translation.text["menu.nodes.details"]));
    ul.append(detailsButton);

    var relatedButton = $(document.createElement("li")).
    addClass("related").
    append($(document.createElement("span")).text(odr.translation.text["menu.nodes.related"]));
    ul.append(relatedButton);


    var nodeGroup = $("li.nodes > ul > li");

    nodeGroup.last().before(li);

    $(".nodes > .overflowList > .scrollup").click();
    $(".topMenuClickBlocker").click();

    visibleButton.click(function() {
        node.visible(true);
        return false;
    });

    invisibleButton.click(function() {
        node.visible(false);
        return false;
    });

    detailsButton.click(function() {
        node.additionalInformationFunction()(node);
        return false;
    });

    relatedButton.click(function() {
        odr.showRelatedNodesDialog(node);
        $(".topMenuClickBlocker").click();
        return false;
    });

    node.bind(odr.Drawable.listener.visibilityChanged, function() {
        if (node.visible()) {
            li.removeClass("invisible").addClass("visible");
        } else {
            li.removeClass("visible").addClass("invisible");
        }
    }, "nodeMenuListener");
}










/**
 * @private
 * @description
 * Create a new status group entry in the menu
 *
 * @param {String} name The name of the group
 * @param {odr.Drawable[]} items Some items which visibility should be controled
 * @return {jQueryLiElement} The created element
 */
odr._newStatusGroup = function(name, items) {
    var showText = odr.translation.text["menu.status.show"];
    var hideText = odr.translation.text["menu.status.hide"];

    var li = $(document.createElement("li")).
    addClass("visible").
    append($(document.createElement("span")).text(name));

    li.click(function() {
        return false;
    });

    var childs = $(document.createElement("div")).addClass("childMarker");
    li.append(childs);
    var ul = $(document.createElement("ul"));
    childs.append(ul);

    var visibleButton = $(document.createElement("li")).
    addClass("visible").
    append($(document.createElement("span")).text(showText));
    ul.append(visibleButton);

    var invisibleButton = $(document.createElement("li")).
    addClass("invisible").
    append($(document.createElement("span")).text(hideText));
    ul.append(invisibleButton);

    var nodeGroup = $("li.status > ul > li");

    nodeGroup.last().before(li);

    $(".status > .overflowList > .scrollup").click();
    $(".topMenuClickBlocker").click();

    items.unshift(li);

    visibleButton.click(function() {
        for(var i = 1; i < items.length; i++) {
            items[i].visible(true);
        }

        return false;
    });

    invisibleButton.click(function() {
        for(var i = 1; i < items.length; i++) {
            items[i].visible(false);
        }

        return false;
    });
}














/*
 * ###############################################################################################################
 *                                              Projects menu
 */
odr.ready(function() {
    $("#save").click(function() {
        odr._saveAll();
    });

    $("#back").click(function() {
        window.location = odr.settings.request.project.replace("{0}",
            odr.vars.requestParameter[odr.settings.request.parameter.projectId]);
    });

    $("#toRelationshipView").click(function() {
        window.location = odr.settings.request.visualization.
        replace("{0}", odr.vars.requestParameter[odr.settings.request.parameter.projectId]).
        replace("{1}", odr.settings.request.parameter.relationshipView)
    });

    $("#toChronologicalView").click(function() {
        window.location = odr.settings.request.visualization.
        replace("{0}", odr.vars.requestParameter[odr.settings.request.parameter.projectId]).
        replace("{1}", odr.settings.request.parameter.chronologicalView)
    });

    $("#toStakeholderView").click(function() {
        window.location = odr.settings.request.visualization.
        replace("{0}", odr.vars.requestParameter[odr.settings.request.parameter.projectId]).
        replace("{1}", odr.settings.request.parameter.stakeholderView)
    });
            
    $("#refresh").click(function() {
        window.location = window.location;
    });
});







/**
 * @private
 * @description
 * Use this method to save all the new positions, drag handles etc.
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
            odr.showStatus(odr.translation.text["loader.text.save.success"]);
        },
        error : function(data, textStatus, errorThrown) {
            odr.popup.showError("Could not save visualization: " + textStatus, true);
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
    $("#exportMenu li").click(function() {
        var format = $(this).attr("class");

        odr.showStatus(odr.translation.text["menu.export.status"].replace("{0}", format));
        $(".topMenuClickBlocker").click();

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

        var somethingVisible = false;

        for(var e in odr.vars.allDecisionNodes) {
            if (odr.vars.allDecisionNodes[e].visible()) {
                somethingVisible = true;
            }

            nodes.push(odr.vars.allDecisionNodes[e].svgRepresentation());
        }

        for(var e in odr.vars.allIterationNodes) {
            if (odr.vars.allIterationNodes[e].visible()) {
                somethingVisible = true;
            }

            nodes.push(odr.vars.allIterationNodes[e].svgRepresentation());
        }

        if (!somethingVisible) {
            odr.showStatus(odr.translation.text["menu.export.nothing"]);
            return;
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

        var d = new Date();
        var dateString = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
        
        var filename = odr.translation.text["menu.export.filename"].
        replace("{0}", odr.vars.requestedViewpoint).
        replace("{1}", dateString);

        var form = $("#exportMenu form");

        var filenameInput = form.children("input#filename");
        filenameInput.val(filename);

        var dataInput = form.children("input#data");
        dataInput.val(completeSvg);

        var formatInput = form.children("input#format");
        formatInput.val(format);

        form.submit();

        return true;
    });
});







/*
 * ###############################################################################################################
 *                                              Status output
 */
/**
 * @description
 * Show a small status message in the navigation bar. The message will automatically disappear after the specified
 * duration
 *
 * @param {String} text The text that you want to show
 * @param {Number} [duration] The duration, i.e. the time before hiding the text in milliseconds.
 * Default is four seconds.
 */
odr.showStatus = function(text, duration) {
    var out = $(".outputStatus > span");
    out.text(text);
    

    if (duration == undefined) {
        duration = 4000;
    }

    out.stop(true, true).show().delay(duration / 2).fadeOut(duration / 2);
}