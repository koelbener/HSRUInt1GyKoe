package application.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import application.controller.BookDetailController;
import application.core.LibraryActionListener;
import application.core.Repository;
import application.viewModel.CopyListModel;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.view.ValidationResultViewFactory;

import domain.Book;
import domain.Copy;
import domain.Shelf;

public class BookDetailMainView extends MainViewBase<Book, BookDetailController> {

    public static final String NAME_BOOK_DETAIL_MAIN_VIEW = "BookDetailMainView";
    public static final String NAME_BUTTON_CANCEL = "Cancel";

    private static final long serialVersionUID = 1L;
    private JTextField tfTitle;
    private JTextField tfAutor;
    private JTextField tfVerlag;
    private JComboBox<Shelf> tfRegal;
    private JTextField tfNumberOfCopies;
    private CopyListModel copyListModel;

    private JButton btnSave;
    private JButton btnCancel;
    private ValidationResultModel validationModel;

    public BookDetailMainView(Book book) {
        super(book, NAME_BOOK_DETAIL_MAIN_VIEW);
        updateViewValues();
    }

    @Override
    protected void initModel() {
        super.initModel();
        Book referenceObject = getReferenceObject();
        List<Copy> copiesOfBook = new ArrayList<Copy>();
        if (referenceObject != null) {
            copiesOfBook = library.getCopiesOfBook(referenceObject);
        }
        copyListModel = new CopyListModel(copiesOfBook);
    }

    private void updateViewValues() {
        Book referenceObject = getReferenceObject();
        if (referenceObject != null) {
            tfTitle.setText(referenceObject.getName());
            tfAutor.setText(referenceObject.getAuthor());
            tfVerlag.setText(referenceObject.getPublisher());
            // retrieve new possible shelves-data
            tfRegal.setModel(Repository.getInstance().getShelfPMod().getShelfComboBoxModel());
            // select the correct shelf
            tfRegal.setSelectedItem(referenceObject.getShelf());
            tfNumberOfCopies.setText(String.valueOf(copyListModel.getSize()));
        }
    }

    private Book extractViewValues() {
        Book book = getReferenceObject();
        if (book == null) {
            book = new Book();
        }
        book.setAuthor(tfAutor.getText());
        book.setName(tfTitle.getText());
        book.setPublisher(tfVerlag.getText());
        book.setShelf((Shelf) tfRegal.getSelectedItem());
        return book;
    }

    /**
     * Initialize the contents of the frame.
     */
    @Override
    protected void initUIElements() {
        super.initUIElements();
        setTitle("Buch Detailansicht");
        setBounds(100, 100, 450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel_4 = new JPanel();
        getContentPane().add(panel_4, BorderLayout.CENTER);
        panel_4.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel_4.add(panel, BorderLayout.NORTH);
        panel.setBorder(new TitledBorder(null, "Buch Informationen", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setLayout(new MigLayout("", "[][grow]", "[][][][]"));

        JLabel lblTitel = new JLabel("Titel");
        panel.add(lblTitel, "cell 0 0,alignx trailing");

        tfTitle = new JTextField();
        panel.add(tfTitle, "cell 1 0,growx");
        tfTitle.setColumns(10);

        JLabel lblAutor = new JLabel("Autor");
        panel.add(lblAutor, "cell 0 1,alignx trailing");

        tfAutor = new JTextField();
        panel.add(tfAutor, "cell 1 1,growx");
        tfAutor.setColumns(10);

        JLabel lblVerlag = new JLabel("Verlag");
        panel.add(lblVerlag, "cell 0 2,alignx trailing");

        tfVerlag = new JTextField();
        panel.add(tfVerlag, "cell 1 2,growx");
        tfVerlag.setColumns(10);

        JLabel lblRegal = new JLabel("Regal");
        panel.add(lblRegal, "cell 0 3,alignx trailing");

        tfRegal = new JComboBox<Shelf>();
        panel.add(tfRegal, "cell 1 3,growx");

        JPanel panel_1 = new JPanel();
        panel_4.add(panel_1, BorderLayout.CENTER);
        panel_1.setBorder(new TitledBorder(null, "Exemplare", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_2 = new JPanel();
        panel_1.add(panel_2, BorderLayout.NORTH);
        panel_2.setLayout(new MigLayout("", "[][grow][][]", "[]"));

        JLabel lblAnzahl = new JLabel("Anzahl");
        panel_2.add(lblAnzahl, "cell 0 0,alignx trailing");

        tfNumberOfCopies = new JTextField();
        tfNumberOfCopies.setEnabled(false);
        panel_2.add(tfNumberOfCopies, "flowx,cell 1 0");
        tfNumberOfCopies.setColumns(10);

        JButton btnEntfernen = new JButton("Entfernen");
        panel_2.add(btnEntfernen, "cell 2 0");

        JButton btnHinzufgen = new JButton("Hinzuf\u00FCgen");
        panel_2.add(btnHinzufgen, "cell 3 0");

        JPanel panel_3 = new JPanel();
        panel_1.add(panel_3, BorderLayout.CENTER);
        panel_3.setLayout(new BorderLayout(0, 0));

        JList<Copy> list = new JList<Copy>();
        list.setModel(copyListModel);
        panel_3.add(list);

        JPanel panel_5 = new JPanel();
        getContentPane().add(panel_5, BorderLayout.SOUTH);
        panel_5.setLayout(new BorderLayout(0, 0));

        JPanel panel_7 = new JPanel();
        panel_7.setMinimumSize(new Dimension(10, 20));
        panel_5.add(panel_7, BorderLayout.NORTH);

        validationModel = new DefaultValidationResultModel();
        panel_7.setLayout(new MigLayout("", "[grow]", "[1px]"));
        JComponent validationResultList = ValidationResultViewFactory.createReportList(validationModel);
        validationResultList.setMinimumSize(new Dimension(100, 100));
        panel_7.add(validationResultList, "cell 0 0,alignx right,growy");

        JPanel panel_8 = new JPanel();
        panel_5.add(panel_8, BorderLayout.SOUTH);
        panel_8.setLayout(new MigLayout("", "[grow][85px][]", "[23px]"));

        btnSave = new JButton("Speichern");
        panel_8.add(btnSave, "cell 1 0,alignx left,aligny top");
        btnSave.setMnemonic('s');

        btnCancel = new JButton("Abbrechen");
        panel_8.add(btnCancel, "cell 2 0,alignx left,aligny top");
        btnCancel.setName(NAME_BUTTON_CANCEL);
        btnCancel.setMnemonic('c');
    }

    @Override
    protected BookDetailController initController() {
        return new BookDetailController();

    }

    @Override
    protected void initListeners() {
        btnCancel.addActionListener(new LibraryActionListener() {

            @Override
            protected void execute() {
                BookDetailMainView.this.dispose();
            }
        });
        btnSave.addActionListener(new LibraryActionListener() {

            @Override
            protected void execute() {

                Book book = extractViewValues();
                ValidationResult validation = new BookValidator().validate(book);

                validationModel.setResult(validation);
                if (validation.hasErrors()) {

                } else {
                    if (getController().saveBook(book)) {
                        BookDetailMainView.this.dispose();
                    }

                }

            }
        });

    }

}
