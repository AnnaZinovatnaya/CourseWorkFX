package Controllers;

import Models.Component;
import Models.Element;
import Models.Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Font;
import util.ErrorMessage;


public class AddElements2Controller
{
    private AddElementsController              addElementsController;
    private AddComponentController             addComponentController;
    private Alert                              alert = new Alert(Alert.AlertType.ERROR);
    private ObservableList<String>             selectedItems;
    private ObservableList<Element>            elements;
    private Component                          component;

    @FXML private TableColumn<Element, String> elementColumn = new TableColumn<>();
    @FXML private TableColumn<Element, String> percentColumn = new TableColumn<>();
    @FXML private TableColumn<Element, String> adoptColumn = new TableColumn<>();
    @FXML private TableView<Element>           elementsTable = new TableView<>();

    public void setPreviousController(AddElementsController addElementsController)
    {
        this.addElementsController = addElementsController;
        this.component = addElementsController.getAddComponentController().getComponent();
    }

    public void setPreviousController(AddComponentController addComponentController)
    {
        this.addComponentController = addComponentController;
        this.component = addComponentController.getComponent();
    }

    @FXML public  void init(ObservableList<String> selectedItems)
    {
        this.elementsTable.setEditable(true);

        elements = FXCollections.observableArrayList ();
        this.selectedItems = selectedItems;
        for (String selectedItem : selectedItems)
        {
            elements.add(new Element(selectedItem, 0, 0, 0, 0));
        }

        this.elementColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        this.percentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.percentColumn.setOnEditCommit(
            t ->
        {
            boolean b= true;
            double percent = 0;
            try
            {
                percent = Double.parseDouble(t.getNewValue());

                if (percent < 0)
                {
                    throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
                }
            }
            catch (Exception ex)
            {
                alert.setContentText(ErrorMessage.INCORRECT_PERCENT);
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();

                b=false;
            }

            if(b)
            {
                t.getTableView().getItems().get(
                    t.getTablePosition().getRow()).setPercent(Double.parseDouble(t.getNewValue()));
            }
            else
            {
                t.getTableView().getItems().get(
                    t.getTablePosition().getRow()).setPercent(0);

            }
        }
        );

        this.adoptColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.adoptColumn.setOnEditCommit(
            t ->
        {
            boolean b= true;
            double adapt = 0;
            try
            {
                adapt = Double.parseDouble(t.getNewValue());
                if (adapt < 0)
                {
                    throw new RuntimeException(ErrorMessage.INCORRECT_ADAPT);
                }
            }
            catch (Exception ex)
            {
                alert.setContentText(ErrorMessage.INCORRECT_ADAPT);
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b=false;
            }

            if(b)
            {
                t.getTableView().getItems().get(
                    t.getTablePosition().getRow()).setAdopt(Double.parseDouble(t.getNewValue()));
            }
        }
        );
        this.elementsTable.setItems(elements);
        this.elementsTable.getColumns().clear();
        this.elementsTable.getColumns().addAll(elementColumn, percentColumn, adoptColumn);

    }

    @FXML private void backButtonClicked()
    {
        addComponentController.backToScene();
    }

    @FXML private void finishedButtonClicked()
    {
        boolean b=true;
        for(Element aElement: elementsTable.getItems())
        {
            if(aElement.getPercent()==0)
                b=false;
            if(aElement.getAdopt()==0)
                b=false;
        }
        if (b)
        {
            for (int i = 0; i < selectedItems.size(); i++)
            {
                Manager.setComponentElement(selectedItems.get(i),
                                             elementsTable.getItems().get(i).getPercent(),
                                             elementsTable.getItems().get(i).getAdopt());
            }
            try
            {
                Manager.saveComponentParam();
                Manager.saveComponentElements();

                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Information");
                alert.setContentText("Компонент сохранен!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label) node).setFont(Font.font(16)));
                alert.showAndWait();

                this.addComponentController.getMenuController().backToMenu();
            }
            catch (RuntimeException e)
            {
                alert.setContentText(e.getLocalizedMessage());
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }
        }
        else
        {
            alert.setContentText(ErrorMessage.EMPTY_FIELDS);
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }
}
