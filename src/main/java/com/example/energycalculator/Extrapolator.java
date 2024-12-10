package com.example.energycalculator;

import java.util.List;

public class Extrapolator {
    public static double extrapolate(double futureTime, List<Double> timePoints, List<Double> energyUsage) {
        int n = timePoints.size();

        // Use a degree 1 polynomial (linear fit) for extrapolation
        int degree = 1;

        // Get the coefficients for a linear fit using the least squares method
        double[][] coefficients = leastSquaresCoefficients(timePoints, energyUsage, degree);

        // Calculate the extrapolated value for the given future time using the linear polynomial coefficients
        double result = 0;
        for (int i = 0; i <= degree; i++) {
            result += coefficients[i][0] * Math.pow(futureTime, i); // Apply the linear polynomial to the future time
        }

        return result;
    }

    private static double[][] leastSquaresCoefficients(List<Double> timePoints, List<Double> energyUsage, int degree) {
        // Create the augmented matrix for the least squares fitting (degree + 1 rows and degree + 2 columns)
        double[][] matrix = new double[degree + 1][degree + 2];

        // Fill the matrix with the sums for the least squares method
        for (int i = 0; i <= degree; i++) {
            for (int j = 0; j <= degree; j++) {
                for (int k = 0; k < timePoints.size(); k++) {
                    matrix[i][j] += Math.pow(timePoints.get(k), i + j);
                }
            }

            for (int k = 0; k < timePoints.size(); k++) {
                matrix[i][degree + 1] += energyUsage.get(k) * Math.pow(timePoints.get(k), i);
            }
        }

        return solveGaussian(matrix);  // Solve the system of equations using Gaussian elimination
    }

    private static double[][] solveGaussian(double[][] matrix) {
        int n = matrix.length;

        // Perform Gaussian elimination
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double ratio = matrix[j][i] / matrix[i][i];
                for (int k = 0; k <= n; k++) {
                    matrix[j][k] -= ratio * matrix[i][k];
                }
            }
        }

        // Back substitution to find the solution
        double[] solution = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            solution[i] = matrix[i][n];
            for (int j = i + 1; j < n; j++) {
                solution[i] -= matrix[i][j] * solution[j];
            }
            solution[i] /= matrix[i][i];
        }

        // Return the solution coefficients as a 2D array
        double[][] result = new double[n][1];
        for (int i = 0; i < n; i++) {
            result[i][0] = solution[i];
        }
        return result;
    }
}
