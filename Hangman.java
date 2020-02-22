import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

public class Hangman extends Application implements Serializable {
    private Button newButton, loadButton, saveButton, exitButton;
    private ArrayList<Button> topMenuButtons = new ArrayList<>(), letterButtons = new ArrayList<>();
    private boolean savable = false, hasWon = false;
    private boolean[] pickedLetters = new boolean[26];
    private String randomWord, alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private int remainingGuesses = 10;
    private BorderPane borderPane;
    private ArrayList savedArray = new ArrayList();
    private Label remainingGuessesLabel;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new Image("HangmanIcon.png"));
        primaryStage.setTitle("Hangman");
        GridPane topMenu = initTopMenu();
        for (int i = 0; i < 4; i++) {
            topMenuButtons.get(i).setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            topMenu.add(topMenuButtons.get(i), i, 0);
        }
        initTopMenuButtonActions(primaryStage);

        Label hangmanLabel = new Label("   Hangman");
        hangmanLabel.setFont(new Font("Dialog", 30));
        hangmanLabel.setTextFill(Color.RED);
        topMenu.add(hangmanLabel, 4, 0);
        remainingGuessesLabel = new Label("           Remaining Guesses: " + remainingGuesses);
        remainingGuessesLabel.setFont(new Font("Dialog", 30));
        remainingGuessesLabel.setTextFill(Color.WHITE);
        topMenu.add(remainingGuessesLabel, 5, 0);
        canSave(savable);
        randomWord = pickRandomWord();

        borderPane = new BorderPane();
        borderPane.setTop(topMenu);
        borderPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        borderPane.setBottom(updatePickedLetters());
        borderPane.setRight(updateGuesses(Color.WHITE));

        Scene scene = new Scene(borderPane, 1300, 800);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            int index;
            if (key.getText().length() == 0) index = -1;
            else index = key.getText().charAt(0);
            index -= 97;
            if (index < 0 || hasWon || pickedLetters[index]){}
            else if (remainingGuesses <= 0){
            }
            else if (index >= 0 || index <= 25){
                pickedLetters[index] = true;
                canSave(true);
                borderPane.setBottom(updatePickedLetters());
                borderPane.setRight(updateGuesses(Color.WHITE));
                if(!randomWord.contains(Character.toString(key.getText().charAt(0)))) remainingGuesses--;
                borderPane.setLeft(displayHangman(remainingGuesses));
                remainingGuessesLabel.setText("           Remaining Guesses: " + remainingGuesses);
                if(hasWon){
                    canSave(false);
                    GameOverAlertBox.display("😊 You Win!");
                }
                else if(remainingGuesses == 0){
                    canSave(false);
                    borderPane.setRight(updateGuesses(Color.GREY));
                    GameOverAlertBox.display("🙁 You Lose! Word: " + randomWord);
                }
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println(randomWord);
    }

    public void canSave(boolean save){
        savable = save;
        saveButton.setVisible(savable);
    }

    private ImageView displayHangman(int remainingGuesses) {
        for (int i = 9; i >= 0; i--){
            if (9 - remainingGuesses == i)
                return new ImageView(new Image("hangman" + i + ".png"));
        }
        return null;
    }

    private GridPane initTopMenu() {
        GridPane topMenu = new GridPane();
        topMenu.setPadding(new Insets(20,20,20,20));
        topMenu.setHgap(20);
        Image newImage = new Image("New.png");
        newButton = new Button("New", new ImageView(newImage));
        Image loadImage = new Image("Load.png");
        loadButton = new Button("Load Game", new ImageView(loadImage));
        Image saveImage = new Image("Save.png");
        saveButton = new Button("Save", new ImageView(saveImage));
        Image exitImage = new Image("Exit.png");
        exitButton = new Button("Exit Game", new ImageView(exitImage));
        topMenuButtons.addAll(Arrays.asList(newButton, loadButton, saveButton, exitButton));
        topMenu.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        return topMenu;
    }

    private void initTopMenuButtonActions(Stage primaryStage){
        newButton.setOnAction(e -> {
            if(savable) SaveAlertBox.display("Alert!", updateSavedArray());
            resetBoard();
        });
        loadButton.setOnAction(e -> {
            if(savable) SaveAlertBox.display("Load", updateSavedArray());
            savedArray = LoadAlertBox.display();
            if(savedArray != null) {
                randomWord = (String) savedArray.get(0);
                remainingGuesses = (int) savedArray.get(1);
                for (int i = 0; i < 26; i++) {
                    pickedLetters[i] = (boolean) savedArray.get(i + 2);
                }
                borderPane.setRight(updateGuesses(Color.WHITE));
                borderPane.setBottom(updatePickedLetters());
                borderPane.setLeft(displayHangman(remainingGuesses));
                remainingGuessesLabel.setText("           Remaining Guesses: " + remainingGuesses);
                canSave(false);
            }
        });
        saveButton.setOnAction(e -> {
            canSave(!SaveAlertBox.display("Save", updateSavedArray()));
        });
        exitButton.setOnAction(e -> {
            if (savable) {
                if(!ExitAlertBox.display("Alert!", updateSavedArray())) primaryStage.close();
            }
            else primaryStage.close();
        });

    }

    private String pickRandomWord() {
        String s;
        File file = new File("words.txt");
        try {
            Scanner sc = new Scanner(file);
            ArrayList<String> words = new ArrayList<>();

            while (sc.hasNextLine())
                words.add(sc.nextLine());
            sc.close();
            Random random = new Random();
            s = words.get(random.nextInt(words.size()));
            if (s.length() == 0) {
                return pickRandomWord();
            }
            return s;
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    private GridPane updatePickedLetters(){
        GridPane letters = new GridPane();
        letters.setPadding(new Insets(10, 10, 10, 800));
        for(int i = 0; i < 26; i++){
            Button b = new Button(Character.toString(alphabet.charAt(i)));
            b.setPrefSize(50,50);
            b.setMinSize(50, 50);
            b.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            b.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            if(pickedLetters[i])
                b.setBackground(new Background(new BackgroundFill(Color.SLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));
            letterButtons.add(b);
            letters.add(b, i % 7, i / 7);
        }
        return letters;
    }

    private HBox updateGuesses(Color c){
        List<Button> wordCharacters = new ArrayList<>();
        hasWon = true;
        for(int i = 0; i < randomWord.length(); i++){
            Button t = new Button("__");
            t.setPadding(new Insets(0,2,0,2));
            t.setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            t.setFont(new Font("Dialog", 25));
            if (pickedLetters[randomWord.charAt(i) - 97]) {
                t.setText(Character.toString(randomWord.charAt(i)).toUpperCase());
                t.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            }
            else {
                if(c == Color.WHITE) t.setBackground(new Background(new BackgroundFill(c, CornerRadii.EMPTY, Insets.EMPTY)));
                else {
                    t.setText(Character.toString(randomWord.charAt(i)).toUpperCase());
                    t.setBackground(new Background(new BackgroundFill(c, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                hasWon = false;
            }
            wordCharacters.add(t);
        }

        HBox hBox = new HBox();
        hBox.setSpacing(0);
        hBox.getChildren().addAll(wordCharacters);
        hBox.setPadding(new Insets(300, 20, 20, 0));
        return hBox;
    }

    private void resetBoard(){
        remainingGuesses = 10;
        remainingGuessesLabel.setText("           Remaining Guesses: " + remainingGuesses);
        canSave(false);
        pickedLetters = new boolean[26];
        borderPane.setBottom(updatePickedLetters());
        borderPane.setLeft(null);
        randomWord = pickRandomWord().toLowerCase();
        borderPane.setRight(updateGuesses(Color.WHITE));
        hasWon = false;
    }

    private ArrayList updateSavedArray(){
        savedArray.clear();
        savedArray.add(randomWord);
        savedArray.add(remainingGuesses);
        for(int i = 0; i < 26; i++){
            savedArray.add(pickedLetters[i]);
        }
        return savedArray;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
