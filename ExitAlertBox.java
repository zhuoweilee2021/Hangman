import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ExitAlertBox {

    static Button yesButton, noButton, cancelButton;
    static Label label;
    static Stage window;
    static boolean isCancelled = false;
    public static boolean display(String title, ArrayList inputArray) {
        window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        initializeButtons(inputArray);

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

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(borderPane);
        window.setScene(scene);
        window.showAndWait();
        return isCancelled;
    }

    private static void initializeButtons(ArrayList input){
        label = new Label();
        label.setText("Do you want to save the game before exiting?");
        yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            SaveAlertBox.display("Save" ,input);
            window.close();
            isCancelled = false;
        });
        noButton = new Button("No");
        noButton.setOnAction(e -> {
            window.close();
            isCancelled = false;
        });
        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            window.close();
            isCancelled = true;
        });
    }
}