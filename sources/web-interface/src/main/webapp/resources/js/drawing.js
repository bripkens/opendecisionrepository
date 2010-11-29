/*
 * Author: Ben Ripkens <bripkens.dev@gmail.com>
 */

odr.ready(function() {

    var requestError = j("#externalVarRequestError").text();
    if (requestError == "true") {
        return;
    }

    var projectId = j("#externalVarProjectId").text();
    var requestUrl = j("#externalVarRequestUrl").text();

    j.getJSON(requestUrl, {
        "id" : projectId,
        "relationship" : true
    }, function(data) {
        odr._requestedData = data;
//        console.log(data);

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
            association.source(source);
            association.target(target);
            association.label(type);
            association.paint();

            association.value(currentRelationship);

            odr._allAssociations[currentRelationship.Id] = association;
        }
    });
    
})