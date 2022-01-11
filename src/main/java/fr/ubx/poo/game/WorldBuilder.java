package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.*;
import java.util.Hashtable;
import java.util.Map;

public class WorldBuilder
{
    private final Map<Position, Decor> grid = new Hashtable<>();

    // -------------------------  CONSTRUCTEUR -------------------------
    private WorldBuilder()
    {
    }
    // -------------------------  METHODES -------------------------

    // creation du monde (des entities / decors)
    /***
    * @param raw le tableau d'entity (la grille)
    * @param dimension la dimension de la grille
    * @return une map contenant pour chaque position, son décor associé.
    */
    public static Map<Position, Decor> build(final WorldEntity[][] raw, final Dimension dimension) {
        final WorldBuilder builder = new WorldBuilder();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                final Position pos = new Position(x, y); // initialise les position
                final Decor decor = processEntity(raw[y][x]); // initialise les decors (entities)
                if (decor != null)
                    builder.grid.put(pos, decor); // crée les decors par case
            }
        }
        return builder.grid;
    }

    /***
     * mettre en place pour chacune des entités une instance de son décor associé
     * 
     * @param entity l'entité à instancier
     * @return une instance du décor associé à l'entité
     */
    private static Decor processEntity(final WorldEntity entity) {
        switch (entity) {
            case Stone:
                return new Stone();
            case Tree:
                return new Tree();
            case Box:
                return new Box();
            case Heart:
                return new Heart();
            case Key:
                return new Key();
            case Chest_opened:
                return new Chest_opened();
            case Chest_closed:
                return new Chest_closed();
            case Teleporteur:
                return new Teleporteur();
            case Lava:
                return new Lava();
            case DoorNextClosed:
                return new DoorNextClosed();
            case DoorPrevOpened:
                return new DoorPrevOpened();
            case DoorNextOpened:
                return new DoorOpened();
            case Princess:
            	return new Princess();
            case BombRangeInc:
            	return new BombRangeInc();
            case BombRangeDec:
            	return new BombRangeDec();
            case BombNumberInc:
            	return new BombNumberInc();
            case BombNumberDec:
            	return new BombNumberDec();
            default:
                return null;
        }
    }
}
