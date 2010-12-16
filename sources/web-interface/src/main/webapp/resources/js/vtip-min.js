/**
Vertigo Tip by www.vertigo-project.com
Requires jQuery
*/



/**
 *                                              NOTICE
 * Don't replace this version with a newer or minified / packed version as this version has been modified
 * to fit the needs of the visualization!
 *                                              NOTICE
 */





this.vtip = function(elements) {
    this.xOffset = -10; // x distance from mouse
    this.yOffset = 10; // y distance from mouse

    if (elements == undefined) {
        elements = jQuery(".vtip");
    }

    elements.unbind("hover").hover(
        function(e) {
            this.t = this.title;
            this.title = '';
            this.top = (e.pageY + yOffset); this.left = (e.pageX + xOffset);

            jQuery('body').append( '<p id="vtip"><img id="vtipArrow" />' + this.t + '</p>' );

            jQuery('p#vtip #vtipArrow').attr("src", 'resources/images/vtip_arrow.png');
            jQuery('p#vtip').css("top", this.top+"px").css("left", this.left+"px").fadeIn("slow");

        },
        function() {
            this.title = this.t;
            jQuery("p#vtip").fadeOut("slow").remove();
        }
    ).mousemove(
        function(e) {
            this.top = (e.pageY + yOffset);
            this.left = (e.pageX + xOffset);

            jQuery("p#vtip").css("top", this.top+"px").css("left", this.left+"px");
        }
    );

};

jQuery(document).ready(function(jQuery){vtip();})