package com.example.energycalculator;

import java.util.List;

public class Interpolator {
    public static double interpolate(double time, List<Double> timePoints, List<Double> energyUsage) {
        int n = timePoints.size();
        double result = 0;

        for (int i = 0; i < n; i++) {
            double term = energyUsage.get(i);
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term *= (time - timePoints.get(j)) / (timePoints.get(i) - timePoints.get(j));
                }
            }
            result += term;
        }

        return result;
    }
}