package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.io.File;
import java.util.Scanner;
import tms.*;

public class Controller {

    @FXML
    private TextField dateField;

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

    @FXML
    private Text importFileResult;

    @FXML
    private Text exportFileResult;

    private AccountDatabase appAccDatabase = new AccountDatabase();

    final private FileChooser fc = new FileChooser();

    {
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
    }

    @FXML
    void accOpSelect() {
        if(accOpToggle.getSelectedToggle() == null) {
            firstNameField.setDisable(true);
            lastNameField.setDisable(true);
            accTypeToggle.getToggles().forEach(toggle -> ((ToggleButton) toggle).setDisable(true));
            balanceField.setDisable(true);
            dateField.setDisable(true);
            loyalCheck.setDisable(true);
            directCheck.setDisable(true);
            accOpResult.setText("");
            return;
        }
        else {
            firstNameField.setDisable(false);
            lastNameField.setDisable(false);
            accTypeToggle.getToggles().forEach(toggle -> ((ToggleButton) toggle).setDisable(false));
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
                loyalCheck.setSelected(false);
                directCheck.setDisable(true);
                directCheck.setSelected(false);
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
        if(accTypeToggle.getSelectedToggle() == null) {
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
                if(!dateField.getText().matches("([0-1]?)[0-9](/)([0-3]?)[0-9](/)[0-9]([0-9]?)([0-9]?)([0-9]?)")) {
                    accOpCmd.setDisable(true);
                    return;
                }

                String[] dateValues = dateField.getText().split("(/)");
                int inputMonth = Integer.parseInt(dateValues[0]);
                int inputDay = Integer.parseInt(dateValues[1]);
                int inputYear = Integer.parseInt(dateValues[2]);
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
                String[] dateValues = dateField.getText().split("(/)");
                int inputMonth = Integer.parseInt(dateValues[0]);
                int inputDay = Integer.parseInt(dateValues[1]);
                int inputYear = Integer.parseInt(dateValues[2]);
                accOpenDate = new Date(inputYear, inputMonth, inputDay);
                switch (selectedAccType) {
                    case "Savings" -> {
                        if (appAccDatabase.add(new Savings(inputProfile, inputBalance, accOpenDate, loyalCheck.isSelected()))) {
                            accOpResult.setText("Account opened and added to the database.");
                            return;
                        }
                    }
                    case "Checking" -> {
                        if (appAccDatabase.add(new Checking(inputProfile, inputBalance, accOpenDate, directCheck.isSelected()))) {
                            accOpResult.setText("Account opened and added to the database.");
                            return;
                        }
                    }
                    default -> {
                        if (appAccDatabase.add(new MoneyMarket(inputProfile, inputBalance, accOpenDate, 0))) {
                            accOpResult.setText("Account opened and added to the database.");
                            return;
                        }
                    }
                }
                accOpResult.setText("Account is already in the database.");
            }
            case "Close" -> {
                switch (selectedAccType) {
                    case "Savings" -> {
                        if (appAccDatabase.remove(new Savings(inputProfile, inputBalance, accOpenDate, false))) {
                            accOpResult.setText("Account closed and removed from the database.");
                            return;
                        }
                    }
                    case "Checking" -> {
                        if (appAccDatabase.remove(new Checking(inputProfile, inputBalance, accOpenDate, false))) {
                            accOpResult.setText("Account closed and removed from the database.");
                            return;
                        }
                    }
                    default -> {
                        if (appAccDatabase.remove(new MoneyMarket(inputProfile, inputBalance, accOpenDate, 0))) {
                            accOpResult.setText("Account closed and removed from the database.");
                            return;
                        }
                    }
                }
                accOpResult.setText("Account does not exist.");
            }
            case "Deposit" -> {
                inputBalance = Double.parseDouble(balanceField.getText());
                DecimalFormat moneyFormat = new DecimalFormat("0.00");
                switch (selectedAccType) {
                    case "Savings" -> {
                        if (appAccDatabase.deposit(new Savings(inputProfile, 0, accOpenDate, false), inputBalance)) {
                            accOpResult.setText(moneyFormat.format(inputBalance) + " deposited to account.");
                            return;
                        }
                    }
                    case "Checking" -> {
                        if (appAccDatabase.deposit(new Checking(inputProfile, 0, accOpenDate, false), inputBalance)) {
                            accOpResult.setText(moneyFormat.format(inputBalance) + " deposited to account.");
                            return;
                        }
                    }
                    default -> {
                        if (appAccDatabase.deposit(new MoneyMarket(inputProfile, 0, accOpenDate, 0), inputBalance)) {
                            accOpResult.setText(moneyFormat.format(inputBalance) + " deposited to account.");
                            return;
                        }
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

    @FXML
    void importFile() {
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                Scanner sc = new Scanner(selectedFile);
                AccountDatabase inputDatabase = new AccountDatabase();

                while (sc.hasNextLine()) {
                    String[] inputAccValues = sc.nextLine().split("(,)");

                    if (inputAccValues.length != 6) {
                        importFileResult.setText("Database contains invalid data.");
                        return;
                    }

                    Profile tempProfile = new Profile(inputAccValues[1],inputAccValues[2]);

                    double tempBalance;
                    try {
                        tempBalance = Double.parseDouble(inputAccValues[3]);
                        if (tempBalance < 0) {
                            importFileResult.setText("Database contains invalid data.");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        importFileResult.setText("Database contains invalid data.");
                        return;
                    }
                    if (!inputAccValues[4].matches("([0-1]?)[0-9](/)([0-3]?)[0-9](/)[0-9]([0-9]?)([0-9]?)([0-9]?)")) {
                        importFileResult.setText("Database contains invalid data.");
                        return;
                    }

                    String[] tempDateValues = inputAccValues[4].split("(/)");
                    int tempMonth = Integer.parseInt(tempDateValues[0]);
                    int tempDay = Integer.parseInt(tempDateValues[1]);
                    int tempYear = Integer.parseInt(tempDateValues[2]);
                    Date tempDate = new Date(tempYear,tempMonth,tempDay);

                    switch (inputAccValues[0]) {
                        case "S" -> {
                            if (inputAccValues[5].equalsIgnoreCase("true")) {
                                inputDatabase.add(new Savings(tempProfile,tempBalance,tempDate,true));
                            }
                            else if (inputAccValues[5].equalsIgnoreCase("false")) {
                                inputDatabase.add(new Savings(tempProfile,tempBalance,tempDate,false));
                            }
                            else {
                                importFileResult.setText("Database contains invalid data.");
                                return;
                            }
                        }
                        case "C" -> {
                            if (inputAccValues[5].equalsIgnoreCase("true")) {
                                inputDatabase.add(new Checking(tempProfile,tempBalance,tempDate,true));
                            }
                            else if (inputAccValues[5].equalsIgnoreCase("false")) {
                                inputDatabase.add(new Checking(tempProfile,tempBalance,tempDate,false));
                            }
                            else {
                                importFileResult.setText("Database contains invalid data.");
                                return;
                            }
                        }
                        case "M" -> {
                            try {
                                int tempWithdrawals = Integer.parseInt(inputAccValues[5]);
                                if (tempWithdrawals >= 0) {
                                    inputDatabase.add(new MoneyMarket(tempProfile,tempBalance,tempDate,tempWithdrawals));
                                }
                                else {
                                    importFileResult.setText("Database contains invalid data.");
                                    return;
                                }
                            } catch (NumberFormatException e) {
                                importFileResult.setText("Database contains invalid data.");
                                return;
                            }
                        }
                        default -> {
                            importFileResult.setText("Database contains invalid data.");
                            return;
                        }
                    }
                }

                appAccDatabase = inputDatabase;
                importFileResult.setText("'" + selectedFile.getName() + "' successfully imported");
                sc.close();
            } catch (FileNotFoundException ignored) {}
            return;
        }
        importFileResult.setText("Please select a file.");
    }

    @FXML
    void exportFile() {
        File fileToExport = new File("ExportDatabase.txt");
        try {
            PrintWriter outputDatabase = new PrintWriter(fileToExport);

            for (Account account : appAccDatabase.getAccounts()) {
                if(account == null) {
                    exportFileResult.setText("Database successfully exported to 'ExportDatabase.txt'.");
                    outputDatabase.close();
                    return;
                }
                switch (account.getClass().toString()) {
                    case "class tms.Savings" -> {
                        outputDatabase.write("S,");
                        outputDatabase.write(account.getHolder().toString().replace(' ', ',') + ",");
                        outputDatabase.write(account.getBalance() + ",");
                        outputDatabase.write(account.getDateOpen().toString() + ",");
                        outputDatabase.write(((Savings) account).getLoyal() + "\n");
                    }
                    case "class tms.Checking" -> {
                        outputDatabase.write("C,");
                        outputDatabase.write(account.getHolder().toString().replace(' ', ',') + ",");
                        outputDatabase.write(account.getBalance() + ",");
                        outputDatabase.write(account.getDateOpen().toString() + ",");
                        outputDatabase.write(((Checking) account).getDirect() + "\n");
                    }
                    default -> {
                        outputDatabase.write("M,");
                        outputDatabase.write(account.getHolder().toString().replace(' ', ',') + ",");
                        outputDatabase.write(account.getBalance() + ",");
                        outputDatabase.write(account.getDateOpen().toString() + ",");
                        outputDatabase.write(((MoneyMarket) account).getWithdrawals() + "\n");
                    }
                }
            }
            exportFileResult.setText("Database successfully exported to 'ExportDatabase.txt'.");
            outputDatabase.close();
        } catch (FileNotFoundException ignored) {}
    }
}