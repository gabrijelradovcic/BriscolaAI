package Statistics;	
import GameEngine.Briscola;
import Players.Player;
import NeuroEvolution.Game.*;

import java.util.ArrayList;

public class PerformanceGame {

    
    public NeuroEvolution.Game.Deck deck;
    public Player player1;
    public Player player2;
    public ArrayList<Double> player1ComputationTime;
    public ArrayList<Double> player2ComputationTime;
    public int player1score;
    public int player2score;
    public int[] usedCardsuits;
    public int[] usedCards;
    public int[] player1hand;
    public int[] player1handsuits;
    public int[] player2hand;
    public int[] player2handsuits;
    public int briscolaSuit;
    public NeuroEvolution.Game.Card briscola;
    public boolean player1turn=true;
    public static int[] values={0,11,0,10,0,0,0,0,0,0,0,2,3,4};
    public int index=0;
    public Briscola briscolaGame;


    public PerformanceGame(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        this.player1ComputationTime = new ArrayList<Double>();
        this.player2ComputationTime = new ArrayList<Double>();
        this.player1score = 0;
        this.player2score = 0;
        this.usedCardsuits = new int[39];
        this.usedCards = new int[39];
        this.player1hand = new int[3];
        this.player1handsuits = new int[3];
        this.player2hand = new int[3];
        this.player2handsuits = new int[3];
        this.briscolaSuit = 0;
        this.player1turn = true;
        this.deck = new NeuroEvolution.Game.Deck();
        this.briscolaGame=new Briscola(player1, player2);
    }

    public void resetInfo(){
        this.player1score = 0;
        this.player2score = 0;
        this.player1ComputationTime = new ArrayList<Double>();
        this.player2ComputationTime = new ArrayList<Double>();
        this.usedCardsuits = new int[39];
        this.usedCards = new int[39];
        this.player1hand = new int[3];
        this.player1handsuits = new int[3];
        this.player2hand = new int[3];
        this.player2handsuits = new int[3];
        this.briscolaSuit = 0;
        this.player1turn = false;
        this.deck = new NeuroEvolution.Game.Deck();
        this.briscola=null;
        this.index=0;
    }

    public GameState getGameState(boolean player1){
        if(player1){
            return new GameState(player1score, player2score, briscolaSuit, usedCards, usedCardsuits, player1hand, player1handsuits);
        }
        else{
            return new GameState(player2score, player1score, briscolaSuit, usedCards, usedCardsuits, player2hand, player2handsuits);
        }


    }

   

    public void addToUsed(boolean player1,int decision){
        if(index>38){
            index=0;
        }
        if(player1){
            usedCards[index]=player1hand[decision];
            usedCardsuits[index]=player1handsuits[decision];
            index++;
        }
        else{
            usedCards[index]=player2hand[decision];
            usedCardsuits[index]=player2handsuits[decision];
            index++;
        }
    }
    public void RemovechosenCard(boolean player1,int decision){
        if(player1){
            player1hand[decision]=0;
            player1handsuits[decision]=0;
        }
        else{
            player2hand[decision]=0;
            player2handsuits[decision]=0;
        }
    }

    public int getRandomChoice(boolean player1){
        if(player1){
            ArrayList<Integer> choices = new ArrayList<Integer>();
            for(int i=0;i<3;i++){
                if(player1hand[i]!=0){
                    choices.add(i);
                }
            }
            if(choices.size()==0){
                return -1;
            }
            return choices.get((int)(Math.random()*choices.size()));
        }
        else{
            ArrayList<Integer> choices = new ArrayList<Integer>();
            for(int i=0;i<3;i++){
                if(player2hand[i]!=0){
                    choices.add(i);
                }
            }
            if(choices.size()==0){
                return -1;
            }
            return choices.get((int)(Math.random()*choices.size()));
        }
    }


    public void addScore(boolean player1wins,int card,int opponentcard){
        if(card==0||opponentcard==0){
            System.out.println("error");
        }
        if(player1wins){
            player1score+=values[card];
            player1score+=values[opponentcard];
            
        }
        else{
            player2score+=values[opponentcard];
            player2score+=values[card];
            
        }
    }
    
