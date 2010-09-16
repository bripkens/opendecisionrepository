$(document).ready(function() {
    draw();
});


function draw() {
    var canvas = document.getElementById("canvas");
    if (canvas.getContext) {
        var ctx = canvas.getContext("2d");

        ctx.fillStyle = "rgb(40,0,0)";
        ctx.fillRect (20, 20, 65, 60);

        ctx.fillStyle = "rgba(0, 0, 160, 0.5)";
        ctx.fillRect (40, 40, 65, 60);
    }
}