odr.ready(function() {
    // SVG on the page should only be manipulated after the page is
    // finished loading
    var buttons = document.getElementsByTagName('button');
    for (var i = 0; i < buttons.length; i++) {
        buttons[i].disabled = false;
    }
});

function changeColors() {
    // get elements from our embedded SVG first

    // use getElementById
    var circle = document.getElementById('myCircle');

    // change using setAttribute
    circle.setAttribute('stroke', 'green');

    // can also use style property
    circle.style.fill = '#8A2BE2';

// change the value inside our SVG OBJECT now

// use the 'contentDocument' property to navigate into the SVG OBJECT
//    var doc = document.getElementById('mySVGObject').contentDocument;
//    circle = doc.getElementById('myCircle');
//    circle.style.fill = '#8A2BE2';
}

function changeText() {
    // use getElementsByTagNameNS to get our text from our embedded SVG

    // 'svgns' is a 'magic' variable that we make available; it is just
    // the SVG namespace 'http://www.w3.org/2000/svg' so you don't always
    // have to remember it.  We also make the variable 'xlinkns' available.
    var textElems = document.getElementsByTagNameNS(svgns, 'text');

    // change the text Hello World to Goodbye World
    for (var i = 0; i < textElems.length; i++) {
        if (textElems[i].childNodes[0].nodeValue == 'Hello World') {
            textElems[i].childNodes[0].nodeValue = 'Goodbye World';
        }
    }

// change the text inside our SVG OBJECT as well
//    var doc = document.getElementById('mySVGObject').contentDocument;
//    textElems = doc.getElementsByTagNameNS(svgns, 'text');
//    for (var i = 0; i < textElems.length; i++) {
//        if (textElems[i].childNodes[0].nodeValue == 'Hello World') {
//            textElems[i].childNodes[0].nodeValue = 'Goodbye World';
//        }
//    }
}

odr.ready(function() {
    //    $( ".node" ).resizable(odr.settings.resizing.jQueryUiSettings);
    //    $( ".node" ).draggable(odr.settings.dragging.jQueryUiSettings);
    //    $( ".associationHelper" ).draggable({containment : "parent", cursorAt : {top : 5, left : 5}});
    //
    //
    //    $(".hide").click(function() {
    //        var elements = document.getElementsByClassNS(svgns, "someCircleClass", "circle");
    //
    //        for(var i = 0; i < elements.length; i++) {
    //            elements[i].style.display = "none";
    //        }
    //    });


    //    var shape = new odr.Shape();
    //
    ////    shape.bind("positionChanged", function(target) {
    ////        console.log(this);
    ////        console.log(target);
    ////    }.createDelegate({foo : "bla"}));
    //
    //    var rectangle = {
    //        id : "SomeVeryFancyId",
    //        listener : function(thingThatChanged) {
    //            console.log("Observable is now visible? " + thingThatChanged.visible());
    //        }
    //    }
    //
    //    shape.bind(odr.Drawable.listener.visibilityChanged, rectangle.listener.createDelegate(rectangle));
    //
    //    console.log("Changing to visible but the listener should not react");
    //    shape.visible(true);
    //    console.log("Changing to invisible");
    //    shape.visible(false);
    //    console.log("Changing to visible");
    //    shape.visible(true);
    //
    //
    //    shape.bind(odr.Drawable.listener.classesChanged, function(drawable) {
    //        console.log(drawable.classString());
    //    });
    //
    //    shape.addClass("first");
    //    shape.addClass("second");
    //    shape.addClass("third");
    //    shape.removeClass("second");
    //    shape.addClass("first");

    var node = new odr.Node();
    node.label("Java Programming language");
    node.status("approved");
    node.addClass("round");
    node.position(100, 100);

    var node2 = new odr.Node();
    node2.label("Milestone 1: Release");
    node2.status("some date");
    node2.addClass("round");
    node2.position(400,100);



    var handle = new odr.Handle();
    handle.position(300, 300);




    var association = new odr.Association();
    association.source(node);
    association.target(node2);

    var line = new odr.Line();
    line.source(node).target(node2);


    line.bind(odr.Line.listener.mousein, function() {
        console.log("enter");
    }, 5).bind(odr.Line.listener.mouseout, function() {
        console.log("out");
    }, 4).bind(odr.Line.listener.click, function() {
        console.log("click");
    }, 3).bind(odr.Line.listener.mouseover, function() {
        console.log("over");
    }, 2);



    setTimeout(function() {
        line.visible(false);
    }, 2000);

    setTimeout(function() {
        line.visible(true);
    }, 4000);
});

