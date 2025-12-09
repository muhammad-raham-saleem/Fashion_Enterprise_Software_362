package util;


import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class LogManager {

    private final String logFile;

    public LogManager(String logFile){
        this.logFile = logFile;
    }

    public void logPriceChange(int productID, double oldPrice, double newPrice){
        try (FileWriter fw = new FileWriter(logFile, true)){
            fw.write("Price Change | Product ID: " + productID +
                " Old price: " + oldPrice +
                
                " New Price: " + newPrice +

                " Time: " + LocalDateTime.now() + "\n");
        }
        catch (IOException e) {
            System.out.println("Failed to write logs"); 
        }
    }
    

    public List<String> readAllLines(){
        return FileManager.readLines(logFile);
    }
    
}
