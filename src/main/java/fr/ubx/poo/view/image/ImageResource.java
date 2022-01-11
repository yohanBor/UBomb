package fr.ubx.poo.view.image;


/**
 * 
 * banque d'images utiles au jeu
 */
public enum ImageResource {
    BANNER_BOMB ("banner_bomb.png"),
    BANNER_RANGE ("banner_range.png"),
    HEART("heart.png"), //added
    KEY("key.png"), //added
    TELEPORTEUR("teleporteur.png"),
    LAVA("lava.png"),
    DIGIT_0 ("banner_0.jpg"),
    DIGIT_1 ("banner_1.jpg"),
    DIGIT_2 ("banner_2.jpg"),
    DIGIT_3 ("banner_3.jpg"),
    DIGIT_4 ("banner_4.jpg"),
    DIGIT_5 ("banner_5.jpg"),
    DIGIT_6 ("banner_6.jpg"),
    DIGIT_7 ("banner_7.jpg"),
    DIGIT_8 ("banner_8.jpg"),
    DIGIT_9 ("banner_9.jpg"),
    PLAYER_DOWN("player_down.png"),
    PLAYER_LEFT("player_left.png"),
    PLAYER_RIGHT("player_right.png"),
    PLAYER_UP("player_up.png"), 
    PLAYER_UP_INVI("player_up_invi.png"),
    PLAYER_RIGHT_INVI("player_right_invi.png"),
    PLAYER_DOWN_INVI("player_down_invi.png"), 
    PLAYER_LEFT_INVI("player_left_invi.png"),
    MONSTER_DOWN("monster_down.png"), // added 
    MONSTER_LEFT("monster_left.png"), // added 
    MONSTER_RIGHT("monster_right.png"),// added 
    MONSTER_UP("monster_up.png"),  //  added 
    STONE("stone.png"), //added 
    TREE("tree.png"), //added
    BOX("box.png"), //added
    CHEST_OPENED("chest_opened.png"),
    CHEST_CLOSED("chest_closed.png"),
    DOORNEXTCLOSED("door_closed.png"), //added
    DOOROPENED("door_opened.png"), //added
	PRINCESS("princessG.gif"), 
	BOMB_NUMBER_INC("bonus_bomb_nb_inc.png"),
	BOMB_NUMBER_DEC("bonus_bomb_nb_dec.png"), 
	BOMB_NUMBER_RANGE_DEC("bonus_bomb_range_dec.png"),
	BOMB_RANGE_INC("bonus_bomb_range_inc.png"),
	BOMB_1("bomb1.png"), //added (bombe range 1)
	BOMB_2("bomb2.png"),
	BOMB_3("bomb3.png"),
	BOMB_4("bomb4.png"),
    EXPLOSION("explosion.png"),

    // COSTUME 
    PIRATE_DOWN("pirate_down.png"),
    PIRATE_LEFT("pirate_left1.png"),
    PIRATE_RIGHT("pirate_right.png"),
    PIRATE_UP("pirate_up.png"),
    PIRATE_DOWN_INVI("pirate_down_invi.png"),
    PIRATE_LEFT_INVI("pirate_left_invi.png"),
    PIRATE_RIGHT_INVI("pirate_right_invi.png"),
    PIRATE_UP_INVI("pirate_up_invi.png")
    ;

    private final String FileName;

    // -------------------------  CONSTRUCTEUR -------------------------
    ImageResource(String fileName)
    {
        this.FileName = fileName;
    }

    // -------------------------  GETTER -------------------------
    public String getFileName() {
        return FileName;
    }
}
