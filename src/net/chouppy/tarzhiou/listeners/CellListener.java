package net.chouppy.tarzhiou.listeners;

import net.chouppy.tarzhiou.Piece;
import net.chouppy.tarzhiou.Cell;
import net.chouppy.tarzhiou.Player;

/**
 * 
 * @author Jonathan ILIAS-PILLET
 */
public interface CellListener
{
  void onAddPiece(Cell me, Piece thisPiece);

  void onBurst(Cell me);
  
  void onGetPiece (Cell me, Player oldPlayer, Player newPlayer);
}
