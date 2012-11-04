package application.view.subView;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.controller.LendingMasterController;
import application.core.Texts;
import domain.Library;

public class LendingMasterSubView extends SubViewBase<Library, LendingMasterController> {

    public static final String NAME_BUTTON_OPEN = "button.open";
    public static final String NAME_BUTTON_NEW = "button.new";
    public static final String NAME_TABLE_BOOKS = "table.books";
    public static final String NAME_LABEL_NUMBER_OF_BOOKS = "label.numberOfBooks";
    public static final String NAME_SEARCH_FIELD = "textField.search";
    public static final String NAME_COMBOBOX_FILTER = "comboBox.searchFilter";

    private static final Logger logger = LoggerFactory.getLogger(LendingMasterSubView.class);

    public static String searchDefaultText;

    private JPanel inventoryPanel;
    private JPanel statisticsPanel;

    private JLabel lblAnzahlAusleihen;
    private JLabel lblAktuellAusgeliehen;
    private JLabel lblberflligeAusleihen;

    private JTextField txtSearch;
    private JCheckBox chbkOnlyOverdue;
    private JButton btnNew;

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

        lblAnzahlAusleihen = new JLabel();
        statisticsPanel.add(lblAnzahlAusleihen, "cell 0 0");

        JLabel label = new JLabel("0");
        statisticsPanel.add(label, "cell 1 0");

        lblAktuellAusgeliehen = new JLabel();
        statisticsPanel.add(lblAktuellAusgeliehen, "cell 3 0");

        JLabel label_1 = new JLabel("0");
        statisticsPanel.add(label_1, "cell 4 0");

        lblberflligeAusleihen = new JLabel();
        statisticsPanel.add(lblberflligeAusleihen, "cell 6 0");

        JLabel label_2 = new JLabel("0");
        statisticsPanel.add(label_2, "cell 7 0");

        inventoryPanel = new JPanel();
        container.add(inventoryPanel, BorderLayout.CENTER);
        inventoryPanel.setLayout(new BorderLayout(0, 0));

        JPanel searchPanel = new JPanel();
        inventoryPanel.add(searchPanel, BorderLayout.NORTH);
        searchPanel.setLayout(new MigLayout("", "[grow]", "[]"));

        txtSearch = new JTextField();
        txtSearch.setText("Suche..");
        searchPanel.add(txtSearch, "flowx,cell 0 0,growx");
        txtSearch.setColumns(10);

        chbkOnlyOverdue = new JCheckBox();
        searchPanel.add(chbkOnlyOverdue, "cell 0 0");

        JPanel panel_13 = new JPanel();
        inventoryPanel.add(panel_13, BorderLayout.EAST);

        btnNew = new JButton();
        panel_13.add(btnNew);

        JScrollPane scrollPane_1 = new JScrollPane();
        inventoryPanel.add(scrollPane_1, BorderLayout.CENTER);

        JTable table = new JTable();
        scrollPane_1.setViewportView(table);
    }

    @Override
    protected void setTexts() {
        // panel titles
        statisticsPanel.setBorder(new TitledBorder(null, Texts.get("LendingMasterMainView.statisticsPanel.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        inventoryPanel.setBorder(new TitledBorder(null, Texts.get("LendingMasterMainView.inventoryPanel.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

        // components
        lblAnzahlAusleihen.setText(Texts.get("LendingMasterMainView.lblAnzahlAusleihen.text"));
        lblAktuellAusgeliehen.setText(Texts.get("LendingMasterMainView.lblAktuellAusgeliehen.text"));
        lblberflligeAusleihen.setText(Texts.get("LendingMasterMainView.lblberflligeAusleihen.text"));

        txtSearch.setText(Texts.get("LendingMasterMainView.searchDefault"));
        chbkOnlyOverdue.setText(Texts.get("LendingMasterMainView.chbkOnlyOverdue.text"));
        btnNew.setText(Texts.get("LendingMasterMainView.btnNew.text"));
        searchDefaultText = Texts.get("LendingMasterMainView.searchDefault");

        // Tooltips
        btnNew.setToolTipText(Texts.get("LendingMasterMainView.btnNew.toolTipText"));

        // table

        container.revalidate();
    }

    @Override
    protected void initModel() {
        super.initModel();
    }

    @Override
    protected LendingMasterController initController() {
        return new LendingMasterController();
    }

    @Override
    protected void initListeners() {
    }

}