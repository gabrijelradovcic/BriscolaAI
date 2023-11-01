package NeuroEvolution.NeuralNetwork;




public class Edge{

    

    public enum Type{
        INPUT, HIDDEN, OUTPUT
    }
    public Type type;

    public int sourceIndex;

    public int targetIndex;

    public double weight;
    public boolean enabled;

   

    public Edge(int sourceIndex, int targetIndex, double weight, boolean enabled){
        this.sourceIndex = sourceIndex;
        this.targetIndex = targetIndex;
        this.weight = weight;
        this.enabled = enabled;
        
    }
}

