/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

public class DoorOpened extends Decor {
	public boolean needRequest = true;

	@Override

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
		game.saveMap();
		game.setGameLevel(Game.getGameLevel() + 1);
		game.set_render(true); 
		game.setRestart_level(1);
	}
	
	public String toString() {
		return "DoorOpened";
	}
}
