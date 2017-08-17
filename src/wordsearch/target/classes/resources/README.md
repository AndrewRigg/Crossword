Program to auto-generate crosswords.   "CrosswordGenerator" is the program you need to run.

Version 1 has following limitations:   

(1) Clues are a bit naff. I have downloaded a wordlist of >100,000 "words" and then loaded these into the Cambridge online dictionary (dictionary.cambridge.org). If it found a definition I stored this as the "clue" and ignored the word otherwise. Left me with about 17,000 "words". Could get clues also from other dictionaries / a thesaurus / anagram generator. Also, since some dictionary definitions actually contain the word, I searched each clue for that String and replaced it with asterisks which causes its own problems! Meh!

(2) I haven't learned how to use Java's swing library properly yet. In a later version you will be able to enter answers into a grid and be able to check them as you go along. For now, it just prints a crossword grid and the clues onto the screen. Doesn't even have a scroll bar...

(3) Algorithm for fitting words could be improved. At the moment it will always try and fit the largest possible word, which leads to the same words appearing more often than you would expect from a list of 17,000 words.

(4) Words are thrown down randomly and only after a certain number of tries do we check if the grid is "connected" using the Hoshen-Kopeleman algorithm. If it's not, I just clear the grid and try again. This is fine for small crosswords, but as they get bigger (> 30x30) this can make the process very slow.

  
   
   
Following files perform the following functions:
