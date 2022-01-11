/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

public class Box extends Decor {

	public boolean canExplose(Bomb bomb, Position pos) {
		bomb.clearDecor(pos);
		return false;
	}
	
    public boolean MonsterCanMove() {
		return false;
	}

	@Override
	public void action(Game game) {
		Player p = game.getPlayer();
		Position nextPos= p.getDirection().nextPosition(p.getPosition());
		Position nextPosBox = p.getDirection().nextPosition(nextPos);
		// On crée une box en GO afin de pouvoir utiliser des methodes depuis player plus facilement
		p.CreateBoxGO(game, nextPos, nextPosBox);
	}

	@Override
	public boolean canWalk(Player player, Position nextPos) {
		return false;
	}
	
    public String toString() {
        return "Box";
    }
	
}
