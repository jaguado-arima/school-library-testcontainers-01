package eu.arima.schoolLibrary.bookStore;

import javax.persistence.*;
import java.util.*;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    //String with authors separated with ,
    private String authors;
    @Column(unique = true)
    private String isbn;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Copy> copies = new HashSet<>();

    public Book(String title, String authors, String isbn) {
        this.title = title;
        this.authors = authors;
        this.isbn = isbn;
    }

    public Book() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Set<Copy> getCopies() {
        return copies;
    }

    public void addCopy (Copy copy){
        if (copy.getBook() == null){
            copy.setBook(this);
        }
        this.copies.add(copy);
    }

    public void removeCopy(Copy copy){
        this.getCopies().remove(copy);
    }


}
