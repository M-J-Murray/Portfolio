package Entities;

import java.util.ArrayList;
import java.util.List;

public class Universe {
	private List<Base> units = new ArrayList<Base>();
	private final static float GRAVITATIONAL_CONSTANT = (float) (11.674 * Math.pow(10, -8));

	public Universe(){		
	}

	public void updateUniverse(Player player){
		for (int i = 0; i < units.size(); i++) {
			units.get(i).update();
		}
		player.getCharacter().update();
		detectCollision(player);
	}

	public void addUnit(Base unit){
		unit.setUnitID(units.size());
		this.units.add(unit);
	}

	public void removeUnit(int unitID){
		this.units.remove(unitID);
	}

	public List<Unit> getUniverse(){
		List<Unit> temp = new ArrayList<Unit>();
		for (int i = 0; i < units.size(); i++) {
			temp.add((Unit) units.get(i));
		}
		return temp;
	}

	

	public void detectCollision(Player player){
		Unit currentUnit;
		Unit tempUnit;
		for (int i = 0; i < units.size()+1; i++) {
			if (i == units.size()){
				currentUnit = (Unit) player.getCharacter();
			} else {
				currentUnit = (Unit) units.get(i);
			}
			for (int j = 0; j < units.size()+1; j++) {
				if (i != j){
					if (j == units.size()){
						tempUnit = (Unit) player.getCharacter();
					} else {
						tempUnit = (Unit) units.get(j);
					}
					currentUnit.checkCollision(tempUnit, GRAVITATIONAL_CONSTANT);
				}
			}
		}
	}
	
}
