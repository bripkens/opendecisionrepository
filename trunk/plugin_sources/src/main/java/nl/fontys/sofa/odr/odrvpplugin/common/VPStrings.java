/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common;

/**
 *
 * @author eigo
 */
public interface VPStrings {

    String MESSAGETAB = "ODR Log";
    String LOGINUSERNAME = "Login_Username";
    String LOGINPASSWORD = "Login_Password";
    String LOGINFLAG = "Login_Flag";
    String PROJECT = "project";
    String VP_DIAGRAMS = "Diagrams";
    String DECISIONSTATES = "decision_states";
    String DECISIONSTATECOLORS = "state_colors";
    String RELATIONSHIPTYPES = "relationship_types";
    String USERSETTINGS = "user_settings";
    // file specifics
    String FILESEPARATOR = System.getProperty("file.separator");
    String USERHOME = System.getProperty("user.home");
    String VPFOLDER = ".odrplugin";
    String USERSETTINGSFILE = "usersettings.xml";
    String USERSETTINGSFOLDER = USERHOME + FILESEPARATOR + VPFOLDER;
    String USERSETTINGSFILELOCATION =
            USERHOME
            + FILESEPARATOR
            + VPFOLDER
            + FILESEPARATOR
            + USERSETTINGSFILE;
    String CONNECTIONURL = "http://localhost:8080";
    String DEFAULTUSERNAME = "uwe@uwe.de";
    String DEFAULTPASSWORD = "12345";
    String DEFAULTCONNECTIONSTRING = "http://localhost:8080";
    
    String TESTSTRING = "IdentifierUsed4UnitTest";
}
