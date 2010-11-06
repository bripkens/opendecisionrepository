var odr = odr || {};

var j = jQuery.noConflict();

/*
 * settings
 */
odr.menu = "menu";
odr.toggle = "toggle";
odr.open = "open";
odr.closed = "closed";
odr.menuOpenWidth = "300px";
odr.menuClosedWidth = "20px";
odr.opener = "opener";
odr.closer = "closer";

odr.grid = [20, 20];

odr.selectedClass = "selected";

odr.boxIdPrefix = "box";
odr.nodeClass = "node";


odr.relationshipIdPrefix = "relationship";
odr.relationshipClass = "relationship"


odr.dragHandleIdPrefix = "dragHandle";
odr.dragHandle = "dragHandle";
odr.handleSize = {
    x : 8,
    y : 8
};

odr.handleCreationOffsetMultiplier = {
    x : 1,
    y : 1
};



odr.strokeStyle = "#000";
odr.lineWidth = 1;


odr.handleCreationOffset = {
    x : (odr.handleCreationOffsetMultiplier.x * odr.grid[0]) + (odr.handleSize.x / 2),
    y : (odr.handleCreationOffsetMultiplier.y * odr.grid[1]) + (odr.handleSize.y / 2)
};

/*
 * variables
 */
odr.idCounter = 0;
odr.canvas;
odr.ctx;

j(document).ready(function() {

    odr.canvas = document.getElementById("canvas");

    if (odr.canvas.getContext) {
        ctx = odr.canvas.getContext("2d");

        ctx.strokeStyle = odr.strokeStyle;
        ctx.lineWidth = odr.lineWidth;

        j(window).resize(resize);
        resize();

        j("." + odr.toggle).click(toggleMenu);

        odr.view = new odr.View();

        box = new odr.Box("Java Programming language", 400, 200);
        box2 = new odr.Box("Java Enterprise Edition", 500, 600);
        box3 = new odr.Box("Java Enterprise Edition 6", 500, 400);
        box4 = new odr.Box("Java Enterprise Edition 5", 500, 500);
        
        relationship = new odr.Relationship(box, box2);
        relationship.addHandle(new odr.DragHandle(400, 400));

        relationship2 = new odr.Relationship(box3, box2);
        relationship2.addHandle(new odr.DragHandle(450, 450));

        relationship3 = new odr.Relationship(box4, box2);
        relationship3.addHandle(new odr.DragHandle(500, 500));
        
        relationship4 = new odr.Relationship(box3, box4);
        relationship4.addHandle(new odr.DragHandle(550, 550));
        
        odr.view.addElement(box);
        odr.view.addElement(box2);
        odr.view.addElement(box3);
        odr.view.addElement(box4);
        odr.view.addRelationship(relationship);
        odr.view.addRelationship(relationship2);
        odr.view.addRelationship(relationship3);
        odr.view.addRelationship(relationship4);

        odr.view.draw();
    }
});

function getWindowWidth() {
    return document.documentElement.clientWidth;
}

function getWindowHeight() {
    return document.documentElement.clientHeight;
}

function repaint() {
    odr.view.draw();
}

function clearCanvas() {
    odr.canvas.width = odr.canvas.width;
}

function toggleMenu() {

    toggleButton = j(this);
    toggleMenu = toggleButton.parent();

    if (toggleMenu.hasClass(odr.open)) {
        toggleMenu.animate({
            "width" : odr.menuClosedWidth
        });
        toggleMenu.addClass(odr.closed);
        toggleMenu.removeClass(odr.open);
        
        toggleButton.addClass(odr.opener);
        toggleButton.removeClass(odr.closer);
    } else {
        toggleMenu.animate({
            "width" : odr.menuOpenWidth
        });
        toggleMenu.addClass(odr.open);
        toggleMenu.removeClass(odr.closed);

        toggleButton.addClass(odr.closer);
        toggleButton.removeClass(odr.opener);
    }
}

function resize() {
    j("." + odr.menu).css({
        "height" : getWindowHeight()
    });
}








odr.View = function() {
    this.elements = new Array();
    this.relationships = new Array();

    this.addElement = function(element) {
        odr.view.elements[odr.view.elements.length] = element;
    }

    this.addRelationship = function(relationship) {
        //        alert("addRelationship, array size " + odr.view.relationships.length);
        odr.view.relationships[odr.view.relationships.length] = relationship;
    }

    this.getElement = function(id) {
        for(i = 0; i < this.elements.length; i++) {
            if (this.elements[i].id == id) {
                return this.elements[i];
            }
        }

        return undefined;
    }

    this.getRelationship = function(id) {
        for(i = 0; i < this.relationships.length; i++) {
            if (this.relationships[i].id == id) {
                return this.relationships[i];
            }
        }

        return undefined;
    }

    this.getHandle = function(id) {
        for(i = 0; i < this.relationships.length; i++) {
            for(k = 0; k < this.relationships[i].dragHandles.length; k++) {
                if (this.relationships[i].dragHandles[k].id == id) {
                    return this.relationships[i].dragHandles[k];
                }
            }
        }

        return undefined;
    }

    this.draw = function() {
        clearCanvas();

        for(i = 0; i < odr.view.elements.length; i++) {
            odr.view.elements[i].draw();
        }


        for(i = 0; i < odr.view.relationships.length; i++) {
            odr.view.relationships[i].draw();
        }
    }
}











