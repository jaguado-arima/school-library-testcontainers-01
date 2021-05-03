package eu.arima.schoolLibrary.bookStore;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Copy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST})
    @JsonIgnore
    private Book book;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
        book.addCopy(this);
    }

    @Override
    public String toString() {
        return "Copy{" +
               "id=" + id +
               '}';
    }
}
