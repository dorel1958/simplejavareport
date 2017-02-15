package dorel.simplejavareport.designer;

//http://stackoverflow.com/questions/14195071/how-to-move-shape-in-java
import dorel.simplejavareport.designer.actions.AddImageAction;
import dorel.simplejavareport.designer.actions.AddLineAction;
import dorel.simplejavareport.designer.actions.AddTextAction;
import dorel.simplejavareport.designer.actions.HelpAction;
import dorel.simplejavareport.designer.actions.report.BandsAction;
import dorel.simplejavareport.designer.actions.file.CloseFileAction;
import dorel.simplejavareport.designer.actions.edit.CopyAction;
import dorel.simplejavareport.designer.actions.edit.CutAction;
import dorel.simplejavareport.designer.actions.edit.DeleteAction;
import dorel.simplejavareport.designer.actions.edit.PasteAction;
import dorel.simplejavareport.designer.actions.file.NewFileAction;
import dorel.simplejavareport.designer.actions.file.OpenFileAction;
import dorel.simplejavareport.designer.actions.report.PageSetupAction;
import dorel.simplejavareport.designer.actions.report.PreviewAction;
import dorel.simplejavareport.designer.actions.file.SaveFileAction;
import dorel.simplejavareport.designer.actions.file.SaveFileAsAction;
import dorel.simplejavareport.designer.actions.format.AlignBottomEdgesAction;
import dorel.simplejavareport.designer.actions.format.AlignHorizontalCentersAction;
import dorel.simplejavareport.designer.actions.format.AlignLeftSidesAction;
import dorel.simplejavareport.designer.actions.format.AlignRightSidesAction;
import dorel.simplejavareport.designer.actions.format.AlignTopEdgesAction;
import dorel.simplejavareport.designer.actions.format.AlignVerticalCentersAction;
import dorel.simplejavareport.designer.actions.format.BringToFrontAction;
import dorel.simplejavareport.designer.actions.format.CenterHorizontallyAction;
import dorel.simplejavareport.designer.actions.format.CenterVerticallyAction;
import dorel.simplejavareport.designer.actions.format.ToNarrowestAction;
import dorel.simplejavareport.designer.actions.format.ToShortestAction;
import dorel.simplejavareport.designer.actions.format.ToTallestAction;
import dorel.simplejavareport.designer.actions.format.ToWidestAction;
import dorel.simplejavareport.designer.actions.report.FontAndColorAction;
import dorel.simplejavareport.designer.actions.report.QueryAction;
import dorel.simplejavareport.designer.actions.report.TestDataAction;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

public class Designer extends JFrame implements ActionListener {

    public ContentPanel cPanel;
    boolean hRuler = true;
    boolean vRuler = true;
    public JPopupMenu popup;	//definim meniul popup al ferestrei

    public static enum IconType {

        EMPTY,
        FILE,
        MODIFIED
    }

    public static void main(String[] args) {
        Designer designer = new Designer();
        designer.initComponents();
    }

