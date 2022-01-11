package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

public class BombRangeDec extends Decor{

	public String toString() {
        return "BombRangeDec";
    }
	
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
    
	public void action(Game game)
	{
		Player p = game.getPlayer();
		Position nextPos= p.getDirection().nextPosition(p.getPosition());

		//on diminue la portée de toutes les bombes (dans le sac)
		int cpt_range = 0;
		 for(int i = 0; i < p.getNombreBombes(); i++)
		 {
			if(p.getBombEnPoche().get(i).getPorteeBombe() > 1)
			{
				cpt_range ++; 
				p.getBombEnPoche().get(i).setPorteeBombe(p.getBombEnPoche().get(i).getPorteeBombe() - 1);
			}
		 }
		if(cpt_range == p.getNombreBombes())
			p.clearDecor(nextPos);
	}
	
}
