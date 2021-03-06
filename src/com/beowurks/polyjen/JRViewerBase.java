/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2020, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.polyjen;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
public class JRViewerBase extends JRViewer
{
  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public JRViewerBase(final JasperPrint toJasperPrint)
  {
    super(toJasperPrint);

    this.tlbToolBar.remove(this.btnPrint);
    this.tlbToolBar.remove(this.btnReload);
  }

  // ---------------------------------------------------------------------------
  @Override
  public void loadReport(final JasperPrint toJasperPrint)
  {
    super.loadReport(toJasperPrint);

    this.refreshPage();
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
