/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

public class Chest_closed extends Decor {

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
	 * on rajoute au joueur 10 bombes et on augmente de 1 la portée de tout le sac de bombes
	 */
	public void action(Game game) {
		Player p = game.getPlayer();
		Position nextPos= p.getDirection().nextPosition(p.getPosition());
		// Demande une clé pour ouvrir, celle-ci est reutilisable 
		if(p.getKeys() > 0)
            {
                game.getWorld().clear(nextPos);
                game.getWorld().set(nextPos, new Chest_opened());
                Player.setArmor(true);
				game.set_render(true);

				// Ajout de 10 bombes
				for(int cpt = 0; cpt < 10 ; cpt ++)
				{
					Bomb n = new Bomb(game, nextPos);
					n.setPorteeBombe(1);
					p.getBombEnPoche().add(n);
				} 
				p.setNombreBombes(p.getNombreBombes() + 10);

				if(p.getNombreBombes() > 0)
				{
					// Augmentation de la portée des bombes si elle n'est pas deja maximale
					int porteBombe = 4;
					if(p.getBombEnPoche().get(0).getPorteeBombe() < 4)
					{
						porteBombe = p.getBombEnPoche().get(0).getPorteeBombe() + 1;
						for(int i = 0; i < p.getNombreBombes() ; i++)
							p.getBombEnPoche().get(i).setPorteeBombe(porteBombe);
					}							
				}
			}
	}
    
  
	
	public String toString() {
		return "Chest_closed";
	}

}
