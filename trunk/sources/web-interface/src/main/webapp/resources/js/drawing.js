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
    rectangle.height(20);
    rectangle.paint();

//    odr.assertContainerSize();
})