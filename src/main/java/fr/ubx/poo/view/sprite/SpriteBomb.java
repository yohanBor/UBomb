package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.items.Bomb;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpriteBomb extends SpriteGameObject{
	public SpriteBomb(final Pane layer, final GameObject go) {
		super(layer, null, go);
	}

	/**
	 * met à jour le sprite de l'image bombe en fonction de son état (explosion ou non)
	 */
	public void updateImage() {
		final Bomb bomb = (Bomb) go;
		if(bomb.isBoom()) // la bombe est entrain d'exploser
			setImage(ImageFactory.getInstance().getBim()) ; // affichage explosion
		else
			setImage(ImageFactory.getInstance().getBomb(bomb.getPorteeBombe())); // affichage en bombe en fonction de portée
	}	
}
