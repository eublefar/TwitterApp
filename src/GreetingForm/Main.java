package greetingform;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    static public Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane myPane = (Pane) FXMLLoader.load(getClass().getResource("greetings.fxml"));
        Scene myScene = new Scene(myPane);
        this.primaryStage = primaryStage;
        primaryStage.setScene(myScene);
        primaryStage.setTitle("My Twitter Stats");
        primaryStage.getIcons().add(new Image("http://st2.depositphotos.com/1688079/9969/i/950/depositphotos_99692276-Statistics-icon-glossy-cyan-blue-round-button.jpg"));
        primaryStage.setWidth(800);
        primaryStage.setHeight(500);
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
