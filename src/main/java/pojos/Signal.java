package pojos;

import java.util.LinkedList;
import java.util.List;

public class Signal {

    //TODO EMPEZAR A AÑADIR TRY CATCH Y EXCEPTIONS
    private List<Integer> values;
    private String signalFile;
    private TypeSignal type;
    private static final int samplingRate=100; //en hercios
    private int clientId;

    public Signal(TypeSignal type, int clientId) {
        this.values=new LinkedList<Integer>();
        this.type=type;
        this.clientId=clientId;
    }


    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public String getSignalFile() {
        return signalFile;
    }

    public void setSignalFile(String signalFile) {
        this.signalFile = signalFile;
    }

    public TypeSignal getType() {
        return type;
    }

    public void setType(TypeSignal type) {
        this.type = type;
    }

    //este metodo es para q al mandarlo por sockets sea mas comodo, va a ser cadena con espacios
    public String valuesString() {
        StringBuilder sb = new StringBuilder();
        String sep=" ";

        for (int i=0;i<values.size();i++) {
            sb.append(values.get(i));
            if(i<values.size()-1) {
                sb.append(sep);
            }
        }
        return sb.toString();
    }

    public void setValuesFromBitalino (String data){
        this.values=new LinkedList<>();
        String[] element=data.split(",");
        for (String e:element) {
            this.values.add(Integer.parseInt(e));
        }
    }
}
