package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import toolbox.Maths;
import Entities.Unit;
import Shaders.StaticShader;
import Textures.ModelTexture;

public class UnitRenderer {
	
	private StaticShader shader;
	
	public UnitRenderer(StaticShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	
	
	public void render (Map<TexturedModel, List<Unit>> units){
		for (TexturedModel model: units.keySet()) {
			prepareTexturedModel(model);
			List<Unit> batch = units.get(model);
			for (Unit unit: batch){
				prepareInstance(unit);
				GL11.glDrawElements(GL11.GL_TRIANGLES,  model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
		
	}
	
	public void prepareTexturedModel(TexturedModel model){
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture =  model.getTexture();
		if (texture.isHasTransparency()){
			MasterRenderer.disableCulling();
		}
		shader.loadIsLight(texture.isLight());
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	public void unbindTexturedModel(){
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	public void prepareInstance(Unit unit){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(unit.getPosition(), unit.getAngle().x, unit.getAngle().y, unit.getAngle().z, unit.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
	

}
