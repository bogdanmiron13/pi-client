package com.miron.iot.piclient.controller;

import com.miron.iot.piclient.FxBootstrap;
import com.miron.iot.piclient.model.SensorStatus;
import com.miron.iot.piclient.view.ButtonsBackground;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable{

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private FxBootstrap bootstrap;
    private ButtonsBackground buttonsBackground;

    final Stage stage;

    public MainController(final FxBootstrap bootstrap, final Stage stage){
        this.bootstrap = bootstrap;
        this.stage = stage;
    }

    @FXML
    private Button bedroomBtn;
    private boolean bedroomBtnToggle;

    @FXML
    private Label dateLabel;

    @FXML
    private Label clockLabel;

    @FXML
    private Button garageBtn;

    @FXML
    private Button doorBtn;

    @FXML
    private Button lightBtn;

    @FXML
    private Button secureBtn;

    @FXML
    private Button sleepBtn;

    @FXML
    private Button gateBtn;

    @FXML
    private Button extLightBtn;

    @FXML
    private LineChart sensorChart;

    @FXML
    void bedroomBtnAction(ActionEvent event) {

        if(bedroomBtnToggle){
            bedroomBtn.setBackground(buttonsBackground.bedroomOpen());
            bedroomBtnToggle = false;
        } else {
            bedroomBtn.setBackground(buttonsBackground.bedroomClosed());
            bedroomBtnToggle = true;
        }
    }

    @FXML
    void doorBtnAction(ActionEvent event) {
        doorBtn.setStyle("-fx-background-image: url(images/door_open.png);  -fx-background-size: cover;");
    }

    @FXML
    void garageBtnAction(ActionEvent event) {
        LOGGER.info(bootstrap.getSensorStatusCrud().count() + "");
    }

    @FXML
    void lightBtnAction(ActionEvent event) {
    }

    @FXML
    void extlLghtBtnAction(ActionEvent event) {
    }

    @FXML
    void gateBtnAction(ActionEvent event) {
    }

    @FXML
    void secureBtnAction(ActionEvent event) {
    }

    @FXML
    void sleepBtnAction(ActionEvent event) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonsBackground = new ButtonsBackground();
        grafu();
        ceasu();
    }

    private void grafu() {
        final LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");

        final List<SensorStatus> lastFiveMins =
                bootstrap
                        .getSensorStatusCrud()
                        .findByTimeStampBetween(now.minusMinutes(5), now);

        final List<XYChart.Data> allTimestamps =
                lastFiveMins
                        .stream()
                        .map(SensorStatus::getTimeStamp)
                        .sorted()
                        .map(t -> new XYChart.Data(formatter.format(t), Double.valueOf(0.0)))
                        .collect(Collectors.toList());

        final ObservableList<XYChart.Series> allSeries = FXCollections.observableArrayList();
        allSeries.add(new XYChart.Series("Time", FXCollections.observableArrayList(allTimestamps)));

        final Map<String, List<SensorStatus>> splitBySensor =
                lastFiveMins
                    .stream()
                    .collect(Collectors.groupingBy(SensorStatus::getSensorName));

        splitBySensor.forEach((sensor, statusList) -> {
            List<XYChart.Data> data = statusList
                    .stream()
                    .sorted(Comparator.comparing(SensorStatus::getTimeStamp))
                    .map(sensorStatus -> new XYChart.Data(formatter.format(sensorStatus.getTimeStamp()), sensorStatus.getValue()))
                    .collect(Collectors.toList());
            allSeries.add(new XYChart.Series(sensor, FXCollections.observableArrayList(data)));
        });

        sensorChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        sensorChart.setData(allSeries);
    }

    private void ceasu() {
        dateLabel.setText(new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()));
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent actionEvent) {
                                Calendar time = Calendar.getInstance();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                                clockLabel.setText(simpleDateFormat.format(time.getTime()));
                            }
                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

}