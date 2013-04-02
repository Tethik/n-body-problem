package org.dramatic.project.visualiser;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.dramatic.project.EngineOutputListener;
import org.dramatic.project.Entity;
import org.dramatic.project.EntityFactory;
import org.dramatic.project.Vector;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Color;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

public class Visualiser2D implements EngineOutputListener {
	
	private static final int COORD_HEIGHT = 20000;
	private static final int COORD_WIDTH = 20000;
	private final DisplayMode DISPLAY_MODE;
	private final String WINDOW_TITLE = "2D visualiser";
	private final int FPS = 70;	//frames per second
	private float zTranslation = -12f;
	
	boolean fullscreen;
	
	//----------- Variables added for Lighting Test -----------//
	private FloatBuffer matSpecular;
	private FloatBuffer lightPosition;
	private FloatBuffer whiteLight; 
	private FloatBuffer lModelAmbient;
	//----------- END: Variables added for Lighting Test -----------//
	
	public Visualiser2D()
	{
		this(800, 600);
	}

	public Visualiser2D(int width, int height)
	{
		this(width, height, false);
	}
	
	public Visualiser2D(int width, int height, boolean fullscreen)
	{
		DISPLAY_MODE = new DisplayMode(width, height);
		
		this.fullscreen = fullscreen;	
		init();
	}
	
	private void init() {
		createWindow();
		initGL();
	}
	
	private void createWindow() {
		try {
			Display.setDisplayMode(DISPLAY_MODE);
			Display.setFullscreen(fullscreen);
			Display.setTitle(WINDOW_TITLE);
			Display.create();
		} catch (LWJGLException e){
			e.printStackTrace();
		}
	}
	
	private void initGL() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // sets background to grey
		glClearDepth(1.0f); // clear depth buffer
		glEnable(GL_DEPTH_TEST); // Enables depth testing
		glDepthFunc(GL_LEQUAL); // sets the type of test to use for depth testing
		glMatrixMode(GL_PROJECTION); // sets the matrix mode to project
		

		//GLU.gluOrtho2D(-10, 10, -10, 10);
		//GLU.gluOrtho2D(0 - COORD_WIDTH / 2, 0 + COORD_WIDTH / 2, 0 - COORD_HEIGHT / 2, 0 + COORD_HEIGHT / 2);
		float fovy = 90.0f;
		float aspect = DISPLAY_MODE.getWidth() / (float)DISPLAY_MODE.getHeight();
		float zNear = 0.1f;
		float zFar = 20000.0f;
		GLU.gluPerspective(fovy, aspect, zNear, zFar);
		
		glViewport(0, 0, DISPLAY_MODE.getWidth(), DISPLAY_MODE.getHeight());
		//GLU.gluOrtho2D(-10, 10, -10, 10);
		//GLU.gluOrtho2D(-1, 1, -1, 1);
		
		glOrtho(0 - COORD_WIDTH / 2, 0 + COORD_WIDTH / 2, 0 - COORD_HEIGHT / 2, 0 + COORD_HEIGHT / 2, zNear, zFar);
		
		System.out.println(COORD_WIDTH + ", " + COORD_HEIGHT);
		
		
		
		
		
		glMatrixMode(GL_MODELVIEW);
		
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 
		
		//----------- Variables & method calls added for Lighting Test -----------//
		initLightArrays();
		glShadeModel(GL_SMOOTH);
		glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);				// sets specular material color
		glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);					// sets shininess
		
		glLight(GL_LIGHT0, GL_POSITION, lightPosition);				// sets light position
		glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);				// sets specular light to white
		glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);					// sets diffuse light to white
		glLightModel(GL_LIGHT_MODEL_AMBIENT, lModelAmbient);		// global ambient light 
		
		glEnable(GL_LIGHTING);										// enables lighting
		glEnable(GL_LIGHT0);										// enables light0
		
		glEnable(GL_COLOR_MATERIAL);								// enables opengl to use glColor3f to define material color
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);			// tell opengl glColor3f effects the ambient and diffuse properties of material
		//----------- END: Variables & method calls added for Lighting Test -----------//
		
	}
	

	//------- Added for Lighting Test----------//
	private void initLightArrays() {
		matSpecular = BufferUtils.createFloatBuffer(4);
		matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		
		lightPosition = BufferUtils.createFloatBuffer(4);
		lightPosition.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
		
		whiteLight = BufferUtils.createFloatBuffer(4);
		whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		
		lModelAmbient = BufferUtils.createFloatBuffer(4);
		lModelAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
	}
	
	private HashMap<Integer, Color> colormap = new HashMap<Integer, Color>();
	
	public void waitForInput()
	{
		for(;;)
		{
			if(getInputCloseSignal())
				return;
			//Display.sync(FPS);
			Display.update();	//update the view/screen
		}
	}	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Visualiser2D vis = new Visualiser2D();
		
		EntityFactory.POS_MAGNITUDE_FACTOR = 10000;
		
		List<Entity> ens = EntityFactory.getEntities(100);
		
		for(int i = 0; i < 200; i++)
		{
			for(Entity e : ens)
			{
				e.Update();
			}
			vis.handleOutput(ens);
		}
		
		System.out.println("Waiting for close");
		vis.waitForInput();
	}


	@Override
	public void handleOutput(List<Entity> collection) {
		if(getInputCloseSignal()) //read input
			System.exit(0);
		glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
		 
		for(Entity e : collection)
			render(e);
		Display.sync(FPS);	//sync to fps
		Display.update();	//update the view/screen
	}
	
	private Random rand = new Random();
	
	private Color getColor(Entity e)
	{
		// Lazy initialisation
		if(!colormap.containsKey(e.getId()))
		{
			colormap.put(e.getId(), new Color(100 + rand.nextInt() % 126,60 + rand.nextInt() % 126,100 + rand.nextInt() % 126));
		}
		return colormap.get(e.getId());
	}

	private void render(Entity e) {		
		Vector pos = e.getPosition();
		//System.out.println(e.getId() + ": ( " + pos.getVar(0) + ", " + pos.getVar(1) + " )");
		glLoadIdentity();
		
		double x = pos.getVar(0);
		double y = (pos.getDimensions() > 1) ? pos.getVar(1) : 0;
		glTranslated(x,y, 100.0f);
		
		Color c = getColor(e);		
		glColor3b((byte) c.getRed(), (byte) c.getGreen(), (byte) c.getBlue());
	
		Sphere s = new Sphere();
		s.draw((float) e.getRadius(), 20, 16);
	}
	
	private boolean getInputCloseSignal() {
		return Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Display.isCloseRequested();
	}

}
