package application.view.mainView.dialogView;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;
import application.controller.ReturnLoansController;
import application.core.Repository;
import application.core.Texts;
import application.presentationModel.componentModel.BatchReturnLoansTableModel;
import application.util.IconUtil;
import application.view.component.JNumberTextField;
import application.view.helper.EnableCompontentOnTableSelectionListener;

import com.toedter.calendar.JDateChooser;

import domain.Loan;

public class ReturnLoansMainView extends DialogViewBase<Object, ReturnLoansController> implements Observer {
    private JNumberTextField txtLoanSearch;
    private JTable tblLoansToReturn;
    private JLabel lblLoanFeedback;
    private JButton btnAddLoan;
    private JButton btnReturnLoans;
    private JPanel mainPanel;
    private JButton btnRemoveLoan;
    private JDateChooser dtDate;
    private String txtLoanSearchDefault;
    private JLabel lblDate;

    public ReturnLoansMainView(Object referenceObject, String icon) {
        super(referenceObject, icon);
    }

    @Override
    protected void addObservables() {
        observables.add(Repository.getInstance().getLoansPMod());
        observables.add(Repository.getInstance().getBooksPMod());
    }

    /**
     * @wbp.parser.entryPoint
     */
    @Override
    protected void initUIElements() {
        super.initUIElements();
        txtLoanSearchDefault = Texts.get("ReturnLoansMainView.button.txtLoanSearchDefault");

        Container contentPane = getContainer().getContentPane();
        getContainer().setBounds(100, 100, 700, 400);
        getContainer().setMinimumSize(new Dimension(700, 400));

        mainPanel = new JPanel();
        contentPane.add(mainPanel);
        mainPanel.setLayout(new MigLayout("", "[322.00,grow][-10.00][][]", "[][grow][]"));

        txtLoanSearch = new JNumberTextField(txtLoanSearchDefault);
        mainPanel.add(txtLoanSearch, "cell 0 0 3 1,growx");
        txtLoanSearch.setColumns(10);

        lblLoanFeedback = new JLabel();
        mainPanel.add(lblLoanFeedback, "cell 2 0");

        btnAddLoan = new JButton();
        mainPanel.add(btnAddLoan, "cell 3 0,alignx left,aligny baseline");

        tblLoansToReturn = new JTable(Repository.getInstance().getLoansPMod().getBatchReturnLoansTableModel());
        tblLoansToReturn.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainPanel.add(new JScrollPane(tblLoansToReturn), "cell 0 1 3 1,grow");

        btnRemoveLoan = new JButton("");
        mainPanel.add(btnRemoveLoan, "flowx,cell 0 2,aligny bottom");

        lblDate = new JLabel("");
        mainPanel.add(lblDate, "cell 1 2");

        btnReturnLoans = new JButton();
        mainPanel.add(btnReturnLoans, "cell 3 2");

        btnAddLoan.setEnabled(false);
        btnReturnLoans.setEnabled(false);
        btnRemoveLoan.setEnabled(false);

        dtDate = new JDateChooser(new Date());
        dtDate.setMinimumSize(new Dimension(70, 20));
        mainPanel.add(dtDate, "cell 2 2,alignx right");

        getContainer().getRootPane().setDefaultButton(btnReturnLoans);
        setMinMaxDates();
        setBookTableColumnWidths();
    }

    private void setBookTableColumnWidths() {
        TableColumn column = null;
        for (int i = 0; i < 5; i++) {
            column = tblLoansToReturn.getColumnModel().getColumn(i);
            if (i == BatchReturnLoansTableModel.COLUMN_COPY_ID) {
                column.setPreferredWidth(45);
            } else if (i == BatchReturnLoansTableModel.COLUMN_ENDDATE) {
                column.setPreferredWidth(65);
            } else if (i == BatchReturnLoansTableModel.COLUMN_MESSAGE) {
                column.setPreferredWidth(160);
            } else {
                column.setPreferredWidth(70);
            }
        }
    }

    @Override
    protected void setTexts() {
        btnReturnLoans.setText(Texts.get("ReturnLoansMainView.button.returnLoans"));
        btnAddLoan.setText(Texts.get("ReturnLoansMainView.button.addLoan"));
        btnRemoveLoan.setText(Texts.get("ReturnLoansMainView.button.removeLoan"));
        lblDate.setText(Texts.get("ReturnLoansMainView.date.explanation"));

        dtDate.setLocale(Texts.getInstance().getCurrentLocale());
        txtLoanSearchDefault = Texts.get("ReturnLoansMainView.button.txtLoanSearchDefault");
        txtLoanSearch.setToolTipText(txtLoanSearchDefault);
        txtLoanSearch.setDefaultText(txtLoanSearchDefault);

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

        getContainer().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent arg0) {
                Repository.getInstance().getMainViewFactory().closeReturnLoansView();
            }
        });

        new EnableCompontentOnTableSelectionListener(tblLoansToReturn, btnRemoveLoan, true);

        btnReturnLoans.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getController().returnBooks(dtDate.getCalendar());
            }
        });

        btnRemoveLoan.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                getController().removeLoan(tblLoansToReturn.getSelectedRow());
                setBtnReturnLoansStatus();
                setMinMaxDates();
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
                    if (hasLoan) {
                        // if the loan already is in the table, don't add it again
                        for (Loan loan : Repository.getInstance().getLoansPMod().getBatchReturnLoansTableModel().getLoans()) {
                            if (loan.getCopy().getInventoryNumber() == copyId) {
                                hasLoan = false;
                                break;
                            }
                        }
                    }
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
                setMinMaxDates();
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        txtLoanSearch.setText(txtLoanSearch.getDefaultText());
                        txtLoanSearch.requestFocus();
                    }
                });
            }
        });

        initKeyListeners();
    }

    private void initKeyListeners() {
        txtLoanSearch.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        txtLoanSearch.getActionMap().put("enter", new AbstractAction() {
            private static final long serialVersionUID = -5664120575484177305L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnAddLoan.isEnabled()) {
                    for (ActionListener listener : btnAddLoan.getActionListeners()) {
                        listener.actionPerformed(null);
                    }
                }
            }
        });

        tblLoansToReturn.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
        tblLoansToReturn.getActionMap().put("delete", new AbstractAction() {
            private static final long serialVersionUID = -5664120575484177305L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnRemoveLoan.isEnabled()) {
                    for (ActionListener listener : btnRemoveLoan.getActionListeners()) {
                        listener.actionPerformed(null);
                    }
                }
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

    private void setMinMaxDates() {
        GregorianCalendar minDate = new GregorianCalendar();
        minDate.setTime(dtDate.getMinSelectableDate());
        for (Loan loan : Repository.getInstance().getLoansPMod().getBatchReturnLoansTableModel().getLoans()) {
            if (minDate == null || minDate.compareTo(loan.getPickupDate()) < 0) {
                minDate = loan.getPickupDate();
            }
        }
        if (minDate != null) {
            dtDate.setMinSelectableDate(minDate.getTime());
            if (minDate.compareTo(dtDate.getCalendar()) > 0) {
                dtDate.setDate(minDate.getTime());
            }
        }
        dtDate.setMaxSelectableDate(new Date());
    }

    @Override
    protected ReturnLoansController initController() {
        return new ReturnLoansController();
    }

    @Override
    public void update(Observable o, Object arg) {
        Repository.getInstance().getLoansPMod().getBatchReturnLoansTableModel().updateTableData();
        super.update(o, arg);
    }
}
