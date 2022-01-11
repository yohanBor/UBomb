package fr.ubx.poo.game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class WorldCreatorFromFile extends World {
	
	private static String FILENAME = null;    
    
	/***
    * lire le fichier texte et associer chacun des codes caractère à un élement du jeu
    * @param none
    * @return un tableau d'entity à 2 dimensions correspondant à la grille du jeu
    */
	public static WorldEntity[][] lireFichier() throws IOException, FileNotFoundException
	{		
		FILENAME = "src/main/resources/sample/level" + (char) (Game.getGameLevel() +'0') + ".txt";
		
		//examen de la hauteur et largeur de la grille
		BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
		int nombreLignes = 0;
		int nombreCaracteres = 0;

		int c = 0;
		while ((c = reader.read()) != -1)
		{
			nombreCaracteres ++;
			if((char) c =='\n' )
    			nombreLignes ++;
    	}
  
    	final int nombreCharParLigne = (nombreCaracteres / nombreLignes) - 1;

		final WorldEntity[][] tableau = new WorldEntity[nombreLignes][nombreCharParLigne];

		reader.close();
		reader = new BufferedReader(new FileReader(FILENAME));

		int x = 0;
		int y = 0;
		while ((c = reader.read()) != -1) {
			// si on a atteint le bout de la ligne
			if (y == nombreCharParLigne) {
				y = 0; // on réinitialise en y
				x++;
			}
			try {
				if ((char) c != '\n') {
					final WorldEntity wrldE = WorldEntity.fromCode((char) c).get();
					tableau[x][y] = wrldE;
					y++;
				}
			} catch (final Exception e) {
				System.err.println("Cette entity n'existe pas");
			}
		}
		reader.close();
		return tableau;
 	
	}
	
	public WorldCreatorFromFile() throws IOException {
        super(lireFichier());
    }
}
