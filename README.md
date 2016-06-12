# pt1-jar-game-with-testing
project techdegree #1 with unit testing. Main class `Prompter` is tested up to 92 %.
New `Logger` class is used mostly to test classes (see tests where Hint, Error and Success
messages are used). `Game` class contains `public static void main` function, and its not tested.
The Logger class is also not tested, as used as a simple writer function. All `protected` methods
are done so in order to be tested.
# Game itself 
Main purpose of the game is to guess how many jar items are there in the Jar.
Here are steps that are performed in `Game` class:
- Create new `Prompter` object - our Controller method.
- Ask administrator for type of items in Jar.
- Ask administrator for capacity of Jar (max possible number of Jar items).
- Then starts primary `do...while` loop in which:
  - player Name is set;
  - new Jar is set, and randomly filled with items (from 1 to Jar capacity);
  - player is asked to guess number of items in jar (primary game method);
  - when player guesses the right number of items, he is asked to finish the game;
- In the end best player name and score are printed
