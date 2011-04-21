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
	private Piece listener_this_piece;
	
	protected void setUp() throws Exception {
		tested_player = new Player (PlayerTest.PLAYER_NAME);
	}
	
	public void test_name ()
	{
		assertEquals (PlayerTest.PLAYER_NAME, tested_player.getCanonicalName());
	}
	
	public void test_new_piece ()
	{
		assertEquals(0, tested_player.getPiecesCount());
		
		Piece p = tested_player.newPiece();
		
		assertSame (p.getOwner(), tested_player);
		assertEquals(1, tested_player.getPiecesCount());
		
		tested_player.newPiece();
		tested_player.newPiece();
		assertEquals(3, tested_player.getPiecesCount());
	}
	
	public void test_alive ()
	{
		assertTrue (tested_player.isAlive());
		
		Piece new_piece = tested_player.newPiece();
		assertTrue (tested_player.isAlive());
		
		Piece piece_won = new Piece (new Player ("other"));
		tested_player.winAPiece(piece_won);
		assertTrue(tested_player.isAlive());
		
		tested_player.looseAPiece(new_piece);
		assertTrue(tested_player.isAlive());
		tested_player.looseAPiece(piece_won);
		assertFalse (tested_player.isAlive());
	}
	
	public void test_retract ()
	{
		assertTrue (tested_player.isAlive());
		
		tested_player.newPiece();
		
		tested_player.retract();
		
		assertFalse(tested_player.isAlive());
		assertEquals(1, tested_player.getPiecesCount());
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

	public void onLooseAPiece(Player me, Piece thisPiece) {
		listener_loose_trace = true;
		listener_me = me;
		listener_this_piece = thisPiece;		
	}

	public void onNewPiece(Player me, Piece thisPiece) {
		listener_new_trace = true;
		listener_me = me;
		listener_this_piece = thisPiece;	
	}

	public void onWinAPiece(Player me, Piece thisPiece) {
		listener_win_trace = true;
		listener_me = me;
		listener_this_piece = thisPiece;	
	}

}
