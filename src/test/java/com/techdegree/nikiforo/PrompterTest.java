package com.techdegree.nikiforo;

import com.techdegree.nikiforo.log.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.mockito.ArgumentCaptor;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PrompterTest {
    private Prompter mPrompter;
    private Jar mJar;
    private BufferedReader mBufferedReader;
    private Logger mLogger;

    @Before
    public void setUp() throws Exception {
        mBufferedReader = mock(BufferedReader.class);
        mLogger = mock(Logger.class);
        mPrompter = new Prompter(mBufferedReader,mLogger);
    }

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.printf("%n -------- Starting test: %s %n",description.getMethodName());
        }
    };

    private String  createPrompterUsingGivenStringPutIntoBufReader(String userInput) {
        InputStream inputStream = new ByteArrayInputStream(userInput.getBytes());
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        mPrompter = new Prompter(bufferedReader,mLogger);
        return userInput;
    }

    @Test
    public void stringWithValueTestIsProcessedCorrectlyByBufferedReader() throws Exception {
        createPrompterUsingGivenStringPutIntoBufReader("test");
        assertEquals("test",mPrompter.promptForString());
    }

    @Test
    public void anyCharacterPatterWorksWithLetterA() throws Exception {
        String userInput = createPrompterUsingGivenStringPutIntoBufReader("a");
        mPrompter.checkStringForPattern(userInput,".");
    }

    @Test
    public void mockingPromptForStringToReturnIoException() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        Prompter prompter = new Prompter(bufferedReader,mLogger);
        doThrow(new IOException("user")).when(bufferedReader).readLine();
        expectedException.expect(IOException.class);
        expectedException.expectMessage("Problems with reading user input");
        prompter.promptForString();
