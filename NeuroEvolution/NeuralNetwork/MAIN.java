package NeuroEvolution.NeuralNetwork;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.List;


public class MAIN {

    public static String inputPath = "NeuroEvolution\\input.txt";
    public static String BestGenoPath= "NeuroEvolution\\Geno.txt";
    public static int amountOfGenerations = 2000;
    public static boolean stop=false;
    public static void main(String[] args) {
        //test saving a population to a file and loading it back
        Population.Initialize();
        NeuralFactory.Initialize();
        Mutation.Initialize();
        Crossover.Initialize();
        Tournament t = new Tournament();
       
        //if the input file exists, load the population from it
        if (new java.io.File(inputPath).exists()) {
            LoadState(t);
        }
        else{
            t.Initialize();
        }
        t.doubleShuffe(t.contestants,t.contestantsGenotypes);
        //make sure all the contestants have a fitness of 0
       
        //print all markings
        // for(int i=0;i<Mutation.instance.history.size();i++){
        //     Marking mark= Mutation.instance.history.get(i);
        //     System.out.println("in: "+mark.sourceIndex+", out: "+mark.targetIndex+ ", innovation: "+mark.order);
        // }
        Thread thread = new stopthread();
        thread.start();
        //set to max value
    
        for(int i=0;i<amountOfGenerations;i++){
            System.out.println("=========================================");
            double start=System.currentTimeMillis();
            System.out.println("executing tournament");
            t.ExecuteTournament();
            System.out.println("execution time: "+((System.currentTimeMillis()-start)/1000)+" seconds");
            start=System.currentTimeMillis();
            System.out.println("Generating new generation");
            Population.instance.newGeneration();
            System.out.println("generation time: "+((System.currentTimeMillis()-start)/1000)+" seconds");
            start=System.currentTimeMillis();
            System.out.println("current genertion: " +Population.instance.generation);
            System.out.println("amount of species: "+Population.instance.species.size());      
            //checkPopulation();
            SaveBestGenotype(BestGenoPath, t);
            if(stop){
                break;
            }
            //save every 5 generations
            if(i%10==0){
                System.out.println("saving state");
                SaveState(inputPath,t);
                System.out.println("saving time: "+((System.currentTimeMillis()-start ) /1000)+" seconds");
            }

        }
        //Genotype genotype=loadGenotype();
        //SaveState(inputPath,t);
        //checkPopulation();
        //SaveState(inputPath,t   );
        //find the genotype with the most vertices
        int max=0;
        int index=0;
        for(int i=0;i<Population.instance.genetics.size();i++){
            if(Population.instance.genetics.get(i).vertices.size()>max){
                max=Population.instance.genetics.get(i).vertices.size();
                index=i;
            }
        }
        Visualitation v = new Visualitation(Population.instance.genetics.get(index));
    
        

        
    }

    public static class stopthread extends Thread{
        public void run(){
            while(true){
                String s= System.console().readLine();
                if(s.equals("stop")){
                    stop=true;
                    return;
                }
            }
        }
    }

    public static String GenoPath= "NeuroEvolution/Geno.txt";
    public static void SaveGenotype( Genotype genes){ 
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(GenoPath));
            String SaveString = "";
            int vertexCount=genes.vertices.size();
            //save each vertex of the member
            for(int k=0;k<vertexCount;k++){
                SaveString+=genes.vertices.get(k).type;
                SaveString+=delimiterComma;
                
            }

