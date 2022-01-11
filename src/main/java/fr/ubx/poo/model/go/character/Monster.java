/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;

public class Monster extends GameObject implements Movable {
	
    private Direction direction;
    private int lives = 1;
    private long timeLM; // time last move
    private int level;

    // ------------------------- CONSTRUCTEUR -------------------------

	public Monster(final Game game, final Position position) {
        super(game, position);
        this.direction = Direction.E;
        this.timeLM = System.currentTimeMillis(); // when have have moved the last time
        this.setLevel(Game.getGameLevel());
    }

    // ------------------------- METHODES -------------------------

    // ------------------------- GESTION Mouvement -------------------------

	/**
	 * A partir de la position du joueur, détermine quelle direction doit prendre le monstre pour s'approcher du joueur	
	 * @return une direction s'approchant du joueur 
	 */
    public Direction intelliArtifi() {

        // on récupère la position du player

        final int posPlayerX = game.getPlayer().getPosition().x;
        final int posPlayerY = game.getPlayer().getPosition().y;
        // Level 3 à une dimention
        if (Game.getGameLevel() == 3) {
            if (posPlayerX < this.position().x)
                return Direction.W;
            else
                return Direction.E;
        }

        // puis la position du monster
        final int posMonsterX = this.getPosition().x;
        final int posMonsterY = this.getPosition().y;

        // decor haut
        final Position up = Direction.N.nextPosition(getPosition());
        final Decor haut = game.getWorld().get(up);

        // decor bas
        final Position down = Direction.S.nextPosition(getPosition());
        final Decor bas = game.getWorld().get(down);

        // decor gauche
        final Position left = Direction.W.nextPosition(getPosition());
        final Decor gauche = game.getWorld().get(left);

        // decor droit
        final Position right = Direction.E.nextPosition(getPosition());
        final Decor droite = game.getWorld().get(right);

        // On compare les coordonnées
        if (posPlayerX < posMonsterX && gauche == null)
            return Direction.W;
        if (posPlayerY < posMonsterY && haut == null)
            return Direction.N;
        if (posPlayerX > posMonsterX && droite == null)
            return Direction.E;
        if (posPlayerY > posMonsterY && bas == null)
            return Direction.S;
        else
            return Direction.random();
    }

    @Override
    public boolean canMove(final Direction direction) {

        this.direction = Direction.random(); // choisi au hasard une direction
        // si on est au dernier niveau, on utilise le module d'intelligence artificielle
        // pour les monstres, i.e, se dirigent vers le player
        if (Game.getGameLevel() == 4 || Game.getGameLevel() == 3)
            this.direction = this.intelliArtifi();

        final Position nextPos = this.direction.nextPosition(getPosition());
        final Decor decorNext = game.getWorld().get(nextPos);

        if (nextPos.inside(game.getWorld().dimension)) {
            if (decorNext == null || decorNext.MonsterCanMove())
                return true;
        }
        if (Game.getGameLevel() == 3 || Game.getGameLevel() == 4)
            return false;

        return canMove(Direction.random());
    }

    // Deplace le monster ( appelée que par update() dans GameEngine.java)
    public void doMove(final Direction direction) throws StackOverflowError {

        if (getLevel() == Game.getGameLevel()) {
            final Position nextPos = direction.nextPosition(getPosition());
            setPosition(nextPos);

            if (this.getPosition().equals(this.game.getPlayer().getPosition()) && getLevel() == Game.getGameLevel()
                    && !this.game.getPlayer().isInvincible()) {
                this.game.getPlayer().setLives(this.game.getPlayer().getLives() - 1); // enleve une vie au player si le
                                                                                      // monster va sur lui
                this.game.getPlayer().setToTime(true);
                this.game.getPlayer().setInvincible(true);
            }
        }
    }

    // ------------------------- GESTION game evolution -------------------------

    /**
     * "Faire en sorte que la vitesse de déplacement des monstres soit faible dans les premiers niveaux et augmente plus on se rapproche de la princesse"
     * @param niveauCourant le niveau actuel
     * @return la vitesse de déplacement des monstres en fonction du niveau sur lequel on se trouve
     */
    public int vitesseDeplacementMonster(int niveauCourant) {	
    	if (niveauCourant == 1)
    		return 2000;
    	return 2000 - (niveauCourant * 200);	
    }

    public void update(final long now) {

        final int niveauCourant = Game.getGameLevel();
        int tempoMoveMonster = this.vitesseDeplacementMonster(niveauCourant);
        if (Game.getGameLevel() == 3)
            tempoMoveMonster = 750;
        if (System.currentTimeMillis() - this.timeLM >= tempoMoveMonster) // si le monster n'a pas bougé depuis plus de
                                                                          // x ms
        {
            if (canMove(direction)) {
                doMove(direction);
            }
            this.timeLM = System.currentTimeMillis(); // met a jour le time du dernier mouvement
        }
    }

    public boolean isAlive() {
        return this.lives > 0;
    }
    // ------------------------- GETTERS -------------------------

    public int getLives() {
        if (lives == 0)
            game.set_render(true);

        return lives;
    }

    public Direction getDirection() {
        return direction;
    }

    public Position position() {
        return this.getPosition();
    }

    public int getLevel() {
        return level;
    }

    // ------------------------- SETTERS -------------------------

    public void setLives(final int l) {
        this.lives = l;
    }

    public void setLevel(final int level) {
		this.level = level;
	}
    
}
