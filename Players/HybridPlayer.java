
package Players;
import GameEngine.Briscola;
import NeuroEvolution.Game.GameState;


public class HybridPlayer extends Player {

    private NeuralPlayer neuralPlayer;
    private MonteCarloPlayer monteCarloPlayer;
    
    public static int switchpoint=30;

    public HybridPlayer(){
        super("HybridPlayer");
        neuralPlayer = new NeuralPlayer();
        monteCarloPlayer = new MonteCarloPlayer();
        
    }

    public int GamestateToTurn(GameState state){
        for(int i =0;i<state.usedCardnumbers.length;i++){
            if(state.usedCardnumbers[i]==0){
                return i+1;
            }
        }
        for(int i =0;i<state.yourCardnumbers.length;i++){
            if(state.yourCardnumbers[i]==0){
                return i+38;
            }
        }
        return 40;
    }

    public int getDecision(GameState state,Briscola game){
        int decision=0;
        if(GamestateToTurn(state)<switchpoint){
            decision=neuralPlayer.getDecision(state,game);
        }
        else{
            decision= monteCarloPlayer.getDecision(state, game);
        }

        int[] cards = state.yourCardnumbers;
        //if all cards are used, throw error
        if(cards[0]==0&&cards[1]==0&&cards[2]==0){
            System.out.println("Error: All cards are used");
            return -1;
        }
        //if the decision is not valid, select a random card
        while(cards[decision]==0){
            decision = (int) (Math.random()*3);
        }
        return decision;
    }

}



    

