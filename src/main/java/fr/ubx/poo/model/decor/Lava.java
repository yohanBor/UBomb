/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

public class Lava extends Decor {

	public boolean canWalk(Player player, Position nextPos) {
		return true;
	}

	@Override
	public boolean MonsterCanMove() {
		return false;
	}

	@Override
	public boolean canExplose(Bomb bomb, Position pos) {
		return false;
	}

	@Override
	/**
	 * enlève une vie au joueur et ne l'immunise pas
	 */
	public void action(Game game) {
		Player p = game.getPlayer();
		p.setLives(p.getLives() - 1);
	}
	
	@Override
	public String toString() {
		return "Lava";
	}


}
