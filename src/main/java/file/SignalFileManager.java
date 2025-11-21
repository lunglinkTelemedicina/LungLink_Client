package file;
import pojos.*;
import java.io.*;


public class SignalFileManager {
    public static void saveSignalToCSV(Signal signal, String filePath) throws IOException {
        try(FileWriter fw = new FileWriter(filePath)) {
            for(int v: signal.getValues() )fw.write(v+",");
        }
    }
    public static Signal getSignalFromCSV(String filePath, TypeSignal type, int clientId) throws IOException {
        Signal signal = new Signal(type, clientId);
        BufferedReader br=new BufferedReader(new FileReader(filePath));
        String line=br.readLine();
        if(line!=null){
            for(String p: line.split(",")){
                if(!p.isBlank()) signal.addSample(Integer.parseInt(p.trim()));
            }
        }
        br.close();
        return signal;
    }
}
