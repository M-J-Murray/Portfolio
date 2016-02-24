/* CS2150Coursework.java
 * A scene consisting of a controllable car, a grassy terrain, a race track, race gates, a brick wall boarder and a sky box
 * 10/12/2015
 * 
 * Scene Graph:
 *  Scene origin
 *  |
 *  +-- [Ry(skyBoxRotation)] SkyBox
 *  |
 *  +-- Grass Ground Plane
 *  |
 *  +-- [T(0,0.001,0)] Track Ground Plane
 *  |
 *  +-- Brick Ground Plane Boarder
 *  |
 *  +-- [T(finishGate.getPosition().x, finishGate.getPosition().y, finishGate.getPosition().z) Ry(finishGate.getAngle() S(3.5, 1, 1))] Finish Gate
 *  |
 *  +-- [T(raceGate1.getPosition().x, raceGate1.getPosition().y, raceGate1.getPosition().z) Ry(raceGate1.getAngle() S(3.5, 1, 1))] Race Gate 1
 *  |
 *  +-- [T(raceGate2.getPosition().x, raceGate2.getPosition().y, raceGate2.getPosition().z) Ry(raceGate2.getAngle() S(3.5, 1, 1))] Race Gate 2
 *  |
 *  +-- [T(raceGate3.getPosition().x, raceGate3.getPosition().y, raceGate3.getPosition().z) Ry(raceGate3.getAngle() S(3.5, 1, 1))] Race Gate 3
 *  |
 *  +-- [T(raceGate4.getPosition().x, raceGate4.getPosition().y, raceGate4.getPosition().z) Ry(raceGate4.getAngle() S(3.5, 1, 1))] Race Gate 4
 *  |
 *  +-- [T(raceGate5.getPosition().x, raceGate5.getPosition().y, raceGate5.getPosition().z) Ry(raceGate5.getAngle() S(3.5, 1, 1))] Race Gate 5
 *  |
 *  +-- [T(raceGate6.getPosition().x, raceGate6.getPosition().y, raceGate6.getPosition().z) Ry(raceGate6.getAngle() S(3.5, 1, 1))] Race Gate 6
 *  |
 *  +-- [T(raceGate7.getPosition().x, raceGate7.getPosition().y, raceGate7.getPosition().z) Ry(raceGate7.getAngle() S(3.5, 1, 1))] Race Gate 7
 *  |
 *  +-- [T(raceGate8.getPosition().x, raceGate8.getPosition().y, raceGate8.getPosition().z) Ry(raceGate8.getAngle() S(3.5, 1, 1))] Race Gate 8
 *  |
 *  +-- [T(raceGate9.getPosition().x, raceGate9.getPosition().y, raceGate9.getPosition().z) Ry(raceGate9.getAngle() S(3.5, 1, 1))] Race Gate 9
 *  |
 *  +-- [T(raceGate10.getPosition().x, raceGate10.getPosition().y, raceGate10.getPosition().z) Ry(raceGate10.getAngle() S(3.5, 1, 1))] Race Gate 10
 *  |
 *  +-- [T(0, 0.05, 0) T(car1.getPosition().x, car1.getPosition().y, car1.getPosition().z) Ry(car1.getCarAngle())] Car
 *  |	|
 *  |	+[S(1.1, 1.0, 1.0)] Car exterior
 *  |   |
 *  |   +[T(-0.12, 0.09, -0.12) Ry(-90)] Left Car Wing Mirror
 *  |   |
 *  |   +[T(-0.12, 0.09, 0.12) Ry(-90)] Right Car Wing Mirror
 *  |	|
 *  |	+[T(0.165, 0.01, 0.12) Rz(car1.getWheelRotationAngle())] Back Left Car Wheel
 *  |	|
 *  |	+[T(-0.165, 0.01, 0.12) Ry(car1.getCarWheelAngle()) Rz(car1.getWheelRotationAngle())] Front Left Car Wheel
 *  |	|
 *  |	+[T(0.165, 0.01, -0.12) Rz(car1.getWheelRotationAngle())] Back Right Car Wheel
 *  |	|
 *  |	+[T(-0.165, 0.01, -0.12) Ry(car1.getCarWheelAngle()) Rz(car1.getWheelRotationAngle())] Front Right Car Wheel
 */

import java.util.Date;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import GraphicsLab.*;

/**
 * This program demonstrates a car followed by a 3rd person camera driving around a track through gates to get the fastest time possible.
 * The track is made of two layers, a grass plane which has a repeating grass texture on it, and a track plane with the texture for the track.
 * Around the track is a brick wall boarder which stops the car from falling off of the track.
 * Outside of a the track is a giant cube with  textures for the sky which slowly rotates around the track.
 *
 * <p>Controls:
 * <ul>
 * <li>Press the escape key to exit the application.
 * <li>Hold the x, y and z keys to view the scene along the x, y and z axis, respectively
 * <li>While viewing the scene along the x, y or z axis, use the up and down cursor keys
 *      to increase or decrease the viewpoint's distance from the scene origin
 * <li>Hold the w or s keys to accelerate or decelerate the car respectively
 * <li>While moving hold the a or d key to turn the front wheels left or right respectively and therefore the car
 * <li>While moving hold the left shift key to brake
 * <li>Hold the r key to rotate the camera around the car, hold the brake button and r to rotate the other way around
 * </ul>
 * 
 * @author Michael Murray
 */
public class CS2150Coursework extends GraphicsLab
{
	/** display list id for the wheels */
	private final int wheelList = 1;
	/** display list id for the exterior */
	private final int carExteriorList = 2;
	/** display list id for the wing mirror */
	private final int carWingMirrorList = 3;
	/** display list id for the grass plane */
	private final int grassPlaneList = 4;
	/** display list id for the track plane */
	private final int trackPlaneList = 5;
	/** display list id for the sky box */
	private final int skyBoxList = 6;
	/** display list id for the brick wall boarder */
	private final int brickBoarderList = 7;
	/** display list id for the finish gate */
	private final int finishGateList = 8;
	/** display list id for the green gate */
	private final int greenGateList = 9;
	/** display list id for the red gate */
	private final int redGateList = 10;

	/** ids for nearest, linear and mipmapped textures for the car wheel rim */
	private Texture rimTexture;
	/** ids for nearest, linear and mipmapped textures for the car wheel tyre */
	private Texture tyreTexture;
	/** ids for nearest, linear and mipmapped textures for the car metal and for the red flag on the race gate */
	private Texture redTexture;
	/** ids for nearest, linear and mipmapped textures for the green flag on race gate */
	private Texture greenTexture;
	/** ids for nearest, linear and mipmapped textures for the black poles on race gate */
	private Texture blackTexture;
	/** ids for nearest, linear and mipmapped textures for the front wind screen on the car */
	private Texture frontWindScreenTexture;
	/** ids for nearest, linear and mipmapped textures for the rear windscreen on the car */
	private Texture rearWindScreenTexture;
	/** ids for nearest, linear and mipmapped textures for the side windows in the car */
	private Texture sideWindowTexture;
	/** ids for nearest, linear and mipmapped textures for the rear bonnet of the car*/
	private Texture rearBonnetTexture;
	/** ids for nearest, linear and mipmapped textures for the front bonnet of the car*/
	private Texture frontBonnetTexture;
	/** ids for nearest, linear and mipmapped textures for the boot of the car*/
	private Texture carBootTexture;
	/** ids for nearest, linear and mipmapped textures for the grass plane*/
	private Texture grassTexture;
	/** ids for nearest, linear and mipmapped textures for the track plane*/
	private Texture trackTexture;
	/** ids for nearest, linear and mipmapped textures for the brick wall boarder*/
	private Texture brickWallTexture;
	/** ids for nearest, linear and mipmapped textures for the top quadrant of the sky box */
	private Texture backgroundTexture0;
	/** ids for nearest, linear and mipmapped textures for the front quadrant of the sky box */
	private Texture backgroundTexture1;
	/** ids for nearest, linear and mipmapped textures for the right quadrant of the sky box */
	private Texture backgroundTexture2;
	/** ids for nearest, linear and mipmapped textures for the back quadrant of the sky box */
	private Texture backgroundTexture3;
	/** ids for nearest, linear and mipmapped textures for the left quadrant of the sky box */
	private Texture backgroundTexture4;
	/** ids for nearest, linear and mipmapped textures for the bottom quadrant of the sky box */
	private Texture backgroundTexture5;
	/** ids for nearest, linear and mipmapped textures for the finish flag on the finish race gate*/
	private Texture finishFlagTexture;

	/** the Car */
	private Car car1;
	/** the start and finish gate on the track*/
	private Gate finishGate;
	/** the first race gate on the track*/
	private Gate raceGate1;
	/** the second race gate on the track*/
	private Gate raceGate2;
	/** the third race gate on the track*/
	private Gate raceGate3;
	/** the fourth race gate on the track*/
	private Gate raceGate4;
	/** the fifth race gate on the track*/
	private Gate raceGate5;
	/** the sixth race gate on the track*/
	private Gate raceGate6;
	/** the seventh race gate on the track*/
	private Gate raceGate7;
	/** the eighth race gate on the track*/
	private Gate raceGate8;
	/** the ninth race gate on the track*/
	private Gate raceGate9;
	/** the tenth race gate on the track*/
	private Gate raceGate10;

	/** the sky boxes current angle of rotation */
	private float skyBoxRotation;
	/** time taken when race begins */
	private long startTime;
	/** the current lap time of the car */
	private float lapTime;
	/** the fastest lap time achieved by the car */
	private float fastestTime = 27.038f;
	/** shows when a lap has been completed to record final lapTime*/
	private boolean lapComplete = false;

