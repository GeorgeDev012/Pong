package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Random;


public class Pong extends Application {


    private static final short rectangleWidth = 15;
    private static final short rectangleHeight = 100;
    private static int rectangleY;
    private AnchorPane anchorPane;
    private static Rectangle player1, player2;
    private static Circle circle;
    private static double velocityX = 2, velocityY;
    private static final double ENTRY_Y_VELOCITY = 2;
    private static double sideAnchor = 10.0;
    private static Scene playScene, dialogScene;
    private static double sceneWidth = 800, sceneHeight = 300;
    private static int firstPlayerScore = 0, secondPlayerScore = 0;
    private int winScore = 5;
    private static boolean goUpFirstPlayer, goDownFirstPlayer;
    private static boolean goUpSecondPlayer, goDownSecondPlayer;
    private int level = 3;
    private static MediaPlayer mediaPlayer;
    private static MediaPlayer mediaPlayer2;
    private static Text gameName, scoreText;
    private static double speed;
    private static Stage stage;
    private static boolean gameScene;
    private static Shape firstPlayerIntersect, secondPlayerIntersect;
    private AnimationTimer movementTimer;


    @Override
    public void start(Stage primaryStage) {

        stage = primaryStage;
        stage.setTitle("Pong");
        setDialogScene();
        setMainScene();
        stage.setScene(dialogScene);
        stage.setResizable(false);

        stage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }

    private void setMainScene() {
        setSound();
        setText();
        setPlayers();
        setCircle();
        playScene = new Scene(addAnchorPane());
        playScene.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        setKeyEvents();

        setAnchors();
    }

    private static void getIntersection() {
        firstPlayerIntersect = Shape.intersect(player1, circle);
        secondPlayerIntersect = Shape.intersect(player2, circle);
    }

    private void setDialogScene() {
        dialogScene = new Scene(new DialogScene());
        dialogScene.getStylesheets().add(this.getClass().getResource("/resources/dialogStyles.css").toExternalForm());
    }

    private void setCircle() {
        circle = new Circle();
        circle.setRadius(15);
        circle.setFill(new RadialGradient(40, 10, 7, 12, 8, true, CycleMethod.REFLECT, new Stop(0, Color.DARKRED), new Stop(1, Color.AQUAMARINE)));
        updateCircle();
    }

    private void setPlayers() {
        player1 = new Rectangle(rectangleWidth, rectangleHeight);
        player2 = new Rectangle(rectangleWidth, rectangleHeight);
        player1.setFill(Color.DEEPSKYBLUE);
        player2.setFill(Color.DARKMAGENTA);
        player1.setTranslateY(rectangleY);
        player2.setTranslateY(rectangleY);
    }

    private AnchorPane addAnchorPane() {
        anchorPane = new AnchorPane();
        anchorPane.setPrefSize(sceneWidth,sceneHeight);
        anchorPane.getChildren().addAll(player1, player2, circle, gameName, scoreText);
        anchorPane.setId("anchorPane");
        return anchorPane;
    }

    private void setAnchors() {
        AnchorPane.setLeftAnchor(player1, sideAnchor);
        AnchorPane.setRightAnchor(player2, sideAnchor);

    }

    private void setSound() {
        String musicFile = "/resources/creepy-background-daniel_simon.mp3";
        Media sound = new Media(Pong.class.getResource(musicFile).toString());
        mediaPlayer = new MediaPlayer(sound);

        String musicFile2 = "/resources/front-desk-bells-daniel_simon.mp3";
        Media sound2 = new Media(new File(musicFile2).toURI().toString());
        mediaPlayer2 = new MediaPlayer(sound2);

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer2.setStartTime(Duration.millis(0));
        mediaPlayer2.setStopTime(Duration.millis(500));
    }

    private void setText() {
        gameName = new Text("Pong");
        gameName.setFill(new LinearGradient(1, 3, 20, 15, false, null, new Stop(0, Color.GREEN), new Stop(0.7, Color.DARKVIOLET), new Stop(1, Color.DARKGREY)));
        gameName.setFont(Font.font("Verdana", 32));
        gameName.setTranslateX(sceneWidth / 2 - 40);
        gameName.setTranslateY(32);

        scoreText = new Text(firstPlayerScore + " : " + secondPlayerScore);
        scoreText.setFill(new LinearGradient(1, 3, 20, 15, false, null, new Stop(0, Color.RED), new Stop(0.7, Color.WHITE), new Stop(1, Color.GREEN)));
        scoreText.setFont(Font.font("Verdana", 32));
        scoreText.setTranslateX(sceneWidth / 2 - 40);
        scoreText.setTranslateY(64);
    }

    private void setKeyEvents() {
        playScene.setOnKeyPressed(event -> {
            switch(event.getCode()) {
                case UP:    goUpFirstPlayer = true; break;
                case DOWN:  goDownFirstPlayer = true; break;
                case W:     goUpSecondPlayer = true; break;
                case S:     goDownSecondPlayer = true; break;
            }
        });

        playScene.setOnKeyReleased(e -> {
            switch(e.getCode()) {
                case UP:    goUpFirstPlayer = false; break;
                case DOWN:  goDownFirstPlayer = false; break;
                case W:     goUpSecondPlayer = false; break;
                case S:     goDownSecondPlayer = false; break;
            }
        });
    }

