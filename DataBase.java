import java.io.*;
import java.util.HashMap;

public class DataBase {
    private final HashMap<String, Controller> dataBase = new HashMap<>();
    static private DataBase singleTone;

    static public DataBase getSingleTone() {
        if (singleTone == null) {
            singleTone = new DataBase();
        }
        return singleTone;
    }

    void addDataBase(String str, Controller controller) {

        dataBase.put(str, controller);
    }

    Controller getController(String str) {

        return dataBase.get(str);
    }
}

class Controller {
    private final File file;
    private RandomAccessFile raf;
    private FileWriter fw;

    public Controller(String str) {
        file = new File(str);
        try {
            raf = new RandomAccessFile(file,"rw");
            String last = readFile();
            fw = new FileWriter(file);
            writeFile(last);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String readFile() {
        StringBuilder recovery = new StringBuilder();
        String i;
        try {
            while ((i = raf.readLine()) != null) {
                recovery.append(i).append("\n");
            }
            raf.seek(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recovery.toString();
    }

    void writeFile(String str, boolean... reset) {
        try {
            if (reset.length != 0) {
                fw = new FileWriter(file);
            }
            fw.write(str);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getRow(String id) {
        String[] split = this.readFile().split("\n");
        for (String str : split) {
            if (str.startsWith(id)) {
                return str;
            }
        }
        return "invalid";
    }

    void removeId(String id) {

    }
}
