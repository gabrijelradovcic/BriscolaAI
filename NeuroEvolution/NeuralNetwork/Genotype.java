package NeuroEvolution.NeuralNetwork;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Genotype {

    public List<edgeInfo> edges;
    public List<vertexInfo> vertices;
    public int inputCount;
    public int outputCount;
    public int bracket;
    public double fitness;
    public double adjustedFitness;

    public Genotype(){
        this.vertices = new ArrayList<vertexInfo>();
        this.edges = new ArrayList<edgeInfo>();
        this.inputCount = 0;
        this.outputCount = 0;
        this.bracket = 0;
        this.fitness = 0;
        this.adjustedFitness = 0;
    }

    public void addVertex(vertexInfo.Type type){
        vertexInfo vertex = new vertexInfo(type);
        this.vertices.add(vertex);
    }

    
    public Genotype Clone(){
        Genotype clone = new Genotype();
        for(int i = 0; i < this.vertices.size(); i++){
            clone.addVertex(this.vertices.get(i).type);
        }
        for(int i = 0; i < this.edges.size(); i++){
            clone.addEdge(this.edges.get(i).sourceIndex, this.edges.get(i).targetIndex, this.edges.get(i).weight, this.edges.get(i).enabled, this.edges.get(i).ID);
        }
        return clone;
    }

    public void sortVertices(){
        
    }
    public void sortEdges(){
        compareEdges comparator = new compareEdges();
        Collections.sort(edges, comparator);
    }
    public void addEdge(int sourceIndex, int targetIndex, double weight, boolean enabled){
        
        edgeInfo edge = new edgeInfo(sourceIndex, targetIndex, weight, enabled);
        
        this.edges.add(edge);
    }
    public void addEdge(int sourceIndex, int targetIndex, double weight, boolean enabled, int innovationNumber){
        edgeInfo edge = new edgeInfo(sourceIndex, targetIndex, weight, enabled);

        edge.ID = innovationNumber;
        
        
        this.edges.add(edge);
    }
  

    public class compareEdges implements Comparator<edgeInfo>{
        public int compare(edgeInfo a, edgeInfo b){
            if(a.ID>b.ID){
                return 1;
            }
            else if(a.ID<b.ID){
                return -1;
            }
            else{
                return 0;
            }
        }
    }


    public boolean inGenotype(edgeInfo edge){
        //check if the in and out nodes of the edge are in the genotype
        boolean in = false;
        boolean out = false;
        for(int i = 0; i < this.vertices.size(); i++){
            if(i == edge.sourceIndex){
                in = true;
            }
            if(i == edge.targetIndex){
                out = true;
            }
        }
        if(!in || !out){
            System.out.println("Error: edge source or target not in genotype");
            //print out all indices in the genotype
            
        }
        return in && out;
    }

    




}
