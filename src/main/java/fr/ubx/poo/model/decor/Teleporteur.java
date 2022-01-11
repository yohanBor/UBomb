/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

public class Teleporteur extends Decor {
	public boolean needRequest = true;

    
	public boolean canMove() { //polymorphisme pour éviter le instance of dans la classe player (notamment)
		return false;
	}
	
	@Override
    // ToString --> afficher pour voir quand on a des prblms
    public String toString() {
        return "Teleporteur";
    }
	
	public boolean canExplose(Bomb bomb, Position pos) {
		return false;
	}

	public boolean MonsterCanMove() {
		return false;
	}
	
	public boolean canWalk(Player player, Position nextPos) {
		return true;
	}
	
	public boolean canWalk(Player player, Game game, Position nextPos, Position nextPosBox) {
		return canWalk(player, nextPos);
	}

	@Override
	public void action(Game game) {}

	/**
	 * si on est au dernier niveau, on revient au niveau 1 (à l'endroit où la princesse se trouve)
	 */
	public void open(Game game)
	{
		game.saveMap();
		if(Game.getGameLevel() == 4)
                game.setGameLevel(1);
        else
			game.setGameLevel(4);  
			
		game.set_render(true); 	
		game.setRestart_level(10);

	}

}
