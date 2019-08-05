/*
 * PolyJen: a program that predicts chain-growth polymerization (specifically oxyalkylation)
 *          reactions and minimizes expensive laboratory experimentation.
 *
 * Copyright(c) 2003-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.polyjen;

import com.beowurks.BeoCommon.Util;
import com.beowurks.BeoTable.SortingTable;
import com.beowurks.BeoTable.SortingTableModel;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class ListComponents implements ListSelectionListener, ActionListener
{
  private final static int UNIQUE_NAME_COLUMN = 0;
  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;
  private final AppProperties foAppProperties;
  private final String fcDataDirectory;
  private SortingTable grdPolymer1 = null;
  private JComboBox cboParameterList1 = null;
  private Vector<ListIndex> foList = null;
  private int fnInitialSortColumn;
  private boolean flInitialAscending;
  private int[][] faColumnProps = null;
  private boolean flUpdatingProgrammatically = false;

  // ---------------------------------------------------------------------------
  public ListComponents(final String tcDataDirectory, final AppProperties toAppProperties)
  {
    this.fcDataDirectory = tcDataDirectory;

    this.foAppProperties = toAppProperties;

    this.getProperties();
  }

  // ---------------------------------------------------------------------------
  private static boolean isComponentOnActiveWindow(final JComponent toComponent)
  {
    boolean llActive = false;

    final Container loContainer = toComponent.getTopLevelAncestor();
    if (loContainer != null)
    {
      if (loContainer instanceof Window)
      {
        llActive = ((Window) loContainer).isActive();
      }

    }

    return (llActive);
  }

  // ---------------------------------------------------------------------------
  public SortingTable getTable()
  {
    return (this.grdPolymer1);
  }

  // ---------------------------------------------------------------------------
  public JComboBox getComboBox()
  {
    return (this.cboParameterList1);
  }

  // ---------------------------------------------------------------------------
  public Vector<ListIndex> getVector()
  {
    return (this.foList);
  }

  // ---------------------------------------------------------------------------
  public String getCurrentUniqueNameFromComboBox()
  {
    final int lnIndex = this.cboParameterList1.getSelectedIndex();
    if (lnIndex < 0)
    {
      return (null);
    }

    return ((this.foList.get(lnIndex)).fcFileName);
  }

  // ---------------------------------------------------------------------------
  public PolymerParametersXML getPropertiesFromComboBoxSelection()
  {
    final String lcUnique = this.getCurrentUniqueNameFromComboBox();
    if (lcUnique == null)
    {
      return (null);
    }

    return (new PolymerParametersXML(this.fcDataDirectory, lcUnique, true));
  }

  // ---------------------------------------------------------------------------
  public void matchTableToComboBox()
  {
    final String lcUnique = this.getCurrentUniqueNameFromComboBox();
    if (lcUnique == null)
    {
      return;
    }

    final int lnRows = this.grdPolymer1.getRowCount();
    final SortingTableModel loModel = this.grdPolymer1.getSortModel();
    for (int i = 0; i < lnRows; ++i)
    {
      if (loModel.getValueAt(i, ListComponents.UNIQUE_NAME_COLUMN).toString().compareTo(lcUnique) == 0)
      {
        this.grdPolymer1.setRowSelectionInterval(i, i);
        break;
      }
    }
  }

  // ---------------------------------------------------------------------------
  public void matchComboBoxToTable()
  {
    final String lcUnique = this.getCurrentUniqueNameFromTable();
    if (lcUnique == null)
    {
      return;
    }

    final int lnRows = this.foList.size();
    for (int i = 0; i < lnRows; ++i)
    {
      final String lcFileName = (this.foList.get(i)).fcFileName;
      if (lcFileName.compareTo(lcUnique) == 0)
      {
        this.cboParameterList1.setSelectedIndex(i);
        break;
      }
    }
  }

  // ---------------------------------------------------------------------------
  public String getCurrentUniqueNameFromTable()
  {
    final int lnRow = this.grdPolymer1.getSelectedRow();
    if (lnRow < 0)
    {
      return (null);
    }

    final SortingTableModel loModel = this.grdPolymer1.getSortModel();

    return (loModel.getValueAt(lnRow, ListComponents.UNIQUE_NAME_COLUMN).toString());
  }

  // ---------------------------------------------------------------------------
  public PolymerParametersXML getPropertiesFromTableSelection()
  {
    final String lcUnique = this.getCurrentUniqueNameFromTable();
    if (lcUnique == null)
    {
      return (null);
    }

    return (new PolymerParametersXML(this.fcDataDirectory, lcUnique, true));
  }

  // ---------------------------------------------------------------------------
  public void initializeComponents()
  {
    boolean llRefresh = false;

    if (this.grdPolymer1 == null)
    {
      llRefresh = true;

      this.grdPolymer1 = new SortingTable();
      this.grdPolymer1.setupColumns(new Object[][]{{"Unique Name", ""}, {"Title", ""}, {"# of Initiator Molecules", 0}, {"Molecular Weight of Initiator", (double) 0}, {"Molecular Weight of Monomer", (double) 0}, {"Polymer Sites", 0}, {"Monomer Levels Used", 0}, {"Created On", new Date()}, {"Created By", ""}, {"Last Modified On", new Date()}, {"Last Modified By", ""}});

      this.grdPolymer1.setupHeaderRenderer();

      this.grdPolymer1.setupColumnWidthsThenPositions(this.faColumnProps);
    }

    if (this.cboParameterList1 == null)
    {
      llRefresh = true;

      this.cboParameterList1 = new JComboBox();
    }

    if (this.foList == null)
    {
      llRefresh = true;

      this.foList = new Vector<>();
    }

    if (llRefresh)
    {
      this.refreshData();
    }
  }

  // ---------------------------------------------------------------------------
  public void addGridListeners()
  {
    this.grdPolymer1.getSelectionModel().addListSelectionListener(this);
  }

  // ---------------------------------------------------------------------------
  public void removeGridListeners()
  {
    this.grdPolymer1.getSelectionModel().removeListSelectionListener(this);
  }

  // ---------------------------------------------------------------------------
  public void addComboListeners()
  {
    this.cboParameterList1.addActionListener(this);
  }

  // ---------------------------------------------------------------------------
  public void removeComboListeners()
  {
    this.cboParameterList1.removeActionListener(this);
  }

  // ---------------------------------------------------------------------------
  public boolean isReadyToRefresh()
  {
    return (((this.foList != null) || (this.cboParameterList1 != null) || (this.foList != null)));
  }

  // ---------------------------------------------------------------------------
  public void refreshData()
  {
    if (!this.isReadyToRefresh())
    {
      Util.errorMessage(null, "Components have not been initialized in refreshData!");
      return;
    }

    this.flUpdatingProgrammatically = true;

    final String lcUnique = this.getCurrentUniqueNameFromTable();

    this.foList.clear();
    this.cboParameterList1.removeAllItems();
    final SortingTableModel loModel = this.grdPolymer1.getSortModel();
    loModel.clearAll();

    final File loFile = new File(this.fcDataDirectory);
    final ListFileFilter loFilterData = new ListFileFilter();

    final File[] laList = loFile.listFiles(loFilterData);

    if (laList == null)
    {
      return;
    }

    // Populate the grid.
    for (final File loList : laList)
    {
      final PolymerParametersXML loProp = new PolymerParametersXML(this.fcDataDirectory, loList.getName(), true);

      final ListIndex loIndex = new ListIndex();
      loIndex.fcTitle = loProp.getTitle();
      loIndex.fcFileName = loProp.getFileName();
      this.foList.add(loIndex);

      loModel.addRow(new Object[]{loProp.getFileName(), loProp.getTitle(), loProp.getInitiatorMolecules(), loProp.getMWOfInitiator(), loProp.getMWOfMonomer(), loProp.getSites(), loProp.getLevels(), loProp.getCreatedOn(), loProp.getCreatedBy(), loProp.getModifiedOn(), loProp.getModifiedBy()});
    }

    final boolean llLoadFromProperties = !loModel.isSorted();

    final int lnColumn = llLoadFromProperties ? this.fnInitialSortColumn : loModel.getSortColumn();

    this.grdPolymer1.sortColumn(lnColumn, false, llLoadFromProperties ? this.flInitialAscending : this.grdPolymer1.getSortButtonRenderer().isCurrentColumnAscending());

    this.foList.sort(new StringComparator());

    // Populate the combo box.
    for (final ListIndex loList : this.foList)
    {
      this.cboParameterList1.addItem(loList.fcTitle);
    }

    // Reselect the previous selection if possible.
    this.setTableToUniqueName(lcUnique);

    this.matchTableToComboBox();
    this.flUpdatingProgrammatically = false;
    this.grdPolymer1.autoFitAllColumns();
  }

  // ---------------------------------------------------------------------------
  protected void setTableToUniqueName(final String tcUnique)
  {
    if (tcUnique == null)
    {
      return;
    }

    final SortingTableModel loModel = this.grdPolymer1.getSortModel();

    final int lnRows = this.grdPolymer1.getRowCount();
    for (int i = 0; i < lnRows; ++i)
    {
      if (loModel.getValueAt(i, ListComponents.UNIQUE_NAME_COLUMN).toString().compareTo(tcUnique) == 0)
      {
        this.grdPolymer1.setRowSelectionInterval(i, i);
        this.matchComboBoxToTable();
        break;
      }
    }
  }

  // ---------------------------------------------------------------------------
  protected void getProperties()
  {
    final AppProperties loProp = this.foAppProperties;

    this.fnInitialSortColumn = loProp.getPolymerSortColumn();
    this.flInitialAscending = loProp.getPolymerAscending();

    final int lnColumns = loProp.getPolymerColumnCount();
    if (lnColumns < 1)
    {
      this.faColumnProps = null;
    }

    else
    {
      this.faColumnProps = new int[lnColumns][SortingTable.COLUMN_PROPERTIES];
      for (int i = 0; i < lnColumns; ++i)
      {
        this.faColumnProps[i][SortingTable.COLUMN_ORDER] = loProp.getPolymerOrder(i);
        this.faColumnProps[i][SortingTable.COLUMN_WIDTH] = loProp.getPolymerWidth(i);
      }
    }

  }

  // ---------------------------------------------------------------------------
  protected void setProperties()
  {
    final AppProperties loProp = this.foAppProperties;

    loProp.setPolymerSortColumn(this.grdPolymer1.getSortModel().getSortColumn());
    loProp.setPolymerAscending(this.grdPolymer1.getSortButtonRenderer().isCurrentColumnAscending());

    final TableColumnModel loModel = this.grdPolymer1.getColumnModel();
    final int lnColumns = loModel.getColumnCount();

    loProp.setPolymerColumnCount(lnColumns);

    for (int i = 0; i < lnColumns; ++i)
    {
      final int lnModelColumn = this.grdPolymer1.convertColumnIndexToModel(i);
      loProp.setPolymerWidth(lnModelColumn, loModel.getColumn(i).getWidth());
      loProp.setPolymerOrder(lnModelColumn, i);
    }

  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface ActionListener
  // ---------------------------------------------------------------------------
  @Override
  public void actionPerformed(final ActionEvent e)
  {
    if (this.flUpdatingProgrammatically)
    {
      return;
    }

    // This prevents infinite looping: combobox sets the table which resets the combobox
    // which resets the table, etc. Only if the form, where the combobox resides,
    // is active does the table get set.
    if (ListComponents.isComponentOnActiveWindow(this.cboParameterList1))
    {
      // You don't have to check if the frame containing the table is active
      // 'cause the table actually belongs to the ListComponents class.
      this.matchTableToComboBox();
    }
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface ListSelectionListener
  // ---------------------------------------------------------------------------
  @Override
  public void valueChanged(final ListSelectionEvent e)
  {
    if (this.flUpdatingProgrammatically)
    {
      return;
    }

    // valueChanged() method is called twice when row selection is made.
    // - before row is selected, ListSelectionEvent.getValueIsAdjusting() returns true
    // - after row is selected, ListSelectionEvent.getValueIsAdjusting() returns false
    if (e.getValueIsAdjusting())
    {
      return;
    }

    // This prevents infinite looping: the table sets the combobox which resets the table
    // which resets the combobox, etc. Only if the form, where the table resides,
    // is active does the combobox get set.
    if (ListComponents.isComponentOnActiveWindow(this.grdPolymer1))
    {
      // You don't have to check if the frame containing the combobox is active
      // 'cause the combobox actually belongs to the ListComponents class.
      this.matchComboBoxToTable();
    }
  }
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
}

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
class ListIndex
{
  protected String fcFileName;
  protected String fcTitle;
}

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
class ListFileFilter implements FileFilter
{
  // ---------------------------------------------------------------------------
  public ListFileFilter()
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  public boolean accept(final File toFile)
  {
    if (toFile == null)
    {
      return (false);
    }

    if (toFile.isDirectory())
    {
      return (false);
    }

    return (toFile.getAbsolutePath().endsWith(Global.DATA_POSTFIX_EXT));
  }
  // ---------------------------------------------------------------------------
}

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
class StringComparator implements Comparator<ListIndex>, Serializable
{
  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public StringComparator()
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  public int compare(final ListIndex toValue1, final ListIndex toValue2)
  {
    final String loValue1 = toValue1.fcTitle;
    final String loValue2 = toValue2.fcTitle;

    if ((loValue1 == null) && (loValue2 == null))
    {
      return (0);
    }
    else if (loValue1 == null)
    {
      return (1);
    }
    else if (loValue2 == null)
    {
      return (-1);
    }

    return (loValue1.compareToIgnoreCase(loValue2));
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
