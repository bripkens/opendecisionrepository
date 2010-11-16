/*
 * Author: Ben Ripkens <bripkens.dev@gmail.com>
 */


odr.ready(function() {

    var rectangle = odr.addNode("Java Programming Language", 50, 200, true);
    
    var rectangle2 = odr.addNode("Java Enterprise Edition", 50, 350);

    var rectangle3 = odr.addNode("Tcl", 500, 300, true);

    var rectangle4 = odr.addNode("PHP", 500, 380);

    var handle = new odr.Handle();
    handle.x(80);
    handle.y(300);

    var handle2 = new odr.Handle();
    handle2.x(160);
    handle2.y(300);

    var association = new odr.Association();
    association.source(rectangle2);
    association.target(rectangle);
    association.addHandle(handle);
    association.addHandle(handle2);
    association.label("depends on");
    association.paint();

    var association2 = new odr.Association();
    association2.source(rectangle);
    association2.target(rectangle3);
    association2.label("replaces");
    association2.paint();

    var association3 = new odr.Association();
    association3.source(rectangle3);
    association3.target(rectangle4);
    association3.label("is alternative for");
    association3.paint();
    
})