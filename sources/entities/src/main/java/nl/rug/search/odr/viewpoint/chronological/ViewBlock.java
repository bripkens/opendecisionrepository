package nl.rug.search.odr.viewpoint.chronological;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Version;

class ViewBlock implements Comparable<ViewBlock> {

    private Iteration iteration;

    private Map<List<Long>, List<Version>> versionsForStakeholderGroups;

    private boolean ordered;




    public ViewBlock(Iteration it) {
        this.iteration = it;
        this.versionsForStakeholderGroups = new HashMap<List<Long>, List<Version>>();
        ordered = true;
    }




    public void addVersion(List<Long> group, Version v) {
        List<Version> versions = versionsForStakeholderGroups.get(group);
        if (versions == null) {
            versions = new ArrayList<Version>();
            versionsForStakeholderGroups.put(group, versions);
        }
        versions.add(v);
        ordered = false;
    }




    public List<List<Version>> getVersions() {
        List<List<Version>> allVersionsInGroups = new ArrayList<List<Version>>(versionsForStakeholderGroups.size());
        for (Entry<List<Long>, List<Version>> entry : versionsForStakeholderGroups.entrySet()) {
            if (!ordered) {
                Collections.sort(entry.getValue(), new Version.DecidedWhenComparator());
            }
            allVersionsInGroups.add(entry.getValue());
        }

        ordered = true;

        return allVersionsInGroups;
    }




    public Iteration getIteration() {
        return iteration;
    }




    @Override
    public int compareTo(ViewBlock o) {
        return iteration.getStartDate().compareTo(o.iteration.getStartDate());
    }




}



