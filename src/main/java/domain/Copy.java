package domain;

import java.util.ArrayList;
import java.util.List;

public class Copy {

    public enum Condition {
        NEW("General.condition.new", "cond_new.gif"), //
        GOOD("General.condition.good", "cond_good.gif"), //
        DAMAGED("General.condition.damaged", "cond_damaged.gif"), //
        WASTE("General.condition.waste", "cond_waste.gif"), //
        LOST("General.condition.lost", "cond_lost.gif");

        private String key;
        private final String icon;

        Condition(String key, String icon) {
            this.key = key;
            this.icon = icon;
        }

        public String getKey() {
            return key;
        }

        public String getIcon() {
            return icon;
        }
    }

    public static long nextInventoryNumber = 1;

    private final Long inventoryNumber;
    private Book book;
    private Condition condition;

    public Copy() {
        this.book = null;
        inventoryNumber = nextInventoryNumber++;
        condition = Condition.NEW;
    }

    public Copy(Book title) {
        this.book = title;
        inventoryNumber = nextInventoryNumber++;
        condition = Condition.NEW;
    }

    public Copy(Copy copy) {
        this.inventoryNumber = copy.inventoryNumber;
        this.book = copy.book;
        this.condition = copy.condition;
    }

    public Book getTitle() {
        return book;
    }

    public void setTitle(Book book) {
        this.book = book;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public long getInventoryNumber() {
        return inventoryNumber;
    }

    @Override
    public String toString() {

        return "#" + inventoryNumber + " (" + condition + ")";
    }

    @Override
    public boolean equals(Object obj) {
        Copy other = (Copy) obj;
        return inventoryNumber.equals(other.inventoryNumber);
    }

    public static List<Copy> cloneCopies(List<Copy> copies) {
        List<Copy> result = new ArrayList<Copy>();
        for (Copy copy : copies) {
            result.add(new Copy(copy));
        }
        return result;

    }

    public void updateFrom(Copy copy) {
        condition = copy.condition;
    }

}