    public void playGame(){
        briscola = deck.deal();
        briscolaSuit = briscola.suit();

        //deal cards
        for(int i=0;i<3;i++){
            Card temp=deck.deal();
            player1hand[i]=temp.rank();
            player1handsuits[i]=temp.suit();
            temp=deck.deal();
            player2hand[i]=temp.rank();
            player2handsuits[i]=temp.suit();
        }
        
        double start = System.currentTimeMillis();
        for(int i=0;i<100;i++){
            int decision = player1.getDecision(getGameState(true),briscolaGame);
        }
        double end = System.currentTimeMillis();
        player1ComputationTime.add((end-start)/100);
        return;
                

    }




    

    public Briscola getBriscola(Player player1, Player player2){
        //recreate the game in the form of the briscola class
        Briscola game = new Briscola(player1,player2);
        
        return game;
    }
    public void refillHand(int player1decision,int player2decision){

        //refills the player hands in order and uses the briscola if the deck is empty
        if(player1turn){
            Card temp = deck.deal();
            player1hand[player1decision]=temp.rank();
            player1handsuits[player1decision]=temp.suit();
            temp = deck.deal();
            if(temp==null){
                player2hand[player2decision]=briscola.rank();
                player2handsuits[player2decision]=briscola.suit();
                usedCards[0]=0;
                usedCardsuits[0]=0;
                return;
            }
            player2hand[player2decision]=temp.rank();
            player2handsuits[player2decision]=temp.suit();
        }
        else{
            Card temp = deck.deal();
            player2hand[player2decision]=temp.rank();
            player2handsuits[player2decision]=temp.suit();
            temp = deck.deal();
            if(temp==null){
                player1hand[player1decision]=briscola.rank();
                player1handsuits[player1decision]=briscola.suit();
                usedCards[0]=0;
                usedCardsuits[0]=0;
                return;
            }
            player1hand[player1decision]=temp.rank();
            player1handsuits[player1decision]=temp.suit();
        }
    }
    



    public void printRoundResult(boolean player1wins,boolean player1turn, int card, int suit, int opponentcard, int opponentsuit,int briscolla){
            String temp= player1turn?"Player 1":"Player 2";
            String temp2=player1wins?"Player 1":"Player 2";
            System.out.println(temp+" went first");
            System.out.println("round result: "+temp2+" wins");
            System.out.println("player1 card played: "+card+" "+suitToString(suit));
            System.out.println("player2 card played: "+opponentcard+" "+suitToString(opponentsuit));
            System.out.println("briscola: "+" "+suitToString(briscolaSuit));
            System.out.println("===================================");
            
    }

    public String suitToString(int suit){
        //converts the suit to a string
        if(suit==1){
            return "spades";
        }
        else if(suit==2){
            return "clubs";
        }
        else if(suit==3){
            return "hearts";
        }
        else{
            return "diamonds";
        }
    }

    //variable to decide what card should win based on its strength
    public static int[] strengths ={0,11,0,10,0,0,0,0,0,0,0,2,3,4};
    public boolean checkWin(int card, int suit, int opponentcard, int opponentsuit){
        //assumes card and suit are the player who went first
        if(suit==briscolaSuit){
            if(opponentsuit==briscolaSuit){
                if(strengths[card]>strengths[opponentcard]){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return true;
            }
        }
        else{
            if(opponentsuit==briscolaSuit){
                return false;
            }
            else{
                return true;
            }
        }
        
    }



        /* 
        public static void main(String[] args) {
            //test the game
            //initialize the neuralfactory
            NeuroEvolution.NeuralNetwork.NeuralFactory neuralFactory = NeuroEvolution.NeuralNetwork.NeuralFactory.getInstance();
        
            //make 2 player phenotypes
            NeuroEvolution.NeuralNetwork.Phenotype player2 = neuralFactory.createBasePhenotype(87,3);
            NeuroEvolution.NeuralNetwork.Phenotype player1 = neuralFactory.createBasePhenotype(87,3);
            player2.ProcessGraph();
            player1.ProcessGraph();

            NeuralPlayer player1neural = new NeuralPlayer(player1);
            NeuralPlayer player2neural = new NeuralPlayer(player2);
            //play the game
            BotBriscola game = new BotBriscola(player1neural, player2neural);
            double [] average = new double[2];
            double[] result=game.playGame();
            
  
            // System.out.println("Player 1 winrate: "+average[0]/100);
            // System.out.println("Player 2 winrate: "+average[1]/100);

            
            
        }*/
    
    
}
