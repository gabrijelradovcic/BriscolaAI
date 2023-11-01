package NeuroEvolution.NeuralNetwork;


public class test {
    public static String inputPath = "NeuroEvolution\\input.txt";
    public static void main(String[] args) {
        //test saving a population to a file and loading it back
        Population.Initialize();
        NeuralFactory.Initialize();
        Mutation.Initialize();
        Crossover.Initialize();
        Tournament t = new Tournament();
        Population p= Population.instance;
        //if the input file exists, load the population from it
        if (new java.io.File(inputPath).exists()) {
            MAIN.LoadState(t);
        }
        else{
            t.Initialize();
        }

        Genotype genotype= p.genetics.get(0);
        Genotype copy= p.genetics.get(203);
        double start= System.currentTimeMillis();
        Crossover.instance.ProduceOffSpring(genotype, copy);
        double end= System.currentTimeMillis();
        System.out.println("Time taken: "+(end-start)/1000+" seconds");
        
    }
    
}
