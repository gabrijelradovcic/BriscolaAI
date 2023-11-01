package Statistics;

import java.util.ArrayList;


import NeuroEvolution.Game.BotBriscola;
import Players.GreedyPlayer;
import Players.HybridPlayer;
import Players.MonteCarloPlayer;
import Players.NeuralPlayer;
import Players.Player;
import Players.QPlayer;
import Players.QPlayer2;
import Players.RandomPlayer;

public class Statistics {
    public static void main(String[] args) {
        
        Player Player3 = new RandomPlayer();
        Player Player2 = new MonteCarloPlayer();
        
        //create a neural player from the phenotype
        Player Player1 = new NeuralPlayer();
        Player Player4 = new HybridPlayer();
        Player Player5 = new GreedyPlayer();
        Player Player6 = new QPlayer();
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(Player1);
        players.add(Player2);
        players.add(Player3);
        players.add(Player4);
        players.add(Player5);
        players.add(Player6);
        int games=10000;

        for(int i=0;i<players.size()-1;i++){
            gamesWithPlayers(players.get(i),players.get(i+1),games,"\\Statistics\\Fair\\results"+players.get(i).getName()+".txt");
            System.out.println("Done with "+players.get(i).getName());
        }
        
    }
    
    
    public static  void getRoundStatsFromPlayers(Player Player1,Player Player2, int games, String filename){
        double[][][] results = new double[games][20][2];

        for(int i=0;i<games/2;i++){
            //play a game
            BotBriscola game = new BotBriscola(Player1,Player2);
            double[][] scores = game.playGameStatistics2();
            results[i]=scores;

            game = new BotBriscola(Player2,Player1);
            scores = game.playGameStatistics2();
            results[i+games/2]=scores;
        }


        //write results to file called results2.txt
        try {
            java.io.FileWriter writer = new java.io.FileWriter(filename);
            for (int i = 0; i < results.length; i++) {
                for (int j = 0; j < results[i].length; j++) {
                    writer.write(results[i][j][0] + "," + results[i][j][1] + ";");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing to file");
        }


    }

    public static void oneSidedGetRoundStatsFromPlayers(Player Player1,Player Player2, int games, String filename){
        double[][][] results = new double[games][20][2];
        System.out.println("Starting "+Player1.getName()+" vs "+Player2.getName());

        for(int i=0;i<games;i++){
            //play a game
            BotBriscola game = new BotBriscola(Player1,Player2);
            double[][] scores = game.playGameStatistics2();
            results[i]=scores;
            if(i%100==0)
                System.out.println("Game "+i+" of "+games);
        }


        //write results to file called results2.txt
        try {
            java.io.FileWriter writer = new java.io.FileWriter(filename);
            for (int i = 0; i < results.length; i++) {
                for (int j = 0; j < results[i].length; j++) {
                    writer.write(results[i][j][0] + "," + results[i][j][1] + ";");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing to file");
        }

    }
    public static void oneSidedGetRoundStatsFromQPlayers2(Player Player1, int games, String filename,boolean first){
        double[][][] results = new double[games][20][2];

        for(int i=0;i<games;i++){
            //play a game
            BotBriscola game;
            if(first)
                game = new BotBriscola(null,Player1);
            else
                game = new BotBriscola(Player1,null);
            Player Player2 = new QPlayer2(0.2,0.4,0.4,game.deck,10000000);
            if(first)
                game.player1=Player2;
            else
                game.player2=Player2;
            double[][] scores = game.playGameStatistics2();
            results[i]=scores;
        }


        //write results to file called results2.txt
        try {
            java.io.FileWriter writer = new java.io.FileWriter(filename);
            for (int i = 0; i < results.length; i++) {
                for (int j = 0; j < results[i].length; j++) {
                    writer.write(results[i][j][0] + "," + results[i][j][1] + ";");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing to file");
        }

    }
    public static void oneSidedGamesWithPlayers(Player Player1, Player Player2, int games,String filename ){
        //we want to compare the two players by playing 1000 games with each player going first
        //and 1000 games with each player going second
        //we will then compare the results
        int[][] results = new int[games][2];
     
        System.out.println("Playing "+games+" games with "+Player1.getName()+" vs "+Player2.getName());
        for(int i=0;i<games;i++){
            if(i%50==0)
                    System.out.println(i);
            //play a game
            BotBriscola game = new BotBriscola(Player1,Player2);
            double[] scores = game.playGameStatistics();
            results[i][0]=(int)scores[0];
            results[i][1]=(int)scores[1];
            
        }


        //write results to file called results.txt
        try {
            java.io.FileWriter writer = new java.io.FileWriter(filename);
            for (int i = 0; i < results.length; i++) {
                writer.write(results[i][0]+","+results[i][1]);
                writer.write(System.lineSeparator());
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing to file");
        }
    }

    public static  void gamesWithPlayers(Player Player1, Player Player2, int games,String filename ){
        //we want to compare the two players by playing 1000 games with each player going first
        //and 1000 games with each player going second
        //we will then compare the results
        int[][] results = new int[games][2];
        int wins1=0;
        int wins2=0;
        
        for(int i=0;i<games/2;i++){
            //play a game
            BotBriscola game = new BotBriscola(Player1,Player2);
            double[] scores = game.playGameStatistics();
            results[i][0]=(int)scores[0];
            results[i][1]=(int)scores[1];
            //System.out.println("Game "+i+" scores: "+scores[0]+" "+scores[1]);
            game = new BotBriscola(Player2,Player1);
            scores = game.playGameStatistics();
            results[i+games/2][0]=(int)scores[1];
            results[i+games/2][1]=(int)scores[0];
            //System.out.println("Game "+(i+games/2)+" scores: "+scores[1]+" "+scores[0]);
            if(scores[0]>scores[1]){
                wins1++;
            }
            else{
                wins2++;
            }

        }
        System.out.println("Player 1 won "+wins1+" games");
        System.out.println("Player 2 won "+wins2+" games");
     
            

        //write results to file called results.txt
        try {
            java.io.FileWriter writer = new java.io.FileWriter(filename);
            writer.write("Player 1: "+Player1.getName()+" Player 2: "+Player2.getName());
            writer.write(System.lineSeparator());
            writer.write("player 1 wins: "+wins1+" player 2 wins: "+wins2);



        } catch (Exception e) {
            System.out.println("Error writing to file");
        }
    }
    
}
