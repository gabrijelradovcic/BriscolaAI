package NeuroEvolution.NeuralNetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Crossover {
    
    public static Crossover instance=null;
    public double CrossoverChance = 0.65;

    public double speciationconstant1=1.0;
    public double speciationconstant2=1.0;
    public double speciationconstant3=0.4;
    public double distance =1;

    public static void Initialize(){
        if(instance == null){
            instance = new Crossover();
        }
    }

    public Crossover(){
        
    }

    

    public static int getDepthCross(Genotype genotype,int index){
        int max=0;
        for(int i=0;i<genotype.edges.size();i++){
            if(genotype.edges.get(i).targetIndex==index){
                int depth=getDepthCross(genotype, genotype.edges.get(i).sourceIndex);
                if(depth>max){
                    max=depth;
                }
            }
        }
        return max+1;
    }

    public static HashMap<Integer,ArrayList<Integer>> makeHash(Genotype genotype){
        HashMap<Integer,ArrayList<Integer>> hash= new HashMap<Integer,ArrayList<Integer>>();
        for(int i=0;i<genotype.edges.size();i++){
            if(hash.containsKey(genotype.edges.get(i).sourceIndex)){
                hash.get(genotype.edges.get(i).sourceIndex).add(genotype.edges.get(i).targetIndex);
            }else{
                ArrayList<Integer> list= new ArrayList<Integer>();
                list.add(genotype.edges.get(i).targetIndex);
                hash.put(genotype.edges.get(i).sourceIndex, list);
            }
        }
        return hash;
    }

    public static int getDepthHash(Genotype genotype,int index){
        HashMap<Integer,ArrayList<Integer>> map= new HashMap<Integer,ArrayList<Integer>>();
        for(int i=0;i<genotype.edges.size();i++){
            if(map.containsKey(genotype.edges.get(i).targetIndex)){
                map.get(genotype.edges.get(i).targetIndex).add(genotype.edges.get(i).sourceIndex);
            }else{
                ArrayList<Integer> list= new ArrayList<Integer>();
                list.add(genotype.edges.get(i).sourceIndex);
                map.put(genotype.edges.get(i).targetIndex, list);
            }
        }
      

        int max=getDepthHashSub(map, index,new ArrayList<>(),new HashMap<Integer,Integer>());
        return max;

    }

    public static  int getDepthHashSub(HashMap<Integer,ArrayList<Integer>> map,int index,ArrayList<Integer> visited,HashMap<Integer,Integer> cache){
        if(cache.containsKey(index)){
            return cache.get(index);
        }
        int max=0;
        if(map.containsKey(index)){
            for(int i=0;i<map.get(index).size();i++){
                if(!visited.contains(map.get(index).get(i))){
                    visited.add(map.get(index).get(i));
                    int depth=getDepthHashSub(map, map.get(index).get(i),visited,cache);
                    if(depth>max){
                        max=depth;
                    }
                    visited.remove(visited.size()-1);
                }
            }
        }
        cache.put(index, max+1);
        return max+1;
       
    }
    public Genotype ProduceOffSpring(Genotype first, Genotype second){
        
        
        List<edgeInfo> EdgeCopiesFirst = new ArrayList<edgeInfo>();
        List<edgeInfo> EdgeCopiesSecond = new ArrayList<edgeInfo>();

        List<edgeInfo> DisjointEdgesFirst = new ArrayList<edgeInfo>();
        List<edgeInfo> ExcessEdgesFirst = new ArrayList<edgeInfo>();
     

        //add all edges from first to copyFirst
        for(int i=0;i<first.edges.size();i++){
            edgeInfo info = new edgeInfo(first.edges.get(i).sourceIndex, first.edges.get(i).targetIndex, first.edges.get(i).weight, first.edges.get(i).enabled);
            info.ID = first.edges.get(i).ID;
            EdgeCopiesFirst.add(info);
        }
        //add all edges from second to copySecond
        for(int i=0;i<second.edges.size();i++){
            edgeInfo info = new edgeInfo(second.edges.get(i).sourceIndex, second.edges.get(i).targetIndex, second.edges.get(i).weight, second.edges.get(i).enabled);
            info.ID = second.edges.get(i).ID;
            EdgeCopiesSecond.add(info);
        }

        int amountofgenes1 = first.edges.size();
        int amountofgenessecond = second.edges.size();

        int invmax_first = first.edges.get(first.edges.size()-1).ID;
        int invmax_second = second.edges.get(second.edges.size()-1).ID;

        int invmin = Math.min(invmax_first, invmax_second);

        Genotype child = new Genotype();


        for(int i=0;i<amountofgenes1;i++){
            for(int j=0;j<amountofgenessecond;j++){
                edgeInfo first_edge = EdgeCopiesFirst.get(i);
                edgeInfo second_edge = EdgeCopiesSecond.get(j);

                //matching genes
                if(first_edge.ID == second_edge.ID){
                    
                    double rand= Math.random();
                    if(rand < 0.5){
                        child.addEdge(first_edge.sourceIndex, first_edge.targetIndex, first_edge.weight, first_edge.enabled);
                    }else{
                        child.addEdge(second_edge.sourceIndex, second_edge.targetIndex, second_edge.weight, second_edge.enabled);
                    }

                    EdgeCopiesFirst.remove(first_edge);
                    EdgeCopiesSecond.remove(second_edge);

                    i--;
                    amountofgenes1--;
                    amountofgenessecond--;
                    break;
                }
            }
        }
        for(int i=0;i<EdgeCopiesFirst.size();i++){
            edgeInfo first_edge = EdgeCopiesFirst.get(i);
            if(first_edge.ID > invmin){
                ExcessEdgesFirst.add(first_edge);
            }
            else{
                DisjointEdgesFirst.add(first_edge);
            }
        }
       

        
      
        
        //add all disjoint genes
        for(int i=0;i<DisjointEdgesFirst.size();i++){
            
            child.addEdge(DisjointEdgesFirst.get(i).sourceIndex, DisjointEdgesFirst.get(i).targetIndex, DisjointEdgesFirst.get(i).weight, DisjointEdgesFirst.get(i).enabled);
            
        }
        
       
        //add all excess genes
        for(int i=0;i<ExcessEdgesFirst.size();i++){
            
            child.addEdge(ExcessEdgesFirst.get(i).sourceIndex, ExcessEdgesFirst.get(i).targetIndex, ExcessEdgesFirst.get(i).weight, ExcessEdgesFirst.get(i).enabled);
            
        }
      
    


        child.sortEdges();
        int vertexcount=first.vertices.size();
        for(int i=0;i<vertexcount;i++){
            vertexInfo vertex=first.vertices.get(i);
            if(vertex.type==NeuroEvolution.NeuralNetwork.vertexInfo.Type.HIDDEN){
                break;
            }
            
            child.addVertex(vertex.type);
        }
        
        //add hidden vertices until the number of vertices in the child is equal to the number of vertices in the fittest parent
        int numberofvertices= Math.max(first.vertices.size(), second.vertices.size());
        for(int i=child.vertices.size();i<numberofvertices;i++){
            child.addVertex(NeuroEvolution.NeuralNetwork.vertexInfo.Type.HIDDEN);
        }

       

        return child;
    }


    public static void CheckForCycles(Genotype genotype){
        HashMap<Integer,ArrayList<Integer>> map= new HashMap<Integer,ArrayList<Integer>>();
        for(int i=0;i<genotype.edges.size();i++){
            if(map.containsKey(genotype.edges.get(i).targetIndex)){
                map.get(genotype.edges.get(i).targetIndex).add(genotype.edges.get(i).sourceIndex);
            }else{
                ArrayList<Integer> list= new ArrayList<Integer>();
                list.add(genotype.edges.get(i).sourceIndex);
                map.put(genotype.edges.get(i).targetIndex, list);
            }
        }
        //check for cycles
        // for(int i=0;i<genotype.vertices.size();i++){
        //     if(genotype.vertices.get(i).type==NeuroEvolution.NeuralNetwork.vertexInfo.Type.HIDDEN){
        //         CheckForCyclesSub(map, genotype.vertices.get(i).index,new ArrayList<Integer>());
        //     }
        // }
        
    }

    public static void CheckForCyclesSub(HashMap<Integer,ArrayList<Integer>> map,int index,ArrayList<Integer> visited){
        if(visited.contains(index)){
            System.out.println("cycle");
            
            return;
        }
        visited.add(index);
        if(map.containsKey(index)){
            for(int i=0;i<map.get(index).size();i++){
                CheckForCyclesSub(map, map.get(index).get(i),visited);
            }
        }
        visited.remove(visited.size()-1);
    }

    
    public boolean hasDuplicateEdge(Genotype genotype){
        for(int i=0;i<genotype.edges.size();i++){
            for(int j=0;j<genotype.edges.size();j++){
                if(i==j){
                    continue;
                }
                if(genotype.edges.get(i).sourceIndex==genotype.edges.get(j).sourceIndex && genotype.edges.get(i).targetIndex==genotype.edges.get(j).targetIndex){
                    return true;
                }
            }
        }
        return false;
    }


    public double  speciationDistance(Genotype first,Genotype second){
        List<edgeInfo> copyFirst = new ArrayList<edgeInfo>();
        List<edgeInfo> copySecond = new ArrayList<edgeInfo>();

        for(int i=0;i<first.edges.size();i++){
            edgeInfo info = first.edges.get(i);
            copyFirst.add(info);
        }
        for(int i=0;i<second.edges.size();i++){
            edgeInfo info = second.edges.get(i);
            copySecond.add(info);
        }
        int matchfirst=0;
       // List<edgeInfo> match_second = new ArrayList<edgeInfo>();

        int disjointfirst=0;
        int disjointsecond=0;

        int excessfirst=0;
        int excesssecond=0;

        
        int genes_first = first.edges.size();
        int genes_second = second.edges.size();

        int invmax_first = first.edges.get(first.edges.size()-1).ID;
        int invmax_second = second.edges.get(second.edges.size()-1).ID;

        int invmin= Math.min(invmax_first, invmax_second);

        double diff=0;

        for(int i=0;i<genes_first;i++){
            for(int j=0;j<genes_second;j++){
                edgeInfo first_edge = first.edges.get(i);
                edgeInfo second_edge = second.edges.get(j);

                //matching genes
                if(first_edge.ID == second_edge.ID){
                    double weightDiff = Math.abs(first_edge.weight - second_edge.weight);
                    diff+=weightDiff;

                    matchfirst++;
                    

                    copyFirst.remove(i);
                    copySecond.remove(i);

                    i--;
                    genes_first--;
                    genes_second--;
                    break;
                }

            }

        }
        for(int i=0;i<copyFirst.size();i++){
            if(copyFirst.get(i).ID > invmin){
                disjointfirst++;
            }
            else{
                excessfirst++;
            }
        }

        for(int i=0;i<copySecond.size();i++){
            if(copySecond.get(i).ID > invmin){
                disjointsecond++;
            }
            else{
                excesssecond++;
            }
            
        }

        double matching=matchfirst;
        double  disjoint=disjointfirst+disjointsecond;
        double excess=excessfirst+excesssecond;

        int nConstant=Math.max(first.edges.size(), second.edges.size());

        double EFormula=excess/nConstant;
        double DFormula=disjoint/nConstant;
        double WFormula=diff/matching;


        //Formula for speciation distance
        double distance=EFormula*speciationconstant1+DFormula*speciationconstant2+WFormula*speciationconstant3;
        return distance;
    }


 


        
    
}