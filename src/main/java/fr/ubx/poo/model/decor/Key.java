/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

public class Key extends Decor {

	public boolean MonsterCanMove() {
		return true;
	}

	public boolean canWalk(Player player, Position nextPos) {
		return true;
	}

	@Override
	public boolean canExplose(Bomb bomb, Position pos) {
		return false;
	}

	@Override
	public void action(Game game) {
		Player p = game.getPlayer();
		Position nextPos= p.getDirection().nextPosition(p.getPosition());
		p.setKeys(p.getKeys() + 1);
        p.clearDecor(nextPos);
	}
	
	@Override
	public String toString() {
		return "Key";
	}

}
