package NeuroEvolution.NeuralNetwork;


import java.util.ArrayList;

import java.util.List;

public class Tournament {
    public static int TournamentSize = 512;
    public static int RoundSize = 500;
    public static int workers =10;
    public static int batch_size = 20;

    public double bestFitness = 0;
    public Genotype bestGenotype = null;

    public List<Phenotype> contestants;
    public List<Genotype> contestantsGenotypes;

    public Tournament(){
        contestants = new ArrayList<Phenotype>();
        contestantsGenotypes = new ArrayList<Genotype>();
    }

    public void Initialize(){
        int inputs= 3+3+39+39+3;
        int outputs= 3;

        Population.instance.defaultPopulation(TournamentSize, inputs, outputs);
        
    }


    public void ExecuteTournament(){
        contestants.clear();
        contestantsGenotypes.clear();
        //set all fitnesses to 0
        for(int i=0; i<Population.instance.genetics.size(); i++){
            Population.instance.genetics.get(i).fitness= 0;
            Population.instance.population.get(i).fitness= 0;
            contestants.add(Population.instance.population.get(i));
            contestantsGenotypes.add(Population.instance.genetics.get(i));
        }
        while(contestants.size()>1){
            ExecuteTournamentRound();
            
        }
        for(int i=0;i<TournamentSize;i++){
            double top= 0;

            if(bestGenotype!=null){
                top= bestGenotype.bracket;
            }
            double diff=  Population.instance.genetics.get(i).bracket-top;
            Population.instance.genetics.get(i).fitness=bestFitness+diff*5;
            
        }

        bestGenotype=contestantsGenotypes.get(0);
        bestFitness=bestGenotype.fitness;
    }



    public void doubleShuffe(List<Phenotype> l1, List<Genotype> l2){
        
        //shuffle both list at the same time
        for(int i=0; i<l1.size(); i++){
            int index= (int)(Math.random()*l1.size());
            Phenotype temp= l1.get(i);
            l1.set(i, l1.get(index));
            
            l1.set(index, temp);

            Genotype temp2= l2.get(i);
            l2.set(i, l2.get(index));
            l2.set(index, temp2);
        }

    }
    public void ExecuteTournamentRound(){
        
        doubleShuffe(contestants, contestantsGenotypes);


        //set all fitnesses to 0
        for(int i=0; i<contestants.size(); i++){
            contestants.get(i).fitness= 0;
        }

        //run the tournament round
        //for each contestant
        
        for(int i=0; i<contestants.size(); i+=2){
            

            
            int played=0;
            //play against each other contestant
            while(played<RoundSize){
                Thread[] workers= new Thread[Tournament.workers];
                //create a batch of workers
                for(int j=0; j<workers.length; j++){
                    workers[j]= new gamethread(this, i);
                    workers[j].start();

                }
                //wait for all threads to finish
                for(int j=0; j<workers.length; j++){
                    try {
                        workers[j].join();
                    } catch (InterruptedException e) {
                        
                        e.printStackTrace();
                    }

                }
                played+= batch_size*workers.length;
            }
            if(contestants.get(i).fitness>contestants.get(i+1).fitness){
                contestants.set(i+1,null);
                contestantsGenotypes.get(i).bracket++;
            }
            else{
                contestantsGenotypes.get(i+1).bracket++;
                contestants.set(i,null);
            }
            

        }
        //remove all nulls
        for(int i=0; i<contestants.size(); i++){
            if(contestants.get(i)==null){
                contestants.remove(i);
                contestantsGenotypes.remove(i);
                i--;
            }
        }
        
        return;


            
    }
    


  
 
    public class gamethread extends Thread{

        public Tournament tournament;
        public int t;
        public gamethread(Tournament t, int index)
        {
            this.tournament = t;
            this.t = index;
        }
        public void run(){
            GameThread( tournament,t);
        }
        public void GameThread(Tournament t,int index){
            
            for(int games=0;games<batch_size/2;games++){
                NeuroEvolution.Game.NeuralPlayer player1 = new NeuroEvolution.Game.NeuralPlayer(t.contestants.get(index));
                NeuroEvolution.Game.NeuralPlayer player2 = new NeuroEvolution.Game.NeuralPlayer(t.contestants.get(index+1));
                NeuroEvolution.Game.BotBriscola game = new NeuroEvolution.Game.BotBriscola(player1, player2);
                double[] scores=game.playGame();
                t.contestants.get(index).fitness+=scores[0];
                t.contestants.get(index).fitness+=scores[1];
            }
            for(int games=0;games<batch_size/2;games++){
                NeuroEvolution.Game.NeuralPlayer player2 = new NeuroEvolution.Game.NeuralPlayer(t.contestants.get(index));
                NeuroEvolution.Game.NeuralPlayer player1 = new NeuroEvolution.Game.NeuralPlayer(t.contestants.get(index+1));
                NeuroEvolution.Game.BotBriscola game = new NeuroEvolution.Game.BotBriscola(player1, player2);
                double[] scores=game.playGame();
                t.contestants.get(index).fitness+=scores[0];
                t.contestants.get(index).fitness+=scores[1];

            }
        }
    }

    
}
