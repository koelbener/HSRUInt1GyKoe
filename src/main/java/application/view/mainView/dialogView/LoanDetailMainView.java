package application.view.mainView.dialogView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.controller.LoanDetailController;
import application.core.Repository;
import application.core.Texts;
import application.presentationModel.CopyPMod;
import application.presentationModel.LoansPMod;
import application.presentationModel.componentModel.ConditionListCellRenderer;
import application.presentationModel.componentModel.LoanDetailTableModel;
import application.util.IconUtil;
import application.view.component.JNumberTextField;
import application.view.helper.CustomerListRenderer;
import application.view.helper.DueDateTableCellRenderer;
import application.view.helper.EnableCompontentOnTableSelectionListener;
import application.view.helper.HideTextOnFocusListener;

import com.google.common.base.Joiner;
import com.jgoodies.validation.ValidationResult;

import domain.Condition;
import domain.Copy;
import domain.Customer;
import domain.Loan;

public class LoanDetailMainView extends DialogViewBase<Loan, LoanDetailController> implements Observer {

    private static String defaultSearchValue;

    private JPanel pnCustomerSelection;
    private JPanel pnNewLoan;
    private JPanel pnLoanOverview;
    private JLabel lblSearch;
    private JLabel lblCopyId;
    private JLabel lblCustomer;
    private JLabel lblCondition;
    private JLabel lblCopyTitle;
    private JLabel lblAvailabilityValue;
    private JLabel lblNumberOfLoans;
    private JLabel lblConditionValue;
    private JLabel lblValCopyTitle;
    private JLabel lblValNumberOfLoans;
    private JTable tblLoans;
    private JLabel lblCopyStatus;
    private JLabel lblAvailability;
    private JButton btnCreateLoan;
    private JButton btnReturnButton;
    private JButton btnLinkToLoan;
    private JTextField txtCustomerSearch;
    private JNumberTextField txtCopyId;
    private JComboBox<Condition> comboCondition;
    private JList<Customer> listCustomer;
    private JLabel lblReturnFeedbackLabel;
    private JLabel lblFine;

    private Copy currentSelectedCopy;
    private HideTextOnFocusListener hideTextOnFocusListener;

    private final Logger logger = LoggerFactory.getLogger(LoanDetailMainView.class);

    private LoansPMod pMod;
    private CopyPMod copyPMod;

    public LoanDetailMainView(Loan loan) {
        super(loan, "loan.gif");
    }

    public void switchToLoan(Loan loan, boolean reloadAll) {
        logger.debug("switch to loan {}", loan);
        setReferenceObject(loan);
        if (loan == null) {
            txtCustomerSearch.setText("");
        } else {
            updateLoanOverViewSection();
            if (reloadAll) {
                updateCustomerSelectionSection();
                clearNewLoanSection();
            }
        }
        updateMakeLoanButtonVisibility();
    }

    @Override
    protected void addObservables() {
        observables.add(Repository.getInstance().getCopyPMod());
    }

