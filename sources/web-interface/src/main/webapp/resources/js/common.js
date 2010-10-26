var j = jQuery.noConflict();

j(document).ready(function() {
    preparePrefill();

    preselect();

    enableToggling();

    enableModalPopup();

    datetimepickerFix();
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
    j("a.togglelink").each(function() {
        var button = j(this);
        var name = button.attr("name");
        var element = j("#" + name);

        button.click(function() {
            toggle(element);

            return false;
        });

        element.children("input.togglelink").first().click(function() {
            toggle(element);

            return false;
        });
    });
}

function toggle(element) {
    if (element.css("display") == "none") {
        element.slideDown();
    } else {
        element.slideUp();
    }
}

function enableModalPopup() {
    j(".modalPopupLink").click(function() {
        showModalPopup(j(this).attr("name"));
    })

    j(".hideModalPopupLink").click(hideModalPopup);

    j("#backgroundPopup").click(hideModalPopup);
}

function showModalPopup(id) {
    var windowWidth = j(document).width();
    var windowHeight = j(document).height();
    var popupHeight = j("#" + id).height();
    var popupWidth = j("#" + id).width();

    j("#backgroundPopup").css({
        "height": windowHeight,
        "width": windowWidth
    });

    j("#" + id).css({
        "top": windowHeight/2-popupHeight/2,
        "left": windowWidth/2-popupWidth/2
    });

    j("#backgroundPopup").fadeIn("slow");
    j("#" + id).fadeIn("slow");
}

function hideModalPopup() {
    j(".modalPopup").fadeOut("slow");
    j("#backgroundPopup").fadeOut("slow");
}

function hideDecisionAddForm() {
    toggle(j('#decisionQuickAddContainer'));
}
function showIterationDeleteForm() {
    showModalPopup("deleteConfirmationPopup");
}
function datetimepickerValidationFix() {
    j(".datetimepickerDateInput").focus().blur();
}
function refresh() {
    location.reload(true);
}