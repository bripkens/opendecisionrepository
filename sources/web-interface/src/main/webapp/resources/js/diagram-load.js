/**
 * @fileOverview
 *
 * This file contains the JavaScript that is responsible for loading data form the server.
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */

odr.ready(function() {
    // analyse request parameter
    var projectId = odr.vars.requestParameter[odr.settings.request.parameter.projectId];

    if (isNaN(projectId)) {
        odr.popup.showError("Invalid project id: " + projectId);
        return;
    }

    var relationship = odr.vars.requestParameter[odr.settings.request.parameter.relationshipView];
    var chronological = odr.vars.requestParameter[odr.settings.request.parameter.chronologicalView];
    var stakeholder = odr.vars.requestParameter[odr.settings.request.parameter.stakeholderView];

    var viewHandler = null;
    var viewParameter = null;

    if (relationship != undefined) {
        viewHandler = odr.handleRelationshipView;
        viewParameter = odr.settings.request.parameter.relationshipView;
    } else if (chronological != undefined) {
        viewHandler = odr.handleChronologicalView;
        viewParameter = odr.settings.request.parameter.chronologicalView;
    } else if (stakeholder != undefined) {
        viewHandler = odr.handleStakeholderView;
        viewParameter = odr.settings.request.parameter.stakeholderView;
    } else {
        odr.popup.showError("Invalid viewpoint");
        return;
    }

    odr.vars.requestedViewpoint = viewParameter;

    var parameter = {};
    parameter[odr.settings.request.parameter.projectId] = projectId;
    parameter[viewParameter] = true;

    $.ajax({
        url : odr.settings.request.dataProvider,
        data : parameter,
        dataType : "json",
        timeout : 2000,
        async : false,
        error : function(data, textStatus, errorThrown) {
            odr.popup.showError("Error while retrieving data from the server: " + textStatus + " /// " + errorThrown);
        },
        success : viewHandler
    });
});








/**
 * @description
 * Handle the data which has been loaded from the server. The data must have the correct form for a relationship view
 *
 * @param {Object} data The data from the server
 */
odr.handleRelationshipView = function(data) {
    $("#toRelationshipView").hide();

    odr.vars.json = data;

    var root = odr.canvas();
    var suspendID = root.suspendRedraw(5000);

    for(var i = 0; i < data.Nodes.length; i++) {
        var nodeJson = data.Nodes[i];

        var node = odr.addNode(nodeJson.Version.Decision.Name,
            nodeJson.Version.State.StatusName,
            nodeJson.Version.State.StatusName,
            nodeJson.Visible);

        node.json = nodeJson;

        if (nodeJson.X == 0) {
            nodeJson.X = 50;
        }

        if (nodeJson.Y == 0) {
            nodeJson.Y = 50;
        }

        node.position(nodeJson.X, nodeJson.Y);
        node.additionalInformationFunction(odr.showAdditionalDecisionDetails);

        if (nodeJson.Width != 0 && nodeJson.Height != 0) {
            node.size(nodeJson.Width, nodeJson.Height);
        }
        
        node.addClass(odr.settings.node.additionalClasses.roundCorners);
        node.addClass(odr.settings.node.additionalClasses.decision);

        odr.vars.allDecisionNodes[nodeJson.Version.Id] = node;
    }


    for(var i = 0; i < data.Associations.length; i++) {
        var associationJson = data.Associations[i];

        var association = new odr.Association();
        association.json = associationJson;
        association.label(associationJson.Relationship.Type.Name);

        if (associationJson.LabelX == 0) {
            associationJson.LabelX = 50;
        }

        if (associationJson.LabelY == 0) {
            associationJson.LabelY = 50;
        }

        association.labelPosition(associationJson.LabelX, associationJson.LabelY);
        association.source(odr.vars.allDecisionNodes[associationJson.Relationship.Source.Id]);
        association.target(odr.vars.allDecisionNodes[associationJson.Relationship.Target.Id]);

        association.loadHandles(associationJson.Handles);

        odr.vars.allAssociations[associationJson.Id] = association;
    }

    root.unsuspendRedraw(suspendID);
};









/**
 * @description
 * Handle the data which has been loaded from the server. The data must have the correct form for a chronological view
 *
 * @param {Object} data The data from the server
 */
odr.handleChronologicalView = function(data) {
    $("#toChronologicalView").hide();

    odr.vars.json = data;

    var root = odr.canvas();
    var suspendID = root.suspendRedraw(5000);

    for(var i = 0; i < data.Nodes.length; i++) {
        var nodeJson = data.Nodes[i];

        var label, status, statusToShow, additionalInformationFunction;

        if (nodeJson.Version != null) {
            label = nodeJson.Version.Decision.Name;
            status = nodeJson.Version.State.StatusName;
            statusToShow = nodeJson.Version.State.StatusName;
            additionalInformationFunction = odr.showAdditionalDecisionDetails;
        } else {
            label = nodeJson.Iteration.Name;
            status = nodeJson.Iteration.EndDate;
            statusToShow = odr.translation.text["label.iteration"];
            additionalInformationFunction = odr.showAdditionalIterationDetails;
        }
        

        var node = odr.addNode(label,
            status,
            statusToShow,
            nodeJson.Visible);


        node.json = nodeJson;
        node.position(nodeJson.X, nodeJson.Y);
        node.additionalInformationFunction(additionalInformationFunction);

        if (nodeJson.Width != 0 && nodeJson.Height != 0) {
            node.size(nodeJson.Width, nodeJson.Height);
        }

        if (nodeJson.Disconnected == true) {
            node.addClass(odr.settings.node.additionalClasses.disconnected);
        }


        if (nodeJson.Version != null) {
            node.addClass(odr.settings.node.additionalClasses.roundCorners);
            node.addClass(odr.settings.node.additionalClasses.decision);
            odr.vars.allDecisionNodes[nodeJson.Version.Id] = node;
        } else {
            node.addClass(odr.settings.node.additionalClasses.iteration);
            odr.vars.allIterationNodes[nodeJson.Iteration.Id] = node;
        }
    }


    for(var i = 0; i < data.Associations.length; i++) {
        var associationJson = data.Associations[i];

        var association = new odr.Association();
        association.json = associationJson;

        if (associationJson.SourceIteration != null) {
            association.source(odr.vars.allIterationNodes[associationJson.SourceIteration.Id]);
        } else {
            association.source(odr.vars.allDecisionNodes[associationJson.SourceVersion.Id]);
        }

        if (associationJson.TargetIteration != null) {
            association.target(odr.vars.allIterationNodes[associationJson.TargetIteration.Id]);
        } else {
            association.target(odr.vars.allDecisionNodes[associationJson.TargetVersion.Id]);
        }

        
        association.loadHandles(associationJson.Handles);

        odr.vars.allAssociations[associationJson.Id] = association;
    }

    root.unsuspendRedraw(suspendID);
};








/**
 * @description
 * Handle the data which has been loaded from the server. The data must have the correct form for a stakeholver view
 *
 * @param {Object} data The data from the server
 */
odr.handleStakeholderView = function(data) {
    $("#toStakeholderView").hide();

    console.log(data);
    odr.popup.showError("Unsupported viewpoint");
};