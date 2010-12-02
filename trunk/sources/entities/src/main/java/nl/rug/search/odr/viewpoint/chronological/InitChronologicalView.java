/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.viewpoint.chronological;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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

    private List<IterationSpan> columnsForIterations;




    public InitChronologicalView(Project project) {
        this.project = project;
    }




    public ChronologicalViewVisualization getView() {
        visualization = new ChronologicalViewVisualization();

        versionsForStakeholderGroups = new HashMap<List<Long>, List<Version>>();
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
            IterationSpan span = new IterationSpan(it);

            addVersionsWhichWereMadeDuringIteration(span);
            columnsForIterations.add(span);
        }
    }




    private void addVersionsWhichWereMadeDuringIteration(IterationSpan ispan) {
        for (Entry<List<Long>, List<Version>> entry : versionsForStakeholderGroups.entrySet()) {
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
        // Can't do anything when this thing is empty
        if (columnsForIterations.isEmpty()) {
            return;
        }

        IterationSpan currentSpan = null, nextSpan = null;

        currentSpan = columnsForIterations.get(0);

        for (int i = 0; i < columnsForIterations.size(); i++) {
            if (i + 1 < columnsForIterations.size()) {
                nextSpan = columnsForIterations.get(i + 1);
            } else {
                nextSpan = null;
            }

            ChronologicalViewNode iterationNode = addIterationNode(currentSpan.it);

            if (currentSpan.getOrderedVersions().isEmpty() && nextSpan == null) {
                iterationNode.setEndPoint(true);
            } else if (currentSpan.getOrderedVersions().isEmpty()) {
                ChronologicalViewAssociation associationToNextIteration = new ChronologicalViewAssociation();
                associationToNextIteration.setSourceIteration(currentSpan.it);
                associationToNextIteration.setTargetIteration(nextSpan.it);
                visualization.addAssociation(associationToNextIteration);
            }



            for (List<Version> versionGroup : currentSpan.getOrderedVersions()) {
                Version previousVersion = null;
                for (int j = 0; j < versionGroup.size(); j++) {
                    Version currentVersion = versionGroup.get(j);

                    ChronologicalViewNode versionNode = addVersionNode(currentVersion);

                    ChronologicalViewAssociation association = new ChronologicalViewAssociation();

                    if (previousVersion != null) {
                        association.setSourceVersion(previousVersion);
                    } else {
                        association.setSourceIteration(currentSpan.it);
                    }

                    association.setTargetVersion(currentVersion);

                    visualization.addAssociation(association);

                    if (j + 1 == versionGroup.size() && nextSpan == null) {
                        versionNode.setEndPoint(true);
                    } else if (j + 1 == versionGroup.size() && nextSpan != null) {
                        ChronologicalViewAssociation associationToNextIteration = new ChronologicalViewAssociation();
                        associationToNextIteration.setSourceVersion(currentVersion);
                        associationToNextIteration.setTargetIteration(nextSpan.it);
                        visualization.addAssociation(associationToNextIteration);
                    }

                    previousVersion = currentVersion;
                }
            }




            // needs to stay at the end
            currentSpan = nextSpan;
        }
    }




    private ChronologicalViewNode addIterationNode(Iteration it) {
        ChronologicalViewNode node = new ChronologicalViewNode();
        node.setIteration(it);
        visualization.addNode(node);
        return node;
    }




    private ChronologicalViewNode addVersionNode(Version v) {
        ChronologicalViewNode node = new ChronologicalViewNode();
        node.setVersion(v);
        visualization.addNode(node);

        return node;
    }
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="getter">

    public Project getProject() {
        return project;
    }
    // </editor-fold>




    private static class IterationSpan implements Comparable<IterationSpan> {

        private Iteration it;

        private Map<List<Long>, List<Version>> versionsForStakeholderGroups;




        public IterationSpan(Iteration it) {
            this.it = it;
            this.versionsForStakeholderGroups = new HashMap<List<Long>, List<Version>>();
        }




        public void addVersion(List<Long> group, Version v) {
            List<Version> versions = versionsForStakeholderGroups.get(group);

            if (versions == null) {
                versions = new ArrayList<Version>();
                versionsForStakeholderGroups.put(group, versions);
            }

            versions.add(v);
        }




        public List<List<Version>> getOrderedVersions() {
            List<List<Version>> allVersionsInGroups = new ArrayList<List<Version>>(versionsForStakeholderGroups.size());

            for (Entry<List<Long>, List<Version>> entry : versionsForStakeholderGroups.entrySet()) {

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



