package net.chouppy.tarzhiou;

import java.util.Set;

import net.chouppy.tarzhiou.listeners.CellListener;
import net.chouppy.tarzhiou.listeners.LinkeableCellListener;

import junit.framework.TestCase;

public class CellTests extends TestCase implements CellListener, LinkeableCellListener
{

	/* mainly tested cell 
	 */
	private LinkeableCell tested_cell;
	
	/*
	 * neighbors of tested cell
	 */
	private LinkeableCell n1;
	private LinkeableCell n2;
	private LinkeableCell n3;
	
	/*
	 * variables used to get back informations from listener callbacks
	 */
	private Cell listener_me;
	private Piece listener_piece;
	private boolean listener_add_trace;
	private boolean listener_burst_trace;
	private boolean listener_link_trace;

	private LinkeableCell listener_other;
	
	private static final String CELL_KEY_NAME = "a";
	
	protected void setUp() throws Exception {
		super.setUp();
	
		tested_cell = new LinkeableCell(new NameCellKey(CELL_KEY_NAME));
		
		n1 = new LinkeableCell(new NameCellKey("b"));
		n2 = new LinkeableCell(new NameCellKey("c"));
		n3 = new LinkeableCell(new NameCellKey("d"));
		
		tested_cell.linkTo(n1);
		tested_cell.linkTo(n2);
		tested_cell.linkTo(n3);
	}
	
	public void test_get_neighbors ()
	{
		Set<Cell> n = tested_cell.getNeighbors();
		
		assertEquals(3, n.size());
		assertTrue (n.contains(n1));
		assertTrue (n.contains(n2));
		assertTrue (n.contains(n3));
	}
	
	public void test_hashCode ()
	{
		assertEquals((new NameCellKey (CELL_KEY_NAME)).hashCode (), tested_cell.hashCode());
	}
	
	public void test_capacity ()
	{
		assertEquals(3, tested_cell.getCapacity());
		
		tested_cell.linkTo(new LinkeableCell(new NameCellKey("e")));
		assertEquals(4, tested_cell.getCapacity());
	}
	
	public void test_get_key ()
	{
		assertEquals (new NameCellKey(CELL_KEY_NAME), tested_cell.getKey());
		
		assertNotSame(new NameCellKey (""), tested_cell.getKey());
	}
	
	public void test_filling ()
	{
		Player p = new Player ("1");
		
		assertEquals(0, tested_cell.getPiecesCount());
		assertEquals(false, tested_cell.isOverloaded());
		
		tested_cell.addPiece(p.newPiece());
		assertEquals(1, tested_cell.getPiecesCount());
		assertEquals(false, tested_cell.isOverloaded());
		
		tested_cell.addPiece(p.newPiece());
		tested_cell.addPiece(p.newPiece());
		assertEquals(3, tested_cell.getPiecesCount());
		assertEquals(true, tested_cell.isOverloaded());
		
		tested_cell.addPiece(p.newPiece());
		tested_cell.addPiece(p.newPiece());
		assertEquals(5, tested_cell.getPiecesCount());
		assertEquals(true, tested_cell.isOverloaded());
	}
	
	public void test_burst ()
	{
		Player p = new Player ("1");
		
		tested_cell.addPiece(p.newPiece());
		tested_cell.addPiece(p.newPiece());
		tested_cell.addPiece(p.newPiece());
		tested_cell.burst();
		
		assertEquals(0, tested_cell.getPiecesCount());
		assertEquals(false, tested_cell.isOverloaded());
		assertEquals(null, tested_cell.getPiecesOwner());
		
		for (Cell neighbor : tested_cell.getNeighbors())
		{
			assertEquals(1, neighbor.getPiecesCount());
			assertEquals(false, tested_cell.isOverloaded());
			assertSame(p, neighbor.getPiecesOwner());
		}	
	}

	public void test_listener ()
	{
		Player p1 = new Player ("p1");
		Piece added_piece = p1.newPiece();
		
		tested_cell.addListener(this);
		
		listener_add_trace = false;
		listener_burst_trace = false;
		tested_cell.addPiece(added_piece);
		assertTrue (listener_add_trace);
		assertFalse (listener_burst_trace);
		assertSame (tested_cell, listener_me);
		assertSame(added_piece, listener_piece);
		
		listener_add_trace = false;
		tested_cell.addPiece(p1.newPiece());
		tested_cell.addPiece(p1.newPiece());
		tested_cell.addPiece(p1.newPiece());
		assertTrue (listener_add_trace);
		assertFalse (listener_burst_trace);
		
		tested_cell.burst();
		assertTrue (listener_burst_trace);
		assertSame (tested_cell, listener_me);
	}
	
	public void test_link_listener ()
	{
		LinkeableCell other = new LinkeableCell(new NameCellKey("o"));
		
		tested_cell.setLinkeableListener (this);
		
		listener_link_trace = false;
		tested_cell.linkTo(other);
		assertTrue (listener_link_trace);
		assertSame (tested_cell, listener_me);
		assertSame (other, listener_other);
		assertSame (tested_cell, other.getNeighbors().iterator().next());
	}

	public void onAddPiece(Cell me, Piece thisPiece) {
		listener_add_trace = true;
		listener_me = me;
		listener_piece = thisPiece;		
	}

	public void onBurst(Cell me) {
		listener_me = me;
		listener_burst_trace = true;
		
	}

	public void onLink(LinkeableCell me, LinkeableCell other) {
		listener_link_trace = true;
		listener_me = me;
		listener_other = other;
	}
}
