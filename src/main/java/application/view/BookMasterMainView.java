package application.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultComboBoxModel;
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
import application.core.Language;
import application.core.LibraryActionListener;
import application.core.Repository;
import application.core.Texts;
import application.presentationModel.BooksPMod;
import application.viewModel.SearchFilterElement;
import domain.Library;

public class BookMasterMainView extends MainViewBase<Library, BookMasterController> {

    private static final Logger logger = LoggerFactory.getLogger(BookMasterMainView.class);

    public static String searchDefaultText;

    public static final String NAME_BUTTON_OPEN = "button.open";
    public static final String NAME_BUTTON_NEW = "button.new";
    public static final String NAME_TABLE_BOOKS = "table.books";
    public static final String NAME_LABEL_NUMBER_OF_BOOKS = "label.numberOfBooks";
    public static final String NAME_SEARCH_FIELD = "textField.search";
    public static final String NAME_COMBOBOX_FILTER = "comboBox.searchFilter";

    private static final long serialVersionUID = -5636590532882178863L;

    private JTextField txtFieldSearch;
    private JLabel numberOfCopies;
    private JLabel numberOfBooks;
    private JButton btnOpenBook;
    private BooksPMod booksPMod;
    private JButton btnNewBook;
    private JTable booksTable;

    private JComboBox<SearchFilterElement> searchFilterComboBox;
    private JComboBox<Language> languageComboBox;
    private JPanel panel;
    private JLabel lblAnzahlExemplare;
    private JLabel lblLasd;
    private JPanel panel_4;
    private JLabel lblNewLabel_1;
    private JLabel lblNurVerfgbare;
    private JTabbedPane tabbedPane;
    private JLabel lblSwingingLibrary;
    private JPanel panel_3;

    public BookMasterMainView() {
        super(null);
        setMinimumSize(new Dimension(616, 445));
    }

