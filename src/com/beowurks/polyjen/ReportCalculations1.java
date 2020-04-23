/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2020, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.polyjen;

import com.beowurks.BeoCommon.SchemaWriter;
import com.beowurks.BeoCommon.Util;
import com.beowurks.BeoCommon.XMLTextWriter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.w3c.dom.Node;

// Refer to the following link for some extremely helpful information:
// http://www.clientjava.com/blog/2005/03/30/1112196050182.html

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class ReportCalculations1 extends ReportBase
{
  private final static String FIELD_OLIGOMER = "NoOfOligomers";
  private final static String FIELD_TITLE = "Title";

  private final JFreeChart foChart;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public ReportCalculations1(final MainFrame toMainFrame, final String tcTitle, final String tcDefaultName, final JFreeChart toChart)
  {
    super(toMainFrame, tcTitle, tcDefaultName);

    this.foChart = toChart;

    this.fcReportStem = "Calculations1";

    try
    {
      this.foJasperReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/com/beowurks/polyjen/reports/" + this.fcReportStem + ".jasper"));
    }
    catch (final JRException ignore)
    {
    }

    this.fcXMLFilePath = Global.getTemporaryPath() + this.fcReportStem + ".xml";

    try
    {
      this.jbInit();
    }
    catch (final Exception loErr)
    {
      Util.showStackTraceInMessage(this, loErr, "Report Error");
    }
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void createXMLData()
  {
    final XMLTextWriter loTextWriter = new XMLTextWriter(true);
    loTextWriter.initializeXMLDocument();
    loTextWriter.createRootNode("CalculationsData", null);

    final CategoryDataset loDataset = this.foChart.getCategoryPlot().getDataset();
    final int lnColumns = loDataset.getColumnCount();
    final int lnRows = loDataset.getRowCount();

    for (int lnCol = 0; lnCol < lnColumns; ++lnCol)
    {
      final Node loRecord = loTextWriter.appendNodeToRoot(SchemaWriter.RECORDS_LABEL, (String) null, null);
      loTextWriter.appendToNode(loRecord, ReportCalculations1.FIELD_OLIGOMER, lnCol, null);
      for (int lnRow = 0; lnRow < lnRows; ++lnRow)
      {
        // Get rid of all non-word characters
        final String lcLabel = loDataset.getRowKey(lnRow).toString().replaceAll("\\W", "");
        loTextWriter.appendToNode(loRecord, lcLabel, loDataset.getValue(lnRow, lnCol).doubleValue(), null);
      }
      loTextWriter.appendToNode(loRecord, ReportCalculations1.FIELD_TITLE, this.fcDefaultFileName, null);
    }

    loTextWriter.saveToFile(this.fcXMLFilePath, 2);
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
