package Controllers;

import Models.Element;
import Models.Manager;
import javafx.collections.FXCollections;
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
import Util.Message;

public class AddCharge2Controller
{
    private AddCharge1Controller               addCharge1Controller;
    private Stage                              primaryStage;

    private ObservableList<Element>            elements;

    @FXML private TableColumn<Element, String> elementNameColumn = new TableColumn<>();
    @FXML private TableColumn<Element, String> minElementPercentColumn = new TableColumn<>();
    @FXML private TableColumn<Element, String> maxElementPercentColumn = new TableColumn<>();
    @FXML private TableView<Element>           elementsTable = new TableView<>();

    public void init(AddCharge1Controller addCharge1Controller)
    {
        this.addCharge1Controller = addCharge1Controller;
        this.primaryStage = this.addCharge1Controller.getPrimaryStage();

        this.elements = FXCollections.observableArrayList();
        for(Element aElement: Manager.getChargeElements())
        {
            this.elements.add(new Element(aElement));
        }

        this.elementNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.elementNameColumn.setEditable(false);

        initMinPercentColumn();
        initMaxPercentColumn();

        this.elementsTable.setEditable(true);
        this.elementsTable.setItems(this.elements);
        this.elementsTable.getColumns().clear();
        this.elementsTable.getColumns().addAll(this.elementNameColumn, this.minElementPercentColumn, this.maxElementPercentColumn);
    }

    @FXML private void backButtonClicked()
    {
        addCharge1Controller.backToScene();
    }

    @FXML private void nextButtonClicked()
    {
        for (Element aElement: this.elements)
        {
            if (aElement.getMinPercentDouble() > aElement.getMaxPercentDouble())
            {
                Message.showErrorMessage(ErrorMessage.MIN_BIGGER_THAN_MAX);
                return;
            }
        }

        Manager.setChargeElements(this.elements);
        loadAddCharge3Scene();
    }

    public void backToScene()
    {
        this.primaryStage.setScene(this.elementsTable.getScene());
    }

    public Stage getPrimaryStage()
    {
        return this.primaryStage;
    }

    private void loadAddCharge3Scene()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/AddCharge3Scene.fxml")
            );
            Parent root = loader.load();
            AddCharge3Controller addCharge3Controller = loader.getController();
            addCharge3Controller.init(this);
            this.primaryStage.setScene(new Scene(root));
        }
        catch (Exception ex)
        {
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
        }
    }

    private void initMinPercentColumn()
    {
        this.minElementPercentColumn.setCellValueFactory(new PropertyValueFactory<>("minPercent"));
        this.minElementPercentColumn.setEditable(true);
        this.minElementPercentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.minElementPercentColumn.setOnEditCommit(
                t ->
                {
                    double minPercent;
                    boolean validValue = true;
                    try
                    {
                        minPercent = Double.parseDouble(t.getNewValue());

                        if (minPercent < 0)
                        {
                            throw new RuntimeException(ErrorMessage.INCORRECT_AMOUNT);
                        }
                    }
                    catch (Exception ex)
                    {
                        Message.showErrorMessage(ErrorMessage.INCORRECT_MIN_PERCENT);
                        validValue = false;
                    }

                    if(validValue)
                    {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setMinPercent(Double.parseDouble(t.getNewValue()));
                    }
                }
        );
    }

    private void initMaxPercentColumn()
    {
        this.maxElementPercentColumn.setCellValueFactory(new PropertyValueFactory<>("maxPercent"));
        this.maxElementPercentColumn.setEditable(true);
        this.maxElementPercentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.maxElementPercentColumn.setOnEditCommit(
                t ->
                {
                    double maxPercent;
                    boolean validValue= true;
                    try
                    {
                        maxPercent = Double.parseDouble(t.getNewValue());

                        if (maxPercent < 0)
                        {
                            throw new RuntimeException(ErrorMessage.INCORRECT_AMOUNT);
                        }
                    }
                    catch (Exception ex)
                    {
                        Message.showErrorMessage(ErrorMessage.INCORRECT_MAX_PERCENT);
                        validValue = false;
                    }

                    if(validValue)
                    {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setMaxPercent(Double.parseDouble(t.getNewValue()));
                    }
                }
        );
    }
}
