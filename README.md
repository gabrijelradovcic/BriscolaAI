Gabrijel Radovcic

To run our code you should extract this zip file to your desktop. Then you should run it using GUI class.(GUI.java)
Game logic is not working in MAC because it has different way of storing path and then when we use File Visitior
it reads cards in different order.


Make sure you have the latest javafx library installed and connected to it, if you don't know how to do it in the
following video you have instructions to do it:
https://youtu.be/H67COH9F718

# playing the game
once you have Javafx all setup please run GUI.java this will bring up the main menu of the game, there you can choose what kind of game you want to play. human v human, human v bot or bot v bot based on what you select. from there on you can press play and the game will start playing

# Implemented algorithms
we have implemented 6 kinds of players here are descriptions of what they do

# monte carlo tree search
As the name already suggests the algorithm makes use of a widely known method in statistics, namely Monte Carlo simulation. This principle is utilized to forecast probabilistic outcomes in the case that random variables are present, averaging its results to yield a good estimate. The MCTS consists of four defined steps, starting with selecting a root node and traversing down the best child node(s). Hereafter, the branch is expanded in the case that the selected child node is not one where the game ends, by running the third step of simulating the play-out until the game's end. Finally, the taken route is updated with the scores that the simulation yielded for a specified number of iterations \cite{web:mcts}. Figure 1 shows a schematic representation of the working of the MCTS, clearly indicating the four distinct steps mentioned before. 

# Neuro Evolution 
ENN makes use of the workings of Artificial Neural Networks (ANN) combined with evolutionary algorithms. These networks are inspired by neurons as found in the human brain, which function by subjecting incoming weighted signals to an activation function. From this principle, the ENN utilizes the learning principle of the ANN and combines it with the evolutionary motives stemming from biology to enhance performance in dynamic situations \cite
Although this selected algorithm is relatively new in the domain of machine learning, e.g. has not been researched thoroughly and widely applied yet, its workings have been well defined. The ENN works in theory much like a Genetic algorithm, It Iterates over population scoring them on performance and creating new generation based on these scores. where the main difference lies is in its output. unlike in Genetic algorithms it does not try and optimized set variables, it tries to optimize Neural Networks. The first advantage of this is that the output won't be limited by the variables set by its programmer and his understanding of what variables should influence decision making.
the second advantage of having Neural nets as output is that their output can be interpreted to allow for more complex decision making. for example it could in theory make multiple decisions at the same time that require different kinds of outputs if the scenario calls for this kind of decision making. 
the disadvantage of a neural network trained by ENN is that its inner working are hard to decipher the more complex an individuals decision making becomes. so it will have great decision making but we can't deduce how we could do this ourselves.

To train the neural net run MAIN.java, its output can be seen in input.txt and will be used be neuralPlayer for in game calculations

## Game 
this file contains a more optimised version of briscola for training.
## Tournament
this file contains the means of evaluating the Neural networks performance
## Crossover 
This file contains Variables related to creating offspring and the next generation
## Mutation
This file contains various mutation methods for the Neural networks


# Q learning
The second ML algorithm is Q-Learning, which makes use of reinforcement learning. This algorithm learns, through trial and error, how to solve our Briscola multi-level problem and hence, approximate a real-life agent Because it interacts with the environment, it takes decisions sequentially and is therefore capable of measuring rewards and selecting the optimal action at each time. As the chosen card game has a large multitude of strategies, this algorithm will quickly help reduce the decision tree by leaving out unrewarding moves. The algorithmic pseudo-code is derived from Rusell et al. \cite{book:ai-ama}. We should note that   is an exploration function that regulates the trade-off between exploring new steps or relying on already evaluated steps. 
For Q-Learning, the algorithm will determine a reward or penalty for each action a that has been taken in state s. The agent selects the action with a maximum Q-value for a given state for each iteration, evaluating how good the action was when proceeding to the next state \cite{art:qlearning}. To avoid the agent getting trapped in a certain space, a random greedy probability  factor is added. This allows the algorithm to also learn new spaces, even if these are not the most rewarding actions. Following from this variable, the algorithm makes a balanced consideration between exploitation (via taking the highest Q-values) and exploration (via taking the random greedy approach). As Figure 3 illustrates, the algorithm ends when the endpoint is reached - doing this for the number of specified iterations.


## variants
We implemented 2 variants of this algorithms

### global (QPlayer1)
this one can be trained before the game is started on random games, which causes it to act like a greedy player. can be trained by running Qlearning.java which will output a trained file which will be used when initializing this player(we didnt upload a trained version as the file was too large for github)
### local (QPlayer2)
this one is trained on the spot when initialized using the deck you are planning on playing, this will be better on average

# hybrid
This player consists of the ENN and MCTS. it will use the ENN for turns 1-30 and MCTS for turn 31-40

# greedy player
This is a baseline player which tries to score points as fast as possible
# random player
Another baseline player which plays moves at random


# statistics
all our statistics were done by files stored in Statistics which are the following
## CardsStatistics
a file which test what cards the winner draws on average
## performance statistics and performancegame
Files for testing the performance of the bots in relation to calculation time and startup time
## Statistics
files for testing bots against eachother
