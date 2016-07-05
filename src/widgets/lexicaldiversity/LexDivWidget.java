package widgets.lexicaldiversity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import twitter4j.*;
import widgets.Widget;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Pan on 7/4/2016.
 */
public class LexDivWidget extends Widget {
    private String filter;
    private Twitter twitter;
    private HashMap<String, Long> friendList;
    private List<User> listFriends;
    private Queue<Long> usersToProcess;
    public final ObservableList userList =
            FXCollections.observableArrayList();
    private boolean filterUpdated = true;
    private boolean once = true;
    private BarChart<String,Number> bc;
    private List<XYChart.Data<String,Number>> charts;

    public LexDivWidget() throws IOException {
        super("lexdiv.fxml");
        twitter = TwitterFactory.getSingleton();
        friendList = new HashMap<>();
        usersToProcess = new ArrayDeque<>();
        filter = new String(".*");
        listFriends = new ArrayList<>();
        charts = new ArrayList<XYChart.Data<String,Number>>();
    }

    protected void initBgWorker()  {
        long cursor =-1L;
        IDs ids;
        if(once) {
            try {

                PagableResponseList<User> pagableFollowings;
                do {
                    pagableFollowings = twitter.getFriendsList(twitter.getId(), cursor);
                    for (User user : pagableFollowings) {
                        listFriends.add(user); // ArrayList<User>
                        friendList.put(user.getScreenName(), user.getId());
                        System.out.println(user.getScreenName());
                    }
                } while ((cursor = pagableFollowings.getNextCursor()) != 0);
               bc = (BarChart<String,Number>) getFirstNodeAt(HBox.class, BarChart.class);
//                bc.getData().add(new XYChart.Series<String,Number>());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            once=false;

        }
        getCharts();

    }

    protected void updateUI() {


        if(filterUpdated) {
            userList.clear();
            ListView lv =null;
            for (String userName : friendList.keySet()) {
                System.out.println(userName);
            }
            for (String userName : friendList.keySet()) {
                if (Pattern.compile(filter).matcher(userName).matches()) {

                    userList.add(userName);

                }
            }

            lv = (ListView) getFirstNodeAt(HBox.class, VBox.class, ListView.class);

            lv.setItems(userList);
            filterUpdated = false;
        }

        for (XYChart.Data<String,Number> it : charts) {
            bc.getData().get(0).getData().add(it);
        }
    }

    private void getCharts() {
        charts.clear();
        if(!usersToProcess.isEmpty()) {
            String userName = new String();

            try {
            do {
                long id = usersToProcess.remove();
                userName = twitter.showUser(id).getScreenName();
                double lexDiv = computeLexDiv(id);
                charts.add(new XYChart.Data<String,Number>(userName, lexDiv));
            }while(!usersToProcess.isEmpty());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
    }

    private double computeLexDiv(long id) {
        //TODO: all computations

        int pageno = 1;
        List<Status> statuses = new ArrayList();
        while (true) {

            try {

                int size = statuses.size();
                Paging page = new Paging(pageno++, 100);
                statuses.addAll(twitter.getUserTimeline(id, page));
                if (statuses.size() == size)
                    break;
            }
            catch(TwitterException e) {

                e.printStackTrace();
            }
        }
        long allTokens = 0L;
        Set<String> words = new HashSet<>();
        for(Status status : statuses) {
            List<String> statusWords = Arrays.asList(status.getText().split(" "));
            words.addAll(statusWords);
            allTokens+=statusWords.size();
        }
        return ((double) words.size())/((double)allTokens);
    }



    public void setFilter(String filter) {
        this.filter = filter;
        filterUpdated = true;
    }

    protected Node getFirstNodeAt(Class<?> ... args) {
        Node result;
        result = widgetPane;
        for(Class<?> NodeClass : args ) {
            for(Node i : ((Pane) result).getChildren()) {
                if(i.getClass() == NodeClass) {
                    result = i;
                }
            }
        }

        return result;
    }

    public void addUsersToProcess(String userName) {
        this.usersToProcess.add(friendList.get(userName));
    }
}
