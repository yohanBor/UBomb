/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.Entity;

/***
 * A GameObject can access the game and knows its position in the grid.
 */
public abstract class GameObject extends Entity
{
    protected final Game game;
    private Position position;

    // -------------------------  CONSTRUCTEUR -------------------------

    public GameObject(Game game, Position position)
    {
        this.game = game;
        this.position = position;
    }

    public void clearDecor(Position pos)
    {
         game.getWorld().clear(pos);
         game.set_render(true);
    }
    
    // -------------------------  GETTER -------------------------

    public Position getPosition() {
        return position;
    }

    // -------------------------  SETTER -------------------------

    public void setPosition(Position position) {
        this.position = position;
    }

    

    
}