    private static void createInstancesOfTimers() {
         new AnimationTimer() { // movement timers
            @Override
            public void handle(long now) {
                if(!gameScene) {
                    this.stop();
                }

                if (goUpFirstPlayer && player1.getTranslateY() > 0)
                    player1.setTranslateY(player1.getTranslateY() - 3 * Math.sqrt(DialogScene.ballSpeed));
                else if (goDownFirstPlayer && player1.getTranslateY() < (300 - rectangleHeight))
                    player1.setTranslateY(player1.getTranslateY() + 3 * Math.sqrt(DialogScene.ballSpeed));


                if (DialogScene.playerMode && goUpSecondPlayer && player2.getTranslateY() > 0)
                    player2.setTranslateY(player2.getTranslateY() - 3);
                else if (DialogScene.playerMode && goDownSecondPlayer && player2.getTranslateY() < (300 - rectangleHeight))
                        player2.setTranslateY(player2.getTranslateY() + 3);

                else if (!DialogScene.playerMode) { //computer player
                    double distance = sceneWidth - (sceneWidth / 5 * Math.sqrt(DialogScene.difficulty));
                    if (circle.getTranslateX() - circle.getRadius() > distance) {
                        if (player2.getTranslateY() < sceneHeight - rectangleHeight && (player2.getTranslateY() + rectangleHeight / 2) < circle.getTranslateY() + circle.getRadius())
                            player2.setTranslateY(player2.getTranslateY() + (2 + DialogScene.difficulty * Math.abs(velocityY / 2)));
                        else if (player2.getTranslateY() > 0)
                            player2.setTranslateY(player2.getTranslateY() - (2 + DialogScene.difficulty * Math.abs(velocityY / 2)));
                    }
                }
            }
        }.start();


        new AnimationTimer() { //circle and sounds
            @Override
            public void handle(long now) {

                if(gameScene) {
                    circle.setTranslateX(circle.getTranslateX() + velocityX);
                    circle.setTranslateY(circle.getTranslateY() + velocityY);

                    getIntersection();

                    if (firstPlayerIntersect.getBoundsInLocal().getWidth() != -1) {
                        if (velocityX > 0 && circle.getTranslateX() > sceneWidth / 2
                                || velocityX < 0 && circle.getTranslateX() < sceneWidth / 2) {
                            velocityX = -velocityX;
                            mediaPlayer2.seek(Duration.ZERO);
                            mediaPlayer2.play();
                        }
                    } else if (circle.getTranslateX() <= rectangleWidth + sideAnchor) // && circle.getTranslateX() >= sideAnchor)
                        secondPlayerGoal();

                    if (secondPlayerIntersect.getBoundsInLocal().getWidth() != -1) {
                        if (velocityX > 0 && circle.getTranslateX() > sceneWidth / 2
                                || velocityX < 0 && circle.getTranslateX() < sceneWidth / 2) {
                            velocityX = -velocityX;
                            mediaPlayer2.seek(Duration.ZERO);
                            mediaPlayer2.play();
                        }
                    } else if (circle.getTranslateX() >= sceneWidth - sideAnchor) firstPlayerGoal();

                    if (circle.getTranslateY() - circle.getRadius() < 0 || circle.getTranslateY() + circle.getRadius() > 300) {
                        velocityY *= -1.15;
                        velocityX *= speed;
                    }
                }
            }

            private void firstPlayerGoal() {
                firstPlayerScore++;
                updateScoreText();
                updateCircle();
                velocityY = ENTRY_Y_VELOCITY;
                if(firstPlayerScore >= DialogScene.goalsToWin) {
                    this.stop();
                    gameEnd();
                }
            }

            private void secondPlayerGoal() {
                secondPlayerScore++;
                updateScoreText();
                updateCircle();
                velocityY = ENTRY_Y_VELOCITY;
                if(secondPlayerScore >= DialogScene.goalsToWin) {
                    this.stop();
                    gameEnd();
                }
            }




            private void secondPlayerSide() {
                if(circle.getTranslateY() >= rectangleY && circle.getTranslateY() <= rectangleY + rectangleHeight) velocityX = -velocityX;
            }
        }.start();
    }

    private static void updateScoreText() {
        scoreText.setText(firstPlayerScore + " : " + secondPlayerScore);
    }

    private static void updateCircle() {
        Random rand = new Random();
        velocityX = rand.nextInt(2) == 0 ? 2 : -2;
        velocityY = rand.nextInt(2) == 0 ? 2 : -2;
        circle.setTranslateX(sceneWidth / 2 - circle.getRadius() / 2);
        circle.setTranslateY(sceneHeight / 2 - circle.getRadius() / 2);
    }
    
    private static void gameEnd() {
        //stopBallTimer();
        stopSound();
        resetGoals();
        gameScene = false;
        stage.setScene(dialogScene);
    }

    private static void startSound() {

    }

    private static void stopSound() {
        mediaPlayer2.stop();
        mediaPlayer.stop();
    }


    private static void resetGoals() {
        firstPlayerScore = 0;
        secondPlayerScore = 0;
        updateScoreText();
    }

    private static void setSpeed() {
        switch(DialogScene.ballSpeed) {
            case 1: speed = 1.1;   break;
            case 2: speed = 1.2;   break;
            case 3: speed = 1.3;    break;
            case 4: speed = 1.5;    break;
            case 5: speed = 1.7;      break;
        }
    }


    static void setPlayScene() {
        setSpeed();
        stage.setScene(playScene);
        mediaPlayer.play();
        gameScene = true;
        createInstancesOfTimers();
    }
}
