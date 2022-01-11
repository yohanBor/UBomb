/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

public class DoorNextClosed extends Decor {

	public boolean MonsterCanMove() {
		return false;
	}

	public boolean canWalk(Player player, Position nextPos) {
		return true;
	}

	public void action(Game game) {}

	@Override
	public boolean canExplose(Bomb bomb, Position pos) {
		return false;
	}

	public void open(Game game)
	{
		Player p = game.getPlayer();
		if(p.getKeys() > 0)
        {
            p.setKeys(p.getKeys() - 1); //on perd la clé
			game.getWorld().clear(p.getPosition());
			game.getWorld().set(p.getPosition(), new DoorOpened());
			game.saveMap();
			game.setGameLevel(Game.getGameLevel() + 1);
			game.set_render(true); 
			game.setRestart_level(1);
		}
	}
	@Override
	public String toString() {
		return "DoorNextClosed";
	}

}
