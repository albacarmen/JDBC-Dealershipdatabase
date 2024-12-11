package com.pluralsight.dealership.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InventoryDao {
    private DataSource dataSource;

    public InventoryDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addVehicleToInventory(String vin, int dealershipId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO inventory (VIN, dealership_id) " +
                             "values(?,?) "
             )) {
            preparedStatement.setString(1, vin);
            preparedStatement.setInt(2, dealershipId);

            int rows = preparedStatement.executeUpdate();
            System.out.println("Rows were affected " + rows);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeVehicleFromInventory(String vin) {
        try( Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM inventory WHERE VIN = ?"
             )) {
            preparedStatement.setString(1, vin);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}