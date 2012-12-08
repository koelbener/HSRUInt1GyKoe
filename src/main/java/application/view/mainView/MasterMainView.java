package application.view.mainView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;
import application.controller.MasterController;
import application.core.Language;
import application.core.Texts;
import application.util.IconUtil;
import application.view.subView.BookMasterSubView;
import application.view.subView.LendingMasterSubView;
import domain.Library;

public class MasterMainView extends MainViewBase<Library, MasterController, JFrame> {

    private JComboBox<Language> languageComboBox;
    private JTabbedPane tabbedPane;
    private JLabel lblSwingingLibrary;
    private JPanel pnMainPanel;

    public MasterMainView() {
        super(null);
        container.setMinimumSize(new Dimension(716, 445));
        setIcon("book.gif");
    }

    @Override
    protected void setTexts() {
        lblSwingingLibrary.setText(Texts.get("BookMasterMainView.lblSwingingLibrary.text"));
        // title
        container.setTitle(Texts.get("BookMasterMainView.this.title"));
        // Tabs
        tabbedPane.setTitleAt(0, Texts.get("BookMasterMainView.tab.books"));
        tabbedPane.setTitleAt(1, Texts.get("BookMasterMainView.tab.lending"));

    }

    @Override
    /**
     * @wbp.parser.entryPoint
     */
    protected void initUIElements() {
        container.getContentPane().removeAll();

        container.setBounds(100, 100, 800, 445);
        container.setMinimumSize(new Dimension(800, 445));
        Container contentPane = container.getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new MigLayout("", "[55px][28px][][][][][grow][][][][grow][][][][][][][][]", "[20px,grow]"));

        lblSwingingLibrary = new JLabel("test");
        headerPanel.add(lblSwingingLibrary, "cell 0 0,alignx left,aligny center");

        pnMainPanel = new JPanel();
        headerPanel.add(pnMainPanel, "cell 10 0,grow");

        languageComboBox = new JComboBox<Language>();
        languageComboBox.setModel(new DefaultComboBoxModel<Language>(Language.values()));
        headerPanel.add(languageComboBox, "cell 18 0,alignx right,aligny top");
        contentPane.add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new CompoundBorder(new EmptyBorder(10, 5, 5, 5), new LineBorder(new Color(0, 0, 0), 1, true)));

        LendingMasterSubView lendingMasterPanel = new LendingMasterSubView();
        BookMasterSubView bookMasterPanel = new BookMasterSubView();

        tabbedPane.addTab(Texts.get("BookMasterMainView.tab.books"), IconUtil.loadIcon("book_closed.gif"), bookMasterPanel.getContainer(), null);
        tabbedPane.addTab(Texts.get("BookMasterMainView.tab.lending"), IconUtil.loadIcon("loan.gif"), lendingMasterPanel.getContainer(), null);
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        container.pack();
    }

    @Override
    protected void initListeners() {
        languageComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Language selectedLanguage = (Language) languageComboBox.getSelectedItem();
                getController().switchToLanguage(selectedLanguage);
            }
        });
    }

    @Override
    protected MasterController initController() {
        return new MasterController();
    }

    @Override
    protected JFrame initContainer() {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
        return jFrame;
    }

    @Override
    protected void addObservables() {
    }

}
