package application.view.subView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.controller.BookMasterController;
import application.core.Repository;
import application.core.Texts;
import application.presentationModel.BooksPMod;
import application.presentationModel.componentModel.BookTableModel;
import application.presentationModel.componentModel.SearchFilterElement;
import application.view.helper.BooksTableContextMenuListener;
import application.view.helper.EnableCompontentOnTableSelectionListener;
import application.view.helper.HideTextOnFocusListener;
import domain.Library;

public class BookMasterSubView extends SubViewBase<Library, BookMasterController> {

    public static final String NAME_BUTTON_OPEN = "button.open";
    public static final String NAME_BUTTON_NEW = "button.new";
    public static final String NAME_TABLE_BOOKS = "table.books";
    public static final String NAME_LABEL_NUMBER_OF_BOOKS = "label.numberOfBooks";
    public static final String NAME_SEARCH_FIELD = "textField.search";
    public static final String NAME_COMBOBOX_FILTER = "comboBox.searchFilter";

    private static final Logger logger = LoggerFactory.getLogger(BookMasterSubView.class);

    public static String searchDefaultText;

    public JTextField txtSearch;
    public JLabel lblNumberOfCopies;
    public JLabel lblNumberOfBooks;
    public JButton btnOpenBook;
    public JButton btnNewBook;
    public JTable tblBooks;
    public JComboBox<SearchFilterElement> comboSearchFilter;
    public JLabel lblAnzahlExemplare;
    public JLabel lblLasd;
    public JPanel pnInventory;
    public JLabel lblAllBooksHint;
    public JLabel lblNurVerfgbare;
    public JPanel pnStatistics;
    private HideTextOnFocusListener hideTextOnFocusListener;
    private JCheckBox checkBoxOnlyAvailable;

    public BooksPMod booksPMod;

    public BookMasterSubView() {
        super(null);
    }

    /**
     * @wbp.parser.entryPoint
     */
    /*
     * (non-Javadoc)
     * 
     * @see application.view.subView.SubViewBase#initUIElements()
     */
    @Override
    protected void initUIElements() {
        container.setLayout(new BorderLayout());
        pnStatistics = new JPanel();
        container.add(pnStatistics, BorderLayout.NORTH);
        pnStatistics.setLayout(new MigLayout("", "[][][fill][][]", "[]"));

        lblLasd = new JLabel();
        pnStatistics.add(lblLasd, "cell 0 0");
        lblNumberOfBooks = new JLabel();
        lblNumberOfBooks.setName(NAME_LABEL_NUMBER_OF_BOOKS);
        pnStatistics.add(lblNumberOfBooks, "cell 1 0");

        lblAnzahlExemplare = new JLabel();
        pnStatistics.add(lblAnzahlExemplare, "cell 3 0");

        lblNumberOfCopies = new JLabel();
        pnStatistics.add(lblNumberOfCopies, "cell 4 0");

        updateStatistics();

        pnInventory = new JPanel();
        container.add(pnInventory, BorderLayout.CENTER);
        pnInventory.setLayout(new BorderLayout(0, 0));

        JPanel panel_5 = new JPanel();
        pnInventory.add(panel_5, BorderLayout.NORTH);
        panel_5.setLayout(new MigLayout("", "[grow][grow][][]", "[][]"));

        lblAllBooksHint = new JLabel();
        panel_5.add(lblAllBooksHint, "cell 0 0");

        txtSearch = new JTextField();
        txtSearch.setName(NAME_SEARCH_FIELD);
        panel_5.add(txtSearch, "flowx,cell 0 1,growx");
        txtSearch.setColumns(10);

        comboSearchFilter = new JComboBox<SearchFilterElement>();
        comboSearchFilter.setName(NAME_COMBOBOX_FILTER);
        comboSearchFilter.setModel(booksPMod.getFilterComboBoxModel());
        panel_5.add(comboSearchFilter, "cell 1 1,growx");

        checkBoxOnlyAvailable = new JCheckBox();
        panel_5.add(checkBoxOnlyAvailable, "cell 2 1");

        lblNurVerfgbare = new JLabel();
        panel_5.add(lblNurVerfgbare, "cell 3 1,alignx trailing");

        JPanel panel_6 = new JPanel();
        pnInventory.add(panel_6, BorderLayout.CENTER);
        panel_6.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();

        panel_6.add(scrollPane, BorderLayout.CENTER);

        tblBooks = new JTable(booksPMod.getBookTableModel());
        tblBooks.setRowSorter(booksPMod.getBookTableRowSorter());
        tblBooks.setName(NAME_TABLE_BOOKS);
        setBookTableColumnWidths();

        scrollPane.setViewportView(tblBooks);

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
    }

