package Q_learning;
import java.util.ArrayList;

import NeuroEvolution.Game.*;

public class QGame {
    
    public Deck deck;
    public QPlayerInterface player1;
    public QPlayerInterface player2;
    public ArrayList<Integer> Rewards;
    public ArrayList<Integer> statesPlayer1;
    public ArrayList<Integer> choicesPlayer1;
    public ArrayList<Integer> statesPlayer2;
    public ArrayList<Integer> choicesPlayer2;
    public int player1score;
    public int player2score;
    public int[] usedCardsuits;
    public int[] usedCards;
    public int[] player1hand;
    public int[] player1handsuits;
    public int[] player2hand;
    public int[] player2handsuits;
    public int briscolaSuit;
    public Card briscola;
    public boolean player1turn=true;
    public static int[] values={0,11,0,10,0,0,0,0,0,0,0,2,3,4};
    public int index=0;


    public QGame(QPlayerInterface player1, QPlayerInterface player2){
        this.Rewards=new ArrayList<Integer>();
        this.statesPlayer1=new ArrayList<Integer>();
        this.statesPlayer2=new ArrayList<Integer>();
        this.choicesPlayer1=new ArrayList<Integer>();
        this.choicesPlayer2=new ArrayList<Integer>();
        this.player1 = player1;
        this.player2 = player2;
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
        this.deck = new Deck();
    }

    public void resetInfo(){
        this.Rewards=new ArrayList<Integer>();
        this.statesPlayer1=new ArrayList<Integer>();
        this.statesPlayer2=new ArrayList<Integer>();
        this.choicesPlayer1=new ArrayList<Integer>();
        this.choicesPlayer2=new ArrayList<Integer>();
        this.player1score = 0;
        this.player2score = 0;
        this.usedCardsuits = new int[39];
        this.usedCards = new int[39];
        this.player1hand = new int[3];
        this.player1handsuits = new int[3];
        this.player2hand = new int[3];
        this.player2handsuits = new int[3];
        this.briscolaSuit = 0;
        this.player1turn = false;
        this.deck = new Deck();
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
            Rewards.add(values[card]);
            
        }
        else{
            player2score+=values[opponentcard];
            player2score+=values[card];
            Rewards.add(-values[card]);
            
        }
    }
    
    public double[] playGame(){
        //resetInfo();
        //deck.shuffle();
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
        int round=0;
        while(player1score<61&&player2score<61&& round<20){
            
            //play round
            if(player1turn){
                //get the move from the neural network
                int decision = player1.getDecision(getGameState(true),null);
                statesPlayer1.add(player1.StatefromGame(getGameState(true)));
                choicesPlayer1.add(decision);
                //play the card
                addToUsed(true,decision);
                //if the card is empty the player has lost
                if(player1hand[decision]==0){
                    player1score=0;
                    player2score=61;
                    System.out.println("error");
                    break;
                }
                
                //get the opponent's move
                int opponentdecision = player2.getDecision(getGameState(false),null);
                statesPlayer2.add(player2.StatefromGame(getGameState(false)));
                choicesPlayer2.add(opponentdecision);
                //play the opponent's card
                addToUsed(false,opponentdecision);


                //if the card is empty the opponent has lost
                if(player2hand[opponentdecision]==0){
                    player2score=0;
                    player1score=61;
                    System.out.println("error");
                    break;
                }
                
                //calculate who won the round
                boolean player1wins = checkWin(player1hand[decision],player1handsuits[decision],player2hand[opponentdecision],player2handsuits[opponentdecision]);
                //update the scores
                addScore(player1wins,player1hand[decision],player2hand[opponentdecision]);

                //printRoundResult(player1wins,player1turn,player1hand[decision],player1handsuits[decision],player2hand[opponentdecision],player2handsuits[opponentdecision],briscolaSuit);
                //refill the hands used cards
                if(deck.isEmpty()){
                    RemovechosenCard(true,decision);
                    RemovechosenCard(false,opponentdecision);
                }
                else{
                    refillHand(decision,opponentdecision);
                }
                if(player1wins){
                    player1turn=true;
                }
                else{
                    player1turn=false;
                }
                
                
                
                
            }
            else{
                //get the move from the neural network
                int decision = player2.getDecision(getGameState(false),null);
                statesPlayer2.add(player2.StatefromGame(getGameState(false)));
                choicesPlayer2.add(decision);
                //play the card

                addToUsed(false,decision);

                //if the card is empty the player has lost
                if(player2hand[decision]==0){
                    player2score=0;
                    player1score=61;
                    System.out.println("error");
                    break;
                }
                //get the opponent's move
                int opponentdecision = player1.getDecision(getGameState(true),null);
                statesPlayer1.add(player1.StatefromGame(getGameState(true)));
                choicesPlayer1.add(opponentdecision);
                //play the opponent's card
                addToUsed(true,opponentdecision);

                //if the card is empty the opponent has lost
                if(player1hand[opponentdecision]==0){
                    player1score=0;
                    player2score=61;
                    System.out.println("error");
                    break;
                }
                
                //calculate who won the round
                boolean player2wins = checkWin(player2hand[decision],player2handsuits[decision],player1hand[opponentdecision],player1handsuits[opponentdecision]);
                
                //update the scores
                addScore(!player2wins,player2hand[decision],player1hand[opponentdecision]);

                //printRoundResult(!player2wins,player1turn,player1hand[opponentdecision],player1handsuits[opponentdecision],player2hand[decision],player2handsuits[decision],briscolaSuit);
                //refill the hands used cards
                if(deck.isEmpty()){
                    RemovechosenCard(true,opponentdecision);
                    RemovechosenCard(false,decision);
                }
                else{
                    refillHand(opponentdecision, decision);
                }
                if(player2wins){
                    player1turn=false;
                }
                else{
                    player1turn=true;
                }
                
            }
            round++;
            
            
            
        }
        if(player1score>player2score){
            return new double[]{1,0};
        }
        else if(player1score<player2score){
            return new double[]{0,1};
        }
        else{
            return new double[]{0.5,0.5};
        }

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

}
