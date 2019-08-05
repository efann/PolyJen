/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.polyjen;

import com.beowurks.BeoCommon.BaseFileFilter;
import com.beowurks.BeoCommon.Util;
import com.beowurks.BeoExport.ExportBaseStream;
import com.beowurks.BeoExport.ExportCommaQuoteDelimiter;
import com.beowurks.BeoExport.ExportExcel21;
import com.beowurks.BeoExport.ExportHTML;
import com.beowurks.BeoExport.ExportTabDelimiter;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.filechooser.FileFilter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileWriter;
import java.io.IOException;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class FileFilterExports
{
  final static protected int FILEEXPORT_XLS_21 = 1;
  final static protected int FILEEXPORT_TABTEXT = 2;
  final static protected int FILEEXPORT_COMMATEXT = 3;
  final static protected int FILEEXPORT_QUOTETEXT = 4;
  final static protected int FILEEXPORT_HTML = 5;
  final static protected int FILEEXPORT_XML = 6;

  protected final BaseFileFilter foFileFilter;

  private final int fnExportType;

  // ---------------------------------------------------------------------------
  public FileFilterExports(final String tcExtension, final String tcDescription, final int tnExportType)
  {
    this.foFileFilter = new BaseFileFilter(tcExtension, tcDescription);
    this.fnExportType = tnExportType;
  }

  // ---------------------------------------------------------------------------
  public static int getFileFilter(final FileFilterExports[] taFilters, final FileFilter toFilter)
  {
    int lnFilter = 0;
    final int lnFilters = taFilters.length;

    for (int i = 0; i < lnFilters; ++i)
    {
      if (toFilter == taFilters[i].foFileFilter)
      {
        lnFilter = i;
        break;
      }
    }

    return (lnFilter);
  }

  // ---------------------------------------------------------------------------
  public void exportReportRawData(final String tcFileName, final Document toXMLDocument)
  {
    String lcFileName = tcFileName;
    final String lcExtension = this.foFileFilter.getFilter();

    lcFileName += tcFileName.endsWith("." + lcExtension) ? "" : "." + lcExtension;

    switch (this.fnExportType)
    {
      case FileFilterExports.FILEEXPORT_XLS_21:
        this.exportStreamText(new ExportExcel21(lcFileName), toXMLDocument);
        break;

      case FileFilterExports.FILEEXPORT_XML:
        this.exportXML(lcFileName, toXMLDocument);
        break;

      case FileFilterExports.FILEEXPORT_TABTEXT:
        this.exportStreamText(new ExportTabDelimiter(lcFileName), toXMLDocument);
        break;

      case FileFilterExports.FILEEXPORT_COMMATEXT:
        this.exportStreamText(new ExportBaseStream(lcFileName), toXMLDocument);
        break;

      case FileFilterExports.FILEEXPORT_QUOTETEXT:
        this.exportStreamText(new ExportCommaQuoteDelimiter(lcFileName), toXMLDocument);
        break;

      case FileFilterExports.FILEEXPORT_HTML:
        this.exportStreamText(new ExportHTML(lcFileName), toXMLDocument);
        break;

      default:
        Util.errorMessage(null, "Unrecognized FileFilterExports type in the class FileFilterExports. Notify support@beowurks.com");
        break;
    }
  }

  // ---------------------------------------------------------------------------
  public void exportStreamText(final ExportBaseStream toExportBaseStream, final Document toXMLDocument)
  {
    boolean llOkay = true;

    try
    {
      toExportBaseStream.openFile();
      toExportBaseStream.writeBOF();

      final NodeList loRecords = toXMLDocument.getElementsByTagName("Records");
      final Node loFirstRecord = loRecords.item(0);
      final int lnRows = loRecords.getLength();
      final int lnChildNodesCount = loFirstRecord.getChildNodes().getLength();

      int lnColumns = 0;
      for (int x = 0; x < lnChildNodesCount; ++x)
      {
        if (loFirstRecord.getChildNodes().item(x).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
        {
          ++lnColumns;
        }
      }

      toExportBaseStream.setMax((short) lnColumns, (short) lnRows);
      toExportBaseStream.writeDimensions();

      // Write column header labels.
      toExportBaseStream.writeBOL();

      final String[] laFieldNames = new String[lnColumns];

      for (int x = 0, lnTrack = 0; x < lnChildNodesCount; ++x)
      {
        final Node loNode = loFirstRecord.getChildNodes().item(x);
        if (loNode.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE)
        {
          continue;
        }

        laFieldNames[lnTrack] = loNode.getNodeName();
        toExportBaseStream.writeLabel(laFieldNames[lnTrack]);
        if (lnTrack < (lnColumns - 1))
        {
          toExportBaseStream.writeSeparator();
        }
        lnTrack++;
      }
      toExportBaseStream.writeEOL();

      final String[] laFieldValues = new String[lnColumns];

      for (int y = 0; y < lnRows; ++y)
      {
        // Reinitialize each time
        for (int z = 0; z < lnColumns; ++z)
        {
          laFieldValues[z] = " ";
        }

        final NodeList loFields = loRecords.item(y).getChildNodes();

        final int lnFields = loFields.getLength();
        for (int f = 0; f < lnFields; f++)
        {
          final Node loNode = loFields.item(f).getFirstChild();
          if (loNode == null)
          {
            continue;
          }

          final String lcLabel = loFields.item(f).getNodeName();
          for (int l = 0; l < lnColumns; ++l)
          {
            if (lcLabel.compareToIgnoreCase(laFieldNames[l]) == 0)
            {
              laFieldValues[l] = loNode.getNodeValue();
              break;
            }
          }
        }

        toExportBaseStream.writeBOL();

        for (int z = 0; z < lnColumns; ++z)
        {
          toExportBaseStream.writeDataField(laFieldValues[z]);

          if (z < (lnColumns - 1))
          {
            toExportBaseStream.writeSeparator();
          }
        }

        toExportBaseStream.writeEOL();
      }

      toExportBaseStream.writeEOF();
      toExportBaseStream.closeFile();
    }
    catch (final DOMException | IOException loErr)
    {
      llOkay = false;
      Util.errorMessage(null, "There was an error in saving " + toExportBaseStream.getFileName() + ".\n\n" + loErr.getMessage());
    }

    if (llOkay)
    {
      Util.infoMessage(null, toExportBaseStream.getFileName() + " has been saved.");
    }
  }

  // ---------------------------------------------------------------------------
  public void exportXML(final String tcFileName, final Document toXMLDocument)
  {
    boolean llOkay = true;

    try
    {
      final TransformerFactory loFactory = TransformerFactory.newInstance();
      // JRE 1.5 did not indent the XML output, whereas 1.4 did. I got setAttribute
      // fix from the following:
      // http://forum.java.sun.com/thread.jspa?threadID=562510&start=15&tstart=15

      // Also when saving to a file, use a Writer class.

      // Sometimes this creates problems
      // http://forums.sun.com/thread.jspa?forumID=34&threadID=562510
      // If, for example, the xalan.jar is included with your project (e.g., JasperReports)
      // then you will get the "Not supported: indent-number" error.
      try
      {
        loFactory.setAttribute("indent-number", 2);
      }
      catch (final IllegalArgumentException ignored)
      {
      }

      final Transformer loTransformer = loFactory.newTransformer();

      loTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
      loTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      loTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

      final DOMSource loDOMSource = new DOMSource(toXMLDocument);
      final StreamResult loStreamResult = new StreamResult(new FileWriter(tcFileName));
      // this line actually does the save
      loTransformer.transform(loDOMSource, loStreamResult);
    }
    catch (final TransformerConfigurationException loErr)
    {
      llOkay = false;
      Util.errorMessage(null, "There was a Transformer Configuration Exception in saving the XML data to file.\n\n" + loErr.getMessage());
    }
    catch (final TransformerFactoryConfigurationError loErr)
    {
      llOkay = false;
      Util.errorMessage(null, "There was a Transformer Factory Configuration Error in saving the XML data to file.\n\n" + loErr.getMessage());
    }
    catch (final IOException loErr)
    {
      llOkay = false;
      Util.errorMessage(null, "There was a IO Exception in saving the XML data to file.\n\n" + loErr.getMessage());
    }
    catch (final TransformerException loErr)
    {
      llOkay = false;
      Util.errorMessage(null, "There was a Transformer Exception in saving the XML data to file.\n\n" + loErr.getMessage());
    }

    if (llOkay)
    {
      Util.infoMessage(null, tcFileName + " has been saved.");
    }
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
