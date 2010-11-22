/*
 * Author: Ben Ripkens <bripkens.dev@gmail.com>
 */

odr.__allRectangles = {};

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
        console.log(data);

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

            odr.__allRectangles[currentNode.Version.Id] = rectangle;
        }

        var allRelationships = data.Associations;

        for(var i = 0; i < allRelationships.length; i++) {
            var currentRelationship = allRelationships[i];

            var source = odr.__allRectangles[currentRelationship.Relationship.Source.Id];
            var target = odr.__allRectangles[currentRelationship.Relationship.Target.Id];
            var type = currentRelationship.Relationship.Type.Name;

            var association = new odr.Association();
            association.source(source);
            association.target(target);
            association.label(type);
            association.paint();
        }
    });

//    var rectangle = odr.addNode("Java Programming Language", "approved", 50, 200, true);
//
//    var rectangle2 = odr.addNode("Java Enterprise Edition", "approved", 50, 350, false);
//
//    var rectangle3 = odr.addNode("Tcl", "rejected", 500, 300, false);
//
//    var rectangle4 = odr.addNode("PHP", "considered", 500, 380);
//
//    var handle = new odr.Handle();
//    handle.x(80);
//    handle.y(300);
//
//    var handle2 = new odr.Handle();
//    handle2.x(160);
//    handle2.y(300);
//
//    var association = new odr.Association();
//    association.source(rectangle2);
//    association.target(rectangle);
//    association.addHandle(handle);
//    association.addHandle(handle2);
//    association.label("depends on");
//    association.paint();
//
//    var association2 = new odr.Association();
//    association2.source(rectangle);
//    association2.target(rectangle3);
//    association2.label("replaces");
//    association2.paint();
//
//    var association3 = new odr.Association();
//    association3.source(rectangle3);
//    association3.target(rectangle4);
//    association3.label("is alternative for");
//    association3.paint();
    
})