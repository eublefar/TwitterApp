package widgets;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * Created by Pan on 6/18/2016.
 */


public class WidgetController {
@FXML private Pane root;
    @FXML
    private void onClose(ActionEvent event) {
        ((Pane)root.getParent()).getChildren().remove(root);
    }
}
