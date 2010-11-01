
package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class DecisionWizardTest extends AbstractSelenseTestCase {

    private void prepareForTest() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);
        ManageProjectTest.createDefaultProject(selenium);
    }

    
}
