package org.dramatic.project;


public class Entity 
{
	private static int idCounter = 0;
	private int id;
	private Vector position;
	private Vector velocity;
	private Vector acceleration;
	
	private double radius;
	private Vector[] forceList = null;
	
	private double mass;

	public Entity()
	{
		mass = 1;
		radius = 1;
	}
	
	public Entity(double mass, Vector position, Vector velocity)
	{
		id = idCounter++;
		this.mass = mass;
		this.position = position;
		this.velocity = velocity;
		this.acceleration = new Vector(this.velocity.getDimensions());
		//forceList = new Vector(this.velocity.getDimensions());
		//forceList.clear();
	}
	
	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}

	public Vector getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector acceleration) {
		this.acceleration = acceleration;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}
	
	/**
	 * Initializes a new forcelist with input size.
	 * This should be called between every update.
	 * @param entityCount
	 */
	public void initForceList(int entityCount)
	{
		forceList = new Vector[entityCount];
	}
	
	/**
	 * Adds a force to the forcelist.
	 * 
	 * Throws IllegalAcessException if forcelist is null
	 * 
	 * @param force
	 * @return
	 * @throws IllegalAccessException 
	 */
	public void setForce(int taskid, Vector force) 
	{
		//if(forceList == null)
//			throw new IllegalAccessException("Entity-addForce: forceList must be initialized for every timestep. It is: " + forceList);
		forceList[taskid] = force;
	}
	
	/**
	 * Sums all the forces in forcelist and returns them.
	 * Collection forcelist is cleared in this method invocation.
	 * @return
	 */
	private Vector getForceVector()
	{
		Vector force = new Vector(acceleration.getDimensions());
		for(int i = 0; i < forceList.length; i++)
		{
			Vector v = forceList[i];
			if(v != null)
			{
				force = force.add(v);
			}
			
		}
		return force;
	}
	
	/**
	 * This is a critical part and shouldn't be run parallel on the same entity.
	 */
	public void Update()
	{
	// F = ma
		Vector forceVector = getForceVector();
		acceleration = forceVector.divideByScalar(mass);
		
	// a = (v-u)/t, where t = 1
		// Thus: v = a+u
		velocity = velocity.add(acceleration);
		
	// newS = v/t, where t = 1 
		// thus total distance = newS + s = v + s 
		position = position.add(velocity);
			
		
		
		//System.out.println(this);
		//System.out.println("\t\tforce: " + force);
			
		//force.clear();
	}	
	
	public int getId()
	{
		return id;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Entity-").append(id).append(": ");
		sb.append("M=").append(mass).append(" ");
		sb.append("p=").append(position).append(" ");
		sb.append("v=").append(velocity).append(" ");
		sb.append("a=").append(acceleration).append(" ");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object o)
	{
		Entity e = (Entity) o;
		return this.id == e.id;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
	
}
