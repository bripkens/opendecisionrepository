/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class MalformedJsonException extends RuntimeException {


    private String title, description;


    public MalformedJsonException() {
    }




    public MalformedJsonException(Throwable cause) {
        super(cause);
    }




    public MalformedJsonException(String message, Throwable cause) {
        super(message, cause);
    }




    public MalformedJsonException(String message) {
        super(message);
    }




    public String getDescription() {
        return description;
    }




    public void setDescription(String description) {
        this.description = description;
    }




    public String getTitle() {
        return title;
    }




    public void setTitle(String title) {
        this.title = title;
    }


    
}
