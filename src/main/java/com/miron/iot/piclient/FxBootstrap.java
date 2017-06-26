package com.miron.iot.piclient;

import com.miron.iot.piclient.controller.LoginWindowController;
import com.miron.iot.piclient.controller.MainController;
import com.miron.iot.piclient.crud.SensorStatusCrud;
import com.miron.iot.piclient.http.RelayClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class FxBootstrap extends Application {

    protected static void startup() {
        new Thread(() -> launch(FxBootstrap.class, StaticBridge.ARGS)).start();
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setScene(getLoginScene(primaryStage));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }

    private Scene getLoginScene(final Stage primaryStage) throws Exception {
        final FXMLLoader loginLoader = new FXMLLoader(getResource("/fxml/LoginWindow.fxml"));
        loginLoader.setController(new LoginWindowController(this, primaryStage));

        final Parent loginParent = loginLoader.load();
        final Scene loginScene = new Scene(loginParent);
        Application.setUserAgentStylesheet(STYLESHEET_MODENA);
        //loginScene.getStylesheets().add(getResource("/css/style.css").toExternalForm());
        return loginScene;
    }

    public Scene getMainScene(final Stage primaryStage) throws Exception {
        final FXMLLoader mainLoader = new FXMLLoader(getResource("/fxml/MainWindow.fxml"));
        mainLoader.setController(new MainController(this, primaryStage));

        final Parent mainParent = mainLoader.load();
        final Scene mainScene = new Scene(mainParent);
        Application.setUserAgentStylesheet(STYLESHEET_MODENA);
   //     mainScene.getStylesheets().add(getResource("/css/style.css").toExternalForm());
        return mainScene;
    }

    private URL getResource(final String path) {
        return getClass().getResource(path);
    }

    public SensorStatusCrud getSensorStatusCrud() {
        return StaticBridge.APP_CONTEXT.getBean(SensorStatusCrud.class);
    }

    public RelayClient getRelayClient() {
        return StaticBridge.APP_CONTEXT.getBean(RelayClient.class);
    }
}