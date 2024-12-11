package com.pluralsight.dealership.db;

import com.pluralsight.dealership.models.SalesContract;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesDao {
    private DataSource dataSource;

    public SalesDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Adds a new sales contract to the database.
     */
    public void addSalesContract(SalesContract salesContract) {
        String sql = "INSERT INTO sales_contracts (vin, sale_date, price) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, salesContract.getVin());
            preparedStatement.setDate(2, Date.valueOf(salesContract.getSaleDate()));
            preparedStatement.setDouble(3, salesContract.getPrice());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Sales contract added successfully.");
            } else {
                System.out.println("No records were added.");
            }
        } catch (SQLException e) {
            System.err.println("Error while adding sales contract: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Finds a sales contract by its VIN.
     *
     */
    public SalesContract findByVin(String vin) {
        String sql = "SELECT * FROM sales_contracts WHERE vin = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, vin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Create a new SalesContract object
                Date saleDate = resultSet.getDate("sale_date");
                double price = resultSet.getDouble("price");
                return new SalesContract(vin, saleDate.toLocalDate(), price);
            } else {
                System.out.println("Sales contract not found for VIN: " + vin);
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving sales contract: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
