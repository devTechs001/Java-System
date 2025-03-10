package gui;

import models.Book;
import dbConnection.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryDashboard extends JFrame {

    private CardLayout cardLayout; // CardLayout for the main content area
    private JPanel mainPanel; // Main content area
    private JList<String> bookList; // List to display books
    private DefaultListModel<String> bookListModel; // Model for the book list
    private List<Book> allBooks; // List to store all books
    private int currentPage = 1; // Current page number
    private int itemsPerPage = 10; // Number of items per page
    private JLabel pageLabel; // Label to display the current page
    private JTextArea bookDetails; // Text area to display book details
    private JTextField titleField, authorField, genreField, yearField, isbnField; // Fields for adding/editing books
    private int selectedBookId; // ID of the selected book

    public LibraryDashboard() {
        super("Library Dashboard");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close application on exit

        // Create panels
        JPanel topPanel = createTopPanel();
        JPanel leftPanel = createLeftPanel();
        mainPanel = createMainPanel();
        JPanel bottomPanel = createBottomPanel();

        // Add panels to the frame
        this.setLayout(new BorderLayout());
        this.add(topPanel, BorderLayout.NORTH);
        this.add(leftPanel, BorderLayout.WEST);
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        // Make the frame visible
        this.setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(42, 73, 23));
        panel.setPreferredSize(new Dimension(600, 50));

        JLabel titleLabel = new JLabel("Welcome to the Library Dashboard");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel);

        return panel;
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(200, 600));
        panel.setBackground(new Color(34, 56, 67));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Vertical layout for buttons

        // Navigation buttons
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> showCard("Home"));
        panel.add(homeButton);

        JButton searchButton = new JButton("Search Books");
        searchButton.addActionListener(e -> showCard("Search"));
        panel.add(searchButton);

        JButton addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(e -> showCard("AddBook"));
        panel.add(addBookButton);

        JButton viewBooksButton = new JButton("View Books");
        viewBooksButton.addActionListener(e -> showCard("ViewBooks"));
        panel.add(viewBooksButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logging out...");
            dispose(); // Close the dashboard
        });
        panel.add(logoutButton);

        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        cardLayout = new CardLayout();
        panel.setLayout(cardLayout);
        panel.setBackground(new Color(42, 73, 23));

        // Home View
        JPanel homePanel = new JPanel();
        homePanel.setBackground(new Color(34, 67, 76));
        homePanel.add(new JLabel("Welcome to the Library!"));
        panel.add(homePanel, "Home");

        // Search View
        JPanel searchPanel = createSearchPanel();
        panel.add(searchPanel, "Search");

        // Add Book View
        JPanel addBookPanel = createAddBookPanel();
        panel.add(addBookPanel, "AddBook");

        // View Books Panel
        JPanel viewBooksPanel = createViewBooksPanel();
        panel.add(viewBooksPanel, "ViewBooks");

        // Edit Book Panel
        JPanel editBookPanel = createEditBookPanel();
        panel.add(editBookPanel, "EditBook");

        // Book Details Panel
        JPanel bookDetailsPanel = createBookDetailsPanel();
        panel.add(bookDetailsPanel, "BookDetails");

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBackground(new Color(42, 73, 23));

        // Search bar
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setBackground(new Color(42, 73, 23));
        JTextField searchField = new JTextField(30);
        JButton searchButton = new JButton("Search");
        searchBarPanel.add(searchField);
        searchBarPanel.add(searchButton);
        panel.add(searchBarPanel, BorderLayout.NORTH);

        // Book list
        bookListModel = new DefaultListModel<>();
        bookList = new JList<>(bookListModel);
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane bookListScrollPane = new JScrollPane(bookList);
        panel.add(bookListScrollPane, BorderLayout.CENTER);

        // Book details
        bookDetails = new JTextArea();
        bookDetails.setEditable(false);
        JScrollPane bookDetailsScrollPane = new JScrollPane(bookDetails);
        panel.add(bookDetailsScrollPane, BorderLayout.EAST);

        // Add action listener to book list
        bookList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedBook = bookList.getSelectedValue();
                displayBookDetails(selectedBook);
            }
        });

        // Add action listener to search button
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            filterBookList(query);
        });

        return panel;
    }

    private JPanel createAddBookPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 10, 10, 10));
        panel.setBackground(new Color(42, 73, 23));

        // Form fields for adding a book
        panel.add(new JLabel("Title:"));
        titleField = new JTextField();
        panel.add(titleField);

        panel.add(new JLabel("Author ID:")); // Changed to Author ID
        authorField = new JTextField();
        panel.add(authorField);

        panel.add(new JLabel("Genre:"));
        genreField = new JTextField();
        panel.add(genreField);

        panel.add(new JLabel("Year:"));
        yearField = new JTextField();
        panel.add(yearField);

        panel.add(new JLabel("ISBN:"));
        isbnField = new JTextField();
        panel.add(isbnField);

        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(e -> {
            // Logic to add the book to the database
            addBook(new Book(
                    0, // ID will be auto-generated
                    titleField.getText(),
                    authorField.getText(), // Use author name
                    genreField.getText(),
                    Integer.parseInt(yearField.getText()),
                    isbnField.getText(),
                    "", // Publisher (optional)
                    1, // Quantity
                    1  // Available
            ));
            JOptionPane.showMessageDialog(this, "Book added successfully!");
            clearAddBookFields();
        });
        panel.add(addButton);

        return panel;
    }

    private JPanel createViewBooksPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(42, 73, 23));

        // Book list
        bookListModel = new DefaultListModel<>();
        bookList = new JList<>(bookListModel);
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane bookListScrollPane = new JScrollPane(bookList);
        panel.add(bookListScrollPane, BorderLayout.CENTER);

        // Pagination controls
        JPanel paginationPanel = new JPanel();
        JButton prevButton = new JButton("Previous");
        prevButton.addActionListener(e -> changePage(-1));
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> changePage(1));
        pageLabel = new JLabel("Page 1");
        paginationPanel.add(prevButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextButton);
        panel.add(paginationPanel, BorderLayout.SOUTH);

        // Edit button
        JButton editButton = new JButton("Edit Book");
        editButton.addActionListener(e -> {
            String selectedBook = bookList.getSelectedValue();
            if (selectedBook != null) {
                showEditBookPanel(selectedBook);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to edit.");
            }
        });
        panel.add(editButton, BorderLayout.NORTH);

        // Delete button
        JButton deleteButton = new JButton("Delete Book");
        deleteButton.addActionListener(e -> {
            String selectedBook = bookList.getSelectedValue();
            if (selectedBook != null) {
                deleteBook(selectedBook);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            }
        });
        panel.add (deleteButton, BorderLayout.NORTH);

        // Initialize book list
        initializeBookList();

        return panel;
    }

    private JPanel createEditBookPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        panel.setBackground(new Color(42, 73, 23));

        // Form fields for editing a book
        panel.add(new JLabel("Title:"));
        titleField = new JTextField();
        panel.add(titleField);

        panel.add(new JLabel("Author ID:")); // Changed to Author ID
        authorField = new JTextField();
        panel.add(authorField);

        panel.add(new JLabel("Genre:"));
        genreField = new JTextField();
        panel.add(genreField);

        panel.add(new JLabel("Year:"));
        yearField = new JTextField();
        panel.add(yearField);

        panel.add(new JLabel("ISBN:"));
        isbnField = new JTextField();
        panel.add(isbnField);

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            // Logic to update the book in the database
            updateBook(new Book(
                    selectedBookId, // ID of the book being edited
                    titleField.getText(),
                    authorField.getText(), // Use author name
                    genreField.getText(),
                    Integer.parseInt(yearField.getText()),
                    isbnField.getText(),
                    "", // Publisher (optional)
                    1, // Quantity
                    1  // Available
            ));
            JOptionPane.showMessageDialog(this, "Book details updated successfully!");
            showCard("ViewBooks");
            initializeBookList(); // Refresh the book list
        });
        panel.add(saveButton);

        return panel;
    }

    private JPanel createBookDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(42, 73, 23));

        bookDetails = new JTextArea();
        bookDetails.setEditable(false);
        JScrollPane bookDetailsScrollPane = new JScrollPane(bookDetails);
        panel.add(bookDetailsScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void showEditBookPanel(String title) {
        Book book = getBookDetails(title);
        if (book != null) {
            selectedBookId = book.getId();
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor()); // Set author name
            genreField.setText(book.getGenre());
            yearField.setText(String.valueOf(book.getYear()));
            isbnField.setText(book.getIsbn());
            showCard("EditBook");
        } else {
            JOptionPane.showMessageDialog(this, "No details available for the selected book.");
        }
    }

    private void deleteBook(String title) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM Books WHERE title = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully!");
                initializeBookList(); // Refresh the book list
            } else {
                JOptionPane.showMessageDialog(this, "Book not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while deleting the book.");
        }
    }

    private void initializeBookList() {
        allBooks = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Books";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"), // Use author name
                        rs.getString("genre"),
                        rs.getInt("publication_year"),
                        rs.getString("isbn"),
                        rs.getString("publisher"),
                        rs.getInt("available_copies"),
                        rs.getInt("available_copies") // Assuming available copies is the same as quantity
                );
                allBooks.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateBookList();
    }

    private void updateBookList() {
        bookListModel.clear();
        int start = (currentPage - 1) * itemsPerPage;
        int end = Math.min(start + itemsPerPage, allBooks.size());
        for (int i = start; i < end; i++) {
            bookListModel.addElement(allBooks.get(i).getTitle());
        }
        pageLabel.setText("Page " + currentPage);
    }

    private void filterBookList(String query) {
        bookListModel.clear();
        for (Book book : allBooks) {
            if ( book.getTitle().toLowerCase().contains(query) || book.getAuthor().toLowerCase().contains(query)) {
                bookListModel.addElement(book.getTitle());
            }
        }
    }

    private void changePage(int delta) {
        int newPage = currentPage + delta;
        int totalPages = (int) Math.ceil((double) allBooks.size() / itemsPerPage);
        if (newPage > 0 && newPage <= totalPages) {
            currentPage = newPage;
            updateBookList();
        }
    }

    private void displayBookDetails(String title) {
        Book book = getBookDetails(title);
        if (book != null) {
            bookDetails.setText(
                    "Title: " + book.getTitle() + "\n" +
                            "Author: " + book.getAuthor() + "\n" +
                            "Genre: " + book.getGenre() + "\n" +
                            "Year: " + book.getYear() + "\n" +
                            "ISBN: " + book.getIsbn() + "\n" +
                            "Publisher: " + book.getPublisher() + "\n" +
                            "Quantity: " + book.getQuantity() + "\n" +
                            "Available: " + book.getAvailable()
            );
        } else {
            bookDetails.setText("No details available for the selected book.");
        }
    }

    private Book getBookDetails(String title) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Books WHERE title = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"), // Use author name
                        rs.getString("genre"),
                        rs.getInt("publication_year"),
                        rs.getString("isbn"),
                        rs.getString("publisher"),
                        rs.getInt("available_copies"),
                        rs.getInt("available_copies") // Assuming available copies is the same as quantity
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addBook(Book book) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Books (title, author, publication_year, genre, available_copies) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor()); // Use author name
            pstmt.setInt(3, book.getYear());
            pstmt.setString(4, book.getGenre());
            pstmt.setInt(5, book.getQuantity());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateBook(Book book) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE Books SET title = ?, author = ?, publication_year = ?, genre = ?, available_copies = ? WHERE book_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor()); // Use author name
            pstmt.setInt(3, book.getYear());
            pstmt.setString(4, book.getGenre());
            pstmt.setInt(5, book.getQuantity());
            pstmt.setInt(6, book.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(600, 50));
        panel.setBackground(new Color(26, 12, 53));

        JButton exportButton = new JButton("Export to CSV");
        exportButton.addActionListener(e -> exportBooksToCSV());
        panel.add(exportButton);

        JButton importButton = new JButton("Import from CSV");
        importButton.addActionListener(e -> importBooksFromCSV());
        panel.add(importButton);

        return panel;
    }

    private void clearAddBookFields() {
        titleField.setText("");
        authorField.setText("");
        genreField.setText("");
        yearField.setText("");
        isbnField.setText("");
    }

    private void showCard(String cardName) {
        cardLayout.show(mainPanel, cardName); // Show the specified card
    }

    private void exportBooksToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.csv"))) {
            writer.write("Title,Author,Genre,Year,ISBN,Publisher,Quantity,Available\n");
            for (Book book : allBooks) {
                writer.write(String.format("%s,%s,%s,%d,%s,% %s,%d,%d\n",
                        book.getTitle(), book.getAuthor(), book.getGenre(),
                        book.getYear(), book.getIsbn(), book.getPublisher(),
                        book.getQuantity(), book.getAvailable()));
            }
            JOptionPane.showMessageDialog(this, "Books exported successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error exporting books.");
        }
    }

    private void importBooksFromCSV() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                reader.readLine(); // Skip header
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 8) {
                        addBook(new Book(
                                0, // ID will be auto-generated
                                data[0], // Title
                                data[1], // Author
                                data[2], // Genre
                                Integer.parseInt(data[3]), // Year
                                data[4], // ISBN
                                data[5], // Publisher
                                Integer.parseInt(data[6]), // Quantity
                                Integer.parseInt(data[7])  // Available
                        ));
                    }
                }
                JOptionPane.showMessageDialog(this, "Books imported successfully!");
                initializeBookList(); // Refresh the book list
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error importing books.");
            }
        }
    }
}
