package net.chouppy.tarzhiou;

import net.chouppy.tarzhiou.listeners.CellListener;
import junit.framework.TestCase;

public class DisorderedCellSpaceTests extends TestCase implements CellListener
{

  private DisorderedCellSpace tested_cellspace;
  Player p1;
  Player p2;
  
  boolean listener_trace_add_piece;
  boolean listener_trace_burst;
  
  boolean test_stop_all_bursts;
  
  protected void setUp() throws Exception
  {
    super.setUp();
    
    BuildableCellSpace bcs = new BuildableCellSpace();
    tested_cellspace = bcs;
    
    LinkeableCell c1 = new LinkeableCell(new NameCellKey("c1"));
    LinkeableCell c2 = new LinkeableCell(new NameCellKey("c2"));
    LinkeableCell c3 = new LinkeableCell(new NameCellKey("c3"));
    LinkeableCell c4 = new LinkeableCell(new NameCellKey("c4"));
    LinkeableCell c5 = new LinkeableCell(new NameCellKey("c5"));
    
    bcs.addCell(c1);
    bcs.addCell(c2);
    bcs.addCell(c3);
    bcs.addCell(c4);
    bcs.addCell(c5);
    
    bcs.linkCells(c1, c2);
    bcs.linkCells(c2, c3);
    bcs.linkCells(c3, c4);
    bcs.linkCells(c4, c1);
    bcs.linkCells(c5, c1);
    bcs.linkCells(c5, c2);
    bcs.linkCells(c5, c3);
    
    bcs.validate();
    
    p1 = new Player ("p1");
    p2 = new Player ("p2");
    
    // initialize flags for listener behavior
    test_stop_all_bursts = false;
  }
  
  public void test_simple_burst_do_all ()
  {
    PlayableCell cell = tested_cellspace.getPlayableCellFromKey(new NameCellKey("c4"));
    
    for (int i = 0 ; i < cell.getCapacity() ; i++)
      cell.addPiece(p1.newPiece());
    
    tested_cellspace.doAllBursts();
    
    assertEquals (1, tested_cellspace.getCellFromKey(new NameCellKey("c1")).getPiecesCount());
    assertEquals (0, tested_cellspace.getCellFromKey(new NameCellKey("c2")).getPiecesCount());
    assertEquals (1, tested_cellspace.getCellFromKey(new NameCellKey("c3")).getPiecesCount());
    assertEquals (0, tested_cellspace.getCellFromKey(new NameCellKey("c4")).getPiecesCount());
    assertEquals (0, tested_cellspace.getCellFromKey(new NameCellKey("c5")).getPiecesCount());
  }
  
  public void test_simple_burst_do_increment ()
  {
    PlayableCell cell = tested_cellspace.getPlayableCellFromKey(new NameCellKey("c4"));
    
    for (int i = 0 ; i < cell.getCapacity() ; i++)
      cell.addPiece(p1.newPiece());
    
    boolean result = tested_cellspace.doOneBurstIncrement();
    assertTrue (result);
    result = tested_cellspace.doOneBurstIncrement();
    assertFalse(result);
    result = tested_cellspace.doOneBurstIncrement();
    assertFalse(result);
    
    assertEquals (1, tested_cellspace.getCellFromKey(new NameCellKey("c1")).getPiecesCount());
    assertEquals (0, tested_cellspace.getCellFromKey(new NameCellKey("c2")).getPiecesCount());
    assertEquals (1, tested_cellspace.getCellFromKey(new NameCellKey("c3")).getPiecesCount());
    assertEquals (0, tested_cellspace.getCellFromKey(new NameCellKey("c4")).getPiecesCount());
    assertEquals (0, tested_cellspace.getCellFromKey(new NameCellKey("c5")).getPiecesCount());
  }
  
  public void test_player_eats_another ()
  {
    PlayableCell cell_p1 = tested_cellspace.getPlayableCellFromKey(new NameCellKey("c1"));
    PlayableCell cell_p2 = tested_cellspace.getPlayableCellFromKey(new NameCellKey("c2"));
    
    for (int i = 0 ; i < cell_p1.getCapacity() ; i++)
      cell_p1.addPiece(p1.newPiece());
    cell_p2.addPiece(p2.newPiece());
    
    tested_cellspace.doAllBursts();
    
    assertEquals(0, p2.getPiecesCount());
    assertEquals(cell_p1.getCapacity()+1, p1.getPiecesCount());
  }
  
  public void test_long_burst_chain ()
  {
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c1")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c1")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c1")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c2")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c2")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c5")).addPiece(p1.newPiece());
    
    tested_cellspace.doAllBursts();
    
    assertEquals(2, tested_cellspace.getPlayableCellFromKey(new NameCellKey("c1")).getPiecesCount());
    assertEquals(1, tested_cellspace.getPlayableCellFromKey(new NameCellKey("c2")).getPiecesCount());
    assertEquals(2, tested_cellspace.getPlayableCellFromKey(new NameCellKey("c3")).getPiecesCount());
    assertEquals(1, tested_cellspace.getPlayableCellFromKey(new NameCellKey("c4")).getPiecesCount());
    assertEquals(0, tested_cellspace.getPlayableCellFromKey(new NameCellKey("c5")).getPiecesCount());
  }
  
  public void test_infinite_burst_chain_incremental ()
  {
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c1")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c1")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c1")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c2")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c2")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c3")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c3")).addPiece(p1.newPiece());
    
    boolean result = true;
    for (int i = 0 ; i < 100 ; i++)
      result = tested_cellspace.doOneBurstIncrement();
    assertTrue(result);
  }
  
  public void test_infinite_burst_chain_stoppable ()
  {
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c1")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c1")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c1")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c2")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c2")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c3")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c3")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c3")).addPiece(p1.newPiece());

    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c5")).addListener(this);

    test_stop_all_bursts = true;
    
    tested_cellspace.doAllBursts();

    assertTrue (true);
  }
  
  public void test_listeners ()
  {
    listener_trace_add_piece = false;
    listener_trace_burst = false;
    
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c1")).addListener(this);
    
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c1")).addPiece(p1.newPiece());
    
    assertTrue (listener_trace_add_piece);
    assertFalse (listener_trace_burst);
    
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c1")).addPiece(p1.newPiece());
    tested_cellspace.getPlayableCellFromKey(new NameCellKey("c1")).addPiece(p1.newPiece());
        
    listener_trace_add_piece = false;
    listener_trace_burst = false;
    
    tested_cellspace.doAllBursts();
    
    assertFalse (listener_trace_add_piece);
    assertTrue (listener_trace_burst);
  }

  public void onAddPiece(Cell me, Piece thisPiece) 
  {
    listener_trace_add_piece = true;
  }

  public void onBurst(Cell me)
  {
    if (test_stop_all_bursts && me.getKey().equals(new NameCellKey("c5")))
      tested_cellspace.stopDoingAllBursts();
    
    listener_trace_burst = true;
  }
}
