package application.view.mainView.dialogView;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import application.controller.BookDetailController;
import application.core.Repository;
import application.core.Texts;
import application.presentationModel.CopyPMod;
import application.presentationModel.componentModel.ConditionComboBoxModel;
import application.presentationModel.componentModel.ConditionListCellRenderer;
import application.presentationModel.componentModel.CopyListModel;
import application.util.IconUtil;
import application.view.helper.CopiesListCellRenderer;
import application.view.helper.CopiesListContextMenuListener;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.view.ValidationResultViewFactory;

import domain.Book;
import domain.Condition;
import domain.Copy;
import domain.Shelf;
import domain.validator.BookValidator;

public abstract class BookDetailMainViewBase extends DialogViewBase<Book, BookDetailController> implements Observer {

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
    protected JTextField txtValNrOfCopies;
    protected JComboBox<Shelf> comboShelf;
    private JButton btnSave;
    private JButton btnCancel;
    private JButton btnAdd;
    private JButton btnRemove;
    private JButton btnSetCondition;
    private JPanel pnBookInfo;
    private JLabel lblTitle;
    private JLabel lblAuthor;
    private JLabel lblPublisher;
    private JLabel lblShelf;
    private JPanel pnCopies;
    private JLabel lblNrOfCopies;
    private JList<Copy> listCopies;
    private JComboBox<Condition> comboCondition;
    protected CopyListModel listModelCopies;
    protected ValidationResultModel validationModel;
    protected JComponent validationResultList;
    protected JPanel pnValidation;

    public BookDetailMainViewBase(Book book) {
        super(book, "book_closed.gif");
        updateViewValues();
    }

    @Override
    protected void addObservables() {
        observables.add(Repository.getInstance().getCopyPMod());
    }

    @Override
    protected void initModel() {
        super.initModel();
        Book referenceObject = getReferenceObject();
        List<Copy> copiesOfBook = new ArrayList<Copy>();
        if (referenceObject != null) {
            copiesOfBook = Repository.getInstance().getBooksPMod().getCopiesOfBook(referenceObject);
        }
        listModelCopies = new CopyListModel(Copy.cloneCopies(copiesOfBook));
    }

