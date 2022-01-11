/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class Sprite
{
    public static final int size = 40;
    private final Pane layer; // crée une "page blanche"
    private ImageView imageView; // acceseur d'image
    private Image image;

    // -------------------------  CONSTRUCTEUR -------------------------

    public Sprite(final Pane layer, final Image image) {
        this.layer = layer;
        this.image = image;
    }

    // ------------------------- METHODES -------------------------
    public abstract void updateImage();

    public abstract Position getPosition();

    public final void render() {
        if (imageView != null) {
            remove();
        }
        updateImage(); // affiche image en fonction de la direction des sprites (Monster et Player)
        imageView = new ImageView(this.image);
        imageView.setX(getPosition().x * size); // affichage des images par ligne
        imageView.setY(getPosition().y * size); // affichage des images par colonne
        layer.getChildren().add(imageView); // ajout d'images
    }

    public final void remove() {
        layer.getChildren().remove(imageView); // supprime image (une fois deplacé)
        imageView = null;
    }

    // SETTER
    public final void setImage(final Image image) {
        if (this.image == null || this.image != image) {
            this.image = image;
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(final ImageView imageView) {
        this.imageView = imageView;
    }

    public Pane getLayer() {
        return layer;
    }
    
	/**
	 * Méthode qui affiche un sprite du joueur différent en fonction de s'il a gagné/perdu
	 * @param victory booléan qui renvoie true si victoire, false sinon
	 */
    public void RenderEnd(Boolean victory)
    {
        if(victory)
            imageView = new ImageView("images/player_victory.png");
        else
            imageView = new ImageView("images/player_defeat.png");
        imageView.setX(getPosition().x * size); // affichage des images par ligne 
        imageView.setY(getPosition().y *size ); // affichage des images par colonne 
        layer.getChildren().add(imageView); // ajout d'images
    }
}
