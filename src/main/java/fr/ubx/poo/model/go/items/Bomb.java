package fr.ubx.poo.model.go.items;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;

public class Bomb extends GameObject{

	private int porteeBombe = 1; //la portée minimale (et par défaut) d'une bombe est de 1
	private long timer ; // initialisé a la pose de la bombe
	private boolean boom ; // juste pour l'affichage de l'explosion (dans spriteBomb.java)
	private int level;
	private boolean toUpdate = true; //doit-on mettre à jour le nombre de bombes ? 

 	// -------------------------  CONSTRUCTEUR -------------------------

	public boolean isToUpdate() {
		return toUpdate;
	}


	public void setToUpdate(boolean toUpdate) {
		this.toUpdate = toUpdate;
	}


	public Bomb(Game game, Position position) {
		super(game, position);
		this.setBoom(false);
	}

	
	// -------------------------  METHODES -------------------------
	
	/**
	 * @param now unused
	 * @return true si la bombe à exploser, false sinon
	 */
	public boolean explosed(long now)
    {
		/**
		 * vérifie si la bombe a été posée depuis plus de 4 secondes
		 */
        if(System.currentTimeMillis() - this.getTimer()  >= 4000) 
        {
			setBoom(true);
					
			game.set_render(true); //pour faire disparaître l'image de l'explosion
			if(System.currentTimeMillis() - this.getTimer()  >= 4500) // durée de l'explosion
        	{
				if (this.toUpdate) { //si on doit mettre à jour le nombre de bombes lors de l'explosion
					int portee = this.GetLastPortee(); //sauvegarde de l'ancienne portée
					this.game.getPlayer().setNombreBombes(this.game.getPlayer().getNombreBombes()+1);
					Bomb nouvelle = new Bomb(game, game.getPlayer().getPosition());
					nouvelle.porteeBombe = portee;
					//on rajoute une bombe au player
					this.game.getPlayer().getBombEnPoche().add(nouvelle);
				}
				
				isInRangeHorizontalPositif(); //regarde "à droite"
				isInRangeHorizontalNegatif(); //regarde "à gauche"
				isInRangeVerticalPositif(); //regarde "en haut"
				isInRangeVerticalNegatif(); //regarde "en bas"
				return true;
			}
		}  
		return false;
	}
	
	/**
	 * pour gerer affichage sprite propagation explosion 
	 * @param toBOOM la position à laquelle on doit afficher l'explosion
	 */
	public void propagationExplosion(Position toBOOM) {
		Bomb temporaire = new Bomb(game, toBOOM); //on crée une bombe temporaire (pour affichage sprite explosion)
		// Initialisation de la bombe
		temporaire.toUpdate = false; //ne pas mettre à jour le nombre de bombes lors de l'explosion de celle-ci
		temporaire.porteeBombe = 0;
		temporaire.timer = System.currentTimeMillis() - 3999;
		temporaire.boom = true;
		temporaire.level = Game.getGameLevel();
		game.getBombPropag().add(temporaire);
		game.set_render(true);	
	}
	
	/**
	 * @param toBOOM la position à laquelle on doit afficher l'explosion
	 * @return true si le sprite explosion doit être affiché
	 */
	public boolean toDestruct(Position toBOOM){ //gère les cas de destruction ou non selon la nature de l'explosion
		
		Decor decorInRange = game.getWorld().get(toBOOM); //position de l'éventuel décor selon la portée

		// JOUEUR
		if(toBOOM.equals(this.game.getPlayer().getPosition()) && !this.game.getPlayer().isInvincible()) {
			this.game.getPlayer().setLives(this.game.getPlayer().getLives() - 1);
			this.game.getPlayer().setToTime(true);
            this.game.getPlayer().setInvincible(true);
		}

		// MONSTRE
		for(int m = 0 ; m < this.game.getMonsters().size(); m ++)
		{
			if(toBOOM.equals(this.game.getMonsters().get(m).getPosition()) && this.game.getMonsters().get(m).getLevel() == Game.getGameLevel() ) {
				this.game.getMonsters().get(m).setLives(this.game.getMonsters().get(m).getLives() - 1);
				this.propagationExplosion(toBOOM); //on propage l'affichage de l'explosion
			}		
		}
		// BOMBE
		for (int i = 0; i < this.game.getBombOnSet().size() ; i++)
			if (toBOOM.equals(this.game.getBombOnSet().get(i).getPosition()))
				this.game.getBombOnSet().get(i).setBoom(true);
			
		if (decorInRange == null) {
			return true;
		}
		
		if (decorInRange.canExplose(this, toBOOM) ||decorInRange instanceof Box) {
			this.propagationExplosion(toBOOM); //on propage l'affichage de l'explosion
		}

		return decorInRange.canExplose(this, toBOOM);
	}
	
	/**
	 * pour regarder sur la portée à l'horizontale (coordonnées positives)
	 */
	public void isInRangeHorizontalPositif() { 
	
		for (int x = 0; x <= this.porteeBombe; x++) {
			int posX = this.getPosition().x + x;
			Position toBoom = new Position(posX, this.getPosition().y); 
			boolean verify = toDestruct(toBoom);
			if (verify == false) { //si il y a un obstacle obstruant l'explosion
				break;
			}
		}
	}
	
	/**
	 * pour regarder sur la portée à l'horizontale (coordonnées négatives)
	 */
	public void isInRangeHorizontalNegatif() { 
		
		for (int x = -1; x >= -this.porteeBombe; x--) {
			int posX = this.getPosition().x + x;
			Position toBoom = new Position(posX, this.getPosition().y);
			boolean verify = toDestruct(toBoom);
			if (verify == false) { //si il y a un obstacle obstruant l'explosion
				break;
			}
		}
			
	}
	
	/**
	 * pour regarder sur la portée à la verticale (coordonnées positives)
	 */
	public void isInRangeVerticalPositif() { 
		
		for (int y = 1; y <= this.porteeBombe; y++) {
			int posY = this.getPosition().y + y;
			Position toBoom = new Position(this.getPosition().x, posY);
			boolean verify = toDestruct(toBoom);
			if (verify == false) { //si il y a un obstacle obstruant l'explosion
				break;
			}
		}
	}
	
	/**
	 * pour regarder sur la portée à l'horizontale (coordonnées négatives)
	 */
	public void isInRangeVerticalNegatif() { 
		
		for (int y = -1; y >= -this.porteeBombe && y!=0; y--) {
			int posY = this.getPosition().y + y;
			Position toBoom = new Position(this.getPosition().x, posY);
			boolean verify = toDestruct(toBoom);
			if (verify == false) { //si il y a un obstacle obstruant l'explosion
				break;
			}
		}
	}
	
	
	
	// -------------------------  GETTER -------------------------
	
	public int GetLastPortee() {
		return game.getPlayer().getLastPortee();
	}
	
	public int getPorteeBombe() {
		return porteeBombe;
	}
	
	public int getLevel() {
		return level;
	}
	
	public long getTimer() {
		return timer;
	}


	// -------------------------  SETTER -------------------------

	public void setPorteeBombe(int porteeBombe) {
		this.porteeBombe = porteeBombe;
	}

	public String toString() {
        return "\n Bomb" + " portee bombe: " + porteeBombe + "\n";
    }


	public boolean isBoom() {
		return boom;
	}


	public void setBoom(boolean boom) {
		this.boom = boom;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setTimer(long timer) {
		this.timer = timer;
	}

	

	
	
	

}
