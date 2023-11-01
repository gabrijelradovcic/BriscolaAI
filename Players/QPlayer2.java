package Players;
import GameEngine.*;
import NeuroEvolution.Game.GameState;
import Q_learning.QPlayerInterface;
import Q_learning.Qtraining;

public class QPlayer2 extends Player implements QPlayerInterface{
    public static void main(String[] args) {
        double start=System.currentTimeMillis();
        QPlayer2 player=new QPlayer2(0.2,0.4,0.8, new GameEngine.Deck(), 100000);
        double end=System.currentTimeMillis();
        System.out.println("Time: "+(end-start)/1000);
        start=System.currentTimeMillis();
        player=new QPlayer2(0.2,0.4,0.8, new GameEngine.Deck(), 100000);
        end=System.currentTimeMillis();
        System.out.println("Time: "+(end-start)/1000);
        
    }

    
    public static double[][] qTable;
    public double Alpha;
    public double Gamma;
    public double ExploreRate;
    public QPlayer2(double gamma,double alpha,double exploreRate, GameEngine.Deck deck, double iterations){
        super("QPlayer");
        Alpha=alpha;
        Gamma=gamma;
        ExploreRate=exploreRate;
        if(qTable==null){
            qTable=new double[4*41*41*41*41][3];
        }
        Qtraining.Train(deck,iterations,this);
    }
    public QPlayer2(double gamma,double alpha,double exploreRate, NeuroEvolution.Game.Deck deck, double iterations){
        super("QPlayer");
        Alpha=alpha;
        Gamma=gamma;
        ExploreRate=exploreRate;
        if(qTable==null){
            qTable=new double[4*41*41*41*41][3];
        }
        Qtraining.Train(deck,iterations,this);
    }

    public int getDecision(GameState state,Briscola brisc){
        //give random number
        int State=StatefromGame(state);
        int choice=0;
        if(ExploreRate>Math.random()){
            //get the highest value from the qtable
            try{
                int max=0;
                for(int i=0;i<3;i++){
                    if(qTable[State][i]>qTable[State][max]){
                        max=i;
                    }
                }
                choice=max;
            }
            catch(Exception e){
                System.out.println(State);
                return 0;
            }
        }
        else{
            choice=(int)(Math.random()*3);
        }
        //if the card is not in the hand, choose another card
        while(state.yourCardnumbers[choice]==0){
            choice=(int)(Math.random()*3);
        }
        return choice;
    }


    //briscola contains 10 cards it excludes the 8,9 and 10
    //we want to cast the number to a number from 0-10 where 0 is nothing and 8 is jack, 9 is queen and 10 is king
    public static int[] cardToNumber={0,1,2,3,4,5,6,7,0,0,0,8,9,10};

    public int StatefromGame(GameState state){

        //
        int suit=state.BriscolaSuit-1;

        int[] yourCards= new int[3];
        for(int i=0;i<state.yourCardnumbers.length;i++){
            yourCards[i]=cardToNumber[state.yourCardnumbers[i]]*(state.yourCardsuits[i]);
        }

        int opponentcard=0;
        for(int i=0;i<state.usedCardnumbers.length;i++){
            if(state.usedCardnumbers[i]==0){
                if(!(i%2==0)){
                    opponentcard=cardToNumber[state.usedCardnumbers[i-1]]*(state.usedCardsuits[i-1]);
                    break;
                }
                else{
                    opponentcard=0;
                    break;
                }
            }
        }
        double temp=opponentcard+yourCards[0]*41+yourCards[1]*41*41+yourCards[2]*41*41*41+suit*41*41*41*41;
        if(temp>4*41*41*41*41){
            System.out.println("Error");
        }
        return (int)temp;

    }
}
