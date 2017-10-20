package com.bookscrud;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(name = "searchForAllFields",
                query = "select b from Book b " +
                        "where b.id = :id and b.title = :title and b.description = :description " +
                        "and b.author = :author and b.isbn = :isbn and b.printYear = :printYear"),

        @NamedQuery(name = "searchByAuthor",
                query = "select b from Book b " +
                        "where b.author = :author"),

        @NamedQuery(name = "searchByTitle",
                query = "select b from Book b " +
                        "where b.title = :title"),

        @NamedQuery(name = "searchByYear",
                query = "select b from Book b " +
                        "where b.printYear = :printYear"),

        @NamedQuery(name = "searchById",
                query = "select b from Book b where b.id = :id"),

        @NamedQuery(name = "searchByISBN",
                query = "select b from Book b where b.isbn = :isbn"),


        @NamedQuery(name = "searchByReadAlready",
                query = "select b from Book b where b.readAlready= :readAlready")

})

@Table(name = "book")
public class Book implements Serializable {
    private long id;
    private String title;
    private String description;
    private String author;
    private String isbn;
    private int printYear;
    private boolean readAlready = false;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NotEmpty(message = "{validation.title.NotEmpty.message}")
    @Size(min = 3, max = 100, message = "{validation.title.Size.message}")
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Size(max = 255, message = "{validation.description.Size.message}")
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Pattern(regexp = "([A-Z][a-z]+)(\\x20+[A-Z][a-z]+)*")
    @NotEmpty(message = "{validation.author.NotEmpty.message}")
    @Size(min = 3, max = 100, message = "{validation.author.Size.message}")
    @Column(name = "author", updatable = false)
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Pattern(regexp = "[1-9][0-9]{19}")
    @NotEmpty(message = "{validation.isbn.NotEmpty.message}")
    @Size(min = 20, message = "{validation.isbn.Size.message}")
    @Column(name = "isbn")
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Min(1142)
    @Max(2017)
    @NotNull(message = "{validation.year.NotEmpty.message}")
    //  @Size(min = 4, message = "{validation.year.Size.message}")
    @Column(name = "printyear")
    public int getPrintYear() {
        return printYear;
    }

    public void setPrintYear(int printYear) {
        this.printYear = printYear;
    }

    @Column(name = "readalready")
    public boolean isReadAlready() {
        return readAlready;
    }

    public void setReadAlready(boolean readAlready) {
        this.readAlready = readAlready;
    }

    @Override
    public String toString() {
        return "id: " + id + " |title: " + title + " |author: " + author + " | year: " + printYear + " |read: " + readAlready;
    }
}
