/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2018, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.PolyJen;

import com.beowurks.BeoCommon.SchemaWriter;
import com.beowurks.BeoCommon.XMLTextWriter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.w3c.dom.Node;

// Refer to the following link for some extremely helpful information:
// http://www.clientjava.com/blog/2005/03/30/1112196050182.html

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class ReportEvaluation1 extends ReportBase
{
  final private int[] faElements;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public ReportEvaluation1(final MainFrame toMainFrame, final String tcTitle, final int[] taElements)
  {
    super(toMainFrame, tcTitle, "Evaluation");

    this.faElements = taElements;

    this.fcReportStem = "Evaluation1";
    try
    {
      this.foJasperReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/com/beowurks/PolyJen/reports/" + this.fcReportStem + ".jasper"));
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
      loErr.printStackTrace();
    }
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void createXMLData()
  {
    final XMLTextWriter loTextWriter = new XMLTextWriter(true);

    loTextWriter.initializeXMLDocument();
    loTextWriter.createRootNode("EvaluationData", null);

    final int lnElements = this.faElements.length;
    for (int lnElement = 0; lnElement < lnElements; ++lnElement)
    {
      final Node loRecord = loTextWriter.appendNodeToRoot(SchemaWriter.RECORDS_LABEL, (String) null, null);

      loTextWriter.appendToNode(loRecord, "Initiator", lnElement + 1, null);
      loTextWriter.appendToNode(loRecord, "Reacted", this.faElements[lnElement], null);
    }

    loTextWriter.saveToFile(this.fcXMLFilePath, 2);
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
