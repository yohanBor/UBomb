
package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

//created
public class Princess extends Decor {
	
	public boolean MonsterCanMove() {
		return false;
	}

	public boolean canExplose(Bomb bomb, Position pos) {
		return false;
	}
	
	public boolean canWalk(Player player, Position nextPos) {
		return true;
	}

	@Override
	public void action(Game game) {
		Player p = game.getPlayer();
		p.setWinner(true);
	}

	public String toString() {
        return "Princess";
    }
	

}
