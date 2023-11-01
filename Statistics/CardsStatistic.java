package Statistics;

import NeuroEvolution.Game.BotBriscola;
import Players.*;

public class CardsStatistic {

    public static void main(String[] args) {
        
        // calculate the average amount of each card the winner of a game has
        int[] cards = new int[15];//corresponds to 1,2,3,4,5,6,7,8,9,10,j,q,k
        double briscolaCards=0;
        Player player1=new GreedyPlayer();
        Player player2=new GreedyPlayer();
        int games=10000000;
        for(int i=0;i<games;i++){
            if(i%100000==0){
                System.out.println(i);
            }
            BotBriscola game = new BotBriscola(player1,player2);
            int[][] cardsFromGame=game.playGameStatistics3();//[round][cardsuit,cardrank]
            for(int j=0;j<cardsFromGame.length;j++){
                cards[cardsFromGame[j][1]-1]++;
                if(cardsFromGame[j][0]==1){
                    briscolaCards++;
                }
            }
        }
        //write the results to a file
        try{
            java.io.PrintWriter output = new java.io.PrintWriter("Statistics\\CardsStatistics\\CardsStats2.txt");
            output.println("amount of games: "+games);
            for(int i=0;i<cards.length;i++){
                output.println("the average amount of "+(i+1)+"s is "+((double)cards[i]/games));
            }
            output.println("the average amount of briscola cards is "+(briscolaCards/(games*20)));
            output.close();
        }
        catch(Exception e){
            System.out.println("Error: "+e);
        }

    }
    
}
