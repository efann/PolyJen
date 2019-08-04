/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2019, Beowurks.
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
public class ReportParameters1 extends ReportBase
{
  private final PolymerParametersXML foPolymerParametersXML;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public ReportParameters1(final MainFrame toMainFrame, final String tcTitle, final PolymerParametersXML toPolymerParametersXML)
  {
    super(toMainFrame, tcTitle, toPolymerParametersXML.getTitle());

    this.foPolymerParametersXML = toPolymerParametersXML;

    this.fcReportStem = "Parameters1";

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
    final PolymerParametersXML loXML = this.foPolymerParametersXML;

    final XMLTextWriter loTextWriter = new XMLTextWriter(true);

    loTextWriter.initializeXMLDocument();
    loTextWriter.createRootNode("ParameterData", null);

    final int lnLevels = loXML.getLevels();
    final int lnSites = loXML.getSites();

    for (int lnLevel = 0; lnLevel < lnLevels; ++lnLevel)
    {
      for (int lnSite = 0; lnSite < lnSites; ++lnSite)
      {
        final int[][] laArray = loXML.getArrays(lnLevel, lnSite);

        final int lnRows = laArray.length;
        for (int lnRow = 0; lnRow < lnRows; ++lnRow)
        {
          final Node loRecord = loTextWriter.appendNodeToRoot(SchemaWriter.RECORDS_LABEL, (String) null, null);

          loTextWriter.appendToNode(loRecord, Global.FIELD_TITLE, loXML.getTitle(), null);
          loTextWriter.appendToNode(loRecord, Global.FIELD_INITIATORS, loXML.getInitiatorMolecules(), null);
          loTextWriter.appendToNode(loRecord, Global.FIELD_MW_INITIATOR, loXML.getMWOfInitiator(), null);
          loTextWriter.appendToNode(loRecord, Global.FIELD_MW_MONOMER, loXML.getMWOfMonomer(), null);
          loTextWriter.appendToNode(loRecord, Global.FIELD_SITES, loXML.getSites(), null);
          loTextWriter.appendToNode(loRecord, Global.FIELD_LEVELS, loXML.getLevels(), null);
          loTextWriter.appendToNode(loRecord, Global.FIELD_CREATED_BY, loXML.getCreatedBy(), null);
          loTextWriter.appendToNode(loRecord, Global.FIELD_CREATED_ON, loXML.getCreatedOn().toString(), null);
          loTextWriter.appendToNode(loRecord, Global.FIELD_MODIFIED_BY, loXML.getModifiedBy(), null);
          loTextWriter.appendToNode(loRecord, Global.FIELD_MODIFIED_ON, loXML.getModifiedOn().toString(), null);
          loTextWriter.appendToNode(loRecord, "SiteNo", lnSite + 1, null);
          loTextWriter.appendToNode(loRecord, "LevelNo", lnLevel + 1, null);
          loTextWriter.appendToNode(loRecord, "MonomersReacted", loXML.getMonomerLevelReacted(lnLevel), null);

          if (lnRow == 0)
          {
            loTextWriter.appendToNode(loRecord, "MonomerReaction", "First", null);
          }
          else if (lnRow == (lnRows - 1))
          {
            loTextWriter.appendToNode(loRecord, "MonomerReaction", "Final", null);
          }
          else
          {
            loTextWriter.appendToNode(loRecord, "MonomerReaction", laArray[lnRow][0], null);
          }

          loTextWriter.appendToNode(loRecord, "ReactionRate", Double.toString(laArray[lnRow][1] / 100.0) + "%", null);
        }
      }
    }

    loTextWriter.saveToFile(this.fcXMLFilePath, 2);
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
