package com.techdegree.nikiforo.view;

import com.techdegree.nikiforo.Jar;
import com.techdegree.nikiforo.Prompter;

import java.io.IOException;

public class Game {
    public static void main(String[] args) throws IOException {

        Prompter prompter = new Prompter();

        String typeOfJarItems = prompter.askAdminForTypeofItemsInJar();

        int jarCapacity = prompter.askAdminForCapacityOfJar();

        // The game plays until player
        // answers 'y' to finish the game
        boolean gameIsOver = false;
        do {
            prompter.setPlayerName();
            Jar jar = new Jar(typeOfJarItems, jarCapacity);
            prompter.askPlayerToGuessNumberOfItemsInJar(jar);
            gameIsOver = prompter.askPlayerToFinishGame();
        } while (!gameIsOver);

        System.out.printf("The best player is: '%s' with score '%d'%n",
                prompter.getPlayerWithHighestScore(),
                prompter.getHighScore());
    }
}
