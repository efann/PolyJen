/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.PolyJen;

import org.jfree.chart.JFreeChart;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class ChartButton extends JButton implements ComponentListener
{
  private final JFreeChart foFreeChart;
  private final BaseAbstractSeriesDataset foBaseAbstractSeriesDataset;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public ChartButton(final JFreeChart toFreeChart, final BaseAbstractSeriesDataset toBaseAbstractSeriesDataset)
  {
    this.foFreeChart = toFreeChart;
    this.foBaseAbstractSeriesDataset = toBaseAbstractSeriesDataset;

    this.setupListeners();
  }

  // ---------------------------------------------------------------------------
  private void setupListeners()
  {
    this.addComponentListener(this);

  }

  // ---------------------------------------------------------------------------
  public void updateChart()
  {
    this.foBaseAbstractSeriesDataset.changeDataset();
    this.redrawChartImageBuffers();
  }

  // ---------------------------------------------------------------------------
  private void redrawChartImageBuffers()
  {
    // By the way, you don't need to setPreferredSize as the chart icon will determine the size.
    int lnWidth = this.getWidth();
    int lnHeight = this.getHeight();

    if ((lnWidth <= 0) || (lnHeight <= 0))
    {
      final Dimension ldScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

      if (lnWidth <= 0)
      {
        lnWidth = (int) (ldScreenSize.width * 0.6);
      }

      if (lnHeight <= 0)
      {
        lnHeight = (int) (ldScreenSize.height * 0.25);
      }
    }

    final Insets loInsets = this.getBorder().getBorderInsets(this);
    lnWidth -= (loInsets.left + loInsets.right);
    lnHeight -= (loInsets.top + loInsets.bottom);

    // It's much faster using the BufferedImage than ChartPanel with buffering
    // set to true (e.g., new ChartPanel(this.chart, true)). And on slow machines,
    // like a 233 MHz AMD chip, ChartPanel has too many delays in refreshing ChartPanel,
    // even with buffering.
    final BufferedImage loImage = this.foFreeChart.createBufferedImage(lnWidth, lnHeight);
    this.setIcon(new ImageIcon(loImage));
  }

  // ---------------------------------------------------------------------------
  public void removeListeners()
  {
    this.removeComponentListener(this);
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface ComponentListener
  // ---------------------------------------------------------------------------
  @Override
  public void componentResized(final ComponentEvent e)
  {
    if (e.getID() == ComponentEvent.COMPONENT_RESIZED)
    {
      this.updateChart();
    }
  }

  // ---------------------------------------------------------------------------
  @Override
  public void componentMoved(final ComponentEvent e)
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  public void componentShown(final ComponentEvent e)
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  public void componentHidden(final ComponentEvent e)
  {
  }
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
