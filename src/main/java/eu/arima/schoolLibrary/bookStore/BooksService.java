package eu.arima.schoolLibrary.bookStore;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BooksService {
    private final CopiesRepository copiesRepository;
    private final BooksRepository booksRepository;

    public BooksService(CopiesRepository copiesRepository, BooksRepository booksRepository) {
        this.copiesRepository = copiesRepository;
        this.booksRepository = booksRepository;
    }

    @Transactional
    public Copy addCopy(String title, String authors, String isbn) {
        Optional<Book> book = booksRepository.findBookByIsbnEquals(isbn);
        Book bookToAddCopyTo = book.isPresent() ? book.orElseThrow() : new Book(title, authors, isbn);
        Copy newCopy = new Copy();
        newCopy.setBook(bookToAddCopyTo);
        return copiesRepository.save(newCopy);
    }

    @Transactional
    public void deleteCopy(Copy copy) {
        Book bookToRemoveCopyFrom = booksRepository.findBookByCopiesContains(copy).orElseThrow();
        bookToRemoveCopyFrom.removeCopy(copy);
        copiesRepository.delete(copy);
    }
}
