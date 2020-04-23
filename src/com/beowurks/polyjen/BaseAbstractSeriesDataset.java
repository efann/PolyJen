/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2020, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.polyjen;

import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.XYDataset;

// By the way, this class is used for charting the data
// in the evaluation section. This way, I can point to
// the rather large numeric arrays rather than duplicate
// them with DefaultCategoryDataset.addValue routine
// which would double the memory usage.
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public abstract class BaseAbstractSeriesDataset extends AbstractXYDataset implements XYDataset
{
  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public BaseAbstractSeriesDataset()
  {
  }

  // ---------------------------------------------------------------------------
  public void changeDataset()
  {
    this.notifyListeners(new DatasetChangeEvent(this, this));
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
