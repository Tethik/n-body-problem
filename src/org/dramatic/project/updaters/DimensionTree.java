package org.dramatic.project.updaters;

import org.dramatic.project.Entity;
import org.dramatic.project.Vector;

public class DimensionTree {
	
	protected TreeNode root;
	protected final double granularity;
	public final static double GRAVITY_CONSTANT = 6.67384 * Math.pow(10, -11);
	
	public DimensionTree(Vector slices, double width, double granularity)
	{		
		this.root = new TreeNode(slices, width, null);
		this.granularity = granularity;
	}
	
	public int getDimensions()
	{
		return root.getSlices().getDimensions();		
	}
	
	protected class TreeNode {
		protected final TreeNode[] nodes;		 		
		protected Vector slices;
		protected Entity val;
		protected int children = 0;
		protected double width;
		protected double mass;
		protected Vector center_of_mass;
		
		public TreeNode(Vector slices, double width, Entity val)
		{			
			this.slices = slices;			
			if(slices.getDimensions() > 31 || slices.getDimensions() < 1)
				throw new IllegalArgumentException("Dimensions of given slices-vector must be between 31 and 1");
			
			int number_of_nodes = 1 << slices.getDimensions();
			this.nodes = new TreeNode[number_of_nodes ];
			this.val = val;
			this.width = width;
			if(val != null) {
				this.mass += val.getMass();
				this.center_of_mass = val.getPosition();
			} else {
				this.mass = 0;
				this.center_of_mass = slices;
			}
		}
		
		public Vector getSlices()
		{
			return slices;
		}
		

		
		public void add(Entity item)
		{				
			if(this.mass == 0)
			{
				this.mass = item.getMass();
				this.center_of_mass = item.getPosition();
			} else {
				Vector rikt = this.center_of_mass.subtract(item.getPosition());
				Vector diff = rikt.divideByScalar(this.mass+item.getMass()).multiplyByScalar(item.getMass());
				this.center_of_mass = this.center_of_mass.add(diff);
				this.mass += item.getMass();
			}
			children++;
			
			int pos = 0;
			for(int i = 0; i < slices.getDimensions(); i++)
				pos += (slices.getVar(i) > item.getPosition().getVar(i)) ? 1 << i : 0;
			
			TreeNode node = nodes[pos];
			if(node != null)
			{				
				node.add(item);	
				return;
			}
			
			Vector newSlices = new Vector(slices.getDimensions());
			
			for(int i = 0; i < slices.getDimensions(); i++)
				newSlices.setVar(i, slices.getVar(i) + width / (((pos & (1 << i)) < 1) ? 2 : -2));			
			
			nodes[pos] = new TreeNode(newSlices,width / 2, item);		
			
			if(val != null && children == 1) // lazy breakpoint from endless recursion -.^
			{				
				add(val);
				val = null;			
			}
		}	
		
		
		public int getChildrenCount()
		{
			return children;
		}
		
		public Entity getVal()
		{
			return val;
		}
		
		public Vector getForce(Entity e)
		{			
			if(val != null) {
				return getGravitationalForce(e, val);
			} else if(width / e.getPosition().subtract(center_of_mass).length() < granularity) {
				Entity e2 = new Entity();
				e2.setPosition(center_of_mass);
				e2.setMass(mass);
				return getGravitationalForce(e, e2);
			} else {
				Vector force = new Vector(slices.getDimensions());
				for(int i = 0; i < 1 << slices.getDimensions(); i++)
					if(nodes[i] != null)
						force = force.add(nodes[i].getForce(e));
				return force;
			}
		}
		
		/**
		 * Calculates the force that entity e2 exerts on e1.
		 * 
		 * @param e1
		 * @param e2
		 * @return
		 */
		private Vector getGravitationalForce(Entity e1, Entity e2)
		{
		// F = G * Mm / r�
			Vector pos1 = e1.getPosition();
			Vector pos2 = e2.getPosition();
			Vector R = new Vector(pos1.getDimensions());
			double len = Math.pow(pos2.subtract(pos1).length(),2);
			if(len < 1000)
				return R;
			
			Vector posR = pos2.subtract(pos1).normalize();
			double f = GRAVITY_CONSTANT*e1.getMass()*e2.getMass();
			f /= len;
			
			return posR.multiplyByScalar(f);
		}
		
		public double getWidth()
		{
			return width;
		}
	}
	
	private boolean isEntityInBounds(Entity item)
	{		
		Vector slices = root.getSlices();
		
		for(int i = 0; i < slices.getDimensions(); i++)			
			if(item.getPosition().getVar(i) > slices.getVar(i) + root.getWidth() || item.getPosition().getVar(i) < slices.getVar(i) - root.getWidth())
			{
				/*
				System.out.println("Entity out of bounds! " + item.toString());
				System.out.println(i);
				System.out.println(slices.getVar(i) + root.getWidth());
				System.out.println(slices.getVar(i) - root.getWidth());
				*/
				return false;
			}
		
		return true;
	}
	
	public boolean add(Entity item)
	{		
		if(!isEntityInBounds(item))
			return false;
		
		root.add(item);
		return true;
	}
	
	public Vector getForce(Entity e)
	{
		if(!isEntityInBounds(e))
			return new Vector(e.getPosition().getDimensions());
		
		return root.getForce(e);
	}
}
