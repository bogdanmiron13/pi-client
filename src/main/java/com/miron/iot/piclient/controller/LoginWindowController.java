package com.miron.iot.piclient.controller;

import com.miron.iot.piclient.FxBootstrap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginWindowController implements Initializable{

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginWindowController.class);

    private FxBootstrap bootstrap;

    private final Stage stage;

    public LoginWindowController(final FxBootstrap bootstrap, final Stage stage){
        this.bootstrap = bootstrap;
        this.stage = stage;
    }

    @FXML
    private TextField userIdText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Label errorLabel;

    @FXML
    void loginBtnAction(final ActionEvent event) {
        if(true){
            try {
                stage.setScene(bootstrap.getMainScene(stage));
                stage.setResizable(true);
            } catch (final Exception e){
                LOGGER.error(e.getMessage(), e);
            }
        }else{
            errorLabel.setText("Invalid credentials!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}