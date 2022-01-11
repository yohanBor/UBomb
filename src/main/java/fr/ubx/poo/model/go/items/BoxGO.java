/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.items;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;

public class BoxGO extends GameObject {

	public BoxGO(final Game game, final Position position) {
		super(game, position);
	}

	/**
	 * gestion déplacement box
	 * @param positionBox la posiiton de la boîte à bouger
	 * @return true si la boite peut bouger, false sinon
	 */
    public boolean boxCanMove(Position positionBox)
    {
    	//on récupere le décor situé "derrière" la box
        Decor decor = game.getWorld().get(positionBox);
        
		//on vérifie qu'il n'y a pas de monstre sur la map
		for(Monster m : this.game.getMonsters())
			if(positionBox.equals(m.getPosition()) && m.getLevel() == Game.getGameLevel() )
				return false;
	
        // // on vérifie que la box ne sorte pas de la grille
        if (!positionBox.inside(game.getWorld().dimension)) 	
            return false;
        
        //si la case est vide
        if (decor  == null)
        	return true;
    	
    	return false;
    }
    
	  /**
	   * Si la boite peut bouger, on la déplace
	   * @param nextPosition la position où se situe la box
	   * @param nextPosBox la position qui se situe derrière la pox
	   * @param decorNext la boite à déplacer
	   */
	  public void bougerBox(Position nextPosition, Position nextPosBox, Decor decorNext) {
    	game.getWorld().clear(nextPosition); // supprime le contenu de la case courante
		game.getWorld().set(nextPosBox, decorNext); // ajoute la box sur la case courante
		game.set_render(true); // on met à jour le render
	}
	
	  /**
	   * La boîte peut-elle bouger? 
	   * @param player le joueur
	   * @param nextPos la position de la boite
	   * @param nextNext la position derrière la boite
	   * @return true si la boite peut bouger (pas d'obstacle), false sinon
	   */
    public boolean canMove(Player player, Position nextPos, Position nextNext) {
		final Decor box = game.getWorld().get(nextPos);
    	//si la box peut bouger
		if(boxCanMove(nextNext)) {
			//on la bouge
			bougerBox(nextPos, nextNext, box);
			//et on déplace le joueur
			player.setPosition(nextPos); 
		}
		
		return false;
	}

	@Override
    public String toString() {
        return "BoxGO";
	}	
	
}
