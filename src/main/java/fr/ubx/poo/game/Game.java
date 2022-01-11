/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;

public class Game {

    public World world;
    public final String worldPath; // endroit ou se trouve les fichiers textes qui decrivent chaque monde (pas utilisé)
    private final Player player;
    private ArrayList<Monster> monsters ; // Indique au jeu qu'il y a un monster (pour le fonctionemment)
    private ArrayList<Bomb> bombOnSet;
    public ArrayList<Bomb> bombPropag;

	public int initPlayerLives;
    public int nbKeys;
    public int initKeys;
    public int initBombs;
    private static int gameLevel = 1;
    private boolean restart_render = false;
    private int restart_level = 0;
    
    // Sauvegarde du jeu
    public HashMap<Integer, WorldEntity[][]> saverRaw = new HashMap<Integer, WorldEntity[][]>();
    public HashMap<Integer, Map<Position, Decor>> saverGrid = new HashMap<Integer, Map<Position, Decor>>();

    
    // ------------------------- CONSTRUCTEUR -------------------------

    public Game(final String worldPath) throws IOException {
        this.worldPath = worldPath;
        loadConfig(worldPath);
        // Monstre creation
        monsters = new ArrayList<Monster>(); // on crée le tableau de monstres
        bombOnSet = new ArrayList<Bomb>(); // toutes les bombes qui sont sur la map
        bombPropag = new ArrayList<Bomb>();
        CreateWorld();
        Position positionPlayer = null;
        try {
            // Player creation
            positionPlayer = world.findWrlE(WorldEntity.Player);
            player = new Player(this, positionPlayer);

            // Monstre creation
            for (int x = 0; x < world.dimension.width; x++)
                for (int y = 0; y < world.dimension.height; y++)
                    if (world.getWorldEntity()[y][x] == WorldEntity.Monster)
                        monsters.add(new Monster(this, new Position(x, y)));

        } catch (final PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    // ------------------------- Construction world -------------------------

    /***
     * Deplace le joueur sur une portes / teleporteur / ou autreen fonction du niveau et avancé du jeu
     * Crée les monstres si pas deja crées
     */
    public void CreateCharacters() {
        Position positionPlayer = null;
        try {
            // Player creation
            if (restart_level == 10) {
                positionPlayer = world.findWrlE(WorldEntity.Teleporteur);
            } else {
                // si on tente de revenir à un niveau inférieur
                if (restart_level < 0) {
                    try {
                        positionPlayer = world.findWrlE(WorldEntity.DoorNextClosed);
                    } catch (final Exception e) {
                        positionPlayer = world.findWrlE(WorldEntity.DoorNextOpened);
                    }
                }
                // si on tente d'aller à un niveau supérieur
                if (restart_level > 0)
                    positionPlayer = world.findWrlE(WorldEntity.DoorPrevOpened);

                if (restart_level == 0)
                    positionPlayer = world.findWrlE(WorldEntity.Player);
            }
            // on enregistre la position du player
            player.setPosition(positionPlayer);

            // - Monstre creation -
            // Ajoute les monsters de ce niveau
            if (!(saverRaw.containsKey(Game.getGameLevel()))) {
                for (int x = 0; x < world.dimension.width; x++)
                    for (int y = 0; y < world.dimension.height; y++)
                        if (world.getWorldEntity()[y][x] == WorldEntity.Monster)
                            monsters.add(new Monster(this, new Position(x, y)));
            }
        } catch (final PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
    /**
     * Crée le jeu à partir du fichier texte correspondant au niveau
     * @throws IOException Arrive pas à trouver le fichier
     */ 
    public void CreateWorld() throws IOException {
        world = new WorldCreatorFromFile();
    }

    // ------------------------- METHODES -------------------------

    /**
     * CONFIGURE le fichier config.properties
     * @param path
     */
    public void loadConfig(final String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            final Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
            initKeys = Integer.parseInt(prop.getProperty("nbKeys", "0")); // added
            initBombs = Integer.parseInt(prop.getProperty("nbBombs", "1")); // added

        } catch (final IOException ex) {
            System.err.println("Error loading configuration");
        }
    }
    /**
     * Enregistre la map actuel dans les Hmap correspondants
     */
    public void saveMap() {
        saverRaw.put(Game.getGameLevel(), world.getRaw());
        saverGrid.put(gameLevel, world.getGrid());
    }

    /**
     * Efface toutes les données du jeu ( lors d'un "rejouer" )
     */
    public void newSaveMap() {
        gameLevel = 1;
        saverRaw = new HashMap<Integer, WorldEntity[][]>();
        saverGrid = new HashMap<Integer, Map<Position, Decor>>();

        monsters = new ArrayList<Monster>();
        bombOnSet = new ArrayList<Bomb>();
       
    }
    // ------------------------- SETTERS -------------------------

    // Reconstruit la carte en la mettant à jour
    public void set_render(final boolean bool) {
        this.restart_render = bool;
    }

    public void setRestart_level(final int restart_level) {
        this.restart_level = restart_level;
    }

    public void setGameLevel(final int l) {
        Game.gameLevel = l;
    }

    public void setWorld(final WorldEntity[][] raw, final Map<Position, Decor> grid) {
    	world = new World(raw, grid);
    }
    

 	public void setBombPropag(ArrayList<Bomb> bombPropag) {
 		this.bombPropag = bombPropag;
 	}
    

    // -------------------------  GETTERS -------------------------
    public int getRestart_level() {
        return restart_level;
    }
    
    public static int getGameLevel() {
        return Game.gameLevel;
    }
   
    public boolean get_render() {
    	return restart_render;
    }

    public int getInitPlayerLives() {
        return initPlayerLives;
    }
    
    public int getInitnbKeys() {
    	return initKeys;
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ArrayList<Monster> getMonsters() {
        return this.monsters;
    }
    
    public ArrayList<Bomb> getBombOnSet() {
        return this.bombOnSet;
    }
    
    public boolean isRestart_render() {
		return restart_render;
    }
    
 	public ArrayList<Bomb> getBombPropag() {
 		return bombPropag;
 	}

}
