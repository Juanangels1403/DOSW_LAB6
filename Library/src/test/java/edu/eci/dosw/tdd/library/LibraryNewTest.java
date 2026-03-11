package edu.eci.dosw.tdd.library;

import edu.eci.dosw.tdd.library.book.Book;
import edu.eci.dosw.tdd.library.loan.Loan;
import edu.eci.dosw.tdd.library.loan.LoanStatus;
import edu.eci.dosw.tdd.library.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryNewTest {

    private Library library;

    @BeforeEach
    public void setUp() {
        library = new Library();
    }

    @Test
    public void shouldNotAddBookWithNullAuthor() {
        Book book = new Book("Clean Code", null, "ISBN-003");
        assertFalse(library.addBook(book));
    }

    @Test
    public void shouldLoanCorrectBook() {
        Book book = new Book("Clean Code", "Robert Martin", "ISBN-001");
        library.addBook(book);
        User user = new User();
        user.setId("U001");
        user.setName("Juan");
        library.addUser(user);
        Loan loan = library.loanABook("U001", "ISBN-001");
        assertEquals(book, loan.getBook());
    }

    @Test
    public void shouldHaveReturnedStatusAfterReturn() {
        Book book = new Book("Clean Code", "Robert Martin", "ISBN-001");
        library.addBook(book);
        User user = new User();
        user.setId("U001");
        user.setName("Juan");
        library.addUser(user);
        Loan loan = library.loanABook("U001", "ISBN-001");
        Loan returned = library.returnLoan(loan);
        assertEquals(LoanStatus.RETURNED, returned.getStatus());
    }
}
