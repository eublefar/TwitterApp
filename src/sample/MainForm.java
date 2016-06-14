package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Created by Pan on 6/14/2016.
 */
public class MainForm {


    @FXML private ToolBar toolBar;
    @FXML private FlowPane flowpane;
   public MainForm() {

        System.out.println(toolBar==null);
       // toolBar.prefWidthProperty().bind(Main.primaryStage.widthProperty());

    }
    @FXML
    private void onAction(ActionEvent event) {
        System.out.println(flowpane==null);
    }
}
