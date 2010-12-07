/**
 * @fileOverview
 *
 * This file contains the JavaScript that is executed when the page is loaded. In general that is,
 * loading the visualization from the server.
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */

odr.ready(function() {
    odr.setLoadingText();
    odr.loadPopupIcon();
    odr.showWaitAnimation();

    var requestError = j("#externalVarRequestError").text();
    if (requestError == "true") {
        return;
    }

    var projectId = j("#externalVarProjectId").text();
    var requestUrl = j("#externalVarRequestUrl").text();
    var projectIdParameter = j("#externalVarProjectIdParameterName").text();
    var viewpointParameter = j("#externalVarViewpointParameterName").text();

    var parameter = {};
    parameter[projectIdParameter] = projectId;
    parameter[viewpointParameter] = true;

    var handlerFunction = null;

    if (viewpointParameter == j("#externalVarRelationshipViewpointParameterName").text()) {
        handlerFunction = odr._handleRelationshipViewGet;
    } else if (viewpointParameter == j("#externalVarChronologicalViewpointParameterName").text()) {
        handlerFunction = odr._handleChronologicalViewGet;
    } else if (viewpointParameter == j("#externalVarStakeholderInvolvementViewpointParameterName").text()) {
        handlerFunction = odr._handleStakeholderViewGet;
    } else {
        throw ("Viewpoint " + viewpointParameter + "not supported!");
    }

    j.ajax({
        url : requestUrl,
        data : parameter,
        dataType : "json",
        error : function(data, textStatus, errorThrown) {
            odr.setErrorText();
            odr.errorPopupIcon();
        },
        success : handlerFunction
    });

    
});


odr._handleRelationshipViewGet = function(data) {

    odr._requestedData = data;

    var allNodes = data.Nodes;

    for(var i = 0; i < allNodes.length; i++) {
        var currentNode = allNodes[i];

        var x = currentNode.X;
        var y = currentNode.Y;
        var name = currentNode.Version.Decision.Name;
        var state = currentNode.Version.State.StatusName;
        var visible = currentNode.Visible;

        if (x == 0) {
            x = 50;
        }

        if (y == 0) {
            y = 50;
        }

        var rectangle = odr.addNode(name, state, state, x, y, visible, true, odr.css.decisionClass);

        rectangle.value(currentNode);

        odr._allRectangles[currentNode.Version.Id] = rectangle;
    }

    var allRelationships = data.Associations;

    for(var i = 0; i < allRelationships.length; i++) {
        var currentRelationship = allRelationships[i];

        var source = odr._allRectangles[currentRelationship.Relationship.Source.Id];
        var target = odr._allRectangles[currentRelationship.Relationship.Target.Id];
        var type = currentRelationship.Relationship.Type.Name;

        var association = new odr.Association();
        if (currentRelationship.LabelX != 0 && currentRelationship.LabelY != 0) {

            association._labelPosition = {
                x : currentRelationship.LabelX,
                y : currentRelationship.LabelY
            }

        }
        association.source(source);
        association.target(target);
        association.label(type);

        for(var k = 0; k < currentRelationship.Handles.length; k++) {
            var handle = new odr.Handle();
            handle.x(currentRelationship.Handles[k].X);
            handle.y(currentRelationship.Handles[k].Y);
            association.addHandle(handle);
        }

        association.paint();



        association.value(currentRelationship);

        odr._allAssociations[currentRelationship.Id] = association;
    }

    odr.hideWaitAnimation();
}


odr._handleChronologicalViewGet = function(data) {
    odr._requestedData = data;

    var allNodes = data.Nodes;

    var stereotypeForIterations = j("#iterationStereotype").text();

    for(var i = 0; i < allNodes.length; i++) {
        var currentNode = allNodes[i];

        var x = currentNode.X;
        var y = currentNode.Y;

        var name = null;
        var stereotype = null;
        var stereotypeToShow = null;
        var id = null;
        var round;
        var extraClasses = null;

        if (currentNode.Iteration != null) {
            id = currentNode.Iteration.Id;
            name = currentNode.Iteration.Name;
            stereotypeToShow = currentNode.Iteration.EndDate;
            stereotype = stereotypeForIterations;
            round = false;
            extraClasses = odr.css.iterationClass;
        } else {
            id = currentNode.Version.Id;
            name = currentNode.Version.Decision.Name;
            stereotypeToShow = currentNode.Version.State.StatusName;
            stereotype = currentNode.Version.State.StatusName;
            round = true;

            if (currentNode.Disconnected) {
                extraClasses = odr.css.disconnectedClass;
            } else {
                extraClasses = odr.css.decisionClass;
            }
        }

        var visible = currentNode.Visible;

        if (x == 0) {
            x = 50;
        }

        if (y == 0) {
            y = 50;
        }

        var rectangle = odr.addNode(name, stereotype, stereotypeToShow, x, y, visible, round, extraClasses);

        rectangle.value(currentNode);

        odr._allRectangles[id] = rectangle;
    }

    var allRelationships = data.Associations;

    for(var i = 0; i < allRelationships.length; i++) {
        var currentRelationship = allRelationships[i];

        var sourceId;
        var targetId;

        if (currentRelationship.SourceIteration != null) {
            sourceId = currentRelationship.SourceIteration.Id;
        } else {
            sourceId = currentRelationship.SourceVersion.Id;
        }

        if (currentRelationship.TargetIteration != null) {
            targetId = currentRelationship.TargetIteration.Id;
        } else {
            targetId = currentRelationship.TargetVersion.Id;
        }

        var source = odr._allRectangles[sourceId];
        var target = odr._allRectangles[targetId];

        var association = new odr.Association();
        if (currentRelationship.LabelX != 0 && currentRelationship.LabelY != 0) {

            association._labelPosition = {
                x : currentRelationship.LabelX,
                y : currentRelationship.LabelY
            }

        }
        
        association.source(source);
        association.target(target);

        for(var k = 0; k < currentRelationship.Handles.length; k++) {
            var handle = new odr.Handle();
            handle.x(currentRelationship.Handles[k].X);
            handle.y(currentRelationship.Handles[k].Y);
            association.addHandle(handle);
        }

        association.paint();

        association.value(currentRelationship);

        odr._allAssociations[currentRelationship.Id] = association;
    }

    odr.hideWaitAnimation();
}

odr._handleStakeholderViewGet = function(data) {
    console.log(data);

    odr.popupText("Stakeholder involvement view is currently not supported.");
    odr.errorPopupIcon();
}