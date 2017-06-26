package com.miron.iot.piclient.controller;

import com.miron.iot.piclient.FxBootstrap;
import com.miron.iot.piclient.http.RelayClient;
import com.miron.iot.piclient.http.RelayStatus;
import com.miron.iot.piclient.model.SensorStatus;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class MainController implements Initializable {

    private static final int SLEEP_DELAY = 10 * 1000;

    private final FxBootstrap bootstrap;

    private final Stage stage;

    private List<RelayButton> relayButtons;
    @FXML
    private Label dateLabel;
    @FXML
    private Label clockLabel;
    @FXML
    private Button gateBtn;
    @FXML
    private Button lightBtn;
    @FXML
    private Button doorBtn;
    @FXML
    private Button garageBtn;
    @FXML
    private LineChart sensorChart;

    public MainController(final FxBootstrap bootstrap, final Stage stage) {
        this.bootstrap = bootstrap;
        this.stage = stage;
    }

    @FXML
    void gateBtnAction() {
        findRelayButton(gateBtn).toggle(bootstrap.getRelayClient());
    }

    @FXML
    void lightBtnAction() {
        findRelayButton(lightBtn).toggle(bootstrap.getRelayClient());
    }

    @FXML
    void doorBtnAction() {
        findRelayButton(doorBtn).toggle(bootstrap.getRelayClient());
    }

    @FXML
    void garageBtnAction() {
        findRelayButton(garageBtn).toggle(bootstrap.getRelayClient());
    }

    @FXML
    void secureBtnAction() {
        final RelayClient relayClient = bootstrap.getRelayClient();
        relayButtons.stream().forEach(relayButton -> relayButton.setOn(relayClient));
    }

    @FXML
    void sleepBtnAction() {
        new Thread( () -> {
            try {
                Thread.sleep(SLEEP_DELAY);
                secureBtnAction();
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupClock();
        loadButtonStates();
        loadLastFiveMinutesOfData();
    }

    private void setupClock() {
        dateLabel.setText(new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()));
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        actionEvent -> {
                            Calendar time = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                            clockLabel.setText(simpleDateFormat.format(time.getTime()));
                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void loadButtonStates() {
        relayButtons = Arrays.asList(
                new RelayButton(0, gateBtn),
                new RelayButton(1, lightBtn),
                new RelayButton(2, doorBtn),
                new RelayButton(3, garageBtn));

        final RelayClient relayClient = bootstrap.getRelayClient();

        relayButtons.stream().forEach(relayButton -> relayButton.refreshState(relayClient));
    }


    private void loadLastFiveMinutesOfData() {
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

    private RelayButton findRelayButton(final Button button) {
        return
                relayButtons.stream()
                        .filter(relayButton -> relayButton.wrapsButton(button))
                        .findFirst()
                        .orElseThrow(RuntimeException::new);
    }

    private static final class RelayButton {

        private final int relayNumber;
        private final Button button;
        private boolean on;

        public RelayButton(final int relayNumber, final Button button) {
            this.relayNumber = relayNumber;
            this.button = button;
        }

        public boolean wrapsButton(final Button button) {
            return this.button == button;
        }

        public void refreshState(final RelayClient relayClient) {
            on = relayClient.getStatus(relayNumber).isOn();
            applyColor();
        }

        public void setOn(final RelayClient relayClient) {
            relayClient.setStatus(relayNumber, new RelayStatus(relayNumber, true));
            on = true;
            applyColor();
        }

        public void toggle(final RelayClient relayClient) {
            relayClient.setStatus(relayNumber, new RelayStatus(relayNumber, !on));
            on = !on;
            applyColor();
        }

        private void applyColor() {
            if (on) {
                button.setStyle("-fx-background-color: greenyellow");
            } else {
                button.setStyle("-fx-background-color: indianred");
            }
        }

    }

}