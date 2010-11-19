var odr = odr || {};
var j = jQuery.noConflict();





// svg container
odr.targetId = "svgTarget";
odr.svgContainerPadding = {
    x : 10,
    y : 10
};
odr.svgContainerInitialSize = {
    width : "auto",
    height : "auto"
}
odr.viewportScrollMargin = {
    x : 30,
    y : 30
}
odr.scrollSpeed = {
    up : -10,
    down : 10,
    left : -10,
    right : 10
}
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
odr.smallMenu = "smallMenu";
odr.smallMenuCollapsedSize = "-15px"
odr.smallMenuAnimationDuration = 500;
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
    x : 10,
    y : 10
};
odr.gridClass = "grid";

// handles
odr.handleSize = 6;

// corners of the rectangle
odr.rx = 10;
odr.ry = 10;

// containers for layering
odr.lineContainer = "lines";
odr.handleContainer = "handles";
odr.nodeContainer = "nodes";

odr.svgStyleUrl = "resources/css/graph.css";
odr.svgStyle = '@import "' + odr.svgStyleUrl + '";';

// scale
odr.scale = 1
odr.inputSetScale = "inputSetScale";
odr.inputScale = "inputScale";
odr.errorClass = "error";
/*
 * Variables
 */
odr.idCounter = 0;
odr.svg;
odr.dragPreviousEvent = new Array();
odr.elementToDrag = new Array();
odr.itemToDrag = new Array();



j(document).ready(function() {
    odr.register = new odr.Register();

//odr.smallMenu = "smallMenu";
//odr.smallMenuCollapsedSize = "-15px;"

    j("." + odr.smallMenu).mouseenter(function() {
        j(this).stop(true, false).animate({
           "top" : odr.smallMenuCollapsedSize
        }, odr.smallMenuAnimationDuration);
    });

    j("." + odr.smallMenu).mouseleave(function() {
        j(this).stop(true, false).animate({
           "top" : (j(this).height() + 11) * -1
        }, odr.smallMenuAnimationDuration);
    });

    j("#" + odr.targetId).svg({
        onLoad: odr.drawInitial
    });

    j("#addNode").click(function(e) {
        
        source = new odr.Node();
        source.width = 200;
        source.height = 40;
        source.center(500, 100);
        source.draw();

        target = new odr.Node();
        target.width = 200;
        target.height = 40;
        target.center(500, 500);
        target.draw();

        handle1 = new odr.Handle();
        handle1.x = 220;
        handle1.y = 320;

        handle2 = new odr.Handle();
        handle2.x = 300;
        handle2.y = 320;

        handle3 = new odr.Handle();
        handle3.x = 400;
        handle3.y = 320;

        relationship = new odr.Relationship();
        relationship.setSource(source);
        relationship.setTarget(target);
        relationship.addHandle(handle1);
        relationship.addHandle(handle2);
        relationship.addHandle(handle3);


        relationship.draw();
        handle1.draw();
        handle2.draw();
        handle3.draw();

        odr.assertContainerSize();
    });

    j("." + odr.toggle).click(odr.toggleMenu);

    odr.prepareExport();

    odr.prepareZoom();

    j(window).resize(odr.resize);
    odr.resize();
});

