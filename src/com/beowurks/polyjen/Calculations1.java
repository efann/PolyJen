/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.polyjen;

import com.beowurks.BeoCommon.GridBagLayoutHelper;
import com.beowurks.BeoCommon.Util;
import com.beowurks.polyjen.Global.DistributionStructure;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class Calculations1 extends PolyJenBaseInternalFrame implements ActionListener, Runnable
{
  private final static int RESULTS_INVALID = 0;
  private final static int RESULTS_CANCELED = 1;
  private final static int RESULTS_COMPLETE = 2;

  private JFreeChart chtDistribution1;

  private JComboBox cboParameterList1;
  private final JCheckBox chkMoleculeCalculations1 = new JCheckBox();

  private final Global.ReactionParametersStructure[] faReactionParameters = new Global.ReactionParametersStructure[Global.MAX_POLYMER_LEVELS];

  private final int[][] faPolymerSites = new int[Global.DISTRIBUTION_LIMIT][];
  private Global.DistributionStructure[] faDistributions;

  private int fnInitiatorMolecules;
  private int fnSiteCount;
  private int fnMonomerLevelsUsed;
  private int fnTotalMonomers;

  private PolymerParametersXML foSelectedProperty;

  private ProgressMonitor foProgressMonitor;

  private AppProperties foAppProperties;

  private ChartPanel pnlChart1;

  private final ButtonGroup bgrpGenerate1 = new ButtonGroup();
  private JPanel pnlGenerate1 = null;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public Calculations1(final MainFrame toMainFrame, final String tcTitle)
  {
    super(toMainFrame, tcTitle);

    try
    {
      this.jbInit();
    }
    catch (final Exception ex)
    {
      ex.printStackTrace();
    }
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void jbInit() throws Exception
  {
    super.jbInit();

    this.foAppProperties = this.getMainFrame().foAppProperties;

    this.setupArrays();
    this.setupCharts();
    this.setupPanels();
    this.setupComboBoxes();
    this.setupOptionGroups();
    this.setupCheckBoxes();
    this.setupListeners();
    this.setupLayouts();
  }

  // ---------------------------------------------------------------------------
  private void setupCheckBoxes()
  {
    this.chkMoleculeCalculations1.setText("Molecule Calculations");
    this.chkMoleculeCalculations1.setCursor(new Cursor(Cursor.HAND_CURSOR));

    this.chkMoleculeCalculations1.setSelected(this.foAppProperties.getMoleculeCalculations());
  }

  // ---------------------------------------------------------------------------
  private void setupArrays()
  {
    final int lnLevels = this.faReactionParameters.length;
    for (int i = 0; i < lnLevels; ++i)
    {
      this.faReactionParameters[i] = new Global.ReactionParametersStructure();
    }

    this.faDistributions = new Global.DistributionStructure[Global.DISTRIBUTION_LIMIT];

    for (int i = 0; i < this.faDistributions.length; ++i)
    {
      this.faDistributions[i] = new Global.DistributionStructure();
    }
  }

  // ---------------------------------------------------------------------------
  private void setupLayouts()
  {
    final GridBagLayoutHelper loGridBag = new GridBagLayoutHelper();

    this.getContentPane().setLayout(loGridBag);

    loGridBag.setInsets(4, 10, 10, 10);
    this.getContentPane().add(this.cboParameterList1, loGridBag.getConstraint(0, 0, GridBagConstraints.NORTH, GridBagConstraints.NONE));

    this.getContentPane().add(this.pnlGenerate1, loGridBag.getConstraint(1, 0, GridBagConstraints.NORTH, GridBagConstraints.NONE));

    this.getContentPane().add(this.chkMoleculeCalculations1, loGridBag.getConstraint(2, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE));

    loGridBag.setInsets(4, 10, 10, 10);
    this.getContentPane().add(this.pnlChart1, loGridBag.getConstraint(0, 1, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH));
  }

  // ---------------------------------------------------------------------------
  private void setupComboBoxes()
  {
    this.cboParameterList1 = this.getMainFrame().foListComponents.getComboBox();

    final int lnHeight = Global.TEXT_HEIGHT;
    this.cboParameterList1.setPreferredSize(new Dimension(320, lnHeight));
    this.cboParameterList1.setMinimumSize(new Dimension(320, lnHeight));
    this.cboParameterList1.setCursor(new Cursor(Cursor.HAND_CURSOR));
    // You do not need to call setSelectedIndex for cboParameterList1. It gets set
    // by the routine ListComponents.matchComboBoxToTable.

    this.cboParameterList1.setToolTipText("Title of the currently selected polymerization parameters");

  }

  // ---------------------------------------------------------------------------
  private void setupOptionGroups() throws Exception
  {
    if (this.pnlGenerate1 == null)
    {
      throw new Exception("The panel(s) have not been initialized in the routine setupOptionGroups in Calculations.");
    }

    final int lnOption = this.foAppProperties.getGenerationRunOption();
    JRadioButton loRadio;

    loRadio = new JRadioButton(Global.GENERATION_OPTION_SEQ);
    loRadio.setActionCommand(Global.GENERATION_OPTION_SEQ);
    loRadio.setSelected(lnOption == Global.GENERATION_OPTION_SEQ_NBR);
    loRadio.setCursor(new Cursor(Cursor.HAND_CURSOR));
    loRadio.setToolTipText("The polymers are selected sequentially for reaction probability");

    this.bgrpGenerate1.add(loRadio);
    this.pnlGenerate1.add(loRadio);

    loRadio = new JRadioButton(Global.GENERATION_OPTION_RAN);
    loRadio.setActionCommand(Global.GENERATION_OPTION_RAN);
    loRadio.setSelected(lnOption == Global.GENERATION_OPTION_RAN_NBR);
    loRadio.setCursor(new Cursor(Cursor.HAND_CURSOR));
    loRadio.setToolTipText("The polymers are selected randomly for reaction probability");

    this.bgrpGenerate1.add(loRadio);
    this.pnlGenerate1.add(loRadio);
  }

  // ---------------------------------------------------------------------------
  private void setupPanels()
  {
    this.pnlGenerate1 = new JPanel();
    this.pnlGenerate1.setLayout(new BoxLayout(this.pnlGenerate1, BoxLayout.Y_AXIS));
    this.pnlGenerate1.setAlignmentY(Component.TOP_ALIGNMENT);
    this.pnlGenerate1.setAlignmentX(Component.LEFT_ALIGNMENT);
    this.pnlGenerate1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
  }

  // ---------------------------------------------------------------------------
  private void setupCharts()
  {
    this.chtDistribution1 = ChartFactory.createBarChart3D("Distribution", "# of Oligomers", // domain
        "Mole %", // range
        null, PlotOrientation.VERTICAL, false, true, // tooltips
        false // urls
    );

    this.chtDistribution1.setBackgroundPaint(new GradientPaint(0, 0, UIManager.getColor("Button.background"), 0, 1000, Color.darkGray, false));
    // skip some labels if they overlap...
    // this.chtDistribution1.getCategoryPlot().getDomainAxis().setSkipCategoryLabelsToFit(true);
    // For some reason, this fixes the problem of labels becoming an ellipse.
    this.chtDistribution1.getCategoryPlot().getDomainAxis().setMaximumCategoryLabelWidthRatio(10f);

    this.pnlChart1 = new ChartPanel(this.chtDistribution1);

    final Dimension ldScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

    final int lnChartWidth = (int) (ldScreenSize.width * 0.7);
    final int lnChartHeight = (int) (ldScreenSize.height * 0.5);

    this.pnlChart1.setPreferredSize(new Dimension(lnChartWidth, lnChartHeight));
    this.pnlChart1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
  }

  // ---------------------------------------------------------------------------
  private void setupListeners()
  {
    this.chkMoleculeCalculations1.addActionListener(this);

    this.getMainFrame().foListComponents.addComboListeners();
  }

  // ---------------------------------------------------------------------------
  private int determineGenerationOptionSelected()
  {
    int lnOption = 0;
    if (this.bgrpGenerate1.getSelection().getActionCommand().equals(Global.GENERATION_OPTION_SEQ))
    {
      lnOption = Global.GENERATION_OPTION_SEQ_NBR;
    }
    else if (this.bgrpGenerate1.getSelection().getActionCommand().equals(Global.GENERATION_OPTION_RAN))
    {
      lnOption = Global.GENERATION_OPTION_RAN_NBR;
    }

    return (lnOption);
  }

  // ---------------------------------------------------------------------------
  private void setProperties()
  {
    final AppProperties loProp = this.foAppProperties;

    loProp.setGenerationRunOption(this.determineGenerationOptionSelected());

    loProp.setMoleculeCalculations(this.chkMoleculeCalculations1.isSelected());
  }

  // ---------------------------------------------------------------------------
  protected void setParametersValuesToNull()
  {
    this.fnInitiatorMolecules = 0;

    this.fnSiteCount = 0;

    this.fnTotalMonomers = 0;

    for (int lnElements = 0; lnElements < Global.DISTRIBUTION_LIMIT; ++lnElements)
    {
      this.faPolymerSites[lnElements] = null;
    }

    for (int lnLevel = 0; lnLevel < Global.MAX_POLYMER_LEVELS; ++lnLevel)
    {
      this.faReactionParameters[lnLevel].TotalMonomerReacted = 0;

      for (int lnSite = 0; lnSite < Global.MAX_POLYMER_SITES; ++lnSite)
      {
        this.faReactionParameters[lnLevel].ReactionRates[lnSite] = null;
      }
    }

    for (final DistributionStructure laDistribution : this.faDistributions)
    {
      laDistribution.Distribution = null;
    }

  }

  // ---------------------------------------------------------------------------
  // This particular function does not set the Distribution Ranges. That is
  // set in the SetDistributionRanges after all of the reactions have taken
  // place.
  protected void setParametersValues(final PolymerParametersXML toProp)
  {
    this.fnInitiatorMolecules = toProp.getInitiatorMolecules();

    this.fnSiteCount = toProp.getSites();
    this.fnMonomerLevelsUsed = toProp.getLevels();

    this.fnTotalMonomers = toProp.getTotalMonomersFromUsedLevels();

    // REMEMBER: You don't need to use a routine similar to memset to set all values
    // to zero: it's already been done. Read below.
    // http://java.sun.com/docs/books/jls/second_edition/html/typesValues.doc.html#96595
    //
    // Each class variable, instance variable, or array component is initialized with a default value when it is created
    // For type byte, the default value is zero, that is, the value of (byte)0.
    // For type short, the default value is zero, that is, the value of (short)0.
    // For type int, the default value is zero, that is, 0.
    // For type long, the default value is zero, that is, 0L.
    // For type float, the default value is positive zero, that is, 0.0f.
    // For type double, the default value is positive zero, that is, 0.0d.
    // For type char, the default value is the null character, that is, '\u0000'.
    // For type boolean, the default value is false.

    for (int lnSite = 0; lnSite < this.fnSiteCount; ++lnSite)
    {
      this.faPolymerSites[lnSite] = new int[this.fnInitiatorMolecules];
    }

    // Now you need to initialize the array for the complete molecule.
    this.faPolymerSites[Global.POLYMER_MOLECULE] = new int[this.fnInitiatorMolecules];

    for (int lnLevel = 0; lnLevel < this.fnMonomerLevelsUsed; ++lnLevel)
    {
      this.faReactionParameters[lnLevel].TotalMonomerReacted = toProp.getMonomerLevelReacted(lnLevel);

      for (int lnSite = 0; lnSite < this.fnSiteCount; ++lnSite)
      {
        final int[][] laValues = toProp.getArrays(lnLevel, lnSite);
        this.faReactionParameters[lnLevel].ReactionRates[lnSite] = new int[laValues.length][laValues[0].length];

        final int lnYLength = laValues.length;
        for (int lnY = 0; lnY < lnYLength; ++lnY)
        {
          final int lnXLength = laValues[0].length;

          System.arraycopy(laValues[lnY], 0, this.faReactionParameters[lnLevel].ReactionRates[lnSite][lnY], 0, lnXLength);
        }
      }
    }
  }

  // ---------------------------------------------------------------------------
  private boolean isRunCompleted()
  {
    return (this.faPolymerSites[0] != null);
  }

  // ---------------------------------------------------------------------------
  protected void redrawChart()
  {
    final JFreeChart loChart = this.chtDistribution1;

    // create the dataset...
    final DefaultCategoryDataset loDataset = new DefaultCategoryDataset();

    final double lnTotalMolecules = this.fnInitiatorMolecules;
    final int lnSiteCount = this.fnSiteCount;
    final int lnMoleculeOffset = Global.POLYMER_MOLECULE;
    final boolean llMoleculeDisplay = this.chkMoleculeCalculations1.isSelected();

    int lnOverAllMin = Integer.MAX_VALUE;
    int lnOverAllMax = Integer.MIN_VALUE;
    for (int lnSite = 0; lnSite < lnSiteCount; ++lnSite)
    {
      lnOverAllMin = Math.min(lnOverAllMin, this.faDistributions[lnSite].Minimum);
      lnOverAllMax = Math.max(lnOverAllMax, this.faDistributions[lnSite].Maximum);
    }

    // This way, the range won't be wider than needed if Molecules are not being
    // displayed.
    if (llMoleculeDisplay)
    {
      lnOverAllMin = Math.min(lnOverAllMin, this.faDistributions[lnMoleculeOffset].Minimum);
      lnOverAllMax = Math.max(lnOverAllMax, this.faDistributions[lnMoleculeOffset].Maximum);
    }

    int lnMin, lnMax;
    for (int lnSite = 0; lnSite < lnSiteCount; ++lnSite)
    {
      lnMin = this.faDistributions[lnSite].Minimum;
      lnMax = this.faDistributions[lnSite].Maximum;
      for (int i = lnOverAllMin; i <= lnOverAllMax; ++i)
      {

        if ((i >= lnMin) && (i <= lnMax))
        {
          loDataset.addValue(100.00 * this.faDistributions[lnSite].Distribution[i - lnMin] / lnTotalMolecules, "Site #" + (lnSite + 1), Integer.toString(i));
        }
        else
        {
          loDataset.addValue(0, "Site #" + (lnSite + 1), Integer.toString(i));
        }

      }
    }

    if (llMoleculeDisplay)
    {
      lnMin = this.faDistributions[lnMoleculeOffset].Minimum;
      lnMax = this.faDistributions[lnMoleculeOffset].Maximum;
      for (int i = lnOverAllMin; i <= lnOverAllMax; ++i)
      {

        if ((i >= lnMin) && (i <= lnMax))
        {
          loDataset.addValue(100.00 * this.faDistributions[lnMoleculeOffset].Distribution[i - lnMin] / lnTotalMolecules, "Molecule", Integer.toString(i));
        }
        else
        {
          loDataset.addValue(0, "Molecule", Integer.toString(i));
        }

      }
    }

    loChart.getCategoryPlot().setDataset(loDataset);
    // loChart.setLegend(new StandardLegend(loChart));
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void finalOperations()
  {
    this.setProperties();

    super.finalOperations();
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void removeListeners()
  {
    this.chkMoleculeCalculations1.removeActionListener(this);

    this.getMainFrame().foListComponents.removeComboListeners();

    super.removeListeners();
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void releasePointers()
  {
    this.chtDistribution1 = null;

    // Release the combobox that's part of ListComponents.
    this.cboParameterList1 = null;

    super.releasePointers();
  }

  // ---------------------------------------------------------------------------
  private void sequentialGeneration()
  {
    final Random loRandomChance = new Random();
    final int lnRandomChanceLimit = Global.ENGINE_LIMIT;

    int lnReactionTrack = 0;
    int lnAtom;
    int lnSite;
    int lnChance;
    int[][] laRates;
    int lnRandomNumber;
    final int lnLevelCount = this.fnMonomerLevelsUsed;
    final int lnSiteCount = this.fnSiteCount;
    final int lnInitiatorMolecules = this.fnInitiatorMolecules;
    final int lnTotalMonomers = this.fnTotalMonomers;

    int lnTrackProgress = -1;
    int lnCurrentProgress;

    for (int lnLevel = 0; lnLevel < lnLevelCount; ++lnLevel)
    {
      final int lnLevelReaction = this.faReactionParameters[lnLevel].TotalMonomerReacted;

      boolean llContinue = true;
      while (llContinue)
      {
        for (lnSite = 0; (lnSite < lnSiteCount) && (llContinue); ++lnSite)
        {
          for (lnAtom = 0; (lnAtom < lnInitiatorMolecules) && (llContinue); ++lnAtom)
          {
            laRates = this.faReactionParameters[lnLevel].ReactionRates[lnSite];
            lnChance = -1;

            for (final int[] laRate : laRates)
            {
              // Remember: it's less than laRates[y][0] not less than or equal to. Y is equal to
              // the reaction #, not the number of oligomers attached whereas
              // this.faPolymerSites[lnSite][lnAtom] is the number of oligomers attached.
              if (this.faPolymerSites[lnSite][lnAtom] < laRate[0])
              {
                lnChance = laRate[1];
                break;
              }
            }

            lnRandomNumber = loRandomChance.nextInt(lnRandomChanceLimit);

            // nextInt returns a random number between 0 and (num-1).
            // In the case of RANDOM_LIMIT (currently 10000),
            // 0 - 9999. Therefore if the lnChance = 0.14, which
            // when converted is 14, then the valid range
            // should be from 0 - 13. So lnRandomNumber must less than lnChance,
            // not less than or equal.
            if (lnRandomNumber < lnChance)
            {
              ++this.faPolymerSites[lnSite][lnAtom];
              ++lnReactionTrack;
              llContinue = (lnReactionTrack < lnLevelReaction);
              lnCurrentProgress = (int) (((double) lnReactionTrack / (double) lnTotalMonomers) * 100.0);
              if (lnTrackProgress != lnCurrentProgress)
              {
                llContinue = !this.foProgressMonitor.isCanceled();
                if (llContinue)
                {
                  lnTrackProgress = lnCurrentProgress;
                  this.foProgressMonitor.setProgress(lnCurrentProgress);
                }
              }
            }
          }
        }

      }
    }
  }

  // ---------------------------------------------------------------------------
  private void randomGeneration()
  {
    final Random loRandomChance = new Random(System.currentTimeMillis());
    final Random loRandomSite = new Random(System.currentTimeMillis() + 1);
    final Random loRandomAtom = new Random(System.currentTimeMillis() + 3);

    final int lnRandomChanceLimit = Global.ENGINE_LIMIT;

    int lnReactionTrack = 0;
    int lnAtom;
    int lnSite;
    int lnChance;
    int[][] laRates;
    int lnRandomNumber;
    final int lnLevelCount = this.fnMonomerLevelsUsed;
    final int lnSiteCount = this.fnSiteCount;
    final int lnInitiatorMolecules = this.fnInitiatorMolecules;
    final int lnTotalMonomers = this.fnTotalMonomers;

    int lnTrackProgress = -1;
    int lnCurrentProgress;

    for (int lnLevel = 0; lnLevel < lnLevelCount; ++lnLevel)
    {
      final int lnLevelReaction = this.faReactionParameters[lnLevel].TotalMonomerReacted;
      boolean llContinue = true;
      while (llContinue)
      {
        lnSite = loRandomSite.nextInt(lnSiteCount);
        lnAtom = loRandomAtom.nextInt(lnInitiatorMolecules);

        laRates = this.faReactionParameters[lnLevel].ReactionRates[lnSite];
        lnChance = -1;

        for (final int[] laRate : laRates)
        {
          // Remember: it's less than laRates[y][0] not less than or equal to. Y is equal to
          // the reaction #, not the number of oligomers attached whereas
          // this.faPolymerSites[lnSite][lnAtom] is the number of oligomers attached.
          if (this.faPolymerSites[lnSite][lnAtom] < laRate[0])
          {
            lnChance = laRate[1];
            break;
          }
        }

        lnRandomNumber = loRandomChance.nextInt(lnRandomChanceLimit);

        // nextInt returns a random number between 0 and (num-1).
        // In the case of RANDOM_LIMIT (currently 10000),
        // 0 - 9999. Therefore if the lnChance = 0.14, which
        // when converted is 14, then the valid range
        // should be from 0 - 13. So lnRandomNumber must less than lnChance,
        // not less than or equal.
        if (lnRandomNumber < lnChance)
        {
          ++this.faPolymerSites[lnSite][lnAtom];
          ++lnReactionTrack;
          llContinue = (lnReactionTrack < lnLevelReaction);
          lnCurrentProgress = (int) (((double) lnReactionTrack / (double) lnTotalMonomers) * 100.0);
          if (lnTrackProgress != lnCurrentProgress)
          {
            llContinue = !this.foProgressMonitor.isCanceled();
            if (llContinue)
            {
              lnTrackProgress = lnCurrentProgress;
              this.foProgressMonitor.setProgress(lnCurrentProgress);
            }
          }
        }
      }
    }
  }

  // ---------------------------------------------------------------------------
  private void setDistributionRanges()
  {
    final int lnInitiatorMolecules = this.fnInitiatorMolecules;
    final int lnSiteCount = this.fnSiteCount;

    // Get the total size for each molecule.
    for (int lnAtom = 0; lnAtom < lnInitiatorMolecules; ++lnAtom)
    {
      int lnTotal = 0;
      for (int lnSite = 0; lnSite < lnSiteCount; ++lnSite)
      {
        lnTotal += this.faPolymerSites[lnSite][lnAtom];
      }

      this.faPolymerSites[Global.POLYMER_MOLECULE][lnAtom] = lnTotal;
    }

    int lnMax;
    int lnMin;
    int lnSize;
    // Now get the distribution for each Site.
    for (int lnSite = 0; lnSite < lnSiteCount; ++lnSite)
    {
      lnMax = 0;
      lnMin = Integer.MAX_VALUE;
      for (int lnAtom = 0; lnAtom < lnInitiatorMolecules; ++lnAtom)
      {
        if (lnMin > this.faPolymerSites[lnSite][lnAtom])
        {
          lnMin = this.faPolymerSites[lnSite][lnAtom];
        }

        if (lnMax < this.faPolymerSites[lnSite][lnAtom])
        {
          lnMax = this.faPolymerSites[lnSite][lnAtom];
        }

      }

      lnSize = lnMax - lnMin + 1;
      this.faDistributions[lnSite].Maximum = lnMax;
      this.faDistributions[lnSite].Minimum = lnMin;
      this.faDistributions[lnSite].Distribution = new int[lnSize];

      for (int lnAtom = 0; lnAtom < lnInitiatorMolecules; ++lnAtom)
      {
        ++this.faDistributions[lnSite].Distribution[this.faPolymerSites[lnSite][lnAtom] - lnMin];
      }

    }

    // Now get the distribution for the molecules.
    lnMax = 0;
    lnMin = Integer.MAX_VALUE;
    final int lnMoleculeOffset = Global.POLYMER_MOLECULE;
    for (int lnAtom = 0; lnAtom < lnInitiatorMolecules; ++lnAtom)
    {

      if (lnMin > this.faPolymerSites[lnMoleculeOffset][lnAtom])
      {
        lnMin = this.faPolymerSites[lnMoleculeOffset][lnAtom];
      }

      if (lnMax < this.faPolymerSites[lnMoleculeOffset][lnAtom])
      {
        lnMax = this.faPolymerSites[lnMoleculeOffset][lnAtom];
      }

    }

    lnSize = lnMax - lnMin + 1;
    this.faDistributions[lnMoleculeOffset].Maximum = lnMax;
    this.faDistributions[lnMoleculeOffset].Minimum = lnMin;
    this.faDistributions[lnMoleculeOffset].Distribution = new int[lnSize];

    for (int lnAtom = 0; lnAtom < lnInitiatorMolecules; ++lnAtom)
    {
      ++this.faDistributions[lnMoleculeOffset].Distribution[this.faPolymerSites[lnMoleculeOffset][lnAtom] - lnMin];
    }

  }

  // ---------------------------------------------------------------------------
  protected void runCalculations()
  {
    this.setParametersValuesToNull();

    this.foSelectedProperty = this.getMainFrame().foListComponents.getPropertiesFromComboBoxSelection();
    if (this.foSelectedProperty.verifyData(this, Global.VERIFY_DISPLAY_ON_ERROR))
    {
      // Though it's a little odd, I think that you should set the cursor here
      // as there is a slight delay before run is called.
      this.setBusy(true);

      this.setParametersValuesToNull();
      this.setParametersValues(this.foSelectedProperty);

      final Thread loThread = new Thread(this);
      loThread.setPriority(Thread.NORM_PRIORITY);
      loThread.start();
    }
  }

  // ---------------------------------------------------------------------------
  protected JFreeChart getDistributionChart()
  {
    return (this.chtDistribution1);
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface ActionListener
  // ---------------------------------------------------------------------------
  @Override
  public void actionPerformed(final ActionEvent e)
  {
    final Object loObject = e.getSource();

    if (loObject == this.chkMoleculeCalculations1)
    {
      if (this.isRunCompleted())
      {
        this.redrawChart();
      }
    }
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface Runnable
  // ---------------------------------------------------------------------------
  @Override
  public void run()
  {
    final Date loDateBegin = new Date();

    // If you pass a string for the message, ProgressMonitor and JOptionPane.showMessageDialog won't format the html.
    // However, the JLabel object can. So just pass a JLabel. I love this language.
    this.foProgressMonitor = new ProgressMonitor(this, new JLabel("<html><font face=\"Arial\">Running <b>" + this.foSelectedProperty.getTitle() + "</b>. . . .</font></html>"), "", 0, 100);

    this.foProgressMonitor.setProgress(0);
    this.foProgressMonitor.setMillisToDecideToPopup(100);

    boolean llValidOption = true;
    final int lnOption = this.determineGenerationOptionSelected();

    switch (lnOption)
    {
      case Global.GENERATION_OPTION_SEQ_NBR:
        this.sequentialGeneration();
        break;

      case Global.GENERATION_OPTION_RAN_NBR:
        this.randomGeneration();
        break;

      default:
        llValidOption = false;
        break;
    }

    if (llValidOption)
    {
      if (!this.foProgressMonitor.isCanceled())
      {
        this.setDistributionRanges();
        this.redrawChart();
      }
    }

    int lnResults = Calculations1.RESULTS_COMPLETE;

    if (!llValidOption)
    {
      lnResults = Calculations1.RESULTS_INVALID;
    }
    else if (this.foProgressMonitor.isCanceled())
    {
      lnResults = Calculations1.RESULTS_CANCELED;
    }

    this.foProgressMonitor.close();

    this.setBusy(false);

    switch (lnResults)
    {
      case Calculations1.RESULTS_COMPLETE:
        Util.infoMessageInThread(this, new JLabel("<html><font face=\"Arial\">The calculations for <b>" + this.foSelectedProperty.getTitle() + "</b> have successfully completed.<br><br><i>(" + Util.displayTimeDifference(loDateBegin, new Date(), 2) + ")</i><br></font></html>"));
        break;

      case Calculations1.RESULTS_CANCELED:
        Util.errorMessageInThread(this, new JLabel("<html><font face=\"Arial\">You canceled the calculations for <b>" + this.foSelectedProperty.getTitle() + "</b>.</font></html>"));
        break;

      case Calculations1.RESULTS_INVALID:
        Util.errorMessageInThread(this, new JLabel("<html><font face=\"Arial\">Unable to determine the generation option for <b>" + this.foSelectedProperty.getTitle() + "</b>.<br><br>Please contact support@beowurks.com</font></html>"));
        break;
    }

    if (lnResults != Calculations1.RESULTS_COMPLETE)
    {
      this.setParametersValuesToNull();
      this.chtDistribution1.getCategoryPlot().setDataset(null);
    }
  }
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
