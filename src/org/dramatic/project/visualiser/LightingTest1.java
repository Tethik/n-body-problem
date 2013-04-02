package org.dramatic.project.visualiser;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import static org.lwjgl.opengl.GL11.*;

public class LightingTest1 {
	private static final int WIDTH = 800, HEIGHT = 600;
	private static final DisplayMode DISPLAY_MODE = new DisplayMode(WIDTH, HEIGHT);
	private static final String WINDOW_TITLE = "Lighting Test";
	private static final int FPS = 70;	//frames per second
	
	private boolean isRunning;	//variable to tell if program is running or not
	private float zTranslation = -12f;
	
	//----------- Variables added for Lighting Test -----------//
	private FloatBuffer matSpecular;
	private FloatBuffer lightPosition;
	private FloatBuffer whiteLight; 
	private FloatBuffer lModelAmbient;
	//----------- END: Variables added for Lighting Test -----------//
	
	public static void main(String[] args) {
		LightingTest1 test = new LightingTest1();
		test.run( );
	}

	private void run() {
		try {
			init();
			
			
			while( isRunning ) {
				getInput();	//read input
				a += (Math.PI * 2) / (70*10);
				render(); 	//render graphics
				Display.sync(FPS);	//sync to fps
				Display.update();	//update the view/screen
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private double a;
	
	private void render() {
		glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
		glLoadIdentity(); 
	
		glTranslatef((float) (0.0f + Math.cos(a)),(float) (0.0f + Math.sin(a)), zTranslation);
			
		glColor3f(0.1f, 0.4f, 0.9f);
		Sphere s = new Sphere();
		s.draw(1.0f, 20, 16);
	
	}
	
	
	private void getInput() {
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {	// if user presses escape key
			isRunning = false;
		}
		if( Display.isCloseRequested()) {	// if user closes window
			isRunning = false;
		}
	}
	
	private void init() {
		createWindow();
		initGL();
		isRunning = true;
	}
	

	private void createWindow() {
		try {
			Display.setDisplayMode(DISPLAY_MODE);
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
		
		float fovy = 45.0f;
		float aspect = DISPLAY_MODE.getWidth() / (float)DISPLAY_MODE.getHeight();
		float zNear = 0.1f;
		float zFar = 100.0f;
		GLU.gluPerspective(fovy, aspect, zNear, zFar);
		
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
}