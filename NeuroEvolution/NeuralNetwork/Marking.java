package NeuroEvolution.NeuralNetwork;


public class Marking {

    public int order;
    public int sourceIndex;
    public int targetIndex;
    public Marking(int order, int sourceIndex, int targetIndex){
        this.order = order;
        this.sourceIndex = sourceIndex;
        this.targetIndex = targetIndex;
    }
    
}
