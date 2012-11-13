package application.view.mainView;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.TableRowSorter;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.controller.BookDetailController;
import application.core.Repository;
import application.core.Texts;
import application.view.helper.CustomerComboBoxRenderer;
import application.viewModel.LoanDetailTableModel;
import domain.Customer;
import domain.Loan;

public class LoanDetailMainViewBase extends MainViewBase<Loan, BookDetailController> {

    private static final Logger logger = LoggerFactory.getLogger(LoanDetailMainViewBase.class);
    private static String defaultSearchValue;

    private JPanel panel;
    private JPanel panel_2;
    private JPanel panel_3;
    private JLabel searchLabel;
    private JLabel customerLabel;
    private JTextField searchTextField;
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

        defaultSearchValue = Texts.get("LoanDetailMainViewBase.defaultSearchValue");

        // border of panels
        String overViewTitle;
        if (customerComboBox.getSelectedItem() instanceof Customer) {
            overViewTitle = Texts.get("LoanDetailMainViewBase.loansOverview.title") + " " + ((Customer) customerComboBox.getSelectedItem()).getName();
        } else {
            overViewTitle = Texts.get("LoanDetailMainViewBase.loansOverview.titleAlone");
        }
        panel_3.setBorder(new TitledBorder(null, overViewTitle, TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_2.setBorder(new TitledBorder(null, Texts.get("LoanDetailMainViewBase.newLoan.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBorder(new TitledBorder(null, Texts.get("LoanDetailMainViewBase.customerSelection.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

        // components
        backDateLabel.setText(Texts.get("LoanDetailMainViewBase.newLoan.backUntilLabel"));
        numberOfLoansLabel.setText(Texts.get("LoanDetailMainViewBase.loansOverview.numberOfLoans"));
        copyIdLabel.setText(Texts.get("LoanDetailMainViewBase.newLoan.copyId"));
        buttonMakeLoan.setText(Texts.get("LoanDetailMainViewBase.newLoan.lendCopyButton"));
        customerLabel.setText(Texts.get("LoanDetailMainViewBase.customerSelection.customerLabel"));
        searchLabel.setText(Texts.get("LoanDetailMainViewBase.customerSelection.searchLabel"));
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

    /**
     * <code>@SuppressWarnings("unchecked")</code> because Java 1.6 does not support typed {@linkplain AbstractListModel}
     */
    @SuppressWarnings("unchecked")
    private void createCustomerSelectionSection(JPanel panel_4) {
        panel = new JPanel();
        panel_4.add(panel, "cell 0 0,growx,aligny top");
        panel.setLayout(new MigLayout("", "[][193.00][grow]", "[grow][grow]"));

        searchLabel = new JLabel();
        panel.add(searchLabel, "cell 0 0,alignx trailing");

        searchTextField = new JTextField();
        panel.add(searchTextField, "cell 1 0,growx");
        searchTextField.setColumns(10);
        searchTextField.setToolTipText(defaultSearchValue);

        customerLabel = new JLabel();
        panel.add(customerLabel, "cell 0 1,alignx trailing");

        customerComboBox = new JComboBox<Customer>(Repository.getInstance().getCutomerPMod().getCustomerComboBoxModel());
        customerComboBox.setRenderer(new CustomerComboBoxRenderer());
        panel.add(customerComboBox, "cell 1 1 2 1,growx");
    }

    private void createLoanOverviewSection(JPanel panel_4) {
        panel_3 = new JPanel();
        panel_4.add(panel_3, "cell 0 2,grow");
        panel_3.setLayout(new MigLayout("", "[grow][]", "[][grow]"));

        numberOfLoansLabel = new JLabel();
        panel_3.add(numberOfLoansLabel, "cell 0 0");

        numberOfLoansNumberLabel = new JLabel("1");
        panel_3.add(numberOfLoansNumberLabel, "cell 1 0,aligny center");

        loansTable = new JTable();
        if (customerComboBox.getSelectedItem() instanceof Customer) {
            loansTable.setModel(new LoanDetailTableModel((Customer) customerComboBox.getSelectedItem()));
        } else {
            loansTable.setModel(new LoanDetailTableModel(null));
        }
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

    private void initLoanTable() {
        logger.debug("reload loansTable");
        Customer customer;
        if (customerComboBox.getSelectedItem() instanceof Customer) {
            customer = (Customer) customerComboBox.getSelectedItem();
        } else {
            customer = null;
        }
        LoanDetailTableModel loanDetailTableModel = new LoanDetailTableModel(customer);
        loansTable.setModel(loanDetailTableModel);
        loansTable.setRowSorter(new TableRowSorter<LoanDetailTableModel>(loanDetailTableModel));
    }

    @Override
    protected BookDetailController initController() {
        return new BookDetailController();

    }

    @Override
    protected void initListeners() {
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
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

            private void search() {
                if (searchTextField.getText().equals(defaultSearchValue)) {
                    getController().filterCustomers("");
                } else {
                    getController().filterCustomers(searchTextField.getText());
                }
            }
        });

        searchTextField.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent arg0) {
                logger.debug("searchField focus lost");
                if (searchTextField.getText().equals("")) {
                    searchTextField.setText(defaultSearchValue);
                }
            }

            @Override
            public void focusGained(FocusEvent arg0) {
                logger.debug("searchField focus gained");
                if (searchTextField.getText().equals(defaultSearchValue)) {
                    searchTextField.setText("");
                }
            }
        });

        Repository.getInstance().getCutomerPMod().getCustomerComboBoxModel().addListDataListener(new ListDataListener() {

            @Override
            public void intervalRemoved(ListDataEvent arg0) {
            }

            @Override
            public void intervalAdded(ListDataEvent arg0) {
            }

            @Override
            public void contentsChanged(ListDataEvent arg0) {
                // call later, cause no mutation allowed in Listener
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        initLoanTable();
                        setTexts();
                    }
                });
            }
        });

        customerComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                initLoanTable();
                setTexts();
            }

        });
    }
}
