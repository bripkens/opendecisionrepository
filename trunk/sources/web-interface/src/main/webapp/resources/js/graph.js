var odr = odr || {};
var j = jQuery.noConflict();





// svg container
odr.targetId = "svgTarget";

// nodes and other visualizations
odr.nodeClass = "node";
odr.relationshipClass = "relationship";
odr.dragHandleClass = "dragHandle";
odr.lineClass = "line";

odr.nodeIdPrefix = "node";
odr.relationshipIdPrefix = "relationship";
odr.dragHandleIdPrefix = "dragHandle";
odr.lineIdPrefix = "line";

// menu
odr.menu = "menu";
odr.toggle = "toggle";
odr.open = "open";
odr.closed = "closed";
odr.menuOpenLeft = "0px";
odr.menuClosedLeft = "-283px";
odr.opener = "opener";
odr.closer = "closer";

// grid
odr.grid = {
    x : 20,
    y : 20
};
odr.gridClass = "grid";

// handles
odr.handleSize = 6;

// corners of the rectangle
odr.rx = 10;
odr.ry = 10;

// dragging mouse leave threshold
odr.draggingThreshold = 10;


/*
 * Variables
 */
odr.idCounter = 0;
odr.svg;
odr.dragPreviousEvent = undefined;


j(document).ready(function() {
    odr.register = new odr.Register();

    j("#" + odr.targetId).svg({
        onLoad: odr.drawInitial
    });

    j("#addNode").click(function(e) {
        //        source = new odr.Node();
        //        source.width = 200;
        //        source.height = 30;
        //        source.center(500, 100);
        //        odr.register.addItem(source);
        //
        //
        //        target = new odr.Node();
        //        target.width = 200;
        //        target.height = 30;
        //        target.center(500, 400);
        //        odr.register.addItem(target);

        handle = new odr.Handle();
        handle.x = 210;
        handle.y = 305;
        handle.draw();

    //        relationship = new odr.Relationship();
    //        relationship.setSource(source);
    //        relationship.setTarget(target);
    //        relationship.addHandle(handle);
    //        odr.register.addItem(relationship);
    //        relationship.draw();
    //
    //        source.draw();
    //        target.draw();
    });

    j("." + odr.toggle).click(odr.toggleMenu);

    j(window).resize(odr.resize);
    odr.resize();
});

odr.drawInitial = function(svg) {
    odr.svg = svg;

    odr.svg.style('@import "resources/css/graph.css";');
}

odr.toggleMenu = function() {

    toggleButton = j(this);
    toggleMenu = toggleButton.parent();

    if (toggleMenu.hasClass(odr.open)) {
        toggleMenu.animate({
            "left" : odr.menuClosedLeft
        });
        toggleMenu.addClass(odr.closed);
        toggleMenu.removeClass(odr.open);

        toggleButton.addClass(odr.opener);
        toggleButton.removeClass(odr.closer);
    } else {
        toggleMenu.animate({
            "left" : odr.menuOpenLeft
        });
        toggleMenu.addClass(odr.open);
        toggleMenu.removeClass(odr.closed);

        toggleButton.addClass(odr.closer);
        toggleButton.removeClass(odr.opener);
    }
}

odr.resize = function() {
    j("." + odr.menu).css({
        "height" : document.documentElement.clientHeight
    });
}

Function.prototype.createDelegate = function(scope) {
    var fn = this;
    return function() {
        // Forward to the original function using 'scope' as 'this'.
        return fn.apply(scope, arguments);
    }
}








/*
 * Snapping
 */

odr.round = function(value, roundTo) {
    modResult = value % roundTo;

    if(modResult == 0) {
        return value;
    } else if (modResult >= (roundTo / 2)) {
        return value + (roundTo - modResult);
    } else {
        return value - value % roundTo;
    }
}


odr.roundUp = function(value, roundTo) {
    modResult = value % roundTo;

    if(modResult == 0) {
        return value;
    }

    return value + (roundTo - (modResult));
}

odr.snapPosition = function(element) {
    if (element.x != undefined) {
        element.x = odr.round(element.x, odr.grid.x);
    }

    if (element.y != undefined) {
        element.y = odr.round(element.y, odr.grid.y);
    }
}

odr.snap = function(element) {
    odr.snapPosition(element);

    if (element.width != undefined) {
        element.width = odr.roundUp(element.width, odr.grid.x);
    }

    if (element.height != undefined) {
        element.height = odr.roundUp(element.height, odr.grid.y);
    }
}













