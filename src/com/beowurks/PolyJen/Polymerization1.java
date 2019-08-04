/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.PolyJen;

import com.beowurks.BeoCommon.BaseButton;
import com.beowurks.BeoCommon.GridBagLayoutHelper;
import com.beowurks.BeoCommon.Util;
import com.beowurks.BeoTable.SortingTable;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class Polymerization1 extends PolyJenBaseInternalFrame implements ActionListener, MouseListener
{
  protected final BaseButton btnRemove1 = new BaseButton(96, 30);
  protected final BaseButton btnModify1 = new BaseButton(96, 30);
  protected final BaseButton btnCreate1 = new BaseButton(96, 30);
  protected final BaseButton btnClone1 = new BaseButton(96, 30);

  protected final BaseButton btnVerify1 = new BaseButton(96, 30);
  protected SortingTable grdPolymer1;

  private Box boxButtons1;
  private final JScrollPane grdPolymerScrollPane11 = new JScrollPane();
  private final String fcDataDirectory = Global.getXMLTablesPath();

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public Polymerization1(final MainFrame toMainFrame, final String tcTitle)
  {
    super(toMainFrame, tcTitle);

    try
    {
      this.jbInit();
    }
    catch (final Exception loErr)
    {
      Util.showStackTraceInMessage(this, loErr, "Exception");
    }
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void jbInit() throws Exception
  {
    super.jbInit();

    this.setupTables();
    this.setupButtons();
    this.setupListeners();
    this.setupLayouts();

    this.getMainFrame().foListComponents.matchTableToComboBox();
  }

  // ---------------------------------------------------------------------------
  private void setupTables()
  {
    this.grdPolymer1 = this.getMainFrame().foListComponents.getTable();

    final Dimension ldScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

    final int lnWidth = (int) (ldScreenSize.width * 0.6);
    this.grdPolymerScrollPane11.setPreferredSize(new Dimension(lnWidth, (int) (ldScreenSize.height * 0.6)));
    this.grdPolymerScrollPane11.setMaximumSize(new Dimension(2147483647, 2147483647));
    this.grdPolymerScrollPane11.getViewport().add(this.grdPolymer1, null);

    this.grdPolymer1.setBackground(this.getBackground());
    this.grdPolymer1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    this.grdPolymer1.setRowSelectionAllowed(true);
    this.grdPolymer1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

  // ---------------------------------------------------------------------------
  private void setupButtons()
  {
    this.btnCreate1.setText(Global.MENU_CREATE);
    this.btnCreate1.setMnemonic(Global.MENU_CREATE_MNEMONIC);
    this.btnCreate1.setToolTipText(Global.MENU_CREATE_HINT);
    this.btnCreate1.setActionCommand(Global.MENU_CREATE);
    this.btnCreate1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/create22.png")));

    this.btnClone1.setText(Global.MENU_CLONE);
    this.btnClone1.setMnemonic(Global.MENU_CLONE_MNEMONIC);
    this.btnClone1.setToolTipText(Global.MENU_CLONE_HINT);
    this.btnClone1.setActionCommand(Global.MENU_CLONE);
    this.btnClone1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/clone22.png")));

    this.btnModify1.setText(Global.MENU_MODIFY);
    this.btnModify1.setMnemonic(Global.MENU_MODIFY_MNEMONIC);
    this.btnModify1.setToolTipText(Global.MENU_MODIFY_HINT);
    this.btnModify1.setActionCommand(Global.MENU_MODIFY);
    this.btnModify1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/modify22.png")));

    this.btnRemove1.setText(Global.MENU_REMOVE);
    this.btnRemove1.setMnemonic(Global.MENU_REMOVE_MNEMONIC);
    this.btnRemove1.setToolTipText(Global.MENU_REMOVE_HINT);
    this.btnRemove1.setActionCommand(Global.MENU_REMOVE);
    this.btnRemove1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/remove22.png")));

    this.btnVerify1.setText(Global.MENU_VERIFY);
    this.btnVerify1.setMnemonic(Global.MENU_VERIFY_MNEMONIC);
    this.btnVerify1.setToolTipText(Global.MENU_VERIFY_HINT);
    this.btnVerify1.setActionCommand(Global.MENU_VERIFY);
    this.btnVerify1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/PolyJen/images/verify22.png")));
  }

  // ---------------------------------------------------------------------------
  private void setupLayouts()
  {
    final GridBagLayoutHelper loGridBag = new GridBagLayoutHelper();
    this.getContentPane().setLayout(loGridBag);

    this.boxButtons1 = Box.createVerticalBox();
    this.boxButtons1.add(this.btnCreate1);
    this.boxButtons1.add(this.btnClone1);
    this.boxButtons1.add(this.btnModify1);
    this.boxButtons1.add(this.btnRemove1);
    this.boxButtons1.add(Box.createVerticalStrut(BaseButton.BASE_HEIGHT));
    this.boxButtons1.add(this.btnVerify1);

    loGridBag.setInsets(10, 10, 4, 4);
    this.getContentPane().add(this.grdPolymerScrollPane11, loGridBag.getConstraint(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH));

    loGridBag.setInsets(10, 4, 4, 10);
    this.getContentPane().add(this.boxButtons1, loGridBag.getConstraint(1, 0, GridBagConstraints.NORTH, GridBagConstraints.NONE));
  }

  // ---------------------------------------------------------------------------
  private void setupListeners()
  {
    this.btnCreate1.addActionListener(this);
    this.btnClone1.addActionListener(this);
    this.btnModify1.addActionListener(this);
    this.btnRemove1.addActionListener(this);
    this.btnVerify1.addActionListener(this);

    this.grdPolymer1.addMouseListener(this);

    this.getMainFrame().foListComponents.addGridListeners();
  }

  // ---------------------------------------------------------------------------
  private void createData(final String tcAction)
  {
    final PolymerParametersXML loParams = new PolymerParametersXML(this.fcDataDirectory, PolymerParametersXML.createUniqueFileName(this.fcDataDirectory), false);
    boolean llRemoveFile = true;

    if (loParams.createLock())
    {
      final PolymerizationEdit1 loDlg = new PolymerizationEdit1(this.getParentFrame(), tcAction, tcAction, loParams);
      loDlg.setVisible(true);

      if (loDlg.getResults() == Global.EDIT_OKAY)
      {
        llRemoveFile = false;
        this.getMainFrame().foListComponents.refreshData();
        this.getMainFrame().foListComponents.setTableToUniqueName(loParams.getFileName());
      }

      loParams.releaseLock();
    }
    else
    {
      Util.infoMessage(this, "Someone else is currently editing this record. Try again at a later time.");
    }

    // Remember: PolymerParameterProperties.createUniqueFileName(this.fcDataDirectory) creates a file.
    if (llRemoveFile)
    {
      loParams.deleteFile();
    }
  }

  // ---------------------------------------------------------------------------
  private void cloneData(final PolymerParametersXML toParams, final String tcAction)
  {
    final String lcUniqueFile = PolymerParametersXML.createUniqueFileName(this.fcDataDirectory);

    if (!toParams.copyToFile(this.fcDataDirectory, lcUniqueFile))
    {
      Util.infoMessage(this, "Unable to copy the data to a new record. Please contact support@beowurks.com");
    }
    else
    {
      boolean llRemoveFile = true;

      final PolymerParametersXML loParams = new PolymerParametersXML(this.fcDataDirectory, lcUniqueFile, true);

      if (loParams.createLock())
      {
        final PolymerizationEdit1 loDlg = new PolymerizationEdit1(this.getParentFrame(), tcAction, tcAction, loParams);
        loDlg.setVisible(true);

        if (loDlg.getResults() == Global.EDIT_OKAY)
        {
          llRemoveFile = false;
          this.getMainFrame().foListComponents.refreshData();
          this.getMainFrame().foListComponents.setTableToUniqueName(loParams.getFileName());
        }

        loParams.releaseLock();
      }
      else
      {
        Util.infoMessage(this, "Someone else is currently editing this record. Try again at a later time.");
      }

      // Remember: PolymerParameterProperties.createUniqueFileName(this.fcDataDirectory) creates a file.
      if (llRemoveFile)
      {
        loParams.deleteFile();
      }
    }
  }

  // ---------------------------------------------------------------------------
  private void modifyData(final PolymerParametersXML toParams, final String tcAction)
  {
    if (toParams.createLock())
    {
      final PolymerizationEdit1 loDlg = new PolymerizationEdit1(this.getParentFrame(), tcAction, tcAction, toParams);
      loDlg.setVisible(true);

      if (loDlg.getResults() == Global.EDIT_OKAY)
      {
        this.getMainFrame().foListComponents.refreshData();
      }

      toParams.releaseLock();
    }
    else
    {
      Util.infoMessage(this, "Someone else is currently editing this record. Try again at a later time.");
    }
  }

  // ---------------------------------------------------------------------------
  private void removeData(final PolymerParametersXML toParams)
  {
    if (toParams.createLock())
    {
      if (Util.yesNo(this, "Do you want to remove the record titled \"" + toParams.getTitle() + "\" (Unique Name '" + toParams.getFileName() + "')?"))
      {
        if (toParams.deleteFile())
        {
          this.getMainFrame().foListComponents.refreshData();
        }
        else
        {
          Util.infoMessage(this, "Unable to remove this record. Please contact support@beowurks.com");
        }
      }
      toParams.releaseLock();
    }
    else
    {
      Util.infoMessage(this, "Someone else is currently editing this record. Try again at a later time.");
    }
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void finalOperations()
  {
    this.getMainFrame().foListComponents.setProperties();

    super.finalOperations();
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void removeListeners()
  {
    this.btnCreate1.removeActionListener(this);
    this.btnClone1.removeActionListener(this);
    this.btnModify1.removeActionListener(this);
    this.btnRemove1.removeActionListener(this);
    this.btnVerify1.removeActionListener(this);

    this.grdPolymer1.removeMouseListener(this);

    this.getMainFrame().foListComponents.removeGridListeners();

    super.removeListeners();
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void releasePointers()
  {
    this.grdPolymerScrollPane11.getViewport().removeAll();
    this.grdPolymerScrollPane11.removeAll();

    this.grdPolymer1 = null;

    super.releasePointers();
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface ActionListener
  // ---------------------------------------------------------------------------
  @Override
  public void actionPerformed(final ActionEvent e)
  {
    final Object loObject = e.getSource();

    if (loObject == this.btnCreate1)
    {
      this.createData(e.getActionCommand());
    }
    else
    {
      final PolymerParametersXML loParams = this.getMainFrame().foListComponents.getPropertiesFromTableSelection();

      if (loParams == null)
      {
        Util.errorMessage(this, "You have not yet selected a record.");
      }
      else
      {
        if (loObject == this.btnVerify1)
        {
          loParams.verifyData(this.getParentFrame(), Global.VERIFY_DISPLAY_ALWAYS);
        }
        else if (loObject == this.btnRemove1)
        {
          this.removeData(loParams);
        }
        else if (loObject == this.btnModify1)
        {
          this.modifyData(loParams, e.getActionCommand());
        }
        else if (loObject == this.btnClone1)
        {
          this.cloneData(loParams, e.getActionCommand());
        }
      }
    }
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface MouseListener
  // ---------------------------------------------------------------------------
  @Override
  public void mouseClicked(final MouseEvent toEvent)
  {
    if (toEvent.getClickCount() == 2)
    {
      final Object loSource = toEvent.getSource();

      if (loSource instanceof JTable)
      {
        this.btnModify1.doClick();
      }
    }
  }

  // ---------------------------------------------------------------------------
  @Override
  public void mousePressed(final MouseEvent toEvent)
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  public void mouseReleased(final MouseEvent toEvent)
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  public void mouseEntered(final MouseEvent toEvent)
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  public void mouseExited(final MouseEvent toEvent)
  {
  }
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