	public static void main(String args[])
	{   new CS2150Coursework().run(WINDOWED,"CS2150 Coursework Submission",0.01f);
	}

	protected void initScene() throws Exception
	{		
		//initiates car with its position, top speed and angle
		car1 = new Car(new Vector3f(43.0f, 0.0f, 2.0f), 120.0f, 90.0f);
		//initiates finish gate with its gate number, position, angle and flag type
		finishGate = new Gate(0, new Vector3f(44.8f, 0.0f, 3.0f), 0.0f, 0);
		//initiates race gate 1 with its gate number, position, angle and flag type
		raceGate1 = new Gate(1, new Vector3f(44.3f, 0.0f, 30.0f), 0.0f, 2);
		//initiates race gate 2 with its gate number, position, angle and flag type
		raceGate2 = new Gate(2, new Vector3f(5f, 0.0f, 32.7f), 90.0f, 2);
		//initiates race gate 3 with its gate number, position, angle and flag type
		raceGate3 = new Gate(3, new Vector3f(-25f, 0.0f, 42.0f), 120.0f, 2);
		//initiates race gate 4 with its gate number, position, angle and flag type
		raceGate4 = new Gate(4, new Vector3f(-40.5f, 0.0f, 18.0f), 0.0f, 2);
		//initiates race gate 5 with its gate number, position, angle and flag type
		raceGate5 = new Gate(5, new Vector3f(-17f, 0.0f, 9.8f), 90.0f, 2);
		//initiates race gate 6 with its gate number, position, angle and flag type
		raceGate6 = new Gate(6, new Vector3f(11.5f, 0.0f, -5.0f), -30.0f, 2);
		//initiates race gate 7 with its gate number, position, angle and flag type
		raceGate7 = new Gate(7, new Vector3f(-23.0f, 0.0f, -8.3f), 80.0f, 2);
		//initiates race gate 8 with its gate number, position, angle and flag type
		raceGate8 = new Gate(8, new Vector3f(-35.4f, 0.0f, -25.0f), 0.0f, 2);
		//initiates race gate 9 with its gate number, position, angle and flag type
		raceGate9 = new Gate(9, new Vector3f(0.0f, 0.0f, -38.7f), 90.0f, 2);
		//initiates race gate 10 with its gate number, position, angle and flag type
		raceGate10 = new Gate(10, new Vector3f(43.4f, 0.0f, -25.0f), 0.0f, 2);


		// load the textures
		trackTexture = loadTexture("Coursework/murraym1/textures/track2.png");
		grassTexture = loadTexture("Coursework/murraym1/textures/grass.png");
		rimTexture = loadTexture("Coursework/murraym1/textures/rim.png");
		tyreTexture = loadTexture("Coursework/murraym1/textures/tyre.png");	
		redTexture = loadTexture("Coursework/murraym1/textures/red.png");
		greenTexture = loadTexture("Coursework/murraym1/textures/green.png");
		blackTexture = loadTexture("Coursework/murraym1/textures/black.png");
		frontWindScreenTexture = loadTexture("Coursework/murraym1/textures/fws4.png");
		rearWindScreenTexture = loadTexture("Coursework/murraym1/textures/rw2.png");
		sideWindowTexture = loadTexture("Coursework/murraym1/textures/lsw1.png");
		rearBonnetTexture = loadTexture("Coursework/murraym1/textures/rt1.png");
		frontBonnetTexture = loadTexture("Coursework/murraym1/textures/fb1.png");
		carBootTexture = loadTexture("Coursework/murraym1/textures/carBoot.png");
		backgroundTexture0 = loadTexture("Coursework/murraym1/textures/topBackground.png");
		backgroundTexture1 = loadTexture("Coursework/murraym1/textures/frontBackground.png");
		backgroundTexture2 = loadTexture("Coursework/murraym1/textures/rightBackground.png");
		backgroundTexture3 = loadTexture("Coursework/murraym1/textures/backBackground.png");
		backgroundTexture4 = loadTexture("Coursework/murraym1/textures/leftBackground.png");
		backgroundTexture5 = loadTexture("Coursework/murraym1/textures/bottomBackground.png");
		brickWallTexture = loadTexture("Coursework/murraym1/textures/brickWall.png");
		finishFlagTexture = loadTexture("Coursework/murraym1/textures/finishFlag.png");

		// prepare the display lists for later use
		GL11.glNewList(wheelList,GL11.GL_COMPILE);
		{   drawWheel();
		}
		GL11.glEndList();
		GL11.glNewList(carExteriorList,GL11.GL_COMPILE);
		{   drawCarExterior();
		}
		GL11.glEndList();
		GL11.glNewList(carWingMirrorList,GL11.GL_COMPILE);
		{   drawWingMirror();
		}
		GL11.glEndList();
		GL11.glNewList(grassPlaneList,GL11.GL_COMPILE);
		{   drawUnitGrassPlane();
		}
		GL11.glEndList();
		GL11.glNewList(trackPlaneList,GL11.GL_COMPILE);
		{   drawUnitTrackPlane();
		}
		GL11.glEndList();
		GL11.glNewList(skyBoxList,GL11.GL_COMPILE);
		{   drawSkyBox();
		}
		GL11.glEndList();
		GL11.glNewList(brickBoarderList,GL11.GL_COMPILE);
		{   drawBoarder();
		}
		GL11.glEndList();
		GL11.glNewList(finishGateList,GL11.GL_COMPILE);
		{   drawGate(0);
		}
		GL11.glEndList();
		GL11.glNewList(greenGateList,GL11.GL_COMPILE);
		{   drawGate(1);
		}
		GL11.glEndList();
		GL11.glNewList(redGateList,GL11.GL_COMPILE);
		{   drawGate(2);
		}
		GL11.glEndList();
	}

	protected void checkSceneInput()
	{			
		//binds car inputs for accelerate, decelerate, turn left, turn right, brake and rotate camera
		car1.bindKeys(Keyboard.KEY_W, Keyboard.KEY_S, Keyboard.KEY_A, Keyboard.KEY_D, Keyboard.KEY_LSHIFT, Keyboard.KEY_R);
	}

	protected void updateScene()
	{
		//updates sky box rotation to make it slowly rotate
		skyBoxRotation += 0.001f;
		//updates car variables
		car1.update();
		//checks the car is not colliding with track boundaries
		car1.checkBoundaries(-50.0f, -50.0f, 100.0f, 100.0f);

		//Checks which gate the car is expected to go through next then updates the current lap time
		switch (car1.getCurrentGate()){
		case 0 : 
			if (finishGate.checkGateCrossed(car1) == true){
				//finalises track time then updates fastest track time if it is beaten
				if (lapComplete == true){
					lapComplete = false;
					lapTime = (float) (new Date().getTime() - startTime)/1000;
					System.out.println("Lap Time = " + lapTime);
					System.out.println("Fastest Lap Time = " + fastestTime);
					if (lapTime < fastestTime){
						fastestTime = lapTime;
						System.out.println("New Fastest Time!");
						System.out.println("Fastest Lap Time = " + fastestTime);
					}
				}
				startTime = new Date().getTime();
			}
			break;
		case 1 : 
			if (raceGate1.checkGateCrossed(car1) == true){
				lapTime = (float) (new Date().getTime() - startTime)/1000;
				System.out.println("Current Lap Time = " + lapTime+" Seconds");
			}
			break;
		case 2 : 
			if (raceGate2.checkGateCrossed(car1) == true){
				lapTime = (float) (new Date().getTime() - startTime)/1000;
				System.out.println("Current Lap Time = " + lapTime+" Seconds");
			}
			break;
		case 3 : 
			if (raceGate3.checkGateCrossed(car1) == true){
				lapTime = (float) (new Date().getTime() - startTime)/1000;
				System.out.println("Current Lap Time = " + lapTime+" Seconds");
			}
			break;
		case 4 : 
			if (raceGate4.checkGateCrossed(car1) == true){
				lapTime = (float)(new Date().getTime() - startTime)/1000;
				System.out.println("Current Lap Time = " + lapTime+" Seconds");
			}
			break;
		case 5 : 
			if (raceGate5.checkGateCrossed(car1) == true){
				lapTime = (float) (new Date().getTime() - startTime)/1000;
				System.out.println("Current Lap Time = " + lapTime+" Seconds");
			}
			break;
		case 6 : 
			if (raceGate6.checkGateCrossed(car1) == true){
				lapTime = (float) (new Date().getTime() - startTime)/1000;
				System.out.println("Current Lap Time = " + lapTime+" Seconds");
			}
			break;
		case 7 : 
			if (raceGate7.checkGateCrossed(car1) == true){
				lapTime = (float) (new Date().getTime() - startTime)/1000;
				System.out.println("Current Lap Time = " + lapTime+" Seconds");
			}
			break;
		case 8 : 
			if (raceGate8.checkGateCrossed(car1) == true){
				lapTime = (float) (new Date().getTime() - startTime)/1000;
				System.out.println("Current Lap Time = " + lapTime+" Seconds");
			}
			break;
		case 9 :
			if (raceGate9.checkGateCrossed(car1) == true){
				lapTime = (float) (new Date().getTime() - startTime)/1000;
				System.out.println("Current Lap Time = " + lapTime+" Seconds");
			}
			break;
		case 10 :
			if (raceGate10.checkGateCrossed(car1) == true){
				lapTime = (float)(new Date().getTime() - startTime)/1000;
				System.out.println("Current Lap Time = " + lapTime+" Seconds");
				lapComplete = true;
			}
			break;
		} 
	}

