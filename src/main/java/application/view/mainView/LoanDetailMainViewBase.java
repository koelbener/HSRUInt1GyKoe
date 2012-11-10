package application.view.mainView;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableRowSorter;

import net.miginfocom.swing.MigLayout;
import application.controller.BookDetailController;
import application.core.Texts;
import application.viewModel.CopyListModel;
import application.viewModel.CustomerComboBoxModel;
import application.viewModel.LoanDetailTableModel;

import com.jgoodies.validation.ValidationResultModel;

import domain.Customer;
import domain.Loan;
import domain.Shelf;

public class LoanDetailMainViewBase extends MainViewBase<Loan, BookDetailController> {

    public static final String NAME_VALIDATION_PANEL = "ValidationPanel";
    public static final String NAME_BUTTON_SAVE = "Save";
    public static final String NAME_BUTTON_CANCEL = "Cancel";
    public static final String NAME_TEXTBOX_TITLE = "textbox.title";
    public static final String NAME_TEXTBOX_AUTHOR = "textbox.author";
    public static final String NAME_TEXTBOX_PUBLISHER = "textbox.publisher";
    public static final String NAME_COMBOBOX_SHELF = "combobox.shelf";
    protected JTextField txtFieldPublisher;
    protected JComboBox<Shelf> comboBoxShelf;
    protected JTextField tfNumberOfCopies;
    protected CopyListModel copyListModel;
    protected ValidationResultModel validationModel;
    protected JComponent validationResultList;
    protected JPanel validationPanel;
    private JPanel panel;
    private JPanel panel_2;
    private JPanel panel_3;
    private JLabel idendityLabel;
    private JLabel customerLabel;
    private JTextField idendityTextField;
    private JComboBox<Customer> customerComboBox;
    private JLabel copyIdLabel;
    private JLabel backDateLabel;
    private JTextField copyIdTextField;
    private JButton buttonMakeLoan;
    private JLabel copyOkLabel;
    private JTextField backDateTextField;
    private JLabel numberOfLoansLabel;
    private JLabel numberOfLoansNumberLabel;
    private JTable loansTable;
    private JPanel spacerPanel3;
    private JPanel spacerPanel4;
    private JPanel spacerPanel1;
    private JPanel spacerPanel2;

    public LoanDetailMainViewBase(Loan loan) {
        super(loan);
    }

    @Override
    protected void initModel() {
        super.initModel();
    }

