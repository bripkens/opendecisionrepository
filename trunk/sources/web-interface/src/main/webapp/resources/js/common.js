var j = jQuery.noConflict();

j(document).ready(function() {
    preparePrefill();

    preselect();

    enableToggling();
});

var prefillValue = new Array();

function preparePrefill() {
    j("input.prefill").each(function() {
        var element = j(this);
        var id = element.attr("id");
        var text = j(".prefillValue[for=" + id + "]").text();
        
        prefillValue[id] = text;
        
        element.focus(function() {
            var element = j(this);

            if (element.val() == prefillValue[element.attr("id")]) {
                element.val("");
                element.removeClass("prefilled");
            }
        });
        
        element.blur(function() {
            var element = j(this);
            
            prefill(element);
        });

        prefill(element);
    });
}

function prefill(element) {
    if (element.val() == "") {
        element.val(prefillValue[element.attr("id")]);
        element.addClass("prefilled");
    }
}

function preselect() {
    j("a.preselectValue").each(function() {
        var sourceElement = j(this);
        var optionToSelect = sourceElement.attr("name");

        var selectElement = sourceElement.next("select");

        var listener = selectElement.attr("onchange");
        selectElement.removeAttr("onchange");

        selectElement.children("option:selected").removeAttr("selected");
        selectElement.children("option[value=" + optionToSelect + "]").attr("selected", "selected");

        selectElement.attr("onchange", listener);
        selectElement.focus();
        selectElement.blur();

        selectElement.change(function() {
            var selectElement = j(this);
            var previousElement = selectElement.prev();

            var selectedOption = selectElement.children("option:selected").attr("value");
            previousElement.attr("name", selectedOption);
        });
    });
}

function enableToggling() {
    j("a.togglelink").click(function(){
        var button = j(this);
        var name = button.attr("name");
        var element = j("div#" + name);

        if (element.css("display") == "none") {
            element.slideDown();
        } else {
            element.slideUp();
        }

    });
}