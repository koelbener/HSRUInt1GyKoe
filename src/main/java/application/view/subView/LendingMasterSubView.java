package application.view.subView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.controller.LendingMasterController;
import application.core.Repository;
import application.core.Texts;
import application.presentationModel.LoansPMod;
import application.view.helper.EnableCompontentOnTableSelectionListener;
import application.view.helper.HideTextOnFocusListener;
import application.viewModel.LoanSearchFilterComboBoxModel;
import domain.Library;

public class LendingMasterSubView extends SubViewBase<Library, LendingMasterController> {

    private static final Logger logger = LoggerFactory.getLogger(LendingMasterSubView.class);

    public static final String NAME_BUTTON_OPEN = "button.open";
    public static final String NAME_BUTTON_NEW = "button.new";
    public static final String NAME_TABLE_LOANS = "table.loans";
    public static final String NAME_LABEL_NUMBER_OF_BOOKS = "label.numberOfBooks";
    public static final String NAME_SEARCH_FIELD = "textField.search";
    public static final String NAME_COMBOBOX_FILTER = "comboBox.searchFilter";

    public static String searchDefaultText;

    private JPanel inventoryPanel;
    private JPanel statisticsPanel;

    private JLabel lblNumberOfLoans;
    private JLabel lblCurrentLoans;
    private JLabel lblOverdueLoans;

    private JTextField txtSearch;
    private JComboBox<LoanSearchFilterComboBoxModel.FilterOption> comboBoxSearchFilter;
    private JButton btnNew;
    private JButton btnOpen;
    private LoansPMod loansPMod;
    private JTable loansTable;

    private JLabel valNumberOfLoans;
    private JLabel valCurrentLoans;
    private JLabel valOverdueLoans;
    private HideTextOnFocusListener hideTextOnFocusListener;

    public LendingMasterSubView(Library referenceObject) {
        super(referenceObject);
    }

    /**
     * @wbp.parser.entryPoint
     */
    @Override
    public void initUIElements() {
        container.setLayout(new BorderLayout());
        statisticsPanel = new JPanel();
        container.add(statisticsPanel, BorderLayout.NORTH);

        statisticsPanel.setLayout(new MigLayout("", "[][][][][][][][]", "[]"));

        lblNumberOfLoans = new JLabel();
        statisticsPanel.add(lblNumberOfLoans, "cell 0 0");

        valNumberOfLoans = new JLabel();
        statisticsPanel.add(valNumberOfLoans, "cell 1 0");

        lblCurrentLoans = new JLabel();
        statisticsPanel.add(lblCurrentLoans, "cell 3 0");

        valCurrentLoans = new JLabel();
        statisticsPanel.add(valCurrentLoans, "cell 4 0");

        lblOverdueLoans = new JLabel();
        statisticsPanel.add(lblOverdueLoans, "cell 6 0");

        valOverdueLoans = new JLabel();
        statisticsPanel.add(valOverdueLoans, "cell 7 0");

        updateStatistics();

        inventoryPanel = new JPanel();
        container.add(inventoryPanel, BorderLayout.CENTER);
        inventoryPanel.setLayout(new BorderLayout(0, 0));

        JPanel searchPanel = new JPanel();
        inventoryPanel.add(searchPanel, BorderLayout.NORTH);
        searchPanel.setLayout(new MigLayout("", "[grow]", "[]"));

        txtSearch = new JTextField();
        searchPanel.add(txtSearch, "flowx,cell 0 0,growx");
        txtSearch.setColumns(10);

        comboBoxSearchFilter = new JComboBox<LoanSearchFilterComboBoxModel.FilterOption>(Repository.getInstance().getLoansPMod().getSearchFilterModel());
        comboBoxSearchFilter.setName(NAME_COMBOBOX_FILTER);
        searchPanel.add(comboBoxSearchFilter, "cell 0 0");

        JPanel panel_13 = new JPanel();
        panel_13.setLayout(new MigLayout("", "[]", "[23px][]"));
        inventoryPanel.add(panel_13, BorderLayout.EAST);

        btnNew = new JButton();
        btnOpen = new JButton();
        btnOpen.setEnabled(false);
        panel_13.add(btnNew, "cell 0 0,growx,aligny center");
        panel_13.add(btnOpen, "cell 0 1,growx,aligny center");

        JScrollPane scrollPane_1 = new JScrollPane();
        inventoryPanel.add(scrollPane_1, BorderLayout.CENTER);

        loansTable = new JTable(loansPMod.getLoanTableModel());
        loansTable.setName(NAME_TABLE_LOANS);
        loansTable.setRowSorter(loansPMod.getLoanTableRowSorter());
        loansTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane_1.setViewportView(loansTable);

    }

