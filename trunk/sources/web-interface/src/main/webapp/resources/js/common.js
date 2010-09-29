var j = jQuery.noConflict();

j(document).ready(function() {
    preparePrefill();
    j("a#togglelink").click(function(){
        var button = j(this);
        var name = button.attr("name");
        var element = j("div#" + name);

        if (element.css("display") == "none") {
            element.slideDown();
        } else {
            element.slideUp();
        }

    });
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