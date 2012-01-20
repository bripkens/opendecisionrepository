
package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface WizardStep {
    String getFaceletName();

    String getSidebarFaceletName();

    String getStepName();

    void focus();

    void blur();
}
