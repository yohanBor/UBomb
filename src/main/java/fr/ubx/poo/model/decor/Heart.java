/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

public class Heart extends Decor {
	
	public boolean MonsterCanMove() {
		return true;
	}
	
	public boolean canExplose(Bomb bomb, Position pos) {
		bomb.clearDecor(pos);
		return true;
	}

	public boolean canWalk(Player player, Position nextPos) {
		return true;
	}

	@Override
	public void action(Game game) {
		Player p = game.getPlayer();
		Position nextPos= p.getDirection().nextPosition(p.getPosition());
		p.setLives(p.getLives() + 1);
        p.clearDecor(nextPos);
	}
	
	@Override
    public String toString() {
        return "Heart";
    }
    
}