    @Override
    protected void setTexts() {
        // panel titles
        statisticsPanel.setBorder(new TitledBorder(null, Texts.get("LendingMasterMainView.statisticsPanel.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        inventoryPanel.setBorder(new TitledBorder(null, Texts.get("LendingMasterMainView.inventoryPanel.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

        // components
        lblNumberOfLoans.setText(Texts.get("LendingMasterMainView.lblAnzahlAusleihen.text"));
        lblCurrentLoans.setText(Texts.get("LendingMasterMainView.lblAktuellAusgeliehen.text"));
        lblOverdueLoans.setText(Texts.get("LendingMasterMainView.lblberflligeAusleihen.text"));

        searchDefaultText = Texts.get("LendingMasterMainView.searchDefault");
        txtSearch.setText(Texts.get("LendingMasterMainView.searchDefault"));
        if (hideTextOnFocusListener != null)
            hideTextOnFocusListener.updateText(searchDefaultText);

        btnNew.setText(Texts.get("LendingMasterMainView.btnNew.text"));
        btnOpen.setText(Texts.get("LendingMasterMainView.btnOpen.text"));

        // Tooltips
        btnNew.setToolTipText(Texts.get("LendingMasterMainView.btnNew.toolTipText"));
        txtSearch.setToolTipText(Texts.get("LendingMasterMainView.searchToolTip"));

        // table
        loansPMod.getLoanTableModel().setColumns();
        loansPMod.getLoanTableModel().updateStateValues();

        container.revalidate();
    }

    @Override
    protected void initModel() {
        super.initModel();
        loansPMod = Repository.getInstance().getLoansPMod();
        loansPMod.addObserver(this);
    }

    @Override
    protected LendingMasterController initController() {
        return new LendingMasterController();
    }

    @Override
    protected void initListeners() {

        new EnableCompontentOnTableSelectionListener(loansTable, btnOpen);

        hideTextOnFocusListener = new HideTextOnFocusListener(txtSearch, searchDefaultText);

        btnNew.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                getController().newLoan();
            }
        });

        btnOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                getController().openLoans(loansTable.getSelectedRows());
            }
        });

        comboBoxSearchFilter.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                getController().filterLoans((LoanSearchFilterComboBoxModel.FilterOption) comboBoxSearchFilter.getSelectedItem());
            }
        });

        loansTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    logger.trace("doubleClick detected");
                    getController().openLoans(new int[] { loansTable.rowAtPoint(e.getPoint()) });
                }
            }
        });

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
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

        });

    }

    private void search() {
        if (!txtSearch.getText().equals(searchDefaultText)) {
            getController().filterLoans(txtSearch.getText());
        } else {
            getController().filterLoans("");
        }
    }

    @Override
    public void update(Observable observable, Object arg1) {
        if (observable instanceof LoansPMod) {
            updateStatistics();
        } else {
            super.update(observable, arg1);
        }
    }

    private void updateStatistics() {
        valNumberOfLoans.setText(String.valueOf(loansPMod.getLoansCount()));
        valCurrentLoans.setText(String.valueOf(loansPMod.getCurrentLoansCount()));
        valOverdueLoans.setText(String.valueOf(loansPMod.getOverdueLoansCount()));
    }

}