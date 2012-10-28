package application.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.controller.BookMasterController;
import application.core.LibraryActionListener;
import application.core.Repository;
import application.presentationModel.BooksPMod;
import application.viewModel.SearchFilterComboBoxModel;
import application.viewModel.SearchFilterElement;
import domain.Library;

public class BookMasterMainView extends MainViewBase<Library, BookMasterController> {

    private static final Logger logger = LoggerFactory.getLogger(BookMasterMainView.class);

    public static final String SEARCH_DEFAULT_VALUE = "Suche...";

    public static final String NAME_BOOK_MASTER_MAIN_VIEW = "BookMasterMainView";
    public static final String NAME_BUTTON_OPEN = "button.open";
    public static final String NAME_TABLE_BOOKS = "table.books";
    public static final String NAME_LABEL_NUMBER_OF_BOOKS = "label.numberOfBooks";
    public static final String NAME_SEARCH_FIELD = "textField.search";
    public static final String NAME_COMBOBOX_FILTER = "comboBox.searchFilter";

    private static final long serialVersionUID = -5636590532882178863L;

    private JTextField txtSuche;
    private JLabel numberOfCopies;
    private JLabel numberOfBooks;
    private JButton btnOpenBook;
    private BooksPMod booksPMod;
    private JButton btnNewBook;
    private JTable booksTable;

    private JComboBox<SearchFilterElement> searchFilterComboBox;

    public BookMasterMainView() {
        super(null, NAME_BOOK_MASTER_MAIN_VIEW);
        setMinimumSize(new Dimension(616, 445));
    }

