package eu.arima.schoolLibrary.bookStore;

import eu.arima.schoolLibrary.PostgresContainerBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BooksServiceTest extends PostgresContainerBaseTest {

    @Autowired
    private BooksService booksService;
    @Autowired
    private BooksRepository booksRepository;
    @Autowired
    private CopiesRepository copiesRepository;

    @Test
    @DisplayName("addCopy for a book that exists with the provided isbn adds a new copy to it")
    void addCopy_if_book_exist_adds_new_copy() {
        String isbn = "9780745168197";
        Book existingBook = booksRepository.findBookByIsbnEquals(isbn).orElseThrow();
        int existingNumCopies = existingBook.getCopies().size();

        Copy createdCopy = booksService.addCopy(existingBook.getTitle(), existingBook.getAuthors(), isbn);

        Book updatedBook = booksRepository.findById(existingBook.getId()).orElseThrow();
        assertAll(
                () -> assertEquals(existingNumCopies + 1, updatedBook.getCopies().size()),
                () -> assertRelationBetweenBookAndCopyIsCorrect(createdCopy, updatedBook));

    }

    @Test
    @DisplayName("addCopy for a book that doesn't exist with the provided isbn creates the book and adds the copy to it")
    void addCopy_if_book_not_exists_creates_book_and_adds_copy() {
        String title = "A murder is announced";
        String author = "Agatha Christie";
        String isbn = "9781602839038";

        assertFalse(booksRepository.findBookByIsbnEquals(isbn).isPresent());

        Copy createdCopy = booksService.addCopy(title, author, isbn);

        Book createdBook = booksRepository.findBookByIsbnEquals(isbn).orElseThrow();
        assertAll("The book has been succesfully created and has the new copy",
                () -> assertAll("The book info is correct",
                        () -> assertEquals(title, createdBook.getTitle()),
                        () -> assertEquals(isbn, createdBook.getIsbn()),
                        () -> assertEquals(author, createdBook.getAuthors())),
                () -> assertRelationBetweenBookAndCopyIsCorrect(createdCopy, createdBook)
        );

    }

    @Test
    @DisplayName("deleteCopy removes the copy from the related book")
    void deleteCopy_removes_copy_from_book() {
        String isbn = "9780745168197";
        Book existingBook = booksRepository.findBookByIsbnEquals(isbn).orElseThrow();
        int existingNumCopies = existingBook.getCopies().size();
        Copy copyToBeDeleted = existingBook.getCopies().iterator().next();

        booksService.deleteCopy(copyToBeDeleted);

        Book updatedBook = booksRepository.findById(existingBook.getId()).orElseThrow();
        assertAll(
                () -> assertFalse(copiesRepository.findById(copyToBeDeleted.getId()).isPresent()),
                () -> assertAll("The book has not the copy any more",
                        () -> assertEquals(existingNumCopies - 1, updatedBook.getCopies().size()),
                        () -> assertFalse(updatedBook.getCopies().contains(copyToBeDeleted))
                ));

    }

    private void assertRelationBetweenBookAndCopyIsCorrect(Copy createdCopy, Book updatedBook) {
        assertAll("The relation between book and the copy is correct",
                () -> assertEquals(updatedBook, createdCopy.getBook()),
                () -> assertTrue(updatedBook.getCopies().contains(createdCopy)));
    }

}