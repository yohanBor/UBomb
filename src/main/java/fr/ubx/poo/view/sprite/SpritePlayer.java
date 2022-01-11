/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpritePlayer extends SpriteGameObject
{
    public SpritePlayer(Pane layer, Player player) {
        super(layer, null, player);
        updateImage();
    }

    @Override
    /**
     * met à joueur le sprite de l'image en fonction de si le joueur est invincible et/ou en mode pirate
     */
    public void updateImage()
    {
        Player player = (Player) go; // cast le game object sur Player
        if (player.isInvincible()) {
        	if (Player.isArmor()) // si le joueur a changé d'apparence
        		setImage(ImageFactory.getInstance().getPirateInvi(player.getDirection()));
        	else
        		setImage(ImageFactory.getInstance().getPlayerInvi(player.getDirection()));
        }
        else {
        	setImage(ImageFactory.getInstance().getPlayer(player.getDirection())); // utilisé dans sprite.java --> render()
        }
    }
}
