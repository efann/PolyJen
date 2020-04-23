/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2020, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.polyjen;

import com.beowurks.BeoCommon.Util;

import java.awt.Component;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class Global
{
  protected final static int TEXT_HEIGHT = 21;

  protected final static String GENERATION_OPTION_SEQ = "Sequential Generation";
  protected final static String GENERATION_OPTION_RAN = "Random Generation";

  protected final static int GENERATION_OPTION_SEQ_NBR = 0;
  protected final static int GENERATION_OPTION_RAN_NBR = 1;

  protected final static int ENGINE_LIMIT = 10000;

  protected final static String MENU_POLYMERIZE = "Polymerization Parameters...";
  protected final static String MENU_CALCULATION = "Calculations...";
  protected final static String MENU_EVALUATE = "Evaluate Engine...";
  protected final static String MENU_REPORTS = "Reports...";
  protected final static String MENU_EXPORT = "Export...";
  protected final static String MENU_PRINT = "Print...";
  protected final static String MENU_EXIT = "Exit";
  protected final static String MENU_CREATE = "Create...";
  protected final static String MENU_CLONE = "Clone...";
  protected final static String MENU_MODIFY = "Modify...";
  protected final static String MENU_REMOVE = "Remove...";
  protected final static String MENU_VERIFY = "Verify";
  protected final static String MENU_RUN = "Run";
  protected final static String MENU_REFRESH = "Refresh";
  protected final static String MENU_LOOKFEEL = "Look & Feel...";
  protected final static String MENU_SAMPLES = "Download Sample Data";
  protected final static String MENU_CASCADE = "Cascade";
  protected final static String MENU_CLOSEALL = "Close All";
  protected final static String MENU_CREDITS = "Credits";
  protected final static String MENU_HELP_DOCUMENTATION_PDF = "Documentation (pdf)";
  protected final static String MENU_ABOUT = "About";

  protected final static char MENU_POLYMERIZE_MNEMONIC = 'O';
  protected final static char MENU_CALCULATION_MNEMONIC = 'C';
  protected final static char MENU_EVALUATE_MNEMONIC = 'V';
  protected final static char MENU_REPORTS_MNEMONIC = 'T';
  protected final static char MENU_EXPORT_MNEMONIC = 'E';
  protected final static char MENU_PRINT_MNEMONIC = 'P';
  protected final static char MENU_EXIT_MNEMONIC = 'X';
  protected final static char MENU_CREATE_MNEMONIC = 'A';
  protected final static char MENU_CLONE_MNEMONIC = 'N';
  protected final static char MENU_MODIFY_MNEMONIC = 'M';
  protected final static char MENU_REMOVE_MNEMONIC = 'V';
  protected final static char MENU_VERIFY_MNEMONIC = 'F';
  protected final static char MENU_RUN_MNEMONIC = 'R';
  protected final static char MENU_REFRESH_MNEMONIC = 'H';
  protected final static char MENU_LOOKFEEL_MNEMONIC = 'L';
  protected final static char MENU_CASCADE_MNEMONIC = 'A';
  protected final static char MENU_CLOSEALL_MNEMONIC = 'L';
  protected final static char MENU_CREDITS_MNEMONIC = 'C';
  protected final static char MENU_HELP_DOCUMENTATION_PDF_MNEMONIC = 'D';
  protected final static char MENU_ABOUT_MNEMONIC = 'A';

  protected final static String MENU_LOOKFEEL_HINT = "Customize the look & feel of this application";
  protected final static String MENU_SAMPLES_HINT = "Download Sample Data for calculations and analysis";
  protected final static String MENU_CLOSEALL_HINT = "Close all of the forms in this program";
  protected final static String MENU_CASCADE_HINT = "Cascade all of the visible forms in this program";
  protected final static String MENU_CREATE_HINT = "Create a new record from scratch";
  protected final static String MENU_CLONE_HINT = "Clone the current record to a new record";
  protected final static String MENU_MODIFY_HINT = "Modify the current record";
  protected final static String MENU_REMOVE_HINT = "Remove the current record";
  protected final static String MENU_VERIFY_HINT = "Verify the data of the current parameters";
  protected final static String MENU_RUN_HINT = "Run the calculations and then graph the results";
  protected final static String MENU_REFRESH_HINT = "Refresh the viewed data";
  protected final static String MENU_POLYMERIZE_HINT = "Open the form for editing the polymerization parameters";
  protected final static String MENU_CALCULATION_HINT = "Open the form for calculating the polymerizing reaction";
  protected final static String MENU_EVALUATE_HINT = "Open the form for evaluating the calculation engine of this program";
  protected final static String MENU_REPORTS_HINT = "Open the form for reports (first time takes longer as libraries are being initiated)";
  protected final static String MENU_EXPORT_HINT = "Export the raw data from the current report to the format of your choosing";
  protected final static String MENU_PRINT_HINT = "Print the current report";
  protected final static String MENU_EXIT_HINT = "Exit the PolyJen program";
  protected final static String MENU_CREDITS_HINT = "Credits for pieces parts used in this program";
  protected final static String MENU_HELP_DOCUMENTATION_PDF_HINT = "Documentation, in Adobe Acrobat format, on how to use this program";
  protected final static String MENU_ABOUT_HINT = "About PolyJen";

  protected final static String BUTTON_DATA_SAVE = "Save";
  protected final static String BUTTON_DATA_CANCEL = "Cancel";

  protected final static char BUTTON_DATA_SAVE_MNEMONIC = 'S';
  protected final static char BUTTON_DATA_CANCEL_MNEMONIC = 'C';

  protected final static String BUTTON_DATA_SAVE_HINT = "Save any changes made to the parameters and close the form";
  protected final static String BUTTON_DATA_CANCEL_HINT = "Cancel any changes made to the parameters and close the form";

  protected final static String POPUP_ACTION_INSERT = "Insert Row";
  protected final static String POPUP_ACTION_DELETE = "Delete Row";

  protected final static char POPUP_ACTION_INSERT_MNEMONIC = 'I';
  protected final static char POPUP_ACTION_DELETE_MNEMONIC = 'D';

  protected final static String POPUP_ACTION_INSERT_HINT = "Insert a new row into the current table";
  protected final static String POPUP_ACTION_DELETE_HINT = "Delete the current row";

  protected final static int MAX_MONOMER_VALUE = Integer.MAX_VALUE - 1;
  protected final static int MIN_MONOMER_VALUE = 2;

  protected final static double MAX_REACTIONRATE_VALUE = 100;
  protected final static double MIN_REACTIONRATE_VALUE = 0.01;

  protected final static int MAX_POLYMER_SITES = 9;
  protected final static int MAX_POLYMER_LEVELS = 3;

  protected final static int DISTRIBUTION_LIMIT = Global.MAX_POLYMER_SITES + 1;
  protected final static int POLYMER_MOLECULE = Global.DISTRIBUTION_LIMIT - 1;

  protected final static int VERIFY_DISPLAY_ON_ERROR = 0;
  protected final static int VERIFY_DISPLAY_ALWAYS = 1;
  protected final static int VERIFY_DISPLAY_NEVER = 2;

  protected final static String FIELD_TITLE = "Title";
  protected final static String FIELD_SITES = "PolymerSites";
  protected final static String FIELD_LEVELS = "ReactionLevels";
  protected final static String FIELD_INITIATORS = "InitiatorMolecules";
  protected final static String FIELD_MW_INITIATOR = "MWofInitiator";
  protected final static String FIELD_MW_MONOMER = "MWofMonomer";
  protected final static String FIELD_CREATED_BY = "CreatedBy";
  protected final static String FIELD_CREATED_ON = "CreatedOn";
  protected final static String FIELD_MODIFIED_BY = "ModifiedBy";
  protected final static String FIELD_MODIFIED_ON = "ModifiedOn";
  protected final static String FIELD_MONOMER_LEVELS = "MonomerReactionLevels";
  protected final static String FIELD_MONOMER_LEVELNO = "Reaction";
  protected final static String FIELD_MONOMER_ATTRIBUTE = "Level";
  protected final static String FIELD_ARRAY_HEADER = "Arrays";
  protected final static String FIELD_ARRAY_GRID = "Grid";
  protected final static String FIELD_ARRAY_LEVEL = "Level";
  protected final static String FIELD_ARRAY_SITE = "Site";
  protected final static String FIELD_ARRAY_CELL = "Cell";
  protected final static String FIELD_ARRAY_ROW = "y";
  protected final static String FIELD_ARRAY_COL = "x";

  protected final static String DATA_PREFIX = "pol";
  protected final static String DATA_POSTFIX_EXT = ".xml";

  protected final static String EDIT_FIRST_ROW = "First (1)";
  protected final static String EDIT_LAST_ROW = "Final (" + (char) Global.EDIT_INFINITY + ")";

  protected final static int EDIT_MINIMUM_ROWS = 2;
  protected final static int EDIT_COLUMNS = 2;
  protected final static String EDIT_COLUMN_1 = "<html><font face=\"Arial\">Monomer<p>Reaction</p></font></html>";
  protected final static String EDIT_COLUMN_2 = "<html><font face=\"Arial\">Reaction<p>Rate %</p></font></html>";

  protected final static int EDIT_GRID_WIDTH = 150;
  protected final static int EDIT_GRID_HEIGHT = 150;
  protected final static int EDIT_TEXTBOX_HEIGHT = 21;
  protected final static int EDIT_CANCEL = -1;
  protected final static int EDIT_OKAY = 1;

  protected final static int ENGINE_MIN_INITIATORS = 10;
  protected final static int ENGINE_MAX_INITIATORS = 2500000;
  protected final static int ENGINE_MIN_MONOMERS = 100;
  protected final static int ENGINE_MAX_MONOMERS = 2000000000;

  protected static class ReactionParametersStructure
  {
    int TotalMonomerReacted;
    final int[][][] ReactionRates = new int[Global.MAX_POLYMER_SITES][][];
  }

  protected static class DistributionStructure
  {
    int Maximum;
    int Minimum;
    int[] Distribution;
  }

  private static final StringBuilder fcExceptionError = new StringBuilder(256);
  private final static int EDIT_INFINITY = 8734;

  // ---------------------------------------------------------------------------
  public Global()
  {
  }

  // ---------------------------------------------------------------------------
  static protected void errorExceptionInThread(final Component toComponent, final Exception toException)
  {
    Global.setExceptionError("Please notify support@beowurks.com of the following error:", toException.toString());

    Util.errorMessageInThread(toComponent, Global.fcExceptionError.toString());
  }

  // ---------------------------------------------------------------------------
  static protected void errorException(final Component toComponent, final Exception toException)
  {
    Global.setExceptionError("Please notify support@beowurks.com of the following error:", toException.toString());

    Util.errorMessage(toComponent, Global.fcExceptionError.toString());
  }

  // ---------------------------------------------------------------------------
  static private void setExceptionError(final String tcMessage, final String tcException)
  {
    Util.clearStringBuilder(Global.fcExceptionError);

    Global.fcExceptionError.append("<html><font face=\"Arial\">");
    Global.fcExceptionError.append(tcMessage);
    Global.fcExceptionError.append(" <br><br><i> ");
    Global.fcExceptionError.append(tcException);
    Global.fcExceptionError.append(" </i><p></p></font></html>");
  }

  // ---------------------------------------------------------------------------
  static protected boolean extractResourceStream(final String tcResourceName, final String tcOutputFolder, final String tcFileName)
  {
    if (Global.isDevelopmentEnvironment())
    {
      return (true);
    }

    if (!Util.makeDirectory(tcOutputFolder))
    {
      Util.errorMessage(null, "Unable to create the path of " + tcOutputFolder + ".");
      return (false);
    }

    boolean llOkay = true;
    InputStream loInput = null;
    FileOutputStream loOutput = null;

    loInput = Thread.currentThread().getContextClassLoader().getResourceAsStream(tcResourceName);
    if (loInput == null)
    {
      Util.errorMessage(null, "Can't find " + tcResourceName);
      return (false);
    }

    try
    {
      loOutput = new FileOutputStream(Util.includeTrailingBackslash(tcOutputFolder) + tcFileName);

      final byte[] lcBuffer = new byte[1024];
      int lnBytesRead;

      do
      {
        lnBytesRead = loInput.read(lcBuffer);
        if (lnBytesRead > 0)
        {
          loOutput.write(lcBuffer, 0, lnBytesRead);
        }
      }
      while (lnBytesRead >= 0);
    }
    catch (final IOException loErr)
    {
      Util.errorMessage(null, loErr.getMessage());
      llOkay = false;
    }

    // You don't need to test for loInput == null as it was tested for around line #239.
    try
    {
      loInput.close();
    }
    catch (final IOException loErr)
    {
      Util.errorMessage(null, loErr.getMessage());
      llOkay = false;
    }

    if (loOutput != null)
    {
      try
      {
        loOutput.close();
      }
      catch (final IOException loErr)
      {
        Util.errorMessage(null, loErr.getMessage());
        llOkay = false;
      }
    }

    return (llOkay);
  }

  // ---------------------------------------------------------------------------
  static protected boolean isDevelopmentEnvironment()
  {
    final String lcUserDir = System.getProperty("user.dir").toLowerCase();
    final String lcFileSeparator = System.getProperty("file.separator");

    return (lcUserDir.contains("intellij" + lcFileSeparator + "polyjen"));
  }

  // ---------------------------------------------------------------------------
  static protected String getXMLTablesPath()
  {
    final String lcPath = Util.includeTrailingBackslash(Global.getUserHomeApplicationPath() + "Data");

    return (Global.ensurePathExists(lcPath));
  }

  // ---------------------------------------------------------------------------
  static protected String getUserHomeApplicationPath()
  {
    final String lcPath = Global.isDevelopmentEnvironment() ? Util.includeTrailingBackslash("\\IntelliJ\\PolyJen") : Util.includeTrailingBackslash(Util.includeTrailingBackslash(Util.includeTrailingBackslash(System.getProperty("user.home")) + "Beowurks") + "PolyJen");

    return (Global.ensurePathExists(lcPath));
  }

  // ---------------------------------------------------------------------------
  static protected String getTemporaryPath()
  {
    final String lcPath = Util.includeTrailingBackslash(Util.includeTrailingBackslash(Util.includeTrailingBackslash(System.getProperty("java.io.tmpdir")) + "Beowurks") + "PolyJen");

    return (Global.ensurePathExists(lcPath));
  }

  // ---------------------------------------------------------------------------
  static private String ensurePathExists(final String tcPath)
  {
    if (!Util.makeDirectory(tcPath))
    {
      Util.errorMessage(null, "Unable to create the path of " + tcPath + ".");
    }

    return (tcPath);
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