    // TODO: Code doesn't work
    private void setBookTableColumnWidths() {
        TableColumn column = null;
        for (int i = 0; i < 5; i++) {
            column = tblBooks.getColumnModel().getColumn(i);
            if (i == BookTableModel.COLUMN_SHELF || i == BookTableModel.COLUMN_AMOUNT) {
                column.setPreferredWidth(30);
            } else {
                column.setPreferredWidth(100);
            }
        }
        tblBooks.doLayout();
    }

    @Override
    protected void setTexts() {
        // panel titles
        pnStatistics.setBorder(new TitledBorder(null, Texts.get("BookMasterMainView.statisticsPanel.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        pnInventory.setBorder(new TitledBorder(null, Texts.get("BookMasterMainView.inventoryPanel.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

        // components
        lblAnzahlExemplare.setText(Texts.get("BookMasterMainView.lblAnzahlExemplare.text"));
        lblLasd.setText(Texts.get("BookMasterMainView.lblLasd.text"));
        btnNewBook.setText(Texts.get("BookMasterMainView.btnNewBook.text"));
        lblAllBooksHint.setText(Texts.get("BookMasterMainView.allBooksHint.text"));
        btnOpenBook.setText(Texts.get("BookMasterMainView.btnOpenBook.text"));
        searchDefaultText = Texts.get("BookMasterMainView.searchDefault");
        txtSearch.setText(searchDefaultText);
        if (hideTextOnFocusListener != null)
            hideTextOnFocusListener.updateText(searchDefaultText);
        lblNurVerfgbare.setText(Texts.get("BookMasterMainView.lblNurVerfgbare.text"));

        // Tooltips
        btnOpenBook.setToolTipText(Texts.get("BookMasterMainView.btnOpenBook.toolTipText"));

        // table
        booksPMod.getBookTableModel().setColumns();

        // filter comboBox
        booksPMod.getFilterComboBoxModel().updateTexts();

        container.revalidate();
    }

    @Override
    protected void initModel() {
        super.initModel();
        booksPMod = Repository.getInstance().getBooksPMod();
        booksPMod.addObserver(this);
    }

    @Override
    protected BookMasterController initController() {
        return new BookMasterController();
    }

    @Override
    protected void initListeners() {

        new EnableCompontentOnTableSelectionListener(tblBooks, btnOpenBook);

        comboSearchFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                search();
            }

            private void search() {
                if (!txtSearch.getText().equals(searchDefaultText)) {
                    getController().setSearchFilter(((SearchFilterElement) comboSearchFilter.getSelectedItem()).getTableModelColumn());
                }
            }
        });

        tblBooks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    logger.trace("doubleClick detected");
                    getController().openBooks(new int[] { tblBooks.rowAtPoint(e.getPoint()) });
                }
            }
        });

        btnOpenBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getController().openBooks(tblBooks.getSelectedRows());
            }
        });

        btnNewBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getController().openNewBook();
            }
        });

        checkBoxOnlyAvailable.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                getController().setSearchOnlyAvailableBooks(checkBoxOnlyAvailable.isSelected());
            }
        });

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
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

        hideTextOnFocusListener = new HideTextOnFocusListener(txtSearch, searchDefaultText);

        tblBooks.addMouseListener(new BooksTableContextMenuListener(tblBooks, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getController().openNewBook();
            }
        }, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getController().openBooks(tblBooks.getSelectedRows());
            }
        }));

    }

    private void search() {
        if (!txtSearch.getText().equals(searchDefaultText)) {
            getController().searchBooks(txtSearch.getText());
        }
    }

    @Override
    public void update(Observable observable, Object arg1) {
        if (observable instanceof BooksPMod) {
            logger.debug("Updating statistics");
            updateStatistics();
        } else {
            super.update(observable, arg1);
        }
    }

    private void updateStatistics() {
        lblNumberOfBooks.setText(String.valueOf(booksPMod.getBooksCount()));
        lblNumberOfCopies.setText(String.valueOf(booksPMod.getCopiesCount()));
    }

}
