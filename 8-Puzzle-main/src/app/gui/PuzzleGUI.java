package app.gui;
import app.IntState;
import app.algorithms.A_STAR;
import app.algorithms.BFS;
import app.algorithms.DFS;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class PuzzleGUI implements Initializable {

    @FXML
    private AnchorPane parentLayout;

    @FXML
    private Pane puzzleLayout;

    @FXML
    private VBox controlVBox;

    @FXML
    private HBox optionsHBox;

    @FXML
    private VBox radioButtonsVBox;

    @FXML
    private RadioButton BFSRadioBtn;

    @FXML
    private ToggleGroup algoSelectToggle;

    @FXML
    private RadioButton DFSRadioBtn;

    @FXML
    private RadioButton ASManRadioBtn;

    @FXML
    private RadioButton ASEucRadioBtn;

    @FXML
    private VBox animationCtrVBox;

    @FXML
    private Slider speedSlider;

    @FXML
    private Label animationSpeedLabel;

    @FXML
    private HBox btnDataHBox;

    @FXML
    private Button shuffleBtn;

    @FXML
    private Button solveBtn;

    @FXML
    private Button stopBtn;

    @FXML
    private TextField element00;

    @FXML
    private TextField element01;

    @FXML
    private TextField element02;

    @FXML
    private TextField element10;

    @FXML
    private TextField element11;

    @FXML
    private TextField element12;

    @FXML
    private TextField element20;

    @FXML
    private TextField element21;

    @FXML
    private TextField element22;

    @FXML
    private VBox infoLabelsVBox;

    @FXML
    private Label costLabel;

    @FXML
    private Label nodesExpandedLabel;

    @FXML
    private Label searchDepthLabel;

    @FXML
    private Label runningTimeLabel;

    private TextField[] boardMatrix;

    private int currState;
    private List<Integer> path;
    private int stateCount;


    private int getStateFromBoardMatrix() {
        int state = 0;
        for (int i = 0; i < 9; i++) {
            if (boardMatrix[i].getText().isEmpty())
                return -1;

            state += Integer.parseInt(boardMatrix[i].getText()) * Math.pow(10, 8 - i);
        }

        return state;
    }
    public void buildPuzzle() {
        currState = getStateFromBoardMatrix();
        if (currState == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill the board first.");
            alert.showAndWait();
            return;
        } /*else if (!puzzleIsSolvable(currState)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid initial state.");
            alert.showAndWait();
            return;
        }*/

        for (Tile t : puzzleLayout.getChildren().toArray(new Tile[0]))
            t.stopMoving();

        puzzleLayout.getChildren().clear();
        byte currElement;
        IntState intState = new IntState();
        for (byte i = 0; i < 9; i++) {
            currElement = intState.getValAtSlot(currState, i);
            if (currElement == 0)
                continue;
            puzzleLayout.getChildren().add(new Tile((i % 3) * 174 + 8, (i / 3) * 174 + 8, currElement, this));
        }
    }

    public void moveTile(int oldState, int newState) {
        IntState intState = new IntState();
        int tempOldState = oldState, tempNewState = newState;
        byte tileVal = 0;
        for (byte i = 0; i < 9; i++) {
            if (tempOldState % 10 != tempNewState % 10) {
                tileVal = (byte) (tempOldState % 10 != 0 ? tempOldState % 10 : tempNewState % 10);
                break;
            }

            tempOldState /= 10;
            tempNewState /= 10;

        }

        byte emptySlotNum = intState.getValSlotNum(oldState, (byte) 0);
        byte tileNum = intState.getValSlotNum(oldState, tileVal);



        Tile tile = null;
        for (Tile t : puzzleLayout.getChildren().toArray(new Tile[0])) {
            if (t.getTileVal() == tileVal) {
                tile = t;
                break;
            }
        }

        if (tileNum == emptySlotNum - 1) {
            tile.move(174, 0, (float) speedSlider.getValue());
        } else if (tileNum == emptySlotNum + 1) {
            tile.move(-174, 0, (float) speedSlider.getValue());
        } else if (tileNum == emptySlotNum + 3) {
            tile.move(0, -174, (float) speedSlider.getValue());
        } else {
            tile.move(0, 174, (float) speedSlider.getValue());
        }
    }

    protected void moveNextTile() {
        stateCount++;
        if (stateCount == path.size() - 1)
            return;
        moveTile(path.get(stateCount), path.get(stateCount + 1));
    }

    private boolean puzzleIsSolvable(int initialState) {
        byte[] state = new IntState().intStateToByteArr(initialState);
        int inv_count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 9; j++) {
                if (state[i] > state[j])
                    inv_count++;

            }
        }

        return inv_count%2 == 0;
    }

    private void setUpShuffleBtn() {
        shuffleBtn.setOnAction(e -> {
            List<Integer> container = new ArrayList<>();
            while (true) {
                int state = 0;
                for (int i = 0; i <= 8; i++)
                    container.add(i);

                for (int i = 0; i < 9; i++) {
                    Random generator = new Random();
                    Integer randomNumber = container.get(generator.nextInt(container.size()));
                    container.remove(randomNumber);
                    state += randomNumber * Math.pow(10, i);
                    boardMatrix[i].setText(String.format("%d", randomNumber));
                }

                currState = state;
                if (puzzleIsSolvable(currState))
                    break;
            }
            buildPuzzle();
        });
    }

    private void disableBoardToSolve() {
        shuffleBtn.setDisable(true);
        solveBtn.setDisable(true);
        stopBtn.setDisable(false);
        for (TextField t : boardMatrix)
            t.setDisable(true);

    }

    private void enableBoardAfterFinishing() {
        shuffleBtn.setDisable(false);
        solveBtn.setDisable(false);
        stopBtn.setDisable(true);
        for (TextField t : boardMatrix)
            t.setDisable(false);

    }

    private void setUpSolveBtn() {
        solveBtn.setOnAction(e -> {
            disableBoardToSolve();
            buildPuzzle();
            if (currState == -1) {
                enableBoardAfterFinishing();
                return;
            }
            stateCount = 0;
            int cost = 0;
            int expandedNodes = 0;
            int depth = 0;
            double runningTime = 0;

            long startTime = System.currentTimeMillis();
            if (BFSRadioBtn.isSelected()) {
                BFS bfs = new BFS();
                path = bfs.BFS(currState);
                cost = path != null ? path.size() - 1 : 0;
                expandedNodes = bfs.getNumberOfExpanded();
                depth = bfs.getMaxDepth();
            } else if (DFSRadioBtn.isSelected()) {
                DFS dfs = new DFS();
                path = dfs.DFS(currState);
                cost = path != null ? path.size() - 1 : 0;
                expandedNodes = dfs.getNumberOfExpanded();
                depth = dfs.getMaxDepth();
            } else {
                A_STAR a = new A_STAR();
                path = a.AStar(currState, ASManRadioBtn.isSelected() ? IntState.HeuristicsType.MANHATTAN : IntState.HeuristicsType.EUCLIDEAN);
                cost = path != null ? path.size() - 1 : 0;
                expandedNodes = a.getNumberOfExpanded();
                depth = a.getMaxDepth();
            }

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            runningTime = elapsedTime / 1000.0;

            if (path == null || path.size() < 2) {
                enableBoardAfterFinishing();
                return;
            }

            costLabel.setText("Cost=" + cost);
            nodesExpandedLabel.setText("Nodes Expanded=" + expandedNodes);
            searchDepthLabel.setText("Depth=" + depth);
            runningTimeLabel.setText("Time=" + String.format("%.2f", runningTime));
            moveTile(path.get(stateCount), path.get(stateCount + 1));
        });
    }

    private void setUpStopBtn() {
        stopBtn.setOnAction(e -> {
            buildPuzzle();
            enableBoardAfterFinishing();
        });
    }

    private void setUpBoardElements() {
        for (TextField t : boardMatrix)
            setUpBoardElement(t);
    }

    private void setUpBoardElement(TextField element) {
        element.textProperty().addListener((ov, oldValue, newValue) -> {
            if (element.getText().length() != 0 && (element.getText().charAt(0) < '0' || element.getText().charAt(0) > '8')) {
                element.setText("");
                return;
            } else if (element.getText().length() > 1) {
                String s = element.getText().substring(0, 1);
                element.setText(s);
                return;
            }

            for (TextField t : boardMatrix) {
                if (t != element && !t.getText().isEmpty() && t.getText().equals(element.getText())) {
                    t.setText("");
                    break;
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boardMatrix = new TextField[]{element00, element01, element02, element10, element11, element12, element20, element21, element22};
        setUpShuffleBtn();
        setUpSolveBtn();
        setUpStopBtn();
        setUpBoardElements();
        buildPuzzle();
    }
}
