package Controllers;

import Models.CompInCharge;
import Models.Manager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import Util.ErrorMessage;
import Util.Helper;

public class AddCharge4Controller
{
    private AddCharge3Controller                    addCharge3Controller;
    private Stage                                   primaryStage;

    private ObservableList<CompInCharge>            components;

    @FXML private TableColumn<CompInCharge, String> componentNameColumn = new TableColumn<>();
    @FXML private TableColumn<CompInCharge, String> minComponentPercentColumn = new TableColumn<>();
    @FXML private TableColumn<CompInCharge, String> maxComponentPercentColumn = new TableColumn<>();
    @FXML private TableView<CompInCharge>           componentsTable = new TableView<>();

    private static final int                        INVALID_VALUE = -1;

    @FXML public  void init(AddCharge3Controller addCharge3Controller)
    {
        this.addCharge3Controller = addCharge3Controller;
        this.primaryStage = this.addCharge3Controller.getPrimaryStage();

        this.components = Manager.getChargeMandatoryComps();
        for (CompInCharge aComp: this.components)
        {
            aComp.setMinPercent(INVALID_VALUE);
            aComp.setMaxPercent(INVALID_VALUE);
        }

        this.componentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        initMinPercentColumn();
        initMaxPercentColumn();

        this.componentsTable.setEditable(true);
        this.componentsTable.setItems(this.components);
        this.componentsTable.getColumns().clear();
        this.componentsTable.getColumns().addAll(this.componentNameColumn,
                                                 this.minComponentPercentColumn,
                                                 this.maxComponentPercentColumn);
    }

    @FXML private void backButtonClicked()
    {
        addCharge3Controller.backToScene();
    }

    @FXML private void nextButtonClicked()
    {
        if (!areAllFieldsFilled())
        {
            Helper.showErrorMessage(ErrorMessage.EMPTY_FIELDS);
            return;
        }

        if (minPercentIsBiggerThanMax())
        {
            Helper.showErrorMessage(ErrorMessage.MIN_BIGGER_THAN_MAX);
            return;
        }

        if (sumOfMinIsBiggerThan100())
        {
            Helper.showErrorMessage(ErrorMessage.MIN_SUM_BIGGER_THAN_100);
            return;
        }

        if (sumOfMaxIsLessThan100())
        {
            Helper.showErrorMessage(ErrorMessage.MAX_SUM_LESS_THAN_100);
            return;
        }

        if (Manager.isChargePossible())
        {
            Manager.calculateCheapCharge();
            loadChargeResult();
        }
        else
        {
            Helper.showErrorMessage(ErrorMessage.CANNOT_CALCULATE_CHARGE);
        }
    }

    public void backToScene()
    {
        this.primaryStage.setScene(this.componentsTable.getScene());
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    private void initMinPercentColumn()
    {
        this.minComponentPercentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.minComponentPercentColumn.setOnEditCommit(
                t ->
                {
                    double percent;

                    try
                    {
                        percent = Double.parseDouble(t.getNewValue());

                        if (percent < 0 || percent > 100)
                        {
                            throw new RuntimeException(ErrorMessage.INCORRECT_MIN_PERCENT);
                        }

                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setMinPercent(percent);
                    }
                    catch (Exception ex)
                    {
                        Helper.showErrorMessage(ErrorMessage.INCORRECT_MIN_PERCENT);

                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setMinPercent(INVALID_VALUE);
                    }
                }
        );
    }

    private void initMaxPercentColumn()
    {
        this.maxComponentPercentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.maxComponentPercentColumn.setOnEditCommit(
                t ->
                {
                    double percent;

                    try
                    {
                        percent = Double.parseDouble(t.getNewValue());

                        if (percent < 0 || percent > 100)
                        {
                            throw new RuntimeException(ErrorMessage.INCORRECT_MAX_PERCENT);
                        }

                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setMaxPercent(percent);
                    }
                    catch (Exception ex)
                    {
                        Helper.showErrorMessage(ErrorMessage.INCORRECT_MAX_PERCENT);

                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setMaxPercent(INVALID_VALUE);
                    }
                }
        );
    }

    private void loadChargeResult()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/ChargeResultScene.fxml")
            );
            Parent root = loader.load();
            ChargeResultController chargeResultController = loader.getController();
            chargeResultController.init(this);
            this.primaryStage.setScene(new Scene(root));
        }
        catch (Exception ex)
        {
            Helper.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
        }
    }

    private boolean areAllFieldsFilled()
    {
        for (CompInCharge aComp: this.components)
        {
            if (aComp.getMinPercent() == INVALID_VALUE || aComp.getMaxPercent() == INVALID_VALUE)
            {
                return false;
            }
        }

        return true;
    }

    private boolean minPercentIsBiggerThanMax()
    {
        for (CompInCharge aComp: this.components)
        {
            if (aComp.getMinPercent() > aComp.getMaxPercent())
            {
                return true;
            }
        }

        return false;
    }

    private boolean sumOfMinIsBiggerThan100()
    {
        double sumOfMinPercents = 0;

        for (CompInCharge aComp: this.components)
        {
            sumOfMinPercents += aComp.getMinPercent();
        }

        if (sumOfMinPercents > 100)
        {
            return true;
        }

        return false;
    }

    private boolean sumOfMaxIsLessThan100()
    {
        double sumOfMaxPercents = 0;

        for (CompInCharge aComp: this.components)
        {
            sumOfMaxPercents += aComp.getMaxPercent();
        }

        if (sumOfMaxPercents < 100)
        {
            return true;
        }

        return false;
    }
}
