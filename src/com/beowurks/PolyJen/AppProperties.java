/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2018, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.PolyJen;

import com.beowurks.BeoCommon.BaseProperties;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class AppProperties extends BaseProperties
{
  private final static String LOOKANDFEEL = "Application LookAndFeel";
  private final static String METALTHEME = "Application MetalTheme";

  private final static String GENERATION_OPTION = "Generation Run Option";
  private final static String MOLECULE_CALCULATIONS = "Molecule Calculations";
  private final static String EVALUATE_INITIATOR_MOLECULES = "Evaluate Initiator Molecules";
  private final static String EVALUATE_MONOMER_MOLECULES = "Evaluate Monomer Molecules";

  private final static String POLYMER_SORT_COLUMN = "Polymer Sort Column";
  private final static String POLYMER_ASCENDING = "Polymer Ascending";
  private final static String POLYMER_COLUMN_COUNT = "Polymer Column Count";
  private final static String POLYMER_COLUMN_ORDER = "Polymer Column Order";
  private final static String POLYMER_COLUMN_WIDTH = "Polymer Width";

  private final static String REPORT_EXPORT_DIRECTORY = "Report Export Directory";
  private final static String REPORT_EXPORT_FILTER = "Report Export Filter";

  private final static String LOGIN_EMAIL = "Login Email Address";

  // ---------------------------------------------------------------------------
  public AppProperties(final String tcDirectory, final String tcFileName, final String tcHeader)
  {
    super(tcDirectory, tcFileName, tcHeader);
  }

  // ---------------------------------------------------------------------------
  public String getLookAndFeel()
  {
    return (this.getProperty(AppProperties.LOOKANDFEEL, "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"));
  }

  // ---------------------------------------------------------------------------
  public void setLookAndFeel(final String tcLookFeel)
  {
    this.setProperty(AppProperties.LOOKANDFEEL, tcLookFeel);
  }

  // ---------------------------------------------------------------------------
  public String getMetalTheme()
  {
    return (this.getProperty(AppProperties.METALTHEME, "javax.swing.plaf.metal.OceanTheme"));
  }

  // ---------------------------------------------------------------------------
  public void setMetalTheme(final String tcTheme)
  {
    this.setProperty(AppProperties.METALTHEME, tcTheme);
  }

  // ---------------------------------------------------------------------------
  public int getGenerationRunOption()
  {
    return (this.getProperty(AppProperties.GENERATION_OPTION, 0));
  }

  // ---------------------------------------------------------------------------
  public void setGenerationRunOption(final int tnOption)
  {
    this.setProperty(AppProperties.GENERATION_OPTION, tnOption);
  }

  // ---------------------------------------------------------------------------
  public boolean getMoleculeCalculations()
  {
    return (this.getProperty(AppProperties.MOLECULE_CALCULATIONS, true));
  }

  // ---------------------------------------------------------------------------
  public void setMoleculeCalculations(final boolean tlCalculate)
  {
    this.setProperty(AppProperties.MOLECULE_CALCULATIONS, tlCalculate);
  }

  // ---------------------------------------------------------------------------
  public int getEvaluateInitiatorMolecules()
  {
    return (this.getProperty(AppProperties.EVALUATE_INITIATOR_MOLECULES, 10000));
  }

  // ---------------------------------------------------------------------------
  public void setEvaluateInitiatorMolecules(final int tnMolecules)
  {
    this.setProperty(AppProperties.EVALUATE_INITIATOR_MOLECULES, tnMolecules);
  }

  // ---------------------------------------------------------------------------
  public int getEvaluateMonomerMolecules()
  {
    return (this.getProperty(AppProperties.EVALUATE_MONOMER_MOLECULES, 1000000));
  }

  // ---------------------------------------------------------------------------
  public void setEvaluateMonomerMolecules(final int tnMolecules)
  {
    this.setProperty(AppProperties.EVALUATE_MONOMER_MOLECULES, tnMolecules);
  }

  // ---------------------------------------------------------------------------
  public int getPolymerSortColumn()
  {
    return (this.getProperty(AppProperties.POLYMER_SORT_COLUMN, 0));
  }

  // ---------------------------------------------------------------------------
  public void setPolymerSortColumn(final int tnColumn)
  {
    this.setProperty(AppProperties.POLYMER_SORT_COLUMN, tnColumn);
  }

  // ---------------------------------------------------------------------------
  public boolean getPolymerAscending()
  {
    return (this.getProperty(AppProperties.POLYMER_ASCENDING, true));
  }

  // ---------------------------------------------------------------------------
  public void setPolymerAscending(final boolean tlAscending)
  {
    this.setProperty(AppProperties.POLYMER_ASCENDING, tlAscending);
  }

  // ---------------------------------------------------------------------------
  public int getPolymerColumnCount()
  {
    return (this.getProperty(AppProperties.POLYMER_COLUMN_COUNT, 0));
  }

  // ---------------------------------------------------------------------------
  public void setPolymerColumnCount(final int tnCount)
  {
    this.setProperty(AppProperties.POLYMER_COLUMN_COUNT, tnCount);
  }

  // ---------------------------------------------------------------------------
  public int getPolymerOrder(final int tnColumn)
  {
    return (this.getProperty(AppProperties.POLYMER_COLUMN_ORDER + tnColumn, tnColumn));
  }

  // ---------------------------------------------------------------------------
  public void setPolymerOrder(final int tnColumn, final int tnOrder)
  {
    this.setProperty(AppProperties.POLYMER_COLUMN_ORDER + tnColumn, tnOrder);
  }

  // ---------------------------------------------------------------------------
  public int getPolymerWidth(final int tnColumn)
  {
    return (this.getProperty(AppProperties.POLYMER_COLUMN_WIDTH + tnColumn, 75));
  }

  // ---------------------------------------------------------------------------
  public void setPolymerWidth(final int tnColumn, final int tnWidth)
  {
    this.setProperty(AppProperties.POLYMER_COLUMN_WIDTH + tnColumn, tnWidth);
  }

  // ---------------------------------------------------------------------------
  public String getReportExportDirectory()
  {
    return (this.getProperty(AppProperties.REPORT_EXPORT_DIRECTORY, System.getProperty("user.home")));
  }

  // ---------------------------------------------------------------------------
  public void setReportExportDirectory(final String tcDirectory)
  {
    this.setProperty(AppProperties.REPORT_EXPORT_DIRECTORY, tcDirectory);
  }

  // ---------------------------------------------------------------------------
  public int getReportExportFilter()
  {
    return (this.getProperty(AppProperties.REPORT_EXPORT_FILTER, 0));
  }

  // ---------------------------------------------------------------------------
  public void setReportExportFilter(final int tnFilter)
  {
    this.setProperty(AppProperties.REPORT_EXPORT_FILTER, tnFilter);
  }

  // ---------------------------------------------------------------------------
  public String getLoginEmail()
  {
    return (this.getProperty(AppProperties.LOGIN_EMAIL, ""));
  }

  // ---------------------------------------------------------------------------
  public void setLoginEmail(final String tcLogin)
  {
    this.setProperty(AppProperties.LOGIN_EMAIL, tcLogin);
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
