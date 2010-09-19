var j = jQuery.noConflict();

j(document).ready(function() {
    preparePrefill();
});

var prefillValue = new Array();
var prefillTypes = new Array();

function preparePrefill() {
    j("input.prefill").each(function() {
        var element = j(this);
        var id = element.attr("id");
        var text = j(".prefillValue[for=" + id + "]").text();
        
        prefillValue[id] = text;
        prefillTypes[id] = element.attr("type");
        
        element.focus(function() {
            var element = j(this);

            if (element.val() == prefillValue[element.attr("id")]) {
                element.val("");
                element.attr("type", prefillTypes[element.attr("id")]);
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
        element.attr("type", "text")
        element.addClass("prefilled");
    }
}