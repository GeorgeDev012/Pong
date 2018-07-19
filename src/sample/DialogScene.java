package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DialogScene extends VBox {

    private ChoiceBox<String> levelChoice;
    private ChoiceBox<Integer> ballSpeedChoice;
    private RadioButton rb1, rb2;
    private final ToggleGroup group = new ToggleGroup();
    private TextField goalsField;
    static boolean playerMode;
    static int difficulty = 1;
    private String[] levels = {"Easy", "Medium", "Difficult", "Experienced", "Master"};
    private Integer[] ballSpeedLevels = {1, 2, 3, 4, 5};
    private Button submitButton;
    private Canvas canvas;
    private Image image;
    static int goalsToWin;
    static int ballSpeed = 3;

    DialogScene() {
        setVBox();
    }

    private void setVBox() {
        this.setId("vBox");
        setChildren();
        this.setSpacing(10);
        DialogScene.setMargin(rb1, new Insets(20, 20, 10, 20));
        DialogScene.setMargin(rb2, new Insets(0, 20, 10, 20));
        DialogScene.setMargin(levelChoice, new Insets(0, 20, 10, 20));
        DialogScene.setMargin(ballSpeedChoice, new Insets(0, 20, 10, 20));
        DialogScene.setMargin(goalsField, new Insets(0, 20, 10, 20));
        DialogScene.setMargin(submitButton, new Insets(0, 20, 20, 20));

        //VBox vb = new VBox(10, new ImageView(image), canvas);
        this.getChildren().addAll(rb1, rb2, ballSpeedChoice, levelChoice, goalsField, submitButton);
        //setCanvas();
        //this.getChildren().add(canvas);
    }

    private void setCanvas() {
        image = new Image("resources/Wallpaper39_1610.jpg");

        final double w = image.getWidth();
        final double h = image.getWidth();
        canvas = new Canvas(200, 200);
        canvas.getGraphicsContext2D().drawImage(image, 200, 200, 500, 500, 0, 0, this.getWidth(), this.getHeight());
    }

    private void setChildren() {
        // ball speed
        ballSpeedChoice = new ChoiceBox<>(FXCollections.observableArrayList(ballSpeedLevels));
        ballSpeedChoice.setValue(3);
        ballSpeedChoice.setTooltip(
                new Tooltip("Sets ball's acceleration"));
        setBallSpeedChoiceBoxListener();

        // difficulty
        levelChoice = new ChoiceBox<>(FXCollections.observableArrayList(levels));
        levelChoice.setValue("Easy");
        levelChoice.setTooltip(
                new Tooltip("Sets difficulty level"));

        setLevelChoiceBoxListener();

        // mode
        rb1 = new RadioButton("Player vs Computer");
        rb1.setFont(new Font("Verdana", 20));
        rb1.setTextFill(Color.BLACK);
        rb1.setUserData("Player vs Computer");

        rb2 = new RadioButton("Player vs Player");
        rb2.setFont(new Font("Verdana", 20));
        rb2.setTextFill(Color.BLACK);
        rb2.setUserData("Player vs Player");


        rb1.setToggleGroup(group);
        rb2.setToggleGroup(group);

        rb1.setSelected(true);
        setRadioGroupListener();

        // number of goalsToWin
        goalsField = new TextField();
        goalsField.setPromptText("set goals to win");
        setGoalsFieldListener();

        // button
        submitButton = new Button("Submit");
        submitButton.setAlignment(Pos.CENTER);
        setButtonListener();
    }

    private void setRadioGroupListener() {
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.getUserData().toString().equals("Player vs Player")) {
                    levelChoice.setDisable(true);
                    playerMode = true;
                } else {
                    levelChoice.setDisable(false);
                    playerMode = false;
                }
            }
        });
    }

    private void setBallSpeedChoiceBoxListener() {
        ballSpeedChoice.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> ballSpeed = newValue.intValue()
        );
    }

    private void setLevelChoiceBoxListener() {
        levelChoice.getSelectionModel().selectedIndexProperty().addListener(
                (ov, value, new_value) -> difficulty = new_value.intValue() + 1
        );
    }

    private void setGoalsFieldListener() {
    }

    private void setButtonListener() {
        submitButton.setOnAction(e -> {
                try {
                    goalsToWin = Integer.parseInt(goalsField.getText());
                    Pong.setPlayScene();
                } catch (NumberFormatException ex) {
                    // do nothing
                }
            }
        );
    }


}
