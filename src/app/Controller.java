package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import tms.*;

public class Controller {
    @FXML
    private Button btnOK;
    @FXML
    private Label lblClick;

    public void handleBtnOK(ActionEvent actionEvent) {
        lblClick.setText("clicked ecksdee");
        btnOK.setVisible(false);
    }
}
