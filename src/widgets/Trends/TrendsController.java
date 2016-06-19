package widgets.Trends;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * Created by Pan on 6/18/2016.
 */


public class TrendsController {
@FXML private Pane root;
    @FXML
    private void onAction(ActionEvent event) {
        ((Pane)root.getParent()).getChildren().remove(root);
    }
}