            SaveString+=delimiterVertexAndEdge;
            int edges=genes.edges.size();
            //save each edge of the member
            for(int k=0;k<edges;k++){
                SaveString+=genes.edges.get(k).sourceIndex;
                SaveString+=delimiterComma;
                SaveString+=genes.edges.get(k).targetIndex;
                SaveString+=delimiterComma;
                SaveString+=genes.edges.get(k).weight;
                SaveString+=delimiterComma;
                SaveString+=genes.edges.get(k).enabled;
                SaveString+=delimiterComma;
                SaveString+=genes.edges.get(k).ID;
                SaveString+=delimiterComma;
                
            }
            writer.write(SaveString);
            writer.close();
        } catch (Exception e) {
            System.out.println("Error while saving the population");
        }
    }
    public static void SaveBestGenotype(String path, Tournament t) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            Genotype genes = t.bestGenotype;
            String SaveString = "";
            int vertexCount=genes.vertices.size();
            //save each vertex of the member
            for(int k=0;k<vertexCount;k++){
                SaveString+=genes.vertices.get(k).type;
                SaveString+=delimiterComma;
                
            }

            SaveString+=delimiterVertexAndEdge;
            int edges=genes.edges.size();
            //save each edge of the member
            for(int k=0;k<edges;k++){
                SaveString+=genes.edges.get(k).sourceIndex;
                SaveString+=delimiterComma;
                SaveString+=genes.edges.get(k).targetIndex;
                SaveString+=delimiterComma;
                SaveString+=genes.edges.get(k).weight;
                SaveString+=delimiterComma;
                SaveString+=genes.edges.get(k).enabled;
                SaveString+=delimiterComma;
                SaveString+=genes.edges.get(k).ID;
                SaveString+=delimiterComma;
                
            }
            writer.write(SaveString);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int transformType(String type) {
        if (type.equals("INPUT")) {
            return 0;
        } else if (type.equals("HIDDEN")) {
            return 1;
        } else if (type.equals("OUTPUT")) {
            return 2;
        } else {
            return -1;
        }
    }
    public static vertexInfo.Type transformToType(String type){
        if (type.equals("INPUT")) {
            return vertexInfo.Type.INPUT;
        } else if (type.equals("HIDDEN")) {
            return vertexInfo.Type.HIDDEN;
        } else if (type.equals("OUTPUT")) {
            return vertexInfo.Type.OUTPUT;
        } else {
            return null;
        }
    }

    public static Genotype loadGenotype(){
        //load the genotype saved in saveGenotype
        try {
            BufferedReader reader = new BufferedReader(new FileReader(GenoPath));
            String line = reader.readLine();
            Genotype g=new Genotype();
            String[] vertices = line.split(""+delimiterVertexAndEdge)[0].split(""+delimiterComma);
            String[] edges = line.split(""+delimiterVertexAndEdge)[1].split(""+delimiterComma);
            for(int i=0;i<vertices.length;i+=2){
                g.addVertex( transformToType(vertices[i+1]));
            }
            for(int i=0;i<edges.length;i+=5){
                g.addEdge(Integer.parseInt(edges[i]),Integer.parseInt(edges[i+1]),Double.parseDouble(edges[i+2]),Boolean.parseBoolean(edges[i+3]),Integer.parseInt(edges[i+4]));
            }
            reader.close();
            

            return g;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String memberString(Genotype genes) {
        String s = "";
        int vertexCount = genes.vertices.size();
        for(int i=0;i<vertexCount;i++){
           
            s+=transformType(genes.vertices.get(i).type+"");
            s+=delimiterComma;
	
        }
        s+="#";
        int edgeCount=genes.edges.size();
        for(int i=0;i<edgeCount;i++){
            s+=genes.edges.get(i).sourceIndex;
            s+=delimiterComma;
            s+=genes.edges.get(i).targetIndex;
            s+=delimiterComma;
            s+=genes.edges.get(i).weight;
            s+=delimiterComma;
            s+=genes.edges.get(i).enabled;
            s+=delimiterComma;
            s+=genes.edges.get(i).ID;
            s+=delimiterComma;
        }

        return s;
    }







    public static char delimiter=';';
    public static char delimiterComma=',';
    public static char delimiterVertexAndEdge='#';
    public static char delimiterSpecies='&';
    public static char delimiterMembers='n';
    public static char delimiterSpeciesStatistcs='%';
    public static void SaveState(String target,Tournament tournament){
        System.out.println("saving state...");
        String SaveString="";
        

        SaveString+=Population.instance.generation;
        SaveString+=delimiter;
        SaveString+=tournament.bestFitness;
        SaveString+=delimiter;

       
        System.out.println("saving history...");
        for(int i=0;i<Mutation.instance.history.size();i++){
            SaveString+=Mutation.instance.history.get(i).order;
            SaveString+=delimiterComma;
            SaveString+=Mutation.instance.history.get(i).sourceIndex;
            SaveString+=delimiterComma;
            SaveString+=Mutation.instance.history.get(i).targetIndex;
            if(i!=Mutation.instance.history.size()-1){
                SaveString+=delimiterComma;
            }
        }
    
        
        
    

        SaveString+=delimiter;

        System.out.println("saving species...");
        int counter=0;
        List<String> speciesStrings=new ArrayList<>();
        //save network species by species
        for(int i=0;i<Population.instance.species.size();i++){
            speciesStrings.add("");
            speciesStrings.set(counter,speciesStrings.get(counter)+Population.instance.species.get(i).topFitness);
            speciesStrings.set(counter,speciesStrings.get(counter)+delimiterComma);
            speciesStrings.set(counter,speciesStrings.get(counter)+Population.instance.species.get(i).staleness);
            speciesStrings.set(counter,speciesStrings.get(counter)+delimiterSpeciesStatistcs);


            
            int member_count=Population.instance.species.get(i).members.size();
            //save each member of the species
            for(int j=0;j<member_count;j++){
                speciesStrings.add("");	
                counter++;

                

                Genotype genes=Population.instance.species.get(i).members.get(j);
                int vertexCount=genes.vertices.size();
                //save each vertex of the member
                for(int k=0;k<vertexCount;k++){

                    speciesStrings.set(counter,speciesStrings.get(counter)+genes.vertices.get(k).type);
                    speciesStrings.set(counter,speciesStrings.get(counter)+delimiterComma);
                    
                }

                speciesStrings.set(counter,speciesStrings.get(counter)+delimiterVertexAndEdge);
                
                int edges=genes.edges.size();
                //save each edge of the member
                for(int k=0;k<edges;k++){
                    speciesStrings.set(counter,speciesStrings.get(counter)+genes.edges.get(k).sourceIndex);
                    speciesStrings.set(counter,speciesStrings.get(counter)+delimiterComma);
                    speciesStrings.set(counter,speciesStrings.get(counter)+genes.edges.get(k).targetIndex);
                    speciesStrings.set(counter,speciesStrings.get(counter)+delimiterComma);
                    speciesStrings.set(counter,speciesStrings.get(counter)+genes.edges.get(k).weight);
                    speciesStrings.set(counter,speciesStrings.get(counter)+delimiterComma);
                    speciesStrings.set(counter,speciesStrings.get(counter)+genes.edges.get(k).enabled);
                    speciesStrings.set(counter,speciesStrings.get(counter)+delimiterComma);
                    speciesStrings.set(counter,speciesStrings.get(counter)+genes.edges.get(k).ID);
                    speciesStrings.set(counter,speciesStrings.get(counter)+delimiterComma);
                    
                }
                if(j!=member_count-1){
                    speciesStrings.set(counter,speciesStrings.get(counter)+delimiterMembers);
                    
                }


            }

            if(i!=Population.instance.species.size()-1){
                speciesStrings.set(counter,speciesStrings.get(counter)+delimiterSpecies);
            }
            counter++;
        }

        //write to file
        try{
            FileWriter fw = new FileWriter(target);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(SaveString);
            for(int i=0;i<speciesStrings.size();i++){
                bw.write(speciesStrings.get(i));
            }
            bw.close();
            
        }catch(Exception e){
            System.out.println("Error writing to file");
            System.out.println(e);
        }
        


    }


    public static void LoadState(Tournament tournament){
        String load="";
        try{
            //read the whole file and save it to a string
            BufferedReader br = new BufferedReader(new FileReader(inputPath));
            String line;
            while ((line = br.readLine()) != null) {
                load+=line;
            }
            br.close();
        }catch(Exception e){
            System.out.println("Error reading file");
        }

        //split the file into 4 parts
        String[] split=load.split(""+delimiter);

        //first and second part are the generation and the best fitness
        int gen=parseInt(split[0]);
        double score=Double.parseDouble(split[1]);
        Population.instance.generation=gen;
        tournament.bestFitness=score;


        //third part is the history of mutations

        String marking=split[2];
        String[] markings=marking.split(""+delimiterComma);
        for(int i=0;i<markings.length;i+=3){
            int order=parseInt(markings[i]);
            int source=parseInt(markings[i+1]);
            int target=parseInt(markings[i+2]);
            Marking mark=new Marking(order,source,target);
            Mutation.instance.history.add(mark);
        }


        //fourth part is the species
        String[] Species=split[3].split(""+delimiterSpecies);
        for(int i=0;i<Species.length;i++){
            String Split=Species[i].split(""+delimiterSpeciesStatistcs)[0];
            String[] stats=Split.split(""+delimiterComma);
            double topFitness=Double.parseDouble(stats[0]);
            int staleness=parseInt(stats[1]);

            Species species=new Species();
            species.topFitness=topFitness;
            species.staleness=staleness;
            Population.instance.species.add(species);
            String networkParts=Species[i].split(""+delimiterSpeciesStatistcs)[1];
            String[] members=networkParts.split(""+delimiterMembers);
            for(int j=0;j<members.length;j++){
                String[] vertices=members[j].split(""+delimiterVertexAndEdge)[0].split(""+delimiterComma);
                String[] edges=members[j].split(""+delimiterVertexAndEdge)[1].split(""+delimiterComma);
                Genotype genotype=new Genotype();
                for(int k=0;k<vertices.length;k++){
                    String type=vertices[k];

                    genotype.addVertex(transformToType(type));
                }
                
                for(int k=0;k<edges.length;k+=5){
                    int source=parseInt(edges[k]);
                    int target=parseInt(edges[k+1]);
                    double weight=Double.parseDouble(edges[k+2]);
                    boolean enabled=Boolean.parseBoolean(edges[k+3]);
                    int innovation=parseInt(edges[k+4]);
                    genotype.addEdge(source,target,weight,enabled,innovation);
                }
                species.members.add(genotype);
                Population.instance.genetics.add(genotype);
            }
               
        }
        Population.instance.inscribePopulation();   
    }


    public static int parseInt(String s){
        if(s.contains(".")){
            return (int)Double.parseDouble(s);
        }
        return Integer.parseInt(s);
    }


    public static void checkPopulation(){
        //method which checks multiple things about the population
        

        //check if all input edges have no incoming edges and all output edges have no outgoing edges and hidden edges have at least one incoming and one outgoing edge
        for(int i=0;i<Population.instance.genetics.size();i++){
            Genotype genes=Population.instance.genetics.get(i);
            for(int j=0;j<genes.vertices.size();j++){
                if(genes.vertices.get(j).type==vertexInfo.Type.INPUT){
                    for(int k=0;k<genes.edges.size();k++){
                        if(genes.edges.get(k).targetIndex==j){
                            System.out.println("Input vertex has incoming edge");
                        }
                    }
                    //check if the depth is 1
                    if(Crossover.getDepthCross(genes,j)!=1){
                        System.out.println("Input vertex has wrong depth");
                    }
                }
                if(genes.vertices.get(j).type==vertexInfo.Type.OUTPUT){
                    for(int k=0;k<genes.edges.size();k++){
                        if(genes.edges.get(k).sourceIndex==j){
                            System.out.println("Output vertex has outgoing edge");
                        }
                    }
                }
                if(genes.vertices.get(j).type==vertexInfo.Type.HIDDEN){
                    boolean incoming=false;
                    boolean outgoing=false;
                    for(int k=0;k<genes.edges.size();k++){
                        
                        if(genes.edges.get(k).sourceIndex==j){
                            incoming=true;
                        }
                        if(genes.edges.get(k).targetIndex==j){
                            outgoing=true;
                        }
                        if(incoming&&outgoing){
                            break;
                        }
                    }
                    if(!incoming){
                        System.out.println("Hidden vertex has no incoming edge");
                        Visualitation v=new Visualitation(genes);
                    }
                    if(!outgoing){
                        System.out.println("Hidden vertex has no outgoing edge");
                    }
                }
            }
        }

        //check if all 

        

    }
    
}