odr.Box = function(text, x, y) {
    this.id = odr.boxIdPrefix + odr.idCounter++;
    this.text = text;

    if (x == undefined) {
        this.x = 0;
    } else {
        this.x = x;
    }

    if (y == undefined) {
        this.y = 0;
    } else {
        this.y = y;
    }






    this.draw = function() {
        newElement = j("#" + this.id);


        if (newElement.length == 0) {
            j("body").append("<div id='" + this.id + "' />");
            newElement = j("#" + this.id);
        }

        newElement.children().remove();

        newElement.text(this.text);
        newElement.addClass(odr.nodeClass);

        outer = this;

        newElement.draggable({
            drag : function(e, ui) {
                id = j(this).attr("id");

                element = odr.view.getElement(id);

                newPosition = j(this).position();

                element.x = newPosition.left;
                element.y = newPosition.top;

                repaint();
            },
            grid : odr.grid
        });

        newElement.css({
            "left" : this.x,
            "top" : this.y,
            "position" : "absolute"
        });

        
    }







    this.center = function() {
        element = j("#" + this.id);

        if (element.length == 0) {
            return undefined;
        }

        position = element.position();

        widthToAdd = element.width() / 2;
        heightToAdd = element.height() / 2;

        return {
            x : position.left + widthToAdd,
            y : position.top + heightToAdd
        };
    }
}














odr.Relationship = function(source, target) {
    this.id = odr.relationshipIdPrefix + odr.idCounter++;

    this.source = source;
    this.target = target;

    this.dragHandles = new Array();

    this.addHandle = function(handle, after) {
        if (after == undefined) {
            this.dragHandles[this.dragHandles.length] = handle;
            return;
        }

        for(i = 0; i < this.dragHandles.length; i++) {
            if (this.dragHandles[i].id == after) {
                this.dragHandles.splice(i+1, 0, handle);
                return;
            }
        }
    }

    this.draw = function() {
        newElement = j("#" + this.id);


        if (newElement.length == 0) {
            j("body").append("<div id='" + this.id + "' />");
            newElement = j("#" + this.id);
        }

        newElement.addClass(odr.relationshipClass)

        sourcePosition = source.center();
        targetPosition = target.center();

        ctx.moveTo(sourcePosition.x, sourcePosition.y);

        for(handleCounter = 0; handleCounter < this.dragHandles.length; handleCounter++) {
            currentHandle = this.dragHandles[handleCounter];

            newHandle = j("#" + currentHandle.id);

            if (newHandle.length == 0) {
                newElement.append("<div id='" + currentHandle.id + "' />");
                newHandle = j("#" + currentHandle.id);
            }

            newHandle.addClass(odr.dragHandle);

            handlePositionLeft = currentHandle.x - (odr.handleSize.x / 2);
            handlePositionTop = currentHandle.y - (odr.handleSize.y / 2);

            newHandle.draggable({
                drag : function() {
                    id = j(this).attr("id");

                    element = odr.view.getHandle(id);

                    newPosition = j(this).position();

                    element.x = newPosition.left + (odr.handleSize.x / 2);
                    element.y = newPosition.top + (odr.handleSize.y / 2);

                    repaint();
                },
                grid: odr.grid
            });

            newHandle.click(function(e) {
                if(!e.ctrlKey) {
                    return;
                }

                e.ctrlKey = false;

                clickedElement = j(this);

                parentElement = odr.view.getRelationship(clickedElement.parent().attr("id"));

                position = clickedElement.position();

                parentElement.addHandle(new odr.DragHandle(position.left + odr.handleCreationOffset.x
                    , position.top + odr.handleCreationOffset.y), clickedElement.attr("id"));

                repaint();
            });

            newHandle.css({
                "left" : handlePositionLeft,
                "top" : handlePositionTop,
                "position" : "absolute"
            });

            ctx.lineTo(currentHandle.x, currentHandle.y);
        }

        ctx.lineTo(targetPosition.x, targetPosition.y);
        ctx.stroke();
    }
}











odr.DragHandle = function(x, y) {
    this.id = odr.dragHandleIdPrefix + odr.idCounter++;

    if (x == undefined) {
        this.x = 0;
    } else {
        this.x = x;
    }

    if (y == undefined) {
        this.y = 0;
    } else {
        this.y = y;
    }
}























    //function addItemAtPosition(x, y) {
    //    $("body").append("<div id='" + idCounter + "' class='node'></div>");
    //
    //    newElement = $("div#" + idCounter);
    //
    //    newElement.css({
    //        "left" : x - (newElement.width() / 2),
    //        "top" : y- (newElement.height() / 2)
    //    })
    //
    //    newElement.draggable();
    //
    //    newElement.rightClick(function() {
    //        if (previousElement == null) {
    //            previousElement = $(this);
    //            focusNode(previousElement);
    //            return;
    //        }
    //
    //
    //        canvas = document.getElementById("canvas");
    //        ctx = canvas.getContext("2d");
    //
    //        fromX = previousElement.position().left + 10;
    //        fromY = previousElement.position().top + 10;
    //        toX = $(this).position().left;
    //        toY = $(this).position().top;
    //
    //        ctx.moveTo(fromX, fromY);
    //        ctx.lineTo(fromX, toY);
    //        ctx.lineTo(toX, toY);
    //
    //        ctx.stroke();
    //
    //        $("body").append("<div id='handle" + idCounter + "' class='dragHandle'></div>");
    //
    //        handle = $("#handle" + idCounter);
    //        handle.draggable();
    //        handle.css({
    //            "left" : fromX +3,
    //            "top" : toY+3
    //        });
    //
    //
    //        blurNode(previousElement);
    //        previousElement = null;
    //
    //        idCounter++;
    //    });
    //
    //    idCounter++;
    //}
    //
    //function focusNode(node) {
    //    node.addClass(class)
    //}
    //
    //function blurNode(node) {
    //    node.animate({
    //        "left" : node.position().left + 10 + "px",
    //        "top" : node.position().top + 10 + "px",
    //        "width" : "20px",
    //        "height" : "20px",
    //        "border-radius": "10px",
    //        "background-color": "#fff"
    //    });
    //}