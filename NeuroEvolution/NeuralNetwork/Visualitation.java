package NeuroEvolution.NeuralNetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Dimension;
//import toolkit
import java.awt.Toolkit;


import javax.swing.JFrame;
import java.awt.*;



import java.awt.Graphics;
import java.awt.Color;

public class Visualitation extends JFrame{

    public static int visualitation=1;
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static int width = screenSize.width;
    public static int height = screenSize.height;
    public static int SquareSize = 2;
    public static int BorderSize = 5;
    public static int heightmargin=5;
    public static int widthmargin=3;
    public static int LineThickness=1;
    
  
    public ArrayList<drawvertex> drawvertexs = new ArrayList<drawvertex>();
    public ArrayList<drawedge> drawedges = new ArrayList<drawedge>();
    public static String inputPath="NeuroEvolution\\input.txt";

    public static void main(String[] args) {
        //test saving a population to a file and loading it back
        Population.Initialize();
        NeuralFactory.Initialize();
        Mutation.Initialize();
        Crossover.Initialize();
        Tournament t = new Tournament();
        Population p= Population.instance;
        //if the input file exists, load the population from it
        if (new java.io.File(inputPath).exists()) {
            MAIN.LoadState(t);
        }
        else{
            t.Initialize();
        }
        
        
        //breed the two genotypes
        //Genotype g3 = Crossover.instance.ProduceOffSpring(g, g2);
        
 
       
        for(int i=0;i<p.genetics.size();i++){
            Phenotype phenotype = new Phenotype();
            phenotype.InscribeGenotype(p.genetics.get(i));
            for(int j=0;j<phenotype.vertices.size();i++){
                if((phenotype.vertices.get(j).type==Vertex.Type.HIDDEN) && (phenotype.vertices.get(j).incomingEdges.size()==0)){
                    System.out.println("ERROR");
                }
            }
        }

    }

    public Visualitation() {
        
        visualitation++;
        this.setSize(width, height);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        this.setVisible(true);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public Visualitation(Genotype genotype) {
        
        visualitation++;
        this.setSize(width, height);
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        VisualizeGenotype(genotype);
        this.setVisible(true);
    }
    public void VisualizeGenotype(Genotype genotype){

        ArrayList<Integer> depths = new ArrayList<Integer>();
        HashMap <Integer, Integer> amountperDepth = new HashMap<Integer, Integer>();
        int maxDepth = 0;
        int maxLength=0;
        for(int i=0; i<genotype.vertices.size(); i++){
            System.out.println(i);
            if(genotype.vertices.get(i).type==vertexInfo.Type.OUTPUT){
                depths.add(-1);
                continue;
            }
            
            int depth;
            
            
            depth = Crossover.getDepthHash(genotype, i);
            if(genotype.vertices.get(i).type!=vertexInfo.Type.INPUT){
                depth++;
            }
                
            
            depths.add(depth);
            if(depth > maxDepth){
                maxDepth = depth;
            }
            if(amountperDepth.containsKey(depth)){
                int temp=amountperDepth.get(depth);
                if(maxLength<temp){
                    maxLength=temp;
                }
                amountperDepth.put(depth, amountperDepth.get(depth)+1);
            }else{
                amountperDepth.put(depth, 1);
                if(maxLength<1){
                    maxLength=1;
                }
            }

        }
        maxDepth++;
        
        for(int i=0;i<genotype.vertices.size();i++){
            if(genotype.vertices.get(i).type==vertexInfo.Type.OUTPUT){
                if(amountperDepth.containsKey(maxDepth)){
                    amountperDepth.put(maxDepth, amountperDepth.get(maxDepth)+1);
                }else{
                    amountperDepth.put(maxDepth, 1);
                }
            }
        }
        for(int i=0;i<depths.size(); i++){
            if(depths.get(i)==-1){
                depths.set(i, maxDepth);
            }
        }

        //calculate the size of the squares, the size should be the same for all squares
       
        int emptyLayers=maxDepth-amountperDepth.size();
        double spaceforVertexes1 = width-2*BorderSize-(maxDepth)*widthmargin;
        double spaceforVertexes2 = height - 2*BorderSize-(maxLength)*heightmargin;
        int vertexSize=(int)Math.min(spaceforVertexes1/(maxDepth+1-emptyLayers), spaceforVertexes2/(maxLength+1));
        
        int x=BorderSize;
        int y=BorderSize;
        
        for(int i=0;i<=maxDepth;i++){
            if(!amountperDepth.containsKey(i)){
                continue;
            }
            y=BorderSize+(maxLength-amountperDepth.get(i)+1)*(vertexSize+heightmargin)/2;
            for(int j=0;j<genotype.vertices.size();j++){
                if(depths.get(j)==i){
                    drawvertexs.add(new drawvertex(x, y, vertexSize, getVertexColor(genotype.vertices.get(j).type), j));
                    y+=vertexSize+heightmargin;
                }
            }
            x+=vertexSize+widthmargin;
        }
        for(int i=0;i<genotype.edges.size();i++){
            // if(!genotype.edges.get(i).enabled){
            //     continue;
            // }
            int sourceIndex=genotype.edges.get(i).sourceIndex;
            int targetIndex=genotype.edges.get(i).targetIndex;
            int sourceX=0;
            int sourceY=0;
            int targetX=0;
            int targetY=0;
            
            for(int j=0;j<drawvertexs.size();j++){
                if(drawvertexs.get(j).index==sourceIndex){
                    sourceX=drawvertexs.get(j).x;
                    sourceY=drawvertexs.get(j).y;
                }
                if(drawvertexs.get(j).index==targetIndex){
                    targetX=drawvertexs.get(j).x;
                    targetY=drawvertexs.get(j).y;
                }
            }
            //if they are still 0, then there is an error
            if(sourceX==0 || sourceY==0 || targetX==0 || targetY==0){
                System.out.println("ERROR: source or target not found");
            }
            
            drawedges.add(new drawedge(sourceX+vertexSize/2, sourceY+vertexSize/2, targetX+vertexSize/2, targetY+vertexSize/2, getEdgeColor(genotype.edges.get(i))));
        }
        

        
    }




    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(LineThickness));
        super.paint(g2);
        
