package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import twitter4j.*;

import java.io.IOException;

/**
 * Created by Pan on 6/14/2016.
 */
public class TrendsWidget extends Widget {

    private Twitter twitter;
    private Trends trends;
    public static final ObservableList data =
            FXCollections.observableArrayList();

    public TrendsWidget() throws IOException {
        super("trends.fxml");
        twitter = TwitterFactory.getSingleton();
        System.out.println("constructor");
    }

    @Override
    protected void initBgWorker() {
        System.out.println("background");
        try {
            System.out.println("background1");
            trends = twitter.getPlaceTrends(1);
            System.out.println("after got trends");
        } catch (TwitterException e) {
            System.err.println("wrong");
            e.printStackTrace();
        }
    }

    @Override
    protected void updateUI() {
        System.out.println("updating");
        for (Trend trend : trends.getTrends()) {
            data.add(trend.getName());
        }
        ListView lv =null;
        for (Node node : widgetPane.getChildren())
            if (node instanceof ListView) {
                lv = (ListView) node;
            }
        lv.setItems(data);

    }

    public class TrendsController {
        public TrendsController() {

        }
        @FXML
        private void onAction(ActionEvent event) {
            ((Pane)THIS.getParent()).getChildren().remove(THIS);
        }
    }
}
