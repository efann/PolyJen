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
import com.beowurks.BeoCommon.BaseEditTable;
import com.beowurks.BeoCommon.BaseTabbedPane;
import com.beowurks.BeoCommon.BaseTextField;
import com.beowurks.BeoCommon.FormattedDoubleField;
import com.beowurks.BeoCommon.FormattedIntegerField;
import com.beowurks.BeoCommon.GridBagLayoutHelper;
import com.beowurks.BeoCommon.Util;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class PolymerizationEdit1 extends PolyJenBaseDialog implements ActionListener, ChangeListener
{
  private final JPanel pnlTextBoxGeneral = new JPanel();
  private final JPanel pnlTextBoxLevel = new JPanel();
  private final JPanel pnlButtons = new JPanel();
  private JPanel pnlTab[];

  private final BaseTabbedPane tabPane = new BaseTabbedPane();

  private final GridBagLayoutHelper gridBagLayoutTextBoxGeneral = new GridBagLayoutHelper();
  private final GridBagLayoutHelper gridBagLayoutTextBoxLevel = new GridBagLayoutHelper();
  private final GridBagLayoutHelper gridBagLayoutButtons = new GridBagLayoutHelper();
  private GridBagLayoutHelper gridBagLayoutTab[];

  private final BaseTextField txtCreatedOn1 = new BaseTextField();
  private final BaseTextField txtCreatedBy1 = new BaseTextField();
  private final BaseTextField txtTitle1 = new BaseTextField();
  private final BaseTextField txtUniqueName1 = new BaseTextField();
  private final FormattedIntegerField txtInitiatorMolecules1 = new FormattedIntegerField();
  private final FormattedDoubleField txtMWofInitiator1 = new FormattedDoubleField();
  private final FormattedDoubleField txtMWofMonomer1 = new FormattedDoubleField();
  private JSpinner spnSites1;
  private JSpinner spnLevels1;

  private final JLabel lblInitiatorMolecules1 = new JLabel();
  private final JLabel lblMWofInitiator1 = new JLabel();
  private final JLabel lblMWofMonomer1 = new JLabel();
  private final JLabel lblSites1 = new JLabel();
  private final JLabel lblLevels1 = new JLabel();
  private final JLabel lblCreatedOn1 = new JLabel();
  private final JLabel lblCreatedBy1 = new JLabel();
  private final JLabel lblTitle1 = new JLabel();
  private final JLabel lblUniqueName1 = new JLabel();

  private final JLabel lblLevelBlankInstructions1 = new JLabel();
  private final JTextArea lblLevelInstructions1 = new JTextArea();
  private JTextArea lblGridInstructions[];

  private JLabel lblLevel[];
  private FormattedIntegerField txtTotalMonomerReacted[];

  private final BaseButton btnSave1 = new BaseButton(76, 30);
  private final BaseButton btnCancel1 = new BaseButton(76, 30);
  private final BaseButton btnVerify1 = new BaseButton(76, 30);

  private JScrollPane scrollSite[][];
  private BaseEditTable grdSite[][];
  private EditingTabelModel editGridModel[][];

  private PolymerParametersXML foPolymerParametersXML;

  private int fnResults = Global.EDIT_CANCEL;

  private final String fcAction;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public PolymerizationEdit1(final JFrame toFrame, final String tcTitle, final String tcAction, final PolymerParametersXML toPolymerParametersXML)
  {
    super(toFrame, tcTitle);

    this.foPolymerParametersXML = toPolymerParametersXML;
    this.fcAction = tcAction;

    try
    {
      this.jbInit();
      this.centerDialog();
    }
    catch (final Exception loErr)
    {
      Util.showStackTraceInMessage(this, loErr, "Exception");
    }
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void jbInit() throws Exception
  {
    super.jbInit();

    this.setupLabels();
    this.setupSpinners();
    this.setupTextBoxes();
    this.setupButtons();
    this.setupTableModelsForGrids();
    this.setupGrids();
    this.setupTabPanes();
    this.setupLayouts();
    this.setupListeners();
    this.setupTextEntry();

    this.updateSpinnersRelatedAccessibilities();
  }

  // ---------------------------------------------------------------------------
  private void setupTextEntry()
  {
    this.txtUniqueName1.setText(this.foPolymerParametersXML.getFileName());

    this.txtTitle1.setText(this.foPolymerParametersXML.getTitle());
    this.txtCreatedOn1.setText(this.foPolymerParametersXML.getFormattedDate(this.foPolymerParametersXML.getCreatedOn()));
    this.txtCreatedBy1.setText(this.foPolymerParametersXML.getCreatedBy());

    this.txtInitiatorMolecules1.setValue(this.foPolymerParametersXML.getInitiatorMolecules());
    this.txtMWofInitiator1.setValue(this.foPolymerParametersXML.getMWOfInitiator());
    this.txtMWofMonomer1.setValue(this.foPolymerParametersXML.getMWOfMonomer());

    this.spnSites1.setValue(this.foPolymerParametersXML.getSites());
    this.spnLevels1.setValue(this.foPolymerParametersXML.getLevels());

    for (int i = 0; i < this.txtTotalMonomerReacted.length; ++i)
    {
      this.txtTotalMonomerReacted[i].setValue(this.foPolymerParametersXML.getMonomerLevelReacted(i));
    }
  }

  // ---------------------------------------------------------------------------
  private void setupTableModelsForGrids()
  {
    final int lnSites = Global.MAX_POLYMER_SITES;
    final int lnLevels = Global.MAX_POLYMER_LEVELS;

    this.editGridModel = new EditingTabelModel[lnLevels][lnSites];

    for (int lnLevel = 0; lnLevel < lnLevels; ++lnLevel)
    {
      for (int lnSite = 0; lnSite < lnSites; ++lnSite)
      {
        this.editGridModel[lnLevel][lnSite] = new EditingTabelModel(this.foPolymerParametersXML.getArrays(lnLevel, lnSite));
      }
    }
  }

  // ---------------------------------------------------------------------------
  private void flushProperties()
  {
    // If you don't do this, then if you're editing a cell and press Save,
    // the current value is lost (and reverts to the previous value).

    for (int lnLevel = 0; lnLevel < Global.MAX_POLYMER_LEVELS; ++lnLevel)
    {
      for (int lnSite = 0; lnSite < Global.MAX_POLYMER_SITES; ++lnSite)
      {
        if (!this.grdSite[lnLevel][lnSite].getDefaultEditor(Integer.class).stopCellEditing())
        {
          this.grdSite[lnLevel][lnSite].getDefaultEditor(Integer.class).cancelCellEditing();
        }

        if (!this.grdSite[lnLevel][lnSite].getDefaultEditor(Double.class).stopCellEditing())
        {
          this.grdSite[lnLevel][lnSite].getDefaultEditor(Double.class).cancelCellEditing();
        }
      }
    }

    this.foPolymerParametersXML.setTitle(this.txtTitle1.getText());

    final boolean llNewCreate = this.fcAction.equals(Global.MENU_CLONE) || this.fcAction.equals(Global.MENU_CREATE);

    if ((this.foPolymerParametersXML.getCreatedOn().getTime() <= 0) || llNewCreate)
    {
      this.foPolymerParametersXML.setCreatedOn(this.foPolymerParametersXML.getCurrentDateTime());
    }

    if ((this.txtCreatedBy1.getText().length() <= 0) || llNewCreate)
    {
      this.foPolymerParametersXML.setCreatedBy(this.foPolymerParametersXML.getLoginID());
    }

    this.foPolymerParametersXML.setModifiedOn(this.foPolymerParametersXML.getCurrentDateTime());
    this.foPolymerParametersXML.setModifiedBy(this.foPolymerParametersXML.getLoginID());

    this.foPolymerParametersXML.setInitiatorMolecules(this.txtInitiatorMolecules1.getIntegerValue());
    this.foPolymerParametersXML.setMWOfInitiator(this.txtMWofInitiator1.getDoubleValue());
    this.foPolymerParametersXML.setMWOfMonomer(this.txtMWofMonomer1.getDoubleValue());
    this.foPolymerParametersXML.setSites(this.spnSites1.getValue().toString());
    this.foPolymerParametersXML.setLevels(this.spnLevels1.getValue().toString());

    for (int i = 0; i < this.txtTotalMonomerReacted.length; ++i)
    {
      this.foPolymerParametersXML.setMonomerLevelReacted(i, this.txtTotalMonomerReacted[i].getIntegerValue());
    }

    for (int lnLevel = 0; lnLevel < Global.MAX_POLYMER_LEVELS; ++lnLevel)
    {
      for (int lnSite = 0; lnSite < Global.MAX_POLYMER_SITES; ++lnSite)
      {
        this.foPolymerParametersXML.setArrays(lnLevel, lnSite, this.editGridModel[lnLevel][lnSite].getDataArray());
      }
    }
  }

  // ---------------------------------------------------------------------------
  public void setupLayouts()
  {
    this.gridBagLayoutTab = new GridBagLayoutHelper[Global.MAX_POLYMER_SITES];
    for (int i = 0; i < this.gridBagLayoutTab.length; i++)
    {
      this.gridBagLayoutTab[i] = new GridBagLayoutHelper();
    }

    this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

    this.getContentPane().add(this.pnlTextBoxGeneral);
    this.getContentPane().add(this.pnlTextBoxLevel);
    this.getContentPane().add(this.tabPane);
    this.getContentPane().add(this.pnlButtons);

    GridBagLayoutHelper loGridBag;
    // pnlTextBoxGeneral
    loGridBag = this.gridBagLayoutTextBoxGeneral;
    this.pnlTextBoxGeneral.setLayout(loGridBag);

    loGridBag.setInsets(10, 4, 4, 4);
    this.pnlTextBoxGeneral.add(this.lblUniqueName1, loGridBag.getConstraint(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE));
    loGridBag.setInsetDefaults();
    this.pnlTextBoxGeneral.add(this.txtUniqueName1, loGridBag.getConstraint(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE));

    loGridBag.setInsets(10, 4, 4, 4);
    this.pnlTextBoxGeneral.add(this.lblCreatedOn1, loGridBag.getConstraint(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
    loGridBag.setInsetDefaults();
    this.pnlTextBoxGeneral.add(this.txtCreatedOn1, loGridBag.getConstraint(1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));

    loGridBag.setInsets(10, 4, 4, 4);
    this.pnlTextBoxGeneral.add(this.lblCreatedBy1, loGridBag.getConstraint(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE));
    loGridBag.setInsetDefaults();
    this.pnlTextBoxGeneral.add(this.txtCreatedBy1, loGridBag.getConstraint(1, 2, GridBagConstraints.WEST, GridBagConstraints.NONE));

    loGridBag.setInsets(10, 4, 4, 4);
    this.pnlTextBoxGeneral.add(this.lblTitle1, loGridBag.getConstraint(0, 3, GridBagConstraints.EAST, GridBagConstraints.NONE));
    loGridBag.setInsetDefaults();
    this.pnlTextBoxGeneral.add(this.txtTitle1, loGridBag.getConstraint(1, 3, GridBagConstraints.WEST, GridBagConstraints.NONE));

    loGridBag.setInsets(10, 4, 4, 4);
    this.pnlTextBoxGeneral.add(this.lblInitiatorMolecules1, loGridBag.getConstraint(0, 4, GridBagConstraints.EAST, GridBagConstraints.NONE));
    loGridBag.setInsetDefaults();
    this.pnlTextBoxGeneral.add(this.txtInitiatorMolecules1, loGridBag.getConstraint(1, 4, GridBagConstraints.WEST, GridBagConstraints.NONE));

    loGridBag.setInsets(10, 4, 4, 4);
    this.pnlTextBoxGeneral.add(this.lblMWofInitiator1, loGridBag.getConstraint(0, 5, GridBagConstraints.EAST, GridBagConstraints.NONE));
    loGridBag.setInsetDefaults();
    this.pnlTextBoxGeneral.add(this.txtMWofInitiator1, loGridBag.getConstraint(1, 5, GridBagConstraints.WEST, GridBagConstraints.NONE));

    loGridBag.setInsets(10, 4, 4, 4);
    this.pnlTextBoxGeneral.add(this.lblMWofMonomer1, loGridBag.getConstraint(0, 6, GridBagConstraints.EAST, GridBagConstraints.NONE));
    loGridBag.setInsetDefaults();
    this.pnlTextBoxGeneral.add(this.txtMWofMonomer1, loGridBag.getConstraint(1, 6, GridBagConstraints.WEST, GridBagConstraints.NONE));

    loGridBag.setInsets(10, 4, 4, 4);

    this.pnlTextBoxGeneral.add(this.lblSites1, loGridBag.getConstraint(0, 7, GridBagConstraints.EAST, GridBagConstraints.NONE));
    loGridBag.setInsetDefaults();
    this.pnlTextBoxGeneral.add(this.spnSites1, loGridBag.getConstraint(1, 7, GridBagConstraints.WEST, GridBagConstraints.NONE));

    this.pnlTextBoxGeneral.add(this.lblLevels1, loGridBag.getConstraint(0, 8, GridBagConstraints.EAST, GridBagConstraints.NONE));
    loGridBag.setInsetDefaults();
    this.pnlTextBoxGeneral.add(this.spnLevels1, loGridBag.getConstraint(1, 8, GridBagConstraints.WEST, GridBagConstraints.NONE));

    // pnlTextBoxLevel
    loGridBag = this.gridBagLayoutTextBoxLevel;
    this.pnlTextBoxLevel.setLayout(loGridBag);

    loGridBag.setInsets(4, 4, 0, 4);
    this.pnlTextBoxLevel.add(this.lblLevelBlankInstructions1, loGridBag.getConstraint(0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE));

    loGridBag.setInsets(0, 4, 4, 4);
    this.pnlTextBoxLevel.add(this.lblLevelInstructions1, loGridBag.getConstraint(0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));

    final int lnLevelCount = this.lblLevel.length;
    for (int i = 0; i < lnLevelCount; ++i)
    {
      loGridBag.setInsets(4, 4, 0, 4);
      this.pnlTextBoxLevel.add(this.lblLevel[i], loGridBag.getConstraint(i + 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE));

      loGridBag.setInsets(0, 4, 4, 4);
      this.pnlTextBoxLevel.add(this.txtTotalMonomerReacted[i], loGridBag.getConstraint(i + 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));
    }

    // tabPane
    final int lnTabCount = this.pnlTab.length;
    for (int i = 0; i < lnTabCount; ++i)
    {
      loGridBag = this.gridBagLayoutTab[i];
      this.pnlTab[i].setLayout(loGridBag);

      loGridBag.setInsetDefaults();
      this.pnlTab[i].add(this.lblGridInstructions[i], loGridBag.getConstraint(0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE));

      for (int lnLevel = 0; lnLevel < Global.MAX_POLYMER_LEVELS; ++lnLevel)
      {
        this.pnlTab[i].add(this.scrollSite[lnLevel][i], loGridBag.getConstraint(lnLevel + 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE));
      }

    }

    // pnlButtons
    loGridBag = this.gridBagLayoutButtons;
    this.pnlButtons.setLayout(loGridBag);

    loGridBag.setInsets(20, 4, 10, 4);
    this.pnlButtons.add(this.btnVerify1, loGridBag.getConstraint(0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE));
    this.pnlButtons.add(this.btnSave1, loGridBag.getConstraint(1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE));
    this.pnlButtons.add(this.btnCancel1, loGridBag.getConstraint(2, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE));
  }

  // ---------------------------------------------------------------------------
  private void setupSpinners()
  {
    this.spnSites1 = new JSpinner(new SpinnerNumberModel(1, 1, Global.MAX_POLYMER_SITES, 1));
    this.spnSites1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

    this.spnLevels1 = new JSpinner(new SpinnerNumberModel(1, 1, Global.MAX_POLYMER_LEVELS, 1));
    this.spnLevels1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

    final Dimension ldSize = new Dimension(60, (int) (this.spnSites1.getPreferredSize().getHeight()));

    this.spnSites1.setPreferredSize(ldSize);
    this.spnLevels1.setPreferredSize(ldSize);
  }

  // ---------------------------------------------------------------------------
  private void setupTabPanes()
  {
    final String lcSite = "Site #";
    this.pnlTab = new JPanel[Global.MAX_POLYMER_SITES];
    for (int i = 0; i < this.pnlTab.length; ++i)
    {
      this.pnlTab[i] = new JPanel();
      this.tabPane.add(lcSite + (i + 1), this.pnlTab[i]);
    }
  }

  // ---------------------------------------------------------------------------
  private void setupLabels()
  {
    this.lblUniqueName1.setText("Unique Name");
    this.lblUniqueName1.setHorizontalAlignment(SwingConstants.RIGHT);

    this.lblCreatedOn1.setText("Created On");
    this.lblCreatedOn1.setHorizontalAlignment(SwingConstants.RIGHT);

    this.lblCreatedBy1.setText("Created By");
    this.lblCreatedBy1.setHorizontalAlignment(SwingConstants.RIGHT);

    this.lblTitle1.setText("Title");
    this.lblTitle1.setHorizontalAlignment(SwingConstants.RIGHT);

    this.lblInitiatorMolecules1.setText("# of Initiator Molecules");
    this.lblInitiatorMolecules1.setHorizontalAlignment(SwingConstants.RIGHT);

    this.lblMWofInitiator1.setText("Molecular Weight of Initiator");
    this.lblMWofInitiator1.setHorizontalAlignment(SwingConstants.RIGHT);

    this.lblMWofMonomer1.setText("Molecular Weight of Monomer");
    this.lblMWofMonomer1.setHorizontalAlignment(SwingConstants.RIGHT);

    this.lblSites1.setText("Polymer Sites");
    this.lblSites1.setHorizontalAlignment(SwingConstants.RIGHT);

    this.lblLevels1.setText("Monomer Levels");
    this.lblLevels1.setHorizontalAlignment(SwingConstants.RIGHT);

    final String lcLevel = "Level #";
    this.lblLevel = new JLabel[Global.MAX_POLYMER_LEVELS];
    for (int i = 0; i < this.lblLevel.length; ++i)
    {
      this.lblLevel[i] = new JLabel();
      this.lblLevel[i].setText(lcLevel + (i + 1));
      this.lblLevel[i].setHorizontalAlignment(SwingConstants.CENTER);
    }

    this.lblLevelBlankInstructions1.setText("");

    this.lblLevelInstructions1.setText("Up to this # of Monomer Molecules Reacted");
    this.lblLevelInstructions1.setPreferredSize(new Dimension(Global.EDIT_GRID_WIDTH, Global.EDIT_TEXTBOX_HEIGHT * 2));
    this.lblLevelInstructions1.setLineWrap(true);
    this.lblLevelInstructions1.setWrapStyleWord(true);
    this.lblLevelInstructions1.setHighlighter(null);
    this.lblLevelInstructions1.setEditable(false);
    this.lblLevelInstructions1.setBackground(this.getBackground());
    this.lblLevelInstructions1.setForeground(this.lblCreatedOn1.getForeground());
    this.lblLevelInstructions1.setFont(this.lblCreatedOn1.getFont());

    final String lcGridInstructions = "Reaction rates for the various monomers\n\nRight-click on grids to insert & remove lines.";
    this.lblGridInstructions = new JTextArea[Global.MAX_POLYMER_SITES];
    for (int i = 0; i < this.lblGridInstructions.length; ++i)
    {
      this.lblGridInstructions[i] = new JTextArea();
      this.lblGridInstructions[i].setText(lcGridInstructions);
      this.lblGridInstructions[i].setPreferredSize(new Dimension(Global.EDIT_GRID_WIDTH, Global.EDIT_GRID_HEIGHT));
      this.lblGridInstructions[i].setLineWrap(true);
      this.lblGridInstructions[i].setWrapStyleWord(true);
      this.lblGridInstructions[i].setHighlighter(null);
      this.lblGridInstructions[i].setEditable(false);
      this.lblGridInstructions[i].setBackground(this.getBackground());
      this.lblGridInstructions[i].setForeground(this.lblCreatedOn1.getForeground());
      this.lblGridInstructions[i].setFont(this.lblCreatedOn1.getFont());
    }
  }

  // ---------------------------------------------------------------------------
  private void setupTextBoxes()
  {
    this.txtUniqueName1.setMinimumSize(new Dimension(100, Global.EDIT_TEXTBOX_HEIGHT));
    this.txtUniqueName1.setPreferredSize(new Dimension(100, Global.EDIT_TEXTBOX_HEIGHT));
    this.txtUniqueName1.setEditable(false);

    this.txtCreatedBy1.setMinimumSize(new Dimension(150, Global.EDIT_TEXTBOX_HEIGHT));
    this.txtCreatedBy1.setPreferredSize(new Dimension(150, Global.EDIT_TEXTBOX_HEIGHT));
    this.txtCreatedBy1.setEditable(false);

    this.txtCreatedOn1.setMinimumSize(new Dimension(200, Global.EDIT_TEXTBOX_HEIGHT));
    this.txtCreatedOn1.setPreferredSize(new Dimension(200, Global.EDIT_TEXTBOX_HEIGHT));
    this.txtCreatedOn1.setEditable(false);

    this.txtTitle1.setMinimumSize(new Dimension(300, Global.EDIT_TEXTBOX_HEIGHT));
    this.txtTitle1.setPreferredSize(new Dimension(300, Global.EDIT_TEXTBOX_HEIGHT));

    this.txtInitiatorMolecules1.setMinimumSize(new Dimension(100, Global.EDIT_TEXTBOX_HEIGHT));
    this.txtInitiatorMolecules1.setPreferredSize(new Dimension(100, Global.EDIT_TEXTBOX_HEIGHT));

    this.txtMWofInitiator1.setMinimumSize(new Dimension(100, Global.EDIT_TEXTBOX_HEIGHT));
    this.txtMWofInitiator1.setPreferredSize(new Dimension(100, Global.EDIT_TEXTBOX_HEIGHT));

    this.txtMWofMonomer1.setMinimumSize(new Dimension(100, Global.EDIT_TEXTBOX_HEIGHT));
    this.txtMWofMonomer1.setPreferredSize(new Dimension(100, Global.EDIT_TEXTBOX_HEIGHT));

    this.txtTotalMonomerReacted = new FormattedIntegerField[Global.MAX_POLYMER_LEVELS];
    for (int i = 0; i < this.txtTotalMonomerReacted.length; ++i)
    {
      this.txtTotalMonomerReacted[i] = new FormattedIntegerField();
      this.txtTotalMonomerReacted[i].setMinimumSize(new Dimension(Global.EDIT_GRID_WIDTH, Global.EDIT_TEXTBOX_HEIGHT));
      this.txtTotalMonomerReacted[i].setPreferredSize(new Dimension(Global.EDIT_GRID_WIDTH, Global.EDIT_TEXTBOX_HEIGHT));
    }

  }

  // ---------------------------------------------------------------------------
  private void setupButtons()
  {
    this.btnSave1.setText(Global.BUTTON_DATA_SAVE);
    this.btnSave1.setMnemonic(Global.BUTTON_DATA_SAVE_MNEMONIC);
    this.btnSave1.setToolTipText(Global.BUTTON_DATA_SAVE_HINT);
    this.btnSave1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/ok22.png")));

    this.btnCancel1.setText(Global.BUTTON_DATA_CANCEL);
    this.btnCancel1.setMnemonic(Global.BUTTON_DATA_CANCEL_MNEMONIC);
    this.btnCancel1.setToolTipText(Global.BUTTON_DATA_CANCEL_HINT);
    this.btnCancel1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/cancel22.png")));

    this.btnVerify1.setText(Global.MENU_VERIFY);
    this.btnVerify1.setMnemonic(Global.MENU_VERIFY_MNEMONIC);
    this.btnVerify1.setToolTipText(Global.MENU_VERIFY_HINT);
    this.btnVerify1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/verify22.png")));

  }

  // ---------------------------------------------------------------------------
  private void setupGrids()
  {
    final int lnSites = Global.MAX_POLYMER_SITES;
    final int lnLevels = Global.MAX_POLYMER_LEVELS;

    this.scrollSite = new JScrollPane[lnLevels][lnSites];
    this.grdSite = new BaseEditTable[lnLevels][lnSites];

    for (int lnLevel = 0; lnLevel < lnLevels; ++lnLevel)
    {
      for (int lnSite = 0; lnSite < lnSites; ++lnSite)
      {
        this.scrollSite[lnLevel][lnSite] = new JScrollPane();
        this.grdSite[lnLevel][lnSite] = new BaseEditTable(this.editGridModel[lnLevel][lnSite]);
        this.grdSite[lnLevel][lnSite].setupHeaderRenderer();

        this.scrollSite[lnLevel][lnSite].setPreferredSize(new Dimension(Global.EDIT_GRID_WIDTH, Global.EDIT_GRID_HEIGHT));
        this.scrollSite[lnLevel][lnSite].setMaximumSize(new Dimension(Global.EDIT_GRID_WIDTH, Global.EDIT_GRID_HEIGHT));
        this.scrollSite[lnLevel][lnSite].getViewport().add(this.grdSite[lnLevel][lnSite], null);

        this.grdSite[lnLevel][lnSite].setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        this.grdSite[lnLevel][lnSite].getColumnModel().getColumn(0).setPreferredWidth((int) (Global.EDIT_GRID_WIDTH * 0.5));
        this.grdSite[lnLevel][lnSite].getColumnModel().getColumn(1).setPreferredWidth((int) (Global.EDIT_GRID_WIDTH * 0.5));

        final FormattedIntegerField loIntegerField = new FormattedIntegerField();
        final FormattedDoubleField loDoubleField = new FormattedDoubleField();
        loDoubleField.setMinimumFractionDigits(0);
        loDoubleField.setMaximumFractionDigits(2);

        final MonomerCellEditor loIntegerEditor = new MonomerCellEditor(loIntegerField, this);
        final ReactionRateCellEditor loDoubleEditor = new ReactionRateCellEditor(loDoubleField, this);

        this.grdSite[lnLevel][lnSite].setDefaultEditor(Integer.class, loIntegerEditor);
        this.grdSite[lnLevel][lnSite].setDefaultEditor(Double.class, loDoubleEditor);
        this.grdSite[lnLevel][lnSite].setRowSelectionAllowed(false);
        this.grdSite[lnLevel][lnSite].setCellSelectionEnabled(true);

        new TableTextBoxMouseEventHandler(loIntegerField, this.grdSite[lnLevel][lnSite]);
        new TableTextBoxMouseEventHandler(loDoubleField, this.grdSite[lnLevel][lnSite]);
        new TableTextBoxMouseEventHandler(this.grdSite[lnLevel][lnSite], this.grdSite[lnLevel][lnSite]);
      }
    }

  }

  // ---------------------------------------------------------------------------
  private void setupListeners()
  {
    this.btnVerify1.addActionListener(this);
    this.btnSave1.addActionListener(this);
    this.btnCancel1.addActionListener(this);

    this.spnSites1.addChangeListener(this);
    this.spnLevels1.addChangeListener(this);
  }

  // ---------------------------------------------------------------------------
  private void updateSpinnersRelatedAccessibilities()
  {
    final int lnSites = (Integer) this.spnSites1.getValue();

    for (int i = 0; i < Global.MAX_POLYMER_SITES; ++i)
    {
      this.tabPane.setEnabledAt(i, (i < lnSites));
    }
    if (this.tabPane.getSelectedIndex() >= lnSites)
    {
      this.tabPane.setSelectedIndex(lnSites - 1);
    }

    final int lnLevels = (Integer) this.spnLevels1.getValue();

    for (int i = 0; i < this.txtTotalMonomerReacted.length; ++i)
    {
      final boolean llEnabled = (i < lnLevels);

      this.txtTotalMonomerReacted[i].setEnabled(llEnabled);
      this.lblLevel[i].setEnabled(llEnabled);
    }

    for (int lnLevel = 0; lnLevel < Global.MAX_POLYMER_LEVELS; ++lnLevel)
    {
      for (int lnSite = 0; lnSite < lnSites; ++lnSite)
      {
        final boolean llEnabled = (lnLevel < lnLevels);
        this.grdSite[lnLevel][lnSite].setEnabled(llEnabled);
        this.scrollSite[lnLevel][lnSite].setEnabled(llEnabled);
        this.grdSite[lnLevel][lnSite].getTableHeader().setEnabled(llEnabled);
      }
    }

  }

  // ---------------------------------------------------------------------------
  public int getResults()
  {
    return (this.fnResults);
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void removeListeners()
  {
    this.btnVerify1.removeActionListener(this);
    this.btnSave1.removeActionListener(this);
    this.btnCancel1.removeActionListener(this);

    this.spnSites1.removeChangeListener(this);
    this.spnLevels1.removeChangeListener(this);

    super.removeListeners();
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void releasePointers()
  {
    this.foPolymerParametersXML = null;

    super.releasePointers();
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface ChangeListener
  // ---------------------------------------------------------------------------
  @Override
  public void stateChanged(final ChangeEvent e)
  {
    final Object loObject = e.getSource();

    if (loObject == this.spnSites1)
    {
      this.updateSpinnersRelatedAccessibilities();
    }
    else if (loObject == this.spnLevels1)
    {
      this.updateSpinnersRelatedAccessibilities();
    }

  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface ActionListener
  // ---------------------------------------------------------------------------
  @Override
  public void actionPerformed(final ActionEvent e)
  {
    final Object loObject = e.getSource();

    boolean llDispose = true;

    if (loObject == this.btnVerify1)
    {
      llDispose = false;
      this.flushProperties();
      this.foPolymerParametersXML.verifyData(this, Global.VERIFY_DISPLAY_ALWAYS);
    }
    else if (loObject == this.btnCancel1)
    {
      this.fnResults = Global.EDIT_CANCEL;
    }
    else if (loObject == this.btnSave1)
    {
      this.flushProperties();
      this.foPolymerParametersXML.saveXMLData();

      this.fnResults = Global.EDIT_OKAY;
    }

    if (llDispose)
    {
      this.dispose();
    }
  }
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
}

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
class TableTextBoxMouseEventHandler extends MouseAdapter implements ActionListener
{
  private final JPopupMenu foPopupMenu = new JPopupMenu();
  private final JMenuItem foPopupInsert = new JMenuItem();
  private final JMenuItem foPopupDelete = new JMenuItem();

  private final JComponent foComponent;
  private final JTable foTable;
  private final EditingTabelModel foEditingTabelModel;

  // ---------------------------------------------------------------------------
  public TableTextBoxMouseEventHandler(final JComponent toComponent, final JTable toTable)
  {
    this.foComponent = toComponent;
    this.foTable = toTable;
    this.foEditingTabelModel = (EditingTabelModel) this.foTable.getModel();

    this.setupPopupMenu();
    this.setupListeners();
  }

  // ---------------------------------------------------------------------------
  private void setupPopupMenu()
  {
    this.foPopupInsert.setText(Global.POPUP_ACTION_INSERT);
    this.foPopupInsert.setMnemonic(Global.POPUP_ACTION_INSERT_MNEMONIC);
    this.foPopupInsert.setToolTipText(Global.POPUP_ACTION_INSERT_HINT);
    this.foPopupInsert.setActionCommand(Global.POPUP_ACTION_INSERT);

    this.foPopupDelete.setText(Global.POPUP_ACTION_DELETE);
    this.foPopupDelete.setMnemonic(Global.POPUP_ACTION_DELETE_MNEMONIC);
    this.foPopupDelete.setToolTipText(Global.POPUP_ACTION_DELETE_HINT);
    this.foPopupDelete.setActionCommand(Global.POPUP_ACTION_DELETE);

    this.foPopupMenu.add(this.foPopupInsert);
    this.foPopupMenu.add(this.foPopupDelete);
  }

  // ---------------------------------------------------------------------------
  private void setupListeners()
  {
    this.foComponent.addMouseListener(this);

    this.foPopupInsert.addActionListener(this);
    this.foPopupDelete.addActionListener(this);
  }

  // ---------------------------------------------------------------------------
  @Override
  public void mousePressed(final MouseEvent e)
  {
    if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK)
    {

      if (this.foTable.isEnabled())
      {
        if (this.foComponent instanceof JTable)
        {
          final Point loPoint = new Point(e.getX(), e.getY());
          final JTable loTable = (JTable) this.foComponent;
          final int lnCol = loTable.columnAtPoint(loPoint);
          final int lnRow = loTable.rowAtPoint(loPoint);
          loTable.changeSelection(lnRow, lnCol, false, false);
        }

        this.foPopupMenu.show(this.foComponent, e.getX(), e.getY());
      }
    }
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface ActionListener
  // ---------------------------------------------------------------------------
  @Override
  public void actionPerformed(final ActionEvent e)
  {
    final String lcCommand = e.getActionCommand();

    if (lcCommand.equals(Global.POPUP_ACTION_INSERT))
    {
      final int lnRow = this.foTable.getSelectedRow();
      // If you don't do this, and a cell is currently being edited, then the value
      // of the cell stays in the new row.
      this.foTable.getDefaultEditor(Integer.class).stopCellEditing();
      this.foTable.getDefaultEditor(Double.class).stopCellEditing();
      this.foEditingTabelModel.insertRow(lnRow);
    }
    else if (lcCommand.equals(Global.POPUP_ACTION_DELETE))
    {
      final int lnRow = this.foTable.getSelectedRow();
      // If you don't do this, and a cell is currently being edited, then the value
      // of the cell stays in the row that is moved up.
      this.foTable.getDefaultEditor(Integer.class).stopCellEditing();
      this.foTable.getDefaultEditor(Double.class).stopCellEditing();
      this.foEditingTabelModel.removeRow(lnRow);
    }
  }
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
}

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
class EditingTabelModel extends AbstractTableModel
{
  protected final Vector<Object[]> faData;

  private final String[] faColumnNames = {Global.EDIT_COLUMN_1, Global.EDIT_COLUMN_2};

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public EditingTabelModel(final int[][] taValues)
  {
    final int lnCount = taValues.length;

    // private final Vector<HighScopeProgram> foHighScopeList = new Vector<HighScopeProgram>();

    this.faData = new Vector<>((lnCount > 6) ? lnCount : 6);

    for (int i = 0; i < lnCount; i++)
    {
      // Remember: the 2nd column needs to be converted to percentages with 2 digits
      // to the right of the decimal.

      if (i == 0)
      {
        this.faData.add(new Object[]{Global.EDIT_FIRST_ROW, taValues[i][1] / 100.0});
      }

      else if (i == (lnCount - 1))
      {
        this.faData.add(new Object[]{Global.EDIT_LAST_ROW, taValues[i][1] / 100.0});
      }

      else
      {
        this.faData.add(new Object[]{taValues[i][0], taValues[i][1] / 100.0});
      }

    }
  }

  // ---------------------------------------------------------------------------
  @Override
  public boolean isCellEditable(final int tnRow, final int tnCol)
  {
    return (!this.isSpecialLabelCell(tnRow, tnCol));
  }

  // ---------------------------------------------------------------------------
  @Override
  public String getColumnName(final int tnCol)
  {
    return (this.faColumnNames[tnCol]);
  }

  // ---------------------------------------------------------------------------
  @Override
  public int getRowCount()
  {
    return (this.faData.size());
  }

  // ---------------------------------------------------------------------------
  @Override
  public int getColumnCount()
  {
    return (this.faColumnNames.length);
  }

  // ---------------------------------------------------------------------------
  @Override
  public Object getValueAt(final int tnRow, final int tnCol)
  {
    final Object[] loRow = this.faData.elementAt(tnRow);

    return (loRow[tnCol]);
  }

  // ---------------------------------------------------------------------------
  // This method only seems to be called after editing a cell, which is good.
  @Override
  public void setValueAt(final Object toValue, final int tnRow, final int tnCol)
  {
    final Object[] loRow = this.faData.get(tnRow);
    loRow[tnCol] = toValue;
    this.fireTableCellUpdated(tnRow, tnCol);
  }

  // ---------------------------------------------------------------------------
  @Override
  public Class<?> getColumnClass(final int tnCol)
  {
    if (tnCol == 0)
    {
      return (Integer.class);
    }

    return (Double.class);
  }

  // ---------------------------------------------------------------------------
  public void insertRow(int tnRow)
  {
    if ((tnRow >= 0) && (tnRow < this.faData.size()))
    {
      if (tnRow == 0)
      {
        tnRow = 1;
      }

      this.faData.insertElementAt(new Object[]{0, (double) 0}, tnRow);
      this.fireTableRowsInserted(tnRow, tnRow);
    }
    else
    {
      Util.errorMessage(null, "The row # of " + tnRow + " is invalid.");
    }
  }

  // ---------------------------------------------------------------------------
  public void removeRow(final int tnRow)
  {
    if ((tnRow >= 0) && (tnRow < this.faData.size()))
    {
      if ((tnRow != 0) && (tnRow != this.faData.size() - 1))
      {
        this.faData.removeElementAt(tnRow);
        this.fireTableRowsDeleted(tnRow, tnRow);
      }
      else
      {
        Util.errorMessage(null, "You cannot remove the first and last lines.");
      }
    }
    else
    {
      Util.errorMessage(null, "The row # of " + tnRow + " is invalid.");
    }
  }

  // ---------------------------------------------------------------------------
  private boolean isSpecialLabelCell(final int tnRow, final int tnCol)
  {
    return (tnCol == 0) && ((tnRow == 0) || (tnRow == this.getRowCount() - 1));

  }

  // ---------------------------------------------------------------------------
  public int[][] getDataArray()
  {
    final int lnRow = this.getRowCount();
    final int lnCol = this.getColumnCount();
    final int[][] laData = new int[lnRow][lnCol];

    Object loObject;
    Double loDouble;
    Integer loInteger;

    for (int y = 0; y < lnRow; ++y)
    {
      for (int x = 0; x < lnCol; ++x)
      {
        loObject = this.getValueAt(y, x);

        if ((y == 0) && (x == 0))
        {
          laData[y][x] = 1;
        }
        else if ((y == lnRow - 1) && (x == 0))
        {
          laData[y][x] = Integer.MAX_VALUE;
        }

        else if (loObject instanceof Double)
        {
          // Remember: the 2nd column needs to be converted from percentages with 2 digits
          // to the right of the decimal back to an integer value.
          loDouble = (Double) loObject;
          laData[y][x] = (int) (loDouble * 100.0);
        }
        else if (loObject instanceof Integer)
        {
          loInteger = (Integer) loObject;
          laData[y][x] = loInteger;
        }
      }
    }

    return (laData);
  }
  // ---------------------------------------------------------------------------
}

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
class MonomerCellEditor extends DefaultCellEditor
{
  private final FormattedIntegerField foTextField;

  private final Window foWindow;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public MonomerCellEditor(final FormattedIntegerField toTextField, final Window toWindow)
  {
    super(toTextField);

    this.foTextField = toTextField;
    this.foWindow = toWindow;

    this.setClickCountToStart(1);
  }

  // ---------------------------------------------------------------------------
  // Override DefaultCellEditor's getCellEditorValue method
  // to return an Integer, not a String:
  @Override
  public Object getCellEditorValue()
  {
    boolean llOkay = false;
    try
    {
      this.foTextField.commitEdit();
    }
    catch (final ParseException ignored)
    {
    }

    int lnValue = this.foTextField.getIntegerValue();
    final int lnErrorValue = lnValue;

    if (lnValue > Global.MAX_MONOMER_VALUE)
    {
      lnValue = Global.MAX_MONOMER_VALUE;
    }
    else if ((lnValue < Global.MIN_MONOMER_VALUE) && (lnValue != 0))
    {
      lnValue = Global.MIN_MONOMER_VALUE;
    }
    else
    {
      llOkay = true;
    }

    if (!llOkay)
    {
      Util.errorMessage(this.foWindow, "The value of this cell is '" + lnErrorValue + "'.\n\nHowever, it should be between '" + Global.MIN_MONOMER_VALUE + "' and '" + Global.MAX_MONOMER_VALUE + "': the number will be reset to '" + lnValue + "'.");
    }

    return (lnValue);
  }
  // ---------------------------------------------------------------------------
}

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
class ReactionRateCellEditor extends DefaultCellEditor
{
  private final FormattedDoubleField foTextField;
  private final Window foWindow;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public ReactionRateCellEditor(final FormattedDoubleField toTextField, final Window toWindow)
  {
    super(toTextField);

    this.foWindow = toWindow;
    this.foTextField = toTextField;

    this.setClickCountToStart(1);
  }

  // ---------------------------------------------------------------------------
  // Override DefaultCellEditor's getCellEditorValue method
  // to return a Double, not a String:
  @Override
  public Object getCellEditorValue()
  {
    boolean llOkay = false;
    try
    {
      this.foTextField.commitEdit();
    }
    catch (final ParseException ignored)
    {
    }

    double lnValue = this.foTextField.getDoubleValue();
    final double lnErrorValue = lnValue;

    if (lnValue > Global.MAX_REACTIONRATE_VALUE)
    {
      lnValue = Global.MAX_REACTIONRATE_VALUE;
    }
    else if ((lnValue < Global.MIN_REACTIONRATE_VALUE) && (lnValue != 0))
    {
      lnValue = Global.MIN_REACTIONRATE_VALUE;
    }
    else
    {
      llOkay = true;
    }

    if (!llOkay)
    {
      Util.errorMessage(this.foWindow, "The value of this cell is '" + lnErrorValue + "'.\n\nHowever, it should be between '" + Global.MIN_REACTIONRATE_VALUE + "' and '" + Global.MAX_REACTIONRATE_VALUE + "': the number will be reset to '" + lnValue + "'.");
    }

    return (lnValue);
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
