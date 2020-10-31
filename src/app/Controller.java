package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import java.text.DecimalFormat;
import tms.*;

public class Controller {

    @FXML
    private DatePicker dateField;

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
    private CheckBox lastNameCheck;

    @FXML
    private CheckBox dateOpenCheck;

    @FXML
    private TextArea printDisplay;

    final private AccountDatabase appAccDatabase = new AccountDatabase();

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
            case "Open" -> {
                balanceField.setDisable(false);
                dateField.setDisable(false);
                accOpResult.setText(selectedOperation + " account operation selected");
                accTypeSelect();
                return;
            }
            case "Close" -> {
                loyalCheck.setDisable(true);
                directCheck.setDisable(true);
                balanceField.setDisable(true);
                dateField.setDisable(true);
            }
            default -> {
                loyalCheck.setDisable(true);
                directCheck.setDisable(true);
                balanceField.setDisable(false);
                dateField.setDisable(true);
            }
        }
        accOpResult.setText(selectedOperation + " account operation selected");
        filledFieldsCheck();
    }

    @FXML
    void accTypeSelect() {
        if(accTypeToggle.getSelectedToggle() == null || accOpToggle.getSelectedToggle() == null) {
            loyalCheck.setDisable(true);
            directCheck.setDisable(true);
            return;
        }
        if(!((ToggleButton) accOpToggle.getSelectedToggle()).getText().equals("Open")) {
            loyalCheck.setDisable(true);
            directCheck.setDisable(true);
            return;
        }
        String selectedAccType = ((ToggleButton) accTypeToggle.getSelectedToggle()).getText();
        switch (selectedAccType) {
            case "Savings" -> {
                loyalCheck.setDisable(false);
                directCheck.setDisable(true);
            }
            case "Checking" -> {
                loyalCheck.setDisable(true);
                directCheck.setDisable(false);
            }
            default -> {
                loyalCheck.setDisable(true);
                directCheck.setDisable(true);
            }
        }
        filledFieldsCheck();
    }

    @FXML
    void filledFieldsCheck(){
        if(accOpToggle.getSelectedToggle() == null || accTypeToggle.getSelectedToggle() == null) {
            accOpCmd.setDisable(true);
            return;
        }
        String selectedOperation = ((ToggleButton) accOpToggle.getSelectedToggle()).getText();
        switch (selectedOperation) {
            case "Open":
                if(dateField.getValue() == null) {
                    accOpCmd.setDisable(true);
                    return;
                }
                int inputYear = dateField.getValue().getYear();
                int inputMonth = dateField.getValue().getMonthValue();
                int inputDay = dateField.getValue().getDayOfMonth();
                if(!(new tms.Date(inputYear,inputMonth,inputDay)).isValid()) {
                    accOpCmd.setDisable(true);
                    return;
                }
            case "Deposit":
            case "Withdraw":
                if(balanceField.getText().isBlank()) {
                    accOpCmd.setDisable(true);
                    return;
                }
                if(!balanceField.getText().matches("([0-9]+)(\\.?)([0-9]*)|([0-9]*)(\\.?)([0-9]+)")) {
                    accOpCmd.setDisable(true);
                    return;
                }
            default:
                if(firstNameField.getText().isBlank() || lastNameField.getText().isBlank()) {
                    accOpCmd.setDisable(true);
                    return;
                }
        }
        accOpCmd.setDisable(false);
    }

    @FXML
    void executeAccOp() {
        String selectedOperation = ((ToggleButton) accOpToggle.getSelectedToggle()).getText();

        String selectedAccType = ((ToggleButton) accTypeToggle.getSelectedToggle()).getText();
        Profile inputProfile = new tms.Profile(firstNameField.getText(),lastNameField.getText());
        tms.Date accOpenDate = new tms.Date(0,0,0);
        double inputBalance = 0;

        switch (selectedOperation) {
            case "Open" -> {
                inputBalance = Double.parseDouble(balanceField.getText());
                accOpenDate = new Date(dateField.getValue().getYear(), dateField.getValue().getMonthValue(), dateField.getValue().getDayOfMonth());
                switch (selectedAccType) {
                    case "Savings":
                        if (appAccDatabase.add(new Savings(inputProfile, inputBalance, accOpenDate, loyalCheck.isSelected()))) {
                            accOpResult.setText("Account opened and added to the database.");
                            return;
                        }
                    case "Checking":
                        if (appAccDatabase.add(new Checking(inputProfile, inputBalance, accOpenDate, directCheck.isSelected()))) {
                            accOpResult.setText("Account opened and added to the database.");
                            return;
                        }
                    default:
                        if (appAccDatabase.add(new MoneyMarket(inputProfile, inputBalance, accOpenDate, 0))) {
                            accOpResult.setText("Account opened and added to the database.");
                            return;
                        }
                }
                accOpResult.setText("Account is already in the database.");
            }
            case "Close" -> {
                switch (selectedAccType) {
                    case "Savings":
                        if (appAccDatabase.remove(new Savings(inputProfile, inputBalance, accOpenDate, false))) {
                            accOpResult.setText("Account closed and removed from the database.");
                            return;
                        }
                    case "Checking":
                        if (appAccDatabase.remove(new Checking(inputProfile, inputBalance, accOpenDate, false))) {
                            accOpResult.setText("Account closed and removed from the database.");
                            return;
                        }
                    default:
                        if (appAccDatabase.remove(new MoneyMarket(inputProfile, inputBalance, accOpenDate, 0))) {
                            accOpResult.setText("Account closed and removed from the database.");
                            return;
                        }
                }
                accOpResult.setText("Account does not exist.");
            }
            case "Deposit" -> {
                inputBalance = Double.parseDouble(balanceField.getText());
                DecimalFormat moneyFormat = new DecimalFormat("0.00");
                switch (selectedAccType) {
                    case "Savings":
                        if (appAccDatabase.deposit(new Savings(inputProfile, 0, accOpenDate, false), inputBalance)) {
                            accOpResult.setText(moneyFormat.format(inputBalance) + " deposited to account.");
                            return;
                        }
                    case "Checking":
                        if (appAccDatabase.deposit(new Checking(inputProfile, 0, accOpenDate, false), inputBalance)) {
                            accOpResult.setText(moneyFormat.format(inputBalance) + " deposited to account.");
                            return;
                        }
                    default:
                        if (appAccDatabase.deposit(new MoneyMarket(inputProfile, 0, accOpenDate, 0), inputBalance)) {
                            accOpResult.setText(moneyFormat.format(inputBalance) + " deposited to account.");
                            return;
                        }
                }
                accOpResult.setText("Account does not exist.");
            }
            default -> {
                inputBalance = Double.parseDouble(balanceField.getText());
                DecimalFormat moneyFormat = new DecimalFormat("0.00");
                int opResult;
                switch (selectedAccType) {
                    case "Savings" -> {
                        opResult = appAccDatabase.withdrawal(new Savings(inputProfile, 0, accOpenDate, false), inputBalance);
                        if (opResult == 0) {
                            accOpResult.setText(moneyFormat.format(inputBalance) + " withdrawn from account.");
                            return;
                        } else if (opResult == 1) {
                            accOpResult.setText("Insufficient funds.");
                            return;
                        }
                    }
                    case "Checking" -> {
                        opResult = appAccDatabase.withdrawal(new Checking(inputProfile, 0, accOpenDate, false), inputBalance);
                        if (opResult == 0) {
                            accOpResult.setText(moneyFormat.format(inputBalance) + " withdrawn to account.");
                            return;
                        } else if (opResult == 1) {
                            accOpResult.setText("Insufficient funds.");
                        }
                    }
                    default -> {
                        opResult = appAccDatabase.withdrawal(new MoneyMarket(inputProfile, 0, accOpenDate, 0), inputBalance);
                        if (opResult == 0) {
                            accOpResult.setText(moneyFormat.format(inputBalance) + " withdrawn to account.");
                            return;
                        } else if (opResult == 1) {
                            accOpResult.setText("Insufficient funds.");
                        }
                    }
                }
                accOpResult.setText("Account does not exist.");
            }
        }
    }

    @FXML
    void printOp() {
        printDisplay.clear();
        if (lastNameCheck.isSelected()) {
            printDisplay.appendText(appAccDatabase.printByLastName());
        }
        else if (dateOpenCheck.isSelected()) {
            printDisplay.appendText(appAccDatabase.printByDateOpen());
        }
        else {
            printDisplay.appendText(appAccDatabase.printAccounts());
        }
    }

    @FXML
    void sortChoice(ActionEvent actionEvent) {
        if (actionEvent.getSource() == lastNameCheck) {
            dateOpenCheck.setSelected(false);
        }
        else {
            lastNameCheck.setSelected(false);
        }
    }
}
