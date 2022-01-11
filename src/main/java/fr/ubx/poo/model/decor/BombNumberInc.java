package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

public class BombNumberInc extends Decor{
	
	public boolean MonsterCanMove() {
		return true;
	}
	
	public boolean canExplose(Bomb bomb, Position pos) {
		bomb.clearDecor(pos);
		return true;
	}

	public boolean canWalk(Player player, Position nextPos) {
		return true;
	}
    
	public void action(Game game){
		Player p = game.getPlayer();
		Position nextPos= p.getDirection().nextPosition(p.getPosition());
		Bomb b = new Bomb(game, nextPos);
		b.setPorteeBombe(p.getLastPortee()); //on conserve la portée actuelle
        p.setNombreBombes(p.getNombreBombes()+1);
        p.getBombEnPoche().add(b);
        p.clearDecor(nextPos);
	}
	public String toString() {
        return "BombNumberInc";
    }
	
}
