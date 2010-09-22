package nl.rug.search.odr.controller;

import com.icesoft.faces.component.ext.RowSelectorEvent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class ProjectsController {

    //  List of sample inventory data.
    private InventoryItem[] carInventory = new InventoryItem[]{
        new InventoryItem(58285, "blAAAA", " a", 23452435, 3452345),
        new InventoryItem(58285, "blAAAA", " s", 23452435, 3452345),
        new InventoryItem(58285, "blAAAA", " d", 23452435, 3452345),
        new InventoryItem(58285, "blAAAA", " h", 23452435, 3452345),
        new InventoryItem(58285, "blAAAA", " g", 23452435, 3452345),
        new InventoryItem(58285, "blAAAA", " k", 23452435, 3452345),};

    public void rowSelectionListener(RowSelectorEvent event) {
        InventoryItem item;
        for (int i = 0, max = carInventory.length; i < max; i++) {
            item = (InventoryItem) carInventory[i];
                item.setSelected(false);
            }
        carInventory[event.getRow()].setSelected(true);

        for (int i = 0; i < carInventory.length; i++) {
            if (carInventory[i].isSelected()) {
//                System.out.println(carInventory[i].description);
            }
        }
    }

    /**
     * Gets the inventoryItem array of car data.
     * @return array of car inventory data.
     */
    public InventoryItem[] getCarInventory() {
        return carInventory;
    }

    /**
     * Inventory Item subclass stores data about a cars inventory data.  Properties
     * such a stock, model, description, odometer and price are stored.
     */
    public class InventoryItem {
        // slock number

        int stock;
        String model;
        String description;
        int odometer;
        int price;
        private boolean selected;

        public InventoryItem(int stock, String model, String description, int odometer, int price) {
            this.stock = stock;
            this.model = model;
            this.description = description;
            this.odometer = odometer;
            this.price = price;
        }

        public int getStock() {
            return stock;
        }

        public String getModel() {
            return model;
        }

        public String getDescription() {
            return description;
        }

        public int getOdometer() {
            return odometer;
        }

        public int getPrice() {
            return price;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
