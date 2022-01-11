package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

public class BombRangeInc extends Decor{
	
	public boolean MonsterCanMove() {
		return true;
	}
	
	@Override
	public boolean canWalk(Player player, Position nextPos) {
		return true;
	}
	public boolean canExplose(Bomb bomb, Position pos) {
		bomb.clearDecor(pos);
		return true;
	}

	@Override
	public void action(Game game) {
		Player p = game.getPlayer();
		Position nextPos= p.getDirection().nextPosition(p.getPosition());
		if(p.getNombreBombes() > 0 )
		{
			if(p.getBombEnPoche().get(0).getPorteeBombe() < 4)
			{
				for(int i = 0; i < p.getNombreBombes() ; i++)
					p.getBombEnPoche().get(i).setPorteeBombe(p.getBombEnPoche().get(i).getPorteeBombe() + 1);	       
				p.clearDecor(nextPos);
			}
		}
    } 
	
	public String toString() {
        return "BombRangeInc";
    }


}
