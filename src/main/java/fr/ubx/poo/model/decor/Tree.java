/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

public class Tree extends Decor {

	public boolean MonsterCanMove() {
		return false;
	}


	public boolean canWalk(Player player, Position nextPos) {
		return false;
	}
	@Override
	public boolean canExplose(Bomb bomb, Position pos) {
		return false;
	}

	@Override
	public void action(Game game) {}
	
	@Override
	public String toString() {
		return "Tree";
	}

}
