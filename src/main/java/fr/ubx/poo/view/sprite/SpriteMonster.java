/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpriteMonster extends SpriteGameObject
{
    public SpriteMonster(Pane layer, Monster monster) {
        super(layer, null, monster);
        updateImage();
    }

    @Override
    /**
     * met à jour le sprite du monstre en fonction de sa direction
     */
    public void updateImage() {
        Monster monster = (Monster) go; // cast le game object sur Monster
        setImage(ImageFactory.getInstance().getMonster(monster.getDirection())); // utilisé dans sprite.java --> render()
    }

}
