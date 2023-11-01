package NeuroEvolution.NeuralNetwork;


import java.util.List;
import java.util.ArrayList;


public class Vertex{
        
    public enum Type{
        INPUT, HIDDEN, OUTPUT
    }
    public Type type;


    public List<Edge> incomingEdges;

    public double value;

    public Vertex(Type type){
        this.type = type;        
        this.incomingEdges = new ArrayList<Edge>();
        this.value = 0;
    }


}

