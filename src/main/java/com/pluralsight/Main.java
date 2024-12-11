package com.pluralsight.dealership.db;

import com.pluralsight.dealership.models.Vehicle;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDao {
    private DataSource dataSource;

    public VehicleDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Vehicle> searchByPriceRange(double minPrice, double maxPrice) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE price BETWEEN ? AND ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, minPrice);
            statement.setDouble(2, maxPrice);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                vehicles.add(mapResultSetToVehicle(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public List<Vehicle> searchByMakeModel(String make, String model) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE make = ? AND model = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, make);
            statement.setString(2, model);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                vehicles.add(mapResultSetToVehicle(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    // Additional search methods (by year range, color, mileage range, type) would follow the same pattern

    public void addVehicle(Vehicle vehicle) {
        String query = "INSERT INTO vehicles (vin, make, model, year, is_sold, color, type, mileage, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, vehicle.getVin());
            statement.setString(2, vehicle.getMake());
            statement.setString(3, vehicle.getModel());
            statement.setInt(4, vehicle.getYear());
            statement.setBoolean(5, vehicle.isSold());
            statement.setString(6, vehicle.getColor());
            statement.setString(7, vehicle.getType());
            statement.setInt(8, vehicle.getMileage());
            statement.setDouble(9, vehicle.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeVehicle(String vin) {
        String query = "DELETE FROM vehicles WHERE vin = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, vin);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Vehicle mapResultSetToVehicle(ResultSet resultSet) throws SQLException {
        return new Vehicle(
                resultSet.getString("vin"),
                resultSet.getString("make"),
                resultSet.getString("model"),
                resultSet.getInt("year"),
                resultSet.getBoolean("is_sold"),
                resultSet.getString("color"),
                resultSet.getString("type"),
                resultSet.getInt("mileage"),
                resultSet.getDouble("price")
        );
    }
}
