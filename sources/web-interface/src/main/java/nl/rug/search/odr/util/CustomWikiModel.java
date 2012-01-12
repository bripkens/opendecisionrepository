
package nl.rug.search.odr.util;

import info.bliki.wiki.model.WikiModel;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.controller.BaseController;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class CustomWikiModel {

    private final WikiModel wikiModel;

    public CustomWikiModel() {
        BaseController controller = new BaseController();
        String path = controller.
                getBaseHref().
                substring(1).
                concat(RequestParameter.PROJECT_PATH_SHORT).
                concat("${title}");


        wikiModel = new WikiModel("${image}", path);
    }

    public String wikiToHtml(String wiki) {
        return wikiModel.render(wiki);
    }
}
