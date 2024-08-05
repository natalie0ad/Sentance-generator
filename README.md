Final project for my data structures and algorithms class as a freshman

Task: Create a Java program that generates text with similar characteristics to an input text file.
Implement at least two classes: one for the model (processing input and generating text) and another for the command-line application (user interface with the model).

  For the text file you have to lowercase all letters and strip punctuation.
  Words are sequences of characters without whitespace or punctuation.

  Calculate the probability of a word following another based on occurrences in the input text.
  Use efficient data structures to store these probabilities.You input a file of such as a book this is then used to create the odds of the next word for each word

  Must use 3 or 4 args
  First arg: Path and name of the input text file.
  Second arg: Seed word (must be in the input file).
  Third arg: Number of words to generate (K).
  Fourth argument (optional): Determines output method (all for random selection, one for the most probable word).
  
  If no fourth argument, output the K most probable words following the seed.
  If the fourth argument is all, generate output by randomly selecting next words based on probabilities.
  If the fourth argument is one, always select the most probable next word, breaking ties lexicographically.
  In cases with no possible next word, reuse the seed word.

A large part of project was effiency, it was timed in comparison to classmates, part of grade was comparison to others times. 

