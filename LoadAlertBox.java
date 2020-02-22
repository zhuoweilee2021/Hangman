
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.*;

import java.io.*;
import java.util.ArrayList;

public class LoadAlertBox {

    public static ArrayList display() {
        File f = null;
        while(f == null) {
            f = popupSelectDirectory();
            if (f == null) return null;
            if (f != null) {
                String fileDirectory = f.getAbsolutePath();
                System.out.println(fileDirectory);
                try {
                    FileInputStream file = new FileInputStream(fileDirectory);
                    ObjectInputStream fin = new ObjectInputStream(file);
                    ArrayList a = (ArrayList)fin.readObject();
                    fin.close();
                    return a;
                } catch (ClassNotFoundException i) {
                    System.out.println("Could not write or save file!Load");
                }
                catch (IOException io){
                    System.out.println("io except");
                }
            }
        }
        return null;
    }

    private static File popupSelectDirectory(){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new ExtensionFilter("HangMan files", "*.hng"));
        File selectedFile = fc.showOpenDialog(null);
        return selectedFile;
    }
}