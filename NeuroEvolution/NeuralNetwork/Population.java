package NeuroEvolution.NeuralNetwork;


import java.util.ArrayList;
import java.util.List;

public class Population {
    public static Population instance;

    public int generation;
    public int populationSize=512;
    public int inputs=0;
    public int outputs=0;
    public int MAXSTALENESS=25;
    public int breedThreads=10;

    public double portion=0.2;

    public List<Species> species;
    public List<Genotype> genetics;

    public List<Phenotype> population;

    public static void Initialize(){
        if(instance==null){
            instance= new Population();
        }
    }

    public Population()
        {
            species = new ArrayList<Species>();
            genetics = new ArrayList<Genotype>();
            population = new ArrayList<Phenotype>();
        }
    public void defaultPopulation(int populationSize, int inputs, int outputs){
        this.populationSize= populationSize;
        this.inputs= inputs;
        this.outputs= outputs;
        for(int i= 0; i<populationSize; i++){
            Genotype genotype= NeuralFactory.instance.DefaultGenotype(inputs, outputs);
            genetics.add(genotype);
            AddToSpecies(genotype);
        }
        NeuralFactory.instance.registerDefaultEdges(inputs, outputs);
        for(int i=0; i<populationSize ; i++){
            Mutation.instance.MutateAll(genetics.get(i));
        }
        inscribePopulation();

    }

    public Phenotype getBestPhenotype(){
        Phenotype best= population.get(0);
        for(int i=0; i<population.size(); i++){
            if(population.get(i).fitness>best.fitness){
                best= population.get(i);
            }
        }
        return best;
    }
    public Genotype GetBestGenotype(){
        Genotype best= genetics.get(0);
        for(int i=1; i<genetics.size(); i++){
            if(genetics.get(i).fitness>best.fitness){
                best= genetics.get(i);
            }
        }
        return best;
    }

    public void CalculateAdjustedFitness(){
        for(int i=0; i<species.size(); i++){
            Species s= species.get(i);
            for(int j=0; j<s.members.size(); j++){
                s.members.get(j).adjustedFitness= s.members.get(j).fitness/s.members.size();
               // System.out.println("Adjusted Fitness: "+s.members.get(j).adjustedFitness);
            }
        }
    }

    public void newGeneration(){
        //calculate adjusted fitness
        CalculateAdjustedFitness();

        //cull all species to 20% of their members
        //and remove all species with no members
        for(int i=0; i<species.size(); i++){
            species.get(i).SortMembers();
            species.get(i).ReduceSpeciesSize(portion);
            if(species.get(i).members.size()==0){
                species.remove(i);
                i--;
            }

        }

        //
        UpdateStaleness();

        //calculate total adjusted fitness
        double fitnessSum= 0;
        for (int i = 0; i < species.size(); i++)
        {
            species.get(i).CalculateAdjustedFitnessSum();
            fitnessSum += species.get(i).fitnessSum;
            
        }

        List<Genotype> children= new ArrayList<Genotype>();
        

        
        //for each species add children to the children list based on their fitness and population size
        for(int i=0; i<species.size(); i++){
            
            //calculate the number of children to add
            //subtract 1 because the best member of the species is added automatically
            int breed= (int)(populationSize*(species.get(i).fitnessSum/fitnessSum))-1;
            
            
            while(breed>0){
                Thread[] threads= new Thread[breedThreads];
                for(int j=0; j<threads.length; j++){
                    threads[j]= new Thread(new BreedThread(species.get(i), children));
                    threads[j].start();
                }
                for(int j=0; j<threads.length; j++){
                    try {
                        threads[j].join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                breed-=breedThreads;
                //break before it adds too many children
                if(breed-breedThreads<0){
                    break;
                }
                
            }
            //add the last amount of children
            for(int j=0; j<breed; j++){
                children.add(species.get(i).Breed());
            }
            //System.out.println("Batch");
        }
     

        //straglers
        while(populationSize>children.size()+species.size()){
            int s1= (int)(Math.random()*species.size());
            children.add(species.get(s1).Breed());
        }
        while(populationSize<children.size()+species.size()){
            children.remove(children.size()-1);
        }

        //cull to one
        for(int i=0; i<species.size(); i++){
            species.get(i).ReduceSpeciesToOne();
        }
    
        for(int i=0; i<children.size(); i++){
            AddToSpecies(children.get(i));
        }
        

 

        genetics.clear();

        for(int i=0; i<species.size(); i++){
            for(int j=0; j<species.get(i).members.size(); j++){
                genetics.add(species.get(i).members.get(j));
            }
        }
        

        inscribePopulation();
        generation++;
    }

    public void UpdateStaleness(){
        int speciescount= species.size();

        for(int i=0; i<speciescount; i++){
            if(species.size()==1){
                return;
            }
            double topFitness= species.get(i).members.get(0).fitness;
            if(topFitness>species.get(i).topFitness){
                species.get(i).topFitness= topFitness;
                species.get(i).staleness= 0;
            }
            else{
                species.get(i).staleness++;
            }

            if(species.get(i).staleness>MAXSTALENESS){
                species.remove(i);
                i--;
                speciescount--;
            }

            
        }
    }


    public void inscribePopulation(){
        population.clear();
        for(int i=0; i<populationSize; i++){
            genetics.get(i).fitness= 0;
            genetics.get(i).adjustedFitness= 0;

            Phenotype phenotype= new Phenotype();
            phenotype.InscribeGenotype(genetics.get(i));
            phenotype.ProcessGraph();
            population.add(phenotype);
        }
    }

    public void AddToSpecies(Genotype genotype){
        if(species.size()==0){
            Species newSpecies= new Species();
            newSpecies.members.add(genotype);
            species.add(newSpecies);
        }
        else{
            boolean found= false;
            for(int i=0; i<species.size(); i++){
                
                double distance= Crossover.instance.speciationDistance(genotype, species.get(i).members.get(0));
                if(distance<Crossover.instance.distance){
                    species.get(i).members.add(genotype);
                    found= true;
                    break;
                }
            }
            if(!found){
                Species newSpecies= new Species();
                newSpecies.members.add(genotype);
                species.add(newSpecies);
            }
        }
    }    

    public class BreedThread extends Thread{
        public Species species;
        public List<Genotype> children;
        public BreedThread(Species s,List<Genotype> children){
            this.species= s;
            this.children= children;
        }
        public void run(){
            Genotype child = species.Breed();
            children.add(child);

            return;
        }
    }
    
    
}
