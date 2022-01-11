/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.BitSet;

import fr.ubx.poo.game.Game;

import static javafx.scene.input.KeyCode.*;

public final class Input {

    /**
     * Bitset which registers if any {@link KeyCode} keeps being pressed or if it is
     * released.
     */
    private final BitSet keyboardBitSet = new BitSet();
    /**
     * "Key Pressed" handler for all input events: register pressed key in the bitset
     */
    private final EventHandler<KeyEvent> keyPressedEventHandler = event -> {

        // register key down
        keyboardBitSet.set(event.getCode().ordinal(), true);
    };
    /**
     * "Key Released" handler for all input events: unregister released key in the bitset
     */
    private final EventHandler<KeyEvent> keyReleasedEventHandler = event -> {

        // register key up
        keyboardBitSet.set(event.getCode().ordinal(), false);
    };
    
    public Input(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
    }

    public void clear() {
        keyboardBitSet.clear();
    }

    private boolean is(KeyCode key) {
        return keyboardBitSet.get(key.ordinal());
    }

    // -------------------------------------------------
    // Evaluate bitset of pressed keys and return the player input.
    // If direction and its opposite direction are pressed simultaneously, then the
    // direction isn't handled.
    // -------------------------------------------------

    /**
     * si on est au niveau 3, ce niveau est en 2D, ainsi on peut seulement aller à gauche/droite
     * @return true si le joueur peut "monter" sur la map
     */
    public boolean isMoveUp() {
        if(Game.getGameLevel() != 3)
            return is(UP) && !is(DOWN);
        return false;
    }

    /**
     * si on est au niveau 3, ce niveau est en 2D, ainsi on peut seulement aller à gauche/droite
     * @return true si le joueur peut "descendre" sur la map
     */
    public boolean isMoveDown() {
        if(Game.getGameLevel() != 3)
            return is(DOWN) && !is(UP);
        return false;   
    }

    public boolean isMoveLeft() {
        return is(LEFT) && !is(RIGHT);
    }

    public boolean isMoveRight() {
        return is(RIGHT) && !is(LEFT);
    }

    public boolean isBomb() {
        return is(SPACE);
    }

    public boolean isKey() {
        return is(ENTER);
    }

    public boolean isExit() {
        return is(ESCAPE);
    }
}
