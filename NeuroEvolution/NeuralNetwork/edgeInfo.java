package NeuroEvolution.NeuralNetwork;


public class edgeInfo{
    public int sourceIndex;
    public int targetIndex;
    public double weight;
    public boolean enabled;
    public int ID;
    public edgeInfo(int sourceIndex, int targetIndex, double weight, boolean enabled){
        this.sourceIndex = sourceIndex;
        this.targetIndex = targetIndex;
        this.weight = weight;
        this.enabled = enabled;
        this.ID = 0;
    }
}
