package app.main;
import app.IntState;
import app.gui.PuzzleGUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{

		PuzzleGUI puzzleGUI = null;
		Parent parent = null;
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/gui/gui.fxml"));
			loader.setController(puzzleGUI);
			parent = loader.load();
			puzzleGUI = loader.getController();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		Scene scene = new Scene(parent, 550, 750);
		primaryStage.setScene(scene);
		primaryStage.setTitle("8 Puzzle");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
   }

   private static byte[] scanStartState(int boardSize) {
	    Scanner scanner = new Scanner(System.in);
	    System.out.println("**Enter the puzzle elements (use zero to express empty field)**");
	    byte[] inputState = new byte[boardSize];
	    for (int i = 0; i < inputState.length; i++) {
            System.out.print("Enter the field in row=" + (int)(i/Math.sqrt(boardSize)) + ", col=" + (int)(i%Math.sqrt(boardSize)) + " :-");
            inputState[i] = scanner.nextByte();
        }

	    return inputState;

   }

}
