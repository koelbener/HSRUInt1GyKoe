package application.view.mainView.dialogView;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
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

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.controller.LoanDetailController;
import application.core.Repository;
import application.core.Texts;
import application.util.IconUtil;
import application.view.component.JNumberTextField;
import application.view.helper.CustomerComboBoxRenderer;
import application.view.helper.EnableCompontentOnTableSelectionListener;
import application.view.helper.HideTextOnFocusListener;
import application.viewModel.LoanDetailTableModel;

import com.jgoodies.validation.ValidationResult;

import domain.Copy;
import domain.Customer;
import domain.Loan;

public class LoanDetailMainView extends DialogViewBase<Loan, LoanDetailController> {

    private static String defaultSearchValue;

    private JPanel panel;
    private JPanel panel_2;
    private JPanel panel_3;
    private JLabel lblSearch;
    private JLabel lblCopyId;
    private JLabel lblCustomer;
    private JLabel lblCondition;
    private JLabel lblCopyTitle;
    private JLabel lblLoanStatus;
    private JLabel lblNumberOfLoans;
    private JLabel lblConditionValue;
    private JLabel lblCopyDescription;
    private JLabel lblNumberOfLoansNumber;
    private JTable tblLoans;
    private JButton btnCreateLoan;
    private JButton btnReturnButton;
    private JTextField txtCustomerSearch;
    private JNumberTextField txtCopyId;
    private JComboBox<Customer> cbCustomer;
    private JLabel lblReturnFeedbackLabel;

    private HideTextOnFocusListener hideTextOnFocusListener;

    private static final Logger logger = LoggerFactory.getLogger(LoanDetailMainView.class);

    public LoanDetailMainView(Loan loan) {
        super(loan);
    }

