package widgets.trends;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Pan on 6/18/2016.
 */


public class TrendsController  implements Initializable {
    @FXML
    private Pane root;
    @FXML
    private Pane woeTranslateRoot;
    @FXML
    private TextField location;

    public TrendsController()  {

    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {


        location.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().toString().equals("ENTER")) {
                    ((TrendsWidget) root.getParent()).setPlace(location.getText());
                    ((TrendsWidget) root.getParent()).forceUpdateUI();
                }
            }
        });
    }


    @FXML
    private void onAction(ActionEvent event) {
        ((Pane)root.getParent()).getChildren().remove(root);
    }


}
