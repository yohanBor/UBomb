/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

public class Chest_opened extends Decor {

	//voir fichier "regles_du_jeu.txt" pour les ajouts personnels
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
	/**
	 * si le joueur a le look pirate, on remet le look de base, sinon on fait l'inverse
	 */
	public void action(Game game) {
		if(Player.isArmor() == true)
		{
			Player.setArmor(false);
		}
		else
			Player.setArmor(true);

		game.set_render(true); 

	}
	
	public String toString() {
		return "Chest_opened";
	}

}
