package org.dramatic.project.updaters;

import org.dramatic.project.Entity;
import org.dramatic.project.Vector;

/***
 * Octree for BarnesHut based on:
 * http://arborjs.org/docs/barnes-hut
 * @author tethik
 * Very ugly :D
 */
public class Octree {
		
	protected OctreeNode root;
	protected final double granularity;
	public final static double GRAVITY_CONSTANT = 6.67384 * Math.pow(10, -11);
	
	public Octree(double x, double y, double z, double width, double granularity)
	{		
		this.root = new OctreeNode(x, y, z, width, null);
		this.granularity = granularity;
	}
	
	protected class OctreeNode {
		protected OctreeNode[][][] nodes = new OctreeNode[2][2][2]; 		
		
		protected Vector slices = new Vector(3);		
		protected Entity val;
		protected int children = 0;
		protected double width;
		protected double mass;
		protected Vector center_of_mass;
		
		public OctreeNode(double x, double y, double z, double width, Entity val)
		{			
			slices.setVar(0, x);
			slices.setVar(1, y);
			slices.setVar(2, z);			
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
		
		public void add(Entity item)
		{	
			if(this.mass == 0)
			{
				this.mass = item.getMass();
				this.center_of_mass = item.getPosition();
			} else {
				Vector rikt = this.center_of_mass.subtract(item.getPosition());
				Vector diff = rikt.divideByScalar(this.mass+item.getMass()).multiplyByScalar(item.getMass());
				// Enjoy :D
				this.center_of_mass = this.center_of_mass.add(diff);
				this.mass += item.getMass();
			}
			children++;
			
			int pos[] = new int[3];
			for(int i = 0; i < 3; i++)
				pos[i] = (slices.getVar(i) > item.getPosition().getVar(i)) ? 1 : 0;
			
			OctreeNode node = nodes[pos[0]][pos[1]][pos[2]];
			if(node != null)
			{				
				node.add(item);	
				return;
			}
			
			double newpos[] = new double[3];
			for(int i = 0; i < 3; i++)
				newpos[i] = slices.getVar(i) + width / ((pos[i] < 1) ? 2 : -2);
			
			nodes[pos[0]][pos[1]][pos[2]] = new OctreeNode(newpos[0],newpos[1],newpos[2],width / 2, item);		
			
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
				Vector force = new Vector(3);
				for(int x = 0; x < 2; x++)
					for(int y = 0; y < 2; y++)
						for(int z = 0; z < 2; z++)
							if(nodes[x][y][z] != null)
								force = force.add(nodes[x][y][z].getForce(e));
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
	}
	
	public void add(Entity item)
	{		
		root.add(item);
	}
	
	private static void addNode(double[] pos, Octree tree)
	{
		Entity e = new Entity();
		Vector position = new Vector(3);
		position.setVars(pos);
		e.setPosition(position);				
		tree.add(e);		
	}
	
	public Vector getForce(Entity e)
	{
		return root.getForce(e);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Octree tree = new Octree(0, 0, 0, 10000, 0.5);
		
		addNode(new double[]{100,100,0}, tree);
		addNode(new double[]{-100,-100,0}, tree);
		addNode(new double[]{100,-100,0}, tree);
		addNode(new double[]{-100,-100,-100}, tree);
		
		// Split
		addNode(new double[]{9000,9000,0}, tree);
		addNode(new double[]{9001,9001,0}, tree);


	}

}
