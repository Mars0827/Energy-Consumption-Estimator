package com.example.energycalculator;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class EnergyEstimatorController {

    @FXML
    private LineChart<Number, Number> energyChart;

    @FXML
    private TextField timeField, usageField, interpolateField, extrapolateField;

    @FXML
    private Button addButton, interpolateButton, extrapolateButton, resetButton;

    @FXML
    private TextArea outputArea;



    private List<Double> timePoints;
    private List<Double> energyUsage;
    private XYChart.Series<Number, Number> historicalDataSeries;
    private XYChart.Series<Number, Number> predictedDataSeries;

    public EnergyEstimatorController() {
        this.timePoints = new ArrayList<>();
        this.energyUsage = new ArrayList<>();
        this.historicalDataSeries = new XYChart.Series<>();
        this.historicalDataSeries.setName("Historical Data");
        this.predictedDataSeries = new XYChart.Series<>();
        this.predictedDataSeries.setName("Predicted Data");
    }

    @FXML
    public void initialize() {
        // Add the series to the chart
        energyChart.getData().add(historicalDataSeries);
        energyChart.getData().add(predictedDataSeries);

        // add time and usage
        addButton.setOnAction(e -> {
            try {
                double time = Double.parseDouble(timeField.getText());
                double usage = Double.parseDouble(usageField.getText());
                timePoints.add(time);
                energyUsage.add(usage);
                historicalDataSeries.getData().add(new XYChart.Data<>(time, usage));  // Update historical data series
                outputArea.appendText("Added Data Point - Time: " + time + ", Usage: " + usage + "\n");
                timeField.clear();
                usageField.clear();
            } catch (NumberFormatException ex) {
                showAlert("Invalid input! Please enter numeric values.");
            }
        });

        // Interpolation part
        interpolateButton.setOnAction(e -> {
            try {
                double timeToInterpolate = Double.parseDouble(interpolateField.getText());
                double interpolatedValue = Interpolator.interpolate(timeToInterpolate, timePoints, energyUsage);
                predictedDataSeries.getData().add(new XYChart.Data<>(timeToInterpolate, interpolatedValue));  // Update predicted data series
                outputArea.appendText("Interpolated Value at Time " + timeToInterpolate + ": " + interpolatedValue + "\n");
            } catch (NumberFormatException ex) {
                showAlert("Invalid input! Please enter a numeric value.");
            } catch (Exception ex) {
                showAlert("Error in interpolation: " + ex.getMessage());
            }
        });

        // Extrapolation part
        extrapolateButton.setOnAction(e -> {
            try {
                double futureTime = Double.parseDouble(extrapolateField.getText());
                double extrapolatedValue = Extrapolator.extrapolate(futureTime, timePoints, energyUsage);
                predictedDataSeries.getData().add(new XYChart.Data<>(futureTime, extrapolatedValue));  // Update predicted data series
                outputArea.appendText("Extrapolated Value at Time " + futureTime + ": " + extrapolatedValue + "\n");
            } catch (NumberFormatException ex) {
                showAlert("Invalid input! Please enter a numeric value.");
            } catch (Exception ex) {
                showAlert("Error in extrapolation: " + ex.getMessage());
            }
        });

        resetButton.setOnAction(e -> {
            resetData();
        });
    }

    //Para mo show og alert if naa error
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //For reseting the data
    private void resetData() {
        // Clear the input fields
        timeField.clear();
        usageField.clear();
        interpolateField.clear();
        extrapolateField.clear();

        // Clear the output area
        outputArea.clear();

        // Clear the data lists
        timePoints.clear();
        energyUsage.clear();

        // Clear the chart data
        historicalDataSeries.getData().clear();
        predictedDataSeries.getData().clear();
    }
}
