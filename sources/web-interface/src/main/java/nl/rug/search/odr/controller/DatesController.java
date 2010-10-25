package nl.rug.search.odr.controller;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@ApplicationScoped
public class DatesController {

    private final int[] hours;

    private final int[] minutes;




    public DatesController() {
        hours = getIntArray(0, 23);
        minutes = getIntArray(0, 59);
    }




    private static int[] getIntArray(int from, int to) {
        int[] result = new int[to - from + 1];

        for (int i = from; i <= to; i++) {
            result[i - from] = i;
        }

        return result;
    }




    public int[] getHours() {
        return hours;
    }




    public int[] getMinutes() {
        return minutes;
    }
}
