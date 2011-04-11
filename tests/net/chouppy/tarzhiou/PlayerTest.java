package net.chouppy.tarzhiou;

import net.chouppy.tarzhiou.listeners.PlayerListener;
import junit.framework.TestCase;

public class PlayerTest extends TestCase implements PlayerListener {

	private static final String PLAYER_NAME = "tested_player";
	
	/*
	 * tested object
	 */
	private Player tested_player;
	
	/*
	 * listener flags back
	 */
	private boolean listener_new_trace;
	private boolean listener_win_trace;
	private boolean listener_loose_trace;
	private Player listener_me;
	private ReadOnlyPiece listener_this_piece;
	
	protected void setUp() throws Exception {
		tested_player = new Player (PlayerTest.PLAYER_NAME);
	}
	
	public void test_name ()
	{
		assertSame (PlayerTest.PLAYER_NAME, tested_player.get_name());
	}
	
	public void test_new_piece ()
	{
		assertEquals(0, tested_player.get_pieces_count());
		
		Piece p = tested_player.newPiece();
		
		assertSame (p.getOwner(), tested_player);
		assertEquals(1, tested_player.get_pieces_count());
		
		tested_player.newPiece();
		tested_player.newPiece();
		assertEquals(3, tested_player.get_pieces_count());
	}
	
	public void test_alive ()
	{
		assertTrue (tested_player.is_alive());
		
		Piece new_piece = tested_player.newPiece();
		assertTrue (tested_player.is_alive());
		
		Piece piece_won = new Piece (new Player ("other"));
		tested_player.winAPiece(piece_won);
		assertTrue(tested_player.is_alive());
		
		tested_player.looseAPiece(new_piece);
		assertTrue(tested_player.is_alive());
		tested_player.looseAPiece(piece_won);
		assertFalse (tested_player.is_alive());
	}
	
	public void test_retract ()
	{
		assertTrue (tested_player.is_alive());
		
		tested_player.newPiece();
		
		tested_player.retract();
		
		assertFalse(tested_player.is_alive());
		assertEquals(1, tested_player.get_pieces_count());
	}
	
	public void test_equals ()
	{
		Player tintin = tested_player;
		assertTrue (tested_player.equals(tintin));
		
		Player other = new Player ("I'm not you");
		assertFalse(other.equals(tested_player));
	}
	
	public void test_listener ()
	{
		tested_player.addListener(this);
		
		listener_win_trace = false;
		listener_loose_trace = false;
		listener_new_trace = false;
		Piece piece_won = new Piece (tested_player);
		tested_player.winAPiece(piece_won);
		assertTrue (listener_win_trace);
		assertFalse (listener_new_trace);
		assertFalse (listener_loose_trace);
		assertSame (tested_player, listener_me);
		assertSame (piece_won, listener_this_piece);
		
		listener_win_trace = false;
		listener_loose_trace = false;
		listener_new_trace = false;
		Piece new_piece = tested_player.newPiece();
		assertTrue (listener_new_trace);
		assertFalse(listener_win_trace);
		assertFalse(listener_loose_trace);
		assertSame (tested_player, listener_me);
		assertSame (new_piece, listener_this_piece);
		
		listener_win_trace = false;
		listener_loose_trace = false;
		listener_new_trace = false;
		tested_player.looseAPiece(new_piece);
		assertTrue (listener_loose_trace);
		assertFalse(listener_win_trace);
		assertFalse(listener_new_trace);
		assertSame (tested_player, listener_me);
		assertSame (new_piece, listener_this_piece);
	}

	@Override
	public void onLooseAPiece(Player me, ReadOnlyPiece thisPiece) {
		listener_loose_trace = true;
		listener_me = me;
		listener_this_piece = thisPiece;		
	}

	@Override
	public void onNewPiece(Player me, ReadOnlyPiece thisPiece) {
		listener_new_trace = true;
		listener_me = me;
		listener_this_piece = thisPiece;	
	}

	@Override
	public void onWinAPiece(Player me, ReadOnlyPiece thisPiece) {
		listener_win_trace = true;
		listener_me = me;
		listener_this_piece = thisPiece;	
	}

}