        for(int i=0;i<drawedges.size();i++){
            g2.setColor(drawedges.get(i).color);
            g2.drawLine(drawedges.get(i).x1, drawedges.get(i).y1, drawedges.get(i).x2, drawedges.get(i).y2);
        }
        for(int i=0;i<drawvertexs.size();i++){
            g2.setColor(drawvertexs.get(i).color);
            g2.fillRect(drawvertexs.get(i).x, drawvertexs.get(i).y, drawvertexs.get(i).size, drawvertexs.get(i).size);

        }
  
    }

    public static Color getVertexColor(vertexInfo.Type type){
        switch(type){
            case INPUT:
                return Color.BLUE;
            case HIDDEN:
                return Color.BLACK;
            case OUTPUT:
                return Color.RED;
            default:
                return Color.BLACK;
        }
    }
    public static Color edgecoler1 = Color.green;
    public static Color edgecoler2 = Color.red;
    
    public static Color getEdgeColor(edgeInfo edge){
        if(edge.weight>2){
            return edgecoler1;
        }else if(edge.weight<-2){
            return edgecoler2;
        }else{
        //cast weight to a value between 0 and 1, the weight is between -2 and 2
            double weight = (edge.weight+2)/4;
            int red = edgecoler1.getRed()+ (int)((edgecoler2.getRed()-edgecoler1.getRed())*weight);
            int green = edgecoler1.getGreen()+ (int)((edgecoler2.getGreen()-edgecoler1.getGreen())*weight);
            int blue = edgecoler1.getBlue()+ (int)((edgecoler2.getBlue()-edgecoler1.getBlue())*weight);
            return new Color(red, green, blue);
        }
    }


 


    public static class drawvertex{
        public int x;
        public int y;
        public int size;
        public Color color;
        public int index;
        public drawvertex(int x, int y, int size, Color color, int index){
            this.x=x;
            this.y=y;
            this.size=size;
            this.color=color;
            this.index=index;
        }
    }
    public static class drawedge{
        public int x1;
        public int y1;
        public int x2;
        public int y2;
        public Color color;
        public drawedge(int x1, int y1, int x2, int y2, Color color){
            this.x1=x1;
            this.y1=y1;
            this.x2=x2;
            this.y2=y2;
            this.color=color;
        }
    }
}
