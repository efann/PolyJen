/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.polyjen;

import com.beowurks.BeoCommon.BaseFrame;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.net.URL;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class PolyJenBaseFrame extends BaseFrame
{
  protected MainFrame foMainFrame;

  private boolean flHaveMenus = true;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public PolyJenBaseFrame()
  {
    this.foMainFrame = (MainFrame) this;
  }

  // ---------------------------------------------------------------------------
  public PolyJenBaseFrame(final MainFrame toMainFrame, final String tcTitle, final boolean tlPackFrame, final boolean tlHaveMenus)
  {
    super(tcTitle, tlPackFrame);

    this.foMainFrame = toMainFrame;
    this.flHaveMenus = tlHaveMenus;
  }

  // ---------------------------------------------------------------------------
  // Component initialization
  @Override
  protected void jbInit() throws Exception
  {
    super.jbInit();

    if (this.flHaveMenus)
    {
      if (this.foMainFrame != this)
      {
        this.createDuplicateTopMenu(this.foMainFrame.getJMenuBar());
        this.setJMenuBar(this.foDuplicateMenuBar);
      }
    }

    final URL loURLIcon = PolyJenBaseFrame.class.getResource("/com/beowurks/polyjen/images/Icon.jpg");
    if (loURLIcon != null)
    {
      final Image loImage = Toolkit.getDefaultToolkit().createImage(loURLIcon);

      // If you don't getScaledInstance, then the Window seems to resize
      // according to Image.SCALE_DEFAULT and doesn't look quite right.
      this.setIconImage(loImage.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }
  }

  // ---------------------------------------------------------------------------
  // Overridden so we can exit when window is closed
  @Override
  protected void processWindowEvent(final WindowEvent e)
  {
    // This has to go here. this.foMainFrame.updateMenus() uses the getActiveFrame
    // which is set in super.processWindowEvent.
    super.processWindowEvent(e);

    if (e.getID() == WindowEvent.WINDOW_ACTIVATED)
    {
      if (this.flHaveMenus)
      {
        this.foMainFrame.updateMenusAndToolBars();
      }
    }
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void releasePointers()
  {
    this.foMainFrame = null;

    super.releasePointers();
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
