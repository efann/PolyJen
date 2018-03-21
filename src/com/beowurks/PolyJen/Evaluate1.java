/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2018, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.PolyJen;

import com.beowurks.BeoCommon.FormattedIntegerField;
import com.beowurks.BeoCommon.GridBagLayoutHelper;
import com.beowurks.BeoCommon.Util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.util.Date;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class Evaluate1 extends PolyJenBaseInternalFrame implements Runnable
{
  protected ChartButton btnSpreadChart1;
  protected ChartButton btnDistributionChart1;

  protected int[] faElements;
  protected int[] faDistribution;

  protected int fnDistributionMax;
  protected int fnDistributionMin;

  private int fnInitiatorMolecules;
  private int fnMonomerMolecules;

  private AppProperties foAppProperties;

  private EvaluateSpreadDataset foSpreadXYData;
  private EvaluateDistributionDataset foDistributionXYData;
  private JFreeChart chtSpread1;
  private JFreeChart chtDistribution1;

  private final FormattedIntegerField txtInitiatorMolecules1 = new FormattedIntegerField();

  private final FormattedIntegerField txtMonomerMolecules1 = new FormattedIntegerField();

  private final JLabel lblInitiatorMolecules1 = new JLabel();
  private final JLabel lblMonomerMolecules1 = new JLabel();

  private final Box boxButtons1 = Box.createVerticalBox();
  private final JPanel pnlTextElements1 = new JPanel();

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public Evaluate1(final MainFrame toMainFrame, final String tcTitle)
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

    this.getProperties();

    this.setupTextBoxes();
    this.setupCharts();
    this.resetArrays();
    this.setupLabels();
    this.setupLayouts();
  }

  // ---------------------------------------------------------------------------
  private void setupLayouts()
  {
    GridBagLayoutHelper loGridBag = new GridBagLayoutHelper();
    this.pnlTextElements1.setLayout(loGridBag);

    loGridBag.setInsets(4, 5, 4, 4);
    this.pnlTextElements1.add(this.lblInitiatorMolecules1, loGridBag.getConstraint(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE));
    loGridBag.setInsetDefaults();
    this.pnlTextElements1.add(this.txtInitiatorMolecules1, loGridBag.getConstraint(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE));
    loGridBag.setInsets(4, 5, 4, 4);
    this.pnlTextElements1.add(this.lblMonomerMolecules1, loGridBag.getConstraint(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
    loGridBag.setInsetDefaults();
    this.pnlTextElements1.add(this.txtMonomerMolecules1, loGridBag.getConstraint(1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));

    loGridBag = new GridBagLayoutHelper();
    this.getContentPane().setLayout(loGridBag);
    loGridBag.setInsets(6, 4, 4, 4);
    this.getContentPane().add(this.pnlTextElements1, loGridBag.getConstraint(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE));
    loGridBag.setInsets(10, 4, 4, 8);
    this.getContentPane().add(this.boxButtons1, loGridBag.getConstraint(2, 0, GridBagConstraints.EAST, GridBagConstraints.NONE));
    loGridBag.setInsets(4, 10, 4, 10);
    this.getContentPane().add(this.btnSpreadChart1, loGridBag.getConstraint(0, 2, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH));
    loGridBag.setInsets(4, 10, 10, 10);
    this.getContentPane().add(this.btnDistributionChart1, loGridBag.getConstraint(0, 3, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH));
  }

  // ---------------------------------------------------------------------------
  private void setupCharts()
  {
    this.chtSpread1 = this.createSpreadChart();
    this.chtDistribution1 = this.createDistrutionChart();

    this.btnSpreadChart1 = new ChartButton(this.chtSpread1, this.foSpreadXYData);
    this.btnDistributionChart1 = new ChartButton(this.chtDistribution1, this.foDistributionXYData);

    this.btnSpreadChart1.updateChart();
    this.btnDistributionChart1.updateChart();

    this.btnSpreadChart1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    this.btnDistributionChart1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
  }

  // ---------------------------------------------------------------------------
  private JFreeChart createSpreadChart()
  {
    this.foSpreadXYData = new EvaluateSpreadDataset(this);

    final JFreeChart loChart = ChartFactory.createXYLineChart("Evaluate Spread", "Initiator Molecule", "Monomers Reacted", this.foSpreadXYData, PlotOrientation.VERTICAL, false, true, // tooltips
            false // urls
    );

    final XYPlot loPlot = loChart.getXYPlot();
    loPlot.setRenderer(new XYDotRenderer());

    loChart.setBackgroundPaint(new GradientPaint(0, 0, UIManager.getColor("Button.background"), 0, 1000, Color.darkGray, false));

    return (loChart);
  }

  // ---------------------------------------------------------------------------
  private JFreeChart createDistrutionChart()
  {
    this.foDistributionXYData = new EvaluateDistributionDataset(this);

    final JFreeChart loChart = ChartFactory.createXYLineChart("Evaluate Distribution", "# of Monomers in Polymer", "# of Initiator Molecules", this.foDistributionXYData, PlotOrientation.VERTICAL, false, true, // tooltips
            false // urls
    );

    loChart.setBackgroundPaint(new GradientPaint(0, 0, UIManager.getColor("Button.background"), 0, 1000, Color.darkGray, false));

    return (loChart);
  }

  // ---------------------------------------------------------------------------
  private void setupTextBoxes()
  {
    final Dimension loDimension = new Dimension(100, (int) this.txtMonomerMolecules1.getPreferredSize().getHeight());

    this.txtMonomerMolecules1.setMinimumSize(loDimension);
    this.txtMonomerMolecules1.setPreferredSize(loDimension);
    this.txtMonomerMolecules1.setMaximumSize(loDimension);

    this.txtInitiatorMolecules1.setMinimumSize(loDimension);
    this.txtInitiatorMolecules1.setPreferredSize(loDimension);
    this.txtInitiatorMolecules1.setMaximumSize(loDimension);

    this.txtMonomerMolecules1.setMinValue(Global.ENGINE_MIN_MONOMERS);
    this.txtMonomerMolecules1.setMaxValue(Global.ENGINE_MAX_MONOMERS);

    this.txtInitiatorMolecules1.setMinValue(Global.ENGINE_MIN_INITIATORS);
    this.txtInitiatorMolecules1.setMaxValue(Global.ENGINE_MAX_INITIATORS);
  }

  // ---------------------------------------------------------------------------
  private void setupLabels()
  {
    this.lblInitiatorMolecules1.setText("# of Initiator Molecules");
    this.lblMonomerMolecules1.setText("# of Monomer Molecules");
  }

  // ---------------------------------------------------------------------------
  private void getProperties()
  {
    if ((this.txtInitiatorMolecules1 == null) || (this.txtMonomerMolecules1 == null))
    {
      Global.errorException(this, new Exception("txtInitiatorMolecules1 and/or txtMonomerMolecules1 have not yet been initialized"));
      return;
    }

    final AppProperties loProp = this.foAppProperties;

    int lnValue;

    lnValue = loProp.getEvaluateInitiatorMolecules();
    this.txtInitiatorMolecules1.setValue(lnValue);

    lnValue = loProp.getEvaluateMonomerMolecules();
    this.txtMonomerMolecules1.setValue(lnValue);
  }

  // ---------------------------------------------------------------------------
  private void setProperties()
  {
    final AppProperties loProp = this.foAppProperties;

    loProp.setEvaluateInitiatorMolecules(this.txtInitiatorMolecules1.getIntegerValue());
    loProp.setEvaluateMonomerMolecules(this.txtMonomerMolecules1.getIntegerValue());
  }

  // ---------------------------------------------------------------------------
  private boolean setArrays()
  {
    boolean llOkay = false;

    try
    {
      this.fnInitiatorMolecules = this.txtInitiatorMolecules1.getIntegerValue();
      this.fnMonomerMolecules = this.txtMonomerMolecules1.getIntegerValue();
    }
    catch (final NumberFormatException e)
    {
      this.fnInitiatorMolecules = 0;
      this.fnMonomerMolecules = 0;
    }

    if (this.fnInitiatorMolecules > 0)
    {
      if ((this.fnInitiatorMolecules >= Global.ENGINE_MIN_INITIATORS) && (this.fnInitiatorMolecules <= Global.ENGINE_MAX_INITIATORS) && (this.fnMonomerMolecules >= Global.ENGINE_MIN_MONOMERS) && (this.fnMonomerMolecules <= Global.ENGINE_MAX_MONOMERS))
      {
        final int lnRandom = this.fnInitiatorMolecules;
        this.faElements = new int[lnRandom];
        for (int i = 0; i < lnRandom; ++i)
        {
          this.faElements[i] = 0;
        }

        llOkay = true;
      }
    }

    return (llOkay);
  }

  // ---------------------------------------------------------------------------
  protected void resetArrays()
  {
    this.faElements = null;
    this.faDistribution = null;
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void finalOperations()
  {
    this.resetArrays();
    this.setProperties();

    super.finalOperations();
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void removeListeners()
  {
    this.btnSpreadChart1.removeListeners();
    this.btnDistributionChart1.removeListeners();

    super.removeListeners();
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void releasePointers()
  {
    this.foSpreadXYData.releasePointers();
    this.foDistributionXYData.releasePointers();

    this.foDistributionXYData = null;
    this.foSpreadXYData = null;
    this.chtDistribution1 = null;
    this.chtSpread1 = null;

    super.releasePointers();
  }

  // ---------------------------------------------------------------------------
  protected void runCalculations()
  {
    if (this.setArrays())
    {
      final Thread loThread = new Thread(this);
      loThread.setPriority(Thread.NORM_PRIORITY);
      loThread.start();
    }
    else
    {
      Util.errorMessage(this, "There was an error in setting the arrays.\n\n- The # of initiators must be in the range from " + Global.ENGINE_MIN_INITIATORS + " through " + Global.ENGINE_MAX_INITIATORS + "\n- The # of monomers must be in the range from " + Global.ENGINE_MIN_MONOMERS + " through " + Global.ENGINE_MAX_MONOMERS + ".");
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

    this.setBusy(true);

    final ProgressMonitor loProgressMonitor = new ProgressMonitor(this, "Evaluating engine. . . .", "", 0, 100);

    loProgressMonitor.setProgress(0);
    loProgressMonitor.setMillisToDecideToPopup(100);

    final Random loRandomGenerator = new Random();
    final int lnInitiatorMolecules = this.fnInitiatorMolecules;
    final int lnMonomerMolecules = this.fnMonomerMolecules;

    final int laElements[] = this.faElements;

    int lnTrackProgress = -1;
    int lnCurrentProgress;

    for (int i = 0; i < lnMonomerMolecules; i++)
    {
      lnCurrentProgress = (int) (((double) i / (double) lnMonomerMolecules) * 100.0);
      if (lnTrackProgress != lnCurrentProgress)
      {
        if (!loProgressMonitor.isCanceled())
        {
          lnTrackProgress = lnCurrentProgress;
          loProgressMonitor.setProgress(lnCurrentProgress);
        }
        else
        {
          break;
        }
      }

      ++laElements[loRandomGenerator.nextInt(lnInitiatorMolecules)];
    }

    if (!loProgressMonitor.isCanceled())
    {
      this.fnDistributionMax = 0;
      this.fnDistributionMin = Integer.MAX_VALUE;

      for (int i = 0; i < lnInitiatorMolecules; ++i)
      {
        if (this.fnDistributionMax < laElements[i])
        {
          this.fnDistributionMax = laElements[i];
        }
        if (this.fnDistributionMin > laElements[i])
        {
          this.fnDistributionMin = laElements[i];
        }
      }

      final int lnDistribution = this.fnDistributionMax - this.fnDistributionMin + 1;
      this.faDistribution = new int[lnDistribution];
      final int laDistribution[] = this.faDistribution;

      for (int i = 0; i < lnDistribution; ++i)
      {
        laDistribution[i] = 0;
      }

      for (int i = 0; i < this.fnInitiatorMolecules; ++i)
      {
        ++laDistribution[laElements[i] - this.fnDistributionMin];
      }

    }
    else
    {
      this.resetArrays();
      Util.errorMessageInThread(this, "You canceled the current operation.");
    }

    SwingUtilities.invokeLater(() ->
    {
      Evaluate1.this.btnSpreadChart1.updateChart();
      Evaluate1.this.btnDistributionChart1.updateChart();
    });

    final boolean llCompleted = !loProgressMonitor.isCanceled();
    loProgressMonitor.close();

    this.setBusy(false);

    if (llCompleted)
    {
      Util.infoMessageInThread(this, new JLabel("<html><font face=\"Arial\">The evaluation of the engine has completed.<br><br><i>(" + Util.displayTimeDifference(loDateBegin, new Date(), 2) + ")</i><br></font></html>"));
    }
  }
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
}

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
class EvaluateDistributionDataset extends BaseAbstractSeriesDataset
{
  private Evaluate1 foEvaluate;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public EvaluateDistributionDataset(final Evaluate1 toEvaluate)
  {
    this.foEvaluate = toEvaluate;
  }

  // ---------------------------------------------------------------------------
  @Override
  public Number getX(final int series, final int item)
  {
    return (item + this.foEvaluate.fnDistributionMin);
  }

  // ---------------------------------------------------------------------------
  @Override
  public Number getY(final int series, final int item)
  {
    final int lnValue = (this.foEvaluate.faDistribution == null) ? 0 : this.foEvaluate.faDistribution[item];
    return (lnValue);
  }

  // ---------------------------------------------------------------------------
  @Override
  public int getSeriesCount()
  {
    return ((this.foEvaluate.faDistribution == null) ? 0 : 1);
  }

  // ---------------------------------------------------------------------------
  public String getSeriesName(final int series)
  {
    return ("Distribution");
  }

  // ---------------------------------------------------------------------------
  @Override
  public String getSeriesKey(final int series)
  {
    return ("Distribution");
  }

  // ---------------------------------------------------------------------------
  @Override
  public int getItemCount(final int series)
  {
    return ((this.foEvaluate.faDistribution == null) ? 0 : this.foEvaluate.faDistribution.length);
  }

  // ---------------------------------------------------------------------------
  public void releasePointers()
  {
    this.foEvaluate = null;
  }
  // ---------------------------------------------------------------------------
}

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
class EvaluateSpreadDataset extends BaseAbstractSeriesDataset
{
  private Evaluate1 foEvaluate;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public EvaluateSpreadDataset(final Evaluate1 toEvaluate)
  {
    this.foEvaluate = toEvaluate;
  }

  // ---------------------------------------------------------------------------
  @Override
  public Number getX(final int series, final int item)
  {
    return (item);
  }

  // ---------------------------------------------------------------------------
  @Override
  public Number getY(final int series, final int item)
  {
    final int lnValue = (this.foEvaluate.faElements == null) ? 0 : this.foEvaluate.faElements[item];
    return (lnValue);
  }

  // ---------------------------------------------------------------------------
  @Override
  public int getSeriesCount()
  {
    return ((this.foEvaluate.faElements == null) ? 0 : 1);
  }

  // ---------------------------------------------------------------------------
  public String getSeriesName(final int series)
  {
    return ("Elements");
  }

  // ---------------------------------------------------------------------------
  @Override
  public String getSeriesKey(final int series)
  {
    return ("Elements");
  }

  // ---------------------------------------------------------------------------
  @Override
  public int getItemCount(final int series)
  {
    return ((this.foEvaluate.faElements == null) ? 0 : this.foEvaluate.faElements.length);
  }

  // ---------------------------------------------------------------------------
  public void releasePointers()
  {
    this.foEvaluate = null;
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
