package app;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class Controller {

    @FXML
    private ToggleGroup accOpToggle;

    @FXML
    private ToggleGroup accTypeToggle;

    @FXML
    private CheckBox directCheck;

    @FXML
    private CheckBox loyalCheck;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField balanceField;

    @FXML
    private Button accOpCmd;

    @FXML
    private Text accOpResult;

    @FXML
    private RadioButton lastNameSort;

    @FXML
    private ToggleGroup sortChoice;

    @FXML
    private RadioButton dateOpenSort;

    @FXML
    private Button printCmd;

    @FXML
    private Button clearCmd;

    @FXML
    private TextArea printDisplay;

    @FXML
    void accOpSelect() {
        if(accOpToggle.getSelectedToggle() == null) {
            loyalCheck.setDisable(true);
            directCheck.setDisable(true);
            balanceField.setDisable(true);
            accOpCmd.setDisable(true);
            return;
        }
        String selectedOperation = ((ToggleButton) accOpToggle.getSelectedToggle()).getText();
        switch (selectedOperation) {
            case "Open":
                loyalCheck.setDisable(false);
                directCheck.setDisable(false);
                balanceField.setDisable(false);
                break;
            case "Close":
                loyalCheck.setDisable(true);
                directCheck.setDisable(true);
                balanceField.setDisable(true);
                break;
            case "Deposit":
                loyalCheck.setDisable(true);
                directCheck.setDisable(true);
                balanceField.setDisable(false);
                break;
            default:
        }
        accOpResult.setText(selectedOperation + " account operation selected");
        filledFieldsCheck();
    }

    @FXML
    void filledFieldsCheck(){
        if(accOpToggle.getSelectedToggle() == null) {
            return;
        }
        String selectedOperation = ((ToggleButton) accOpToggle.getSelectedToggle()).getText();
        switch (selectedOperation) {
            case "Open":
            case "Deposit":
            case "Withdraw":
                if(balanceField.getText().isBlank()) {
                    accOpCmd.setDisable(true);
                    return;
                }
            default:
                if(firstNameField.getText().isBlank() || lastNameField.getText().isBlank() || accTypeToggle.getSelectedToggle() == null) {
                    accOpCmd.setDisable(true);
                    return;
                }
        }
        accOpCmd.setDisable(false);
    }
}
