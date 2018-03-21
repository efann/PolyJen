/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2018, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.PolyJen;

import com.beowurks.BeoCommon.BaseButton;
import com.beowurks.BeoCommon.BaseFrame;
import com.beowurks.BeoCommon.BaseInternalFrame;
import com.beowurks.BeoCommon.Dialogs.About.AboutAdapter;
import com.beowurks.BeoCommon.Dialogs.About.DialogAbout;
import com.beowurks.BeoCommon.Dialogs.About.IAbout;
import com.beowurks.BeoCommon.Dialogs.Credits.CreditAdapter;
import com.beowurks.BeoCommon.Dialogs.Credits.DialogCredits;
import com.beowurks.BeoCommon.Dialogs.Credits.ICredit;
import com.beowurks.BeoCommon.Util;
import com.beowurks.BeoLookFeel.LFCommon;
import com.beowurks.BeoLookFeel.LFDialog;
import com.beowurks.apple.eawt.IOSXAdapter;
import com.beowurks.apple.eawt.OSXAdapterHelper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class MainFrame extends PolyJenBaseFrame implements ActionListener, MouseMotionListener, WindowFocusListener, Runnable, IOSXAdapter
{
  protected ListComponents foListComponents = null;

  protected final AppProperties foAppProperties;

  private final BaseButton btnRun1 = new BaseButton(64, 64);
  private final BaseButton btnReports1 = new BaseButton(64, 64);
  private final BaseButton btnPrint1 = new BaseButton(64, 64);
  private final BaseButton btnExport1 = new BaseButton(64, 64);

  private final JToolBar tlbPolyJen1 = new JToolBar("PolyJen Tool Bar");

  private final JLabel lblStatusBar1 = new JLabel();

  private final JMenuBar menuBar1 = new JMenuBar();
  private final JMenu menuFile1 = new JMenu();
  private final JMenu menuActions1 = new JMenu();
  private final JMenu menuTools1 = new JMenu();
  private final JMenu menuWindow1 = new JMenu();
  private final JMenu menuHelp1 = new JMenu();

  private final JMenuItem menuPolymer1 = new JMenuItem();
  private final JMenuItem menuCalculations1 = new JMenuItem();
  private final JMenuItem menuEvaluate1 = new JMenuItem();
  private final JMenuItem menuReports1 = new JMenuItem();
  private final JMenuItem menuExport1 = new JMenuItem();
  private final JMenuItem menuPrint1 = new JMenuItem();
  private final JMenuItem menuFileExit1 = new JMenuItem();

  private final JMenuItem menuCreate1 = new JMenuItem();
  private final JMenuItem menuClone1 = new JMenuItem();
  private final JMenuItem menuModify1 = new JMenuItem();
  private final JMenuItem menuRemove1 = new JMenuItem();
  private final JMenuItem menuVerify1 = new JMenuItem();
  private final JMenuItem menuRun1 = new JMenuItem();
  private final JMenuItem menuRefresh1 = new JMenuItem();

  private final JMenuItem menuLookFeel1 = new JMenuItem();
  private final JMenuItem menuSampleData1 = new JMenuItem();
  private final JMenuItem menuJavaManager1 = new JMenuItem();

  private final JMenuItem menuCascade1 = new JMenuItem();
  private final JMenuItem menuCloseAll1 = new JMenuItem();

  private final JMenuItem menuCredits1 = new JMenuItem();
  private final JMenuItem menuDocumentationPDF1 = new JMenuItem();
  private final JMenuItem menuDocumentationHTM1 = new JMenuItem();
  private final JMenuItem menuAbout1 = new JMenuItem();

  private boolean flFirstTime = true;

  private GradientDesktopPane pnlGradientDesktop;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public MainFrame(final Application toApplication)
  {
    this.foAppProperties = toApplication.foAppProperties;

    this.foListComponents = new ListComponents(Global.getXMLTablesPath(), this.foAppProperties);
    this.foListComponents.initializeComponents();

    try
    {
      this.jbInit();

      if (!MainFrame.setupDataDirectory())
      {
        Global.errorException(this, new Exception("Unable to create the directory, '" + Global.getXMLTablesPath() + "'."));
        System.exit(1);
      }

      this.updateMenusAndToolBars();
    }
    catch (final Exception loErr)
    {
      Util.showStackTraceInMessage(this, loErr, "Exception");
    }

  }

  // ---------------------------------------------------------------------------
  // Component initialization
  @Override
  protected void jbInit() throws Exception
  {
    super.jbInit();

    this.getContentPane().setLayout(new BorderLayout());

    final Dimension ldScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.setSize(new Dimension((int) (ldScreenSize.width * 0.75), (int) (ldScreenSize.height * 0.75)));

    // The title gets reset after a successful login.
    this.setTitle(Util.getTitle());

    this.setupMenu();
    this.setupListeners();
    this.setupBackground();
    this.setupButtons();
    this.setupToolBars();

    this.lblStatusBar1.setText("Welcome to PolyJen");
    this.getContentPane().add(this.lblStatusBar1, BorderLayout.SOUTH);

    this.getContentPane().add(this.tlbPolyJen1, BorderLayout.NORTH);

    // Only sets up if running on a Mac.
    OSXAdapterHelper.setupOSXAdapter(this);

  }

  // ---------------------------------------------------------------------------
  private void setupToolBars()
  {
    this.tlbPolyJen1.setFloatable(false);
    this.tlbPolyJen1.setRollover(true);
    this.tlbPolyJen1.setBorderPainted(false);
    this.tlbPolyJen1.setBorder(null);

    this.tlbPolyJen1.add(this.btnRun1);
    this.tlbPolyJen1.addSeparator();
    this.tlbPolyJen1.add(this.btnReports1);
    this.tlbPolyJen1.add(this.btnPrint1);
    this.tlbPolyJen1.add(this.btnExport1);
  }

  // ---------------------------------------------------------------------------
  private void setupButtons() throws Exception
  {
    if (this.menuFile1.getText().isEmpty())
    {
      throw new Exception("The menus have not yet been set for the routine setupButtons in PolyJen.");
    }

    this.setupToolBarButtons(this.btnRun1, "/com/beowurks/PolyJen/images/run32.png", this.menuRun1);
    this.setupToolBarButtons(this.btnReports1, "/com/beowurks/PolyJen/images/report32.png", this.menuReports1);
    this.setupToolBarButtons(this.btnPrint1, "/com/beowurks/PolyJen/images/printer32.png", this.menuPrint1);
    this.setupToolBarButtons(this.btnExport1, "/com/beowurks/PolyJen/images/export32.png", this.menuExport1);
  }

  // ---------------------------------------------------------------------------
  private void setupToolBarButtons(final JButton toButton, final String tcIcon, final JMenuItem toMenuItem)
  {
    final Font loFont = toButton.getFont().deriveFont(10.0f);

    toButton.setIcon(new ImageIcon(this.getClass().getResource(tcIcon)));
    toButton.setFont(loFont);
    toButton.setText(toMenuItem.getText());
    toButton.setVerticalTextPosition(SwingConstants.BOTTOM);
    toButton.setHorizontalTextPosition(SwingConstants.CENTER);
    toButton.setMnemonic(toMenuItem.getMnemonic());
    toButton.setToolTipText(toMenuItem.getToolTipText());
  }

  // ---------------------------------------------------------------------------
  private void setupBackground()
  {
    this.pnlGradientDesktop = new GradientDesktopPane();
    this.getContentPane().add(this.pnlGradientDesktop, BorderLayout.CENTER);
  }

  // ---------------------------------------------------------------------------
  private void setupMenu()
  {
    // ----
    this.menuFile1.setText("File");
    this.menuFile1.setMnemonic('F');

    this.menuPolymer1.setText(Global.MENU_POLYMERIZE);
    this.menuPolymer1.setMnemonic(Global.MENU_POLYMERIZE_MNEMONIC);
    this.menuPolymer1.setToolTipText(Global.MENU_POLYMERIZE_HINT);
    this.menuPolymer1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/polymer_parameters32.png")));

    this.menuCalculations1.setText(Global.MENU_CALCULATION);
    this.menuCalculations1.setMnemonic(Global.MENU_CALCULATION_MNEMONIC);
    this.menuCalculations1.setToolTipText(Global.MENU_CALCULATION_HINT);
    this.menuCalculations1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/calculations32.png")));

    this.menuEvaluate1.setText(Global.MENU_EVALUATE);
    this.menuEvaluate1.setMnemonic(Global.MENU_EVALUATE_MNEMONIC);
    this.menuEvaluate1.setToolTipText(Global.MENU_EVALUATE_HINT);
    this.menuEvaluate1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/evaluate32.png")));

    this.menuReports1.setText(Global.MENU_REPORTS);
    this.menuReports1.setMnemonic(Global.MENU_REPORTS_MNEMONIC);
    this.menuReports1.setToolTipText(Global.MENU_REPORTS_HINT);
    this.menuReports1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/report32.png")));

    this.menuPrint1.setText(Global.MENU_PRINT);
    this.menuPrint1.setMnemonic(Global.MENU_PRINT_MNEMONIC);
    this.menuPrint1.setToolTipText(Global.MENU_PRINT_HINT);
    this.menuPrint1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
    this.menuPrint1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/printer32.png")));

    this.menuExport1.setText(Global.MENU_EXPORT);
    this.menuExport1.setMnemonic(Global.MENU_EXPORT_MNEMONIC);
    this.menuExport1.setToolTipText(Global.MENU_EXPORT_HINT);
    this.menuExport1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/export32.png")));

    this.menuFileExit1.setText(Global.MENU_EXIT);
    this.menuFileExit1.setMnemonic(Global.MENU_EXIT_MNEMONIC);
    this.menuFileExit1.setToolTipText(Global.MENU_EXIT_HINT);
    this.menuFileExit1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/exit32.png")));

    // ----
    this.menuActions1.setText("Actions");
    this.menuActions1.setMnemonic('A');

    this.menuCreate1.setText(Global.MENU_CREATE);
    this.menuCreate1.setMnemonic(Global.MENU_CREATE_MNEMONIC);
    this.menuCreate1.setToolTipText(Global.MENU_CREATE_HINT);
    this.menuCreate1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/create32.png")));

    this.menuClone1.setText(Global.MENU_CLONE);
    this.menuClone1.setMnemonic(Global.MENU_CLONE_MNEMONIC);
    this.menuClone1.setToolTipText(Global.MENU_CLONE_HINT);
    this.menuClone1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/clone32.png")));

    this.menuModify1.setText(Global.MENU_MODIFY);
    this.menuModify1.setMnemonic(Global.MENU_MODIFY_MNEMONIC);
    this.menuModify1.setToolTipText(Global.MENU_MODIFY_HINT);
    this.menuModify1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/modify32.png")));

    this.menuRemove1.setText(Global.MENU_REMOVE);
    this.menuRemove1.setMnemonic(Global.MENU_REMOVE_MNEMONIC);
    this.menuRemove1.setToolTipText(Global.MENU_REMOVE_HINT);
    this.menuRemove1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/remove32.png")));

    this.menuVerify1.setText(Global.MENU_VERIFY);
    this.menuVerify1.setMnemonic(Global.MENU_VERIFY_MNEMONIC);
    this.menuVerify1.setToolTipText(Global.MENU_VERIFY_HINT);
    this.menuVerify1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/verify32.png")));

    this.menuRun1.setText(Global.MENU_RUN);
    this.menuRun1.setMnemonic(Global.MENU_RUN_MNEMONIC);
    this.menuRun1.setToolTipText(Global.MENU_RUN_HINT);
    this.menuRun1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/run32.png")));

    this.menuRefresh1.setText(Global.MENU_REFRESH);
    this.menuRefresh1.setMnemonic(Global.MENU_REFRESH_MNEMONIC);
    this.menuRefresh1.setToolTipText(Global.MENU_REFRESH_HINT);
    this.menuRefresh1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/refresh32.png")));

    // ----
    this.menuTools1.setText("Tools");
    this.menuTools1.setMnemonic('T');

    this.menuLookFeel1.setText(Global.MENU_LOOKFEEL);
    this.menuLookFeel1.setMnemonic(Global.MENU_LOOKFEEL_MNEMONIC);
    this.menuLookFeel1.setToolTipText(Global.MENU_LOOKFEEL_HINT);
    this.menuLookFeel1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/looknfeel32.png")));

    this.menuSampleData1.setText(Global.MENU_SAMPLES);
    this.menuSampleData1.setToolTipText(Global.MENU_SAMPLES_HINT);
    this.menuSampleData1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/download32.png")));

    this.menuJavaManager1.setText(Global.MENU_JAVAMANAGER);
    this.menuJavaManager1.setToolTipText(Global.MENU_JAVAMANAGER_HINT);
    this.menuJavaManager1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/coffee32.png")));

    // ----
    this.menuWindow1.setText("Window");
    this.menuWindow1.setMnemonic('W');

    this.menuCascade1.setText(Global.MENU_CASCADE);
    this.menuCascade1.setMnemonic(Global.MENU_CASCADE_MNEMONIC);
    this.menuCascade1.setToolTipText(Global.MENU_CASCADE_HINT);

    this.menuCloseAll1.setText(Global.MENU_CLOSEALL);
    this.menuCloseAll1.setMnemonic(Global.MENU_CLOSEALL_MNEMONIC);
    this.menuCloseAll1.setToolTipText(Global.MENU_CLOSEALL_HINT);

    // ----
    this.menuHelp1.setText("Help");
    this.menuHelp1.setMnemonic('H');

    this.menuCredits1.setText(Global.MENU_CREDITS);
    this.menuCredits1.setMnemonic(Global.MENU_CREDITS_MNEMONIC);
    this.menuCredits1.setToolTipText(Global.MENU_CREDITS_HINT);
    this.menuCredits1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/credits32.png")));

    this.menuDocumentationPDF1.setText(Global.MENU_HELP_DOCUMENTATION_PDF);
    this.menuDocumentationPDF1.setMnemonic(Global.MENU_HELP_DOCUMENTATION_PDF_MNEMONIC);
    this.menuDocumentationPDF1.setToolTipText(Global.MENU_HELP_DOCUMENTATION_PDF_HINT);
    this.menuDocumentationPDF1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
    this.menuDocumentationPDF1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/help32.png")));

    this.menuDocumentationHTM1.setText(Global.MENU_HELP_DOCUMENTATION_HTM);
    this.menuDocumentationHTM1.setMnemonic(Global.MENU_HELP_DOCUMENTATION_HTM_MNEMONIC);
    this.menuDocumentationHTM1.setToolTipText(Global.MENU_HELP_DOCUMENTATION_HTM_HINT);
    this.menuDocumentationHTM1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/help32.png")));

    this.menuAbout1.setText(Global.MENU_ABOUT);
    this.menuAbout1.setMnemonic(Global.MENU_ABOUT_MNEMONIC);
    this.menuAbout1.setToolTipText(Global.MENU_ABOUT_HINT);
    this.menuAbout1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/about32.png")));

    // ----
    this.menuBar1.add(this.menuFile1);
    this.menuBar1.add(this.menuActions1);
    this.menuBar1.add(this.menuTools1);
    this.menuBar1.add(this.menuWindow1);
    this.menuBar1.add(this.menuHelp1);

    this.menuFile1.add(this.menuPolymer1);
    this.menuFile1.add(this.menuCalculations1);
    this.menuFile1.addSeparator();
    this.menuFile1.add(this.menuEvaluate1);
    this.menuFile1.addSeparator();
    this.menuFile1.add(this.menuReports1);
    this.menuFile1.add(this.menuPrint1);
    this.menuFile1.add(this.menuExport1);
    if (!Util.isMacintosh())
    {
      this.menuFile1.addSeparator();
      this.menuFile1.add(this.menuFileExit1);
    }

    this.menuActions1.add(this.menuCreate1);
    this.menuActions1.add(this.menuClone1);
    this.menuActions1.add(this.menuModify1);
    this.menuActions1.add(this.menuRemove1);
    this.menuActions1.addSeparator();
    this.menuActions1.add(this.menuVerify1);
    this.menuActions1.addSeparator();
    this.menuActions1.add(this.menuRun1);
    this.menuActions1.addSeparator();
    this.menuActions1.add(this.menuRefresh1);

    if (!Util.isMacintosh())
    {
      this.menuTools1.add(this.menuLookFeel1);
    }
    this.menuTools1.add(this.menuSampleData1);
    this.menuTools1.addSeparator();
    this.menuTools1.add(this.menuJavaManager1);

    this.menuWindow1.add(this.menuCascade1);
    this.menuWindow1.add(this.menuCloseAll1);

    this.menuHelp1.add(this.menuCredits1);
    this.menuHelp1.addSeparator();
    this.menuHelp1.add(this.menuDocumentationPDF1);
    this.menuHelp1.add(this.menuDocumentationHTM1);
    if (!Util.isMacintosh())
    {
      this.menuHelp1.addSeparator();
      this.menuHelp1.add(this.menuAbout1);
    }

    // ----
    this.addMenuActionCommands(this.menuBar1);
    this.setJMenuBar(this.menuBar1);
  }

  // ---------------------------------------------------------------------------
  protected void updateMenusAndToolBars()
  {
    this.menuLookFeel1.setEnabled(!Util.isMacintosh());

    // It doesn't matter if loActiveFrame is null or not.
    final JInternalFrame loActiveFrame = this.pnlGradientDesktop.getSelectedFrame();

    if (loActiveFrame instanceof Polymerization1)
    {
      final Polymerization1 loPolyFrame = (Polymerization1) loActiveFrame;
      this.menuCreate1.setEnabled(loPolyFrame.btnCreate1.isEnabled());
      this.menuClone1.setEnabled(loPolyFrame.btnClone1.isEnabled());
      this.menuModify1.setEnabled(loPolyFrame.btnModify1.isEnabled());
      this.menuRemove1.setEnabled(loPolyFrame.btnRemove1.isEnabled());
      this.menuVerify1.setEnabled(loPolyFrame.btnVerify1.isEnabled());
    }
    else
    {
      this.menuCreate1.setEnabled(false);
      this.menuClone1.setEnabled(false);
      this.menuModify1.setEnabled(false);
      this.menuRemove1.setEnabled(false);
      this.menuVerify1.setEnabled(false);
    }

    this.menuRun1.setEnabled(((loActiveFrame instanceof Polymerization1) || (loActiveFrame instanceof Evaluate1) || (loActiveFrame instanceof Calculations1)));
    this.menuRefresh1.setEnabled(((loActiveFrame instanceof Polymerization1) || (loActiveFrame instanceof Calculations1)));
    this.menuReports1.setEnabled(((loActiveFrame instanceof Evaluate1) || (loActiveFrame instanceof Polymerization1) || (loActiveFrame instanceof Calculations1)));
    this.menuPrint1.setEnabled(loActiveFrame instanceof ReportBase);
    this.menuExport1.setEnabled(loActiveFrame instanceof ReportBase);

    this.btnRun1.setEnabled(this.menuRun1.isEnabled());
    this.btnReports1.setEnabled(this.menuReports1.isEnabled());
    this.btnPrint1.setEnabled(this.menuPrint1.isEnabled());
    this.btnExport1.setEnabled(this.menuExport1.isEnabled());
  }

  // ---------------------------------------------------------------------------
  private void setupListeners()
  {
    final int lnCountMenu = this.menuBar1.getMenuCount();
    for (int xx = 0; xx < lnCountMenu; ++xx)
    {
      final JMenu loMenu = this.menuBar1.getMenu(xx);
      final int lnCountComp = loMenu.getMenuComponentCount();
      for (int yy = 0; yy < lnCountComp; ++yy)
      {
        final Component loItem = loMenu.getMenuComponent(yy);
        if (loItem instanceof JMenuItem)
        {
          ((JMenuItem) loItem).addActionListener(this);
          ((JMenuItem) loItem).addMouseMotionListener(this);
        }
      }
    }

    this.btnRun1.addActionListener(this);
    this.btnReports1.addActionListener(this);
    this.btnPrint1.addActionListener(this);
    this.btnExport1.addActionListener(this);

    this.addWindowFocusListener(this);
  }

  // ---------------------------------------------------------------------------
  private void setProperties()
  {
    this.foAppProperties.setLookAndFeel(LFCommon.getCurrentLookAndFeel());
    this.foAppProperties.setMetalTheme(LFCommon.getCurrentMetalTheme());
  }

  // ---------------------------------------------------------------------------
  private void launchPolymerizationInternalFrame()
  {
    BaseInternalFrame loInternalFrame = (BaseInternalFrame) Util.isFrameObject(Polymerization1.class);
    if (loInternalFrame == null)
    {
      loInternalFrame = new Polymerization1(this, this.menuPolymer1.getText());
    }

    loInternalFrame.makeVisible(true);
  }

  // ---------------------------------------------------------------------------
  private void downloadSampleData()
  {
    if (!Util.yesNo(this, "From the menu of Tools | Download Sample Data\n\nDownloading the sample data files will overwrite any data with the same Unique Name (which only previously downloaded sample data could have).\n\nDo you wish to proceed?"))
    {
      return;
    }

    this.setBusy(true);

    boolean llOkay = false;
    final String lcFileName = "SampleData.zip";
    final String lcLocalPath = Global.getUserHomeApplicationPath() + lcFileName;
    if (Util.downloadWebFile("http://www.beowurks.com/Software/PolyJen/SampleData/" + lcFileName, lcLocalPath))
    {
      llOkay = true;

      final ZipTesting loZipTesting = new ZipTesting(lcLocalPath);

      try
      {
        loZipTesting.testFiles();
      }
      catch (final Exception loErr)
      {
        llOkay = false;
        Util.errorMessage(this, "There was an error with the file " + lcFileName + "\n\n" + loErr.getMessage());
      }

      if (llOkay)
      {
        final ZipExtraction loZipExtraction = new ZipExtraction(lcLocalPath, Global.getXMLTablesPath());

        try
        {
          loZipExtraction.extractFiles();
        }
        catch (final Exception loErr)
        {
          llOkay = false;
          Util.errorMessage(this, "There was an error with the file " + lcFileName + "\n\n" + loErr.getMessage());
        }
      }

      if (llOkay)
      {
        if (this.foListComponents.isReadyToRefresh())
        {
          this.foListComponents.refreshData();
        }

        Util.infoMessage(this, "The sample data files have been successfully retrieved.");
      }
    }

    this.setBusy(false);

    if (llOkay)
    {
      this.launchPolymerizationInternalFrame();
    }
  }

  // ---------------------------------------------------------------------------
  private void performButtonAction(final ActionEvent toEvent)
  {
    final Object loObject = toEvent.getSource();

    BaseInternalFrame loInternalFrame;
    final JInternalFrame loActiveFrame = BaseInternalFrame.getActiveFrame();

    if (loObject == this.btnRun1)
    {
      if (loActiveFrame instanceof Evaluate1)
      {
        final Evaluate1 loEvaluateFrame = (Evaluate1) loActiveFrame;
        loEvaluateFrame.runCalculations();
      }
      else if (loActiveFrame instanceof Calculations1)
      {
        final Calculations1 loCalculateFrame = (Calculations1) loActiveFrame;
        loCalculateFrame.runCalculations();
      }
      else if (loActiveFrame instanceof Polymerization1)
      {
        loInternalFrame = (BaseInternalFrame) Util.isFrameObject(Calculations1.class);
        if (loInternalFrame == null)
        {
          loInternalFrame = new Calculations1(this, this.menuCalculations1.getText());
        }

        loInternalFrame.makeVisible(true);
        ((Calculations1) loInternalFrame).runCalculations();
      }
    }
    else if (loObject == this.btnReports1)
    {
      if (loActiveFrame instanceof Polymerization1)
      {
        final PolymerParametersXML loParams = this.foMainFrame.foListComponents.getPropertiesFromTableSelection();

        if (loParams == null)
        {
          Util.errorMessage(this, "You have not yet selected a record.");
        }
        else
        {
          loInternalFrame = new ReportParameters1(this, "Parameters Report", loParams);
          loInternalFrame.makeVisible(true);
        }
      }
      else if (loActiveFrame instanceof Evaluate1)
      {
        if (((Evaluate1) loActiveFrame).faElements == null)
        {
          Util.errorMessage(this, "You have not yet run an evaluation.");
        }
        else
        {
          loInternalFrame = new ReportEvaluation1(this, "Evaluation Report", ((Evaluate1) loActiveFrame).faElements);
          loInternalFrame.makeVisible(true);
        }
      }
      else if (loActiveFrame instanceof Calculations1)
      {
        if (((Calculations1) loActiveFrame).getDistributionChart().getCategoryPlot().getDataset() == null)
        {
          Util.errorMessage(this, "You have not yet run a calculation.");
        }
        else
        {
          final PolymerParametersXML loParams = this.foMainFrame.foListComponents.getPropertiesFromTableSelection();
          loInternalFrame = new ReportCalculations1(this, "Calculations Report", (loParams == null) ? "" : loParams.getTitle(), ((Calculations1) loActiveFrame).getDistributionChart());
          loInternalFrame.makeVisible(true);
        }
      }
    }
    else if (loObject == this.btnPrint1)
    {
      if (loActiveFrame instanceof ReportBase)
      {
        final ReportBase loReportBase = (ReportBase) loActiveFrame;
        loReportBase.printReport();
      }
      else
      {
        Util.errorMessage(this, "No reports are currently active.");
      }
    }
    else if (loObject == this.btnExport1)
    {
      if (loActiveFrame instanceof ReportBase)
      {
        final ReportBase loReportBase = (ReportBase) loActiveFrame;
        loReportBase.exportReport();
      }
      else
      {
        Util.errorMessage(this, "No reports are currently active.");
      }
    }
  }

  // ---------------------------------------------------------------------------
  private void performMenuAction(final ActionEvent toEvent)
  {
    final Object loObject = toEvent.getSource();

    BaseInternalFrame loInternalFrame;

    if (loObject == this.menuFileExit1)
    {
      this.performShutdownMaintenance();
    }
    else if (loObject == this.menuPolymer1)
    {
      this.launchPolymerizationInternalFrame();
    }
    else if (loObject == this.menuCalculations1)
    {
      loInternalFrame = (BaseInternalFrame) Util.isFrameObject(Calculations1.class);
      if (loInternalFrame == null)
      {
        loInternalFrame = new Calculations1(this, this.menuCalculations1.getText());
      }

      loInternalFrame.makeVisible(true);
    }
    else if (loObject == this.menuEvaluate1)
    {
      loInternalFrame = (BaseInternalFrame) Util.isFrameObject(Evaluate1.class);
      if (loInternalFrame == null)
      {
        loInternalFrame = new Evaluate1(this, this.menuEvaluate1.getText());
      }

      loInternalFrame.makeVisible(true);
    }
    else if (loObject == this.menuReports1)
    {
      this.btnReports1.doClick();
    }
    else if (loObject == this.menuPrint1)
    {
      this.btnPrint1.doClick();
    }
    else if (loObject == this.menuExport1)
    {
      this.btnExport1.doClick();
    }
    else if (loObject == this.menuCreate1)
    {
      final Polymerization1 loPolyFrame = (Polymerization1) Util.isFrameObject(Polymerization1.class);
      if (loPolyFrame != null)
      {
        loPolyFrame.btnCreate1.doClick();
      }
    }
    else if (loObject == this.menuClone1)
    {
      final Polymerization1 loPolyFrame = (Polymerization1) Util.isFrameObject(Polymerization1.class);
      if (loPolyFrame != null)
      {
        loPolyFrame.btnClone1.doClick();
      }
    }
    else if (loObject == this.menuModify1)
    {
      final Polymerization1 loPolyFrame = (Polymerization1) Util.isFrameObject(Polymerization1.class);
      if (loPolyFrame != null)
      {
        loPolyFrame.btnModify1.doClick();
      }
    }
    else if (loObject == this.menuRemove1)
    {
      final Polymerization1 loPolyFrame = (Polymerization1) Util.isFrameObject(Polymerization1.class);
      if (loPolyFrame != null)
      {
        loPolyFrame.btnRemove1.doClick();
      }
    }
    else if (loObject == this.menuVerify1)
    {
      final Polymerization1 loPolyFrame = (Polymerization1) Util.isFrameObject(Polymerization1.class);
      if (loPolyFrame != null)
      {
        loPolyFrame.btnVerify1.doClick();
      }
    }
    else if (loObject == this.menuRun1)
    {
      this.btnRun1.doClick();
    }
    else if (loObject == this.menuRefresh1)
    {
      this.foListComponents.refreshData();
    }
    else if (loObject == this.menuLookFeel1)
    {
      new LFDialog(BaseFrame.getActiveFrame());
    }
    else if (loObject == this.menuSampleData1)
    {
      this.downloadSampleData();
    }
    else if (loObject == this.menuJavaManager1)
    {
      Util.launchJavaApplicationManager();
    }
    else if (loObject == this.menuCascade1)
    {
      Util.cascadeWindows(this.pnlGradientDesktop);
    }
    else if (loObject == this.menuCloseAll1)
    {
      Util.closeAllWindows(this.pnlGradientDesktop);
    }
    else if (loObject == this.menuCredits1)
    {
      this.showCredits();
    }
    else if (loObject == this.menuDocumentationPDF1)
    {
      Util.launchBrowser("http://www.beowurks.com/Software/PolyJen/Help/Documentation.pdf");
    }
    else if (loObject == this.menuDocumentationHTM1)
    {
      Util.launchBrowser("http://www.beowurks.com/Software/PolyJen/Help/Documentation.html");
    }
    else if (loObject == this.menuAbout1)
    {
      this.showAbout();
    }
  }

  // ---------------------------------------------------------------------------
  private void showAbout()
  {
    try
    {
      final BufferedImage loLogo = ImageIO.read(this.getClass().getResource("/com/beowurks/PolyJen/images/Logo.jpg"));
      final String lcTitleURL = "http://polyjen.sourceforge.net/";

      //Polymerization in a digital laboratory
      final IAbout loAbout = new AboutAdapter(Util.getTitle(), lcTitleURL, loLogo, lcTitleURL, "Eclipse Public License 1.0", "http://opensource.org/licenses/eclipse-1.0.php", 2003, "Beowurks", "http://www.beowurks.com/");

      new DialogAbout(BaseFrame.getActiveFrame(), loAbout);
    }
    catch (final Exception loError)
    {
      Util.showStackTraceInMessage(this, loError, "Oops!");
    }
  }

  // ---------------------------------------------------------------------------
  private void showCredits()
  {
    final Vector<ICredit> loVectorLinks = new Vector<>();

    loVectorLinks.add(new CreditAdapter("The theory behind PolyJen was developed by Dr. Edward Nieh.", "https://www.facebook.com/edward.nieh.78759"));
    loVectorLinks.add(new CreditAdapter("PolyJen uses the excellent charting library of JFreeChart.", "http://www.jfree.org/jfreechart/"));
    loVectorLinks.add(new CreditAdapter("PolyJen also uses the wonderful reporting library of JasperReports.", "http://community.jaspersoft.com/project/jasperreports-library"));
    loVectorLinks.add(new CreditAdapter("All other images, save for J'Envelope icon, came from the <em>Nuvola</em> icon theme for KDE 3.x by David Vignoni.", "http://commons.wikimedia.org/wiki/Nuvola"));
    loVectorLinks.add(new CreditAdapter("Code examples from the book, Swing, 2nd Edition, by Matthew Robinson & Pavel Vorobiev, gave us great tutorials on Java Swing.", "http://www.manning.com/robinson2/"));

    new DialogCredits(this, loVectorLinks);
  }

  // ---------------------------------------------------------------------------
  // Overridden so we can exit when window is closed
  @Override
  protected void processWindowEvent(final WindowEvent e)
  {
    super.processWindowEvent(e);

    // By the way, WindowEvent.WINDOW_ACTIVATED is defined in PolyJenBaseFrame
    switch (e.getID())
    {
      case WindowEvent.WINDOW_CLOSING:
        this.performShutdownMaintenance();
        break;
    }
  }

  // ---------------------------------------------------------------------------
  private void performShutdownMaintenance()
  {
    this.runCleanupOnAllFrames();

    this.setProperties();

    this.foAppProperties.writeProperties();
    System.exit(0);
  }

  // ---------------------------------------------------------------------------
  private void runCleanupOnAllFrames()
  {
    final Frame[] laFrames = Frame.getFrames();
    int lnLength = laFrames.length;
    for (int i = 0; i < lnLength; ++i)
    {
      if (laFrames[i] instanceof BaseFrame)
      {
        final BaseFrame loFrame = (BaseFrame) laFrames[i];
        if (loFrame.isVisible())
        {
          loFrame.cleanUp();
        }
      }
    }

    final JInternalFrame[] laIFrames = this.pnlGradientDesktop.getAllFrames();
    lnLength = laIFrames.length;
    for (int i = 0; i < lnLength; ++i)
    {
      final BaseInternalFrame loFrame = (BaseInternalFrame) laIFrames[i];
      if (loFrame.isVisible())
      {
        loFrame.cleanUp();
      }
    }

  }

  // ---------------------------------------------------------------------------
  private static boolean setupDataDirectory()
  {
    return (Util.makeDirectory(Global.getXMLTablesPath()));
  }

  // ---------------------------------------------------------------------------
  protected void addToDesktop(final JInternalFrame toFrame)
  {
    this.pnlGradientDesktop.add(toFrame);
  }

  // ---------------------------------------------------------------------------
  protected void removeFromDesktop(final JInternalFrame toFrame)
  {
    this.pnlGradientDesktop.remove(toFrame);
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void removeListeners()
  {
    this.btnRun1.removeActionListener(this);
    this.btnReports1.removeActionListener(this);
    this.btnPrint1.removeActionListener(this);
    this.btnExport1.removeActionListener(this);

    super.removeListeners();
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface ActionListener
  // ---------------------------------------------------------------------------
  @Override
  public void actionPerformed(final ActionEvent e)
  {
    final Object loSource = e.getSource();

    if (loSource instanceof JMenuItem)
    {
      this.performMenuAction(e);
    }
    else if (loSource instanceof JButton)
    {
      this.performButtonAction(e);
    }
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface MouseMotionListener
  // ---------------------------------------------------------------------------
  @Override
  public void mouseDragged(final MouseEvent e)
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  public void mouseMoved(final MouseEvent e)
  {
    final Object loSource = e.getSource();
    if (loSource instanceof JMenuItem)
    {
      this.lblStatusBar1.setText(((JMenuItem) loSource).getToolTipText());
    }
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface Runnable
  @Override
  public void run()
  {
    this.launchPolymerizationInternalFrame();
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface WindowFocusListener
  @Override
  public void windowGainedFocus(final WindowEvent toEvent)
  {
    if (this.flFirstTime)
    {
      this.flFirstTime = false;

      if (this.foListComponents.getVector().isEmpty())
      {
        this.downloadSampleData();
      }

      SwingUtilities.invokeLater(this);
    }
  }

  // ---------------------------------------------------------------------------
  @Override
  public void windowLostFocus(final WindowEvent toEvent)
  {
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface IOSXAdapter
  // These routines are only called when a user selects from Mac OS X application menu.
  @Override
  public void AboutHandler()
  {
    this.showAbout();
  }

  // ---------------------------------------------------------------------------
  @Override
  public void FileHandler(final String tcFileName)
  {
    this.launchPolymerizationInternalFrame();
  }

  // ---------------------------------------------------------------------------
  @Override
  public void PreferencesHandler()
  {
    Util.infoMessage(this, "Currently, there are not any preferences for this program.");
  }

  // ---------------------------------------------------------------------------
  @Override
  public void QuitHandler()
  {
    this.performShutdownMaintenance();
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
}

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
class GradientDesktopPane extends JDesktopPane
{
  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public GradientDesktopPane()
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void paintComponent(final Graphics toGraphics)
  {
    this.drawBackground(toGraphics);
  }

  // ---------------------------------------------------------------------------
  private void drawBackground(final Graphics toGraphics)
  {
    final Dimension ldSize = this.getSize();
    final int lnWidth = ldSize.width;
    final int lnHeight = ldSize.height;

    final Color loEndColor = this.getBackground();

    final double lnRed = loEndColor.getRed();
    final double lnGreen = loEndColor.getGreen();
    final double lnBlue = loEndColor.getBlue();

    final double lnRatioRed = lnRed / lnHeight;
    final double lnRatioGreen = lnGreen / lnHeight;
    final double lnRatioBlue = lnBlue / lnHeight;

    // This is not a mistake. The area goes from 0 to ldSize.height inclusive.
    for (int yy = 0; yy <= lnHeight; yy++)
    {
      final int lnYLoc = lnHeight - yy;

      toGraphics.setColor(new Color((int) (yy * lnRatioRed), (int) (yy * lnRatioGreen), (int) (yy * lnRatioBlue)));
      toGraphics.drawLine(0, lnYLoc, lnWidth, lnYLoc);
    }
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
