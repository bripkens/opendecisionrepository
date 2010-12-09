/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function doFilter (phrase, _id){
    if(typeof( phrase ) != 'string'){
        var words = phrase.value.toLowerCase().split(" ");
    }
    else {
        var words = phrase.toLowerCase().split(" ");
    }
    var table = document.getElementById(_id);
    var ele;
    for (var r = 1; r < table.rows.length; r++){
        ele = table.rows[r].innerHTML.replace(/<[^>]+>/g,"");
        var displayStyle = 'none';
        for (var i = 0; i < words.length; i++) {
            if (ele.toLowerCase().indexOf(words[i])>=0){
                j(".toggleSubTable").each(function(){
                    j(this).addClass("toggleSubTableHide");
                    j(this).removeClass("toggleSubTableShow");
                })
                displayStyle = '';
            } else {
                displayStyle = 'none';
                break;
            }
        }
        table.rows[r].style.display = displayStyle;
    }

}

j(document).ready(function() {
    j(".dataList").find("tr").each(function(){
        var current = j(this);
        if(current.next().hasClass("subRow")){
            var toogleImg = current.find("a.toggleSubTable").css("display", "block");

            toogleImg.click(function() {
                if (toogleImg.hasClass("toggleSubTableShow")) {
                    toogleImg.addClass("toggleSubTableHide");
                    toogleImg.removeClass("toggleSubTableShow");
                } else {
                    toogleImg.addClass("toggleSubTableShow");
                    toogleImg.removeClass("toggleSubTableHide");
                }

                var groupClass = null;

                var allClasses = current.attr("class").split(" ");

                for(var i = 0; i < allClasses.length; i++) {
                    if (allClasses[i].substr(0, 5) == "group") {
                        groupClass = allClasses[i];
                        break;
                    }
                }

                var next = current.next();

                while(next.hasClass(groupClass)){
                    next.toggle();

                    next = next.next();
                }

                return false;
            });
        }

    })
});