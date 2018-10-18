package xyz.baoanj.api.domain;

import java.io.Serializable;

public class Book implements Serializable {
    private String bookId;
    private String bookName;
    private int bookPage;

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookPage(int bookPage) {
        this.bookPage = bookPage;
    }

    public int getBookPage() {
        return bookPage;
    }
}
