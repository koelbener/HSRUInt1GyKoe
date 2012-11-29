package application.view.mainView.dialogView;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import application.controller.BookDetailController;
import application.core.Repository;
import application.core.Texts;
import application.presentationModel.componentModel.CopyListModel;
import application.util.IconUtil;
import application.view.helper.CopiesListCellRenderer;
import application.view.helper.CopiesListContextMenuListener;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.view.ValidationResultViewFactory;

import domain.Book;
import domain.Copy;
import domain.Shelf;
import domain.validator.BookValidator;

public abstract class BookDetailMainViewBase extends DialogViewBase<Book, BookDetailController> {

    public static final String NAME_VALIDATION_PANEL = "ValidationPanel";
    public static final String NAME_BUTTON_SAVE = "Save";
    public static final String NAME_BUTTON_CANCEL = "Cancel";
    public static final String NAME_BUTTON_ADD_COPY = "AddCopy";
    public static final String NAME_BUTTON_DELETE_COPY = "DeleteCopy";
    public static final String NAME_TEXTBOX_TITLE = "textbox.title";
    public static final String NAME_TEXTBOX_AUTHOR = "textbox.author";
    public static final String NAME_TEXTBOX_PUBLISHER = "textbox.publisher";
    public static final String NAME_COMBOBOX_SHELF = "combobox.shelf";

    protected JTextField txtFieldTitle;
    protected JTextField txtFieldAuthor;
    protected JTextField txtFieldPublisher;
    protected JTextField valNrOfCopies;
    protected JComboBox<Shelf> comboBoxShelf;
    private JButton btnSave;
    private JButton btnCancel;
    private JButton btnAdd;
    private JButton btnRemove;
    private JPanel panel;
    private JLabel lblTitle;
    private JLabel lblAuthor;
    private JLabel lblPublisher;
    private JLabel lblShelf;
    private JPanel panel_1;
    private JLabel lblNrOfCopies;
    private JList<Copy> copiesList;
    protected CopyListModel copyListModel;
    protected ValidationResultModel validationModel;
    protected JComponent validationResultList;
    protected JPanel validationPanel;

    public BookDetailMainViewBase(Book book) {
        super(book, "book_closed.gif");
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
        copyListModel = new CopyListModel(Copy.cloneCopies(copiesOfBook));
    }

    private void updateViewValues() {
        // retrieve new possible shelves-data
        comboBoxShelf.setModel(Repository.getInstance().getShelfPMod().getShelfComboBoxModel());

        Book referenceObject = getReferenceObject();
        if (referenceObject != null) {
            txtFieldTitle.setText(referenceObject.getName());
            txtFieldAuthor.setText(referenceObject.getAuthor());
            txtFieldPublisher.setText(referenceObject.getPublisher());
            // select the correct shelf
            comboBoxShelf.setSelectedItem(referenceObject.getShelf());
        } else {
            comboBoxShelf.setSelectedItem(Shelf.A1);
        }
        updateCopiesCount();
    }

    private Book extractViewValues(Book book) {
        if (book == null) {
            book = new Book();
        }
        book.setAuthor(txtFieldAuthor.getText());
        book.setName(txtFieldTitle.getText());
        book.setPublisher(txtFieldPublisher.getText());
        book.setShelf((Shelf) comboBoxShelf.getSelectedItem());
        return book;
    }

