/* 
 * author Stefan
 */


j(document).ready(function() {

    var projectId = j("#externalVarProjectId").text();
    var requestUrl = j("#externalVarRequestUrl").text();

    j(function() {
        j.getJSON(requestUrl,
        {
            "id" : projectId
        },
        function(data) {
            console.log(data);
            j("#datepicker").datepicker({
                showWeek: true,
                firstDay: 1,


                beforeShowDay: function(date) {
                    //disabled all dates
                    return false;
                },

                onChangeMonthYear: function(year, month) {
                    setTimeout(                  
                        checkDates, 1000, data, month, year);
                }
            });
            var da = new Date();
            checkDates(data, da.getMonth()+1, (da.getYear()+1900));
        });
    });
});

function checkDates (data, month, year){
    var mo = new String(month);
    for(var i = 0; i < data.length; i++) {
        var name = data[i].Name;

        var startMonth = data[i].startDate.substring(5,7);
        var startYear  = data[i].startDate.substring(0,4);
        var startDay   = data[i].startDate.substring(8,10);

        var endMonth = data[i].endDate.substring(5,7);
        var endYear  = data[i].endDate.substring(0,4);
        var endDay   = data[i].endDate.substring(8,10);

        if(mo.length == 1){
            mo = "0"+mo;
        }
        if(((startMonth == mo) && (startYear == year))&&(endMonth != mo)){
            while(startDay <= 31){
                colortd(startDay, name, i);
                startDay++;
            }
        } else if(((startMonth == mo) && (startYear == year))&&((endMonth == mo) && (endYear == year))){
            if(startDay.substring(0,1) == 0){
                startDay = startDay.substring(1,2);
            }
            while(startDay <= endDay){
                colortd(startDay, name, i);
                startDay++;
            }

        } else if((startMonth != mo) &&((endMonth == mo) && (endYear == year))){
            var t = 1;
            while(t <= endDay){
                colortd(t, name, i);
                t++;
            }
        } else if(startMonth < mo && endMonth > mo && startYear == year && endYear == year){
            for(var a = 0; a <31; a++){
                alert(a);
                colortd(a, name, i)
            }
        }else if(startMonth < mo && startYear == year && endYear > year){
            for(var b = 0; b < 31; b++){
                colortd(b, name, i)
            }
        }
    }
}
function colortd(t, name, i){
    j("#datepicker").find("td").each(function(k){
        if( j(this).children("span").text() == t){
            j(this).addClass("vtip");
            j(this).attr("title", name);
            if(i % 2 == 0){
                j(this).children("span").css("background-color", "#7CCD7C" );
            }
            else{
                j(this).children("span").css("background-color", "#EEB422" );
            }
        }
    })
}