package synalp.generation.jeni.filtering.dlx;



@SuppressWarnings("javadoc")
public class TestSolver
{

	public static void main(String[] args)
	{
		DLXSolver dlx = new DLXSolver(test2());
		dlx.findSolution();
	}
	
	
	public static Cell test1()
	{
		Cell root = new Cell("Root", -1, null);
		
		Cell a = new Cell("A", 1, null);
		Cell b = new Cell("B", 1, null);
		Cell c = new Cell("C", 1, null);
		
		Cell a1 = new Cell("A1", -1, a);
		Cell b1 = new Cell("B1", -1, b);
		Cell c1 = new Cell("C1", -1, c);
		
		root.right = a;
		root.left = c;
		
		a.up = a1;
		a.down = a1;
		a.left = root;
		a.right = b;

		b.up = b1;
		b.down = b1;
		b.left = a;
		b.right = c;

		c.up = c1;
		c.down = c1;
		c.left = b;
		c.right = root;
		
		a1.up = a;
		a1.down = a;
		a1.left = a1;
		a1.right = a1;
		
		b1.up = b;
		b1.down = b;
		b1.left = b1;
		b1.right = b1;
		
		c1.up = c;
		c1.down = c;
		c1.left = c1;
		c1.right = c1;
		
		return root;
	}
	
	public static Cell test2()
	{
		Cell root = new Cell("Root", -1, null);
		
		Cell a = new Cell("A", 2, null);
		Cell b = new Cell("B", 1, null);
		Cell c = new Cell("C", 2, null);
		
		Cell a1 = new Cell("A1", -1, a);
		Cell a2 = new Cell("A2", -1, a);
		Cell b1 = new Cell("B1", -1, b);
		Cell c1 = new Cell("C1", -1, c);
		Cell c2 = new Cell("C2", -1, c);
		
		root.right = a;
		root.left = c;
		
		a.up = a2;
		a.down = a1;
		a.left = root;
		a.right = b;

		b.up = b1;
		b.down = b1;
		b.left = a;
		b.right = c;

		c.up = c2;
		c.down = c1;
		c.left = b;
		c.right = root;
		
		a1.up = a;
		a1.down = a2;
		a1.left = c1;
		a1.right = c1;
		
		a2.up = a1;
		a2.down = a;
		a2.left = c2;
		a2.right = c2;
		
		b1.up = b;
		b1.down = b;
		b1.left = b1;
		b1.right = b1;
		
		c1.up = c;
		c1.down = c2;
		c1.left = a1;
		c1.right = a1;
		
		c2.up = c1;
		c2.down = c;
		c2.left = a2;
		c2.right = a2;
		
		return root;
	}


	public static Cell test3()
	{
		Cell root = new Cell("Root", -1, null);

		Cell a = new Cell("A", 2, null);
		Cell b = new Cell("B", 2, null);
		Cell c = new Cell("C", 2, null);
		Cell d = new Cell("D", 3, null);
		Cell e = new Cell("E", 2, null);
		Cell f = new Cell("F", 2, null);
		Cell g = new Cell("G", 3, null);

		Cell a1 = new Cell("A1", -1, a);
		Cell a2 = new Cell("A2", -1, a);
		Cell b1 = new Cell("B1", -1, b);
		Cell b2 = new Cell("B2", -1, b);
		Cell c1 = new Cell("C1", -1, c);
		Cell c2 = new Cell("C2", -1, c);
		Cell d1 = new Cell("D1", -1, d);
		Cell d2 = new Cell("D2", -1, d);
		Cell d3 = new Cell("D3", -1, d);
		Cell e1 = new Cell("E1", -1, e);
		Cell e2 = new Cell("E2", -1, e);
		Cell f1 = new Cell("F1", -1, f);
		Cell f2 = new Cell("F2", -1, f);
		Cell g1 = new Cell("G1", -1, g);
		Cell g2 = new Cell("G2", -1, g);
		Cell g3 = new Cell("G3", -1, g);

		root.right = a;
		root.left = g;

		a.up = a2;
		a.down = a1;
		a.left = root;
		a.right = b;

		b.up = b2;
		b.down = b1;
		b.left = a;
		b.right = c;

		c.up = c2;
		c.down = c1;
		c.left = b;
		c.right = d;

		d.up = d3;
		d.down = d1;
		d.left = c;
		d.right = e;

		e.up = e2;
		e.down = e1;
		e.left = d;
		e.right = f;

		f.up = f2;
		f.down = f1;
		f.left = e;
		f.right = g;

		g.up = g3;
		g.down = g1;
		g.left = f;
		g.right = root;

		a1.up = a;
		a1.down = a2;
		a1.left = g1;
		a1.right = d1;

		a2.up = a1;
		a2.down = a;
		a2.left = d2;
		a2.right = d2;

		b1.up = b;
		b1.down = b2;
		b1.left = f2;
		b1.right = c2;

		b2.up = b1;
		b2.down = b;
		b2.left = g2;
		b2.right = g2;

		c1.up = c;
		c1.down = c2;
		c1.left = f1;
		c1.right = e1;

		c2.up = c1;
		c2.down = c;
		c2.left = b1;
		c2.right = f2;

		d1.up = d;
		d1.down = d2;
		d1.left = a1;
		d1.right = g1;

		d2.up = d1;
		d2.down = d3;
		d2.left = a2;
		d2.right = a2;

		d3.up = d2;
		d3.down = d;
		d3.left = g3;
		d3.right = e2;

		e1.up = e;
		e1.down = e2;
		e1.left = c1;
		e1.right = f1;

		e2.up = e1;
		e2.down = e;
		e2.left = d3;
		e2.right = g3;

		f1.up = f;
		f1.down = f2;
		f1.left = e1;
		f1.right = c1;

		f2.up = f1;
		f2.down = f;
		f2.left = c2;
		f2.right = b1;

		g1.up = g;
		g1.down = g2;
		g1.left = d1;
		g1.right = a1;

		g2.up = g1;
		g2.down = g3;
		g2.left = b2;
		g2.right = b2;

		g3.up = g2;
		g3.down = g;
		g3.left = e2;
		g3.right = d3;

		return root;
	}
}
