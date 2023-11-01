package NeuroEvolution.NeuralNetwork;


import java.util.ArrayList;

import java.util.List;

public class Mutation {

    public static Mutation instance = null;
    
    public double MutateLink=1.4;
    public double MutateNode=1.7;
    public double MutateEnable=0.6;
    public double MutateDisable=0.2;
    public double MutateWeight=2;

    public double Petrub_chance=0.9;
    public double shiftStep=0.1;

    public List<Marking> history = new ArrayList<Marking>();

    public static void Initialize(){
        if(instance==null){
            instance = new Mutation();
        }
       
    }
    public Mutation(){

    }
    


    public void MutateLink(Genotype genotype){
        int vertexCount=genotype.vertices.size();
        int edgeCount=genotype.edges.size();

        List<edgeInfo> potential = new ArrayList<edgeInfo>();
        
        for(int i=0;i<vertexCount;i++){
            for(int j=0;j<vertexCount;j++){
                int source=i;
                int target=j;
                
                vertexInfo.Type sourceType=genotype.vertices.get(i).type;
                vertexInfo.Type targetType=genotype.vertices.get(j).type;

                if(sourceType==vertexInfo.Type.OUTPUT || targetType==vertexInfo.Type.INPUT){
                    continue;
                }   
                if(source==target){
                    continue;
                }

                
                boolean found=false;

                //make sure the edge doesn't already exist
                for(int k=0;k<edgeCount;k++){
                    edgeInfo edge=genotype.edges.get(k);
                    if(edge.sourceIndex==source && edge.targetIndex==target){
                        found=true;
                        break;
                    }
                }
                if(!found){
                    double weight=Math.random()*4-2;
                    edgeInfo info = new edgeInfo(source, target, weight, true);
                    potential.add(info);
                }

            }
        }

        if(potential.size()==0){
            return;
        }
        int selection=(int)(Math.random()*potential.size());
        edgeInfo mutation=potential.get(selection);
        mutation.ID=RegisterMarking(mutation);

        genotype.addEdge(mutation.sourceIndex, mutation.targetIndex, mutation.weight, mutation.enabled, mutation.ID);


    }

    

    public void MutateEnable(Genotype genotype){
        int edgeCount = genotype.edges.size();
        List<edgeInfo> potential = new ArrayList<edgeInfo>();
        for(int i=0;i<edgeCount;i++){
            if(genotype.edges.get(i).enabled){
                potential.add(genotype.edges.get(i));
            }
        }
        if(potential.size()==0){
            return;
        }
        int selection = (int)(Math.random()*potential.size());
        edgeInfo edge = potential.get(selection);
        edge.enabled=true;

    }
    public void MutateNode(Genotype genotype){
        int edgeCount = genotype.edges.size();
        int selection = (int)(Math.random()*edgeCount);
        edgeInfo edge = genotype.edges.get(selection);
        if(edge.enabled==false){
            return;
        }
        edge.enabled=false;

        int vertexnew=genotype.vertices.size();
        vertexInfo vertex=new vertexInfo(vertexInfo.Type.HIDDEN);

        edgeInfo edge1=new edgeInfo(edge.sourceIndex, vertexnew, 1, true);
        edgeInfo edge2=new edgeInfo(vertexnew, edge.targetIndex, edge.weight, true);

        edge1.ID=RegisterMarking(edge1);
        edge2.ID=RegisterMarking(edge2);

        genotype.addVertex(vertex.type);

        genotype.addEdge(edge1.sourceIndex, edge1.targetIndex, edge1.weight, edge1.enabled, edge1.ID);
        genotype.addEdge(edge2.sourceIndex, edge2.targetIndex, edge2.weight, edge2.enabled, edge2.ID);
    }


   

    public void MutateWeight(Genotype genotype){
        int selection=(int)(Math.random()*genotype.edges.size());
        edgeInfo edge=genotype.edges.get(selection);
        if(Math.random()<Petrub_chance){
            edge.weight+=Math.random()*shiftStep*2-shiftStep;
        }
        else{
            edge.weight=Math.random()*4-2;
        }
    }

    public void MutateWeightShift(edgeInfo edge, double shift){
        double scaler= Math.random()*shift-shift/2;
        edge.weight+=scaler;
    }

    public void MutateWeightRandom(edgeInfo edge){
        edge.weight=Math.random()*4-2;
    }

    public void MutateDisable(Genotype genotype){
        int edgeCount = genotype.edges.size();
        List<edgeInfo> potential = new ArrayList<edgeInfo>();
        for(int i=0;i<edgeCount;i++){
            if(genotype.edges.get(i).enabled){
                potential.add(genotype.edges.get(i));
            }
        }
        if(potential.size()==0){
            return;
        }
        int selection = (int)(Math.random()*potential.size());
        edgeInfo edge = potential.get(selection);
        edge.enabled=false;

    }

    public void MutateAll(Genotype genotype){
        //edit this to change the mutation rates
        double probability=MutateWeight;

        while(probability>0){
            double roll=Math.random();
            if(roll<probability){
                MutateWeight(genotype);
            }
            probability--;
        }
        probability=MutateLink;
        while(probability>0){
            double roll=Math.random();
            if(roll<probability){
                MutateLink(genotype);
            }
            probability--;
        }
        probability=MutateNode;
        while(probability>0){
            double roll=Math.random();
            if(roll<probability){
                MutateNode(genotype);
            }
            probability--;
        }
        probability=MutateEnable;
        while(probability>0){
            double roll=Math.random();
            if(roll<probability){
                MutateEnable(genotype);
            }
            probability--;
        }
        probability=MutateDisable;
        while(probability>0){
            double roll=Math.random();
            if(roll<probability){
                MutateDisable(genotype);
            }
            probability--;
        }
        

    }
    public int RegisterMarking(edgeInfo info){
        for(int i=0;i<history.size();i++){
            Marking mark = history.get(i);
            if(mark.sourceIndex == info.sourceIndex && mark.targetIndex == info.targetIndex){
                return mark.order;
            }

        }
        Marking mark = new Marking(history.size(), info.sourceIndex, info.targetIndex);
        history.add(mark);

        return history.size()-1;
    }



    
    public static void main(String[] args) {
        Population.Initialize();
        Mutation.Initialize();
        NeuralFactory.Initialize();
        Population.Initialize();
        Genotype g1 = NeuralFactory.instance.DefaultGenotype(10, 3);
        NeuralFactory.instance.registerDefaultEdges(10, 3);
        //print all the vertices
        for(int i=0;i<g1.vertices.size();i++){
            System.out.println(" type: "+g1.vertices.get(i).type);
        }

        

        System.out.println("========================================");
        //mutate 2 Nodes
        for(int i=0;i<8;i++){
            Mutation.instance.MutateNode(g1);
        }


        for(int i=0;i<g1.vertices.size();i++){
            //System.out.println("index: "+g1.vertices.get(i).index+" type: "+g1.vertices.get(i).type);
        }
    }
   

}