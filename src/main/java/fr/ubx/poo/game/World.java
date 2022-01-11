/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class World
{
    private final Map<Position, Decor> grid; // crée dans worldBuilder.java
    private final WorldEntity[][] raw;
    public final Dimension dimension;

    // ------------------------- CONSTRUCTEURS -------------------------
    public World(final WorldEntity[][] raw) {
        this.raw = raw;
        dimension = new Dimension(raw.length, raw[0].length);
        grid = WorldBuilder.build(raw, dimension); // CONSTRUIT UNE GRILLE en parcourant le tableau a 2 dimension avec
                                                   // process entity
    }

    // surcharge de constructeur
    public World(final WorldEntity[][] raw, final Map<Position, Decor> grid) {
        this.raw = raw;
        dimension = new Dimension(raw.length, raw[0].length);
        this.grid = grid;
    }

    // ------------------------- METHODES liée a grille du jeu -------------------------
    
    // ------------------------- Gerer la grille ---------------------------------------

    public void clear(final Position position) {
        grid.remove(position);
    }

    public void forEach(final BiConsumer<Position, Decor> fn) {
        grid.forEach(fn);
    }

    public Collection<Decor> values() {
        return grid.values();
    }

    public boolean isEmpty(final Position position) {
        return grid.get(position) == null;
    }

    // ------------------------- Gerer position dans grille -------------------------

    /**
     * trouver une entity (sa position) donnée en paramètre dans la grille
     * 
     * @param wrldE l'entity à trouver
     * @return la position de l'entity (si trouvée)
     */
    public Position findWrlE(final WorldEntity wrldE) throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++)
            for (int y = 0; y < dimension.height; y++)
                if (raw[y][x] == wrldE)
                    return new Position(x, y);

        throw new PositionNotFoundException("Error finding" + wrldE);
    }

    // GETTER

    public WorldEntity[][] getWorldEntity() {
        return raw;
    }

    public Decor get(final Position position) {
        return grid.get(position);
    }

    public Map<Position, Decor> getGrid() {
        return this.grid;
    }

    public WorldEntity[][] getRaw() {
        return this.raw;
    }

    // SETTER
    public void set(final Position position, final Decor decor) {
        grid.put(position, decor);
    }

    /**
     * Affichage Raw (uniquement pour aide codage)
     */
    public void rawToString() {
        for (int y = 0; y < raw.length; y++) {
            for (int x = 0; x < raw[0].length; x++) {
                System.out.print(raw[y][x]);
            }
            System.out.println("");
        }
    }
      /**
     * Affichage Grid (uniquement pour aide codage)
     */
    public void gridToString() {
        final Iterator<Entry<Position, Decor>> iterator = grid.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<Position, Decor> entry = (Map.Entry<Position, Decor>) iterator.next();
            System.out.print(entry.getKey() + " --> " + entry.getValue());
        }
        System.out.println();
    }
}
