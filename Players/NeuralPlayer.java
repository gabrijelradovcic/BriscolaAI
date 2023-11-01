package Players;
import GameEngine.Briscola;
import NeuroEvolution.Game.GameState;
import NeuroEvolution.NeuralNetwork.*;

public class NeuralPlayer extends Player{

    public NeuroEvolution.NeuralNetwork.Phenotype network;
    
    public NeuralPlayer(NeuroEvolution.NeuralNetwork.Phenotype network){
        super("NeuralPlayer");
        this.network = network;
        
    }
    public NeuralPlayer(){
        super("NeuralPlayer");
        Population.Initialize();
        NeuralFactory.Initialize();
        Mutation.Initialize();
        Crossover.Initialize();
        Tournament t = new Tournament();
        Population p= Population.instance;
        
        MAIN.LoadState(t);
        p.inscribePopulation();
        Genotype g = p.GetBestGenotype();
        
        Phenotype ph = new Phenotype();
        ph.InscribeGenotype(g);
        ph.ProcessGraph();
        
        this.network = ph;
    }

    public int getDecision(GameState state,Briscola game){
        double input[]=new double[3+39+39+3+3];
        input[0]=state.yourScore;
        input[1]=state.opponentScore;
        input[2]=state.BriscolaSuit;
        for(int i=0;i<39;i++){
            input[3+i]=state.usedCardnumbers[i];
        }
        for(int i=0;i<39;i++){
            input[3+39+i]=state.usedCardsuits[i];
        }
        for(int i=0;i<3;i++){
            input[3+39+39+i]=state.yourCardnumbers[i];
        }
        for(int i=0;i<3;i++){
            input[3+39+39+3+i]=state.yourCardsuits[i];
        }
        
        double output[]=network.Propogate(input);
        if(output[0]==0 && output[1]==0 && output[2]==0){
            System.out.println("Error: all outputs are 0");
        }
        if(output[0]==0.0 && output[1]==0.0 && output[2]==0.0){
            System.out.println("Error: all outputs are 0");
        }
        //get highest output
        int decision=0;
        double highest=output[0];
        for(int i=1;i<output.length;i++){
            if(output[i]>highest){
                highest=output[i];
                decision=i;
            }
        }


        //if the decision is not valid (i.e. the card is not in the hand) then choose a random valid card
        if(state.yourCardnumbers[decision]==0){
            int validCards=0;
            for(int i=0;i<3;i++){
                if(state.yourCardnumbers[i]!=0){
                    validCards++;
                }
            }
            int randomCard=(int)(Math.random()*validCards);
            int counter=0;
            for(int i=0;i<3;i++){
                if(state.yourCardnumbers[i]!=0){
                    if(counter==randomCard){
                        decision=i;
                        break;
                    }
                    counter++;
                }
            }
        }
        return decision;

    }
    
    
}