/*
 * Draggable
 */

odr.enableDragging = function(element) {
    element.mousedown(odr.dragStart);
    element.mouseup(odr.dragStop);
}

odr.elementToDrag = undefined;
odr.itemToDrag = undefined;

odr.dragStart = function(e) {
    odr.elementToDrag = j(this);
    odr.dragPreviousEvent = e;
    j("body").mousemove(odr.dragging);
    j("#" + odr.targetId).addClass(odr.gridClass);

    odr.itemToDrag = odr.register.getItem(j(this).attr("id"));
    odr.itemToDrag.callback.dragStart();
}

odr.dragging = function(e) {
    if (odr.dragPreviousEvent == undefined) {
        odr.elementToDrag.unbind("mousemove", odr.nodeMouseMove);
        return;
    }

    
    odr.itemToDrag.x = odr.itemToDrag.x + (e.pageX - odr.dragPreviousEvent.pageX);
    odr.itemToDrag.y = odr.itemToDrag.y + (e.pageY - odr.dragPreviousEvent.pageY);
    odr.dragPreviousEvent = e;

    odr.itemToDrag.callback.dragging();
}


odr.dragStop = function(e) {
    j("#" + odr.targetId).removeClass(odr.gridClass);

    odr.elementToDrag.unbind("mousemove", odr.dragging);
    odr.dragPreviousEvent = undefined;
    
    odr.itemToDrag.callback.dragEnd();
}












/*
 * Classes
 */
odr.Register = function() {
    this.items = new Array();

    this.addItem = function(item) {
        this.items[item.id] = item;
    }

    this.getItem = function(itemId) {
        return this.items[itemId];
    }

    this.removeItem = function(itemId) {
        this.items[itemId] = undefined;
    }
}

odr.Node = function() {
    this.id = odr.nodeIdPrefix + odr.idCounter++;
    this.text = "";
    this.value = undefined;

    this.x = 0;
    this.y = 0;
    this.width = 0;
    this.height = 0;

    this.callback = new odr.Callback();

    this.snap = function() {
        odr.snap(this);
    }

    this.snapPosition = function() {
        odr.snapPosition(this);
    }

    this.draw = function() {
        this.snap();

        j("#" + this.id).remove();

        odr.svg.rect(this.x, this.y, this.width, this.height, odr.rx, odr.ry, {
            "class" : odr.nodeClass,
            "id" : this.id
        });

        element = j("#" + this.id);
        odr.enableDragging(element);
    }

    this.redraw = function() {
        element = j("#" + this.id);
        element.attr("x", this.x);
        element.attr("y", this.y);
    }

    this.center = function(x, y) {
        if (x == undefined && y == undefined) {
            return {
                "x" : this.x + (this.width / 2),
                "y" : this.y + (this.height / 2)
            };
        }

        this.x = x - this.width / 2;
        this.y = y - this.height / 2;
    }
}

odr.Relationship = function() {
    this.id = odr.relationshipIdPrefix + odr.idCounter++;
    this.source = undefined;
    this.target = undefined;
    this.text = "";
    this.handles = new Array();
    this.value = undefined;

    this.addHandle = function(handle) {
        this.handles[handle.id] = handle;
    }

    this.getHandle = function(handleId) {
        return this.handles[handleId];
    }

    this.setSource = function(source) {
        this.source = source;
    }

    this.setTarget = function(target) {
        this.target = target;
    }

    this.draw = function() {
        j("#" + this.id).remove();

        parent = odr.svg.group(this.id, {
            "class" : odr.relationshipClass
        });


        element = this.source.center();

        for (var i in this.handles) {
            odr.svg.line(parent, element.x, element.y, this.handles[i].x, this.handles[i].y);
            this.handles[i].draw();
            element = this.handles[i];
        }

        targetPosition = this.target.center();

        odr.svg.line(parent, element.x, element.y, targetPosition.x, targetPosition.y);
    }

    this.redraw = function() {
    }
}

