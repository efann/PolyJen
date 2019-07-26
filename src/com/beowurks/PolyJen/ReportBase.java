/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.PolyJen;

import com.beowurks.BeoCommon.GridBagLayoutHelper;
import com.beowurks.BeoCommon.Util;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRXmlUtils;

import org.w3c.dom.Document;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
abstract public class ReportBase extends PolyJenBaseInternalFrame
{
  protected final String fcDefaultFileName;
  protected String fcXMLFilePath;
  protected JasperReport foJasperReport;

  protected String fcReportStem;

  private JRViewerBase foJRViewerBase1 = null;

  private AppProperties foAppProperties;

  private final JPanel pnlPreview1 = new JPanel(new GridLayout(1, 1));

  private String fcExportDirectory;
  private int fnCurrentFilter;

  private FileFilterExports[] faFilters;
  private Document foXMLDocument;

  private JasperPrint foJasperPrint;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public ReportBase(final MainFrame toMainFrame, final String tcTitle, final String tcDefaultFileName)
  {
    super(toMainFrame, tcTitle);

    this.fcDefaultFileName = tcDefaultFileName;
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void jbInit() throws Exception
  {
    super.jbInit();

    this.foAppProperties = this.getMainFrame().foAppProperties;

    this.getProperties();

    this.setupFilters();

    this.setupJasperReport();

    this.setupLayouts();
  }

  // ---------------------------------------------------------------------------
  private void setupJasperReport() throws JRException
  {
    this.createXMLData();

    this.foXMLDocument = JRXmlUtils.parse(JRLoader.getLocationInputStream(this.fcXMLFilePath));

    final HashMap<String, Object> loHashMap = new HashMap<>();
    loHashMap.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, this.foXMLDocument);

    this.foJasperPrint = JasperFillManager.fillReport(this.foJasperReport, loHashMap);

    if (this.foJRViewerBase1 == null)
    {
      this.foJRViewerBase1 = new JRViewerBase(this.foJasperPrint);
    }
    else
    {
      this.foJRViewerBase1.loadReport(this.foJasperPrint);
    }
  }

  // ---------------------------------------------------------------------------
  // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4475218
  // This describes the work around for adding a JApplet to
  // a Swing window like JInternalFrame.
  private void setupLayouts()
  {
    GridBagLayoutHelper loGrid;
    final Dimension ldScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

    // --------------------

    loGrid = new GridBagLayoutHelper();

    this.pnlPreview1.setLayout(loGrid);
    this.pnlPreview1.add(this.foJRViewerBase1, loGrid.getConstraint(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH));
    this.pnlPreview1.setPreferredSize(new Dimension((int) (ldScreenSize.width * 0.75), (int) (ldScreenSize.height * 0.75)));

    // --------------------

    loGrid = new GridBagLayoutHelper();
    this.getContentPane().setLayout(loGrid);

    this.getContentPane().add(this.pnlPreview1, loGrid.getConstraint(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH));
  }

  // ---------------------------------------------------------------------------
  protected void setupFilters()
  {
    this.faFilters = new FileFilterExports[6];

    this.faFilters[0] = new FileFilterExports("xls", "Excel 2.1 (*.xls)", FileFilterExports.FILEEXPORT_XLS_21);
    this.faFilters[1] = new FileFilterExports("txt", "Tab-Delimited Text File (*.txt)", FileFilterExports.FILEEXPORT_TABTEXT);
    this.faFilters[2] = new FileFilterExports("txt", "Comma-Delimited Text File (*.txt)", FileFilterExports.FILEEXPORT_COMMATEXT);
    this.faFilters[3] = new FileFilterExports("txt", "Quote-Delimited Text File (*.txt)", FileFilterExports.FILEEXPORT_QUOTETEXT);
    this.faFilters[4] = new FileFilterExports("xml", "XML (*.xml)", FileFilterExports.FILEEXPORT_XML);
    this.faFilters[5] = new FileFilterExports("htm", "HTML (*.htm)", FileFilterExports.FILEEXPORT_HTML);
  }

  // ---------------------------------------------------------------------------
  protected void getProperties()
  {
    final AppProperties loProp = this.foAppProperties;

    this.fcExportDirectory = loProp.getReportExportDirectory();
    this.fnCurrentFilter = loProp.getReportExportFilter();
  }

  // ---------------------------------------------------------------------------
  protected void setProperties()
  {
    final AppProperties loProp = this.foAppProperties;

    loProp.setReportExportDirectory(this.fcExportDirectory);
    loProp.setReportExportFilter(this.fnCurrentFilter);
  }

  // ---------------------------------------------------------------------------
  protected void printReport()
  {
    try
    {
      JasperPrintManager.printReport(this.foJasperPrint, true);
    }
    catch (final JRException loErr)
    {
      Util.infoMessage(this, "Unable to print this report:\n" + loErr.getMessage());
    }
  }

  // ---------------------------------------------------------------------------
  protected void exportReport()
  {
    // There is currently a bug with JFileChooser.
    // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4678049
    // So set the selected file after setting the filter.
    final JFileChooser loChooser = new JFileChooser();

    for (final FileFilterExports laFilter : this.faFilters)
    {
      loChooser.addChoosableFileFilter(laFilter.foFileFilter);
    }

    loChooser.setFileFilter(this.faFilters[this.fnCurrentFilter].foFileFilter);
    loChooser.setAcceptAllFileFilterUsed(false);
    loChooser.setCurrentDirectory(new File(this.fcExportDirectory));
    loChooser.setMultiSelectionEnabled(false);
    loChooser.setSelectedFile(new File(this.fcDefaultFileName));

    if (loChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
    {
      // At the moment, I can't use the setBusy() routine. BaseFrame.getActiveFrame()
      // returns null. It's probably something to do with the ReportViewerBean of
      // Crystal Reports.
      final String lcFileName = loChooser.getSelectedFile().getPath();
      final int lnFilter = FileFilterExports.getFileFilter(this.faFilters, loChooser.getFileFilter());

      this.faFilters[lnFilter].exportReportRawData(lcFileName, this.foXMLDocument);

      this.fnCurrentFilter = lnFilter;
      this.fcExportDirectory = Util.extractDirectory(lcFileName);
    }

  }

  // ---------------------------------------------------------------------------
  @Override
  protected void finalOperations()
  {
    this.setProperties();

    super.finalOperations();
  }

  // ---------------------------------------------------------------------------
  abstract protected void createXMLData();
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
