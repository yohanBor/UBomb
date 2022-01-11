/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class StatusBar {
    public static final int height = 55;
    private HBox hBox = new HBox();
    private Text liveValue = new Text();
    private Text bombsValue = new Text();
    private Text rangeValue = new Text();
    private Text keyValue = new Text();
    private HBox level = new HBox();
    // private int gameLevel = 1;

    private final Game game;
    private final DropShadow ds = new DropShadow();


    // -------------------------  Initialisation StatusBar -------------------------
    public StatusBar(Group root, int sceneWidth, int sceneHeight, Game game)
    {
        this.game = game;
        level.getStyleClass().add("level");

        level.getChildren().add(new ImageView(ImageFactory.getInstance().getDigit(Game.getGameLevel()))); // Affiche le lvl
                                                                                                          


        // Initialise les Hbox
        HBox status = new HBox();
        status.getStyleClass().add("status");

        // Creation des Hbox
        HBox live = statusGroup(ImageFactory.getInstance().get(HEART), this.liveValue);
        HBox bombs = statusGroup(ImageFactory.getInstance().get(BANNER_BOMB), bombsValue);
        HBox range = statusGroup(ImageFactory.getInstance().get(BANNER_RANGE), rangeValue);
        HBox key = statusGroup(ImageFactory.getInstance().get(KEY), this.keyValue);
        status.setSpacing(40.0);
        status.getChildren().addAll(live, bombs, range, key);

        hBox.getChildren().addAll(level, status);
        hBox.getStyleClass().add("statusBar");
        hBox.relocate(0, sceneHeight);
        hBox.setPrefSize(sceneWidth, height);
        root.getChildren().add(hBox);

        // Position (ombre) des Hbox
        ds.setRadius(15.0);
        ds.setOffsetX(3.0);
        ds.setOffsetY(3.0);
        ds.setColor(Color.color(0.5f, 0.5f, 0.5f)); // couleur ombre sous les Hbox
    }

    private HBox statusGroup(Image kind, Text number) {
        HBox group = new HBox();
        ImageView img = new ImageView(kind);
        // Mise en forme des Hbox
        group.setSpacing(4);
        number.setEffect(ds);
        number.setCache(true);
        number.setFill(Color.BLACK);
        number.getStyleClass().add("number");
        //
        group.getChildren().addAll(img, number);
        return group;
    }

    // -------------------------  Update StatusBar -------------------------

    /**
     * Met à jour le niveau dans la barre d'état
     * @param n le niveau courant 
     */
    private void updateLevel(int n)
    {   
        game.setRestart_level(0);
        if (n == Game.getGameLevel()) {
            level.getChildren().clear();
            level.getChildren().add(new ImageView(ImageFactory.getInstance().getDigit(n)));
        }   
    }
    public void update(Game game)
    {
        updateLevel(Game.getGameLevel());

        liveValue.setText(String.valueOf(game.getPlayer().getLives())); // recupere le nb de vie
        rangeValue.setText(String.valueOf(game.getPlayer().getLastPortee())); // a gerer en fonction de la porté de la bombe
        bombsValue.setText(String.valueOf(game.getPlayer().getNombreBombes())); // recupere le nb de bombe
        keyValue.setText(String.valueOf(game.getPlayer().getKeys())); // recupere le nb de clef
    }

    
}
