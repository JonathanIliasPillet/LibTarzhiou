package net.chouppy.tarzhiou;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class SquareSpace {
	
	protected Set<Square> squares;
	
	public abstract void do_bursts (); 
	
	public SquareSpace () {
		squares = new LinkedHashSet<Square> ();
	}
	
	public Iterator<Square> get_iterator () {
		return squares.iterator();
	}
	
	/**
	 * Returns the square given a key.
	 *
	 * @param this_key the key that represents the square
	 * @return the required square or null if none found
	 */
	public Square get_square_from_key (SquareKey this_key) {
		Iterator<Square> i = squares.iterator();
		Square result = null;
		
		while ((i.hasNext()) && (result == null))
		{
			Square temp = i.next();
			if (temp.hashCode() == this_key.hashCode())
				result = temp;
		}
		
		return result;
	}
	
	/**
	 * Provides DOT output
	 */
	public String toString ()
	{
		StringBuffer result = new StringBuffer();
		
		result.append("graph\"Tarzhiou Square Space\" {\n");
		for (Square current_square : squares) 
		{
			result.append("square_");
			result.append(current_square.hashCode());
			result.append(" [label = \"");
			result.append(current_square.get_pieces_count());
			result.append("\"];\n");
			for (Square neighbor : current_square.get_neighbors())
			{
				result.append("square_");
				result.append(current_square.hashCode());
				result.append(" -- square_");
				result.append(neighbor.hashCode());
				result.append(";\n");
			}
		}
		result.append("}\n");
		
		return result.toString();
	}
	
	protected void add_square (Square this_square) {
		squares.add(this_square);
	}
	
	protected void link_squares (LinkeableSquare a, LinkeableSquare b) {
		assert (squares.contains(a));
		assert (squares.contains(b));
		
		a.link_to(b);
	}
	
	protected void validate () {
		boolean is_valid = true;
		Iterator<Square> i = squares.iterator();
		Square current_square;
	
		while (i.hasNext() && is_valid) {
			current_square = i.next ();
			if (current_square.get_capacity() < 2)
				is_valid = false;
		}
	}
}
