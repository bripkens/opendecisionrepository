/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan
 */
@XmlRootElement(name = "diagramProperties")
public class DiagramProperties implements Serializable {

    private long projectId;
    private String projectName;
    private String diagramType;

    /**
     * getter for diagramType
     * @return diagramType
     */
    public String getDiagramType() {
        return diagramType;
    }

    /**
     * setter for diagramType
     * @param diagramType
     */
    public void setDiagramType(String diagramType) {
        this.diagramType = diagramType;
    }

    /**
     * getter for projectId
     * @return projectId
     */
    public long getProjectId() {
        return projectId;
    }

    /**
     * setter for project id
     * @param projectId
     */
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    /**
     * getter for project name
     * @return project name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * setter for project name
     * @param projectName
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
