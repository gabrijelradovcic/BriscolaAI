package Players;



import GameEngine.Briscola;
import GameEngine.Card;
import NeuroEvolution.Game.GameState;



public class Player {
    public Card[] hand = new Card[3];
    public int points = 0;
    private String name;
    private Card lastTrownCard;
    public Player(String name){
        this.name = name;
    }
    public void addCard(Card c){
        for(int i=0;i<hand.length;i++){
            if(hand[i]==null){
                hand[i]=c;
                break;
            }
        }
    }
    public int getNumberOfCards(){
        int counter = 0;
        for (int i = 0; i < hand.length; i++) {
            if(hand[i]!=null){
                counter++;
            }
        }
        return counter;
    }
    public void removeCard(int i){
        lastTrownCard = hand[i];
        hand[i]=null;
    }
    public Card getLastTrownCard(){
        return lastTrownCard;
    }

    public Card getCard(int i){
        return hand[i];
    }
    public String getName(){
        return name;
    }

    public boolean isHandEmpty(){
        for(int i = 0; i < 3; i++){
            if(hand[i]!=null){
                return false;
            }
        }
        return true;
    }

    public int getPoints(){
        return points;
    }

    public void addPoints(int newpoints){
            points += newpoints;
    }
    public int getDecision(GameState state,Briscola game){
        return 0;
    }

    public Player clone(){
        Player player_new = new Player(name);
        player_new.hand = new Card[3];
        player_new.hand[0] = this.hand[0];
        player_new.hand[1] = this.hand[1];
        player_new.hand[2] = this.hand[2];
        player_new.points = this.points;
        return player_new;
    }
}
