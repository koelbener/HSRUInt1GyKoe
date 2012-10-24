package application.core;

import domain.Book;

public class BookChangeEvent {

    private final Book book;

    public BookChangeEvent(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

}
