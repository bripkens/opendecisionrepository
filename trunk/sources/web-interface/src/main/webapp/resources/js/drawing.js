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
    rectangle.width(150);
    rectangle.height(40);
    rectangle.paint();

    var rectangle2 = new odr.Rectangle();
    rectangle2.x(50);
    rectangle2.y(400);
    rectangle2.width(150);
    rectangle2.height(20);
    rectangle2.paint();

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
    association.paint();

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