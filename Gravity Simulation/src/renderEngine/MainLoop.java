package renderEngine;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;

import Textures.ModelTexture;
import Entities.Base;
import Entities.Camera;
import Entities.Light;
import Entities.Player;
import Entities.Unit;
import Entities.Universe;

public class MainLoop {
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		ModelTexture unitTexture = new ModelTexture(loader.loadTexture("blank"));
		unitTexture.setReflectivity(1);
		unitTexture.setShineDamper(10);
		RawModel unitModel = OBJLoader.loadObjModel("unit4", loader);	
		TexturedModel texturedUnit = new TexturedModel(unitModel, unitTexture);
		Player player = new Player(texturedUnit, new Vector3f(0, 0, 50), 0, 0, 0, 1);
		Universe universe = new Universe();
		Unit unit1;
		for (int i = -5; i < 5; i++) {
			for (int j = -5; j < 5; j++) {
				for (int k = -5; k < 5; k++) {
					unit1 = new Unit(texturedUnit, new Vector3f(5*i,5*j,5*k), new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(0,0,0), 1);	
					universe.addUnit(unit1);
				}
			}
		} 
		
		/*unit1 = new Unit(texturedUnit, new Vector3f(-2,0,0), new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(0,0,0), 1);	
		universe.addUnit(unit1);
		unit1 = new Unit(texturedUnit, new Vector3f(-6,0,0), new Vector3f(0,0,0), new Vector3f(0.02f,0,0), new Vector3f(0,0,0), 1);	
		universe.addUnit(unit1);
		unit1 = new Unit(texturedUnit, new Vector3f(2,0,0), new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(0,0,0), 1);	
		universe.addUnit(unit1);
		unit1 = new Unit(texturedUnit, new Vector3f(6,0,0), new Vector3f(0,0,0), new Vector3f(-0.02f,0,0), new Vector3f(0,0,0), 1);	
		universe.addUnit(unit1);*/
		
		MasterRenderer renderer = new MasterRenderer();
		
		Camera camera = new Camera(player);
		
		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(-100,100,0), new Vector3f(0,0,1)));
		lights.add(new Light(new Vector3f(0,100,0), new Vector3f(0,1,0)));
		lights.add(new Light(new Vector3f(100,100,0), new Vector3f(1,0,0)));
		
		List<Unit> units; 
		
		while (!Display.isCloseRequested()){
			units = universe.getUniverse();
			player.move();
			universe.updateUniverse(player);	
			camera.move();
			renderer.processUnit((Unit) player.getCharacter());
			for (Unit tempUnit: units) {
				renderer.processUnit(tempUnit);
			}
			renderer.render(lights, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
