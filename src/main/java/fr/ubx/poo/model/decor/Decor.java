/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

/***
 * A decor is an element that does not know its own position in the grid.
 */
public abstract class Decor extends Entity {
	
	/**	Le monstre peut-il aller sur ce decor ?
     * @return true si le monstre peut marcher sur le décor, false sinon
     */
	public abstract boolean MonsterCanMove();
	
	/** Le joueur peut-il aller sur ce decor ?
	 * @param player le joueur
	 * @param nextPos la position vers laquelle le joueur veut se diriger
     * @return true si le joueur peut marcher sur le décor, false sinon
     */
	public abstract boolean canWalk(Player player, Position nextPos);
	
	/** Le décor est-il explosable ? 
	 * @param bomb la bombe donnée en paramètre
	 * @param pos, la position du décor à examiner
     * @return true si le décor peut exploser, false sinon
     */
	public abstract boolean canExplose(Bomb bomb, Position pos);
	
	/** Spécificité du décor = polymorphisme. Exemple, pour un "heart" --> agumenter de 1 la vie du joueur
	 * @param game le jeu en cours
     */
	public abstract void action(Game game);
	
	/** Requêtes d'ouverture (porte ou coffre)
	 * @param game le jeu en cours
     */
	public void open(Game game){
	}
		


}