odr.Line = function() {
    this.id = odr.lineIdPrefix + odr.idCounter++;
    odr.register.addItem(this);

    this.start = undefined;
    this.end = undefined;
    this.value = undefined;
    this.callback = new odr.Callback();

    this.setStart = function(element) {
        this.start = element;
        element.callback.dragging(this.redraw);
        element.callback.dragEnd(this.redraw);
    }

    this.setEnd = function(element) {
        this.end = element;
        element.callback.dragging(this.redraw);
        element.callback.dragEnd(this.redraw);
    }

    this.draw = function(parent) {
        j("#" + this.id).remove();
        odr.svg.line(parent,
            this.start.x,
            this.start.y,
            this.end.x,
            this.end.y,
            {
                "id" : this.id,
                "class" : odr.lineClass
            });

        this.callback.draw();
    }

    this.redraw = function() {
        element = j("#" + this.id);

        element.attr("x1", this.start.x);
        element.attr("y1", this.start.y);
        element.attr("x2", this.end.x);
        element.attr("y2", this.end.y);

        this.callback.redraw();
    }

    this.remove = function() {
        j("#" + this.id).remove();
        odr.register.removeItem(this.id);

        if (this.start != undefined) {
            this.start.callback.unbind("dragging", this.redraw);
            this.start.callback.unbind("dragEnd", this.redraw);
        }

        if (this.end != undefined) {
            this.end.callback.unbind("dragging", this.redraw);
            this.end.callback.unbind("dragEnd", this.redraw);
        }

        this.callback.remove();
    }
}

odr.Handle = function() {
    this.id = odr.dragHandleIdPrefix + odr.idCounter++;
    odr.register.addItem(this);
    this.x = 0;
    this.y = 0;
    this.value = undefined;
    this.callback = new odr.Callback();

    this.snapPosition = function() {
        odr.snapPosition(this);
    }

    this.draw = function(parent) {
        this.snapPosition();

        j("#" + this.id).remove();

        odr.svg.circle(parent, this.x, this.y, odr.handleSize, {
            "class" : odr.dragHandleClass,
            "id" : this.id
        });

        element = j("#" + this.id);
        odr.enableDragging(element);

        this.callback.draw();
    }

    this.redraw = function() {
        element = j("#" + this.id);
        element.attr("cx", this.x);
        element.attr("cy", this.y);

        this.callback.redraw();
    }

    this.remove = function() {
        j("#" + this.id).remove();
        odr.register.removeItem(this.id);
        this.callback.remove();
    }

    this.callback.dragging(function() {
        this.redraw();
    }.createDelegate(this));

    this.callback.dragEnd(function() {
        this.snapPosition();
        this.redraw();
    }.createDelegate(this));
}

odr.Callback = function() {
    this.listeners = {
        draw : new Array(),
        redraw : new Array(),
        remove : new Array(),
        dragStart : new Array(),
        dragging : new Array(),
        dragEnd : new Array()
    };

    this.draw = function(listener) {
        if (listener == undefined) {
            this.fire(this.listeners.draw);
            return;
        }

        this.listeners.draw[this.listeners.draw.length] = listener;
    }

    this.redraw = function(listener) {
        if (listener == undefined) {
            this.fire(this.listeners.redraw);
            return;
        }

        this.listeners.redraw[this.listeners.redraw.length] = listener;
    }

    this.remove = function(listener) {
        if (listener == undefined) {
            this.fire(this.listeners.remove);
            return;
        }

        this.listeners.remove[this.listeners.remove.length] = listener;
    }

    this.dragStart = function(listener) {
        if (listener == undefined) {
            this.fire(this.listeners.dragStart);
            return;
        }

        this.listeners.dragStart[this.listeners.dragStart.length] = listener;
    }

    this.dragging = function(listener) {
        if (listener == undefined) {
            this.fire(this.listeners.dragging);
            return;
        }

        this.listeners.dragging[this.listeners.dragging.length] = listener;
    }

    this.dragEnd = function(listener) {
        if (listener == undefined) {
            this.fire(this.listeners.dragEnd);
            return;
        }

        this.listeners.dragEnd[this.listeners.dragEnd.length] = listener;
    }

    this.fire = function(listeners) {
        for(i = 0; i < listeners.length; i++) {
            listeners[i]();
            return;
        }
    }

    this.unbind = function(event, listener) {
        listeners = undefined;
        if ("draw" == event) {
            listeners = this.listeners.draw;
        } else if ("redraw" == event) {
            listeners = this.listeners.redraw;
        } else if ("remove" == event) {
            listeners = this.listeners.remove;
        } else if ("dragStart" == event) {
            listeners = this.listeners.dragStart;
        } else if ("dragging" == event) {
            listeners = this.listeners.dragging;
        } else if ("dragEnd" == event) {
            listeners = this.listeners.dragEnd;
        } else {
            console.log("Invalid event name");
            return;
        }

        for(i = 0; i < listeners; i++) {
            if (listeners[i] == listener) {
                listeners.splice(i,1);
                i--;
            }
        }
    }
}