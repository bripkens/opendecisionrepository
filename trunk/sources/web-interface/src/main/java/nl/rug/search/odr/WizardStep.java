
package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface WizardStep {
    String getFaceletName();

    void focus();

    void blur();

    void dispose();
}