    @Override
    protected void setTexts() {
        // title
        getContainer().setTitle(Texts.get("BookDetailMainView.this.title")); //$NON-NLS-1$

        // border of panels
        panel.setBorder(new TitledBorder(null, Texts.get("BookDetailMainView.panel.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_1.setBorder(new TitledBorder(null, Texts.get("BookDetailMainView.panel_1.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$

        // components
        lblTitle.setText(Texts.get("BookDetailMainView.lblTitel.text"));
        lblAuthor.setText(Texts.get("BookDetailMainView.lblAutor.text"));
        lblPublisher.setText(Texts.get("BookDetailMainView.lblVerlag.text"));
        lblShelf.setText(Texts.get("BookDetailMainView.lblRegal.text"));
        lblNrOfCopies.setText(Texts.get("BookDetailMainView.lblAnzahl.text"));
        btnRemove.setText(Texts.get("BookDetailMainView.btnEntfernen.text"));
        btnAdd.setText(Texts.get("BookDetailMainView.btnHinzufgen.text"));
        btnSave.setText(Texts.get("BookDetailMainView.btnSave.text"));
        btnCancel.setText(Texts.get("BookDetailMainView.btnCancel.text"));
    }

    /**
     * Initialize the contents of the frame.
     */
    @Override
    protected void initUIElements() {
        super.initUIElements();
        getContainer().setBounds(100, 100, 450, 450);
        Container contentPane = getContainer().getContentPane();
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel_4 = new JPanel();
        contentPane.add(panel_4, BorderLayout.CENTER);
        panel_4.setLayout(new BorderLayout(0, 0));

        panel = new JPanel();
        panel_4.add(panel, BorderLayout.NORTH);
        panel.setLayout(new MigLayout("", "[][grow]", "[][][][]"));

        lblTitle = new JLabel();
        panel.add(lblTitle, "cell 0 0,alignx trailing");

        txtFieldTitle = new JTextField();
        panel.add(txtFieldTitle, "cell 1 0,growx");
        txtFieldTitle.setColumns(10);
        txtFieldTitle.setName(NAME_TEXTBOX_TITLE);

        lblAuthor = new JLabel();
        panel.add(lblAuthor, "cell 0 1,alignx trailing");

        txtFieldAuthor = new JTextField();
        txtFieldAuthor.setName(NAME_TEXTBOX_AUTHOR);
        panel.add(txtFieldAuthor, "cell 1 1,growx");
        txtFieldAuthor.setColumns(10);

        lblPublisher = new JLabel();
        panel.add(lblPublisher, "cell 0 2,alignx trailing");

        txtFieldPublisher = new JTextField();
        txtFieldPublisher.setName(NAME_TEXTBOX_PUBLISHER);
        panel.add(txtFieldPublisher, "cell 1 2,growx");
        txtFieldPublisher.setColumns(10);

        lblShelf = new JLabel();
        panel.add(lblShelf, "cell 0 3,alignx trailing");

        comboBoxShelf = new JComboBox<Shelf>();
        comboBoxShelf.setName(NAME_COMBOBOX_SHELF);
        panel.add(comboBoxShelf, "cell 1 3,growx");

        panel_1 = new JPanel();
        panel_4.add(panel_1, BorderLayout.CENTER);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_2 = new JPanel();
        panel_1.add(panel_2, BorderLayout.NORTH);
        panel_2.setLayout(new MigLayout("", "[][grow][][]", "[]"));

        lblNrOfCopies = new JLabel();
        panel_2.add(lblNrOfCopies, "cell 0 0,alignx trailing");

        valNrOfCopies = new JTextField();
        valNrOfCopies.setEnabled(false);
        panel_2.add(valNrOfCopies, "flowx,cell 1 0");
        valNrOfCopies.setColumns(10);

        btnAdd = new JButton();
        btnAdd.setName(NAME_BUTTON_ADD_COPY);
        btnAdd.setIcon(IconUtil.loadIcon("add.gif"));
        panel_2.add(btnAdd, "cell 2 0");

        btnRemove = new JButton();
        btnRemove.setName(NAME_BUTTON_DELETE_COPY);
        btnRemove.setEnabled(false);
        btnRemove.setIcon(IconUtil.loadIcon("delete.gif"));
        panel_2.add(btnRemove, "cell 3 0");

        JPanel panel_3 = new JPanel();
        panel_1.add(panel_3, BorderLayout.CENTER);
        panel_3.setLayout(new BorderLayout(0, 0));

        copiesList = new JList<Copy>();
        copiesList.setModel(copyListModel);
        copiesList.setCellRenderer(new CopiesListCellRenderer());
        panel_3.add(copiesList);

        JPanel panel_5 = new JPanel();
        contentPane.add(panel_5, BorderLayout.SOUTH);
        panel_5.setLayout(new BorderLayout(0, 0));

        validationPanel = new JPanel();
        validationPanel.setMinimumSize(new Dimension(0, 0));
        validationPanel.setName(NAME_VALIDATION_PANEL);

        validationModel = new DefaultValidationResultModel();
        validationPanel.setLayout(new MigLayout("", "[grow]", "[40px,grow]"));
        validationResultList = ValidationResultViewFactory.createReportList(validationModel);
        validationPanel.add(validationResultList, "cell 0 0,alignx right,growy");
        panel_5.add(validationPanel, BorderLayout.NORTH);

        JPanel panel_8 = new JPanel();
        panel_8.setLayout(new MigLayout("", "[grow][85px][]", "[23px]"));

        btnSave = new JButton();
        panel_8.add(btnSave, "cell 1 0,alignx left,aligny top");
        btnSave.setMnemonic('s');
        btnSave.setName(NAME_BUTTON_SAVE);
        panel_5.add(panel_8, BorderLayout.SOUTH);

        btnCancel = new JButton();
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
        btnCancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                BookDetailMainViewBase.this.getContainer().dispose();
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book bookToValidate = extractViewValues(new Book());
                ValidationResult validation = new BookValidator().validate(bookToValidate);

                validationModel.setResult(validation);

                if (validateBook()) {
                    Book bookToUpdate = extractViewValues(getReferenceObject());
                    List<Copy> copies = copyListModel.getAll();
                    BookDetailMainViewBase.this.getContainer().dispose();
                    getController().saveBook(bookToUpdate, copies);
                }
            }
        });

        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                copyListModel.addCopy(new Copy());
                updateCopiesCount();
            }

        });
        btnRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (copyListModel.removeCopy(copiesList.getSelectedValuesList()))
                    btnRemove.setEnabled(false);
                updateCopiesCount();
            }
        });
        copiesList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedCopies = copiesList.getSelectedIndices().length;
                btnRemove.setEnabled(selectedCopies > 0 && getController().areCopiesDeletable(copiesList.getSelectedValuesList()));
            }
        });

        copiesList.addMouseListener(new CopiesListContextMenuListener(copiesList, copyListModel, getController()));
    }

    protected boolean validateBook() {
        Book bookToValidate = extractViewValues(new Book());
        ValidationResult validation = new BookValidator().validate(bookToValidate);

        validationModel.setResult(validation);
        return !validation.hasErrors();
    }

    private void updateCopiesCount() {
        valNrOfCopies.setText("" + copyListModel.getSize());
    }
}
