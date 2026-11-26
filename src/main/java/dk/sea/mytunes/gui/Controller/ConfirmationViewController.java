package dk.sea.mytunes.gui.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ConfirmationViewController {
    @FXML
    private Label lblHeading;
    @FXML
    private Label lblItemToDelete;

    private boolean isConfirm = false;

    public void onCancelClick(ActionEvent actionEvent) {
        closeWindow(actionEvent);
    }

    public void onOkClick(ActionEvent actionEvent) {
        isConfirm = true;
        closeWindow(actionEvent);
    }

    public boolean getIsConfirm(){
        return isConfirm;
    }

    private void closeWindow(ActionEvent actionEvent){
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void setContext(String itemtoDelete){
        lblItemToDelete.setText(itemtoDelete);
    }
}
