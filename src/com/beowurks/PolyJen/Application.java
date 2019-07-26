/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.PolyJen;

import com.beowurks.BeoCommon.Util;
import com.beowurks.BeoLookFeel.LFCommon;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class Application
{
  protected final AppProperties foAppProperties = new AppProperties(Global.getUserHomeApplicationPath(), System.getProperty("user.name") + ".Properties", "PolyJen� Application Properties - DO NOT EDIT . . . please");

  // ---------------------------------------------------------------------------
  // Construct the application
  public Application()
  {
    // Turns off the security sandbox which should improve performance somewhat.
    System.setSecurityManager(null);

    String lcTitle = Util.buildTitleFromManifest(this);
    if (Global.isDevelopmentEnvironment())
    {
      lcTitle += " (Development Version)";
    }

    Util.setTitle(lcTitle);

    this.setLookAndFeel();

    final MainFrame loFrame = new MainFrame(this);
    loFrame.makeVisible(true);
  }

  // ---------------------------------------------------------------------------
  private void setLookAndFeel()
  {
    // You can a list of the defaults by doing the following:
    // Util.listDefaultSettings();

    Util.setUIManagerAppearances();

    LFCommon.setLookFeel(this.foAppProperties.getLookAndFeel(), this.foAppProperties.getMetalTheme(), null, true);
  }

  // ---------------------------------------------------------------------------
  // Main method
  public static void main(final String[] args)
  {
    new Application();
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
