package NeuroEvolution.NeuralNetwork;


public class NeuralFactory {

    public static NeuralFactory instance=null;

    public static void Initialize(){
        
        instance = new NeuralFactory();
        
    }

    public static NeuralFactory getInstance(){
        if(instance==null){
            instance = new NeuralFactory();
        }
        return instance;
    }

  

    public Genotype DefaultGenotype(int inputs,int outputs){
        Genotype genotype = new Genotype();
        for(int i=0;i<inputs;i++){
            genotype.addVertex(vertexInfo.Type.INPUT);
        }
        for(int i=0;i<outputs;i++){
            genotype.addVertex(vertexInfo.Type.OUTPUT);
        }


        int innovationNumber =0;
        for(int i=0;i<inputs;i++){
            for(int j=0;j<outputs;j++){
                genotype.addEdge(i,inputs+j,0,true,innovationNumber);
                innovationNumber++;
            }
        }

        return genotype;
    }

    public void registerDefaultEdges(int inputs, int outputs){
        for (int i = 0; i < inputs; i++) {
            for(int j = 0; j < outputs; j++){
                int input= i;
                int output = j+inputs;

                edgeInfo edge = new edgeInfo(input,output,0,true);

                Mutation.instance.RegisterMarking(edge);
            }
            
            
        }
    }

  

    public Phenotype DefaultPhenotype(int inputs, int outputs){
        Phenotype physicals=new Phenotype();
        for(int i=0;i<inputs;i++){
            physicals.addVertex(Vertex.Type.INPUT);
        }
        for(int i=0;i<outputs;i++){
            physicals.addVertex(Vertex.Type.OUTPUT);
        }

        for(int i=0;i<inputs;i++){
            for(int j=0;j<outputs;j++){
                int input=i;
                int output=j+inputs;

                physicals.addEdge(input,output,0,true);
            }
        }
        return physicals;

    }



    
}
