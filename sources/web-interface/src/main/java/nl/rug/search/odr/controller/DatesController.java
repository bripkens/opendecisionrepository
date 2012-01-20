package nl.rug.search.odr.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import nl.rug.search.odr.util.JsfUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@ApplicationScoped
public class DatesController {

    private final String[] hours;

    private final String[] minutes;

    private final String[] day;

    private final Map<String, Integer> month;

    private final String[] year;



    /**
     * create the different time arrays for:
     * hours,
     * minutes,
     * daysinMonts,
     * year and a
     * map<Integer, String> with monthnumber and monthname
     */
    public DatesController() {
        hours = getStringArray(0, 23);
        minutes = getStringArray(0, 59);
        day = getStringArray(1, 31);
        year = getStringArray(1900, 2100);
        month = new LinkedHashMap<String, Integer>();

        String months = JsfUtil.evaluateExpressionGet("#{common['page.dates.month']}", String.class);
        String[] names = months.split(",");
        for (int i = 0; i < 12; i++) {
            month.put(names[i], i + 1);
        }

    }




    public String[] getHours() {
        return hours;
    }




    public String[] getMinutes() {
        return minutes;
    }




    public String[] getDay() {
        return day;
    }




    /**
     * return the year numbers
     * @return int[]
     */
    public String[] getYear() {
        return year;
    }




    /**
     * return the months in a map of string, Integer
     * @return
     */
    public Map<String, Integer> getMonth() {
        return month;
    }



    /**
     * create a Stringarray with all entries between and with the two parameters
     * @param from the first entry in the array
     * @param to the last entry in the array
     * @return the entire array
     */
    private String[] getStringArray(int from, int to) {
        String[] result = new String[to - from + 1];

        for (int i = from; i <= to; i++) {
            result[i - from] = "" + i;
        }

        return result;
    }
}