//        catchException(() -> prompter.promptForString());
//        assert caughtException() instanceof IOException;
    }

    @Test
    public void givingRightTypeOjJarItemsPassesThroughAskAdminMethod() throws Exception {
        String userInput = createPrompterUsingGivenStringPutIntoBufReader("jar");
        String typeOfJarItem = mPrompter.askAdminForTypeofItemsInJar();
        assertEquals(userInput.trim(),typeOfJarItem);
    }
    @Test
    public void givingNumberSpaceAndThenRightTypeWorksWithMock() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        Prompter prompter = new Prompter(bufferedReader,mLogger);
        when(bufferedReader.readLine()).thenReturn("1").thenReturn(" ").thenReturn("jar");
        String typeOfJarItem = prompter.askAdminForTypeofItemsInJar();
        assertEquals("jar",typeOfJarItem);
    }

    @Test
    public void givingSpaceStringAndThenRightTypeOfJarItemsWorksWithBufReaderMock() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        Prompter prompter = new Prompter(bufferedReader,mLogger);
        when(bufferedReader.readLine())
                .thenReturn("jar")
                .thenReturn(" ")
                .thenReturn("1");
        mJar = new Jar("jar",1);
        int jarCapacity = prompter.askAdminForCapacityOfJar();
        assertEquals(mJar.getCapacity(),jarCapacity);
    }
    @Test
    public void givingSpaceStringThenNumberAndThenRightPlayerNameWorksWithBufReaderMock() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        Prompter prompter = new Prompter(bufferedReader,mLogger);
        when(bufferedReader.readLine())
                .thenReturn("1")
                .thenReturn(" ")
                .thenReturn("alex123");
        prompter.setPlayerName();
        String playerName = prompter.getPlayerName();
        assertEquals("alex123",playerName);
    }


    private void setUpSimpleGameWithPlayerNameAlexAndJarWithTwoItems() throws Exception {
        when(mBufferedReader.readLine())
                .thenReturn("alex123");
        mPrompter.setPlayerName();
        mJar = new Jar("jar",2);
        System.out.printf("Test with %d items in %d tries",
                mJar.getCapacity(),mJar.getCapacity());
    }
    @Test
    public void higherHintIsShownInSimpleOneTwoJarGame() throws Exception {
        setUpSimpleGameWithPlayerNameAlexAndJarWithTwoItems();
        mJar.setNumberOfItems(2);
        when(mBufferedReader.readLine())
                .thenReturn("1")
                .thenReturn("2");
        mPrompter.askPlayerToGuessNumberOfItemsInJar(mJar);
        verify(mLogger).setHintMessage(contains("higher"));
    }
    @Test
    public void lowerHintWorkIsShownInSimpleTwoOneJarGame() throws Exception {
        setUpSimpleGameWithPlayerNameAlexAndJarWithTwoItems();
        mJar.setNumberOfItems(1);
        when(mBufferedReader.readLine())
                .thenReturn("2")
                .thenReturn("1");
        mPrompter.askPlayerToGuessNumberOfItemsInJar(mJar);
        verify(mLogger).setHintMessage(contains("lower"));
    }
    @Test
    public void invalidNumberErrorReturnedWhenSpacePassedInJarGame() throws Exception {
        setUpSimpleGameWithPlayerNameAlexAndJarWithTwoItems();
        mJar.setNumberOfItems(1);
        when(mBufferedReader.readLine())
                .thenReturn(" ")
                .thenReturn("1");
        mPrompter.askPlayerToGuessNumberOfItemsInJar(mJar);
        verify(mLogger).setErrorMessage(contains("Invalid number"));
    }
    @Test
    public void givenMoreItemsThanThereIsInJarGivesExceedsError() throws Exception {
        setUpSimpleGameWithPlayerNameAlexAndJarWithTwoItems();
        mJar.setNumberOfItems(1);
        when(mBufferedReader.readLine())
                .thenReturn("3")
                .thenReturn("1");
        mPrompter.askPlayerToGuessNumberOfItemsInJar(mJar);
        verify(mLogger).setErrorMessage(contains("exceeds Jar capacity"));
    }

    @Test
    public void passingYafterSpaceReturnsTrueWhenPlayerIsAskedToFinishTheGame() throws Exception {
        when(mBufferedReader.readLine())
                .thenReturn(" ")
                .thenReturn("y");
        boolean gameIsOver = mPrompter.askPlayerToFinishGame();
        assertEquals(true,gameIsOver);
    }
    @Test
    public void passingNafterNumberReturnsFalseWhenPlayerIsAskedToFinishTheGame() throws Exception {
        when(mBufferedReader.readLine())
                .thenReturn(" ")
                .thenReturn("n");
        boolean gameIsOver = mPrompter.askPlayerToFinishGame();
        assertEquals(false,gameIsOver);
    }
    @Test
    public void playerWithLowerScoreGetsBeBetterSuccessMessage() throws Exception {
        setUpSimpleGameWithPlayerNameAlexAndJarWithTwoItems();
        mJar.setNumberOfItems(2);
        mPrompter.setHighScore(1);
        mPrompter.setPlayerWithHighestScore("playerWithHighScore");
        when(mBufferedReader.readLine())
                .thenReturn("1")
                .thenReturn("2");
        mPrompter.askPlayerToGuessNumberOfItemsInJar(mJar);
        verify(mLogger).setSuccessMessage(contains("Be better"));
    }
    @Test
    public void playerWithSameScoreGetsDrawSuccessMessage() throws Exception {
        setUpSimpleGameWithPlayerNameAlexAndJarWithTwoItems();
        mJar.setNumberOfItems(2);
        mPrompter.setHighScore(2);
        mPrompter.setPlayerWithHighestScore("playerWithHighScore");
        when(mBufferedReader.readLine())
                .thenReturn("1")
                .thenReturn("2");
        mPrompter.askPlayerToGuessNumberOfItemsInJar(mJar);
        verify(mLogger).setSuccessMessage(contains("Draw"));
    }
    @Test
    public void playerWithBetterScoreGetNailedItSuccessMessage() throws Exception {
        setUpSimpleGameWithPlayerNameAlexAndJarWithTwoItems();
        mJar.setNumberOfItems(2);
        mPrompter.setHighScore(2);
        mPrompter.setPlayerWithHighestScore("playerWithHighScore");
        when(mBufferedReader.readLine())
                .thenReturn("2");
        mPrompter.askPlayerToGuessNumberOfItemsInJar(mJar);
        verify(mLogger).setSuccessMessage(contains("Nailed it"));
    }
}