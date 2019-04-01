# CMPT276-1191E1-Eta

### Initial Goal
As a student, I want to be able to learn Chinese by playing a Sudoku style game. I want to get familiar with the words by playing this game.
Example: The game will have two modes, students can choose the mode they like to play the game. It can help the student to get familiar with the languages they want.

### First Game
As a language learner, I would like to start with the numbers at first. I want to learn 1 - 9 in Chinese.
Example: User will input numerical number into a Chinese puzzle, and input Chinese numbers into a regular puzzle to play.

### Puzzle End
As a student, I need to know when I have the correct solution to learn the language properly.  
Example: There is a "submit" button that will check if the current solution is correct.

### Selection GUI
As a student, I want to be able to select a desired number to place on the board.  
Example: A 3 by 3 grid will be available at the bottom of the screen to pick a desired number to fill in a blank spot.  
The student can select the desired number from the 3 x 3 grid then select board position to set board values.  
Example: A "delete" button is available so the user can press it then the desired position on the board to clear that spot.
         

### Language Selection
As a language learner, I want to be able to peek at the correct translation of a word, so that I can try to remember it in filling out the puzzle.  
Example: A "switch" button is available to switch the languages back and forth. This way, the learner can peak as they play the game.  
Note: Variable is available in MainActivity.java to switch initial lanugages.

### ITERATION 2

### Listening Comprehension

As a student who wants to practice my understanding of spoken words in the language that I am learning, I want a listening comprehension mode. In this mode, numbers will appear in the prefilled cells and the corresponding word in the language that I am learning will be read out to me when I press the number.

### Vocabulary Lists

As a teacher, I want to specify a list of word pairs for my students to practice this week.
As a student working with a textbook, I want to load pairs of words to practice from each chapter of the book.
As a student, I want the Sudoku app to keep track of the vocabulary words that I am having difficulty recognizing so that they will be used more often in my practice puzzles.

### Different Devices

As a vocabulary learner practicing at home, I want to use my tablet for Sudoku vocabulary practice, so that the words will be conveniently displayed in larger, easier to read fonts.
As a vocabulary learner taking the bus, I want to use my phone in landscape mode for Sudoku vocabulary practice, so that longer words are displayed in a larger font that standard mode.

### ITERATION 3

### Challenge mode

As a teacher who wants to make sure my student understands the vocabs, student must pass some tests. I want a challenge mode where the student must complete my difficulty requirement in certain amount of time.
If student completes them succcesfully, they pass the test. If they don't, they will fail and must try again.

Example: A student need to pass a 9x9 puzzle in under 10 minutes, then the 9 words in this puzzle will consider learned or understood.

Given:challenge mode is enable
When:user initate a puzzle with timer count down
Then:user must complete the puzzle within certain amount of time to pass, else fail


As a teacher, I want the hints be off on the Challenge mode.

Example: No more hints when you click on prefilled cells, and other pop ups.

Given:challenge mode is enable
When:user fill in the sudoku cells
Then:hints will be complete off when user is playing the sudoku

### Pratice mode

As a student who wants to pass the challenge. I want a pratice mode where I can pratice with no time limit but has a timer on the side and tells me how much time I spent on the game in the end.

Example: Student can pratice on any puzzle. It will have all hint functions and a timer on the side. When it is complete they will know their time spent.

Given:practice mode enable
When:user initialize a puzzle
Then:user gets will get all hints enable and have a timer on the side to show time spent.

### Useful feature

As a student who forgot my last number placed and want to go back, I want a button to back to it.

Example: Student erased a 5 and put down a 7, they want to go back from 7 to 5 so they click undo.

Given:that the user is filling in the sudoku in any mode
      and student erased a 5
      and put down a 7 on the same cell
When:user want to go back
Then:press the undo and the cell will change from 7 back to 5

As a student who messed up most of the puzzle, I would like to clean all the filled cells and start over.

Example: Student have to many error on the puzzle and doesn't want to fix one by one so they clean all.

Given:the user is filling in sudoku in any mode
When:user want to clear all cells and fill all over again
Then:press the reset button to clear all cells

As a student who clicked undo and figure it's not what I wants, I want a button to revert the change.

Example: Student erased a 5 and put down a 7, click undo and go back to 5. then he wants to revert it back to 7, so he clicks revert.

Given:student erased a 5 and put down a 7 in the same cell
      undo button is pressed
When:student want to revert the undo
Then:press the revert

### Update vocab list

As a teacher who wants student to learn Chinese, I would like to have a good mix of different categories of words.

Example: I want more groups of words, like animals, plants, or others.


As a teacher who wants student to learn better, I would like to have my set words for student to learn each week.

Example: I want to pick my own set of words for my student each week for them to learn.

