/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.viewpoint.chronological;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Version;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class InitChronologicalView {

    private final Project project;

    private ChronologicalViewVisualization visualization;

    private Map<List<Long>, List<Version>> versionsForStakeholderGroups;

    private List<ViewBlock> blocks;




    public InitChronologicalView(Project project) {
        this.project = project;
    }




    public ChronologicalViewVisualization getView() {
        visualization = new ChronologicalViewVisualization();
        visualization.setDocumentedWhen(new Date());

        versionsForStakeholderGroups = new HashMap<List<Long>, List<Version>>();
        blocks = new ArrayList<ViewBlock>();

        assignVersionsToStakeholderGroups();
        assignVersionsToIterations();

        Collections.sort(blocks);

        visualization = new VisualizationBuilder(visualization, blocks).build().getVisualization();

        return visualization;
    }


    // <editor-fold defaultstate="collapsed" desc="get view step one">


    private void assignVersionsToStakeholderGroups() {
        for (Decision d : project.getDecisions()) {
            if (d.isRemoved()) {
                continue;
            }

            for (Version v : d.getVersions()) {
                if (!v.isRemoved()) {
                    assignVersionToStakeholderGroup(v);
                }
            }
        }
    }




    private void assignVersionToStakeholderGroup(Version v) {
        List<Long> identification = generateProjectMemberIdentification(v);

        List<Version> versions = versionsForStakeholderGroups.get(identification);

        if (versions == null) {
            versions = new ArrayList<Version>();
            versionsForStakeholderGroups.put(identification, versions);
        }

        versions.add(v);
    }




    /**
     * We need this method to identify the stakeholders in a fast way. The array.equals
     * specification says that one array equals an other array if the size, order
     * and the elements themselves are equal.
     * @param v The version for which the identification should be generated
     * @return  the identification
     */
    private List<Long> generateProjectMemberIdentification(Version v) {
        Collection<ProjectMember> initiators = v.getInitiators();

        List<Long> identification = new ArrayList<Long>(initiators.size());

        for (ProjectMember pm : initiators) {
            identification.add(pm.getId());
        }

        Collections.sort(identification);

        return identification;
    }


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="get view step two">

    private void assignVersionsToIterations() {
        for (Iteration it : project.getIterations()) {
            ViewBlock span = new ViewBlock(it);

            addVersionsWhichWereMadeDuringIteration(span);
            blocks.add(span);
        }
    }




    private void addVersionsWhichWereMadeDuringIteration(ViewBlock block) {
        for (Entry<List<Long>, List<Version>> entry : versionsForStakeholderGroups.entrySet()) {
            for (Version version : entry.getValue()) {
                if (createdDuringIteration(block.getIteration(), version)) {
                    block.addVersion(entry.getKey(), version);
                }
            }
        }
    }




    private boolean createdDuringIteration(Iteration it, Version v) {
        return it.getStartDate().before(v.getDecidedWhen()) && it.getEndDate().after(v.getDecidedWhen());
    }
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="getter">

    public Project getProject() {
        return project;
    }




}



