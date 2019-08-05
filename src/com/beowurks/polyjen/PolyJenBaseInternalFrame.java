/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.polyjen;

import com.beowurks.BeoCommon.BaseInternalFrame;

import javax.swing.ImageIcon;
import javax.swing.event.InternalFrameEvent;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class PolyJenBaseInternalFrame extends BaseInternalFrame
{
  private final MainFrame foMainFrame;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public PolyJenBaseInternalFrame(final MainFrame toMainFrame, final String tcTitle)
  {
    super(tcTitle);

    this.foMainFrame = toMainFrame;
  }

  // ---------------------------------------------------------------------------
  // Component initialization
  @Override
  protected void jbInit() throws Exception
  {
    super.jbInit();
    this.foMainFrame.addToDesktop(this);

    final URL loURLIcon = PolyJenBaseInternalFrame.class.getResource("/com/beowurks/polyjen/images/Icon.jpg");
    if (loURLIcon != null)
    {
      final Image loImage = Toolkit.getDefaultToolkit().createImage(loURLIcon);
      // You need to resize the icon, cause unlike JFrame, JInternalFrame won't
      final ImageIcon loImageIcon = new ImageIcon(loImage.getScaledInstance(16, 16, Image.SCALE_SMOOTH));

      this.setFrameIcon(loImageIcon);
    }
  }

  // ---------------------------------------------------------------------------
  public MainFrame getMainFrame()
  {
    return (this.foMainFrame);
  }

  // ---------------------------------------------------------------------------
  @Override
  public void internalFrameClosed(final InternalFrameEvent e)
  {
    this.foMainFrame.updateMenusAndToolBars();

    super.internalFrameClosed(e);
  }

  // ---------------------------------------------------------------------------
  @Override
  public void internalFrameActivated(final InternalFrameEvent e)
  {
    super.internalFrameActivated(e);

    this.foMainFrame.updateMenusAndToolBars();
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void releasePointers()
  {
    this.foMainFrame.removeFromDesktop(this);

    super.releasePointers();
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
