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

    var parameter = {};
    parameter[odr.settings.request.parameter.projectId] = projectId;
    parameter[viewParameter] = true;

    $.ajax({
        url : odr.settings.request.dataProvider,
        data : parameter,
        dataType : "json",
        error : function(data, textStatus, errorThrown) {
            odr.popup.showError("Error while retrieving data from the server: " + textStatus + " /// " + errorThrown);
        },
        success : viewHandler
    });
});



odr.handleRelationshipView = function(data) {
    console.log(data);

    var root = odr.canvas();
    var suspendID = root.suspendRedraw(5000);

    var allNodes = {};

    for(var i = 0; i < data.Nodes.length; i++) {
        var nodeJson = data.Nodes[i];

        var node = new odr.Node();
        node.json = nodeJson;
        node.position(nodeJson.X, nodeJson.Y);
        node.size(nodeJson.Width, nodeJson.Height);
        node.addClass("round");
        node.label(nodeJson.Version.Decision.Name);
        node.status(nodeJson.Version.State.StatusName);
        node.visible(nodeJson.Visible);

        allNodes[nodeJson.Version.Id] = node;
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
        association.source(allNodes[associationJson.Relationship.Source.Id]);
        association.target(allNodes[associationJson.Relationship.Target.Id]);
    }

    root.unsuspendRedraw(suspendID);
};

odr.handleChronologicalView = function(data) {
    console.log(data);
    odr.popup.showError("Unsupported viewpoint");
};

odr.handleStakeholderView = function(data) {
    console.log(data);
    odr.popup.showError("Unsupported viewpoint");
};

//odr.ready(function() {
//    var node = new odr.Node();
//    node.label("Java Programming language");
//    node.status("approved");
//    node.addClass("round");
//    node.position(100, 100);
//
//    var node2 = new odr.Node();
//    node2.label("Milestone 1: Release");
//    node2.status("some date");
//    node2.addClass("round");
//    node2.position(400,100);
//
//
//    var association = new odr.Association();
//    association.source(node);
//    association.target(node2);
//    association.label("caused by");
//    association.labelPosition(50,40);
//});