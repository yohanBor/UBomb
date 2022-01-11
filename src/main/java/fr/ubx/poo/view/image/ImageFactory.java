/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.image;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.character.Player;
import javafx.scene.image.Image;

import static fr.ubx.poo.view.image.ImageResource.*;

public final class ImageFactory
{
    private final Image[] images;

    // PLAYER
    private final ImageResource[] directions = new ImageResource[]{
            // Direction { N, E, S, W }
            PLAYER_UP, PLAYER_RIGHT, PLAYER_DOWN, PLAYER_LEFT,
    };
    
    /**
     * @return l'image correspondante à la direction du joueur s'il est invincible
     */
    private final ImageResource[] directionsInvi = new ImageResource[]{
            // Direction { N, E, S, W }
            PLAYER_UP_INVI, PLAYER_RIGHT_INVI, PLAYER_DOWN_INVI, PLAYER_LEFT_INVI,
    };
    
    /**
     * @return l'image correspondante à la direction du joueur s'il est invincible et en mode "pirate"
     */
    private final ImageResource[] directionsPirateInvi = new ImageResource[]{
            // Direction { N, E, S, W }
            PIRATE_UP_INVI, PIRATE_RIGHT_INVI, PIRATE_DOWN_INVI, PIRATE_LEFT_INVI,
    };

    /**
     * @return l'image correspondante à la direction du joueur s'il est en mode pirate
     */
    private final ImageResource[] directionsPirate= new ImageResource[]{
        // Direction { N, E, S, W }
        PIRATE_UP, PIRATE_RIGHT, PIRATE_DOWN, PIRATE_LEFT,
};
    // MONSTER
    /**
     * @return l'image correspondante à la direction du monstre
     */
    private final ImageResource[] directionsM = new ImageResource[]{
        // Direction { N, E, S, W }
        MONSTER_UP, MONSTER_RIGHT, MONSTER_DOWN, MONSTER_LEFT,
        
    };    
    // LEVEL
    private final ImageResource[] digits = new ImageResource[]{
            DIGIT_0, DIGIT_1, DIGIT_2, DIGIT_3, DIGIT_4,
            DIGIT_5, DIGIT_6, DIGIT_7, DIGIT_8, DIGIT_9,
    };   
    // BOMB
    private final ImageResource[] bombs = new ImageResource[]{
    		BOMB_1, BOMB_2, BOMB_3, BOMB_4,  
        };    

  
    private ImageFactory() {
        images = new Image[ImageResource.values().length];
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static ImageFactory getInstance() {
        return Holder.instance;
    }

    private Image loadImage(String file) {
    	/**
    	 * Place l'image du niveau correspondant si elle existe, sinon cherche dans le répertoire courant (images)
    	 */
        try{
            return new Image(getClass().getResource("/images/Level" + Game.getGameLevel() + "/" + file).toExternalForm());
        }
        catch(Exception e){
            return new Image(getClass().getResource("/images/" + file).toExternalForm());
        }
    }

    public void load() {
        for (ImageResource img : ImageResource.values()) {
            images[img.ordinal()] = loadImage(img.getFileName());
        }
    }

    public Image get(ImageResource img) {
        return images[img.ordinal()];
    }

    public Image getDigit(int i) {
        if (i < 0 || i > 9)
            throw new IllegalArgumentException();
        return get(digits[i]);
    }

    public Image getPlayer(Direction direction) {
        if(Player.isArmor())
            return get(directionsPirate[direction.ordinal()]);

        return get(directions[direction.ordinal()]);
    }
    
    /**
     * @param direction la direction du joueur
     * @return l'image correspondante à la direction du joueur en mode invincible
     */
    public Image getPlayerInvi(Direction direction) {
    	return get(directionsInvi[direction.ordinal()]);
    }
    
    /**
     * 
     * @param direction la direction du joueur
     * @return l'image correspondante à la direction du joueur en mode invincible et pirate
     */
    public Image getPirateInvi(Direction direction) {
    	return get(directionsPirateInvi[direction.ordinal()]);
    }

    /**
     * 
     * @param direction la direction du monstre
     * @return l'image correspondante à la direction du monstre
     */
    public Image getMonster(Direction direction) {
        return get(directionsM[direction.ordinal()]);
    }
    
    /**
     * @param range la portée de la bombe
     * @return l'image de bombe en fonction de la portée
     */
    public Image getBomb(int range) {
    	return get(bombs[range-1]);
    }

    /**
     * 
     * @return l'image de la bombe en explosion
     */
    public Image getBim() {
    	return get(EXPLOSION);
    }
    
    /**
     * Holder
     */
    private static class Holder {
        /**
         * Instance unique non préinitialisée
         */
        private final static ImageFactory instance = new ImageFactory();
    }

}
