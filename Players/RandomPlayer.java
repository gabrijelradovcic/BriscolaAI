package Players;

import GameEngine.Briscola;
import NeuroEvolution.Game.GameState;


public class RandomPlayer extends Player{

    public RandomPlayer() {
        super("RandomPlayer");
    }

    public int getDecision(GameState state,Briscola briscola) {
        int[] cards = state.yourCardnumbers;
        //if all cards are used, throw error
        if(cards[0]==0&&cards[1]==0&&cards[2]==0){
            System.out.println("Error: All cards are used");
            return -1;
        }
        //select a random card
        int card = (int) (Math.random()*3);
        while(cards[card]==0){
            card = (int) (Math.random()*3);
        }
        
        return card;
    }
    
}
