package Statistics;

import java.util.ArrayList;

import Players.*;
import java.io.FileWriter;
public class PerformanceStatistic {

    public static void main(String[] args) {
        ArrayList<Player[]> players = new ArrayList<Player[]>();
        players.add(new Player[]{new RandomPlayer(), new RandomPlayer()});
        players.add(new Player[]{new MonteCarloPlayer(), new MonteCarloPlayer()});
        players.add(new Player[]{new NeuralPlayer(), new NeuralPlayer()});
        players.add(new Player[]{new HybridPlayer(), new HybridPlayer()});
        players.add(new Player[]{new GreedyPlayer(), new GreedyPlayer()});
        players.add(new Player[]{new QPlayer(), new QPlayer()});
        ArrayList<Double> times = new ArrayList<Double>();
        System.out.println("Starting random");
        double starttime = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            Player rand= new RandomPlayer();
            if(i%10==0){
                System.out.println(i);
            }
        }
        double endtime = System.currentTimeMillis();
        times.add((endtime-starttime)/1000);
        starttime = System.currentTimeMillis();
        for(int i=0;i<1000;i++){

            Player monte = new MonteCarloPlayer();
        }
        endtime = System.currentTimeMillis();
        times.add((endtime-starttime)/1000);
        starttime = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            Player neural = new NeuralPlayer();
            NeuroEvolution.NeuralNetwork.Population.instance=null;
            NeuroEvolution.NeuralNetwork.NeuralFactory.instance=null;
            NeuroEvolution.NeuralNetwork.Mutation.instance=null;
            NeuroEvolution.NeuralNetwork.Crossover.instance=null;
        
        }
        endtime = System.currentTimeMillis();
        times.add((endtime-starttime)/1000);
        starttime = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            Player hybrid = new HybridPlayer();
            NeuroEvolution.NeuralNetwork.Population.instance=null;
            NeuroEvolution.NeuralNetwork.NeuralFactory.instance=null;
            NeuroEvolution.NeuralNetwork.Mutation.instance=null;
            NeuroEvolution.NeuralNetwork.Crossover.instance=null;
        }
        endtime = System.currentTimeMillis();
        times.add((endtime-starttime)/1000);
        starttime = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            Player greedy = new GreedyPlayer();
            NeuroEvolution.NeuralNetwork.Population.instance=null;
            NeuroEvolution.NeuralNetwork.NeuralFactory.instance=null;
            NeuroEvolution.NeuralNetwork.Mutation.instance=null;
            NeuroEvolution.NeuralNetwork.Crossover.instance=null;
        }
        endtime = System.currentTimeMillis();
        times.add((endtime-starttime)/1000);
        starttime = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            Player q = new QPlayer();
            QPlayer.qTable=null;
        }
        endtime = System.currentTimeMillis();
        times.add((endtime-starttime)/1000);
        
        


        for(int i=0;i<players.size();i++){
            TestPlayerPerformance(players.get(i)[0], players.get(i)[1], times.get(i));
        }

        

        
    }

    public static void TestPlayerPerformance(Player player1, Player player2, double starttime){
        System.out.println("Testing player performance for player: "+player1.getName());
        //we want to test the average time it takes this player to make a decision
        //we do this by making a game with 2 players, one of which is the player we want to test
        //the other player is a random player
        //we then make the player play 1000 games and we calculate the average time it takes to make a decision
        //we then write this to a file with the name of the player as follows \\Statistics\\PerformanceStatistics\\PlayerName.txt
        ArrayList<Double> times = new ArrayList<Double>();
        
        PerformanceGame game = new PerformanceGame(player1, player2);
        game.playGame();
        times.addAll(game.player1ComputationTime);
           
        

        //write out the times to a file
        String filename = "Statistics\\PerformanceStatistics\\"+player1.getName()+".txt";
        try{
            FileWriter writer = new FileWriter(filename);
            writer.write("Time taken to make a decision in milliseconds: ");
            for(int i=0;i<times.size();i++){
                writer.write(times.get(i)+";");
            }
            writer.write("\nstartup time: "+starttime);
            writer.close();
        }catch(Exception e){
            System.out.println("Error writing to file");
        }


        



        
    }
        
    
    
}
