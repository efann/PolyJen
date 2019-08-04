/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.PolyJen;

import com.beowurks.BeoCommon.BaseXMLData;
import com.beowurks.BeoCommon.Util;
import com.beowurks.BeoCommon.XMLTextReader;
import com.beowurks.BeoCommon.XMLTextWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class PolymerParametersXML extends BaseXMLData
{
  private final static DateFormat DATEFORMAT_MED = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

  private String fcTitle = "";
  private Date fdCreatedOn = new Date(0);
  private String fcCreatedBy = "";
  private Date fdModifiedOn = new Date(0);
  private String fcModifiedBy = "";

  private int fnSites = 1;
  private int fnLevels = 1;
  private int fnInitiatorMolecules = 0;
  private double fnMWOfInitiator = 0.0;
  private double fnMWOfMonomer = 0.0;

  private final int faMonomerLevelReacted[] = new int[Global.MAX_POLYMER_LEVELS];
  private final GridData faGridData[][];

  // ---------------------------------------------------------------------------
  public PolymerParametersXML(final String tcDirectory, final String tcFileName, final boolean tlParse)
  {
    super(tcDirectory, tcFileName, 2);

    this.faGridData = new GridData[Global.MAX_POLYMER_LEVELS][Global.MAX_POLYMER_SITES];
    for (int y = 0; y < Global.MAX_POLYMER_LEVELS; ++y)
    {
      for (int x = 0; x < Global.MAX_POLYMER_SITES; ++x)
      {
        this.faGridData[y][x] = new GridData();
      }
    }

    if (tlParse)
    {
      this.parseXMLData();
    }
  }

  // ---------------------------------------------------------------------------
  public String getFormattedDate(final Date toDate)
  {
    return (PolymerParametersXML.DATEFORMAT_MED.format(toDate));
  }

  // ---------------------------------------------------------------------------
  public String getTitle()
  {
    return (this.fcTitle);
  }

  // ---------------------------------------------------------------------------
  public Date getCreatedOn()
  {
    return (this.fdCreatedOn);
  }

  // ---------------------------------------------------------------------------
  public String getCreatedBy()
  {
    return (this.fcCreatedBy);
  }

  // ---------------------------------------------------------------------------
  public Date getModifiedOn()
  {
    return (this.fdModifiedOn);
  }

  // ---------------------------------------------------------------------------
  public String getModifiedBy()
  {
    return (this.fcModifiedBy);
  }

  // ---------------------------------------------------------------------------
  public int getSites()
  {
    return (PolymerParametersXML.validateSites(this.fnSites));
  }

  // ---------------------------------------------------------------------------
  public int getLevels()
  {
    return (PolymerParametersXML.validateLevels(this.fnLevels));
  }

  // ---------------------------------------------------------------------------
  static private int validateSites(final int tnSites)
  {
    int lnSites = tnSites;

    if (lnSites < 1)
    {
      lnSites = 1;
    }
    else if (lnSites > Global.MAX_POLYMER_SITES)
    {
      lnSites = Global.MAX_POLYMER_SITES;
    }

    return (lnSites);
  }

  // ---------------------------------------------------------------------------
  static private int validateLevels(final int tnLevels)
  {
    int lnLevels = tnLevels;

    if (lnLevels < 1)
    {
      lnLevels = 1;
    }
    else if (lnLevels > Global.MAX_POLYMER_LEVELS)
    {
      lnLevels = Global.MAX_POLYMER_LEVELS;
    }

    return (lnLevels);
  }

  // ---------------------------------------------------------------------------
  public int getInitiatorMolecules()
  {
    return (this.fnInitiatorMolecules);
  }

  // ---------------------------------------------------------------------------
  public double getMWOfInitiator()
  {
    return (this.fnMWOfInitiator);
  }

  // ---------------------------------------------------------------------------
  public double getMWOfMonomer()
  {
    return (this.fnMWOfMonomer);
  }

  // ---------------------------------------------------------------------------
  public int getMonomerLevelReacted(final int tnLevel)
  {
    final int lnLevelsUsed = this.getLevels() - 1;

    return ((tnLevel <= lnLevelsUsed) ? this.faMonomerLevelReacted[tnLevel] : 0);
  }

  // ---------------------------------------------------------------------------
  public int[][] getArrays(final int tnLevel, final int tnSite)
  {
    if (this.faGridData[tnLevel][tnSite].faRowColData == null)
    {
      PolymerParametersXML.initializeMinimumGridData(this.faGridData[tnLevel][tnSite]);
    }
    // Ensure that the endpoints are correct.
    PolymerParametersXML.setGridDataEndpoints(this.faGridData[tnLevel][tnSite]);

    return (this.faGridData[tnLevel][tnSite].faRowColData);
  }

  // ---------------------------------------------------------------------------
  static private void setGridDataEndpoints(final GridData taGridData)
  {
    taGridData.faRowColData[0][0] = 1;
    taGridData.faRowColData[taGridData.faRowColData.length - 1][0] = Integer.MAX_VALUE;
  }

  // ---------------------------------------------------------------------------
  static private void initializeMinimumGridData(final GridData taGridData)
  {
    // No need to initialize to 0.
    taGridData.faRowColData = new int[2][Global.EDIT_COLUMNS];
  }

  // ---------------------------------------------------------------------------
  public String getLoginID()
  {
    return (System.getProperty("user.name"));
  }

  // ---------------------------------------------------------------------------
  public Date getCurrentDateTime()
  {
    return (new Date());
  }

  // ---------------------------------------------------------------------------
  public void setTitle(final String tcTitle)
  {
    this.fcTitle = tcTitle;
  }

  // ---------------------------------------------------------------------------
  public void setCreatedOn(final Date tdCreatedOn)
  {
    this.fdCreatedOn = tdCreatedOn;
  }

  // ---------------------------------------------------------------------------
  public void setCreatedBy(final String tcCreatedBy)
  {
    this.fcCreatedBy = tcCreatedBy;
  }

  // ---------------------------------------------------------------------------
  public void setModifiedOn(final Date tdModifiedOn)
  {
    this.fdModifiedOn = tdModifiedOn;
  }

  // ---------------------------------------------------------------------------
  public void setModifiedBy(final String tcModifiedBy)
  {
    this.fcModifiedBy = tcModifiedBy;
  }

  // ---------------------------------------------------------------------------
  public void setSites(final String tcSites)
  {
    int lnValue = 0;
    try
    {
      lnValue = Integer.parseInt(tcSites);
    }
    catch (final java.lang.NumberFormatException loErr)
    {
      lnValue = 0;
    }

    this.fnSites = PolymerParametersXML.validateSites(lnValue);
  }

  // ---------------------------------------------------------------------------
  public void setLevels(final String tcLevels)
  {
    int lnValue = 0;
    try
    {
      lnValue = Integer.parseInt(tcLevels);
    }
    catch (final NumberFormatException loErr)
    {
      lnValue = 0;
    }

    this.fnLevels = PolymerParametersXML.validateLevels(lnValue);
  }

  // ---------------------------------------------------------------------------
  public void setInitiatorMolecules(final int tnInitiators)
  {
    this.fnInitiatorMolecules = tnInitiators;
  }

  // ---------------------------------------------------------------------------
  public void setMWOfInitiator(final double tnValue)
  {
    this.fnMWOfInitiator = tnValue;
  }

  // ---------------------------------------------------------------------------
  public void setMWOfMonomer(final double tnValue)
  {
    this.fnMWOfMonomer = tnValue;
  }

  // ---------------------------------------------------------------------------
  public void setMonomerLevelReacted(final int tnLevel, final int tnValue)
  {
    final int lnLevelsUsed = this.getLevels() - 1;

    this.faMonomerLevelReacted[tnLevel] = (tnLevel <= lnLevelsUsed) ? tnValue : 0;
  }

  // ---------------------------------------------------------------------------
  public void setArrays(final int tnLevel, final int tnSite, final int[][] taValues)
  {
    if ((taValues == null) || (tnSite >= this.fnSites) || (tnLevel >= this.fnLevels))
    {
      PolymerParametersXML.initializeMinimumGridData(this.faGridData[tnLevel][tnSite]);
      return;
    }

    this.faGridData[tnLevel][tnSite].faRowColData = new int[taValues.length][taValues[0].length];

    final int lnRowLen = taValues.length;
    final int lnColLen = taValues[0].length;
    for (int lnRow = 0; lnRow < lnRowLen; ++lnRow)
    {
      System.arraycopy(taValues[lnRow], 0, this.faGridData[tnLevel][tnSite].faRowColData[lnRow], 0, lnColLen);
    }
  }

  // ---------------------------------------------------------------------------
  public int getTotalMonomersFromUsedLevels()
  {
    return (this.getMonomerLevelReacted(this.fnLevels - 1));
  }

  // ---------------------------------------------------------------------------
  @Override
  public boolean saveXMLData()
  {
    final boolean llOkay = true;
    final XMLTextWriter loTextWriter = this.foXMLTextWriter;

    loTextWriter.initializeXMLDocument();
    loTextWriter.createRootNode("ParameterData", null);

    loTextWriter.appendNodeToRoot(Global.FIELD_TITLE, this.fcTitle, null);
    loTextWriter.appendNodeToRoot(Global.FIELD_SITES, this.fnSites, null);
    loTextWriter.appendNodeToRoot(Global.FIELD_LEVELS, this.fnLevels, null);
    loTextWriter.appendNodeToRoot(Global.FIELD_INITIATORS, this.fnInitiatorMolecules, null);
    loTextWriter.appendNodeToRoot(Global.FIELD_MW_INITIATOR, this.fnMWOfInitiator, null);
    loTextWriter.appendNodeToRoot(Global.FIELD_MW_MONOMER, this.fnMWOfMonomer, null);
    loTextWriter.appendNodeToRoot(Global.FIELD_CREATED_BY, this.fcCreatedBy, null);
    loTextWriter.appendNodeToRoot(Global.FIELD_CREATED_ON, this.fdCreatedOn.getTime(), null);
    loTextWriter.appendNodeToRoot(Global.FIELD_MODIFIED_BY, this.fcModifiedBy, null);
    loTextWriter.appendNodeToRoot(Global.FIELD_MODIFIED_ON, this.fdModifiedOn.getTime(), null);

    final Node loLevels = loTextWriter.appendNodeToRoot(Global.FIELD_MONOMER_LEVELS, (String) null, null);
    for (int i = 0; i < Global.MAX_POLYMER_LEVELS; ++i)
    {
      loTextWriter.appendToNode(loLevels, Global.FIELD_MONOMER_LEVELNO, Integer.toString(this.faMonomerLevelReacted[i]), new Object[][]{{Global.FIELD_MONOMER_ATTRIBUTE, Integer.toString(i)}});
    }

    final Node loArrays = loTextWriter.appendNodeToRoot(Global.FIELD_ARRAY_HEADER, (String) null, null);
    for (int lnLevel = 0; lnLevel < Global.MAX_POLYMER_LEVELS; ++lnLevel)
    {
      for (int lnSite = 0; lnSite < Global.MAX_POLYMER_SITES; ++lnSite)
      {
        final Node loGrid = loTextWriter.appendToNode(loArrays, Global.FIELD_ARRAY_GRID, null, new Object[][]{{Global.FIELD_ARRAY_LEVEL, Integer.toString(lnLevel)}, {Global.FIELD_ARRAY_SITE, Integer.toString(lnSite)}});

        if (this.faGridData[lnLevel][lnSite].faRowColData == null)
        {
          PolymerParametersXML.initializeMinimumGridData(this.faGridData[lnLevel][lnSite]);
        }
        // Just make sure that the end points are correct.
        PolymerParametersXML.setGridDataEndpoints(this.faGridData[lnLevel][lnSite]);

        final int lnRows = this.faGridData[lnLevel][lnSite].faRowColData.length;
        final int lnCols = this.faGridData[lnLevel][lnSite].faRowColData[0].length;
        for (int lnRow = 0; lnRow < lnRows; ++lnRow)
        {
          for (int lnCol = 0; lnCol < lnCols; ++lnCol)
          {
            loTextWriter.appendToNode(loGrid, Global.FIELD_ARRAY_CELL, Integer.toString(this.faGridData[lnLevel][lnSite].faRowColData[lnRow][lnCol]), new Object[][]{{Global.FIELD_ARRAY_ROW, Integer.toString(lnRow)}, {Global.FIELD_ARRAY_COL, Integer.toString(lnCol)}});
          }
        }

      }
    }

    loTextWriter.saveToFile(this.getFullName(), this.fnIndent);

    return (llOkay);
  }

  // ---------------------------------------------------------------------------
  @Override
  public boolean parseXMLData()
  {
    final XMLTextReader loXMLTextReader = this.foXMLTextReader;
    boolean llOkay = loXMLTextReader.initializeXMLDocument(new File(this.getFullName()));

    if (!llOkay)
    {
      return (llOkay);
    }

    try
    {
      this.fcTitle = loXMLTextReader.getString(Global.FIELD_TITLE, "");

      this.fdCreatedOn = loXMLTextReader.getDate(Global.FIELD_CREATED_ON, 0);
      this.fcCreatedBy = loXMLTextReader.getString(Global.FIELD_CREATED_BY, "");
      this.fdModifiedOn = loXMLTextReader.getDate(Global.FIELD_MODIFIED_ON, 0);
      this.fcModifiedBy = loXMLTextReader.getString(Global.FIELD_MODIFIED_BY, "");

      this.fnSites = loXMLTextReader.getInteger(Global.FIELD_SITES, 1);
      this.fnLevels = loXMLTextReader.getInteger(Global.FIELD_LEVELS, 1);
      this.fnInitiatorMolecules = loXMLTextReader.getInteger(Global.FIELD_INITIATORS, 0);
      this.fnMWOfInitiator = loXMLTextReader.getDouble(Global.FIELD_MW_INITIATOR, 0);
      this.fnMWOfMonomer = loXMLTextReader.getDouble(Global.FIELD_MW_MONOMER, 0);

      this.parseMonomerReactionLevels();
      this.parseXMLArrays();
    }
    catch (final Exception loErr)
    {
      Util.errorMessage(null, "There was a problem in parsing the XML file of " + this.getFullName() + ".\n\n" + loErr.getMessage());
      llOkay = false;
    }

    return (llOkay);
  }

  // ---------------------------------------------------------------------------
  private boolean parseMonomerReactionLevels()
  {
    final XMLTextReader loXMLTextReader = this.foXMLTextReader;

    final Element loParentElement = loXMLTextReader.findFirstElement(Global.FIELD_MONOMER_LEVELS);
    if (loParentElement == null)
    {
      return (false);
    }

    final NodeList loNodeList = loParentElement.getElementsByTagName(Global.FIELD_MONOMER_LEVELNO);
    final int lnLen = loNodeList.getLength();
    for (int i = 0; i < lnLen; ++i)
    {
      final Element loElement = (Element) loNodeList.item(i);
      final int lnLevel = loXMLTextReader.getAttributeInteger(loElement, Global.FIELD_MONOMER_ATTRIBUTE, -1);
      if ((lnLevel < 0) || (lnLevel >= Global.MAX_POLYMER_LEVELS))
      {
        continue;
      }

      try
      {
        final int lnValue = Integer.parseInt(loElement.getFirstChild().getNodeValue());
        this.faMonomerLevelReacted[lnLevel] = lnValue;
      }
      catch (final Exception ignored)
      {
      }
    }

    return (true);
  }

  // ---------------------------------------------------------------------------
  private boolean parseXMLArrays()
  {
    final XMLTextReader loXMLTextReader = this.foXMLTextReader;

    final Document loDoc = loXMLTextReader.getDocument();
    final boolean llOkay = true;

    final NodeList loNodeList = loDoc.getElementsByTagName(Global.FIELD_ARRAY_GRID);
    final int lnLen = loNodeList.getLength();

    for (int i = 0; i < lnLen; ++i)
    {
      final Element loElement = (Element) loNodeList.item(i);
      final int lnLevel = loXMLTextReader.getAttributeInteger(loElement, Global.FIELD_ARRAY_LEVEL, -1);
      final int lnSite = loXMLTextReader.getAttributeInteger(loElement, Global.FIELD_ARRAY_SITE, -1);

      if ((lnLevel < 0) || (lnLevel >= Global.MAX_POLYMER_LEVELS))
      {
        continue;
      }
      if ((lnSite < 0) || (lnSite >= Global.MAX_POLYMER_SITES))
      {
        continue;
      }

      this.parseGridData(this.faGridData[lnLevel][lnSite], loElement);
    }

    return (llOkay);
  }

  // ---------------------------------------------------------------------------
  private void parseGridData(final GridData toGridData, final Element toElementData)
  {
    final XMLTextReader loXMLTextReader = this.foXMLTextReader;

    final NodeList loNodeList = toElementData.getElementsByTagName(Global.FIELD_ARRAY_CELL);
    final int lnLen = loNodeList.getLength();

    // First get the maximum row count;
    int lnRowMax = -1;
    for (int i = 0; i < lnLen; ++i)
    {
      final Element loElement = (Element) loNodeList.item(i);
      final int lnRow = loXMLTextReader.getAttributeInteger(loElement, Global.FIELD_ARRAY_ROW, -1);
      lnRowMax = (lnRow > lnRowMax) ? lnRow : lnRowMax;
    }

    // It's zero-based so increment.
    ++lnRowMax;

    if (lnRowMax < Global.EDIT_MINIMUM_ROWS)
    {
      PolymerParametersXML.initializeMinimumGridData(toGridData);
      return;
    }

    // Initialized to zero with the new.
    toGridData.faRowColData = new int[lnRowMax][Global.EDIT_COLUMNS];

    for (int i = 0; i < lnLen; ++i)
    {
      final Element loElement = (Element) loNodeList.item(i);
      final int lnRow = loXMLTextReader.getAttributeInteger(loElement, Global.FIELD_ARRAY_ROW, -1);
      final int lnCol = loXMLTextReader.getAttributeInteger(loElement, Global.FIELD_ARRAY_COL, -1);

      if ((lnRow < 0) || (lnRow >= lnRowMax))
      {
        continue;
      }
      if ((lnCol < 0) || (lnCol >= Global.EDIT_COLUMNS))
      {
        continue;
      }

      try
      {
        final int lnValue = Integer.parseInt(loElement.getFirstChild().getNodeValue());
        toGridData.faRowColData[lnRow][lnCol] = lnValue;
      }
      catch (final Exception ignored)
      {
      }
    }

  }

  // ---------------------------------------------------------------------------
  public boolean verifyData(final Component toOwnerComponent, final int tnDisplayOption)
  {
    final StringBuffer lcProblems = new StringBuffer("");

    this.doCheckListOfPossibleProblems(lcProblems);

    final boolean llOkay = (lcProblems.length() == 0);

    if (tnDisplayOption == Global.VERIFY_DISPLAY_NEVER)
    {
      return (llOkay);
    }

    if (tnDisplayOption == Global.VERIFY_DISPLAY_ALWAYS)
    {
      if (llOkay)
      {
        Util.infoMessage(toOwnerComponent, new JLabel("<html><font face=\"Arial\">Everything is fine with the parameters for <b>" + this.getTitle() + "</b>!</font></html>"));
      }
      else
      {
        this.displayErrorMessage(toOwnerComponent, lcProblems);
      }
    }
    else if ((tnDisplayOption == Global.VERIFY_DISPLAY_ON_ERROR) && (!llOkay))
    {
      this.displayErrorMessage(toOwnerComponent, lcProblems);
    }

    return (llOkay);
  }

  // ---------------------------------------------------------------------------
  private void doCheckListOfPossibleProblems(final StringBuffer tcProblems)
  {
    final String lcPrefix = "<p>" + '\u25CF' + "&nbsp;&nbsp;&nbsp;";

    // -------------------------------
    // Verify header fields.
    if (this.getInitiatorMolecules() <= 0)
    {
      tcProblems.append(lcPrefix + "<b># of Initiator Molecules</b> should be greater than 0.</p>");
    }

    if (this.getMWOfInitiator() <= 0)
    {
      tcProblems.append(lcPrefix + "<b>Molecular Weight of Initiator</b> should be greater than 0.</p>");
    }

    if (this.getMWOfMonomer() <= 0)
    {
      tcProblems.append(lcPrefix + "<b>Molecular Weight of Monomer</b> should be greater than 0.</p>");
    }

    if ((this.getSites() <= 0) || (this.getSites() > Global.MAX_POLYMER_SITES))
    {
      tcProblems.append(lcPrefix + "<b>Polymer Sites</b> should be between 1 and " + Global.MAX_POLYMER_SITES + ".</p>");
    }

    // -------------------------------
    // Verify the Level fields.
    int lnLevelTrack = -1;
    for (int i = 0; i < Global.MAX_POLYMER_LEVELS; ++i)
    {
      final int lnLevel = this.getMonomerLevelReacted(i);

      if (i == 0)
      {
        if (lnLevel <= 0)
        {
          tcProblems.append(lcPrefix + "<b>Monomer Molecules Reacted, Level #1</b> should be greater than 0.</p>");
        }
      }
      else
      {
        if ((lnLevel > 0) && (lnLevel <= lnLevelTrack))
        {
          tcProblems.append(lcPrefix + "<b>Monomer Molecules Reacted, Level #").append(i + 1).append("</b> should be greater than <b>Monomer Molecules Reacted, Level #").append(i).append("</b>.</p>");
        }
      }

      lnLevelTrack = lnLevel;
    }

    boolean llAboveZero = false;
    // Remember: i == 0 and lnLevel <= 0 is checked in the for loop before this.
    for (int i = Global.MAX_POLYMER_LEVELS - 1; i > 0; --i)
    {
      final int lnLevel = this.getMonomerLevelReacted(i);

      if ((!llAboveZero) && (lnLevel <= 0))
      {
        continue;
      }

      if ((!llAboveZero) && (lnLevel > 0))
      {
        llAboveZero = true;
        continue;
      }

      if ((llAboveZero) && (lnLevel <= 0))
      {
        tcProblems.append(lcPrefix + "<b>Monomer Molecules Reacted, Level #").append(i + 1).append("</b> should be greater than 0.</p>");
      }

    }

    // -------------------------------
    // Verify the arrays.
    for (int lnLev = 0; lnLev < Global.MAX_POLYMER_LEVELS; ++lnLev)
    {
      if (this.getMonomerLevelReacted(lnLev) > 0)
      {
        final int lnSiteCount = this.getSites();
        for (int lnSite = 0; lnSite < lnSiteCount; ++lnSite)
        {
          final int[][] laaArray = this.getArrays(lnLev, lnSite);

          final int lnColCount = laaArray[0].length;

          int lnMonomerTrack = -1;
          boolean llOutOfOrder = false;
          boolean llDuplicate = false;
          boolean llZeroFound = false;
          for (final int[] laArray : laaArray)
          {

            if (laArray[0] < lnMonomerTrack)
            {
              llOutOfOrder = true;
            }

            if (laArray[0] == lnMonomerTrack)
            {
              llDuplicate = true;
            }

            lnMonomerTrack = laArray[0];

            for (int x = 0; x < lnColCount; ++x)
            {
              if (laArray[x] == 0)
              {
                llZeroFound = true;
              }
            }

          }

          if (llOutOfOrder)
          {
            tcProblems.append(lcPrefix + "<b>Level #").append(lnLev + 1).append(", Site #").append(lnSite + 1).append(" Grid</b>: the monomer column is out of order.</p>");
          }

          if (llDuplicate)
          {
            tcProblems.append(lcPrefix + "<b>Level #").append(lnLev + 1).append(", Site #").append(lnSite + 1).append(" Grid</b>: the monomer column has duplicates.</p>");
          }

          if (llZeroFound)
          {
            tcProblems.append(lcPrefix + "<b>Level #").append(lnLev + 1).append(", Site #").append(lnSite + 1).append(" Grid</b>: one of the cells has a zero value.</p>");
          }
        }
      }
    }

  }

  // ---------------------------------------------------------------------------
  private void displayErrorMessage(final Component toOwnerComponent, final StringBuffer tcProblems)
  {
    tcProblems.insert(0, "<html><font face=\"Arial\"><p>The following problems need to be corrected for <b>" + this.getTitle() + "</b>:</p><br>");
    tcProblems.append("</font></html>");

    final JTextPane loTextPane = new JTextPane();
    loTextPane.setEditorKit(new javax.swing.text.html.HTMLEditorKit());
    loTextPane.setText(tcProblems.toString());
    loTextPane.setBackground(toOwnerComponent.getBackground());
    loTextPane.setEditable(false);

    final Dimension ldScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    final Dimension ldSize = new Dimension((int) (ldScreenSize.width * 0.5), (int) (ldScreenSize.height * 0.5));
    final JScrollPane loScrollPane1 = new JScrollPane(loTextPane);
    loScrollPane1.setPreferredSize(ldSize);

    Util.displayMessage(toOwnerComponent, loScrollPane1, "Verify Parameters", JOptionPane.ERROR_MESSAGE);
  }

  // ---------------------------------------------------------------------------
  static public String createUniqueFileName(final String tcDirectory)
  {
    File loFile = null;
    try
    {
      loFile = File.createTempFile(Global.DATA_PREFIX, Global.DATA_POSTFIX_EXT, new File(tcDirectory));
    }
    catch (final IOException loErr)
    {
      loFile = null;
      Util.infoMessage(null, "Unable to get the file name for a new record. Please notify support@beowurks.com" + "\n\n" + loErr.getMessage());
    }

    return ((loFile != null) ? loFile.getName() : Global.DATA_PREFIX + "-----");
  }
  // ---------------------------------------------------------------------------
}

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
class GridData
{
  int[][] faRowColData = null;
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
