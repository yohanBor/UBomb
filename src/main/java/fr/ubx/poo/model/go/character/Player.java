/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import java.util.ArrayList;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.PositionNotFoundException;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.items.Bomb;
import fr.ubx.poo.model.go.items.BoxGO;

public class Player extends GameObject implements Movable {

    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private boolean winner = false;
    private int keys = 0; //added (nombre de clés)
    private int nombreBombes; //pour gérer l'indice dans le tableau
    private ArrayList<Bomb> bombEnPoche; //pour gérer les bombes que le joueur possède "en poche"
    private long timeImmu; // timer immunité player
    private boolean invincible = false;
    private boolean toTime = false; //enregistrer ou non le temps de début d'immunité
    private static boolean armor = false;
   

	// -------------------------  CONSTRUCTEUR -------------------------
    public Player(final Game game, final Position position) throws PositionNotFoundException {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
        this.keys = game.getInitnbKeys();
        this.nombreBombes = 1;
        this.bombEnPoche = new ArrayList<Bomb>();
        bombEnPoche.add(new Bomb(game, position));

    }

    // ------------------------- METHODES -------------------------

    // ------------------------- GESTION Mouvement -------------------------

    /**
     *  requêtes déplacements joueur
     * @param direction Direction du joueur
     */
    public void requestMove(final Direction direction) {
        if (direction != this.direction)
            this.direction = direction;
        moveRequested = true;
    }

    /**
     * Demande création d'une boite en GameObject
     * Nous avons mis les boites en gameObject pour une meilleur implementation / utilisation des methodes
     * @param game
     * @param nextPos
     * @param nextPosBox
     */
    public void CreateBoxGO(final Game game, final Position nextPos, final Position nextPosBox) {
        final BoxGO box = new BoxGO(game, nextPosBox);
        box.canMove(this, nextPos, nextPosBox);
    }

    /***
     * Vérification déplacement joueur
    */
    public boolean canMove(final Direction direction) {
        final Position nextPos = direction.nextPosition(getPosition()); // calcule la nouvelle position (en fonction de
                                                                        // la direction choisie)
        final Decor decorNext = game.getWorld().get(nextPos); // le décor qui se trouve à la nouvelle position
        // Si le joueur tente de sortir de la carte
        if (!nextPos.inside(game.getWorld().dimension)) {
            return false;
        }
        // si la case est vide
        if (decorNext == null) {
            return true;
        }
        decorNext.action(game);
        return decorNext.canWalk(this, nextPos);
    }

    /**
    *  Si le joueur peut bouger
    * @param direction Direction joueur
    */
    public void doMove(final Direction direction) {
        final Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos); // applique le mouvement
        for (final Monster m : this.game.getMonsters()) {
            if (this.getPosition().equals(m.getPosition()) && m.getLevel() == Game.getGameLevel() && !isInvincible()) {
                this.setToTime(true);
                this.setInvincible(true);
                this.lives--;
            }
        }
    }

    // ------------------------- GESTION bombes ---------------------------------
    /**
     * Deposer bombe
     * @param pos Position deposer bombe
     * @param timer Timer debut explosion
     */
    public void deposerBombe(final Position pos, final long timer) {

        if (nombreBombes > 0) {

            final Bomb pose = this.getBombEnPoche().get(nombreBombes - 1); // prend la derniere bombe de la liste de
                                                                           // bombes
            pose.setLevel(Game.getGameLevel());
            pose.setPosition(pos); // position ou la bombe est posé
            if (timer != 0)
                pose.setTimer(System.currentTimeMillis() - timer);
            else
                pose.setTimer(System.currentTimeMillis()); // retardateur

            game.getBombOnSet().add(pose); // ajout d'une bombe sur le terrain
            getBombEnPoche().remove(nombreBombes - 1); // on enlève la dernière bombe
            this.setNombreBombes(this.getNombreBombes() - 1);// on a perdu une bombe
            game.set_render(true); // on met à jour le sprite correspondant à la bombe qu'on vient de déposer
        }
    }
    

    // ------------------------- GESTION game evolution -------------------------

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        return this.lives > 0;
    }

    public void update(final long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    /**
     * Gestion des déplacements entre niveaux
     */
    public void requestOpen() {
        final Decor decor = game.getWorld().get(this.getPosition()); // le décor qui se trouve à la position actuelle
        if (decor != null)
            decor.open(game);
    }

    // ------------------------- SETTERS -------------------------

    public void setKeys(final int keys) {
        this.keys = keys;
    }

    public void setLives(final int lives) {
        this.lives = lives;
    }

    public void setNombreBombes(final int nombreBombes) {
        this.nombreBombes = nombreBombes;
    }

    public void setInvincible(final boolean bool) {
        this.invincible = bool;
    }

    public void setBombEnPoche(final ArrayList<Bomb> bombEnPoche) {
        this.bombEnPoche = bombEnPoche;
    }

    public void setWinner(final boolean win) {
        this.winner = win;
    }

    public void setTimeImmu(final long timeImmu) {
        this.timeImmu = timeImmu;
    }

    public void setToTime(final boolean toTime) {
        this.toTime = toTime;
    }

    public static void setArmor(final boolean armor) {
		Player.armor = armor;
	}
    
	// -------------------------  GETTERS  -------------------------
    public int getKeys() {
		return keys;
	}
	
	public int getLives() {
        return lives;
    }
   
	public int getNombreBombes() {
		return nombreBombes;
	}
    
    public Direction getDirection() {
        return direction;
    }  
    
    /**
     * 
     * @return Portée de la dernière bombe
     */
    public int getLastPortee()
    {	
    	if (this.nombreBombes > 0)
    		return getBombEnPoche().get(nombreBombes-1).getPorteeBombe(); 	
    	return 1;
    }

	public ArrayList<Bomb> getBombEnPoche() {
		return bombEnPoche;
	}
   
	public long getTimeImmu() {
		return timeImmu;
    }
    
	public boolean isInvincible() {
		return invincible;
    }    
    
    public boolean isToTime() {
		return toTime;
	}

    public static boolean isArmor() {
		return armor;
	}

  
}
