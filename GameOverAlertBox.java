import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameOverAlertBox {

    static Button closeButton;
    static Label label;
    static Stage window;
    public static void display(String labelText) {
        window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Game Over");

        label = new Label(labelText);
        label.setFont(new Font("Dialog", 24));
        closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());

        VBox vbox = new VBox();
        vbox.setPrefSize(400, 200);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(100);
        vbox.getChildren().addAll(label, closeButton);

        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();
    }
}