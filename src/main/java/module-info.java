module dk.sea.mytunes {
    requires javafx.controls;
    requires javafx.fxml;


    opens dk.sea.mytunes to javafx.fxml;
    exports dk.sea.mytunes;
}