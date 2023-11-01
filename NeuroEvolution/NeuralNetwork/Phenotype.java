package NeuroEvolution.NeuralNetwork;


import java.util.List;
import java.util.ArrayList;

public class Phenotype {


    public List<Vertex> vertices;
    public List<Edge> edges;
    
    public List<Vertex> inputVertices;
    public List<Vertex> outputVertices;

    public double fitness;

    public Phenotype(){
        this.vertices = new ArrayList<Vertex>();
        this.edges = new ArrayList<Edge>();
        this.inputVertices = new ArrayList<Vertex>();
        this.outputVertices = new ArrayList<Vertex>();
        this.fitness = 0;
    }

    public void addEdge(int sourceIndex, int targetIndex, double weight, boolean enabled){
        Edge edge = new Edge(sourceIndex, targetIndex, weight, enabled);
        edges.add(edge);
        //find the target vertex and add the edge to its incoming edges
        try{
            vertices.get(targetIndex).incomingEdges.add(edge);
        }
        catch(Exception e){
            System.out.println("Error: target vertex not found");
        }
        
        // for(int i=0;i<vertices.size();i++){
        //     if(vertices.get(i).index == sourceIndex){
        //         edge.sourceIndex = i;
        //         s=true;
        //     }
        //     if(vertices.get(i).index == targetIndex){
        //         vertices.get(i).incomingEdges.add(edge);
        //         edge.targetIndex = i;
        //         t=true;
        //     }
        // }
        // if(!(s && t)){
        //     System.out.println("Error: Edge not added");
        // }
    }
    public void addVertex(Vertex.Type type){
        Vertex vertex = new Vertex(type);
        this.vertices.add(vertex);
    }

    public NeuroEvolution.NeuralNetwork.Vertex.Type convert(vertexInfo.Type type){
        if(type == vertexInfo.Type.INPUT){
            return Vertex.Type.INPUT;
        }
        else if(type == vertexInfo.Type.HIDDEN){
            return Vertex.Type.HIDDEN;
        }
        else if(type == vertexInfo.Type.OUTPUT){
            return Vertex.Type.OUTPUT;
        }
        return Vertex.Type.HIDDEN;
    }
    public NeuroEvolution.NeuralNetwork.vertexInfo.Type convert(Vertex.Type type){
        if(type == Vertex.Type.INPUT){
            return vertexInfo.Type.INPUT;
        }
        else if(type == Vertex.Type.HIDDEN){
            return vertexInfo.Type.HIDDEN;
        }
        else if(type == Vertex.Type.OUTPUT){
            return vertexInfo.Type.OUTPUT;
        }
        return vertexInfo.Type.HIDDEN;
    }

    public void InscribeGenotype(Genotype genotype){
        
        
        vertices.clear();
        edges.clear();

        for(int i = 0; i < genotype.vertices.size(); i++){
            addVertex(convert(genotype.vertices.get(i).type));
        }
        for(int i = 0; i < genotype.edges.size(); i++){
            addEdge(genotype.edges.get(i).sourceIndex, genotype.edges.get(i).targetIndex, genotype.edges.get(i).weight, genotype.edges.get(i).enabled);
        }

        
    }

    public void ProcessGraph(){
        for(int i = 0; i < vertices.size(); i++){
            if(vertices.get(i).type == Vertex.Type.INPUT){
                inputVertices.add(vertices.get(i));
            }
            else if(vertices.get(i).type == Vertex.Type.OUTPUT){
                outputVertices.add(vertices.get(i));
            }
        }
    }

    public void resetGraph(){
        for(int i = 0; i < vertices.size(); i++){
            vertices.get(i).value = 0;
        }
    }

    public double[] Propogate(double[] inputs){

        int repeats =10;

        for(int i=0;i<repeats;i++){
            for(int j = 0; j < inputVertices.size(); j++){
                inputVertices.get(j).value = inputs[j];
            }
            for(int j = 0; j < vertices.size(); j++){
                if(vertices.get(j).type==Vertex.Type.OUTPUT){
                    continue;
                }
                int numIncoming = vertices.get(j).incomingEdges.size();

                for(int k = 0; k < numIncoming; k++){
                    vertices.get(j).value += vertices.get(vertices.get(j).incomingEdges.get(k).sourceIndex).value * 
                    vertices.get(j).incomingEdges.get(k).weight
                    * (vertices.get(j).incomingEdges.get(k).enabled? 1.0:0.0);
                }

                if(vertices.get(j).incomingEdges.size() > 0){
                    //sigmoid
                    vertices.get(j).value = 1.0/(1.0 + Math.exp(-4.9*vertices.get(j).value));
                }
            }
            double[] outputs = new double[outputVertices.size()];
            for(int j =0; j < outputVertices.size(); j++){
                int numIncoming = outputVertices.get(j).incomingEdges.size();
                for(int k = 0; k < numIncoming; k++){
                    outputVertices.get(j).value += vertices.get(outputVertices.get(j).incomingEdges.get(k).sourceIndex).value * 
                    outputVertices.get(j).incomingEdges.get(k).weight
                    * (outputVertices.get(j).incomingEdges.get(k).enabled? 1.0:0.0);
                }

                if(outputVertices.get(j).incomingEdges.size() > 0){
                    //sigmoid
                    outputVertices.get(j).value = 1.0/(1.0 + Math.exp(-4.9*outputVertices.get(j).value));
                    outputs[j] = outputVertices.get(j).value;
                }
            }

            if(i==repeats-1){
                return outputs;
            }
        }
        return null;
        
    }


    
    
}
