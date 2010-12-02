/*
 * Author: Ben Ripkens <bripkens.dev@gmail.com>
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

        var rectangle = odr.addNode(name, state, x, y, visible);

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
    console.log(data);

    odr._requestedData = data;

    var allNodes = data.Nodes;

    var stereotypeForIterations = j("#iterationStereotype").text();

    for(var i = 0; i < allNodes.length; i++) {
        var currentNode = allNodes[i];

        var x = currentNode.X;
        var y = currentNode.Y;

        var name = null;
        var stereotype = null;
        var id = null;

        if (currentNode.Iteration != null) {
            id = currentNode.Iteration.Id;
            name = currentNode.Iteration.Name;
            stereotype = stereotypeForIterations;
        } else {
            id = currentNode.Version.Id;
            name = currentNode.Version.Decision.Name;
            stereotype = currentNode.Version.State.StatusName;
        }

        var visible = currentNode.Visible;

        if (x == 0) {
            x = 50;
        }

        if (y == 0) {
            y = 50;
        }

        var rectangle = odr.addNode(name, stereotype, x, y, visible);

        rectangle.value(currentNode);

        odr._allRectangles[id] = rectangle;
    }

    odr.hideWaitAnimation();
}

odr._handleStakeholderViewGet = function(data) {
    console.log(data);

    odr.popupText("Stakeholder involvement view is currently not supported.");
    odr.errorPopupIcon();
}