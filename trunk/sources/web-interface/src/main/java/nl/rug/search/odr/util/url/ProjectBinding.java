
package nl.rug.search.odr.util.url;

import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.project.ProjectLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ProjectBinding extends Binding {

    private final ProjectLocal pl;

    public ProjectBinding(String regex, ProjectLocal pl) {
        super(regex);
        this.pl = pl;
    }

    @Override
    protected String convert(String val) {
        Project p = pl.getByName(val);

        if (p == null) {
            return "-1";
        } else {
            return p.getId().toString();
        }
    }

    
}
