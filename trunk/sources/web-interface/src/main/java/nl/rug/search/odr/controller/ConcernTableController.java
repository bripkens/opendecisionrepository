package nl.rug.search.odr.controller;

import com.icesoft.faces.component.ext.RowSelectorEvent;
import java.awt.ItemSelectable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;

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
    private List<Item> subItems;
    private List<Item> list;




    @PostConstruct
    public void getConcernsFromDb() {
        items = new ArrayList<Item>();
        subItems = new ArrayList<Item>();
        List<Concern> con = new ArrayList<Concern>();
        con = concernLocal.getAll();

        for (Concern concern : con) {
            Item item = new Item();
            item.setConcer(concern);
            items.add(item);
        }
        sortColumn = "Id";
        ascending = true;
    }




    public List<Item> getAllConcerns() {
        Set set = new HashSet();
        list = new ArrayList<Item>();
        System.out.println("###########ALL ITEMS");
        for(Item ite : items){
            System.out.println(ite.concer.getName());
        }


        for (int i = 0; i < items.size(); i++) {
            if (!set.contains(items.get(i).concer.getGroup())) {
                System.out.println(items.get(i).getConcer().getName() + "noch nicht in der liste");
                list.add(items.get(i));
                set.add(items.get(i).concer.getGroup());
            } else {
                System.out.println(items.get(i).getConcer().getName() + "bereits in der liste");
                set.add(items.get(i).concer.getGroup());
            }

            if(items.get(i).isSelected()){
                System.out.println("hole mir subconcerns");
                getSubConcern(items.get(i));
            }
        }
        return list;
    }




    public void getSubConcern(Item item) {
        for (Item it : items) {
            if (it.getConcer().getGroup().equals(item.getConcer().getGroup())) {
                System.out.println("hat subConcerns");
                list.add(it);
            }
        }
    }




    public void setConcernList(ArrayList<Item> items) {
        this.items = items;
    }




    public String getSortColumn() {
        return sortColumn;
    }




    public void setSortColumn(String sortColumn) {
        System.out.println("sortColumn ist" + sortColumn);
        this.sortColumn = sortColumn;
    }




    public boolean isAscending() {
        return ascending;
    }




    public void setAscending(boolean ascending) {
        System.out.println("isAscending ist" + ascending);
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

        Collections.sort(items, comparator);
    }

    public static class Item {

        private boolean selected;
        private Concern concer;




        public boolean isSelected() {
            return selected;
        }




        public void setSelected(boolean selected) {
            if (this.selected == true) {
                this.selected = false;
            } else {
                this.selected = selected;
                System.out.println(concer.getName() + " ist " + this.selected);
            }
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
