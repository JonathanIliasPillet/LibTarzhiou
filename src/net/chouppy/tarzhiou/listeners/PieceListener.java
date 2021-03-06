package net.chouppy.tarzhiou.listeners;

import net.chouppy.tarzhiou.Player;
import net.chouppy.tarzhiou.Piece;

public interface PieceListener
{
  void onOwnerChanged(Piece me, Player previousOwner,
        Player newOwner);
}