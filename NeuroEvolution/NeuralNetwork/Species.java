package NeuroEvolution.NeuralNetwork;


import java.util.List;
import java.util.ArrayList;

public class Species {

    public List<Genotype> members;
    public double topFitness;
    public double staleness;
    public double fitnessSum;
    public Species(){
        members= new ArrayList<Genotype>();
    }

    public Genotype Breed(){
        double roll= Math.random();
        if(roll< Crossover.instance.CrossoverChance&&members.size()>1){
            int s1= (int)(Math.random()*members.size());
            int s2= (int)(Math.random()*members.size()-1);
            if(s2>=s1){
                s2++;

            }
            if(s1>s2){
                int temp= s1;
                s1= s2;
                s2= temp;
            }
            Genotype child= Crossover.instance.ProduceOffSpring(members.get(s1), members.get(s2));
            Mutation.instance.MutateAll(child);

            return child;

        }
        else{
            int s1= (int)(Math.random()*members.size());
            Genotype child= members.get(s1).Clone();
            Mutation.instance.MutateAll(child);
            return child;
        }
    }

    public void SortMembers(){
        members.sort((Genotype a, Genotype b)->{
            if(a.fitness>b.fitness){
                return -1;
            }
            else if(a.fitness<b.fitness){
                return 1;
            }
            else{
                return 0;
            }
        });
    }

    public void ReduceSpeciesSize(double portion){
        if(members.size()<=1){
            return;
        }
        int remaining= (int)(members.size()*portion);
        removeRange(members,remaining, members.size()-remaining);
    }
    public void ReduceSpeciesToOne(){
        if(members.size()<=1){
            return;
        }
        Genotype best= members.get(0);
        members.clear();
        members.add(best);
    }

    public void removeRange(List<Genotype> list, int start, int end){
        for(int i=0;i<end;i++){
            list.remove(start);
        }
    }    

    public void CalculateAdjustedFitnessSum(){
        double sum=0;
        for(int i=0;i<members.size();i++){
            sum+= members.get(i).adjustedFitness;
        }
        fitnessSum= sum;
    }
    

}
