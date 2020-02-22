import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;

public class RequestFilenameAlertBox {

    public static String display() {
        AtomicReference<String> filename = new AtomicReference<>("hangman");
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(400);

        Label label = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        label2.setText("Game will be saved as hangman.hng unless specified below");
        label3 .setText("Then select the directory to save the file in");
        label.setText("Enter the file name to save the game as:");
        TextField textField = new TextField();
        Button closeButton = new Button("OK");
        closeButton.setOnAction(e -> {
            filename.set(textField.getText());
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label2, label3, label, textField, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        return filename.get();
    }

}
