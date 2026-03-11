package edu.eci.dosw.tdd.library;
import edu.eci.dosw.tdd.library.loan.LoanStatus;
import java.time.LocalDateTime;

import edu.eci.dosw.tdd.library.book.Book;
import edu.eci.dosw.tdd.library.loan.Loan;
import edu.eci.dosw.tdd.library.user.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {
    private final List<User> users;
    private final Map<Book, Integer> books;
    private final List<Loan> loans;

    public Library() {
        users = new ArrayList<>();
        books = new HashMap<>();
        loans = new ArrayList<>();
    }

    /**
     * Agrega un libro al sistema.
     * - Si el libro es null, retorna false.
     * - Si el libro ya existe, incrementa su cantidad en 1.
     * - Si es nuevo, lo agrega con cantidad 1.
     * - Retorna true si la operación fue exitosa.
     */
    public boolean addBook(Book book) {
        if (book == null) return false;
        if (books.containsKey(book)) {
            books.put(book, books.get(book) + 1);
        } else {
            books.put(book, 1);
        }
        return true;
    }

    /**
     * Crea un préstamo para un usuario dado un ISBN.
     * Validaciones:
     * 1. El usuario debe existir en el sistema.
     * 2. El libro debe existir en el sistema.
     * 3. Debe haber al menos 1 ejemplar disponible.
     * 4. El usuario no puede tener un préstamo ACTIVE del mismo libro.
     * Si todo es válido: descuenta 1 ejemplar y crea el préstamo con estado ACTIVE.
     */
    public Loan loanABook(String userId, String isbn) {
        // Buscar usuario
        User user = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst().orElse(null);
        if (user == null) return null;

        // Buscar libro
        Book book = books.keySet().stream()
                .filter(b -> b.getIsbn().equals(isbn))
                .findFirst().orElse(null);
        if (book == null) return null;

        // Verificar disponibilidad
        if (books.get(book) <= 0) return null;

        // Verificar que el usuario no tenga el mismo libro activo
        boolean hasActiveLoan = loans.stream()
                .anyMatch(l -> l.getUser().getId().equals(userId)
                        && l.getBook().getIsbn().equals(isbn)
                        && l.getStatus() == LoanStatus.ACTIVE);
        if (hasActiveLoan) return null;

        // Crear préstamo
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDateTime.now());
        loan.setStatus(LoanStatus.ACTIVE);
        books.put(book, books.get(book) - 1);
        loans.add(loan);
        return loan;
    }


    /**
     * Procesa la devolución de un préstamo.
     * Validaciones:
     * 1. El préstamo no puede ser null.
     * 2. El préstamo debe existir en la lista de préstamos.
     * Si es válido: cambia estado a RETURNED, registra fecha de devolución
     * y aumenta en 1 la cantidad del libro.
     */
    public Loan returnLoan(Loan loan) {
        if (loan == null) return null;
        if (!loans.contains(loan)) return null;
        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDateTime.now());
        books.put(loan.getBook(), books.get(loan.getBook()) + 1);
        return loan;
    }

    public boolean addUser(User user) {
        return users.add(user);
    }
}