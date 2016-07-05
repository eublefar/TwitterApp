package widgets;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by Pan on 6/14/2016.
 */
public class Widget extends StackPane {
    protected StackPane THIS = this;
    protected Pane widgetPane;
    private ProgressIndicator progressIndicator;
    public Widget(String widgetFXMLName) throws IOException {
        super();
        loadWidgetPane(widgetFXMLName);
        this.getChildren().add(widgetPane);
        progressIndicator = new ProgressIndicator();
        VBox box = new VBox(progressIndicator);
        box.setAlignment(Pos.CENTER);
        // Grey Background
        widgetPane.setDisable(true);
        this.getChildren().add(box);

        Task<Integer> task = new Task<Integer>() {
            @Override protected Integer call() throws Exception {

                initBgWorker();
                System.out.println("widget background");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                        System.out.println("widget run later");
                        widgetPane.setDisable(false);
                        THIS.getChildren().remove(box);
                    }
                });
                return 0;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
    private void loadWidgetPane(String widgetFXMLName) throws IOException {
        widgetPane = (Pane) FXMLLoader.load(getClass().getResource(widgetFXMLName));
        if(widgetPane instanceof BorderPane) System.out.println("widgetPane is BorderPane");
    }



    protected void initBgWorker() {

    }

    protected void updateUI() {

    }
    public void forceUpdateUI() {
        progressIndicator = new ProgressIndicator();
        VBox box = new VBox(progressIndicator);
        box.setAlignment(Pos.CENTER);
        // Grey Background
        widgetPane.setDisable(true);
        this.getChildren().add(box);

        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {

                initBgWorker();
                System.out.println("widget background");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                        System.out.println("widget run later");
                        widgetPane.setDisable(false);
                        THIS.getChildren().remove(box);
                    }
                });
                return 0;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    protected Node getFirstNodeAt(Class<?> ... args) {
        Node result;
        result = widgetPane;
        for(Class<?> NodeClass : args ) {
            for(Node i : ((Pane) result).getChildren()) {
                System.out.println(NodeClass.getName()+ " -->" + i.getClass().getName());
                if(i.getClass() == NodeClass) {
                    result = i;
                    break;
                }
            }
        }
        return result;
    }

    protected Node getLastNodeAt(Class<?> ... args) {
        Node result;
        result = widgetPane;
        for(Class<?> NodeClass : args ) {
            for(Node i : ((Pane) result).getChildren()) {
                System.out.println(NodeClass.getName()+ " -->" + i.getClass().getName());
                if(i.getClass() == NodeClass) {
                    result = i;
                }
            }
        }
        return result;
    }
}
