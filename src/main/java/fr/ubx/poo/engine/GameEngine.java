/*
 * Copyright (c) 2020. Laurent Réveillère
 */
package fr.ubx.poo.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.items.Bomb;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteFactory;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final Game game;
    private final Player player;

    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;
    private final List<Sprite> sprites = new ArrayList<>();
    private final Sprite[] spriteMonster;
    private final Sprite[] spriteBomb;
    private final Sprite[] PropagExplo;
    
    // ------------------------- Constructeur -------------------------
    public GameEngine(final String windowTitle, final Game game, final Stage stage) {
        this.game = game;
        this.player = game.getPlayer();
        this.spriteMonster = new Sprite[100];
        this.spriteBomb = new Sprite[100];
        this.PropagExplo = new Sprite[100];
        initialize(stage, game);
        buildAndSetGameLoop();
    }

    // ------------------------- Initialisation game / Carte -------------------------

    private void initialize(final Stage stage, final Game game) {
        sprites.clear();
        this.stage = stage;
        final Group root = new Group();
        layer = new Pane();
        layer.setId("pane" + Game.getGameLevel());
        final int height = game.getWorld().dimension.height;
        final int width = game.getWorld().dimension.width;
        final int sceneWidth = width * Sprite.size;
        final int sceneHeight = height * Sprite.size;
        final Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
        // gestion affichage niveau sur fenêtre javaFX
        stage.setTitle("Niveau " + Game.getGameLevel());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer); // Ajoute tout les decors / sprites
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);

        // Crée les sprites
        game.getWorld().forEach((pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        spritePlayer = SpriteFactory.createPlayer(layer, player);

        for (int i = 0; i < game.getBombOnSet().size(); i++)
            spriteBomb[i] = SpriteFactory.createBomb(layer, game.getBombOnSet().get(i));
        
        for (int i = 0; i < game.getBombPropag().size(); i++)
        	PropagExplo[i] = SpriteFactory.createBomb(layer, game.getBombPropag().get(i));

        for (int i = 0; i < game.getMonsters().size(); i++)
            if (game.getMonsters().get(i).getLevel() == Game.getGameLevel())
            	spriteMonster[i] = SpriteFactory.createMonster(layer, game.getMonsters().get(i));
        
        if (!player.isAlive()) {
            layer.setId("red");
            showMessage("", Color.RED, root);
            rejouer(root);
        }
        if (player.isWinner()) {
            layer.setId("green");
            showMessage("Victoire", Color.GREEN, root);
            rejouer(root);
        }
    }

/***
 *  Permet de changer de monde
 *  Charger un monde deja crée : @see {@link #game.setWorld()}
 *  Sinon on le crée : @see {@link #game.createWorld()}
 *  Il faut egalement deplacer le joueur dans le nouveau monde : @see {@link #game.createCharacters()}
 */
    private void initiNewWorld() {
        try {
            ImageFactory.getInstance().load();
            stage.close();
            if (game.saverRaw.containsKey(Game.getGameLevel()))
                game.setWorld(game.saverRaw.get(Game.getGameLevel()), game.saverGrid.get(Game.getGameLevel()));
            else
                game.CreateWorld();

            game.CreateCharacters();
            game.setRestart_level(0);
            initialize(stage, game);
            this.start();
        } catch (final IOException e) {
            System.err.println("Probleme creation monde ! \n");
        }
    }

    // ------------------------- Affiche les sprites -------------------------
    private void render() {
        for (int i = 0; i < game.getBombOnSet().size(); i++)
            if (game.getBombOnSet().get(i).getLevel() == Game.getGameLevel())
                if (spriteBomb[i] != null)
                    spriteBomb[i].render();
        
        for (int i = 0 ; i < game.getBombPropag().size(); i++) 
        	if (game.getBombPropag().get(i).getLevel() == Game.getGameLevel())
        		if (PropagExplo[i] != null)
        			PropagExplo[i].render();
        

        for (int i = 0; i < game.getMonsters().size(); i++)
            if (game.getMonsters().get(i).getLevel() == Game.getGameLevel())
                spriteMonster[i].render();

        sprites.forEach(Sprite::render);
        // Affichage pour fin de partie
        if (game.getPlayer().isAlive() == false)
            spritePlayer.RenderEnd(false);

        else if (game.getPlayer().isWinner())
            spritePlayer.RenderEnd(true);
        // Affichage "normal"
        else
            spritePlayer.render();
    }

    // ------------------------- Appliquer 60 * / sec si possible) -------------------------

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(final long now) // now --> The timestamp of the current frame given in nanoseconds
            {
                // Check keyboard actions
                processInput(now);

                // Do actions
                update(now); // applique les mouvements (joueurs / monster) etc ...

                // Graphic update (added)
                render();

                // Changement de niveau
                if (game.getRestart_level() != 0)
                    initiNewWorld();

                statusBar.update(game);
            }
        };
    }

    // ------------------------- Gere les touches clavier -------------------------
    private void processInput(final long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        if (input.isMoveDown())
            player.requestMove(Direction.S);

        if (input.isMoveLeft())
            player.requestMove(Direction.W);

        if (input.isMoveRight())
            player.requestMove(Direction.E);

        if (input.isMoveUp())
            player.requestMove(Direction.N);

        if (input.isBomb())
            player.deposerBombe(player.getPosition(), 0);

        if (input.isKey())
            player.requestOpen();

        input.clear();
    }

    private void update(final long now) {
        // On a appelé a re initialize la carte
        if (game.get_render() == true) {
            initialize(stage, game);
            game.set_render(false);
        }
        // PLAYER
        player.update(now); // permet de bouger le player --> *60/sec

        // Si le joueur doit bénéficier d'une seconde d'immunité
        if (player.isToTime()) {
            player.setToTime(false);
            player.setTimeImmu(now); // on enregistre 'now' dans le timer d'immunité du player
        }

        // Gestion immunité joueur durant 1 seconde si attaque bombe ou monstre
        if (player.isInvincible())
            if (now - player.getTimeImmu() >= 1000000000)  // si ça fait plus d'une seconde que le joueur est immunisé
                player.setInvincible(false);               // on enlève l'immunité du player

        if (player.isAlive() == false) {
            gameLoop.stop();
            initialize(stage, game);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            initialize(stage, game);
        }
        // MONSTERS
        Iterator<Monster> itMonster = game.getMonsters().iterator();
        while(itMonster.hasNext())
        {
            Monster m = itMonster.next();
            if(m.isAlive() == false){
                itMonster.remove();
                initialize(stage, game);
            }
            else if(m.getLevel() == Game.getGameLevel() && game.getRestart_level() == 0  )
                m.update(now);
        }
        
        
      //BOMBES
        Iterator<Bomb> itBomb = game.getBombOnSet().iterator();
        while(itBomb.hasNext())
        {
            Bomb b = itBomb.next();
            if(b.getLevel() == Game.getGameLevel())
                if (b.explosed(now)) {
                	itBomb.remove();
                }
                    
        }
        
        /**
         * gestion des propagations d'explosions
         */
        Iterator<Bomb> iteBomb = game.getBombPropag().iterator();
        while(iteBomb.hasNext())
        {
            Bomb b = iteBomb.next();
            if(b.getLevel() == Game.getGameLevel())
                if (!b.isToUpdate() && b.explosed(now)) {
                	iteBomb.remove();
                }
                    
        }
    }

    // ------------------------- AFFICHAGE VICTOIRE / DEFAITE -------------------------

    /**
     * 
     * @param root Group actuel utiliser dans @see {@link #initialize(Stage, Game)}
     * Ajout d'un bouton "REJOUER" et reinitialise la partie si on clique dessus
     */
    private void rejouer(final Group root) {
        final Button button = new Button();
        // Essais de placer au milieu de la fenetre
        button.setTranslateX(stage.getWidth() / 2 - 110);
        button.setTranslateY(stage.getHeight() / 2);
        button.setText("REJOUER");
        // CSS du boutton
        button.setStyle("-fx-background-color: rgba(255,255,255,0.5);" + "-fx-text-fill: black;"
                + "-fx-font-size : 20px;" + "-fx-text-align : center;");

        button.setPrefWidth(214.0f);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent e) {
                stage.close();
                // Remise map à 0
                game.newSaveMap();
                ImageFactory.getInstance().load();
                // Reconfiguration de carte
                try {
                    game.CreateWorld();
                } catch (final IOException e1) {
                    e1.printStackTrace();
                }
                // Reconfiguration du joueur
                game.CreateCharacters();
                final Player p = game.getPlayer();
                p.setLives(3);
                Player.setArmor(false);
                p.setWinner(false);
                p.setInvincible(false);
                p.setKeys(0);
                p.setNombreBombes(1);
                p.setBombEnPoche(new ArrayList<Bomb>());
                p.getBombEnPoche().add(new Bomb(game, p.getPosition()));

                // Relance du GameEngine
                final GameEngine engine = new GameEngine("Niveau 1", game, stage);
                engine.start();
            }
        });
        root.getChildren().add(button); // Ajout du boutton au Group
    }

    // Cadre / general de l'affiche des messages
    /**
     * 
     * @param msg Message a afficher
     * @param color Couleur du message
     * @param root Group utiliser
     */
    private void showMessage(final String msg, final Color color, final Group root) {

        // Texte
        final Text text = new Text(msg);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(new Font(40));
        text.setFill(color);
        if (player.isWinner())
            text.setX(stage.getWidth() / 2 - 65);
        else
        
            text.setX(stage.getWidth() / 2 - 60);

        text.setY(stage.getHeight() / 2 - 30);
        // Fond noir du texte
        final Rectangle shape = new Rectangle();
        shape.setX(stage.getWidth() / 2 - 110);
        shape.setY(stage.getHeight() / 2 - 100);
        shape.setWidth(214.0f);
        shape.setHeight(100.0f);
        if(!player.isAlive())
        {
            Image img = new Image("gameover.png");
            shape.setFill(new ImagePattern(img));
        }
        // shape.setFill(Color.BLACK);
        root.getChildren().add(shape);
        root.getChildren().add(text);
        stage.show();
        new AnimationTimer() {
            public void handle(final long now) {
                processInput(now);
            }
        }.start();
    }

    public void start() {
        gameLoop.start();
    }
}
