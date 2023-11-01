package NeuroEvolution.NeuralNetwork;


public class vertexInfo{
    public enum Type{
        INPUT, HIDDEN, OUTPUT
    }
    public Type type;

  
    public vertexInfo(Type type){
        this.type = type;
    
    }
}
