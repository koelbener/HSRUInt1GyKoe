package application.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import application.viewModel.BookListModel;
import domain.Library;

public class BookMaster {

    private JFrame frame;
    private JTextField txtSuche;
    private ListModel bookListModel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BookMaster window = new BookMaster();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public BookMaster() {
        initModel();
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 616, 445);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        frame.getContentPane().add(panel, BorderLayout.NORTH);

        JLabel lblSwingingLibrary = new JLabel("Swinging Library");
        panel.add(lblSwingingLibrary);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new CompoundBorder(new EmptyBorder(10, 5, 5, 5), new LineBorder(new Color(0, 0, 0), 1,
                true)));
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("B\u00FCcher", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_3 = new JPanel();
        panel_3.setBorder(new TitledBorder(null, "Inventar Statistiken", TitledBorder.LEADING, TitledBorder.TOP, null,
                null));
        panel_1.add(panel_3, BorderLayout.NORTH);
        panel_3.setLayout(new MigLayout("", "[][][fill][][]", "[]"));

        JLabel lblLasd = new JLabel("Anzahl B\u00FCcher:");
        panel_3.add(lblLasd, "cell 0 0");

        JLabel lblNewLabel = new JLabel("Nr");
        panel_3.add(lblNewLabel, "cell 1 0");

        JLabel lblAnzahlExemplare = new JLabel("Anzahl Exemplare");
        panel_3.add(lblAnzahlExemplare, "cell 3 0");

        JLabel lblNr = new JLabel("Nr");
        panel_3.add(lblNr, "cell 4 0");

        JPanel panel_4 = new JPanel();
        panel_4.setBorder(new TitledBorder(null, "Buch-Inventar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_1.add(panel_4, BorderLayout.CENTER);
        panel_4.setLayout(new BorderLayout(0, 0));

        JPanel panel_5 = new JPanel();
        panel_4.add(panel_5, BorderLayout.NORTH);
        panel_5.setLayout(new MigLayout("", "[grow][][][]", "[][]"));

        JLabel lblNewLabel_1 = new JLabel("Alle B\u00FCcher stehen in der untenstehenden Tabelle");
        panel_5.add(lblNewLabel_1, "cell 0 0");

        txtSuche = new JTextField();
        txtSuche.setText("Suche...");
        panel_5.add(txtSuche, "flowx,cell 0 1,growx");
        txtSuche.setColumns(10);

        JCheckBox checkBox = new JCheckBox("");
        panel_5.add(checkBox, "cell 0 1");

        JLabel lblNurVerfgbare = new JLabel("Nur Verf\u00FCgbare");
        panel_5.add(lblNurVerfgbare, "cell 1 1");

        JButton btnSelektierteAngaben = new JButton("Selektierte Anzeigen");
        panel_5.add(btnSelektierteAngaben, "cell 2 1,growx");

        JButton btnNeuesBuchHinzufgen = new JButton("Neues Buch hinzuf\u00FCgen");
        panel_5.add(btnNeuesBuchHinzufgen, "cell 3 1,growx");

        JPanel panel_6 = new JPanel();
        panel_4.add(panel_6, BorderLayout.CENTER);

        JList list = new JList();
        list.setModel(bookListModel);
        panel_6.add(list);

        JPanel panel_2 = new JPanel();
        tabbedPane.addTab("Ausleihen", null, panel_2, null);
    }

    private void initModel() {
        Library library = new Library();
        bookListModel = new BookListModel(library.getBooks());
    }

}