    public void setFrameIcon(IconType tip) {
        Image img = null;
        try {
            switch (tip) {
                case EMPTY:
                    setTitle("Simple Java Report Designer");
                    img = ImageIO.read(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/document/empty.png"));
                    break;
                case FILE:
                    img = ImageIO.read(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/document/file.png"));
                    break;
                case MODIFIED:
                    img = ImageIO.read(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/document/modified.png"));
                    break;
            }
            if (img != null) {
                this.setIconImage(img);
            }
        } catch (IOException ex) {
            //
        }
    }

    private void initComponents() {
        setFrameIcon(IconType.EMPTY);
        // Equiv QueryUnload
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowListener(this));
        //
        this.setLayout(new BorderLayout());

        cPanel = new ContentPanel(this);
        cPanel.addMouseListener(cPanel);
        cPanel.addMouseMotionListener(cPanel);
        addKeyListener(cPanel);
        JScrollPane scroll = new JScrollPane(cPanel);

        if (hRuler) {
            Rule cmViewH = new Rule(Rule.HORIZONTAL, true, ContentPanel.coeficientScala);
            scroll.setColumnHeaderView(cmViewH);
        }
        if (vRuler) {
            Rule cmViewV = new Rule(Rule.VERTICAL, true, ContentPanel.coeficientScala);
            scroll.setRowHeaderView(cmViewV);
        }
        this.add(scroll, BorderLayout.CENTER);

        // <editor-fold defaultstate="collapsed" desc="Actions">
        // File
        NewFileAction newFileAction = new NewFileAction("New", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/file/filenew.png")), "New file", 0, this);
        OpenFileAction openFileAction = new OpenFileAction("Open", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/file/fileopen.png")), "Open file", 0, this);
        CloseFileAction closeFileAction = new CloseFileAction("Close", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/file/fileclose.png")), "Close file", 0, this);
        SaveFileAction saveFileAction = new SaveFileAction("Save", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/file/filesave.png")), "Save file", 0, this);
        SaveFileAsAction saveFileAsAction = new SaveFileAsAction("Save as...", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/file/filesaveas.png")), "Save file as...", 0, this);
        // Edit
        CutAction cutAction = new CutAction("Cut", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/edit/cut.png")), "Cut selected controls", 0, this);
        CopyAction copyAction = new CopyAction("Copy", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/edit/copy.png")), "Copy selected controls", 0, this);
        PasteAction pasteAction = new PasteAction("Paste", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/edit/paste.png")), "Paste controls", 0, this);
        DeleteAction deleteAction = new DeleteAction("Delete", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/edit/delete.png")), "Delete selected controls", 0, this);

        // Report
        PageSetupAction pageSetupAction = new PageSetupAction("Page setup", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/report/page.png")), "Page setup", 0, this);
        FontAndColorAction fontAndColorAction = new FontAndColorAction("Default font and color", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/report/font_color.png")), "Set default font and color", 0, this);
        BandsAction bandsAction = new BandsAction("Bands", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/report/bands.png")), "Bands", 0, this);

        TestDataAction testDataAction = new TestDataAction("Test Data", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/report/test_data.png")), "Test data", 0, this);
        PreviewAction previewAction = new PreviewAction("Preview", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/report/preview.png")), "Preview", 0, this);
        //QueryAction queryAction = new QueryAction("Query", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/report/test_data.png")), "Query", 0, this);

        //Controls
        AddTextAction addTextAction = new AddTextAction("Add Text", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/controls/text.png")), "Add Text", 0, this);
        AddLineAction addLineAction = new AddLineAction("Add Line", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/controls/line.png")), "Add Line", 0, this);
        AddImageAction addImageAction = new AddImageAction("Add Image", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/controls/image.png")), "Add Image", 0, this);
        
        // Format
        AlignLeftSidesAction alignLeftSidesAction = new AlignLeftSidesAction("Align left sides", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/format/left.png")), "Align left sides of selected controls", 0, this.cPanel);
        AlignRightSidesAction alignRightSidesAction = new AlignRightSidesAction("Align right sides", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/format/right.png")), "Align right sides of selected controls", 0, this.cPanel);
        AlignTopEdgesAction alignTopEdgesAction = new AlignTopEdgesAction("Align top edges", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/format/top.png")), "Align top edges of selected controls", 0, this.cPanel);
        AlignBottomEdgesAction alignBottomEdgesAction = new AlignBottomEdgesAction("Align bottom edges", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/format/bottom.png")), "Align bottom edges of selected controls", 0, this.cPanel);
        AlignVerticalCentersAction alignVerticalCentersAction = new AlignVerticalCentersAction("Align vertical centers", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/format/vertical.png")), "Align vertical centers of selected controls", 0, this.cPanel);
        AlignHorizontalCentersAction alignHorizontalCentersAction = new AlignHorizontalCentersAction("Align horizontal centers", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/format/horizontal.png")), "Align horizontal centers of selected controls", 0, this.cPanel);
        CenterHorizontallyAction centerHorizontallyAction = new CenterHorizontallyAction("Center horizontally", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/format/centerHoriz.png")), "Center horizontally selected control", 0, this.cPanel);
        CenterVerticallyAction centerVerticallyAction = new CenterVerticallyAction("Center vertically", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/format/centerVert.png")), "Center vertically selected control", 0, this.cPanel);
        BringToFrontAction bringToFrontAction = new BringToFrontAction("Bring to front", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/format/bring_to_front.png")), "Bring to front selected control", 0, this.cPanel);
        //SendToBackAction sendToBackAction = new SendToBackAction("Send to back", null, "Send to back selected control", 0, this.cPanel);

        ToTallestAction toTallestAction = new ToTallestAction("To tallest", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/format/toTallest.png")), "To tallest of selected controls", 0, this.cPanel);
        ToShortestAction toShortestAction = new ToShortestAction("To shortest", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/format/toShortest.png")), "To shortest of selected controls", 0, this.cPanel);
        ToWidestAction toWidestAction = new ToWidestAction("To widest", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/format/toWidest.png")), "To widest of selected controls", 0, this.cPanel);
        ToNarrowestAction toNarrowestAction = new ToNarrowestAction("To narrowest", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/format/toNarrowest.png")), "To narrowest of selected controls", 0, this.cPanel);

        // Help
        //MyAction helpAction = new MyAction("Help", null, "Help", 0);
        HelpAction helpAction = new HelpAction("Help", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/help/help.gif")), "Help", 0, this);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Menu">
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem(newFileAction));
        fileMenu.add(new JMenuItem(openFileAction));
        fileMenu.add(new JMenuItem(closeFileAction));
        fileMenu.add(new JMenuItem(saveFileAction));
        fileMenu.add(new JMenuItem(saveFileAsAction));
        fileMenu.addSeparator();
        JMenuItem exitApp = new JMenuItem("Exit", new javax.swing.ImageIcon(getClass().getResource("/dorel/simplejavareport/designer/actions/resources/file/exit.png")));
        exitApp.addActionListener(this);
        fileMenu.add(exitApp);

        JMenu editMenu = new JMenu("Edit");
        editMenu.add(new JMenuItem(cutAction));
        editMenu.add(new JMenuItem(copyAction));
        editMenu.add(new JMenuItem(pasteAction));
        editMenu.add(new JMenuItem(deleteAction));

        JMenu reportMenu = new JMenu("Report");
        reportMenu.add(new JMenuItem(pageSetupAction));
        reportMenu.add(new JMenuItem(fontAndColorAction));
        reportMenu.add(new JMenuItem(bandsAction));
        //reportMenu.add(new JMenuItem(rulerGridAction));
        reportMenu.addSeparator();
        reportMenu.add(new JMenuItem(testDataAction));
        reportMenu.add(new JMenuItem(previewAction));
        //reportMenu.add(new JMenuItem(queryAction));

        JMenu controlsMenu = new JMenu("Controls");
        controlsMenu.add(new JMenuItem(addTextAction));
        controlsMenu.add(new JMenuItem(addLineAction));
        controlsMenu.add(new JMenuItem(addImageAction));

        JMenu formatMenu = new JMenu("Format");
        JMenu mALign = new JMenu("Align");
        mALign.add(new JMenuItem(alignLeftSidesAction));
        mALign.add(new JMenuItem(alignRightSidesAction));
        mALign.add(new JMenuItem(alignTopEdgesAction));
        mALign.add(new JMenuItem(alignBottomEdgesAction));
        mALign.add(new JMenuItem(alignVerticalCentersAction));
        mALign.add(new JMenuItem(alignHorizontalCentersAction));
        mALign.add(new JMenuItem(centerHorizontallyAction));
        mALign.add(new JMenuItem(centerVerticallyAction));
        mALign.addSeparator();
        mALign.add(new JMenuItem(bringToFrontAction));
        //mALign.add(new JMenuItem(sendToBackAction));
        formatMenu.add(mALign);

        JMenu mSize = new JMenu("Size");
        mSize.add(new JMenuItem(toTallestAction));
        mSize.add(new JMenuItem(toShortestAction));
        mSize.add(new JMenuItem(toWidestAction));
        mSize.add(new JMenuItem(toNarrowestAction));
        formatMenu.add(mSize);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(new JMenuItem(helpAction));

        JMenuBar mainMenu = new JMenuBar();
        mainMenu.add(fileMenu);
        mainMenu.add(editMenu);
        mainMenu.add(reportMenu);
        mainMenu.add(controlsMenu);
        mainMenu.add(formatMenu);
        mainMenu.add(helpMenu);
        this.setJMenuBar(mainMenu);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Toolbar">
        JPanel panelNorth = new JPanel();
        panelNorth.setLayout(new FlowLayout());
        this.add(panelNorth, BorderLayout.NORTH);
        
        //JPanel panelEast = new JPanel();
        //panelEast.setLayout(new FlowLayout());
        //this.add(panelEast, BorderLayout.WEST);
        //
        JToolBar tbFile = new JToolBar("File");
        tbFile.add(newToolbarButton(newFileAction));
        tbFile.add(newToolbarButton(openFileAction));
        tbFile.add(newToolbarButton(closeFileAction));
        tbFile.add(newToolbarButton(saveFileAction));
        tbFile.add(newToolbarButton(saveFileAsAction));
        panelNorth.add(tbFile);
        //
        JToolBar tbEdit = new JToolBar("Edit");
        tbEdit.setFocusable(false);
        //tbEdit.setOrientation(JToolBar.VERTICAL);
        tbEdit.add(newToolbarButton(cutAction));
        tbEdit.add(newToolbarButton(copyAction));
        tbEdit.add(newToolbarButton(pasteAction));
        tbEdit.add(newToolbarButton(deleteAction));
        panelNorth.add(tbEdit);        
        //panelEast.add(tbEdit);
        //
        JToolBar tbReport = new JToolBar("Report");
        tbReport.setFocusable(false);
        tbReport.add(newToolbarButton(pageSetupAction));
        tbReport.add(newToolbarButton(fontAndColorAction));
        tbReport.add(newToolbarButton(bandsAction));
        tbReport.add(newToolbarButton(testDataAction));
        tbReport.add(newToolbarButton(previewAction));
        panelNorth.add(tbReport);
        //
        JToolBar tbControls = new JToolBar("Controls");
        tbControls.setFocusable(false);
        //tbControls.setOrientation(JToolBar.VERTICAL);
        tbControls.add(newToolbarButton(addTextAction));
        tbControls.add(newToolbarButton(addLineAction));
        tbControls.add(newToolbarButton(addImageAction));
        panelNorth.add(tbControls);        
        //panelEast.add(tbControls);
        //
        JToolBar tbFormat = new JToolBar("Format");
        tbFormat.setFocusable(false);
        tbFormat.setOrientation(JToolBar.VERTICAL);
        tbFormat.add(newToolbarButton(alignLeftSidesAction));
        tbFormat.add(newToolbarButton(alignRightSidesAction));
        tbFormat.add(newToolbarButton(alignTopEdgesAction));
        tbFormat.add(newToolbarButton(alignBottomEdgesAction));
        tbFormat.add(newToolbarButton(alignVerticalCentersAction));
        tbFormat.add(newToolbarButton(alignHorizontalCentersAction));
        tbFormat.add(newToolbarButton(centerHorizontallyAction));
        tbFormat.add(newToolbarButton(centerVerticallyAction));
        tbFormat.addSeparator();
        tbFormat.add(newToolbarButton(bringToFrontAction));
        //tbFormat.add(newToolbarButton(sendToBackAction));
        tbFormat.addSeparator();
        tbFormat.add(newToolbarButton(toTallestAction));
        tbFormat.add(newToolbarButton(toShortestAction));
        tbFormat.add(newToolbarButton(toWidestAction));
        tbFormat.add(newToolbarButton(toNarrowestAction));
        this.add(tbFormat, BorderLayout.EAST);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="PopupMenu">      
        popup = new JPopupMenu("Inactiv");
        JMenuItem menuAddText = new JMenuItem(addTextAction);
        JMenuItem menuAddLine = new JMenuItem(addLineAction);
        JMenuItem menuAddImage = new JMenuItem(addImageAction);
        JMenuItem menuCut = new JMenuItem(cutAction);
        JMenuItem menuCopy = new JMenuItem(copyAction);
        JMenuItem menuPaste = new JMenuItem(pasteAction);
        JMenuItem menuDelete = new JMenuItem(deleteAction);
        //menuItem1.addActionListener(this);
        popup.add(menuAddText);
        popup.add(menuAddLine);
        popup.add(menuAddImage);
        popup.addSeparator();
        popup.add(menuCut);
        popup.add(menuCopy);
        popup.add(menuPaste);
        popup.add(menuDelete);
        MouseListener popupListener = new MouseListener(popup);
        cPanel.addMouseListener(popupListener);
        mainMenu.addMouseListener(popupListener);
        //</editor-fold>

        this.pack();

        // <editor-fold defaultstate="collapsed" desc="Center in Screen">      
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        // Determine the new location of the window
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = 0;
        if (dim.width > w) {
            x = (dim.width - w) / 2;
        }
        int y = 0;
        if (dim.height > h) {
            y = (dim.height - h) / 2;
        }
        // Move the window
        this.setLocation(x, y);
        //</editor-fold>

        this.setVisible(true);
    }

    private JButton newToolbarButton(AbstractAction action) {
        // primeste Key de la tastatura NUMAI obiectul ce are focus la momentul respectiv
        // nu mai permit focusarea butoanelor din Toolbar -> focusul ramane pe designer
        // am pus key listenerul pe designer (as fi putut pune si pe toate butoanele)
        JButton buton = new JButton(action);
        buton.setFont(new Font("SansSerif", Font.BOLD, 9));
        //
        // pt a disparea textul scris in buton
        buton.setText("");
        //
        buton.setFocusable(false);
        return buton;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "Exit":
                if (cPanel.testModif()) {
                    System.exit(0);
                }
                break;
            default:
                JOptionPane.showMessageDialog(this, "Comanda necunoscuta:" + ae.getActionCommand());
                break;
        }
    }
}

//
//------------------------------------------------------------------------------
//
class MouseListener extends MouseAdapter {

    JPopupMenu popup;

    public MouseListener(JPopupMenu popup) {
        this.popup = popup;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popup.setLocation(new Point(e.getX(), e.getY()));
            popup.setLabel("Activ," + e.getX() + "," + e.getY());
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

}

//
//------------------------------------------------------------------------------
//
class WindowListener extends WindowAdapter {

    Designer designer;

    public WindowListener(Designer designer) {
        this.designer = designer;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (designer.cPanel.testModif()) {
            System.exit(0);
        }
    }
}
