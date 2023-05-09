package app.gui;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Tile extends StackPane {

    private Rectangle rectangle = new Rectangle();
    private Label label = new Label();
    private boolean tileIsMoving = false;
    private PuzzleGUI puzzleGUI;
    private Timeline movingTimeLine;

    public Tile(float x, float y, int num, PuzzleGUI puzzleGUI) {
        this.puzzleGUI = puzzleGUI;
        label.setText(String.format("%d", num));
        label.setFont(new Font("Tahoma", 30));
        label.styleProperty().set("-fx-text-fill: #c6e4d7;" + "-fx-font-weight: bold;");


        styleProperty().set("-fx-background-color: transparent;");
        setPrefHeight(166);
        setPrefWidth(166);
        rectangle.setWidth(166);
        rectangle.setHeight(166);
        rectangle.strokeWidthProperty().set(2);
        rectangle.setStroke(Color.rgb(14,72,92,1));
        rectangle.setFill(Color.rgb(67,104,142,1));

        getChildren().addAll(rectangle, label);
        setLayoutX(x);
        setLayoutY(y);
    }

    public void move(double addToX, double addToY, float speed) {
        if (addToY == 0) {
            KeyValue xKeyValue = new KeyValue(this.layoutXProperty(), getLayoutX() + addToX);
            KeyFrame xKeyFrame = new KeyFrame(Duration.millis(200 * (100 / speed)), xKeyValue);
            movingTimeLine = new Timeline(xKeyFrame);
        }
        else {
            KeyValue yKeyValue = new KeyValue(this.layoutYProperty(), getLayoutY() + addToY);
            KeyFrame yKeyFrame = new KeyFrame(Duration.millis(200 * (100 / speed)), yKeyValue);
            movingTimeLine = new Timeline(yKeyFrame);
        }

        movingTimeLine.setOnFinished(e -> {
            tileIsMoving = false;
            puzzleGUI.moveNextTile() ;
        });
        tileIsMoving = true;
        movingTimeLine.play();
    }

    public void stopMoving() {
        if (tileIsMoving)
            movingTimeLine.stop();
    }


    public boolean isTileIsMoving() {
        return tileIsMoving;
    }

    public byte getTileVal() {
        return Byte.parseByte(label.getText());
    }



}