    @Override
    protected void initModel() {
        super.initModel();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                // set initial data
                if (getReferenceObject() != null) {
                    txtCustomerSearch.setText(getReferenceObject().getCustomer().getFullName());
                }
            }
        });
    }

    @Override
    protected void setTexts() {
        // title
        getContainer().setTitle(Texts.get("LoanDetailMainView.this.title")); //$NON-NLS-1$

        defaultSearchValue = Texts.get("LoanDetailMainViewBase.defaultSearchValue");
        if (hideTextOnFocusListener != null) {
            hideTextOnFocusListener.updateText(defaultSearchValue);
        }

        // border of panels
        updateLoansPanelTitle();
        panel_2.setBorder(new TitledBorder(null, Texts.get("LoanDetailMainViewBase.newLoan.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBorder(new TitledBorder(null, Texts.get("LoanDetailMainViewBase.customerSelection.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

        // components
        lblCopyTitle.setText(Texts.get("LoanDetailMainViewBase.newLoan.bookDescriptionLabel"));
        lblNumberOfLoans.setText(Texts.get("LoanDetailMainViewBase.loansOverview.numberOfLoans"));
        lblCopyId.setText(Texts.get("LoanDetailMainViewBase.newLoan.copyId"));
        btnCreateLoan.setText(Texts.get("LoanDetailMainViewBase.newLoan.lendCopyButton"));
        lblCustomer.setText(Texts.get("LoanDetailMainViewBase.customerSelection.customerLabel"));
        lblSearch.setText(Texts.get("LoanDetailMainViewBase.customerSelection.searchLabel"));
        lblCondition.setText(Texts.get("LoanDetailMainViewBase.newLoan.copyCondition"));
        btnReturnButton.setText(Texts.get("LoanDetailMainViewBase.loansOverview.returnButton"));
        lblReturnFeedbackLabel.setText("");
    }

    /**
     * Initialize the contents of the frame.
     * 
     * @wbp.parser.entryPoint
     */
    @Override
    protected void initUIElements() {
        super.initUIElements();

        Container contentPane = getContainer().getContentPane();
        getContainer().setBounds(100, 100, 600, 500);
        // TODO Modality interferes with minSize and ESC-Listener
        getContainer().setModalityType(ModalityType.APPLICATION_MODAL);

        JPanel mainPanel = new JPanel();
        contentPane.add(mainPanel, BorderLayout.CENTER);
        mainPanel.setLayout(new MigLayout("", "[grow]", "[74.00][top][grow]"));

        createCustomerSelectionSection(mainPanel);

        createNewLoanSection(mainPanel);

        createLoanOverviewSection(mainPanel);

    }

    private void createCustomerSelectionSection(JPanel panel_4) {
        panel = new JPanel();
        panel_4.add(panel, "cell 0 0,growx,aligny top");
        panel.setLayout(new MigLayout("", "[][193.00][grow]", "[grow][grow]"));

        lblSearch = new JLabel();
        panel.add(lblSearch, "cell 0 0,alignx trailing");

        txtCustomerSearch = new JTextField();
        panel.add(txtCustomerSearch, "cell 1 0,growx");
        txtCustomerSearch.setColumns(10);
        txtCustomerSearch.setToolTipText(defaultSearchValue);

        lblCustomer = new JLabel();
        panel.add(lblCustomer, "cell 0 1,alignx trailing");

        cbCustomer = new JComboBox<Customer>(Repository.getInstance().getCustomerPMod().getCustomerComboBoxModel());
        cbCustomer.setRenderer(new CustomerComboBoxRenderer());
        panel.add(cbCustomer, "cell 1 1 2 1,growx");

    }

    private void createLoanOverviewSection(JPanel panel_4) {
        panel_3 = new JPanel();
        panel_4.add(panel_3, "cell 0 2,grow");
        panel_3.setLayout(new MigLayout("", "[][grow]", "[grow][grow][][]"));

        lblNumberOfLoans = new JLabel();
        panel_3.add(lblNumberOfLoans, "cell 0 0");

        lblNumberOfLoansNumber = new JLabel("1");
        panel_3.add(lblNumberOfLoansNumber, "cell 1 0");

        LoanDetailTableModel loanDetailTableModel = Repository.getInstance().getLoansPMod().getLoanDetailTableModel();
        tblLoans = new JTable(loanDetailTableModel);
        Object selectedItem = cbCustomer.getSelectedItem();
        if (selectedItem instanceof Customer) {
            loanDetailTableModel.updateLoans((Customer) selectedItem);
        }
        updateLoanTable();
        panel_3.add(new JScrollPane(tblLoans), "cell 0 1 2 1,grow");

        btnReturnButton = new JButton();
        panel_3.add(btnReturnButton, "cell 1 2");

        lblReturnFeedbackLabel = new JLabel();
        panel_3.add(lblReturnFeedbackLabel, "cell 1 3");
    }

    private void createNewLoanSection(JPanel panel_4) {
        panel_2 = new JPanel();
        panel_4.add(panel_2, "cell 0 1,growx,aligny top");
        panel_2.setLayout(new MigLayout("", "[54px][54px][54px][:250px:250px][54px][54px][54px][54px]", "[20px][]"));

        lblCopyId = new JLabel();
        panel_2.add(lblCopyId, "cell 0 0,grow");

        txtCopyId = new JNumberTextField();
        txtCopyId.setMaximumSize(new Dimension(40, 20));
        txtCopyId.setPreferredSize(new Dimension(40, 20));
        txtCopyId.setMinimumSize(new Dimension(40, 20));
        panel_2.add(txtCopyId, "cell 1 0,grow");
        txtCopyId.setColumns(10);

        lblCopyTitle = new JLabel();

        panel_2.add(lblCopyTitle, "cell 2 0,grow");

        lblCopyDescription = new JLabel();
        // lblCopyDescription.setMaximumSize(new Dimension(250, 20));
        panel_2.add(lblCopyDescription, "cell 3 0,alignx left,growy");

        lblCondition = new JLabel();
        panel_2.add(lblCondition, "cell 2 1,alignx left,growy");

        lblConditionValue = new JLabel();
        panel_2.add(lblConditionValue, "cell 3 1,alignx left,growy");

        lblLoanStatus = new JLabel();
        panel_2.add(lblLoanStatus, "cell 2 2,span,alignx left,growy");

        btnCreateLoan = new JButton();
        panel_2.add(btnCreateLoan, "cell 0 1,grow");
    }

    private void updateLoanTable() {
        Customer customer = getCurrentCustomerSelection();
        if (customer != null) {
            Repository.getInstance().getLoansPMod().getLoanDetailTableModel().updateLoans(customer);
        }
    }

    private Customer getCurrentCustomerSelection() {
        if (cbCustomer.getSelectedItem() instanceof Customer) {
            return (Customer) cbCustomer.getSelectedItem();
        } else {
            return null;
        }
    }

    @Override
    protected LoanDetailController initController() {
        return new LoanDetailController();
    }

    private void updateLoansPanelTitle() {
        String overViewTitle;
        Customer customer = getCurrentCustomerSelection();
        if (customer != null) {
            String customerName = getCurrentCustomerSelection().getFullName();
            overViewTitle = Texts.get("LoanDetailMainViewBase.loansOverview.title") + " " + customerName;
        } else {
            overViewTitle = Texts.get("LoanDetailMainViewBase.loansOverview.titleAlone");
        }
        panel_3.setBorder(new TitledBorder(null, overViewTitle, TitledBorder.LEADING, TitledBorder.TOP, null, null));
    }

    @Override
    protected void initListeners() {

        new EnableCompontentOnTableSelectionListener(tblLoans, btnReturnButton);

        btnReturnButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String returnedCopies = getController().returnCopies(tblLoans.getSelectedRows());
                lblReturnFeedbackLabel.setText("Copies " + returnedCopies + " returned");
                updateMakeLoanButtonVisibility();
            }
        });

        txtCustomerSearch.getDocument().addDocumentListener(new DocumentListener() {
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
                logger.debug("search for value " + txtCustomerSearch.getText());
                if (txtCustomerSearch.getText().equals(defaultSearchValue)) {
                    getController().filterCustomers("");
                } else {
                    getController().filterCustomers(txtCustomerSearch.getText());
                }
            }
        });
        hideTextOnFocusListener = new HideTextOnFocusListener(txtCustomerSearch, defaultSearchValue);

        txtCustomerSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                txtCustomerSearch.selectAll();
            }
        });

        Repository.getInstance().getCustomerPMod().getCustomerComboBoxModel().addListDataListener(new ListDataListener() {

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
                        updateLoanTable();
                        setTexts();
                        updateMakeLoanButtonVisibility();
                    }
                });
            }
        });

        cbCustomer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                updateLoanTable();
                setTexts();
                updateMakeLoanButtonVisibility();
            }

        });

        btnCreateLoan.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveLoan();
            }

        });

        txtCopyId.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchCopy(txtCopyId.getNumber());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                searchCopy(txtCopyId.getNumber());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchCopy(txtCopyId.getNumber());
            }

            private void searchCopy(Long copyId) {
                if (copyId != null) {
                    updateCopy(getController().searchCopy(copyId));
                } else {
                    updateCopy(null);
                }
                updateMakeLoanButtonVisibility();
            }
        });
    }

    private void updateMakeLoanButtonVisibility() {
        boolean hasErrors = getController().validateLoan(txtCopyId.getNumber(), getCurrentCustomerSelection()).hasErrors();
        btnCreateLoan.setEnabled(!hasErrors);
    }

    private void updateCopy(Copy searchCopy) {
        if (searchCopy == null) {
            lblCopyDescription.setText("");
            lblConditionValue.setText("");
            lblLoanStatus.setIcon(null);
            lblLoanStatus.setText("");
        } else {
            String titleName = searchCopy.getTitle().getName();
            lblCopyDescription.setText(titleName);
            // set it as title as well in case the content is too long
            lblCopyDescription.setToolTipText(titleName);
            lblConditionValue.setText(searchCopy.getCondition().name());
            // lblLoanStatus
            boolean isCopyLent = Repository.getInstance().getLibrary().isCopyLent(searchCopy);
            if (!isCopyLent) {
                lblLoanStatus.setIcon(IconUtil.loadIcon("check.png"));
                lblLoanStatus.setText(Texts.get("validation.copy.isLent.false"));
            } else {
                String lender = Repository.getInstance().getLibrary().getLender(searchCopy).getFullName();
                lblLoanStatus.setText(Texts.get("validation.copy.isLent.true") + lender + ".");
                lblLoanStatus.setIcon(IconUtil.loadIcon("warning.png"));
            }
        }
        updateMakeLoanButtonVisibility();
    }

    private void saveLoan() {

        Long copyId = txtCopyId.getNumber();

        if (copyId == null) {
            lblLoanStatus.setText(Texts.get("LoanDetailMainViewBase.newLoan.noInventoryNumber"));
        } else {
            ValidationResult validationResult = getController().validateLoan(copyId, getCurrentCustomerSelection());
            if (validationResult.hasErrors()) {
                lblLoanStatus.setText(validationResult.getMessagesText());
            } else {
                lblLoanStatus.setText(Texts.get("LoanDetailMainViewBase.newLoan.loanSaved"));
                txtCopyId.setText("");
                updateCopy(null);
                Loan loan = getController().saveLoan(copyId, getCurrentCustomerSelection());
                Repository.getInstance().getLoansPMod().getLoanDetailTableModel().addLoan(loan);
            }
        }
    }
}
