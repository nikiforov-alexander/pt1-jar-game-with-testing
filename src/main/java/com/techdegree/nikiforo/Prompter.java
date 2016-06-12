package com.techdegree.nikiforo;

import com.techdegree.nikiforo.log.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Prompter {

    private BufferedReader mBufferedReader;

    private String mPlayerName;

    private String mPlayerWithHighestScore;
    public String getPlayerWithHighestScore() {
        return mPlayerWithHighestScore;
    }
    protected void setPlayerWithHighestScore(String playerWithHighestScore) {
        mPlayerWithHighestScore = playerWithHighestScore;
    }

    // first time mHighScore is set in askAdminForCapacityOfJar() to jarCapacity + 1
    private int mHighScore;
    protected void setHighScore(int highScore) {
        mHighScore = highScore;
    }
    public int getHighScore() {
        return mHighScore;
    }



    private int mNumberOfGuesses;
    public int getNumberOfGuesses() {
        return mNumberOfGuesses;
    }

    public Prompter() {
        mBufferedReader =
                new BufferedReader(new InputStreamReader(System.in));
        mLogger = new Logger();
    }

    protected Prompter(BufferedReader bufferedReader,Logger logger) {
        mBufferedReader = bufferedReader;
        mLogger = logger;
    }

    // generic function that checks user input string for pattern
    // input string is trimmed
    protected boolean checkStringForPattern(String string, String patternString) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(string.trim());
        return matcher.matches();
    }

    // prompts User for String
    protected String promptForString() throws IOException {
        String userInputString = "";
        try {
            userInputString = mBufferedReader.readLine();
        } catch (IOException ioe) {
            throw new IOException("Problems with reading user input");
        }
        return userInputString;
    }

    // As an administrator of the game I should be able to specify what type
    // of item will be stored in the jar
    // returns trimmed typeOfJarItems
    public String askAdminForTypeofItemsInJar() throws IOException {
        String typeOfJarItems;
        boolean typeOfItemInputIsAccepted = false;
        String errorMessage = "Invalid jar type, has to be one word, without numbers";
        String successMessage = "Jar type '%s' is accepted%n";
        do {
            System.out.println("Administrator, " +
                    "Please specify type of item to be stored in the jar:");
            typeOfJarItems = promptForString();
            typeOfItemInputIsAccepted = checkStringForPattern(
                    typeOfJarItems, "^[a-zA-Z]+$");
            if (!typeOfItemInputIsAccepted) {
                System.out.println(errorMessage);
            }
        } while (!typeOfItemInputIsAccepted);

        System.out.printf(successMessage,typeOfJarItems.trim());
        return typeOfJarItems.trim();
    }

    // As an administrator of the game I should be able to specify jar capacity
    // returns trimmed jarCapacity
    public int askAdminForCapacityOfJar() throws IOException {
        String jarCapacityString;
        boolean jarCapacityIsAccepted = false;
        String errorMessage = "Invalid number of items, has to be an integer > 0";
        do {
            System.out.println("Administrator, " +
                    "Please specify jar capacity: ");
            jarCapacityString = promptForString();
            jarCapacityIsAccepted =
                    checkStringForPattern(jarCapacityString, "^[1-9]\\d*$");
            if (!jarCapacityIsAccepted) {
                System.out.println(errorMessage);
            }
        } while (!jarCapacityIsAccepted);
        int jarCapacity = Integer.valueOf(jarCapacityString.trim());
        mHighScore = jarCapacity + 1;
        return jarCapacity;
    }

    // sets playerName trimmed. Checks for any valid characters, accepts
    // has to be used before main askPlayerToGuessNumberOfItemsInJar(Jar jar)
    // everything any String without spaces
    public void setPlayerName() throws IOException {
        boolean playerNameIsAccepted = false;
        String errorMessage = "Invalid name, has to be anything except string with spaces";
        do {
            System.out.println("Please enter your name: ");
            mPlayerName = promptForString();
            playerNameIsAccepted = checkStringForPattern(mPlayerName,"^[a-zA-Z]\\S+$");
            if (!playerNameIsAccepted) {
                System.out.println(errorMessage);
            }
        } while (!playerNameIsAccepted);
        mPlayerName = mPlayerName.trim();
    }
    // helpful function used in to check that user input is a number
    // askPlayerToGuessNumberOfItemsInJar(Jar jar)
    // returns valid number
    private Logger mLogger;

    private int checkIfUserInputIsNumber() throws IOException {
        String userGuess;
        boolean userGuessIsAccepted = false;
        String errorMessage = "Invalid number, has to be an integer > 0";
        do {
            System.out.println("'" + mPlayerName + "', " +
                    "please guess how many jar items are in the game: ");
            userGuess = promptForString();
            userGuessIsAccepted = checkStringForPattern(userGuess, "^[1-9]\\d*$");
            if (!userGuessIsAccepted) {
                mLogger.setErrorMessage(errorMessage);
            }
        } while (!userGuessIsAccepted);
        int acceptedUserGuess = Integer.valueOf(userGuess.trim());
        return acceptedUserGuess;
    }
    // helpful function in askPlayerToGuessNumberOfItemsInJar(Jar jar)
    // it sets high score if user has guessed in less tries than
    // player before (first player will be best anyway)
    // prints some encouraging messages if there is someone to beat
    private void checkForHighScoreAndPlayerWithHighScore() {
        if (mNumberOfGuesses < mHighScore) {
            mHighScore = mNumberOfGuesses;
            mPlayerWithHighestScore = mPlayerName;
            mLogger.setSuccessMessage("Nailed it, '" + mPlayerName + "'! You've guessed in " + mNumberOfGuesses + " tries!");
        } else if (mNumberOfGuesses == mHighScore) {
            mLogger.setSuccessMessage( "Draw! You've reached '" +
                            mPlayerWithHighestScore +
                            "'(-s) high score! Try to beat him next time" );
        } else {
            mLogger.setSuccessMessage("Be better! Try to beat '" +
                    mPlayerWithHighestScore +
                    " ' with '" + mHighScore + "' tries!");
        }
    }
    // main game function when player is asked for
    // how many jar items are in the jar
    // requires Jar class as an argument
    public void askPlayerToGuessNumberOfItemsInJar(Jar jar) throws IOException {
        mNumberOfGuesses = 0;
        int acceptedUserGuess;
        do {
            System.out.printf("Jar capacity is %d %s(-s)%n",
                    jar.getCapacity(), jar.getTypeOfItems());
            acceptedUserGuess = checkIfUserInputIsNumber();
            if (acceptedUserGuess > jar.getCapacity()) {
                mLogger.setErrorMessage("Your guess exceeds Jar capacity");
            } else {
                if (acceptedUserGuess < jar.getNumberOfItems()) {
                    mLogger.setHintMessage("Try to use higher number");
                } else if (acceptedUserGuess > jar.getNumberOfItems()) {
                    mLogger.setHintMessage("Try to use lower number");
                }
                mNumberOfGuesses++;
            }
        } while (acceptedUserGuess != jar.getNumberOfItems());
        checkForHighScoreAndPlayerWithHighScore();
    }


    // Asks player to finish game.
    // returns true if game is to be over
    // returns false if game is not over yet
    public boolean askPlayerToFinishGame() throws IOException {
        String answer = "";
        boolean answerIsAccepted = false;
        do {
            System.out.println("Do you want to finish the game ('y' or 'n') ? ");
            answer = promptForString();
            answerIsAccepted = checkStringForPattern(answer,"^[yn]$");
        } while (!answerIsAccepted);
        if (answer.equals("y")) {
            return true;  // game is over
        } else {
            return false; // game is not over
        }
    }

    protected String getPlayerName() {
        return mPlayerName;
    }
}
