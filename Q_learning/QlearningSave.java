package Q_learning;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class QlearningSave {

    public static String path = "Q_learning\\Qlearning.txt";
    public static Character separator = ';';	
    public static void SaveList(double[][] largeList){
        System.out.println("saving");
        
        try{
            FileWriter writer = new FileWriter(path);
            for (int i=0;i<largeList.length;i++){
                for (int j=0;j<largeList[i].length;j++){
                    writer.write(largeList[i][j] + separator.toString());
                }
                if(i%100000==0)
                    System.out.println(i);
                writer.write("\n");
            }   
            writer.close();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }


    public static double[][] readList(){
        double[][] largeList =new double[4*41*41*41*41][3];
        //invert SaveList

        try{
            Scanner scanner = new Scanner(new File(path));
            int i=0;
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] values = line.split(separator.toString());
                for (int j=0;j<values.length;j++){
                    largeList[i][j] = Double.parseDouble(values[j]);
                }
                i++;
            }
            scanner.close();
        }
        catch (Exception e){
            System.out.println("no file exists");
            return largeList;
        }
        return largeList;

    }



}
