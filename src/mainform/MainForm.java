package mainform;

import TwitterWidgets.Trends.TrendsWidget;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

/**
 * Created by Pan on 6/14/2016.
 */
public class MainForm {


    @FXML private ToolBar toolBar;
    @FXML private FlowPane flowpane;
    @FXML private ScrollPane scrollPane;
   public MainForm() {
        System.out.println(toolBar==null);

    }
    @FXML
    private void onAction(ActionEvent event) throws IOException {
        System.out.println(flowpane==null);
        flowpane.getChildren().add(new TrendsWidget());
        flowpane.prefWrapLengthProperty().bind(scrollPane.widthProperty().subtract(50));

    }
}
