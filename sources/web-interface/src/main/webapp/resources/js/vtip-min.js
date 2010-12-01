/**
Vertigo Tip by www.vertigo-project.com
Requires jQuery
*/
this.vtip=function(){this.xOffset=-10;this.yOffset=10;j(".vtip").unbind().hover(function(a){this.t=this.title;this.title="";this.top=(a.pageY+yOffset);this.left=(a.pageX+xOffset);j("body").append('<p id="vtip"><img id="vtipArrow" />'+this.t+"</p>");j("p#vtip #vtipArrow").attr("src","images/vtip_arrow.png");j("p#vtip").css("top",this.top+"px").css("left",this.left+"px").fadeIn("slow")},function(){this.title=this.t;j("p#vtip").fadeOut("slow").remove()}).mousemove(function(a){this.top=(a.pageY+yOffset);this.left=(a.pageX+xOffset);j("p#vtip").css("top",this.top+"px").css("left",this.left+"px")})};jQuery(document).ready(function(a){vtip()});