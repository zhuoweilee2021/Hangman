import javafx.geometry.Insets;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.control.Label;
        import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
        import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SaveAlertBox {

    static Button yesButton, noButton, cancelButton;
    static Label label;
    static Stage window;
    static boolean isSaved;
    public static boolean display(String title, ArrayList inputArrayList) {
        window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        initializeButtons(inputArrayList);

        GridPane top = new GridPane();
        top.setPadding(new Insets(20,20,20,20));
        top.add(label,0,0);

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(20,20,20,20));
        layout.setHgap(20);
        layout.add(yesButton, 0 , 0);
        layout.add(noButton, 1, 0);
        layout.add(cancelButton, 2,0);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(top);
        borderPane.setCenter(layout);
        borderPane.setPrefSize(300, 100);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(borderPane);
        window.setScene(scene);
        window.showAndWait();
        return isSaved;
    }

    private static void initializeButtons(ArrayList inputArrayList){
        label = new Label();
        label.setText("Save game?");
        yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            String filename = RequestFilenameAlertBox.display();
            if(filename.length() == 0) filename = "hangman";
            if (!filename.endsWith(".hng")) filename += ".hng";
            File f = popupSelectDirectory();
            if(f != null) {
                String directoryName = f.getAbsolutePath();
                System.out.println(directoryName + "\\" + filename);
                try {
                    FileOutputStream file = new FileOutputStream(directoryName + "\\" + filename);
                    ObjectOutputStream fout = new ObjectOutputStream(file);
                    fout.writeObject(inputArrayList);
                    fout.close();
                } catch (IOException i){
                    System.out.println("Could not write or save file!");
                }
                window.close();
            }
            isSaved = true;
        });
        noButton = new Button("No");
        noButton.setOnAction(e -> {
            window.close();
            isSaved = false;
        });
        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            window.close();
            isSaved = false;
        });
    }

    private static File popupSelectDirectory(){
        DirectoryChooser fc = new DirectoryChooser();
        File selectedFile = fc.showDialog(null);
        return selectedFile;
    }
}