#[Anki]

Anki is a spaced repetition flashcard program. Anki (暗記) is the Japanese word for memorization.

A student is proposed a set of cards, one face of the card contains a question, the other face the answer.
The student studies the question, tries to guess the answer and then looks if the answer he guessed was correct.

The knowledge of a card follows the Fibonacci sequence, starting by 1. The knowledge of a card cannot be less than 1. 

We define Kcx as the current Knowledge of a Card X
if the student did not know the answer of the Card X, he places the card in a red box. The knowledge of this card is considered bad and the knowledge Kcx dicreases of two steps in the Fibonacci sequence
if the student did know a part of the answer, he places the card in an orange box, his knowledge of this card is considered ok, and the knowledge Kcx increases of one step in the Fibonacci sequence
if the student did know the answer, he places the card in an green box, his knowledge of this card is considered perfect and the knowledge Kcx increases of two steps in the Fibonacci sequence
A Card X that is answered will be shown again to the student in (Kcx-1) sessions.

For example, a student studies a new card "Where is Brian ?" -> "In the kitchen", and he places it to the ORANGE box, the Knowledge of the card changes from 1 to 2. 
The card will be shown again to the student in the next session.
On the next session, if he studies the same card and places it to the GREEN box, the Knowledge of the card changes from 2 to 4. The card will be studied again in 3 sessions. 

The goal of the program is: based on a file containing the evaluations of a user for a set of cards, to determine for each card in which session it will be asked in the future, plus the current knowledge of the cards.

Example of input :
```
session | card question                            | card answer | evaluation
1       | (A) What is the first name of Dicaprio ?           | Leonardo        | GREEN
1       | (B) What is the number between one and three ?           | Two      | ORANGE
1       | (C) What is french translation for 'apple' ? | Pomme       | RED
1       | (C) What is french translation for 'apple' ? | Pomme       | ORANGE
2       | (C) What is french translation for 'apple' ? | Pomme       | GREEN
2       | (B) What is the number between one and three ?           | Two      | ORANGE
3       | (A) What is the first name of Dicaprio ?           | Leonardo        | GREEN
```
The output of the program :
```
card question | card | next session | current knowledge
What is the first name of Dicaprio ? | Leonardo | 7 | 5
What is the number between one and three ? | Two | 4 | 3
What is french translation for 'apple' | Pomme | 5 | 4
```
You are invited to share your result on github using a Pull request toward our repository https://github.com/smartholiday/anki
