package org.dramatic.project.visualiser;

import java.util.ArrayList;

import org.dramatic.project.Entity;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import static org.lwjgl.opengl.GL11.*;

public class Main {
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private static int viewportWidth = 800;
	private static int viewportHeight = 600;
	
	/**
	 * Rudimentary OpenGL setup with LWJGL. Use perspective
	 * projection with a field of view of 45 degrees. 
	 */
	public void setup() 
	{
		// set up LWJGL display
		try
		{
			Display.setDisplayMode(new DisplayMode(viewportWidth, viewportHeight));
			Display.create();
		}
		catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
 
		// init viewport and projection matrix
		glViewport(0, 0, viewportWidth, viewportHeight);
		glMatrixMode(GL11.GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective( 45.0f, (float)viewportWidth/(float)viewportHeight, 1.0f, 200.0f ); 
		glMatrixMode(GL11.GL_MODELVIEW);
 
		glEnable(GL_DEPTH_TEST);
		glShadeModel (GL_SMOOTH);
		glClearColor (0f, 0f, 0f, 0f);
	}
 

	public void start()
	{
		setup();
		
		
		while(!Display.isCloseRequested()) {
			for(Entity e :entities)
				draw(e);
			Display.sync(70);
			Display.update();
		}
		
		Display.destroy();
		
	}
	
	public void draw(Entity e)
	{
		
		glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
		glLoadIdentity(); 
		
		glTranslatef(0.0f, 0.0f, 0.0f);
		
		glColor3f(0.1f, 0.4f, 0.9f);
		Sphere s = new Sphere();
		s.draw(1.0f, 20, 16);
	}
	
	public void addEntity(Entity e)
	{
		entities.add(e);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Main main = new Main();
		Entity e = new Entity();
		main.addEntity(e);
		main.start();

	}

}