    private void updateViewValues() {
        // retrieve new possible shelves-data
        comboShelf.setModel(Repository.getInstance().getShelfPMod().getShelfComboBoxModel());

        Book referenceObject = getReferenceObject();
        if (referenceObject != null) {
            txtFieldTitle.setText(referenceObject.getName());
            txtFieldAuthor.setText(referenceObject.getAuthor());
            txtFieldPublisher.setText(referenceObject.getPublisher());
            // select the correct shelf
            comboShelf.setSelectedItem(referenceObject.getShelf());
        } else {
            comboShelf.setSelectedItem(Shelf.A1);
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
        book.setShelf((Shelf) comboShelf.getSelectedItem());
        return book;
    }

    @Override
    protected void setTexts() {
        // title
        getContainer().setTitle(Texts.get("BookDetailMainView.this.title")); //$NON-NLS-1$

        // border of panels
        pnBookInfo.setBorder(new TitledBorder(null, Texts.get("BookDetailMainView.bookInfo.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        pnCopies.setBorder(new TitledBorder(null, Texts.get("BookDetailMainView.copies.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$

        // components
        lblTitle.setText(Texts.get("BookDetailMainView.lblTitel.text"));
        lblAuthor.setText(Texts.get("BookDetailMainView.lblAutor.text"));
        lblPublisher.setText(Texts.get("BookDetailMainView.lblVerlag.text"));
        lblShelf.setText(Texts.get("BookDetailMainView.lblRegal.text"));
        lblNrOfCopies.setText(Texts.get("BookDetailMainView.lblAnzahl.text"));
        btnRemove.setText(Texts.get("BookDetailMainView.btnEntfernen.text"));
        btnAdd.setText(Texts.get("BookDetailMainView.btnHinzufgen.text"));
        btnSetCondition.setText(Texts.get("BookDetailMainView.btnSetCondition.text"));
        btnSave.setText(Texts.get("BookDetailMainView.btnSave.text"));
        btnCancel.setText(Texts.get("BookDetailMainView.btnCancel.text"));
    }

    /**
     * Initialize the contents of the frame.
     * 
     * @wbp.parser.entryPoint
     */
    @Override
    protected void initUIElements() {
        super.initUIElements();
        getContainer().setBounds(100, 100, 616, 445);
        container.setMinimumSize(new Dimension(616, 445));

        Container contentPane = getContainer().getContentPane();
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel pnMainPanel = new JPanel();
        contentPane.add(pnMainPanel, BorderLayout.CENTER);
        pnMainPanel.setLayout(new BorderLayout(0, 0));

        createBookInfoPanel(pnMainPanel);

        createCopiesPanel(pnMainPanel);
        createControlPanel();
        JPanel pnValidationContainer = createValidationPanel(contentPane);

        createButtonsPanel(pnValidationContainer);
    }

    private void createButtonsPanel(JPanel pnValidationContainer) {
        JPanel pnButtons = new JPanel();
        pnButtons.setLayout(new MigLayout("", "[grow][85px][]", "[23px]"));

        btnSave = new JButton();
        pnButtons.add(btnSave, "cell 1 0,alignx left,aligny top");
        btnSave.setMnemonic('s');
        btnSave.setName(NAME_BUTTON_SAVE);
        pnValidationContainer.add(pnButtons, BorderLayout.SOUTH);

        btnCancel = new JButton();
        pnButtons.add(btnCancel, "cell 2 0,alignx left,aligny top");
        btnCancel.setName(NAME_BUTTON_CANCEL);
        btnCancel.setMnemonic('c');
    }

    private JPanel createValidationPanel(Container contentPane) {
        JPanel pnValidationContainer = new JPanel();
        contentPane.add(pnValidationContainer, BorderLayout.SOUTH);
        pnValidationContainer.setLayout(new BorderLayout(0, 0));

        pnValidation = new JPanel();
        pnValidation.setMinimumSize(new Dimension(0, 0));
        pnValidation.setName(NAME_VALIDATION_PANEL);

        validationModel = new DefaultValidationResultModel();
        pnValidation.setLayout(new MigLayout("", "[grow]", "[40px,grow]"));
        validationResultList = ValidationResultViewFactory.createReportList(validationModel);
        pnValidation.add(validationResultList, "cell 0 0,alignx right,growy");
        pnValidationContainer.add(pnValidation, BorderLayout.NORTH);
        return pnValidationContainer;
    }

    private void createCopiesPanel(JPanel pnMainPanel) {
        pnCopies = new JPanel();
        pnMainPanel.add(pnCopies, BorderLayout.CENTER);
        pnCopies.setLayout(new BorderLayout(0, 0));

        JPanel pnCopiesOverview = new JPanel();
        pnCopies.add(pnCopiesOverview, BorderLayout.CENTER);
        pnCopiesOverview.setLayout(new BorderLayout(0, 0));

        listCopies = new JList<Copy>();
        listCopies.setModel(listModelCopies);
        listCopies.setCellRenderer(new CopiesListCellRenderer());
        pnCopiesOverview.add(listCopies);

        JPanel pnCopiesAction = new JPanel();
        pnCopiesAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Condition[] options = new Condition[Condition.values().length + 1];
        options[0] = null;
        for (int i = 1; i <= options.length - 1; i++) {
            options[i] = Condition.values()[i - 1];
        }
        options[1] = Condition.LOST;
        comboCondition = new JComboBox<Condition>(new ConditionComboBoxModel());
        comboCondition.setRenderer(new ConditionListCellRenderer());
        comboCondition.setEnabled(false);
        pnCopiesAction.add(comboCondition);
        btnSetCondition = new JButton();
        btnSetCondition.setEnabled(false);
        btnSetCondition.setMnemonic('t');
        pnCopiesAction.add(btnSetCondition);

        pnCopies.add(pnCopiesAction, BorderLayout.SOUTH);
    }

    private void createControlPanel() {
        JPanel pnControls = new JPanel();
        pnCopies.add(pnControls, BorderLayout.NORTH);
        pnControls.setLayout(new MigLayout("", "[][grow][][]", "[]"));

        lblNrOfCopies = new JLabel();
        pnControls.add(lblNrOfCopies, "cell 0 0,alignx trailing");

        txtValNrOfCopies = new JTextField();
        txtValNrOfCopies.setEnabled(false);
        pnControls.add(txtValNrOfCopies, "flowx,cell 1 0");
        txtValNrOfCopies.setColumns(10);

        btnAdd = new JButton();
        btnAdd.setName(NAME_BUTTON_ADD_COPY);
        btnAdd.setMnemonic('a');
        btnAdd.setIcon(IconUtil.loadIcon("add.gif"));
        pnControls.add(btnAdd, "cell 2 0");

        btnRemove = new JButton();
        btnRemove.setName(NAME_BUTTON_DELETE_COPY);
        btnRemove.setEnabled(false);
        btnRemove.setMnemonic('e');
        btnRemove.setIcon(IconUtil.loadIcon("delete.gif"));
        pnControls.add(btnRemove, "cell 3 0");

    }

    private void createBookInfoPanel(JPanel pnMainPanel) {
        pnBookInfo = new JPanel();
        pnMainPanel.add(pnBookInfo, BorderLayout.NORTH);
        pnBookInfo.setLayout(new MigLayout("", "[][grow]", "[][][][]"));

        lblTitle = new JLabel();
        pnBookInfo.add(lblTitle, "cell 0 0,alignx trailing");

        txtFieldTitle = new JTextField();
        pnBookInfo.add(txtFieldTitle, "cell 1 0,growx");
        txtFieldTitle.setColumns(10);
        txtFieldTitle.setName(NAME_TEXTBOX_TITLE);

        lblAuthor = new JLabel();
        pnBookInfo.add(lblAuthor, "cell 0 1,alignx trailing");

        txtFieldAuthor = new JTextField();
        txtFieldAuthor.setName(NAME_TEXTBOX_AUTHOR);
        pnBookInfo.add(txtFieldAuthor, "cell 1 1,growx");
        txtFieldAuthor.setColumns(10);

        lblPublisher = new JLabel();
        pnBookInfo.add(lblPublisher, "cell 0 2,alignx trailing");

        txtFieldPublisher = new JTextField();
        txtFieldPublisher.setName(NAME_TEXTBOX_PUBLISHER);
        pnBookInfo.add(txtFieldPublisher, "cell 1 2,growx");
        txtFieldPublisher.setColumns(10);

        lblShelf = new JLabel();
        pnBookInfo.add(lblShelf, "cell 0 3,alignx trailing");

        comboShelf = new JComboBox<Shelf>();
        comboShelf.setName(NAME_COMBOBOX_SHELF);
        pnBookInfo.add(comboShelf, "cell 1 3,growx");
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

        initKeyboardListener();

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book bookToValidate = extractViewValues(new Book());
                ValidationResult validation = new BookValidator().validate(bookToValidate);

                validationModel.setResult(validation);

                if (validateBook()) {
                    Book bookToUpdate = extractViewValues(getReferenceObject());
                    List<Copy> copies = listModelCopies.getAll();
                    BookDetailMainViewBase.this.getContainer().dispose();
                    getController().saveBook(bookToUpdate, copies);
                }
            }
        });

        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                listModelCopies.addCopy(new Copy());
                updateCopiesCount();
            }

        });
        btnRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (listModelCopies.removeCopy(listCopies.getSelectedValuesList())) {
                    btnRemove.setEnabled(false);
                }
                updateCopiesCount();
            }
        });
        listCopies.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                List<Copy> selectedCopies = listCopies.getSelectedValuesList();
                boolean copyIsSelected = selectedCopies.size() > 0;
                btnRemove.setEnabled(copyIsSelected && Repository.getInstance().getCopyPMod().areCopiesDeletable(selectedCopies));
                btnSetCondition.setEnabled(copyIsSelected);
                comboCondition.setEnabled(copyIsSelected);
                if (copyIsSelected) {
                    Condition worstCondition = Condition.NEW;
                    for (Copy copy : selectedCopies) {
                        if (copy.getCondition().isWorseThan(worstCondition)) {
                            worstCondition = copy.getCondition();
                        }
                    }
                    comboCondition.setSelectedItem(worstCondition.getNextWorse());
                } else {
                    comboCondition.setSelectedIndex(0);
                }
            }
        });

        btnSetCondition.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                List<Copy> selectedValuesList = listCopies.getSelectedValuesList();
                Condition condition = (Condition) comboCondition.getSelectedItem();
                for (Copy copy : selectedValuesList) {
                    copy.setCondition(condition);
                    listModelCopies.updateCopy(copy);
                }
            }
        });

        listCopies.addMouseListener(new CopiesListContextMenuListener(listCopies, listModelCopies));
    }

    private void initKeyboardListener() {
        // add a listener to the delete button
        listCopies.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "DELETE");
        listCopies.getActionMap().put("DELETE", new AbstractAction() {
            private static final long serialVersionUID = -5664120575484177305L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnRemove.isEnabled()) {
                    if (listModelCopies.removeCopy(listCopies.getSelectedValuesList())) {
                        btnRemove.setEnabled(false);
                    }
                    updateCopiesCount();
                }
            }
        });
    }

    protected boolean validateBook() {
        Book bookToValidate = extractViewValues(new Book());
        ValidationResult validation = new BookValidator().validate(bookToValidate);

        validationModel.setResult(validation);
        return !validation.hasErrors();
    }

    private void updateCopiesCount() {
        txtValNrOfCopies.setText("" + listModelCopies.getSize());
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        if (arg0.getClass().equals(CopyPMod.class)) {
            List<Copy> copiesOfBook = null;
            if (getReferenceObject() != null) {
                copiesOfBook = Repository.getInstance().getBooksPMod().getCopiesOfBook(getReferenceObject());
                listModelCopies = new CopyListModel(Copy.cloneCopies(copiesOfBook));
                listCopies.setModel(listModelCopies);
            }
        }
    }
}
