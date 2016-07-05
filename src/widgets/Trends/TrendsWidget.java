package widgets.trends;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import twitter4j.*;
import widgets.Widget;

import java.io.IOException;

/**
 * Created by Pan on 6/14/2016.
 */
public class TrendsWidget extends Widget {

    private String location;
    public ResponseList<Location> locations;
    private Twitter twitter;
    private Trends trends;
    public final ObservableList data =
            FXCollections.observableArrayList();

    public TrendsWidget() throws IOException {
        super("trends.fxml");
        location = "earth";
        twitter = TwitterFactory.getSingleton();
        System.out.println("constructor");

    }

    @Override
    protected void initBgWorker() {
        System.out.println("background");
        int idTrendLocation = 1;
        try {
            if(locations==null)
            locations = twitter.getAvailableTrends();

            for (Location loc : locations) {
                if (loc.getName().toLowerCase().equals(location.toLowerCase())) {
                    idTrendLocation = loc.getWoeid();
                    break;
                }
            }
            System.out.println("background1");
            trends = twitter.getPlaceTrends(idTrendLocation);
            System.out.println("after got trends");
        } catch (TwitterException e) {
            System.err.println("wrong");
            e.printStackTrace();
        }
    }

    @Override
    protected void updateUI() {
        System.out.println("updating");
        data.clear();
        for (Trend trend : trends.getTrends()) {
            data.add(trend.getName());
        }
        ListView lv =null;
        for (Node node : widgetPane.getChildren())
            if (node instanceof HBox) {
                for (Node node1 : ((HBox)node).getChildren())
                    if (node1 instanceof VBox) {
                        for (Node node2 : ((VBox)node1).getChildren())
                            if (node2 instanceof ListView) {
                                lv = (ListView) node2;
                                break;
                            }
                    }
            }
        if(lv == null) System.out.println("true");
        lv.setItems(data);

    }

    public void setPlace(String placeName) {
        location = placeName;
    }


}
