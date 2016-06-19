package GreetingForm;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.io.IOException;

/**
 * Created by Pan on 6/11/2016.
 */
public class GreetingForm {

    public GreetingForm() throws IOException, TwitterException {
        TwitterListener listener = new TwitterAdapter() {
            @Override
            public void gotOAuthAccessToken(AccessToken accessToken) {
                System.out.println("Successfully logged in");
                try {
                    onAccess();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onException(TwitterException e, TwitterMethod method) {

                    throw new AssertionError("Should not happen");

            }
        };

       TwitterAuth.getInstance().addListener(listener);
    }

    @FXML
    private void onLetsGo() throws TwitterException, IOException {
        TwitterAuth.getInstance().authenticate();

    }
    @FXML
    private void onExit() throws TwitterException {
        System.exit(0);
    }

    private void onAccess() throws IOException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Main.primaryStage.hide();
                Pane myPane = null;
                try {
                    myPane = (Pane) FXMLLoader.load(getClass().getResource("../MainWindow/main_window.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene myScene = new Scene(myPane);
                Main.primaryStage.setScene(myScene);
                Main.primaryStage.setMaximized(true);
                Main.primaryStage.setResizable(true);
                Main.primaryStage.show();
            }
        });
    }
}
