/**
 * @author cm
 */
$(document).ready( function() {
				
    $("#toc .hideArrow").toggle(
        function() {
            $("#toc ul").slideUp("slow");
        },
        function() {
            $("#toc ul").slideDown("slow");
        }
        );
				
});