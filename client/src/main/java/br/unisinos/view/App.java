package br.unisinos.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class App extends Application {

    private static Stage primartyStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primartyStage = primaryStage;

        primaryStage.initStyle(StageStyle.UNDECORATED);

        Parent rootWindow = FXMLLoader.load(ClassLoader.getSystemResource("views/login-view.fxml"));
        primaryStage.setTitle("Java Chat");

        Scene scene = new Scene(rootWindow, 350, 350);
        scene.setRoot(rootWindow);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> Platform.exit());
    }

    public static Stage getPrimartyStage() {
        return primartyStage;
    }
}