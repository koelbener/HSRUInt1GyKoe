package application.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import application.controller.BookMasterController;
import application.core.LibraryActionListener;
import application.core.Repository;
import application.presentationModel.BooksPMod;
import domain.Book;

public class BookMasterMainView extends MainViewBase<BookMasterController> {

    public static final String NAME_BUTTON_SEARCH = "button.search";
    public static final String NAME_BUTTON_OPEN = "button.open";
    public static final String NAME_LIST_BOOKS = "list.books";
    public static final String NAME_LABEL_NUMBER_OF_BOOKS = "label.numberOfBooks";

    private static final long serialVersionUID = -5636590532882178863L;
    private JTextField txtSuche;
    private JList<Book> booksList;
    private JLabel numberOfCopies;
    private JLabel numberOfBooks;
    private JButton btnbuchOeffnen;
    private BooksPMod booksPMod;

    public BookMasterMainView() {
        setMinimumSize(new Dimension(616, 445));
    }

    @Override
    protected void initUIElements() {
        super.initUIElements();
        setTitle("Swinging Library");
        setName("MainWindow");
        setBounds(100, 100, 616, 445);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        getContentPane().add(panel, BorderLayout.NORTH);

        JLabel lblSwingingLibrary = new JLabel("Swinging Library");
        panel.add(lblSwingingLibrary);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new CompoundBorder(new EmptyBorder(10, 5, 5, 5), new LineBorder(new Color(0, 0, 0), 1,
                true)));
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("B\u00FCcher", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_3 = new JPanel();
        panel_3.setBorder(new TitledBorder(null, "Inventar Statistiken", TitledBorder.LEADING, TitledBorder.TOP, null,
                null));
        panel_1.add(panel_3, BorderLayout.NORTH);
        panel_3.setLayout(new MigLayout("", "[][][fill][][]", "[]"));

        JLabel lblLasd = new JLabel("Anzahl B\u00FCcher:");
        panel_3.add(lblLasd, "cell 0 0");
        numberOfBooks = new JLabel(String.valueOf(booksPMod.getBookListModel().getSize()));
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
        panel_5.setLayout(new MigLayout("", "[grow][][][]", "[][]"));

        JLabel lblNewLabel_1 = new JLabel("Alle B\u00FCcher stehen in der untenstehenden Tabelle");
        panel_5.add(lblNewLabel_1, "cell 0 0");

        txtSuche = new JTextField();
        txtSuche.setText("Suche...");
        panel_5.add(txtSuche, "flowx,cell 0 1,growx");
        txtSuche.setColumns(10);

        JCheckBox checkBox = new JCheckBox("");
        panel_5.add(checkBox, "cell 0 1");

        JLabel lblNurVerfgbare = new JLabel("Nur Verf\u00FCgbare");
        panel_5.add(lblNurVerfgbare, "cell 1 1");

        JButton btnSuchen = new JButton("Suchen");
        btnSuchen.setName(NAME_BUTTON_SEARCH);
        panel_5.add(btnSuchen, "cell 2 1");

        JPanel panel_6 = new JPanel();
        panel_4.add(panel_6, BorderLayout.CENTER);
        panel_6.setLayout(new BorderLayout(0, 0));

        booksList = new JList<Book>();
        booksList.setName(NAME_LIST_BOOKS);
        booksList.setModel(booksPMod.getBookListModel());

        JScrollPane scrollPane = new JScrollPane(booksList);

        panel_6.add(scrollPane, BorderLayout.CENTER);

        JPanel panel_7 = new JPanel();
        panel_6.add(panel_7, BorderLayout.EAST);
        panel_7.setLayout(new MigLayout("", "[]", "[23px][]"));

        JButton btnNeuesBuchHinzufgen = new JButton("Neu");
        panel_7.add(btnNeuesBuchHinzufgen, "cell 0 0,growx,aligny center");

        btnbuchOeffnen = new JButton("\u00D6ffnen");
        btnbuchOeffnen.setName(NAME_BUTTON_OPEN);
        panel_7.add(btnbuchOeffnen, "cell 0 1,growx,aligny center");
        btnbuchOeffnen.setToolTipText("Alle selektierten Bücher öffnen");
        btnbuchOeffnen.setEnabled(false);

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
        btnbuchOeffnen.addActionListener(new LibraryActionListener() {
            @Override
            protected void execute() {
                getController().openBooks(booksList.getSelectedIndices());
            }
        });
        // TODO this could be extracted into a reusable class
        booksList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                int numberOfSelectedIndices = booksList.getSelectedIndices().length;
                boolean enableButton = numberOfSelectedIndices > 0;
                btnbuchOeffnen.setEnabled(enableButton);
            }
        });
    }

}
