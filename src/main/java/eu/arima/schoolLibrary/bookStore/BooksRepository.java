package eu.arima.schoolLibrary.bookStore;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BooksRepository extends CrudRepository<Book, Long> {

     Optional<Book> findBookByIsbnEquals(String isbn);

     Optional<Book> findBookByCopiesContains(Copy copy);
}