    @Override
    protected void initModel() {
        super.initModel();
        pMod = Repository.getInstance().getLoansPMod();
        copyPMod = Repository.getInstance().getCopyPMod();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                // set initial data
                if (getReferenceObject() != null) {
                    updateLoanOverViewSection();
                    updateCustomerSelectionSection();
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
        pnNewLoan.setBorder(new TitledBorder(null, Texts.get("LoanDetailMainViewBase.newLoan.title"), TitledBorder.LEADING, TitledBorder.TOP, null,
                null));
        pnCustomerSelection.setBorder(new TitledBorder(null, Texts.get("LoanDetailMainViewBase.customerSelection.title"), TitledBorder.LEADING,
                TitledBorder.TOP, null, null));

        // components
        lblCopyTitle.setText(Texts.get("LoanDetailMainViewBase.newLoan.bookDescriptionLabel"));
        lblNumberOfLoans.setText(Texts.get("LoanDetailMainViewBase.loansOverview.numberOfLoans"));
        lblCopyId.setText(Texts.get("LoanDetailMainViewBase.newLoan.copyId"));
        btnCreateLoan.setText(Texts.get("LoanDetailMainViewBase.newLoan.lendCopyButton"));
        lblCustomer.setText(Texts.get("LoanDetailMainViewBase.customerSelection.customerLabel"));
        lblSearch.setText(Texts.get("LoanDetailMainViewBase.customerSelection.searchLabel"));
        lblCondition.setText(Texts.get("LoanDetailMainViewBase.newLoan.copyCondition"));
        btnReturnButton.setText(Texts.get("LoanDetailMainViewBase.loansOverview.returnButton"));
        btnLinkToLoan.setText(Texts.get("LoanDetailMainViewBase.newLoan.VisitLoan"));
        lblReturnFeedbackLabel.setText("");
        lblAvailability.setText(Texts.get("LoanDetailMainViewBase.newLoan.availability"));
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
        getContainer().setBounds(100, 100, 616, 600);
        getContainer().setMinimumSize(new Dimension(616, 600));

        JPanel mainPanel = new JPanel();
        contentPane.add(mainPanel, BorderLayout.CENTER);
        mainPanel.setLayout(new MigLayout("", "[grow]", "[70.00,grow][89.00,top][]"));

        createCustomerSelectionSection(mainPanel);

        createNewLoanSection(mainPanel);

        createLoanOverviewSection(mainPanel);

        updateCustomerSelectionSection();
        updateLoanOverViewSection();
    }

    private void createCustomerSelectionSection(JPanel customerSelectionPanel) {
        pnCustomerSelection = new JPanel();
        customerSelectionPanel.add(pnCustomerSelection, "cell 0 0,grow");
        pnCustomerSelection.setLayout(new MigLayout("", "[][193.00][grow]", "[50.00][26.00,grow,fill]"));

        lblSearch = new JLabel();
        pnCustomerSelection.add(lblSearch, "cell 0 0,alignx trailing");

        txtCustomerSearch = new JTextField();
        pnCustomerSelection.add(txtCustomerSearch, "cell 1 0,growx");
        txtCustomerSearch.setColumns(10);
        txtCustomerSearch.setToolTipText(defaultSearchValue);

        lblCustomer = new JLabel();
        pnCustomerSelection.add(lblCustomer, "cell 0 1,alignx trailing");

        listCustomer = new JList<Customer>(Repository.getInstance().getCustomerPMod().getCustomerListModel());

        listCustomer.setCellRenderer(new CustomerListRenderer());
        listCustomer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPaneCustomerList = new JScrollPane(listCustomer);

        scrollPaneCustomerList.setMinimumSize(new Dimension(200, 100));
        scrollPaneCustomerList.setPreferredSize(new Dimension(200, 100));

        pnCustomerSelection.add(scrollPaneCustomerList, "cell 1 1 2 1,grow");
    }

    private void updateCustomerSelectionSection() {
        if (getReferenceObject() != null) {
            txtCustomerSearch.setText(getReferenceObject().getCustomer().getFullName());
        }
        listCustomer.setSelectedIndex(0);
        updateMakeLoanButtonVisibility();
    }

    private void createLoanOverviewSection(JPanel loanOverviewTable) {
        pnLoanOverview = new JPanel();
        loanOverviewTable.add(pnLoanOverview, "cell 0 2,grow");
        pnLoanOverview.setLayout(new MigLayout("", "[][][][grow]", "[grow][][][]"));

        lblNumberOfLoans = new JLabel();
        pnLoanOverview.add(lblNumberOfLoans, "cell 0 0");

        lblValNumberOfLoans = new JLabel("1");
        pnLoanOverview.add(lblValNumberOfLoans, "cell 1 0,alignx leading");

        lblFine = new JLabel("");
        pnLoanOverview.add(lblFine, "cell 3 0,alignx right");

        LoanDetailTableModel loanDetailTableModel = pMod.getLoanDetailTableModel();
        tblLoans = new JTable(loanDetailTableModel);
        tblLoans.setDefaultRenderer(Date.class, new DueDateTableCellRenderer());

        JScrollPane scrollPane = new JScrollPane(tblLoans);
        scrollPane.setMaximumSize(new Dimension(32767, 75));
        scrollPane.setMinimumSize(new Dimension(23, 75));
        pnLoanOverview.add(scrollPane, "cell 0 1 4 1,grow");

        JPanel pnReturnLoan = new JPanel();
        pnLoanOverview.add(pnReturnLoan, "cell 0 2 4 1,grow");
        pnReturnLoan.setLayout(new MigLayout("", "[][][][]", "[][]"));

        lblCopyStatus = new JLabel("Status:");
        pnReturnLoan.add(lblCopyStatus, "cell 0 0");

        btnReturnButton = new JButton();
        pnReturnLoan.add(btnReturnButton, "cell 2 0");
        btnReturnButton.setEnabled(false);

        comboCondition = new JComboBox<Condition>(copyPMod.getCopyStatusModel());
        comboCondition.setRenderer(new ConditionListCellRenderer());
        pnReturnLoan.add(comboCondition, "cell 1 0");

        lblReturnFeedbackLabel = new JLabel();
        pnReturnLoan.add(lblReturnFeedbackLabel, "cell 3 0");
    }

    private void updateLoanOverViewSection() {
        String overViewTitle = "";
        Customer customer;

        customer = getCurrentCustomer();
        if (customer != null) {
            // update Table
            pMod.getLoanDetailTableModel().updateLoans(customer);
            int curLoanIndex = pMod.getLoanDetailTableModel().getRowIndex(getReferenceObject());
            // set selection
            if (curLoanIndex >= 0) {
                tblLoans.getSelectionModel().setSelectionInterval(curLoanIndex, curLoanIndex);
            }

            // update border
            overViewTitle = Texts.get("LoanDetailMainViewBase.loansOverview.title") + " " + customer.getFullName();
        } else {
            pMod.getLoanDetailTableModel().updateLoans((Customer) null);
            overViewTitle = Texts.get("LoanDetailMainViewBase.loansOverview.titleAlone");
        }
        // update statistic
        List<Loan> openLoans = Repository.getInstance().getCustomerPMod().getCustomerOpenLoans(customer);
        lblValNumberOfLoans.setText("" + openLoans.size());
        pnLoanOverview.setBorder(new TitledBorder(null, overViewTitle, TitledBorder.LEADING, TitledBorder.TOP, null, null));

    }

    private void createNewLoanSection(JPanel newLoanSelectionPanel) {
        pnNewLoan = new JPanel();
        newLoanSelectionPanel.add(pnNewLoan, "cell 0 1,growx,aligny top");
        pnNewLoan.setLayout(new MigLayout("", "[][][][]", "[][][][][][][]"));

        lblCopyId = new JLabel();
        pnNewLoan.add(lblCopyId, "cell 0 0,grow");

        txtCopyId = new JNumberTextField();
        txtCopyId.setMaximumSize(new Dimension(40, 20));
        txtCopyId.setPreferredSize(new Dimension(40, 20));
        txtCopyId.setMinimumSize(new Dimension(40, 20));
        pnNewLoan.add(txtCopyId, "cell 1 0,alignx left,growy");
        txtCopyId.setColumns(10);

        btnCreateLoan = new JButton();
        pnNewLoan.add(btnCreateLoan, "cell 2 0,alignx left,growy");

        lblCopyTitle = new JLabel();
        pnNewLoan.add(lblCopyTitle, "cell 0 1,grow");

        lblValCopyTitle = new JLabel();
        // lblCopyDescription.setMaximumSize(new Dimension(250, 20));
        pnNewLoan.add(lblValCopyTitle, "cell 1 1 3,alignx left,growy");

        btnLinkToLoan = new JButton("");
        btnLinkToLoan.setVisible(false);
        pnNewLoan.add(btnLinkToLoan, "cell 3 3,alignx left");

        lblCondition = new JLabel();
        pnNewLoan.add(lblCondition, "cell 0 2,alignx left,growy");

        lblConditionValue = new JLabel();
        pnNewLoan.add(lblConditionValue, "cell 1 2,alignx left,growy");

        lblAvailabilityValue = new JLabel();
        pnNewLoan.add(lblAvailabilityValue, "cell 1 3 2 1,alignx left,growy");

        lblAvailability = new JLabel("");
        pnNewLoan.add(lblAvailability, "cell 0 3");
    }

    private Condition getSelectedCondition() {
        return (Condition) comboCondition.getSelectedItem();
    }

    private void clearNewLoanSection() {
        txtCopyId.setText("");
        currentSelectedCopy = null;
        updateNewLoanSection();
    }

    private void updateNewLoanSection() {

        btnLinkToLoan.setVisible(false);
        if (currentSelectedCopy == null) {
            emptyNewLoanSection();
        } else {
            String titleName = currentSelectedCopy.getTitle().getName();
            lblValCopyTitle.setText(titleName);
            // set it as title as well in case the content is too long
            lblValCopyTitle.setToolTipText(titleName);
            lblConditionValue.setText(currentSelectedCopy.getCondition().name());
            lblConditionValue.setIcon(IconUtil.loadIcon(currentSelectedCopy.getCondition().getIcon()));
            // lblLoanStatus
            updateNewLoanStatus();
        }

        updateMakeLoanButtonVisibility();
    }

    private void updateNewLoanStatus() {
        boolean isCopyLent = Repository.getInstance().getCopyPMod().isCopyLent(currentSelectedCopy);
        if (currentSelectedCopy.getCondition().equals(Condition.LOST)) {
            lblAvailabilityValue.setText(Texts.get("validation.copy.lost"));
            lblAvailabilityValue.setIcon(IconUtil.loadIcon("warning.png"));
        } else {
            if (!isCopyLent) {
                lblAvailabilityValue.setIcon(IconUtil.loadIcon("check.png"));
                lblAvailabilityValue.setText(Texts.get("validation.copy.isLent.false"));
            } else {
                String lender = copyPMod.getLender(currentSelectedCopy).getFullName();
                lblAvailabilityValue.setText(Texts.get("validation.copy.isLent.true") + lender + ".");
                lblAvailabilityValue.setIcon(IconUtil.loadIcon("warning.png"));
                btnLinkToLoan.setVisible(true);
            }
        }
    }

    private void emptyNewLoanSection() {
        lblValCopyTitle.setText("");
        lblConditionValue.setText("");
        lblConditionValue.setIcon(null);
        lblAvailabilityValue.setIcon(null);
        lblAvailabilityValue.setText("");
    }

    private void updateMakeLoanButtonVisibility() {
        boolean hasErrors = true;
        if (currentSelectedCopy != null) {
            ValidationResult validation = getController().validateLoan(currentSelectedCopy, getCurrentCustomer());
            hasErrors = validation.hasErrors();
        }
        btnCreateLoan.setEnabled(!hasErrors);
        //
        if (getCurrentCustomer() != null) {
            int overdueLoans = getController().getOverdueLoans(getCurrentCustomer());
            if (overdueLoans > 0) {
                int fine = overdueLoans * Loan.FINE_PER_BOOK;
                logger.info("Customer {} has to pay CHF {} fine.", getCurrentCustomer().getFullName(), fine);
                lblFine.setText(Texts.get("LoanDetailMainViewBase.loansOverview.fine", fine));
                lblFine.setIcon(IconUtil.loadIcon("warning.png"));
            } else {
                lblFine.setText("");
                lblFine.setIcon(null);
            }
        }
    }

    @Override
    protected LoanDetailController initController() {
        return new LoanDetailController();
    }

    @Override
    protected void initListeners() {

        new EnableCompontentOnTableSelectionListener(tblLoans, btnReturnButton);
        new EnableCompontentOnTableSelectionListener(tblLoans, comboCondition, true);

        initKeyListeners();

        getContainer().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                lblReturnFeedbackLabel.setText("");
            }
        });

        tblLoans.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                Long copyId = pMod.getLoanDetailTableModel().getCopyOfRow(tblLoans.getSelectedRow());
                if (copyId != null) {
                    Copy copy = copyPMod.searchCopy(copyId);
                    comboCondition.setSelectedItem(copy.getCondition());
                }
            }
        });

        btnReturnButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                returnCopy();
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
                if (!txtCustomerSearch.getText().equals(defaultSearchValue)) {
                    logger.debug("search for value {}", txtCustomerSearch.getText());
                    getController().filterCustomers(txtCustomerSearch.getText());
                    if (listCustomer.getModel().getSize() > 0) {
                        txtCustomerSearch.setBackground(Color.WHITE);
                    } else {
                        txtCustomerSearch.setBackground(Color.RED);
                    }
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

        Repository.getInstance().getCustomerPMod().getCustomerListModel().addListDataListener(new ListDataListener() {

            @Override
            public void intervalRemoved(ListDataEvent arg0) {
            }

            @Override
            public void intervalAdded(ListDataEvent arg0) {
            }

            @Override
            public void contentsChanged(ListDataEvent arg0) {
                updateList(arg0);
            }

            private void updateList(ListDataEvent e) {
                // bit hackish.... don't update if only one cell was updated
                if (!(e.getIndex0() != 0 && e.getIndex0() == e.getIndex1())) {
                    // call later, cause no mutation allowed in Listener
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            // update ReferencedObject
                            listCustomer.setSelectedIndex(0);
                            Loan firstLoan = pMod.getFirstLoanOfCustomer(listCustomer.getSelectedValue());
                            switchToLoan(firstLoan, false);
                        }
                    });
                }
            }

        });

        listCustomer.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                Loan firstLoanOfCustomer = pMod.getFirstLoanOfCustomer(listCustomer.getSelectedValue());
                switchToLoan(firstLoanOfCustomer, false);
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
                    currentSelectedCopy = copyPMod.searchCopy(copyId);
                    updateNewLoanSection();
                    updateMakeLoanButtonVisibility();
                    if (currentSelectedCopy == null) {
                        txtCopyId.setBackground(Color.RED);
                    } else {
                        txtCopyId.setBackground(Color.WHITE);
                    }
                }
            }
        });

        btnLinkToLoan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (copyPMod.isCopyLent(currentSelectedCopy)) {
                    switchToLoan(copyPMod.getCurrentLoan(currentSelectedCopy), true);
                }
            }
        });
    }

    private void initKeyListeners() {
        // make loan on enter key
        txtCopyId.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        txtCopyId.getActionMap().put("Enter", new AbstractAction() {
            private static final long serialVersionUID = -5664120575484177305L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnCreateLoan.isEnabled()) {
                    saveLoan();
                }
            }
        });

        // Return book on enter key
        tblLoans.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        tblLoans.getActionMap().put("Enter", new AbstractAction() {
            private static final long serialVersionUID = -5664120575484177305L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnReturnButton.isEnabled()) {
                    returnCopy();
                }
            }
        });
    }

    private Customer getCurrentCustomer() {
        if (getReferenceObject() != null) {
            return getReferenceObject().getCustomer();
        }
        return listCustomer.getSelectedValue();
    }

    private void saveLoan() {
        if (currentSelectedCopy == null) {
            lblAvailabilityValue.setText(Texts.get("LoanDetailMainViewBase.newLoan.noInventoryNumber"));
        } else {
            ValidationResult validationResult = getController().validateLoan(currentSelectedCopy, getCurrentCustomer());
            if (!validationResult.hasErrors()) {
                getController().saveLoan(currentSelectedCopy, getCurrentCustomer());
                txtCopyId.setText("");
                currentSelectedCopy = null;
            }
        }
        updateNewLoanSection();
    }

    private void returnCopy() {
        List<Long> returnedCopies;
        Condition newCondition = null;
        int[] selectedRows = tblLoans.getSelectedRows();
        if (tblLoans.getSelectedRowCount() == 1) {
            newCondition = getSelectedCondition();
            getController().changeCondition(tblLoans.getSelectedRow(), newCondition);
        }
        returnedCopies = getController().returnCopies(selectedRows);
        String key = "LoanDetailMainViewBase.loansOverview.returnMessage";
        if (returnedCopies.size() > 1) {
            key += "s";
        }
        String message = Texts.get(key, Joiner.on(", ").join(returnedCopies));
        if (newCondition != null) {
            message = message + " " + Texts.get("LoanDetailMainViewBase.loansOverview.conditionChanged", Texts.get(newCondition.getKey()));
        }
        lblReturnFeedbackLabel.setText(message);
        updateLoanOverViewSection();
        updateMakeLoanButtonVisibility();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o.getClass().equals(CopyPMod.class)) {
            updateNewLoanSection();
            updateLoanOverViewSection();

            Long copyId = pMod.getLoanDetailTableModel().getCopyOfRow(tblLoans.getSelectedRow());
            if (copyId != null) {
                Copy copy = copyPMod.searchCopy(copyId);
                comboCondition.setSelectedItem(copy.getCondition());
            }
        } else {
            super.update(o, arg);
        }
    }
}
