package renderEngine;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;

import terrains.Terrain;
import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Entities.Player;
import Textures.ModelTexture;
import Textures.TerrainTexture;
import Textures.TerrainTexturePack;

public class MainGameLoop {
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		//************ Define Textures *************
		int grassID = loader.loadTexture("grass");
		int dirtID = loader.loadTexture("dirt");
		int flowersID = loader.loadTexture("flowers");
		int pathID = loader.loadTexture("path");
		int woodID = loader.loadTexture("wood");
		int blankID = loader.loadTexture("blank");
		//**************Terrain Texture Stuff**********************************
		
		TerrainTexture backgroundTexture = new TerrainTexture(grassID);
		TerrainTexture rTexture = new TerrainTexture(dirtID);
		TerrainTexture gTexture = new TerrainTexture(flowersID);
		TerrainTexture bTexture = new TerrainTexture(pathID);
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		//**********************************************************************
		ModelTexture woodTexture= new ModelTexture(woodID);
		woodTexture.setUseFakeLighting(true);
		woodTexture.setHasTransparency(true);
		ModelTexture lightTexture= new ModelTexture(blankID);
		lightTexture.setIsLight(true);
		lightTexture.setReflectivity(100);
		lightTexture.setShineDamper(0);
		
		TexturedModel cube = new TexturedModel(OBJLoader.loadObjModel("cube", loader), woodTexture);		
		TexturedModel sphere = new TexturedModel(OBJLoader.loadObjModel("sphere", loader), lightTexture);
		
		Terrain terrain = new Terrain(-0.5f, -0.5f, loader, texturePack, blendMap, "heightMap");

		List<Entity> entities = new ArrayList<Entity>();
		entities.add(new Entity(sphere, new Vector3f(-10,terrain.getHeightOfTerrain(-10,1)+10, 10),0,0,0,1));
		entities.add(new Entity(sphere, new Vector3f(10,terrain.getHeightOfTerrain(10,10)+10, 10),0,0,0,1));
		entities.add(new Entity(sphere, new Vector3f(0,terrain.getHeightOfTerrain(0,20)+10,20),0,0,0,1));
		Light light1 = new Light(new Vector3f(0.5f, 0.001f, 0.001f));
		Light light2 = new Light(new Vector3f(0.5f, 0.001f, 0.001f));
		Light light3 = new Light(new Vector3f(0.5f, 0.001f, 0.001f));
		light1.setColour(new Vector3f(1, 0, 0));
		entities.get(0).setLight(light1);
		light2.setColour(new Vector3f(0, 0, 1));
		entities.get(1).setLight(light2);
		light3.setColour(new Vector3f(0, 1, 0));
		entities.get(2).setLight(light3);
		
		MasterRenderer renderer = new MasterRenderer();
		
		Player player = new Player(cube, new Vector3f(0, 1, 0), 0, 0, 0, 1);
		Camera camera = new Camera(player);
		
		List<Light> lights = new ArrayList<Light>();
		for (int i = 0; i < entities.size(); i++){
			if (entities.get(i).getLight() != null){
				lights.add(entities.get(i).getLight());
			}
		}
		
		while (!Display.isCloseRequested()){
			player.move(terrain);
			camera.move();
			renderer.processTerrain(terrain);
			renderer.processEntity(player);
			
			for (int i = 0; i < entities.size(); i++) {
				entities.get(i).increaseRotation(0, 0.1f, 0);
				renderer.processEntity(entities.get(i));
			}
			renderer.render(lights, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
