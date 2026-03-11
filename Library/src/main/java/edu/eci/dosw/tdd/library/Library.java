package edu.eci.dosw.tdd.library;

import edu.eci.dosw.tdd.library.book.Book;
import edu.eci.dosw.tdd.library.loan.Loan;
import edu.eci.dosw.tdd.library.loan.LoanStatus;
import edu.eci.dosw.tdd.library.user.User;
import java.time.LocalDateTime;
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

    public boolean addBook(Book book) {
        if (book == null || book.getIsbn() == null || book.getTittle() == null || book.getAuthor() == null) {
            return false;
        }
        books.put(book, books.getOrDefault(book, 0) + 1);
        return true;
    }

    public Loan loanABook(String userId, String isbn) {
        User user = users.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
        if (user == null) return null;

        Book book = books.keySet().stream().filter(b -> b.getIsbn().equals(isbn)).findFirst().orElse(null);
        if (book == null || books.get(book) <= 0) return null;

        boolean hasActiveLoan = loans.stream().anyMatch(l ->
                l.getUser().getId().equals(userId) &&
                        l.getBook().getIsbn().equals(isbn) &&
                        l.getStatus() == LoanStatus.ACTIVE
        );
        if (hasActiveLoan) return null;

        books.put(book, books.get(book) - 1);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDateTime.now());
        loan.setStatus(LoanStatus.ACTIVE);
        loans.add(loan);
        return loan;
    }

    public Loan returnLoan(Loan loan) {
        if (loan == null) return null;
        if (!loans.contains(loan)) return null;

        books.put(loan.getBook(), books.get(loan.getBook()) + 1);
        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDateTime.now());
        return loan;
    }

    public boolean addUser(User user) {
        return users.add(user);
    }
}