    @Override
    protected void setTexts() {
        // title
        getContainer().setTitle(Texts.get("BookDetailMainView.this.title")); //$NON-NLS-1$

        // border of panels
        String overViewTitle = Texts.get("LoanDetailMainViewBase.loansOverview.title") + " " + ((Customer) customerComboBox.getSelectedItem()).getName();
        panel_3.setBorder(new TitledBorder(null, overViewTitle, TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_2.setBorder(new TitledBorder(null, Texts.get("LoanDetailMainViewBase.newLoan.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBorder(new TitledBorder(null, Texts.get("LoanDetailMainViewBase.customerSelection.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

        // components
        backDateLabel.setText(Texts.get("LoanDetailMainViewBase.newLoan.backUntilLabel"));
        numberOfLoansLabel.setText(Texts.get("LoanDetailMainViewBase.loansOverview.numberOfLoans"));
        copyIdLabel.setText(Texts.get("LoanDetailMainViewBase.newLoan.copyId"));
        buttonMakeLoan.setText(Texts.get("LoanDetailMainViewBase.newLoan.lendCopyButton"));
        customerLabel.setText(Texts.get("LoanDetailMainViewBase.customerSelection.customerLabel"));
        idendityLabel.setText(Texts.get("LoanDetailMainViewBase.customerSelection.inventoryLabel"));
    }

    /**
     * Initialize the contents of the frame.
     * 
     * @wbp.parser.entryPoint
     */
    @Override
    protected void initUIElements() {
        super.initUIElements();
        getContainer().setBounds(100, 100, 450, 450);
        getContainer().setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContainer().setModalityType(ModalityType.APPLICATION_MODAL);
        Container contentPane = getContainer().getContentPane();

        JPanel mainPanel = new JPanel();
        contentPane.add(mainPanel, BorderLayout.CENTER);
        mainPanel.setLayout(new MigLayout("", "[grow]", "[74.00][top][grow]"));

        createCustomerSelectionSection(mainPanel);

        createNewLoanSection(mainPanel);

        createLoanOverviewSection(mainPanel);

    }

    private void createLoanOverviewSection(JPanel panel_4) {
        panel_3 = new JPanel();
        panel_4.add(panel_3, "cell 0 2,grow");
        panel_3.setLayout(new MigLayout("", "[grow][]", "[][grow]"));

        numberOfLoansLabel = new JLabel();
        panel_3.add(numberOfLoansLabel, "cell 0 0");

        numberOfLoansNumberLabel = new JLabel("1");
        panel_3.add(numberOfLoansNumberLabel, "cell 1 0,aligny center");

        loansTable = new JTable(new LoanDetailTableModel((Customer) customerComboBox.getSelectedItem()));
        initLoanTable();
        panel_3.add(new JScrollPane(loansTable), "cell 0 1,grow");
    }

    private void createNewLoanSection(JPanel panel_4) {
        panel_2 = new JPanel();
        panel_4.add(panel_2, "cell 0 1,growx,aligny top");
        panel_2.setLayout(new MigLayout("", "[][100.00][][][grow]", "[grow][grow]"));

        copyIdLabel = new JLabel();
        panel_2.add(copyIdLabel, "cell 0 0,alignx left");

        copyIdTextField = new JTextField();
        panel_2.add(copyIdTextField, "cell 1 0,alignx left");
        copyIdTextField.setColumns(10);

        copyOkLabel = new JLabel("OK");
        panel_2.add(copyOkLabel, "cell 2 0,alignx left");

        buttonMakeLoan = new JButton();
        panel_2.add(buttonMakeLoan, "cell 3 0,alignx left");

        spacerPanel3 = new JPanel();
        panel_2.add(spacerPanel3, "cell 4 0,grow");

        backDateLabel = new JLabel();
        panel_2.add(backDateLabel, "cell 0 1,alignx trailing");

        backDateTextField = new JTextField();
        panel_2.add(backDateTextField, "cell 1 1 3 1,alignx left,aligny bottom");
        backDateTextField.setColumns(10);

        spacerPanel4 = new JPanel();
        panel_2.add(spacerPanel4, "cell 4 1,grow");
    }

    private void createCustomerSelectionSection(JPanel panel_4) {
        panel = new JPanel();
        panel_4.add(panel, "cell 0 0,growx,aligny top");
        panel.setLayout(new MigLayout("", "[][193.00][grow]", "[grow][grow]"));

        idendityLabel = new JLabel();
        panel.add(idendityLabel, "cell 0 0,alignx trailing");

        idendityTextField = new JTextField();
        panel.add(idendityTextField, "cell 1 0,growx");
        idendityTextField.setColumns(10);

        spacerPanel1 = new JPanel();
        panel.add(spacerPanel1, "cell 2 0,grow");

        customerLabel = new JLabel();
        panel.add(customerLabel, "cell 0 1,alignx trailing");

        customerComboBox = new JComboBox<Customer>(new CustomerComboBoxModel());
        panel.add(customerComboBox, "cell 1 1,growx");

        spacerPanel2 = new JPanel();
        panel.add(spacerPanel2, "cell 2 1,grow");
    }

    private void initLoanTable() {
        LoanDetailTableModel loanDetailTableModel = new LoanDetailTableModel((Customer) customerComboBox.getSelectedItem());
        loansTable.setModel(loanDetailTableModel);
        loansTable.setRowSorter(new TableRowSorter<LoanDetailTableModel>(loanDetailTableModel));
    }

    @Override
    protected BookDetailController initController() {
        return new BookDetailController();

    }

    @Override
    protected void initListeners() {
        customerComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                initLoanTable();
                setTexts();
            }

        });
    }
}
