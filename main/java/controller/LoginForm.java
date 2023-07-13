package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class LoginForm {
    public AnchorPane loginpane;
    public JFXTextField txtUserName;
    private static String username;

    public void btnJoinOnAction(ActionEvent event) {
       username = txtUserName.getText();
        try {
            loadClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadClient() throws IOException {
        URL resource = getClass().getResource("/view/client_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loginpane.getChildren().clear();
        loginpane.getChildren().add(load);
    }

    public static String getUserName(){
        return username;
    }
}
