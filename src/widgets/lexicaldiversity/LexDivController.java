package widgets.lexicaldiversity;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Pan on 7/4/2016.
 */
public class LexDivController implements Initializable {
    @FXML
    private Pane root1;
    @FXML
    private TextField filter;
    @FXML
    private ListView<String> list;
    @FXML
    private BarChart<String,Number> barChart;
    public LexDivController()  {

    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {


         filter.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().toString().equals("ENTER")) {
                    ((LexDivWidget) root1.getParent()).setFilter( filter.getText());
                    ((LexDivWidget) root1.getParent()).forceUpdateUI();
                }
            }
        });
        barChart.getData().add(new XYChart.Series<String,Number>());
    }


    @FXML
    private void onAction(ActionEvent event) {
        ((Pane)root1.getParent()).getChildren().remove(root1);
    }

    @FXML
    private void onAddUser(ActionEvent event) {
        ObservableList<String> selectedUsers = list.getSelectionModel().getSelectedItems();
        LexDivWidget thisWidget = ((LexDivWidget) root1.getParent());
        for(String s : selectedUsers) {
            thisWidget.addUsersToProcess(s);
        }
        thisWidget.forceUpdateUI();
    }


}