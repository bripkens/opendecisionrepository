package nl.rug.search.odr.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.project.ConcernLocal;

/**
 *
 * @author Stefan
 */
@ManagedBean
@ViewScoped
public class ConcernTableController {

    @EJB
    private ConcernLocal concernLocal;

    private List<Item> items;

    private String sortColumn;

    private boolean ascending;

    private List<Item> list;

    private int page;

    private int listSize;

    private Map<Integer, Integer> pagesSize;

    int temp;




    @PostConstruct
    public void getConcernsFromDb() {
        items = new ArrayList<Item>();
        List<Concern> con = new ArrayList<Concern>();
        con = concernLocal.getAll();

        for (Concern concern : con) {
            Item item = new Item();
            item.setConcer(concern);
            item.setSubConcern(false);
            items.add(item);
        }
        sortColumn = "Id";
        ascending = true;

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().
                getRequest();

        if (request.getParameter("page") != null) {
            page = Integer.valueOf(request.getParameter("page"));
        }

    }




    public boolean isValid() {
        return true;
    }




    public List<Item> getAllConcerns() {
        int k = 1;
        int r = 0;
        Set set = new HashSet();
        pagesSize = new HashMap<Integer, Integer>();
        list = new ArrayList<Item>();
        temp = 0;
        for (int i = 0; i < items.size(); i++) {
            if (!set.contains(items.get(i).concer.getGroup())) {
                if (list.size() % 2 == 0) {
                    items.get(i).colored = true;
                }
                list.add(items.get(i));
                set.add(items.get(i).concer.getGroup());
                temp++;
            } else {
                set.add(items.get(i).concer.getGroup());
                temp++;
            }

            List<Item> allsubs = getSubConcern(items.get(i));


            if (items.get(i).isSelected()) {
                list.addAll(allsubs);
                temp += allsubs.size();
            }
            if (allsubs.size() > 0) {
                items.get(i).setHasSub(true);

            }

            if (i % 10 != 0 && i != 0) {
                pagesSize.put(k, temp);
            } else {
                if (i != 0) {
                    k++;
                    temp = 0;
                }

            }
        }
        sort();
        listSize = 0;
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRowNumber(i + 1);
            if (list.get(i).isSelected()) {
                if (list.get(i).isEven()) {
                    for (int o = 0; o < list.size() - i; o++) {
                        if (list.get(o + i).concer.getGroup().equals(list.get(i).concer.getGroup())) {
                            list.get(o + i).setWhite(true);
                            listSize++;
                        }

                    }
                } else {
                    for (int j = 0; j < list.size() - i; j++) {
                        if (list.get(j + i).concer.getGroup().equals(list.get(i).concer.getGroup())) {
                            list.get(j + i).setWhite(false);
                            listSize++;
                        }
                    }
                }
            }
        }
        for (Map.Entry entry : pagesSize.entrySet()) {
            System.out.println("#######");
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            System.out.println("#######");
        }

        return list;
    }




    public int getListSize() {

        return pagesSize.get(page);

    }




    public List<Item> getSubConcern(Item item) {
        List<Item> temp = new ArrayList<Item>();
        for (Item it : items) {
            if (it.getConcer().getGroup().equals(item.getConcer().getGroup())) {
                if (!list.contains(it)) {
                    it.setSubConcern(true);
                    if (list.size() % 2 == 0) {
                        it.colored = true;
                    }
                    temp.add(it);
                }
            }
        }
        return temp;
    }




    public void setConcernList(ArrayList<Item> items) {
        this.items = items;
    }




    public String getSortColumn() {
        return sortColumn;
    }




    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }




    public boolean isAscending() {
        return ascending;
    }




    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }




    public void sort() {
        Comparator<Item> comparator;
        if (sortColumn.equals("Id")) {
            comparator = new ConcernTableController.Item.NameComparator();
        } else /*if (sortColumn.equals("Name"))*/ {
            comparator = new Item.NameComparator();
        }

        if (!ascending) {
            comparator = Collections.reverseOrder(comparator);
        }

        Collections.sort(list, comparator);
    }




    public static class Item {

        private boolean selected;

        private List<Item> subItems = new ArrayList<Item>();

        private Concern concer;

        private boolean subConcern = false;

        private boolean colored = false;

        private int rowNumber;

        private boolean white;

        private boolean hasSub = false;




        public boolean isHasSub() {
            return hasSub;
        }




        public void setHasSub(boolean hasSub) {
            this.hasSub = hasSub;
        }




        public List<Item> getSubItems() {
            return subItems;
        }




        public void setSubItems(List<Item> subItems) {
            this.subItems = subItems;
        }




        public void addSubItem(Item item) {
            subItems.add(item);
        }




        public boolean isWhite() {
            return white;
        }




        public void setWhite(boolean white) {
            this.white = white;
        }




        public int getRowNumber() {
            return rowNumber;
        }




        public boolean isEven() {
            return rowNumber % 2 == 0;
        }




        public void setRowNumber(int rowNumber) {
            this.rowNumber = rowNumber;
        }




        public boolean isSelected() {
            return selected;
        }




        public void setSelected(boolean selected) {
            if (this.selected == true) {
                this.selected = false;
            } else {
                this.selected = selected;
            }
        }




        public boolean isColored() {
            return colored;
        }




        public void setColored(boolean colored) {
            this.colored = colored;
        }




        public boolean isSubConcern() {
            return subConcern;
        }




        public void setSubConcern(boolean subConcern) {
            this.subConcern = subConcern;
        }




        public Concern getConcer() {
            return concer;
        }




        public void setConcer(Concern concer) {
            this.concer = concer;
        }




        public static class NameComparator implements Comparator<Item> {

            @Override
            public int compare(Item o1, Item o2) {
                return o1.concer.getName().compareToIgnoreCase(o2.concer.getName());
            }




        }




        public static class ExternalIdComparator implements Comparator<Item> {

            @Override
            public int compare(Item o1, Item o2) {
                return o1.concer.getExternalId().compareTo(o2.concer.getExternalId());
            }




        }




    }




}


