/*
 * Author: Ben Ripkens <bripkens.dev@gmail.com>
 */


odr.ready(function() {
//
//    rectangle.draw(function() {
//        alert("bla");
//    }, "fooTest");
//
//
//
//
//    rectangle.paint();
//    rectangle.unbind(odr.Callback.types.draw, "fooTest");
//    rectangle.paint();

    var rectangle = new odr.Rectangle();
    rectangle.x(50);
    rectangle.y(200);
    rectangle.width(250);
    rectangle.height(40);
    rectangle.label("Java Enterprise Edition Version 5.1 in der revision 20000");
    rectangle.paint();
    
    
    var rectangle2 = new odr.Rectangle();
    rectangle2.x(50);
    rectangle2.y(400);
    rectangle2.width(250);
    rectangle2.height(20);
    rectangle2.label("Java Programming Language");
    rectangle2.paint();

    var rectangle3 = new odr.Rectangle();
    rectangle3.x(500);
    rectangle3.y(400);
    rectangle3.width(250);
    rectangle3.height(20);
    rectangle3.label("JavaServer Faces");
    rectangle3.paint();

    var handle = new odr.Handle();
    handle.x(80);
    handle.y(300);
    handle.paint();

    var handle2 = new odr.Handle();
    handle2.x(160);
    handle2.y(300);
    handle2.paint();

    var association = new odr.Association();
    association.source(rectangle);
    association.target(rectangle2);
    association.addHandle(handle);
    association.addHandle(handle2);
    association.label("depends on");
    association.paint();

//    var association2 = new odr.Association();
//    association2.source(rectangle3);
//    association2.target(rectangle2);
//    association2.label("caused by");
//    association2.paint();
//
//    var association3 = new odr.Association();
//    association3.source(rectangle3);
//    association3.target(rectangle);
//    association3.label("is alternative for");
//    association3.paint();

    j("input[name=radio1]").change(function() {
        if (j("input[name=radio1]:checked").val() == "hide") {
            rectangle.visible(false);
        } else {
            rectangle.visible(true);
        }
    })


    j("input[name=radio2]").change(function() {
        if (j("input[name=radio2]:checked").val() == "hide") {
            rectangle2.visible(false);
        } else {
            rectangle2.visible(true);
        }
    })

    j("input[name=radio3]").change(function() {
        if (j("input[name=radio3]:checked").val() == "hide") {
            rectangle3.visible(false);
        } else {
            rectangle3.visible(true);
        }
    })

//    setTimeout(function() {
//        handle.visible(false);
//    }, 2000);
//
//    setTimeout(function() {
//        handle.visible(true);
//    }, 4000);
//
//    setTimeout(function() {
//        rectangle.visible(false);
//    }, 2000);
//
//    setTimeout(function() {
//        rectangle.visible(true);
//    }, 4000);
//
//    setTimeout(function() {
//        rectangle2.visible(false);
//    }, 6000);
//
//    setTimeout(function() {
//        rectangle.visible(false);
//    }, 8000);
//
//    setTimeout(function() {
//        rectangle2.visible(true);
//    }, 10000);
//
//    setTimeout(function() {
//        rectangle.visible(true);
//    }, 12000);

//    odr.assertContainerSize();
})