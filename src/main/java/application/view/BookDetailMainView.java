package application.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import application.controller.BookDetailController;
import application.viewModel.CopyListModel;
import domain.Book;

public class BookDetailMainView extends MainViewBase {

    private static final long serialVersionUID = 1L;
    private JTextField tfTitle;
    private JTextField tfAutor;
    private JTextField tfVerlag;
    private JTextField tfRegal;
    private JTextField tfNumberOfCopies;
    private CopyListModel copyListModel;
    private final Book book;

    public BookDetailMainView(Book book) {
        super();
        this.book = book;
        updateValues();
    }

    @Override
    protected void initModel() {
        super.initModel();
        copyListModel = new CopyListModel(library.getCopiesOfBook(book));
    }

    private void updateValues() {
        tfTitle.setText(book.getName());
        tfAutor.setText(book.getAuthor());
        tfVerlag.setText(book.getPublisher());
        tfRegal.setText(book.getShelf().toString());
        tfNumberOfCopies.setText(String.valueOf(copyListModel.getSize()));
    }

    /**
     * Initialize the contents of the frame.
     */
    @Override
    protected void initUIElements() {
        super.initUIElements();
        setTitle("Buch Detailansicht");
        setBounds(100, 100, 450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Buch Informationen", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new MigLayout("", "[][grow]", "[][][][]"));

        JLabel lblTitel = new JLabel("Titel");
        panel.add(lblTitel, "cell 0 0,alignx trailing");

        tfTitle = new JTextField();
        panel.add(tfTitle, "cell 1 0,growx");
        tfTitle.setColumns(10);

        JLabel lblAutor = new JLabel("Autor");
        panel.add(lblAutor, "cell 0 1,alignx trailing");

        tfAutor = new JTextField();
        panel.add(tfAutor, "cell 1 1,growx");
        tfAutor.setColumns(10);

        JLabel lblVerlag = new JLabel("Verlag");
        panel.add(lblVerlag, "cell 0 2,alignx trailing");

        tfVerlag = new JTextField();
        panel.add(tfVerlag, "cell 1 2,growx");
        tfVerlag.setColumns(10);

        JLabel lblRegal = new JLabel("Regal");
        panel.add(lblRegal, "cell 0 3,alignx trailing");

        tfRegal = new JTextField();
        panel.add(tfRegal, "cell 1 3,growx");
        tfRegal.setColumns(10);

        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new TitledBorder(null, "Exemplare", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        getContentPane().add(panel_1, BorderLayout.CENTER);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_2 = new JPanel();
        panel_1.add(panel_2, BorderLayout.NORTH);
        panel_2.setLayout(new MigLayout("", "[][grow][][]", "[]"));

        JLabel lblAnzahl = new JLabel("Anzahl");
        panel_2.add(lblAnzahl, "cell 0 0,alignx trailing");

        tfNumberOfCopies = new JTextField();
        tfNumberOfCopies.setEnabled(false);
        panel_2.add(tfNumberOfCopies, "flowx,cell 1 0");
        tfNumberOfCopies.setColumns(10);

        JButton btnEntfernen = new JButton("Entfernen");
        btnEntfernen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        panel_2.add(btnEntfernen, "cell 2 0");

        JButton btnHinzufgen = new JButton("Hinzuf\u00FCgen");
        panel_2.add(btnHinzufgen, "cell 3 0");

        JPanel panel_3 = new JPanel();
        panel_1.add(panel_3, BorderLayout.CENTER);
        panel_3.setLayout(new BorderLayout(0, 0));

        JList list = new JList();
        list.setModel(copyListModel);
        panel_3.add(list);
    }

    @Override
    public void initController() {
        controller = new BookDetailController();

    }

    @Override
    public BookDetailController getController() {
        return (BookDetailController) controller;
    }

    @Override
    public void initListeners() {
        // TODO Auto-generated method stub

    }

}
