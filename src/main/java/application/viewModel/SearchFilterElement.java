package application.viewModel;

public class SearchFilterElement {

    private String title;
    private int bookTableModelColumn;

    public SearchFilterElement(String title, int bookTableModelColumn) {
        super();
        this.title = title;
        this.bookTableModelColumn = bookTableModelColumn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBookTableModelColumn() {
        return bookTableModelColumn;
    }

    public void setBookTableModelColumn(int bookTableModelColumn) {
        this.bookTableModelColumn = bookTableModelColumn;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + bookTableModelColumn;
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
        if (bookTableModelColumn != other.bookTableModelColumn)
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

}