    @Override
    protected void initUIElements() {
        super.initUIElements();
        setTitle("Swinging Library");
        setBounds(100, 100, 616, 445);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        getContentPane().add(panel, BorderLayout.NORTH);

        JLabel lblSwingingLibrary = new JLabel("Swinging Library");
        panel.add(lblSwingingLibrary);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new CompoundBorder(new EmptyBorder(10, 5, 5, 5), new LineBorder(new Color(0, 0, 0), 1, true)));
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("B\u00FCcher", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_3 = new JPanel();
        panel_3.setBorder(new TitledBorder(null, "Inventar Statistiken", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_1.add(panel_3, BorderLayout.NORTH);
        panel_3.setLayout(new MigLayout("", "[][][fill][][]", "[]"));

        JLabel lblLasd = new JLabel("Anzahl B\u00FCcher:");
        panel_3.add(lblLasd, "cell 0 0");
        numberOfBooks = new JLabel(String.valueOf(booksPMod.getBookTableModel().getRowCount()));
        numberOfBooks.setName(NAME_LABEL_NUMBER_OF_BOOKS);
        panel_3.add(numberOfBooks, "cell 1 0");

        JLabel lblAnzahlExemplare = new JLabel("Anzahl Exemplare:");
        panel_3.add(lblAnzahlExemplare, "cell 3 0");

        numberOfCopies = new JLabel(String.valueOf(library.getCopies().size()));
        panel_3.add(numberOfCopies, "cell 4 0");

        JPanel panel_4 = new JPanel();
        panel_4.setBorder(new TitledBorder(null, "Buch-Inventar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_1.add(panel_4, BorderLayout.CENTER);
        panel_4.setLayout(new BorderLayout(0, 0));

        JPanel panel_5 = new JPanel();
        panel_4.add(panel_5, BorderLayout.NORTH);
        panel_5.setLayout(new MigLayout("", "[grow][][grow][]", "[][]"));

        JLabel lblNewLabel_1 = new JLabel("Alle B\u00FCcher stehen in der untenstehenden Tabelle");
        panel_5.add(lblNewLabel_1, "cell 0 0");

        txtSuche = new JTextField();
        txtSuche.setName(NAME_SEARCH_FIELD);
        txtSuche.setText(SEARCH_DEFAULT_VALUE);
        panel_5.add(txtSuche, "flowx,cell 0 1,growx");
        txtSuche.setColumns(10);

        JCheckBox checkBox = new JCheckBox("");
        panel_5.add(checkBox, "cell 0 1");

        JLabel lblNurVerfgbare = new JLabel("Nur Verf\u00FCgbare");
        panel_5.add(lblNurVerfgbare, "cell 1 1,alignx trailing");

        searchFilterComboBox = new JComboBox<SearchFilterElement>();
        searchFilterComboBox.setName(NAME_COMBOBOX_FILTER);
        searchFilterComboBox.setModel(new SearchFilterComboBoxModel());
        panel_5.add(searchFilterComboBox, "cell 2 1,growx");

        JPanel panel_6 = new JPanel();
        panel_4.add(panel_6, BorderLayout.CENTER);
        panel_6.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();

        panel_6.add(scrollPane, BorderLayout.CENTER);

        booksTable = new JTable(booksPMod.getBookTableModel());
        booksTable.setRowSorter(booksPMod.getBookTableRowSorter());
        booksTable.setName(NAME_TABLE_BOOKS);

        scrollPane.setViewportView(booksTable);

        JPanel panel_7 = new JPanel();
        panel_6.add(panel_7, BorderLayout.EAST);
        panel_7.setLayout(new MigLayout("", "[]", "[23px][]"));

        btnNewBook = new JButton("Neu");
        btnNewBook.setMnemonic('n');
        panel_7.add(btnNewBook, "cell 0 0,growx,aligny center");

        btnOpenBook = new JButton("\u00D6ffnen");
        btnOpenBook.setName(NAME_BUTTON_OPEN);
        btnOpenBook.setMnemonic('o');
        panel_7.add(btnOpenBook, "cell 0 1,growx,aligny center");
        btnOpenBook.setToolTipText("Alle selektierten Bücher öffnen");
        btnOpenBook.setEnabled(false);

        JPanel panel_2 = new JPanel();
        tabbedPane.addTab("Ausleihen", null, panel_2, null);

    }

    @Override
    protected void initModel() {
        super.initModel();
        booksPMod = Repository.getInstance().getBooksPMod();
    }

    @Override
    protected BookMasterController initController() {
        return new BookMasterController();
    }

    @Override
    protected void initListeners() {
        booksTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                int[] rows = booksTable.getSelectedRows();
                if (rows != null && rows.length > 0) {
                    btnOpenBook.setEnabled(true);
                } else {
                    btnOpenBook.setEnabled(false);
                }
            }
        });

        searchFilterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                search();
            }

            private void search() {
                if (!txtSuche.getText().equals(SEARCH_DEFAULT_VALUE)) {
                    getController().searchBooks(txtSuche.getText(), ((SearchFilterElement) searchFilterComboBox.getSelectedItem()).getBookTableModelColumn());
                }
            }
        });

        booksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    logger.trace("doubleClick detected");
                    getController().openBooks(new int[] { booksTable.rowAtPoint(e.getPoint()) });
                }
            }
        });

        btnOpenBook.addActionListener(new LibraryActionListener() {
            @Override
            protected void execute() {
                getController().openBooks(booksTable.getSelectedRows());
            }
        });
        btnNewBook.addActionListener(new LibraryActionListener() {

            @Override
            protected void execute() {
                getController().openNewBook();
            }
        });

        txtSuche.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

        });

        txtSuche.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                if (txtSuche.getText().equals(SEARCH_DEFAULT_VALUE)) {
                    txtSuche.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent arg0) {
                if (txtSuche.getText().equals("")) {
                    txtSuche.setText(SEARCH_DEFAULT_VALUE);
                }
            }
        });

    }

    private void search() {
        if (!txtSuche.getText().equals(SEARCH_DEFAULT_VALUE)) {
            getController().searchBooks(txtSuche.getText(), ((SearchFilterElement) searchFilterComboBox.getSelectedItem()).getBookTableModelColumn());
        }
    }
}
