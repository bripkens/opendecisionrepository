/*
 * Author: Ben Ripkens <bripkens.dev@gmail.com>
 */


/*
 * ###########################################################################
 *                              Menu settings
 */
odr.smallMenu = "smallMenu";
odr.smallMenuExpandedTop = "-15px";
odr.smallMenuCollapsedTop = function(height) {
    return (height + 11) * -1;
}
odr.smallMenuAnimationDuration = 500;





/*
 * ###########################################################################
 *                            Menu initialization
 */
odr.init(function() {
    j("." + odr.smallMenu).mouseenter(function() {
        j(this).stop(true, false).animate({
            "top" : odr.smallMenuExpandedTop
        }, odr.smallMenuAnimationDuration);
    });

    j("." + odr.smallMenu).mouseleave(function() {
        j(this).stop(true, false).animate({
            "top" : odr.smallMenuCollapsedTop(j(this).height())
        }, odr.smallMenuAnimationDuration);
    });
});