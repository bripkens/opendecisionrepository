/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common;

/**
 *
 * @author eigo
 */
public final class Documentation {
    
    private static final String DOCUMENTATION = "\n<!--Please do not change this content-->\n";

    private Documentation() {
    }
    
    /**
     * encodes the documentation with a notification for the user
     * @param content to wrap with userhints
     * @return wrapped content
     */
    public static String encode(String content){
        return DOCUMENTATION+content+DOCUMENTATION;
    }
    
    /**
     * decodes a documentation
     * simply removes the user notification
     * @param docString with user notification
     * @return blank documentation
     */
    public static String decode(String docString){
        return docString.replace(DOCUMENTATION, "");
    }

    /**
     * getter for user notification
     * @return user notification
     */
    public static String getDOCUMENTATION() {
        return DOCUMENTATION;
    }
    
}
