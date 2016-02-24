package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import Entities.Camera;
import Entities.Light;
import Entities.Unit;
import Shaders.StaticShader;

public class MasterRenderer {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private static final float RED = 0;
	private static final float GREEN = 0;
	private static final float BLUE = 0;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private UnitRenderer renderer;
	
	private Map<TexturedModel, List<Unit>> entities = new HashMap<TexturedModel, List<Unit>>();
	
	public MasterRenderer(){
		enableCulling();
		createProjectionMatrix();
		renderer = new UnitRenderer(shader, projectionMatrix);
	}
	
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void render (List<Light> lights, Camera camera){
		prepare();
		shader.start();
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear();
	}
	
	
	public void processUnit(Unit unit){
		TexturedModel unitModel = unit.getModel();
		List<Unit> batch = entities.get(unitModel);
		if (batch != null){
			batch.add(unit);
		} else {
			List<Unit> newBatch = new ArrayList<Unit>();
			newBatch.add(unit);
			entities.put(unit.getModel(), newBatch);
		}
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
	}
	
	private void createProjectionMatrix(){
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((NEAR_PLANE+FAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2*NEAR_PLANE*FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
}
