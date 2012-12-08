package application.view.mainView.dialogView;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;
import application.controller.ReturnLoansController;
import application.core.Repository;
import application.core.Texts;
import application.util.IconUtil;
import application.view.component.JNumberTextField;
import application.view.helper.EnableCompontentOnTableSelectionListener;

public class ReturnLoansMainView extends DialogViewBase<Object, ReturnLoansController> {
    private JNumberTextField txtLoanSearch;
    private JTable tblLoansToReturn;
    private JLabel lblLoanFeedback;
    private JButton btnAddLoan;
    private JButton btnReturnLoans;
    private JPanel mainPanel;
    private JButton btnRemoveLoan;

    public ReturnLoansMainView(Object referenceObject, String icon) {
        super(referenceObject, icon);
    }

    @Override
    protected void addObservables() {
    }

    /**
     * @wbp.parser.entryPoint
     */
    @Override
    protected void initUIElements() {
        super.initUIElements();

        Container contentPane = getContainer().getContentPane();
        getContainer().setBounds(100, 100, 600, 300);
        getContainer().setMinimumSize(new Dimension(600, 300));

        mainPanel = new JPanel();
        contentPane.add(mainPanel);
        mainPanel.setLayout(new MigLayout("", "[grow][][]", "[][grow][]"));

        txtLoanSearch = new JNumberTextField();
        mainPanel.add(txtLoanSearch, "cell 0 0,growx");
        txtLoanSearch.setColumns(10);

        lblLoanFeedback = new JLabel();
        mainPanel.add(lblLoanFeedback, "cell 1 0");

        btnAddLoan = new JButton();
        mainPanel.add(btnAddLoan, "cell 2 0,alignx left,aligny baseline");

        tblLoansToReturn = new JTable(Repository.getInstance().getLoansPMod().getBatchReturnLoansTableModel());
        tblLoansToReturn.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainPanel.add(new JScrollPane(tblLoansToReturn), "cell 0 1 2 1,grow");

        btnRemoveLoan = new JButton("");
        mainPanel.add(btnRemoveLoan, "cell 0 2,aligny bottom");

        btnReturnLoans = new JButton();
        mainPanel.add(btnReturnLoans, "cell 2 2");

        btnAddLoan.setEnabled(false);
        btnReturnLoans.setEnabled(false);
        btnRemoveLoan.setEnabled(false);
    }

    @Override
    protected void setTexts() {
        btnReturnLoans.setText(Texts.get("ReturnLoansMainView.button.returnLoans"));
        btnAddLoan.setText(Texts.get("ReturnLoansMainView.button.addLoan"));
        btnRemoveLoan.setText(Texts.get("ReturnLoansMainView.button.removeLoan"));

        getContainer().setTitle(Texts.get("ReturnLoansMainView.frametitle"));
        mainPanel.setBorder(new TitledBorder(null, Texts.get("ReturnLoansMainView.frametitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
    }

    private void setBtnReturnLoansStatus() {
        if (Repository.getInstance().getLoansPMod().getBatchReturnLoansTableModel().getRowCount() == 0) {
            btnReturnLoans.setEnabled(false);
        } else {
            btnReturnLoans.setEnabled(true);
        }
    }

    @Override
    protected void initListeners() {

        new EnableCompontentOnTableSelectionListener(tblLoansToReturn, btnRemoveLoan, true);

        btnRemoveLoan.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                getController().removeLoan(tblLoansToReturn.getSelectedRow());
                setBtnReturnLoansStatus();
            }
        });

        txtLoanSearch.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent arg0) {
                searchLoan();
            }

            @Override
            public void insertUpdate(DocumentEvent arg0) {
                searchLoan();
            }

            @Override
            public void removeUpdate(DocumentEvent arg0) {
                searchLoan();
            }

            private void searchLoan() {
                boolean hasLoan = false;
                Long copyId = txtLoanSearch.getNumber();
                if (copyId != null) {
                    hasLoan = Repository.getInstance().getLoansPMod().hasOpenLoan(copyId);
                }
                if (hasLoan) {
                    txtLoanSearch.setBackground(Color.GREEN);
                } else if (txtLoanSearch.getNumber() == null) {
                    txtLoanSearch.setBackground(Color.WHITE);
                } else {
                    txtLoanSearch.setBackground(Color.RED);
                }
                updateLoanFeedbackLabel(hasLoan);
                updateAddLoanButton(hasLoan);
            }

        });

        btnAddLoan.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                getController().addLoan(txtLoanSearch.getNumber());
                setBtnReturnLoansStatus();
            }
        });
    }

    private void updateAddLoanButton(boolean hasLoan) {
        if (hasLoan) {
            btnAddLoan.setEnabled(true);
        } else {
            btnAddLoan.setEnabled(false);
        }
    }

    private void updateLoanFeedbackLabel(boolean hasLoan) {
        if (hasLoan) {
            lblLoanFeedback.setIcon(IconUtil.loadIcon("check.png"));
        } else {
            lblLoanFeedback.setIcon(IconUtil.loadIcon("warning.png"));
        }
    }

    @Override
    protected ReturnLoansController initController() {
        return new ReturnLoansController();
    }
}
