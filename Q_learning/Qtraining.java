package Q_learning;

import java.util.ArrayList;
import java.util.List;


import Players.QPlayer;
import Players.QPlayer2;

public class Qtraining {
    public static volatile boolean stop=false;
    public static volatile int keeper=0;
    public Qtraining() {
        
    }

    static double TrainingIterations=1000000;

    public static void main(String[] args) {
        
        stopthread stopthread=new stopthread();
        stopthread.start();
        Thread[] threads=new TrainThread[6];
        double number=1000000;
        int mils=0;
        Qtraining t=new Qtraining();

        for(int i=0;i<threads.length;i++){
            threads[i]=t.new TrainThread();
            threads[i].start();
        }

        while(true){
            
            if(stop){
                for(int i=0;i<threads.length;i++){
                    try {
                        threads[i].join();
                    } catch (InterruptedException e) {
                        
                        e.printStackTrace();
                        QlearningSave.SaveList(QPlayer.qTable);
                    }
                }
                break;
            }
            if(keeper>number){
                number+=1000000;
                mils++;
                System.out.println(mils+" million");

            }
            
        }
        QlearningSave.SaveList(QPlayer.qTable);


    }
    public static class stopthread extends Thread{
        public void run(){
            while(true){
                String s= System.console().readLine();
                if(s.equals("stop")){
                    stop=true;
                    return;
                }
            }
        }
    }


    public class TrainThread extends Thread{
        public TrainThread(){
        }
        public void run(){
            
            while(!stop){
                Train();
                keeper++;
                //System.out.println(keeper);
            }
        }
    }

    public static void Train(){
        double gamma=0.2;
        double alpha=0.8;
        double exploreRate=0.9;
        QPlayer player1=new QPlayer(gamma,alpha, exploreRate);
        QPlayer player2=new QPlayer(gamma,alpha, exploreRate);
        QGame game = new QGame(player1,player2);
        game.playGame();
        ArrayList<Integer> statesPlayer1=game.statesPlayer1;
        ArrayList<Integer> choicesPlayer1=game.choicesPlayer1;
        ArrayList<Integer> statesPlayer2=game.statesPlayer2;
        ArrayList<Integer> choicesPlayer2=game.choicesPlayer2;
        ArrayList<Integer> Rewards=game.Rewards;
        //if the arraylist do not have the same size, something went wrong
        if(statesPlayer1.size()!=choicesPlayer1.size()||statesPlayer2.size()!=choicesPlayer2.size()||Rewards.size()!=statesPlayer1.size()){
            System.out.println("error");
        }

        for(int i=0;i<statesPlayer1.size();i++){
            QPlayer.qTable[statesPlayer1.get(i)][choicesPlayer1.get(i)]+=alpha*(Rewards.get(i)-QPlayer.qTable[statesPlayer1.get(i)][choicesPlayer1.get(i)]);
            QPlayer.qTable[statesPlayer2.get(i)][choicesPlayer2.get(i)]+=alpha*(-Rewards.get(i)-QPlayer.qTable[statesPlayer2.get(i)][choicesPlayer2.get(i)]);
        }

    }

