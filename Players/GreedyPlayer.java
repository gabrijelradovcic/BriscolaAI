

package Players;
import GameEngine.Briscola;
import NeuroEvolution.Game.GameState;


public class GreedyPlayer extends Player{

    public GreedyPlayer() {
        super("GreedyPlayer");

    }

    public int getDecision(GameState state,Briscola game){
        //check if the player is moving first or second
        boolean first=true;
        for(int i=0;i<state.usedCardnumbers.length;i++){
            if(state.usedCardnumbers[i]==0){
                //if i is even the player is moving first
                if(i%2==0){
                    first=true;
                }
                else{
                    first=false;
                }
                break;
            }
        }
        int decision=0;
        if(first){
            decision= movingFirst(state);
        }
        else{
            decision= movingSecond(state);
        }
        int[] hand=state.yourCardnumbers;
        if(hand[0]==0&&hand[1]==0&&hand[2]==0){
            System.out.println("Error with hand");
            return -1;
        }
        while(hand[decision]==0){
            decision=(int)(Math.random()*hand.length);
        }
        return decision;
    }


    public int movingFirst(GameState state){
        //if you have a card that has the same suit as the briscola
        //play it
        //else play the lowest card
        for(int i=0;i<state.yourCardnumbers.length;i++){
            if(state.yourCardsuits[i]==state.BriscolaSuit){
                return i;
            }
        }
        
        int lowest=0;
        for(int i=0;i<state.yourCardnumbers.length;i++){
            if(strengths[state.yourCardnumbers[i]]<strengths[state.yourCardnumbers[lowest]]){
                lowest=i;
            }
        }
        return lowest;

    }

    public int movingSecond(GameState state){
        //if you have a card that beats the opponent card
        //play it
        //else play the lowest card

        //get the opponent card
        int opponentCard=0;
        int opponentCardSuit=0;
        for(int i=0;i<state.usedCardnumbers.length;i++){
            if(state.usedCardnumbers[i]==0){
                opponentCard=state.usedCardnumbers[i-1];
                opponentCardSuit=state.usedCardsuits[i-1];
                break;
            }
        }
        //if the opponent card is the briscola see if you have a card that beats it
        if(opponentCardSuit==state.BriscolaSuit){
            for(int i=0;i<state.yourCardnumbers.length;i++){
                if(state.yourCardsuits[i]==state.BriscolaSuit){
                    if(strengths[state.yourCardnumbers[i]]>strengths[opponentCard]){
                        return i;
                    }
                }
            }
            //if none are found play the lowest card
            int lowest=0;
            for(int i=0;i<state.yourCardnumbers.length;i++){
                if(strengths[state.yourCardnumbers[i]]<strengths[state.yourCardnumbers[lowest]]){
                    lowest=i;
                }
            }
            return lowest;
        }
        else{
            //if not see if you have a card that is the same suit as the briscola
            int index=0;
            boolean found=false;
            for(int i=0;i<state.yourCardnumbers.length;i++){
                if(state.yourCardsuits[i]==state.BriscolaSuit){
                    //pick the lowest card
                    if(!found){
                        index=i;
                        found=true;
                    }
                    else{
                        if(strengths[state.yourCardnumbers[i]]<strengths[state.yourCardnumbers[index]]){
                            index=i;
                        }
                    }
                    
                }
            }
            if(found){
                return index;
            }
            else{
                //if none are found play the lowest card
                int lowest=0;
                for(int i=0;i<state.yourCardnumbers.length;i++){
                    if(strengths[state.yourCardnumbers[i]]<strengths[state.yourCardnumbers[lowest]]){
                        lowest=i;
                    }
                }
                return lowest;
            }

        }

    }
    public static int[] strengths ={0,11,0,10,0,0,0,0,0,0,0,2,3,4};

    
}