    @Override
    protected void setTexts() {
        // title
        setTitle(Texts.get("BookMasterMainView.this.title"));
        // panel titles
        panel.setBorder(new TitledBorder(null, Texts.get("BookMasterMainView.panel_3.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_4.setBorder(new TitledBorder(null, Texts.get("BookMasterMainView.panel_4.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

        // components
        lblAnzahlExemplare.setText(Texts.get("BookMasterMainView.lblAnzahlExemplare.text"));
        lblLasd.setText(Texts.get("BookMasterMainView.lblLasd.text"));
        btnNewBook.setText(Texts.get("BookMasterMainView.btnNewBook.text"));
        lblNewLabel_1.setText(Texts.get("BookMasterMainView.lblNewLabel_1.text"));
        btnOpenBook.setText(Texts.get("BookMasterMainView.btnOpenBook.text"));
        searchDefaultText = Texts.get("BookMasterMainView.searchDefault");
        lblNurVerfgbare.setText(Texts.get("BookMasterMainView.lblNurVerfgbare.text"));
        lblSwingingLibrary.setText(Texts.get("BookMasterMainView.lblSwingingLibrary.text"));

        // Tooltips
        btnOpenBook.setToolTipText(Texts.get("BookMasterMainView.btnOpenBook.toolTipText"));

        // Tabs
        tabbedPane.setTitleAt(0, Texts.get("BookMasterMainView.tab.books"));
        tabbedPane.setTitleAt(1, Texts.get("BookMasterMainView.tab.lending"));

        // table
        booksPMod.getBookTableModel().setColumns();

        // filter comboBox
        booksPMod.getFilterComboBoxModel().updateTexts();

        revalidate();
    }

    @Override
    protected void initUIElements() {
        super.initUIElements();
        setBounds(100, 100, 616, 445);
        getContentPane().setLayout(new BorderLayout(0, 0));

        panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new MigLayout("", "[55px][28px][][][][][grow][][][][][][][][][][][][]", "[20px,grow]"));

        lblSwingingLibrary = new JLabel();
        panel.add(lblSwingingLibrary, "cell 0 0,alignx left,aligny center");

        panel_3 = new JPanel();
        panel.add(panel_3, "cell 6 0,grow");

        languageComboBox = new JComboBox<Language>();
        languageComboBox.setModel(new DefaultComboBoxModel<Language>(Language.values()));
        panel.add(languageComboBox, "cell 18 0,alignx right,aligny top");

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new CompoundBorder(new EmptyBorder(10, 5, 5, 5), new LineBorder(new Color(0, 0, 0), 1, true)));
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab(Texts.get("BookMasterMainView.tab.books"), null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        panel = new JPanel();
        panel_1.add(panel, BorderLayout.NORTH);
        panel.setLayout(new MigLayout("", "[][][fill][][]", "[]"));

        lblLasd = new JLabel();
        panel.add(lblLasd, "cell 0 0");
        numberOfBooks = new JLabel(String.valueOf(booksPMod.getBookTableModel().getRowCount()));
        numberOfBooks.setName(NAME_LABEL_NUMBER_OF_BOOKS);
        panel.add(numberOfBooks, "cell 1 0");

        lblAnzahlExemplare = new JLabel();
        panel.add(lblAnzahlExemplare, "cell 3 0");

        numberOfCopies = new JLabel(String.valueOf(library.getCopies().size()));
        panel.add(numberOfCopies, "cell 4 0");

        panel_4 = new JPanel();
        panel_1.add(panel_4, BorderLayout.CENTER);
        panel_4.setLayout(new BorderLayout(0, 0));

        JPanel panel_5 = new JPanel();
        panel_4.add(panel_5, BorderLayout.NORTH);
        panel_5.setLayout(new MigLayout("", "[grow][][grow][]", "[][]"));

        lblNewLabel_1 = new JLabel();
        panel_5.add(lblNewLabel_1, "cell 0 0");

        txtFieldSearch = new JTextField();
        txtFieldSearch.setName(NAME_SEARCH_FIELD);
        txtFieldSearch.setText(searchDefaultText);
        panel_5.add(txtFieldSearch, "flowx,cell 0 1,growx");
        txtFieldSearch.setColumns(10);

        JCheckBox checkBox = new JCheckBox();
        panel_5.add(checkBox, "cell 0 1");

        lblNurVerfgbare = new JLabel();
        panel_5.add(lblNurVerfgbare, "cell 1 1,alignx trailing");

        searchFilterComboBox = new JComboBox<SearchFilterElement>();
        searchFilterComboBox.setName(NAME_COMBOBOX_FILTER);
        searchFilterComboBox.setModel(booksPMod.getFilterComboBoxModel());
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

        btnNewBook = new JButton();
        btnNewBook.setName(NAME_BUTTON_NEW);
        btnNewBook.setMnemonic('n');
        panel_7.add(btnNewBook, "cell 0 0,growx,aligny center");

        btnOpenBook = new JButton();
        btnOpenBook.setName(NAME_BUTTON_OPEN);
        btnOpenBook.setMnemonic('o');
        panel_7.add(btnOpenBook, "cell 0 1,growx,aligny center");
        btnOpenBook.setEnabled(false);

        JPanel panel_2 = new JPanel();
        tabbedPane.addTab(Texts.get("BookMasterMainView.tab.lending"), null, panel_2, null);
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
                if (!txtFieldSearch.getText().equals(searchDefaultText)) {
                    getController().searchBooks(txtFieldSearch.getText(), ((SearchFilterElement) searchFilterComboBox.getSelectedItem()).getBookTableModelColumn());
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

        languageComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Language selectedLanguage = (Language) languageComboBox.getSelectedItem();
                Texts.getInstance().switchTo(selectedLanguage.getLocale());
            }
        });

        txtFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
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

        txtFieldSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                if (txtFieldSearch.getText().equals(searchDefaultText)) {
                    txtFieldSearch.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent arg0) {
                if (txtFieldSearch.getText().equals("")) {
                    txtFieldSearch.setText(searchDefaultText);
                }
            }
        });

    }

    private void search() {
        if (!txtFieldSearch.getText().equals(searchDefaultText)) {
            getController().searchBooks(txtFieldSearch.getText(), ((SearchFilterElement) searchFilterComboBox.getSelectedItem()).getBookTableModelColumn());
        }
    }

}