    public static void Train(GameEngine.Deck deck,double iterations,QPlayer2 player){
        NeuroEvolution.Game.Deck neuroDeck=DecktoNeuroDeck(deck);
        double gamma=0.2;
        double alpha=0.4;
        double exploreRate=0.8;
        QPlayer2 player1=player;
        QPlayer2 player2=player;
        for(int i=0;i<iterations;i++){
            QGame game = new QGame(player1,player2);
            game.deck=neuroDeck.copy();
            game.playGame();
            ArrayList<Integer> statesPlayer1=game.statesPlayer1;
            ArrayList<Integer> choicesPlayer1=game.choicesPlayer1;
            ArrayList<Integer> statesPlayer2=game.statesPlayer2;
            ArrayList<Integer> choicesPlayer2=game.choicesPlayer2;
            ArrayList<Integer> Rewards=game.Rewards;
            //if the arraylist do not have the same size, something went wrong
            if(statesPlayer1.size()!=choicesPlayer1.size()||statesPlayer2.size()!=choicesPlayer2.size()||Rewards.size()!=statesPlayer1.size()){
                System.out.println("error");
            }

            for(int j=0;j<statesPlayer1.size();j++){
                QPlayer2.qTable[statesPlayer1.get(j)][choicesPlayer1.get(j)]+=alpha*(Rewards.get(j)-QPlayer2.qTable[statesPlayer1.get(j)][choicesPlayer1.get(j)]);
                QPlayer2.qTable[statesPlayer2.get(j)][choicesPlayer2.get(j)]+=alpha*(-Rewards.get(j)-QPlayer2.qTable[statesPlayer2.get(j)][choicesPlayer2.get(j)]);
            }
        }

    }
    public static void Train(NeuroEvolution.Game.Deck deck,double iterations,QPlayer2 player){
        NeuroEvolution.Game.Deck neuroDeck=deck;
        double gamma=0.2;
        double alpha=0.4;
        double exploreRate=0.8;
        QPlayer2 player1=player;
        QPlayer2 player2=player;
        for(int i=0;i<iterations;i++){
            QGame game = new QGame(player1,player2);
            game.deck=neuroDeck.copy();
            game.playGame();
            ArrayList<Integer> statesPlayer1=game.statesPlayer1;
            ArrayList<Integer> choicesPlayer1=game.choicesPlayer1;
            ArrayList<Integer> statesPlayer2=game.statesPlayer2;
            ArrayList<Integer> choicesPlayer2=game.choicesPlayer2;
            ArrayList<Integer> Rewards=game.Rewards;
            //if the arraylist do not have the same size, something went wrong
            if(statesPlayer1.size()!=choicesPlayer1.size()||statesPlayer2.size()!=choicesPlayer2.size()||Rewards.size()!=statesPlayer1.size()){
                System.out.println("error");
            }

            for(int j=0;j<statesPlayer1.size();j++){
                QPlayer2.qTable[statesPlayer1.get(j)][choicesPlayer1.get(j)]+=alpha*(Rewards.get(j)-QPlayer2.qTable[statesPlayer1.get(j)][choicesPlayer1.get(j)]);
                QPlayer2.qTable[statesPlayer2.get(j)][choicesPlayer2.get(j)]+=alpha*(-Rewards.get(j)-QPlayer2.qTable[statesPlayer2.get(j)][choicesPlayer2.get(j)]);
            }
        }

    }



    // public static int[] suits= {1,2,3,4};
    // public static int[] ranks= {1,2,3,4,5,6,7,11,12,13};
    public static int[] values={0,11,0,10,0,0,0,0,0,0,0,2,3,4};
    // public static String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
    // public static String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "Jack", "Queen", "King"};
    // public static int[] values={11,0,10,0,0,0,0,2,3,4};
    
    public static NeuroEvolution.Game.Deck DecktoNeuroDeck(GameEngine.Deck deck){
        NeuroEvolution.Game.Deck neuroDeck=new NeuroEvolution.Game.Deck();
        List<GameEngine.Card> cards=deck.cloneCards();
        List<NeuroEvolution.Game.Card> neuroCards=new ArrayList<NeuroEvolution.Game.Card>();
        for(int i=0;i<cards.size();i++){
            neuroCards.add(CardtoNeuroCard(cards.get(i)));
        }
        neuroDeck.setCards(neuroCards);
        return neuroDeck;
    }

    public static NeuroEvolution.Game.Card CardtoNeuroCard(GameEngine.Card card){
        //first convert the suit to an int
        String suit=card.suit();
        String rank=card.rank();
        int suit1=0;
        int rank1=0;
        if(suit.equals("Clubs")){
            suit1=1;
        }
        else if(suit.equals("Diamonds")){
            suit1=2;
        }
        else if(suit.equals("Hearts")){
            suit1=3;
        }
        else if(suit.equals("Spades")){
            suit1=4;
        }
        if(rank.equals("Ace")){
            rank1=1;
        }
        else if(rank.equals("2")){
            rank1=2;
        }
        else if(rank.equals("3")){
            rank1=3;
        }
        else if(rank.equals("4")){
            rank1=4;
        }
        else if(rank.equals("5")){
            rank1=5;
        }
        else if(rank.equals("6")){
            rank1=6;
        }
        else if(rank.equals("7")){
            rank1=7;
        }
        else if(rank.equals("Jack")){
            rank1=8;
        }
        else if(rank.equals("Queen")){
            rank1=9;
        }
        else if(rank.equals("King")){
            rank1=10;
        }
        return new NeuroEvolution.Game.Card(rank1,suit1,values[rank1]);
    }
    


 
    
}
