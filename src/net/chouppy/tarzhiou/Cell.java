package net.chouppy.tarzhiou;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import net.chouppy.tarzhiou.listeners.CellListener;

/**
 * Represents a cell which is a piece container
 * 
 * @author Jonathan ILIAS-PILLET
 */
public abstract class Cell
{
  /**
   * Set of neighbor cells
   */
  private Set<Cell> myNeighbors;

  /**
   * Set of pieces contained by this cell
   */
  private Set<Piece> myPieces;

  /**
   * Unique key which identifies the cell
   */
  private CellKey myKey;

  /**
   * list of listeners
   */
  private LinkedList<CellListener> myListeners;

  protected Cell(CellKey thisKey)
  {
    myNeighbors = new LinkedHashSet<Cell>();
    myPieces = new LinkedHashSet<Piece>();
    try
    {
      myKey = (CellKey) thisKey.clone();
    }
    catch (CloneNotSupportedException e)
    {
      assert false : "internal error : cannot clone a cloneable ?";
    }
    myListeners = new LinkedList<CellListener>();
  }

  /**
   * Adds a piece (player plays)
   * 
   * @param thisPiece
   *          the piece to be added
   */
  protected void addPiece(Piece thisPiece)
  {
    // The piece must be owned by a player
    assert (thisPiece.getOwner() != null);

    // The cell pieces must all be owned
    // by the same player
    if (!myPieces.isEmpty())
      assert (myPieces.iterator().next().getOwner().equals(thisPiece
            .getOwner()));

    // finally adds the piece to the cell
    myPieces.add(thisPiece);

    // calls listener if exists
    for (CellListener listener : myListeners)
    {
      listener.onAddPiece(this, thisPiece);
    }
  }

  /**
   * Bursts the overloaded cell.
   */
  protected void burst()
  {
    // The cell must be overloaded (otherwise, we have not
    // enough pieces to dispatch in neighbor cells
    assert (isOverloaded());

    Piece movedPiece;
    Iterator<Piece> i = myPieces.iterator();
    for (Cell currentNeighbor : myNeighbors)
    {
      movedPiece = i.next();
      i.remove();
      assert (currentNeighbor instanceof PlayableCell);
      ((PlayableCell) currentNeighbor).receivePiece(movedPiece);
    }

    // call listener if any
    for (CellListener listener : myListeners)
    {
      listener.onBurst(this);
    }
  }

  /**
   * Receives a piece that comes from a burst.
   * 
   * Note that the pieces' owners are automatically updated
   * 
   * @param this_piece
   *          the piece that comes
   */
  protected void receivePiece(Piece this_piece)
  {
    Player oldOwner = null;
    Player newOwner = this_piece.getOwner();
    
    if (!myPieces.isEmpty())
      oldOwner = myPieces.iterator().next().getOwner();
    
    // change all pieces owner
    for (Piece current_piece : myPieces)
    {
      current_piece.changeOwner((Player) this_piece.getOwner());
    }
    // finally adds the piece to the cell
    myPieces.add(this_piece);
    
    // calls listener if any
    for (CellListener listener : myListeners)
    {
      listener.onGetPiece(this, oldOwner, newOwner);
    }
  }

  /**
   * Adds another neighbor cell
   * 
   * @param otherCell
   *          the neighbor
   */
  protected void addNeighbor(Cell otherCell)
  {
    myNeighbors.add(otherCell);
  }

  /**
   * Removes a neighbor
   * 
   * @param thisCell
   *          the neighbor
   */
  protected void removeNeighbor(Cell thisCell)
  {
    myNeighbors.remove(thisCell);
  }

  /**
   * Gives the number of pieces the cell contains
   * 
   * @return number of pieces
   */
  public int getPiecesCount()
  {
    return myPieces.size();
  }

  /**
   * Gives the maximum number of piece the cell can contain before it should
   * burst
   * 
   * @return maximum number of pieces
   */
  public int getCapacity()
  {
    return myNeighbors.size();
  }

  /**
   * Gives a copy of the CellKey
   * @return
   */
  public CellKey getKey()
  {
    CellKey result = null;
    
    try
    {
      result = (CellKey) myKey.clone();  
    }
    catch (CloneNotSupportedException e)
    {
      assert false : "internal error : cannot clone a cloneable ?";
    }

    return result;
  }

  /**
   * Tells if this cell is about to burst
   * 
   * @return true if the cell is overloaded (about to explode)
   */
  public boolean isOverloaded()
  {
    if (myPieces.size() >= myNeighbors.size())
      return true;
    else
      return false;
  }

  /**
   * Gives the owner of all pieces in the cell
   * 
   * @return
   */
  public Player getPiecesOwner()
  {
    if (myPieces.size() > 0)
      return (Player) myPieces.iterator().next().getOwner();
    else
      return null;
  }

  /**
   * Adds another listener to the cell
   * 
   * @param thisListener the listener
   */
  public void addListener(CellListener thisListener)
  {
    myListeners.add(thisListener);
  }

  /**
   * Gives the set of neighbors
   * 
   * @note this set is a copy and can be freely modified
   * 
   * @return a set of neighbors
   */
  public Set<Cell> getNeighbors()
  {
    return new LinkedHashSet<Cell>(myNeighbors);
  }

  /**
   * Return the hashCode of the CellKey
   */
  @Override
  public int hashCode()
  {
    return myKey.hashCode();
  }

  @Override
  public boolean equals (Object o)
  {
    boolean result = false;

    if (o != null)
    {
      if (o instanceof Cell)
      {
        result = ((Cell) o).myKey.equals(myKey);
      }
    }

    return result;
  }
}