	protected void renderScene()
	{				
		// draws the race gate 1
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture 
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);

			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();

			// position, rotate, scale and draw either the green gate or the red gate depending on its current flag type using the relevant display list
			GL11.glTranslatef(raceGate1.getPosition().x, raceGate1.getPosition().y, raceGate1.getPosition().z);
			GL11.glRotatef(raceGate1.getAngle(), 0.0f, 1.0f, 0.0f);
			GL11.glScalef(3.5f, 1.0f, 1.0f);
			switch (raceGate1.getflagType()){
			case 1 : GL11.glCallList(greenGateList);
			break;
			case 2 : GL11.glCallList(redGateList);
			break;
			}
			//enables lighting calculations again
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();

		}
		GL11.glPopMatrix();

		// draw the race gate 2
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture 
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);

			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();

			// position, rotate, scale and draw either the green gate or the red gate depending on its current flag type using the relevant display list
			GL11.glTranslatef(raceGate2.getPosition().x, raceGate2.getPosition().y, raceGate2.getPosition().z);
			GL11.glRotatef(raceGate2.getAngle(), 0.0f, 1.0f, 0.0f);
			GL11.glScalef(3.5f, 1.0f, 1.0f);
			switch (raceGate2.getflagType()){
			case 1 : GL11.glCallList(greenGateList);
			break;
			case 2 : GL11.glCallList(redGateList);
			break;
			}
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();

		}
		GL11.glPopMatrix();

		// draw the race gate 3
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture 
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);

			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();

			// position, rotate, scale and draw either the green gate or the red gate depending on its current flag type using the relevant display list
			GL11.glTranslatef(raceGate3.getPosition().x, raceGate3.getPosition().y, raceGate3.getPosition().z);
			GL11.glRotatef(raceGate3.getAngle(), 0.0f, 1.0f, 0.0f);
			GL11.glScalef(3.5f, 1.0f, 1.0f);
			switch (raceGate3.getflagType()){
			case 1 : GL11.glCallList(greenGateList);
			break;
			case 2 : GL11.glCallList(redGateList);
			break;
			}
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();

		}
		GL11.glPopMatrix();

		// draw the race gate 4
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture 
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);

			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();

			// position, rotate, scale and draw either the green gate or the red gate depending on its current flag type using the relevant display list
			GL11.glTranslatef(raceGate4.getPosition().x, raceGate4.getPosition().y, raceGate4.getPosition().z);
			GL11.glRotatef(raceGate4.getAngle(), 0.0f, 1.0f, 0.0f);
			GL11.glScalef(3.5f, 1.0f, 1.0f);
			switch (raceGate4.getflagType()){
			case 1 : GL11.glCallList(greenGateList);
			break;
			case 2 : GL11.glCallList(redGateList);
			break;
			}
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();

		}
		GL11.glPopMatrix();

		// draw the race gate 5
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture 
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);

			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();

			// position, rotate, scale and draw either the green gate or the red gate depending on its current flag type using the relevant display list
			GL11.glTranslatef(raceGate5.getPosition().x, raceGate5.getPosition().y, raceGate5.getPosition().z);
			GL11.glRotatef(raceGate5.getAngle(), 0.0f, 1.0f, 0.0f);
			GL11.glScalef(3.5f, 1.0f, 1.0f);
			switch (raceGate5.getflagType()){
			case 1 : GL11.glCallList(greenGateList);
			break;
			case 2 : GL11.glCallList(redGateList);
			break;
			}
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();

		}
		GL11.glPopMatrix();

		// draw the race gate 6
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture 
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);

			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();

			// position, rotate, scale and draw either the green gate or the red gate depending on its current flag type using the relevant display list
			GL11.glTranslatef(raceGate6.getPosition().x, raceGate6.getPosition().y, raceGate6.getPosition().z);
			GL11.glRotatef(raceGate6.getAngle(), 0.0f, 1.0f, 0.0f);
			GL11.glScalef(3.5f, 1.0f, 1.0f);
			switch (raceGate6.getflagType()){
			case 1 : GL11.glCallList(greenGateList);
			break;
			case 2 : GL11.glCallList(redGateList);
			break;
			}
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();

		}
		GL11.glPopMatrix();

		// draw the race gate 7
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture 
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);

			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();

			// position, rotate, scale and draw either the green gate or the red gate depending on its current flag type using the relevant display list
			GL11.glTranslatef(raceGate7.getPosition().x, raceGate7.getPosition().y, raceGate7.getPosition().z);
			GL11.glRotatef(raceGate7.getAngle(), 0.0f, 1.0f, 0.0f);
			GL11.glScalef(3.5f, 1.0f, 1.0f);
			switch (raceGate7.getflagType()){
			case 1 : GL11.glCallList(greenGateList);
			break;
			case 2 : GL11.glCallList(redGateList);
			break;
			}
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();

		}
		GL11.glPopMatrix();

		// draw the race gate 8
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture 
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);

			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();

			// position, rotate, scale and draw either the green gate or the red gate depending on its current flag type using the relevant display list
			GL11.glTranslatef(raceGate8.getPosition().x, raceGate8.getPosition().y, raceGate8.getPosition().z);
			GL11.glRotatef(raceGate8.getAngle(), 0.0f, 1.0f, 0.0f);
			GL11.glScalef(3.5f, 1.0f, 1.0f);
			switch (raceGate8.getflagType()){
			case 1 : GL11.glCallList(greenGateList);
			break;
			case 2 : GL11.glCallList(redGateList);
			break;
			}
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();

		}
		GL11.glPopMatrix();

		// draw the race gate 9
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture 
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);

			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();

			// position, rotate, scale and draw either the green gate or the red gate depending on its current flag type using the relevant display list
			GL11.glTranslatef(raceGate9.getPosition().x, raceGate9.getPosition().y, raceGate9.getPosition().z);
			GL11.glRotatef(raceGate9.getAngle(), 0.0f, 1.0f, 0.0f);
			GL11.glScalef(3.5f, 1.0f, 1.0f);
			switch (raceGate9.getflagType()){
			case 1 : GL11.glCallList(greenGateList);
			break;
			case 2 : GL11.glCallList(redGateList);
			break;
			}
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();

		}
		GL11.glPopMatrix();

		// draw the race gate 10
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture 
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);

			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();

			// position, rotate, scale and draw either the green gate or the red gate depending on its current flag type using the relevant display list
			GL11.glTranslatef(raceGate10.getPosition().x, raceGate10.getPosition().y, raceGate10.getPosition().z);
			GL11.glRotatef(raceGate10.getAngle(), 0.0f, 1.0f, 0.0f);
			GL11.glScalef(3.5f, 1.0f, 1.0f);
			switch (raceGate10.getflagType()){
			case 1 : GL11.glCallList(greenGateList);
			break;
			case 2 : GL11.glCallList(redGateList);
			break;
			}
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();

		}
		GL11.glPopMatrix();

		// draw the finish gate
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture 
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);

			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();

			// position, rotate, scale and draw either the green gate or the red gate depending on its current flag type using the relevant display list
			GL11.glTranslatef(finishGate.getPosition().x,finishGate.getPosition().y,finishGate.getPosition().z);
			GL11.glRotatef(finishGate.getAngle(), 0.0f, 1.0f, 0.0f);
			GL11.glScalef(3.5f, 1.0f, 1.0f);
			GL11.glCallList(finishGateList);

			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();

		// draw the Brick Boarder
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture 
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);

			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();

			// draw the brick boarder at origin using its display list
			GL11.glCallList(brickBoarderList);

			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();

		// draw the Sky box
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture 
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);

			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();

			// rotate and draw the ground plane at origin using its display list
			GL11.glRotatef(skyBoxRotation, 0.0f, 1.0f, 0.0f);
			GL11.glCallList(skyBoxList);

			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();

		// draw the Grass ground plane
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture 
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);

			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();

			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,grassTexture.getTextureID());

			// draw the ground plane at origin using its display list
			GL11.glCallList(grassPlaneList);

			// disable textures and reset any local lighting changes
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();

		// draw the Track ground plane
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture 
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);

			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();

			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,trackTexture.getTextureID());

			// enable blending and allow alpha channel to render transparency in png image
			GL11.glEnable(GL11.GL_BLEND);			
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			// position and draw the ground plane using its display list
			GL11.glTranslatef(0.0f, 0.001f,0.0f);
			GL11.glCallList(trackPlaneList);

			// disable textures, blending and reset any local lighting changes
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();



		//draw the car
		GL11.glPushMatrix();
		{	
			// postion and rotate car object
			GL11.glTranslatef(0.0f, 0.05f, 0.0f);
			GL11.glTranslatef(car1.getPosition().x, car1.getPosition().y, car1.getPosition().z);
			GL11.glRotatef(car1.getCarAngle(), 0.0f, 1.0f, 0.0f);
			
			// draw the car exterior
			GL11.glPushMatrix();
			{
				// how shiny are the front faces of the car exterior (specular exponent)
				float exteriorFrontShininess  = 20.0f;
				// specular reflection of the front faces of the car exterior
				float exteriorFrontSpecular[] = {1.0f, 1.0f, 1.0f, 1.0f};
				// diffuse reflection of the front faces of the car exterior
				float exteriorFrontDiffuse[]  = {0.8f, 0.8f, 0.8f, 1.0f};

				// set the material properties for the car using OpenGL
				GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, exteriorFrontShininess);
				GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(exteriorFrontSpecular));
				GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(exteriorFrontDiffuse));

				// scale and draw the car exterior using its display list
				GL11.glScaled(1.1f, 1.0f, 1.0f);
				GL11.glCallList(carExteriorList);  
			}
			GL11.glPopMatrix();
			
			// draw left car wing mirror
			GL11.glPushMatrix();
			{
				// position and draw wing mirror using its display list
				GL11.glTranslatef(-0.12f, 0.09f, 0.12f);
				GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
				GL11.glCallList(carWingMirrorList);   
			}
			GL11.glPopMatrix();
			
			// draw right car wing mirror
			GL11.glPushMatrix();
			{
				// position and draw wing mirror using its display list
				GL11.glTranslatef(-0.12f, 0.09f, -0.12f);
				GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
				GL11.glCallList(carWingMirrorList);   
			}
			GL11.glPopMatrix();
			
			// draw back left wheel
			GL11.glPushMatrix();
			{
				// position, rotate and draw a wheel using its display list
				GL11.glTranslatef(0.165f, 0.01f, 0.12f);
				GL11.glRotatef(car1.getWheelRotationAngle(), 0.0f, 0.0f, 1.0f); 
				GL11.glCallList(wheelList);    
			}
			GL11.glPopMatrix();
			
			// draw front left wheel
			GL11.glPushMatrix();
			{
				// position, rotate and draw a wheel using its display list
				GL11.glTranslatef(-0.165f, 0.01f, 0.12f);
				GL11.glRotatef(car1.getCarWheelAngle(), 0.0f, 1.0f, 0.0f); 
				GL11.glRotatef(car1.getWheelRotationAngle(), 0.0f, 0.0f, 1.0f); 
				GL11.glCallList(wheelList);    
			}
			GL11.glPopMatrix();
			
			// draw back right wheel
			GL11.glPushMatrix();
			{
				// position, rotate and draw a wheel using its display list
				GL11.glTranslatef(0.165f, 0.01f, -0.12f);
				GL11.glRotatef(car1.getWheelRotationAngle(), 0.0f, 0.0f, 1.0f); 
				GL11.glCallList(wheelList);    
			}
			GL11.glPopMatrix();
			
			// draw front right wheel
			GL11.glPushMatrix();
			{
				// position, rotate and draw a wheel using its display list
				GL11.glTranslatef(-0.165f, 0.01f, -0.12f);
				GL11.glRotatef(car1.getCarWheelAngle(), 0.0f, 1.0f, 0.0f); 
				GL11.glRotatef(car1.getWheelRotationAngle(), 0.0f, 0.0f, 1.0f); 
				GL11.glCallList(wheelList);    
			}
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}

	protected void setSceneCamera()
	{
		//uses gluPerspective with 1000 range to allow for sky box to rendered correctly
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f,1.3f,0.1f,1000.0f);

		// default viewpoint is positioned in a 3rd person perspective watching from behind the car
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GLU.gluLookAt(	car1.getCameraPosition().x, car1.getCameraPosition().y, car1.getCameraPosition().z, // viewer location
				car1.getPosition().x, car1.getPosition().y, car1.getPosition().z, // view point loc.
				0.0f, 1.0f, 0.0f); // view-up vector

		//Lighting Calculations are done in setSceneCamera as it allows the camera to move freely while the light is fixed in place

		// global ambient light level
		float globalAmbient[]   = {0.9f,  0.9f,  0.9f, 1.0f};
		// set the global ambient lighting
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT,FloatBuffer.wrap(globalAmbient));

		// the first light for the scene is white...
		float diffuse0[]  = { 0.6f,  0.6f, 0.6f, 1.0f};
		// ...with a medium ambient contribution...
		float ambient0[]  = { 0.5f,  0.5f, 0.5f, 1.0f};
		// ...and is positioned above the viewpoint
		float position0[] = { 0.0f, 50.0f, 0.0f, 1.0f};

		// supply OpenGL with the properties for the first light
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, FloatBuffer.wrap(ambient0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, FloatBuffer.wrap(position0));
		// enable the first light
		GL11.glEnable(GL11.GL_LIGHT0);

		// enable lighting calculations
		GL11.glEnable(GL11.GL_LIGHTING);
		// ensure that all normals are re-normalised after transformations automatically
		GL11.glEnable(GL11.GL_NORMALIZE);

		// use a smooth shade model for the car
		GL11.glShadeModel(GL11.GL_SMOOTH);
	}

	protected void cleanupScene()
	{ // empty
	}

	/**
	 * Draws two poles attached by a flag to create a race gate. 
	 * The gate uses the current OpenGL material settings for its appearance.
	 */
	private void drawGate(int gateType){
		//sets pole width
		float poleWidth = 0.1f;
		Vertex v1 = new Vertex(-poleWidth-1f, 0.0f,-poleWidth); // left,  back pole1
		Vertex v2 = new Vertex( poleWidth-1f, 0.0f,-poleWidth); // right, back pole1
		Vertex v3 = new Vertex( poleWidth-1f, 0.0f, poleWidth); // right, front pole1
		Vertex v4 = new Vertex(-poleWidth-1f, 0.0f, poleWidth); // left,  front pole1
		Vertex v5 = new Vertex(-poleWidth-1f, 1.5f,-poleWidth); // left,  back pole1
		Vertex v6 = new Vertex( poleWidth-1f,	1.5f,-poleWidth); // right, back pole1
		Vertex v7 = new Vertex( poleWidth-1f, 1.5f, poleWidth); // right, front pole1
		Vertex v8 = new Vertex(-poleWidth-1f, 1.5f, poleWidth); // left,  front pole1
		Vertex v9 = new Vertex(-poleWidth+1f, 0.0f,-poleWidth); // left,  back pole2
		Vertex v10 = new Vertex( poleWidth+1f, 0.0f,-poleWidth); // right, back pole2
		Vertex v11 = new Vertex( poleWidth+1f, 0.0f, poleWidth); // right, front pole2
		Vertex v12 = new Vertex(-poleWidth+1f, 0.0f, poleWidth); // left,  front pole2
		Vertex v13 = new Vertex(-poleWidth+1f, 1.5f,-poleWidth); // left,  back pole2
		Vertex v14 = new Vertex( poleWidth+1f,	1.5f,-poleWidth); // right, back pole2
		Vertex v15 = new Vertex( poleWidth+1f, 1.5f, poleWidth); // right, front pole2
		Vertex v16 = new Vertex(-poleWidth+1f, 1.5f, poleWidth); // left,  front pole2
		Vertex v17 = new Vertex(-1f+poleWidth, 1.5f, 0.0f); // flag top left
		Vertex v18 = new Vertex(-1f+poleWidth, 1.0f, 0.0f);	// flag bottom left
		Vertex v19 = new Vertex(1f-poleWidth, 1.0f, 0.0f);	// flag bottom right
		Vertex v20 = new Vertex(1f-poleWidth, 1.5f, 0.0f);	// flag top right

		// enable texturing and bind an appropriate texture
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, blackTexture.getTextureID());
		//draws top of left pole
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v1.toVector(),v2.toVector(),v3.toVector(),v4.toVector()).submit();

			GL11.glTexCoord2f(0.0f,0.0f);
			v1.submit();

			GL11.glTexCoord2f(1.0f,0.0f);
			v2.submit();

			GL11.glTexCoord2f(1.0f,1.0f);
			v3.submit();

			GL11.glTexCoord2f(0.0f,1.0f);
			v4.submit();
		}
		GL11.glEnd();
		//draws front of left pole
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v2.toVector(),v1.toVector(),v5.toVector(),v6.toVector()).submit();

			GL11.glTexCoord2f(0.0f,0.0f);
			v2.submit();

			GL11.glTexCoord2f(1.0f,0.0f);
			v1.submit();

			GL11.glTexCoord2f(1.0f,1.0f);
			v5.submit();

			GL11.glTexCoord2f(0.0f,1.0f);
			v6.submit();
		}
		GL11.glEnd();
		//draws right of left pole
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v2.toVector(),v6.toVector(),v7.toVector(),v3.toVector()).submit();

			GL11.glTexCoord2f(0.0f,0.0f);
			v2.submit();

			GL11.glTexCoord2f(1.0f,0.0f);
			v6.submit();

			GL11.glTexCoord2f(1.0f,1.0f);
			v7.submit();

			GL11.glTexCoord2f(0.0f,1.0f);
			v3.submit();
		}
		GL11.glEnd();
		//draws back of left pole
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v3.toVector(),v7.toVector(),v8.toVector(),v4.toVector()).submit();

			GL11.glTexCoord2f(0.0f,0.0f);
			v3.submit();

			GL11.glTexCoord2f(1.0f,0.0f);
			v7.submit();

			GL11.glTexCoord2f(1.0f,1.0f);
			v8.submit();

			GL11.glTexCoord2f(0.0f,1.0f);
			v4.submit();
		}
		GL11.glEnd();
		//draws left of left pole
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v1.toVector(),v4.toVector(),v8.toVector(),v5.toVector()).submit();

			GL11.glTexCoord2f(0.0f,0.0f);
			v1.submit();

			GL11.glTexCoord2f(1.0f,0.0f);
			v4.submit();

			GL11.glTexCoord2f(1.0f,1.0f);
			v8.submit();

			GL11.glTexCoord2f(0.0f,1.0f);
			v5.submit();
		}
		GL11.glEnd();
		//draws bottom of left pole
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v6.toVector(),v5.toVector(),v8.toVector(),v7.toVector()).submit();

			GL11.glTexCoord2f(0.0f,0.0f);
			v6.submit();

			GL11.glTexCoord2f(1.0f,0.0f);
			v5.submit();

			GL11.glTexCoord2f(1.0f,1.0f);
			v8.submit();

			GL11.glTexCoord2f(0.0f,1.0f);
			v7.submit();
		}
		GL11.glEnd();
		//draws top of right pole
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v9.toVector(),v10.toVector(),v11.toVector(),v12.toVector()).submit();

			GL11.glTexCoord2f(0.0f,0.0f);
			v9.submit();

			GL11.glTexCoord2f(1.0f,0.0f);
			v10.submit();

			GL11.glTexCoord2f(1.0f,1.0f);
			v11.submit();

			GL11.glTexCoord2f(0.0f,1.0f);
			v12.submit();
		}
		GL11.glEnd();
		//draws front of right pole
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v10.toVector(),v9.toVector(),v13.toVector(),v14.toVector()).submit();

			GL11.glTexCoord2f(0.0f,0.0f);
			v10.submit();

			GL11.glTexCoord2f(1.0f,0.0f);
			v9.submit();

			GL11.glTexCoord2f(1.0f,1.0f);
			v13.submit();

			GL11.glTexCoord2f(0.0f,1.0f);
			v14.submit();
		}
		GL11.glEnd();
		//draws right of right pole
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v10.toVector(),v14.toVector(),v15.toVector(),v11.toVector()).submit();

			GL11.glTexCoord2f(0.0f,0.0f);
			v10.submit();

			GL11.glTexCoord2f(1.0f,0.0f);
			v14.submit();

			GL11.glTexCoord2f(1.0f,1.0f);
			v15.submit();

			GL11.glTexCoord2f(0.0f,1.0f);
			v11.submit();
		}
		GL11.glEnd();
		//draws back of right pole
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v11.toVector(),v15.toVector(),v16.toVector(),v12.toVector()).submit();

			GL11.glTexCoord2f(0.0f,0.0f);
			v11.submit();

			GL11.glTexCoord2f(1.0f,0.0f);
			v15.submit();

			GL11.glTexCoord2f(1.0f,1.0f);
			v16.submit();

			GL11.glTexCoord2f(0.0f,1.0f);
			v12.submit();
		}
		GL11.glEnd();
		//draws left of right pole
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v9.toVector(),v12.toVector(),v16.toVector(),v13.toVector()).submit();

			GL11.glTexCoord2f(0.0f,0.0f);
			v9.submit();

			GL11.glTexCoord2f(1.0f,0.0f);
			v12.submit();

			GL11.glTexCoord2f(1.0f,1.0f);
			v16.submit();

			GL11.glTexCoord2f(0.0f,1.0f);
			v13.submit();
		}
		GL11.glEnd();
		//draws bottom of right pole
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v14.toVector(),v13.toVector(),v16.toVector(),v15.toVector()).submit();

			GL11.glTexCoord2f(0.0f,0.0f);
			v14.submit();

			GL11.glTexCoord2f(1.0f,0.0f);
			v13.submit();

			GL11.glTexCoord2f(1.0f,1.0f);
			v16.submit();

			GL11.glTexCoord2f(0.0f,1.0f);
			v15.submit();
		}
		GL11.glEnd();
		//draws flag
		GL11.glPushMatrix();
		{
			// disable cull face to allow double sided 2d shape
			GL11.glDisable(GL11.GL_CULL_FACE);

			// bind coloured texture to flag depending on gate type
			switch (gateType){
			case 0 : GL11.glBindTexture(GL11.GL_TEXTURE_2D, finishFlagTexture.getTextureID());
			break;
			case 1 : GL11.glBindTexture(GL11.GL_TEXTURE_2D, greenTexture.getTextureID());
			break;
			case 2 : GL11.glBindTexture(GL11.GL_TEXTURE_2D, redTexture.getTextureID());
			break;
			}
			// sets texture density horizontally on the flag for the checkered texture
			float textureDensity = 8;
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v14.toVector(),v13.toVector(),v16.toVector(),v15.toVector()).submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v17.submit();

				GL11.glTexCoord2f(2.0f,0.0f);
				v18.submit();

				GL11.glTexCoord2f(2.0f,textureDensity);
				v19.submit();

				GL11.glTexCoord2f(0.0f,textureDensity);
				v20.submit();
			}
			GL11.glEnd();
			// resets cull face for 3d shapes
			GL11.glEnable(GL11.GL_CULL_FACE);
		}
		GL11.glPopMatrix();
		// disable textures
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	/**
	 * Draws a large cube with textures for the sky which surrounds the track. 
	 * The sky box uses the current OpenGL material settings for its appearance.
	 */
	private void drawSkyBox(){
		//determines the size of the sky box
		float skySize = 100f;

		Vertex v1 = new Vertex(-skySize, skySize, skySize);
		Vertex v2 = new Vertex(skySize, skySize, skySize);
		Vertex v3 = new Vertex(skySize, skySize, -skySize);
		Vertex v4 = new Vertex(-skySize, skySize, -skySize);
		Vertex v5 = new Vertex(-skySize, -skySize, skySize);
		Vertex v6 = new Vertex(skySize, -skySize, skySize);
		Vertex v7 = new Vertex(skySize, -skySize, -skySize);
		Vertex v8 = new Vertex(-skySize, -skySize, -skySize);

		// enable textures
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		// draw top face
		GL11.glPushMatrix();
		{
			// bind relevant texture to top face
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTexture0.getTextureID());
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v2.toVector(),v1.toVector(),v4.toVector(),v3.toVector()).submit();

				GL11.glTexCoord2f(1.0f,1.0f);
				v2.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v1.submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v4.submit();

				GL11.glTexCoord2f(1.0f, 0.0f);
				v3.submit();
			}
			GL11.glEnd();
		}
		GL11.glPopMatrix();

		// draw front face
		GL11.glPushMatrix();
		{
			// bind relevant texture to front face
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTexture1.getTextureID());
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v1.toVector(),v2.toVector(),v5.toVector(),v6.toVector()).submit();

				GL11.glTexCoord2f(1.0f,1.0f);
				v1.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v2.submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v6.submit();

				GL11.glTexCoord2f(1.0f,0.0f);
				v5.submit();
			}
			GL11.glEnd();
		}
		GL11.glPopMatrix();

		//draw right face
		GL11.glPushMatrix();
		{
			// bind relevant texture to right face
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTexture2.getTextureID());
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v2.toVector(),v3.toVector(),v6.toVector(),v7.toVector()).submit();

				GL11.glTexCoord2f(1.0f,1.0f);
				v2.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v3.submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v7.submit();

				GL11.glTexCoord2f(1.0f,0.0f);
				v6.submit();
			}
			GL11.glEnd();
		}
		GL11.glPopMatrix();

		// draw back face
		GL11.glPushMatrix();
		{
			// bind relevant texture to back face
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTexture3.getTextureID());
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v3.toVector(),v4.toVector(),v7.toVector(),v8.toVector()).submit();

				GL11.glTexCoord2f(1.0f,1.0f);
				v3.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v4.submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v8.submit();

				GL11.glTexCoord2f(1.0f,0.0f);
				v7.submit();
			}
			GL11.glEnd();
		}
		GL11.glPopMatrix();

		// draw left face
		GL11.glPushMatrix();
		{
			// bind relevant texture to left face
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTexture4.getTextureID());
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v4.toVector(),v1.toVector(),v5.toVector(),v8.toVector()).submit();

				GL11.glTexCoord2f(1.0f,1.0f);
				v4.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v1.submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v5.submit();

				GL11.glTexCoord2f(1.0f,0.0f);
				v8.submit();
			}
			GL11.glEnd();
		}
		GL11.glPopMatrix();

		//draw bottom face
		GL11.glPushMatrix();
		{
			// bind relevant texture to bottom face
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTexture5.getTextureID());
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v5.toVector(),v6.toVector(),v7.toVector(),v8.toVector()).submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v5.submit();

				GL11.glTexCoord2f(1.0f,0.0f);
				v6.submit();

				GL11.glTexCoord2f(1.0f,1.0f);
				v7.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v8.submit();
			}
			GL11.glEnd();
		}
		GL11.glPopMatrix();
		// diable textures
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	/**
	 * Draws a brick wall boarder around the track. 
	 * The brick wall boarder uses the current OpenGL material settings for its appearance.
	 */
	private void drawBoarder(){
		// determines size of ground plane
		float groundSize = 50f;
		// determines brick texture horizontal density on boarder
		float textureDensity = 256;

		Vertex v1 = new Vertex(-groundSize, 0.0f,-groundSize); // left,  back lower
		Vertex v2 = new Vertex( groundSize, 0.0f,-groundSize); // right, back lower
		Vertex v3 = new Vertex( groundSize, 0.0f, groundSize); // right, front lower
		Vertex v4 = new Vertex(-groundSize, 0.0f, groundSize); // left,  front lower
		Vertex v5 = new Vertex(-groundSize, 0.5f,-groundSize); // left,  back higher
		Vertex v6 = new Vertex( groundSize,	0.5f,-groundSize); // right, back higher
		Vertex v7 = new Vertex( groundSize, 0.5f, groundSize); // right, front higher
		Vertex v8 = new Vertex(-groundSize, 0.5f, groundSize); // left,  front higher

		// enable texturing 
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		{	
			// bind an appropriate texture
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, brickWallTexture.getTextureID());
			// draw back face of boarder
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v1.toVector(),v2.toVector(),v6.toVector(),v5.toVector()).submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v1.submit();

				GL11.glTexCoord2f(textureDensity,0.0f);
				v2.submit();

				GL11.glTexCoord2f(textureDensity,1.0f);
				v6.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v5.submit();
			}
			GL11.glEnd();
			// draw right face of boarder
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v7.toVector(),v6.toVector(),v2.toVector(),v3.toVector()).submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v7.submit();

				GL11.glTexCoord2f(textureDensity,0.0f);
				v6.submit();

				GL11.glTexCoord2f(textureDensity,1.0f);
				v2.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v3.submit();
			}
			GL11.glEnd();
			// draw front face of boarder
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v8.toVector(),v7.toVector(),v3.toVector(),v4.toVector()).submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v8.submit();

				GL11.glTexCoord2f(textureDensity,0.0f);
				v7.submit();

				GL11.glTexCoord2f(textureDensity,1.0f);
				v3.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v4.submit();
			}
			GL11.glEnd();
			// draw left face of boarder
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v4.toVector(),v1.toVector(),v5.toVector(),v8.toVector()).submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v4.submit();

				GL11.glTexCoord2f(textureDensity,0.0f);
				v1.submit();

				GL11.glTexCoord2f(textureDensity,1.0f);
				v5.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v8.submit();
			}
			GL11.glEnd();
		}
		GL11.glPopMatrix();
		// disable textures
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	/**
	 * Draws the grass plane. 
	 * The grass plane uses the current OpenGL material settings for its appearance.
	 */
	private void drawUnitGrassPlane()
	{
		// determines ground plane size
		float groundSize = 50;
		Vertex v1 = new Vertex(-groundSize, 0.0f,-groundSize); // left,  back
		Vertex v2 = new Vertex( groundSize, 0.0f,-groundSize); // right, back
		Vertex v3 = new Vertex( groundSize, 0.0f, groundSize); // right, front
		Vertex v4 = new Vertex(-groundSize, 0.0f, groundSize); // left,  front

		// determines grass texture density on plane
		float textureDensity = 256;
		// draw the plane
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v4.toVector(),v3.toVector(),v2.toVector(),v1.toVector()).submit();

			GL11.glTexCoord2f(0.0f,0.0f);
			v4.submit();

			GL11.glTexCoord2f(textureDensity,0.0f);
			v3.submit();

			GL11.glTexCoord2f(textureDensity,textureDensity);
			v2.submit();

			GL11.glTexCoord2f(0.0f,textureDensity);
			v1.submit();
		}
		GL11.glEnd();
	}

	/**
	 * Draws the track plane. 
	 * The track plane uses the current OpenGL material settings for its appearance.
	 */
	private void drawUnitTrackPlane()
	{
		// determines track plane size
		float groundSize = 50;
		Vertex v1 = new Vertex(-groundSize, 0.0f,-groundSize); // left,  back
		Vertex v2 = new Vertex( groundSize, 0.0f,-groundSize); // right, back
		Vertex v3 = new Vertex( groundSize, 0.0f, groundSize); // right, front
		Vertex v4 = new Vertex(-groundSize, 0.0f, groundSize); // left,  front

		// draw the track plane
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v4.toVector(),v3.toVector(),v2.toVector(),v1.toVector()).submit();

			GL11.glTexCoord2f(0.0f,0.0f);
			v4.submit();

			GL11.glTexCoord2f(1.0f,0.0f);
			v3.submit();

			GL11.glTexCoord2f(1.0f,1.0f);
			v2.submit();

			GL11.glTexCoord2f(0.0f,1.0f);
			v1.submit();
		}
		GL11.glEnd();
	}

	/**
	 * Draws the exterior for the car, more details inside car model visio document. 
	 * The car exterior uses the current OpenGL material settings for its appearance.
	 */
	private void drawCarExterior(){
		Vertex v1 = new Vertex(-0.26f, 0.0f, -0.1075f);
		Vertex v2 = new Vertex(0.24f, 0.0f, -0.1075f);
		Vertex v3 = new Vertex(0.24f, 0.0f, 0.1075f);
		Vertex v4 = new Vertex(-0.26f, 0.0f, 0.1075f);
		Vertex v5 = new Vertex(-0.21f, 0.0f, -0.1325f);
		Vertex v6 = new Vertex(-0.21f, 0.0f, -0.1075f);
		Vertex v7 = new Vertex(0.21f, 0.0f, -0.1325f);
		Vertex v8 = new Vertex(0.21f, 0.0f, -0.1075f);
		Vertex v9 = new Vertex(0.21f, 0.0f, 0.1325f);
		Vertex v10 = new Vertex(0.21f, 0.0f, 0.1075f);
		Vertex v11 = new Vertex(-0.21f, 0.0f, 0.1325f);
		Vertex v12 = new Vertex(-0.21f, 0.0f, 0.1075f);
		Vertex v13 = new Vertex(0.09f, 0.0f, -0.1075f);
		Vertex v14 = new Vertex(0.09f, 0.0f, -0.1325f);
		Vertex v15 = new Vertex(-0.09f, 0.0f, -0.1325f);
		Vertex v16 = new Vertex(-0.09f, 0.0f, -0.1075f);
		Vertex v17 = new Vertex(0.09f, 0.0f, 0.1075f);
		Vertex v18 = new Vertex(0.09f, 0.0f, 0.1325f);
		Vertex v19 = new Vertex(-0.09f, 0.0f, 0.1325f);
		Vertex v20 = new Vertex(-0.09f, 0.0f, 0.1075f);
		Vertex v21 = new Vertex(-0.244f, 0.0f,  0.115f);
		Vertex v22 = new Vertex(-0.244f, 0.05f,  0.115f);
		Vertex v23 = new Vertex(-0.26f, 0.05f, 0.11f);
		Vertex v24 = new Vertex(-0.27f, 0.05f, 0.1075f);
		Vertex v25 = new Vertex(-0.244f, 0.06f,  0.115f);
		Vertex v26 = new Vertex(-0.21f, 0.035f, 0.1325f);
		Vertex v27 = new Vertex(-0.21f, 0.06f, 0.1325f);
		Vertex v28 = new Vertex(-0.21f, 0.07f, 0.1325f);
		Vertex v29 = new Vertex(-0.17f, 0.07f, 0.1325f);
		Vertex v30 = new Vertex(-0.09f, 0.07f, 0.1325f);
		Vertex v31 = new Vertex(-0.09f, 0.035f, 0.1325f);
		Vertex v32 = new Vertex(0.09f, 0.07f, 0.1325f);
		Vertex v33 = new Vertex(0.09f, 0.035f, 0.1325f);
		Vertex v34 = new Vertex(0.13f, 0.07f, 0.1325f);
		Vertex v35 = new Vertex(0.21f, 0.07f, 0.1325f);
		Vertex v36 = new Vertex(0.24f, 0.07f, 0.1f);
		Vertex v37 = new Vertex(0.21f, 0.08f, 0.12f);
		Vertex v38 = new Vertex(0.21f, 0.085f, 0.1f);
		Vertex v39 = new Vertex(0.04f, 0.12f, 0.06f);
		Vertex v40 = new Vertex(0.14f, 0.085f, 0.1f);
		Vertex v41 = new Vertex(0.17f, 0.07f, 0.1325f);
		Vertex v42 = new Vertex(0.21f, 0.035f, 0.1325f);
		Vertex v43 = new Vertex(-0.21f, 0.08f, 0.12f);
		Vertex v44 = new Vertex(-0.13f, 0.07f, 0.1325f);
		Vertex v45 = new Vertex(-0.12f, 0.085f, 0.1f);
		Vertex v46 = new Vertex(0.21f, 0.07f, 0.1325f);
		Vertex v47 = new Vertex(-0.05f, 0.12f, 0.06f);
		Vertex v48 = new Vertex(-0.244f, 0.0f,  -0.115f);
		Vertex v49 = new Vertex(-0.244f, 0.05f,  -0.115f);
		Vertex v50 = new Vertex(-0.26f, 0.05f, -0.11f);
		Vertex v51 = new Vertex(-0.27f, 0.05f, -0.1075f);
		Vertex v52 = new Vertex(-0.244f, 0.06f,  -0.115f);
		Vertex v53 = new Vertex(-0.21f, 0.035f, -0.1325f);
		Vertex v54 = new Vertex(-0.21f, 0.06f, -0.1325f);
		Vertex v55 = new Vertex(-0.21f, 0.07f, -0.1325f);
		Vertex v56 = new Vertex(-0.17f, 0.07f, -0.1325f);
		Vertex v57 = new Vertex(-0.09f, 0.07f, -0.1325f);
		Vertex v58 = new Vertex(-0.09f, 0.035f, -0.1325f);
		Vertex v59 = new Vertex(0.09f, 0.07f, -0.1325f);
		Vertex v60 = new Vertex(0.09f, 0.035f, -0.1325f);
		Vertex v61 = new Vertex(0.13f, 0.07f, -0.1325f);
		Vertex v62 = new Vertex(0.21f, 0.07f, -0.1325f);
		Vertex v63 = new Vertex(0.24f, 0.07f, -0.1f);
		Vertex v64 = new Vertex(0.21f, 0.08f, -0.12f);
		Vertex v65 = new Vertex(0.21f, 0.085f, -0.1f);
		Vertex v66 = new Vertex(0.04f, 0.12f, -0.06f);
		Vertex v67 = new Vertex(0.14f, 0.085f, -0.1f);
		Vertex v68 = new Vertex(0.17f, 0.07f, -0.1325f);
		Vertex v69 = new Vertex(0.21f, 0.035f,-0.1325f);
		Vertex v70 = new Vertex(-0.21f, 0.08f, -0.12f);
		Vertex v71 = new Vertex(-0.13f, 0.07f, -0.1325f);
		Vertex v72 = new Vertex(-0.12f, 0.085f, -0.1f);
		Vertex v73 = new Vertex(0.21f, 0.07f, -0.1325f);
		Vertex v74 = new Vertex(-0.05f, 0.12f, -0.06f);
		Vertex v75 = new Vertex(-0.21f, 0.07f, 0.1075f);
		Vertex v76 = new Vertex(-0.09f, 0.07f, 0.1075f);
		Vertex v77 = new Vertex(0.21f, 0.07f, 0.1075f);
		Vertex v78 = new Vertex(0.09f, 0.07f, 0.1075f);
		Vertex v79 = new Vertex(-0.21f, 0.07f, -0.1075f);
		Vertex v80 = new Vertex(-0.09f, 0.07f, -0.1075f);
		Vertex v81= new Vertex(0.21f, 0.07f, -0.1075f);
		Vertex v82 = new Vertex(0.09f, 0.07f, -0.1075f);

		GL11.glPushMatrix();
		{	
			// enable texturing and bind appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, redTexture.getTextureID());

			// DRAW BASE OF CAR
			GL11.glPushMatrix();
			{
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v1.toVector(),v2.toVector(),v3.toVector(),v4.toVector()).submit();

						v1.submit();
						v2.submit();
						v3.submit();
						v4.submit();
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v6.toVector(),v1.toVector(),v5.toVector()).submit();
						v6.submit();
						v1.submit();
						v5.submit();
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v7.toVector(),v2.toVector(),v8.toVector()).submit();

						v7.submit();
						v2.submit();
						v8.submit();
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v10.toVector(),v3.toVector(),v9.toVector()).submit();

						v10.submit();
						v3.submit();
						v9.submit();
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v11.toVector(),v4.toVector(),v12.toVector()).submit();

						v11.submit();
						v4.submit();
						v12.submit();
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v14.toVector(),v13.toVector(),v16.toVector(),v15.toVector()).submit();

						v14.submit();
						v13.submit();
						v16.submit();
						v15.submit();
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v17.toVector(),v18.toVector(),v19.toVector(),v20.toVector()).submit();

						v17.submit();
						v18.submit();
						v19.submit();
						v20.submit();
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
			}
			GL11.glPopMatrix();


			// DRAW LEFT SIDE OF CAR
			GL11.glPushMatrix();
			{
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v4.toVector(),v21.toVector(),v22.toVector(),v23.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v4.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v21.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v22.submit();
						GL11.glTexCoord2f(0.0f,1.0f);
						v23.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v21.toVector(),v11.toVector(),v27.toVector(),v25.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v21.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v11.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v27.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v25.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v4.toVector(),v23.toVector(),v24.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v4.submit();    
						GL11.glTexCoord2f(1.0f,0.0f);
						v23.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v24.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v28.toVector(),v26.toVector(),v29.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v28.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v26.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v29.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v24.toVector(),v22.toVector(),v25.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v24.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v22.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v25.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v28.toVector(),v25.toVector(),v27.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v28.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v25.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v27.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v30.toVector(),v44.toVector(),v31.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v30.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v44.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v31.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v30.toVector(),v19.toVector(),v18.toVector(),v32.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v30.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v19.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v18.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v32.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v32.toVector(),v33.toVector(),v34.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v32.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v33.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v34.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v41.toVector(),v42.toVector(),v35.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v41.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v42.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v35.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v35.toVector(),v9.toVector(),v3.toVector(),v36.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v35.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v9.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v3.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v36.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v43.toVector(),v28.toVector(),v46.toVector(),v37.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v43.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v28.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v46.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v37.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v45.toVector(),v43.toVector(),v37.toVector(),v38.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v45.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v43.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v37.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v38.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					// bind appropriate texture
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, sideWindowTexture.getTextureID());
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v47.toVector(),v45.toVector(),v40.toVector(),v39.toVector()).submit();	            
						GL11.glTexCoord2f(0.22f,0.18f);//tl	
						v47.submit();  
						GL11.glTexCoord2f(0.4f,0.0f);//bl
						v45.submit();  
						GL11.glTexCoord2f(0.36f,0.66f);//br
						v40.submit(); 
						GL11.glTexCoord2f(0.22f,0.44f);//tr
						v39.submit();				
					}
					GL11.glEnd();
					// rebind original metal texture
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, redTexture.getTextureID());
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v43.toVector(),v25.toVector(),v28.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v43.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v25.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v28.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v36.toVector(),v37.toVector(),v46.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v36.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v37.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v46.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v37.toVector(),v36.toVector(),v38.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v37.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v36.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v38.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v12.toVector(),v20.toVector(),v76.toVector(),v75.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v12.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v20.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v76.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v75.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v11.toVector(),v12.toVector(),v75.toVector(),v28.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v11.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v12.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v75.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v28.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v20.toVector(),v19.toVector(),v30.toVector(),v76.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v20.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v19.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v30.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v76.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v30.toVector(),v28.toVector(),v75.toVector(),v76.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v30.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v28.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v75.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v76.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v10.toVector(),v17.toVector(),v77.toVector(),v76.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v17.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v10.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v77.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v78.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v10.toVector(),v9.toVector(),v35.toVector(),v77.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v10.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v9.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v35.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v77.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v18.toVector(),v17.toVector(),v78.toVector(),v32.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v18.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v17.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v78.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v32.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v35.toVector(),v32.toVector(),v78.toVector(),v77.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v35.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v32.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v78.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v77.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
			}
			GL11.glPopMatrix();


			// DRAW RIGHT SIDE OF CAR

			GL11.glPushMatrix();
			{
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v48.toVector(),v1.toVector(),v50.toVector(),v49.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v48.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v1.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v50.submit();
						GL11.glTexCoord2f(0.0f,1.0f);
						v49.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v5.toVector(),v48.toVector(),v52.toVector(),v54.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v5.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v48.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v52.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v54.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v50.toVector(),v1.toVector(),v51.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v50.submit();    
						GL11.glTexCoord2f(1.0f,0.0f);
						v1.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v51.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v53.toVector(),v55.toVector(),v56.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v53.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v55.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v56.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v49.toVector(),v51.toVector(),v52.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v49.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v51.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v52.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v52.toVector(),v55.toVector(),v54.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v52.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v55.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v54.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v71.toVector(),v57.toVector(),v58.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v71.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v57.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v58.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v14.toVector(),v15.toVector(),v57.toVector(),v59.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v14.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v15.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v57.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v59.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v60.toVector(),v59.toVector(),v61.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v60.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v59.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v61.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v69.toVector(),v68.toVector(),v62.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v69.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v68.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v62.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v7.toVector(),v62.toVector(),v63.toVector(),v2.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v7.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v62.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v63.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v2.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v55.toVector(),v70.toVector(),v64.toVector(),v73.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v55.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v70.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v64.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v73.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v70.toVector(),v72.toVector(),v65.toVector(),v64.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v70.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v72.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v65.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v64.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					// bind appropriate texture
					GL11.glBindTexture(GL11.GL_TEXTURE_2D,sideWindowTexture.getTextureID());
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v72.toVector(),v74.toVector(),v66.toVector(),v67.toVector()).submit();	            
						GL11.glTexCoord2f(0.4f,0.0f);//bl
						v72.submit();  
						GL11.glTexCoord2f(0.22f,0.18f);//tl	
						v74.submit();  
						GL11.glTexCoord2f(0.22f,0.44f);//tr
						v66.submit(); 
						GL11.glTexCoord2f(0.36f,0.66f);//br
						v67.submit();				
					}
					GL11.glEnd();
					// rebind original metal texture
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, redTexture.getTextureID());
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v52.toVector(),v70.toVector(),v55.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v52.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v70.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v55.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v64.toVector(),v63.toVector(),v73.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v64.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v63.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v73.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_TRIANGLES);
					{
						new Normal(v63.toVector(),v64.toVector(),v65.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v63.submit();
						GL11.glTexCoord2f(1.0f,0.0f);
						v64.submit();
						GL11.glTexCoord2f(1.0f,1.0f);
						v65.submit();
						GL11.glTexCoord2f(0.0f,1.0f);				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v16.toVector(),v1.toVector(),v79.toVector(),v80.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v16.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v1.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v79.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v80.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v6.toVector(),v5.toVector(),v55.toVector(),v79.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v6.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v5.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v55.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v79.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v15.toVector(),v16.toVector(),v80.toVector(),v57.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v15.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v16.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v80.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v57.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v55.toVector(),v57.toVector(),v80.toVector(),v79.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v55.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v57.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v80.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v79.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v8.toVector(),v13.toVector(),v82.toVector(),v81.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v8.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v13.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v82.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v81.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v7.toVector(),v8.toVector(),v81.toVector(),v62.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v7.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v8.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v81.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v62.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v13.toVector(),v14.toVector(),v59.toVector(),v82.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v13.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v14.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v59.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v82.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v59.toVector(),v62.toVector(),v81.toVector(),v82.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v59.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v62.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v81.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v82.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
			}
			GL11.glPopMatrix();

			// DRAW ROOF OF CAR
			GL11.glPushMatrix();
			{
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v4.toVector(),v24.toVector(),v51.toVector(),v1.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v4.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v24.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v51.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v1.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v52.toVector(),v51.toVector(),v24.toVector(),v25.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v52.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v51.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v24.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v25.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{	
					// bind appropriate texture
					GL11.glBindTexture(GL11.GL_TEXTURE_2D,frontBonnetTexture.getTextureID());
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v70.toVector(),v52.toVector(),v25.toVector(),v43.toVector()).submit();	            
						GL11.glTexCoord2f(0.34f,0.0f);//tl
						v70.submit();  
						GL11.glTexCoord2f(0.42f,0.0f);//bl
						v52.submit();  
						GL11.glTexCoord2f(0.42f,0.7f);//br
						v25.submit(); 
						GL11.glTexCoord2f(0.34f,0.7f);//tr
						v43.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{	
					// bind appropriate texture
					GL11.glBindTexture(GL11.GL_TEXTURE_2D,frontBonnetTexture.getTextureID());
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v72.toVector(),v70.toVector(),v43.toVector(),v45.toVector()).submit();	            
						GL11.glTexCoord2f(0.25f,0.02f);//tl
						v72.submit();  
						GL11.glTexCoord2f(0.3f,0.0f);//bl
						v70.submit();  
						GL11.glTexCoord2f(0.3f,0.68f);//br
						v43.submit(); 
						GL11.glTexCoord2f(0.25f,0.68f);//tr
						v45.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{	
					// bind appropriate texture
					GL11.glBindTexture(GL11.GL_TEXTURE_2D,frontWindScreenTexture.getTextureID());
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v74.toVector(),v72.toVector(),v45.toVector(),v47.toVector()).submit();	            
						GL11.glTexCoord2f(0.35f,0.15f);
						v74.submit();  
						GL11.glTexCoord2f(0.5f,0.0f);
						v72.submit();  
						GL11.glTexCoord2f(0.5f,1.0f);
						v45.submit(); 
						GL11.glTexCoord2f(0.35f,0.85f);
						v47.submit();				
					}
					GL11.glEnd();
					// rebind original metal texture
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, redTexture.getTextureID());
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v66.toVector(),v74.toVector(),v47.toVector(),v39.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v66.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v74.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v47.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v39.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{	
					// bind appropriate texture
					GL11.glBindTexture(GL11.GL_TEXTURE_2D,rearWindScreenTexture.getTextureID());
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v67.toVector(),v66.toVector(),v39.toVector(),v40.toVector()).submit();	            
						GL11.glTexCoord2f(0.5f,0.05f);//br
						v67.submit();  
						GL11.glTexCoord2f(0.6f,0.1f);//tr
						v66.submit();  
						GL11.glTexCoord2f(0.6f,0.5f);//tl
						v39.submit(); 
						GL11.glTexCoord2f(0.5f,0.55f);//bl
						v40.submit();				
					}
					GL11.glEnd();
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, redTexture.getTextureID());
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{	
					// bind appropriate texture
					GL11.glBindTexture(GL11.GL_TEXTURE_2D,rearBonnetTexture.getTextureID());
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v65.toVector(),v67.toVector(),v40.toVector(),v38.toVector()).submit();	            
						GL11.glTexCoord2f(0.58f,0.12f);//br
						v65.submit();  
						GL11.glTexCoord2f(0.67f,0.17f);//tr
						v67.submit();  
						GL11.glTexCoord2f(0.67f,0.5f);//tl
						v40.submit(); 
						GL11.glTexCoord2f(0.58f,0.55f);//bl
						v38.submit();				
					}
					GL11.glEnd();
					// rebind original metal texture
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, redTexture.getTextureID());
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v38.toVector(),v36.toVector(),v63.toVector(),v65.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v38.submit();  
						GL11.glTexCoord2f(1.0f,0.0f);
						v36.submit();  
						GL11.glTexCoord2f(1.0f,1.0f);
						v63.submit(); 
						GL11.glTexCoord2f(0.0f,1.0f);
						v65.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{	
					//bind appropriate texture
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, carBootTexture.getTextureID());
					GL11.glBegin(GL11.GL_POLYGON);
					{
						new Normal(v3.toVector(),v2.toVector(),v63.toVector(),v36.toVector()).submit();	            
						GL11.glTexCoord2f(0.0f,0.0f);
						v3.submit();  
						GL11.glTexCoord2f(0.71f,0.0f);
						v2.submit();  
						GL11.glTexCoord2f(0.71f,0.59f);
						v63.submit(); 
						GL11.glTexCoord2f(0.0f,0.59f);
						v36.submit();				
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
			}
			GL11.glPopMatrix();
			//disable textures
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
		GL11.glPopMatrix();
	}

	/**
	 * Draws the wing mirrors for the car. 
	 * The car wing mirror uses the current OpenGL material settings for its appearance.
	 */
	private void drawWingMirror(){
		// defines the height, width and depth of the wing mirror cube
		float cubeWidth = 0.02f;
		float cubeDepth = 0.002f;
		float cubeHeight = 0.01f;

		Vertex v1 = new Vertex(-cubeWidth, -cubeHeight,-cubeDepth); // left,  back bottom
		Vertex v2 = new Vertex( cubeWidth, -cubeHeight,-cubeDepth); // right, back bottom
		Vertex v3 = new Vertex( cubeWidth, -cubeHeight, cubeDepth); // right, front bottom
		Vertex v4 = new Vertex(-cubeWidth, -cubeHeight, cubeDepth); // left,  front bottom
		Vertex v5 = new Vertex(-cubeWidth, cubeHeight,-cubeDepth); // left,  back top
		Vertex v6 = new Vertex( cubeWidth, cubeHeight,-cubeDepth); // right, back top
		Vertex v7 = new Vertex( cubeWidth, cubeHeight, cubeDepth); // right, front top
		Vertex v8 = new Vertex(-cubeWidth, cubeHeight, cubeDepth); // left,  front top


		// enable texturing and bind an appropriate texture
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, redTexture.getTextureID());
		GL11.glPushMatrix();
		{
			//draws top of wing mirror
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v1.toVector(),v2.toVector(),v3.toVector(),v4.toVector()).submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v1.submit();

				GL11.glTexCoord2f(1.0f,0.0f);
				v2.submit();

				GL11.glTexCoord2f(1.0f,1.0f);
				v3.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v4.submit();
			}
			GL11.glEnd();
			// bind appropriate texture
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, blackTexture.getTextureID());
			//draws front of wing mirror
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v2.toVector(),v1.toVector(),v5.toVector(),v6.toVector()).submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v2.submit();

				GL11.glTexCoord2f(1.0f,0.0f);
				v1.submit();

				GL11.glTexCoord2f(1.0f,1.0f);
				v5.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v6.submit();
			}
			GL11.glEnd();
			// rebind original texture
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, redTexture.getTextureID());
			//draws right of wing mirror
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v2.toVector(),v6.toVector(),v7.toVector(),v3.toVector()).submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v2.submit();

				GL11.glTexCoord2f(1.0f,0.0f);
				v6.submit();

				GL11.glTexCoord2f(1.0f,1.0f);
				v7.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v3.submit();
			}
			GL11.glEnd();
			//draws back of wing mirror
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v3.toVector(),v7.toVector(),v8.toVector(),v4.toVector()).submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v3.submit();

				GL11.glTexCoord2f(1.0f,0.0f);
				v7.submit();

				GL11.glTexCoord2f(1.0f,1.0f);
				v8.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v4.submit();
			}
			GL11.glEnd();
			//draws left of wing mirror
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v1.toVector(),v4.toVector(),v8.toVector(),v5.toVector()).submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v1.submit();

				GL11.glTexCoord2f(1.0f,0.0f);
				v4.submit();

				GL11.glTexCoord2f(1.0f,1.0f);
				v8.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v5.submit();
			}
			GL11.glEnd();
			//draws bottom of wing mirror
			GL11.glBegin(GL11.GL_POLYGON);
			{
				new Normal(v6.toVector(),v5.toVector(),v8.toVector(),v7.toVector()).submit();

				GL11.glTexCoord2f(0.0f,0.0f);
				v6.submit();

				GL11.glTexCoord2f(1.0f,0.0f);
				v5.submit();

				GL11.glTexCoord2f(1.0f,1.0f);
				v8.submit();

				GL11.glTexCoord2f(0.0f,1.0f);
				v7.submit();
			}
			GL11.glEnd();
			// disable texturing
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
		GL11.glPopMatrix();
	}

	/**
	 * Draws the car wheel for the car. 
	 * The car wheel uses the current OpenGL material settings for its appearance.
	 */
	private void drawWheel(){
		GL11.glPushMatrix();
		{
			// enable texturing
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			// create quad models
			Disk backDisk = new Disk();
			Cylinder tyreCylinder = new Cylinder();
			Disk frontDisk = new Disk();

			// setup front facing disc
			GL11.glPushMatrix();
			{
				// bind relevant texture
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,rimTexture.getTextureID());
				// translate and rotate disc into position
				GL11.glTranslatef(0.0f, 0f, -0.0125f);
				GL11.glRotatef(180f, 0.0f, 1.0f, 0.0f);
				// enable texturing on quad
				frontDisk.setTextureFlag(true);
				frontDisk.draw(0f, 0.05f, 50, 10);
			}
			GL11.glPopMatrix();

			// setup cylinder
			GL11.glPushMatrix();
			{	
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,tyreTexture.getTextureID());  
				// translates cylinder into position
				GL11.glTranslatef(0.0f, 0f, -0.0125f);
				// enable texturing on quad
				tyreCylinder.setTextureFlag(true);
				tyreCylinder.draw(0.05f, 0.05f, 0.025f, 50, 1);
			}
			GL11.glPopMatrix();

			//setup back disc
			GL11.glPushMatrix();
			{	
				// bind relevant texture
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,rimTexture.getTextureID());
				// translates disc into position
				GL11.glTranslatef(0.0f, 0f, 0.0125f);
				// enable texturing on quad
				backDisk.setTextureFlag(true);
				backDisk.draw(0f, 0.05f, 50, 10);
			}
			GL11.glPopMatrix();
			// disable textures
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
		GL11.glPopMatrix();
	}

}