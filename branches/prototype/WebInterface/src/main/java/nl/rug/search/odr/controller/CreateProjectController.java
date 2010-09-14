
package nl.rug.search.odr.controller;

import javax.faces.event.ActionEvent;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class CreateProjectController {

    private String name;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void submit() {
        System.out.println("Has been submitted!");
    }

    public void reset() {
        name = null;
        description = null;
    }

    public void reset(ActionEvent e) {
        reset();
    }
}