odr.drawInitial = function(svg) {
    odr.svg = svg;

    odr.svg.style(odr.svgStyle);

    container = j("#" + odr.targetId + " svg");
    if (odr.svgContainerInitialSize.width == "auto") {
        container.attr("width", document.documentElement.clientWidth);
    } else {
        container.attr("width", odr.svgContainerInitialSize.width);
    }

    if (odr.svgContainerInitialSize.height == "auto") {
        container.attr("height", document.documentElement.clientHeight);
    } else {
        container.attr("height", odr.svgContainerInitialSize.height);
    }



    odr.svg.group(odr.lineContainer);
    odr.svg.group(odr.nodeContainer);
    odr.svg.group(odr.handleContainer);
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

odr.assertContainerSize = function(elementId) {

    container = j("#" + odr.targetId + " svg");

    containerWidth = 0;
    containerHeight = 0;

    selector = undefined;

    if (elementId != undefined) {
        selector = "#" + elementId;
        containerWidth = container.attr("width");
        containerHeight = container.attr("height");
    } else {
        selector = "." + odr.nodeClass + ", ." + odr.dragHandleClass;
    }

    j(selector).each(function() {
        currentElement = odr.register.getItem(j(this).attr("id"));
        lowerRightCorner = currentElement.lowerRightCorner();

        lowerRightCorner.x *= odr.scale;
        lowerRightCorner.y *= odr.scale;

        if (lowerRightCorner.x > containerWidth) {
            containerWidth = lowerRightCorner.x;
        }

        if (lowerRightCorner.y > containerHeight) {
            containerHeight = lowerRightCorner.y;
        }
    });

    container.attr("width", containerWidth);
    container.attr("height", containerHeight);
    parent = container.parent();
    parent.css({
        width :  containerWidth,
        height : containerHeight
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

odr.dragStart = function(e) {
    if(e.ctrlKey) {
        return false;
    }

    button = e.button;
    odr.elementToDrag[button] = j(this);
    odr.dragPreviousEvent[button] = e;
    j("body").mousemove(odr.dragging);
    j("body").addClass(odr.gridClass);

    odr.itemToDrag[button] = odr.register.getItem(j(this).attr("id"));
    odr.itemToDrag[button].callback.dragStart();

    return false;
}

odr.dragging = function(e) {
    button = e.button;
    if (odr.dragPreviousEvent[button] == undefined) {
        j("body").unbind("mousemove");
        return false;
    }
    
    odr.itemToDrag[button].x = odr.itemToDrag[button].x + ((e.pageX - odr.dragPreviousEvent[button].pageX) * (1 / odr.scale));
    odr.itemToDrag[button].y = odr.itemToDrag[button].y + ((e.pageY - odr.dragPreviousEvent[button].pageY) * (1 / odr.scale));
    odr.dragPreviousEvent[button] = e;

    odr.itemToDrag[button].callback.dragging();

    odr.assertContainerSize(odr.itemToDrag[button].id);


    viewportWidth = j(window).width();
    viewportHeight = j(window).height();
    scrollLeft = j(window).scrollLeft();
    scrollTop = j(window).scrollTop();
    lowerRightCorner = odr.itemToDrag[button].lowerRightCorner();
    topLeftCorner = odr.itemToDrag[button].topLeftCorner();

    scrollX = 0;
    scrollY = 0;
    
    doScrollRight = lowerRightCorner.x - scrollLeft > viewportWidth - odr.viewportScrollMargin.x;
    doScrollLeft = topLeftCorner.x - scrollLeft < odr.viewportScrollMargin.x;
    doScrollDown = lowerRightCorner.y > viewportHeight - odr.viewportScrollMargin.y - scrollTop;
    doScrollUp = topLeftCorner.y < scrollTop + odr.viewportScrollMargin.y;

    if (doScrollRight) {
        scrollX = odr.scrollSpeed.right;
    } else if (doScrollLeft) {
        scrollX = odr.scrollSpeed.left;
    }

    if (doScrollUp) {
        scrollY = odr.scrollSpeed.up;
    } else if (doScrollDown) {
        scrollY = odr.scrollSpeed.down;
    }

    window.scrollBy(scrollX, scrollY);

    return false;
}



odr.dragStop = function(e) {
    button = e.button;

    if (odr.itemToDrag[button] == undefined || e.ctrlKey) {
        return false;
    }

    j("body").removeClass(odr.gridClass);

    j("body").unbind("mousemove");
    odr.dragPreviousEvent[button] = undefined;

    odr.itemToDrag[button].callback.dragEnd();

    odr.assertContainerSize();

    return false;
}









/*
 * Export
 */
odr.prepareExport = function() {
    j("div.export ul li").click(function() {

        format = j(this).attr("class");

        j.get(odr.svgStyleUrl, function(cssStyle) {
            form = j("div.export form");

            dataInput = form.children("input#data");
            dataInput.val(j("#" + odr.targetId).html().replace(odr.svgStyle, cssStyle));

            formatInput = form.children("input#format");
            formatInput.val(format);

            form.submit();
        });
        
    })
}



/*
 * Zoom
 */
odr.prepareZoom = function() {
    j("#" + odr.inputSetScale).click(odr.handleScaleInput);

    j("#" + odr.inputScale).keyup(function(e) {
        if(e.keyCode == 13) {
            odr.handleScaleInput();
        }
    });

    j("#" + odr.inputScale).val(odr.scale * 100);
}

odr.handleScaleInput = function() {
    scaleInput = j("#" + odr.inputScale);

    newScale = scaleInput.val();

    newScale = parseFloat(newScale);

    if (isNaN(newScale)) {
        scaleInput.addClass(odr.errorClass);
        return;
    }

    scaleInput.removeClass(odr.errorClass);

    odr.setScale(newScale);
}

odr.setScale = function(newScale) {
    odr.scale = newScale / 100;

    transformAttribute = "scale(" + odr.scale + ")";

    j("#" + odr.handleContainer).attr("transform", transformAttribute);
    j("#" + odr.lineContainer).attr("transform", transformAttribute);
    j("#" + odr.nodeContainer).attr("transform", transformAttribute);

    odr.assertContainerSize();
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

odr.Node = function(parent) {
    this.id = odr.nodeIdPrefix + odr.idCounter++;
    odr.register.addItem(this);
    this.text = "";
    this.parent = parent;
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

        parent = parent = j("#" + odr.nodeContainer);

        odr.svg.rect(parent, this.x, this.y, this.width, this.height, odr.rx, odr.ry, {
            "class" : odr.nodeClass,
            "id" : this.id
        });

        element = j("#" + this.id);
        odr.enableDragging(element);

        this.callback.draw();
    }

    this.redraw = function() {
        element = j("#" + this.id);
        element.attr("x", this.x);
        element.attr("y", this.y);

        this.callback.redraw();
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

    this.lowerRightCorner = function() {
        return {
            x : this.x + this.width + 5,
            y : this.y + this.height + 5
        };
    }

    this.topLeftCorner = function() {
        return {
            x : this.x,
            y : this.y
        }
    }

    this.callback.dragging(function() {
        this.redraw();
    }.createDelegate(this));

    this.callback.dragEnd(function() {
        this.snapPosition();
        this.redraw();
        this.parent.optimizePath();
    }.createDelegate(this));
}

odr.Relationship = function() {
    this.id = odr.relationshipIdPrefix + odr.idCounter++;
    odr.register.addItem(this);
    
    this.source = undefined;
    this.target = undefined;
    
    this.text = "";
    this.handles = new Array();
    this.value = undefined;
    
    this.callback = new odr.Callback();

    this.addHandle = function(handle) {
        this.handles[this.handles.length] = handle;
        handle.parent = this;
    }

    this.addHandleBetween = function(first, second, handle) {
        if (this.source.id == first.id) {
            this.handles.splice(0, 0, handle);
            handle.parent = this;
            this.draw();
            return;
        }

        for(i = 0; i < this.handles.length; i++) {
            if (this.handles[i].id == first.id) {
                if (i+1 == this.handles.length) {
                    this.handles[this.handles.length] = handle;
                    handle.parent = this;
                }
                else {
                    this.handles.splice(i+1, 0, handle);
                    handle.parent = this;
                }
                this.draw();
                return;
            }
        }
    }

    this.getHandle = function(handleId) {
        for(i = 0; i < this.handles.length; i++) {
            if (this.handles[i] == handleId) {
                return this.handles[i];
            }
        }
        
        return undefined;
    }

    this.removeHandle = function(handleId) {
        for(i = 0; i < this.handles.length; i++) {
            if (this.handles[i].id == handleId) {
                this.handles[i].remove();
                this.handles.splice(i, 1);
                return;
            }
        }
    }

    this.setSource = function(source) {
        this.source = source;
        this.source.parent = this;
    }

    this.setTarget = function(target) {
        this.target = target;
        this.target.parent = this;
    }

    this.draw = function() {
        j("#" + this.id).remove();

        parent = j("#" + odr.lineContainer);
        parent = odr.svg.group(parent, this.id, {
            "class" : odr.relationshipClass
        });


        element = this.source;

        for (i = 0; i < this.handles.length; i++) {
            line = new odr.Line();
            line.parent = this;
            line.setStart(element);
            line.setEnd(this.handles[i]);
            line.draw(parent);

            element = this.handles[i];
        }

        line = new odr.Line(this);
        line.setStart(element);
        line.setEnd(this.target);
        line.draw(parent);
    }

    this.redraw = function() {
    }

    this.optimizePath = function() {
        firstX = this.source.center().x;
        firstY = this.source.center().y;
        secondX = undefined;
        secondY = undefined;
        thirdX = undefined;
        thirdY = undefined;

        for(i = 0; i < this.handles.length; i++) {
            if (secondX == undefined) {
                secondX = this.handles[i].center().x;
                secondY = this.handles[i].center().y;
                continue;
            }

            thirdX = this.handles[i].center().x;
            thirdY = this.handles[i].center().y;


            if ((firstX == secondX && secondX == thirdX) || (firstY == secondY && secondY == thirdY)) {
                this.removeHandle(this.handles[i-1].id);
            }

            firstX = secondX;
            firstY = secondY;
            secondX = thirdX;
            secondY = thirdY;
        }

        if (secondX == undefined) {
            return;
        }

        thirdX = this.target.center().x;
        thirdY = this.target.center().y;

        if ((firstX == secondX && secondX == thirdX) || (firstY == secondY && secondY == thirdY)) {
            this.removeHandle(this.handles[this.handles.length - 1].id);
        }

        this.draw();
    }
}

odr.Line = function(parent) {
    this.id = odr.lineIdPrefix + odr.idCounter++;
    odr.register.addItem(this);

    this.start = undefined;
    this.end = undefined;
    this.value = undefined;
    this.parent = parent;

    this.setStart = function(element) {
        if (this.start != undefined) {
            this.start.callback.unbind("redraw", this.redraw);
        }

        this.start = element;
        this.start.callback.redraw(this.redraw.createDelegate(this));
    }

    this.setEnd = function(element) {
        if (this.end != undefined) {
            this.end.callback.redraw("redraw", this.redraw);
        }

        this.end = element;
        this.end.callback.redraw(this.redraw.createDelegate(this));
    }

    this.draw = function(parent2) {
        j("#" + this.id).remove();
        odr.svg.line(parent2,
            this.start.center().x,
            this.start.center().y,
            this.end.center().x,
            this.end.center().y,
            {
                "class" : odr.lineClass,
                "id" : this.id
            });

        j("#" + this.id).bind("click", {
            self:this
        }, this.onClick);
    }

    this.onClick = function(e) {
        var self = e.data.self;

        handle = new odr.Handle();
        handle.x = e.pageX  * (1 / odr.scale);
        handle.y = e.pageY  * (1 / odr.scale);
        handle.draw();
        
        self.parent.addHandleBetween(self.start, self.end, handle);
    }

    this.redraw = function() {
        element = j("#" + this.id);

        element.attr("x1", this.start.center().x);
        element.attr("y1", this.start.center().y);
        element.attr("x2", this.end.center().x);
        element.attr("y2", this.end.center().y);
    }
}

odr.Handle = function(parent) {
    this.id = odr.dragHandleIdPrefix + odr.idCounter++;
    odr.register.addItem(this);
    this.parent = parent;
    this.x = 0;
    this.y = 0;
    this.value = undefined;
    this.callback = new odr.Callback();

    this.snapPosition = function() {
        odr.snapPosition(this);
    }

    this.draw = function() {
        this.snapPosition();

        j("#" + this.id).remove();

        parent = parent = j("#" + odr.handleContainer);
        odr.svg.circle(parent, this.x, this.y, odr.handleSize, {
            "class" : odr.dragHandleClass,
            "id" : this.id
        });

        element = j("#" + this.id);
        odr.enableDragging(element);

        j("#" + this.id).bind("click", {
            self:this
        }, this.onClick);

        this.callback.draw();
    }

    this.onClick = function(e) {
        if(!e.ctrlKey) {
            return;
        }

        var self = e.data.self;
        self.parent.removeHandle(self.id);
        self.parent.draw();
    }

    this.redraw = function() {
        element = j("#" + this.id);
        element.attr("cx", this.x);
        element.attr("cy", this.y);

        this.callback.redraw();
    }

    this.center = function(x, y) {
        if (x == undefined && y == undefined) {
            return {
                "x" : this.x,
                "y" : this.y
            };
        }

        this.x = x;
        this.y = y;
    }

    this.remove = function() {
        j("#" + this.id).remove();
    }

    this.lowerRightCorner = function() {
        return {
            x : this.x + (odr.handleSize / 2) + 5,
            y : this.y + (odr.handleSize / 2) + 5
        }
    }

    this.topLeftCorner = function() {
        return {
            x : this.x - (odr.handleSize / 2),
            y : this.y - (odr.handleSize / 2)
        }
    }

    this.callback.dragging(function() {
        this.redraw();
    }.createDelegate(this));

    this.callback.dragEnd(function() {
        this.snapPosition();
        this.redraw();
        this.parent.optimizePath();
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
            alert("Invalid item name");
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