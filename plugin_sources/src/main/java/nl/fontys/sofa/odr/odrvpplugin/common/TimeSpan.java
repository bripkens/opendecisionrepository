/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common;

import java.util.Date;

/**
 *
 * @author Michael
 */
public class TimeSpan {

    private final Date from;
    private final Date until;

    /**
     * TimeSpan, identifies a timespan between two dates
     * @param from startdate
     * @param until enddate
     */
    public TimeSpan(Date from, Date until) {
        this.from = from;
        this.until = until;
    }

    /**
     * getter for startdate
     * @return startdate
     */
    public Date getFrom() {
        return from;
    }

    /**
     * getter for enddate
     * @return enddate
     */
    public Date getUntil() {
        return until;
    }
    
    
}
