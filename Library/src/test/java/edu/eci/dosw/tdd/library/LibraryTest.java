package edu.eci.dosw.tdd.library;

import edu.eci.dosw.tdd.library.book.Book;
import edu.eci.dosw.tdd.library.loan.Loan;
import edu.eci.dosw.tdd.library.loan.LoanStatus;
import edu.eci.dosw.tdd.library.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {

    private Library library;

    @BeforeEach
    public void setUp() {
        library = new Library();
    }

    // ===== addBook =====
    // Si addBook devuelve false, el test FALLA
    @Test
    public void shouldAddNewBookSuccessfully() {
        Book book = new Book("Clean Code", "Robert Martin", "ISBN-001");
        assertTrue(library.addBook(book));
    }

    // Este test verifica que si el libro YA existe, aumenta la cantidad
    @Test
    public void shouldIncrementAmountWhenBookAlreadyExists() {
        Book book = new Book("Clean Code", "Robert Martin", "ISBN-001");
        library.addBook(book);
        assertTrue(library.addBook(book));
    }

    // No debería poder agregar un libro null verifica que el resultado no sea False
    @Test
    public void shouldNotAddNullBook() {
        assertFalse(library.addBook(null));
    }

    // ===== loanABook =====
    //verifica que dos valores sean IGUALES
    //El préstamo debe tener estado ACTIVE
    @Test
    public void shouldLoanBookSuccessfully() {
        Book book = new Book("Clean Code", "Robert Martin", "ISBN-001");
        library.addBook(book);
        User user = new User();
        user.setId("U001");
        user.setName("Juan");
        library.addUser(user);

        Loan loan = library.loanABook("U001", "ISBN-001");
        assertNotNull(loan);
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
    }


    //Verifica la existencia de stock
    @Test
    public void shouldNotLoanIfBookNotAvailable() {
        Book book = new Book("Clean Code", "Robert Martin", "ISBN-001");
        library.addBook(book);
        User user = new User();
        user.setId("U001");
        user.setName("Juan");
        library.addUser(user);
        library.loanABook("U001", "ISBN-001");

        assertNull(library.loanABook("U001", "ISBN-001"));
    }

    // Si el usuario no existe, no puede pedir prestado
    @Test
    public void shouldNotLoanIfUserNotExists() {
        Book book = new Book("Clean Code", "Robert Martin", "ISBN-001");
        library.addBook(book);
        assertNull(library.loanABook("U999", "ISBN-001"));
    }

    // El mismo usuario NO puede tener 2 préstamos ACTIVOS del mismo libro

    @Test
    public void shouldNotLoanSameBookTwiceToSameUser() {
        Book book = new Book("Clean Code", "Robert Martin", "ISBN-001");
        library.addBook(book);
        library.addBook(book);
        User user = new User();
        user.setId("U001");
        user.setName("Juan");
        library.addUser(user);
        library.loanABook("U001", "ISBN-001"); // Primer préstamo OK

        assertNull(library.loanABook("U001", "ISBN-001")); // Segundo debe fallar
    }

    // ===== returnLoan =====
    @Test
    public void shouldReturnLoanSuccessfully() {
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

    // No puedo devolver un préstamo que no existe
    @Test
    public void shouldNotReturnNullLoan() {
        assertNull(library.returnLoan(null));
    }
}