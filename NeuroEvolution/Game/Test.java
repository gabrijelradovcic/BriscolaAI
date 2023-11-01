package NeuroEvolution.Game;

public class Test {

    public static void main(String[] args) {
        //test the game
        //initialize the neuralfactory
        NeuroEvolution.NeuralNetwork.NeuralFactory neuralFactory = NeuroEvolution.NeuralNetwork.NeuralFactory.getInstance();
    
        //make 2 player phenotypes
        NeuroEvolution.NeuralNetwork.Phenotype player2 = neuralFactory.DefaultPhenotype(87,3);
        NeuroEvolution.NeuralNetwork.Phenotype player1 = neuralFactory.DefaultPhenotype(87,3);
        player2.ProcessGraph();
        player1.ProcessGraph();

        NeuralPlayer player1neural = new NeuralPlayer(player1);
        NeuralPlayer player2neural = new NeuralPlayer(player2);
        //play the game
        BotBriscola game = new BotBriscola(player1neural, player2neural);
        

        game.briscolaSuit=3;



        //tests
        System.out.println(game.checkWin(11, 3, 4, 3)==true);
        System.out.println(game.checkWin(3, 3, 11, 3)==false);
        System.out.println(game.checkWin(11, 3, 4, 2)==true);
        System.out.println(game.checkWin(11, 2, 4, 3)==false);
        System.out.println(game.checkWin(11, 2, 4, 2)==true);
    }
    
}
