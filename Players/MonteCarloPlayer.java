package Players;
import GameEngine.Briscola;
import MonteCarlo.MonteCarloSearchTree;
import NeuroEvolution.Game.GameState;
public class MonteCarloPlayer extends Player {

    public MonteCarloPlayer() {
        super("MonteCarloPlayer");
    }

    public int getDecision(GameState state,Briscola briscola) {
        MonteCarloSearchTree tree = new MonteCarloSearchTree();
        int decision=tree.getDecision(briscola);
        //if the decision is not valid, return a random valid decision
        //if the decision is not valid (i.e. the card is not in the hand) then choose a random valid card
        if(state.yourCardnumbers[decision]==0){
            int validCards=0;
            for(int i=0;i<3;i++){
                if(state.yourCardnumbers[i]!=0){
                    validCards++;
                }
            }
            int randomCard=(int)(Math.random()*validCards);
            int counter=0;
            for(int i=0;i<3;i++){
                if(state.yourCardnumbers[i]!=0){
                    if(counter==randomCard){
                        decision=i;
                        break;
                    }
                    counter++;
                }
            }
        }
        return decision;
        
    }
    

  

    public static int[] strengths= {0,11,0,10,0,0,0,0,0,0,0,2,3,4};
    public int cardNumberToStrength(int cardNumber){
        return strengths[cardNumber];
    }
}
