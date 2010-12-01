/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.viewpoint.chronological;

import nl.rug.search.odr.viewpoint.relationship.RelationshipViewVisualization;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

    private RelationshipViewVisualization visualization;

    private Map<Long[], List<Version>> versionsForStakeholderGroups;

    private List<IterationSpan> columnsForIterations;




    public InitChronologicalView(Project project) {
        this.project = project;
    }




    public RelationshipViewVisualization getView() {
        visualization = new RelationshipViewVisualization();

        versionsForStakeholderGroups = new HashMap<Long[], List<Version>>();
        columnsForIterations = new ArrayList<IterationSpan>();

        assignVersionsToStakeholderGroups();
        assignVersionsToIterations();

        Collections.sort(columnsForIterations);

        addNodesToVisualization();

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
        Long[] identification = generateProjectMemberIdentification(v);

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
    private Long[] generateProjectMemberIdentification(Version v) {
        Collection<ProjectMember> initiators = v.getInitiators();

        Long[] identification = new Long[initiators.size()];

        int i = 0;
        for (ProjectMember pm : initiators) {
            identification[i++] = pm.getId();
        }

        Arrays.sort(identification);

        return identification;
    }


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="get view step two">

    private void assignVersionsToIterations() {
        for (Iteration it : project.getIterations()) {
            IterationSpan span = new IterationSpan(it);

            addVersionsWhichWereMadeDuringIteration(span);
            columnsForIterations.add(span);
        }
    }




    private void addVersionsWhichWereMadeDuringIteration(IterationSpan ispan) {
        for (Entry<Long[], List<Version>> entry : versionsForStakeholderGroups.entrySet()) {
            for (Version version : entry.getValue()) {
                if (createdDuringIteration(ispan.it, version)) {
                    ispan.addVersion(entry.getKey(), version);
                }
            }
        }
    }




    private boolean createdDuringIteration(Iteration it, Version v) {
        return it.getStartDate().before(v.getDecidedWhen()) && it.getEndDate().after(v.getDecidedWhen());
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="get view step three">
    private void addNodesToVisualization() {
        
    }
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="getter">
    public Project getProject() {
        return project;
    }
    // </editor-fold>




    private static class IterationSpan implements Comparable<IterationSpan> {

        private Iteration it;

        private Map<Long[], List<Version>> versionsForStakeholderGroups;




        public IterationSpan(Iteration it) {
            this.it = it;
            this.versionsForStakeholderGroups = new HashMap<Long[], List<Version>>();
        }




        public void addVersion(Long[] group, Version v) {
            List<Version> versions = versionsForStakeholderGroups.get(group);

            if (versions == null) {
                versions = new ArrayList<Version>();
                versionsForStakeholderGroups.put(group, versions);
            }

            versions.add(v);
        }



        public List<List<Version>> getOrderedVersions() {
            List<List<Version>> allVersionsInGroups = new ArrayList<List<Version>>(versionsForStakeholderGroups.size());

            for(Entry<Long[], List<Version>> entry : versionsForStakeholderGroups.entrySet()) {

                Collections.sort(entry.getValue(), new Version.DecidedWhenComparator());

                allVersionsInGroups.add(entry.getValue());
            }

            return allVersionsInGroups;
        }



        @Override
        public int compareTo(IterationSpan o) {
            return it.getStartDate().compareTo(o.it.getStartDate());
        }




    }




}



