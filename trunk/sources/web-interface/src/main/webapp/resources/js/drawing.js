var idCounter = 0;
var previousElement;
var canvas;
var ctx;

$(document).ready(function() {
    canvas = document.getElementById("canvas");
    
    if (canvas.getContext) {
        ctx = canvas.getContext("2d");

        ctx.strokeStyle = "#000";
        ctx.lineWidth = 1;

        $("canvas").click(function(e) {
            addItemAtPosition(e.pageX, e.pageY);
        })
    }
});



function addItemAtPosition(x, y) {
    $("body").append("<div id='" + idCounter + "' class='node'></div>");

    newElement = $("div#" + idCounter);

    newElement.css({
        "left" : x - (newElement.width() / 2),
        "top" : y- (newElement.height() / 2)
    })

    newElement.draggable();

    newElement.rightClick(function() {
        if (previousElement == null) {
            previousElement = $(this);
            focusNode(previousElement);
            return;
        }


        canvas = document.getElementById("canvas");
        ctx = canvas.getContext("2d");
        
        fromX = previousElement.position().left + 10;
        fromY = previousElement.position().top + 10;
        toX = $(this).position().left;
        toY = $(this).position().top;

        ctx.moveTo(fromX, fromY);
        ctx.lineTo(fromX, toY);
        ctx.lineTo(toX, toY);

        ctx.stroke();

        $("body").append("<div id='handle" + idCounter + "' class='dragHandle'></div>");

        handle = $("#handle" + idCounter);
        handle.draggable();
        handle.css({
            "left" : fromX +3,
            "top" : toY+3
        });
        
        
        blurNode(previousElement);
        previousElement = null;

        idCounter++;
    });

    idCounter++;
}
function focusNode(node) {
    node.animate({
        "left" : node.position().left - 10 + "px",
        "top" : node.position().top - 10 + "px",
        "width" : "30px",
        "height" : "30px",
        "border-radius": "15px",
        "background-color": "#bebebe"
    });
}

function blurNode(node) {
    node.animate({
        "left" : node.position().left + 10 + "px",
        "top" : node.position().top + 10 + "px",
        "width" : "20px",
        "height" : "20px",
        "border-radius": "10px",
        "background-color": "#fff"
    });
}