package Statistics;
import Players.*;
import java.io.FileWriter;
import java.util.ArrayList;
public class Mash {
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
        

        //each player vs player has 2 files with the results
        //we want to add the results of each player vs player to a single file
        //for example random vs monte carlo has 2 files
        // 1. random vs monte carlo
        // 2. monte carlo vs random
        // the resulting file should be saved in \\Statistics\\Fair\\randomMonteCarlo.txt
        //C:\Users\Dell\Project 2-1\Project-2-1\Statistics\OneSided\GreedyPlayervsGreedyPlayer.txt
        for(int i=0;i<players.size()-1;i++){
            for(int j=i+1;j<players.size();j++){
                String filename = "C:\\Users\\Dell\\Project 2-1\\Project-2-1\\Statistics\\OneSided\\"+players.get(i).getName()+"vs"+players.get(j).getName()+".txt";
                String filename2 = "C:\\Users\\Dell\\Project 2-1\\Project-2-1\\Statistics\\OneSided\\"+players.get(j).getName()+"vs"+players.get(i).getName()+".txt";
                String target= "C:\\Users\\Dell\\Project 2-1\\Project-2-1\\Statistics\\Fair\\"+players.get(i).getName()+"VS"+players.get(j).getName()+".txt";
                CombineFiles(filename,filename2,target);
                System.out.println("Done with "+players.get(i).getName()+" VS "+players.get(j).getName());
            }
        }
    }	



    public static void CombineFiles(String path1,String path2,String target){
        try {
            //target does not exist, create it
        
            java.io.FileWriter writer = new java.io.FileWriter(target);
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(path1));
            String line = reader.readLine();
            while(line!=null){
                writer.write(line);
                writer.write("\n");
                line = reader.readLine();
            }
            reader.close();
            reader = new java.io.BufferedReader(new java.io.FileReader(path2));
            line = reader.readLine();
            //for the second file we need to reverse the scores 
            //they are written as follows: player1score,player2score;player1score,player2score
            //but we want them to be written as follows: player2score,player1score;player2score,player1score
        
            while(line!=null){
                String[] scores = line.split(";");
                //System.out.println(scores.length);
       
                for(int i=0;i<scores.length;i++){
                    String[] score = scores[i].split(",");
                    writer.write(score[1]+","+score[0]);
                    if(i!=scores.length-1){
                        writer.write(";");
                    }
                }
                writer.write("\n");
                line = reader.readLine();
               
            }
            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
