package net.chouppy.tarzhiou;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A cell space in the form it cannot be modified.
 * 
 * @author Jonathan ILIAS-PILLET
 */
public abstract class ReadOnlyCellSpace
{
  /**
   * set of all cells.
   * 
   * @warning all linked cells should be included in this set.
   */
  private Set<PlayableCell> cells;

  public ReadOnlyCellSpace()
  {
    cells = new LinkedHashSet<PlayableCell>();
  }

  /**
   * Returns the cell given a key.
   * 
   * @param this_key
   *          the key that represents the cell
   * @return the required cell or null if none found
   */
  public Cell getCellFromKey(CellKey this_key)
  {
    Iterator<PlayableCell> i = cells.iterator();
    Cell result = null;

    while ((i.hasNext()) && (result == null))
    {
      Cell temp = i.next();
      if (temp.hashCode() == this_key.hashCode())
        result = temp;
    }

    return result;
  }

  /**
   * Provides DOT output
   */
  public String toString()
  {
    StringBuffer result = new StringBuffer();

    result.append("graph\"Tarzhiou Cell Space\" {\n");
    for (Cell current_cell : cells)
    {
      result.append("cell_");
      if (current_cell.getKey() instanceof NameCellKey)
        result.append(((NameCellKey) current_cell.getKey()).toString());
      else
        result.append(current_cell.hashCode());
      result.append(" [label = \"");
      result.append(current_cell.getPiecesCount());
      if (current_cell.getPiecesCount() > 0)
      {
        result.append(" - ");
        result.append(current_cell.getPiecesOwner().getCanonicalName());
      }
      result.append("\"];\n");
      for (Cell neighbor : current_cell.getNeighbors())
      {
        result.append("cell_");
        if (current_cell.getKey() instanceof NameCellKey)
          result.append(((NameCellKey) current_cell.getKey())
                .toString());
        else
          result.append(current_cell.hashCode());
        result.append(" -- cell_");
        if (current_cell.getKey() instanceof NameCellKey)
          result.append(((NameCellKey) neighbor.getKey()).toString());
        else
          result.append(neighbor.hashCode());
        result.append(";\n");
      }
    }
    result.append("}\n");

    return result.toString();
  }

  protected void addCell(PlayableCell thisCell)
  {
    assert (!cells.contains(thisCell));

    cells.add(thisCell);
  }

  protected void linkCells(LinkeableCell a, LinkeableCell b)
  {
    assert (cells.contains(a));
    assert (cells.contains(b));

    a.linkTo(b);
  }

  protected boolean validate()
  {
    boolean is_valid = true;
    Iterator<PlayableCell> i = cells.iterator();
    Cell currentCell;

    while (i.hasNext() && is_valid)
    {
      currentCell = i.next();
      if (currentCell.getCapacity() < 1)
      {
        is_valid = false;
      }
      for (Cell neighbor : currentCell.getNeighbors())
      {
        if (!hasCell(neighbor))
          is_valid = false;
      }
    }

    return is_valid;
  }

  /**
   * Indicates if the specified cell exists in the cell space
   * 
   * @param thisCell the requested cell 
   * 
   * @return true if the cell is present in the cell space
   */
  protected boolean hasCell(Cell thisCell)
  {
    return cells.contains(thisCell);
  }

  /**
   * TODO: fix this : should not give a reference to a private attribute
   * 
   * @return
   */
  protected Set<PlayableCell> getCells()
  {
    return cells;
  }
}
