package application.presentationModel.componentModel;

public class SearchFilterElement {

    private String title;
    private int tableModelColumn;

    public SearchFilterElement(String title, int tableModelColumn) {
        super();
        this.title = title;
        this.tableModelColumn = tableModelColumn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTableModelColumn() {
        return tableModelColumn;
    }

    public void setTableModelColumn(int tableModelColumn) {
        this.tableModelColumn = tableModelColumn;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tableModelColumn;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SearchFilterElement other = (SearchFilterElement) obj;
        if (tableModelColumn != other.tableModelColumn)
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

}
