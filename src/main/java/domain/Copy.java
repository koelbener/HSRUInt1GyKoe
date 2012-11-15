package domain;

public class Copy {

    public enum Condition {
        NEW, GOOD, DAMAGED, WASTE, LOST
    }

    public static long nextInventoryNumber = 1;

    private final long inventoryNumber;
    private Book book;
    private Condition condition;

    public Copy(Book title) {
        this.book = title;
        inventoryNumber = nextInventoryNumber++;
        condition = Condition.NEW;
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

}
