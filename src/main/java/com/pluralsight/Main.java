package com.yearup.dealership.Main;

import com.pluralsight.dealership.db.InventoryDao;
import com.pluralsight.dealership.db.LeaseDao;
import com.pluralsight.dealership.db.SalesDao;
import com.pluralsight.dealership.db.VehicleDao;
import com.pluralsight.dealership.models.LeaseContract;
import com.pluralsight.dealership.models.SalesContract;
import com.pluralsight.dealership.models.Vehicle;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        String username = args[0];
        String password = args[1];


        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/car_dealership");
        dataSource.setUsername(username);
        dataSource.setPassword(password);


        VehicleDao vehicleDao = new VehicleDao(dataSource);
        InventoryDao inventoryDao = new InventoryDao(dataSource);
        SalesDao salesDao = new SalesDao(dataSource);
        LeaseDao leaseDao = new LeaseDao(dataSource);


        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        // Main menu loop
        while (!exit) {
            System.out.println("Main Menu:");
            System.out.println("1. Search vehicles");
            System.out.println("2. Add a vehicle");
            System.out.println("3. Add a contract");
            System.out.println("4. Remove a vehicle");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    searchVehiclesMenu(vehicleDao, scanner);
                    break;
                case 2:
                    addVehicleMenu(vehicleDao, inventoryDao, scanner);
                    break;
                case 3:
                    addContractMenu(salesDao, leaseDao, scanner);
                    break;
                case 4:
                    removeVehicleMenu(vehicleDao, inventoryDao, scanner);
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

        scanner.close();
    }

    // Menu for adding contracts
    private static void addContractMenu(SalesDao salesDao, LeaseDao leaseDao, Scanner scanner) {
        System.out.print("Enter the VIN of the vehicle: ");
        String vin = scanner.nextLine();

        System.out.println("\nChoose contract type:");
        System.out.println("1. Sales Contract");
        System.out.println("2. Lease Contract");
        System.out.print("Enter your choice: ");
        int contractType = scanner.nextInt();
        scanner.nextLine();

        switch (contractType) {
            case 1:
                addSalesContract(salesDao, vin, scanner);
                break;
            case 2:
                addLeaseContract(leaseDao, vin, scanner);
                break;
            default:
                System.out.println("Invalid choice. Contract not added.");
                break;
        }
    }

    // Method for adding a sales contract
    private static void addSalesContract(SalesDao salesDao, String vin, Scanner scanner) {
        System.out.print("Enter sale date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate saleDate = LocalDate.parse(dateStr);

        System.out.print("Enter the price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        SalesContract contract = new SalesContract(vin, saleDate, price);
        salesDao.addSalesContract(contract);
        System.out.println("Sales contract added successfully.");
    }

    // Method for adding a lease contract
    private static void addLeaseContract(LeaseDao leaseDao, String vin, Scanner scanner) {
        System.out.print("Enter lease start date (YYYY-MM-DD): ");
        String startDateStr = scanner.nextLine();
        LocalDate startDate = LocalDate.parse(startDateStr);

        System.out.print("Enter lease end date (YYYY-MM-DD): ");
        String endDateStr = scanner.nextLine();
        LocalDate endDate = LocalDate.parse(endDateStr);

        System.out.print("Enter monthly payment: ");
        double monthlyPayment = scanner.nextDouble();
        scanner.nextLine();

        LeaseContract contract = new LeaseContract(vin, startDate, endDate, monthlyPayment);
        leaseDao.addLeaseContract(contract);
        System.out.println("Lease contract added successfully.");
    }

    // Menu for searching vehicles
    private static void searchVehiclesMenu(VehicleDao vehicleDao, Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\nSearch Vehicles:");
            System.out.println("1. By price range");
            System.out.println("2. By make/model");
            System.out.println("3. By year range");
            System.out.println("4. By color");
            System.out.println("5. By mileage range");
            System.out.println("6. By type");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    searchByPriceRange(vehicleDao, scanner);
                    break;
                case 2:
                    searchByMakeAndModel(vehicleDao, scanner);
                    break;
                case 3:
                    searchByYearRange(vehicleDao, scanner);
                    break;
                case 4:
                    searchByColor(vehicleDao, scanner);
                    break;
                case 5:
                    searchByMileageRange(vehicleDao, scanner);
                    break;
                case 6:
                    searchByType(vehicleDao, scanner);
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    // Helper methods for vehicle searches
    private static void searchByPriceRange(VehicleDao vehicleDao, Scanner scanner) {
        System.out.print("Enter minimum price: ");
        double minPrice = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter maximum price: ");
        double maxPrice = scanner.nextDouble();
        scanner.nextLine();

        List<Vehicle> vehicles = vehicleDao.searchByPriceRange(minPrice, maxPrice);
        displaySearchResults(vehicles);
    }

    private static void searchByMakeAndModel(VehicleDao vehicleDao, Scanner scanner) {
        System.out.print("Enter make: ");
        String make = scanner.nextLine();

        System.out.print("Enter model: ");
        String model = scanner.nextLine();

        List<Vehicle> vehicles = vehicleDao.searchByMakeModel(make, model);
        displaySearchResults(vehicles);
    }

    private static void searchByYearRange(VehicleDao vehicleDao, Scanner scanner) {
        System.out.print("Enter minimum year: ");
        int minYear = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter maximum year: ");
        int maxYear = scanner.nextInt();
        scanner.nextLine();

        List<Vehicle> vehicles = vehicleDao.searchByYearRange(minYear, maxYear);
        displaySearchResults(vehicles);
    }

    private static void searchByColor(VehicleDao vehicleDao, Scanner scanner) {
        System.out.print("Enter color: ");
        String color = scanner.nextLine();

        List<Vehicle> vehicles = vehicleDao.searchByColor(color);
        displaySearchResults(vehicles);
    }

    private static void searchByMileageRange(VehicleDao vehicleDao, Scanner scanner) {
        System.out.print("Enter minimum mileage: ");
        int minMileage = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter maximum mileage: ");
        int maxMileage = scanner.nextInt();
        scanner.nextLine();

        List<Vehicle> vehicles = vehicleDao.searchByMileageRange(minMileage, maxMileage);
        displaySearchResults(vehicles);
    }

    private static void searchByType(VehicleDao vehicleDao, Scanner scanner) {
        System.out.print("Enter vehicle type: ");
        String type = scanner.nextLine();

        List<Vehicle> vehicles = vehicleDao.searchByType(type);
        displaySearchResults(vehicles);
    }


    private static void displaySearchResults(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
        } else {
            System.out.println("\nSearch Results:");
            for (Vehicle vehicle : vehicles) {
                System.out.println(vehicle);
            }
        }
    }

    // ADD NEW VEHICLE
    private static void addVehicleMenu(VehicleDao vehicleDao, InventoryDao inventoryDao, Scanner scanner) {
        String vin = generateRandomVin();

        System.out.print("Enter make: ");
        String make = scanner.nextLine();

        System.out.print("Enter model: ");
        String model = scanner.nextLine();

        System.out.print("Enter year: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter color: ");
        String color = scanner.nextLine();

        System.out.print("Enter mileage: ");
        int mileage = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter type: ");
        String type = scanner.nextLine();

        System.out.print("Enter dealership ID: ");
        int dealershipId = scanner.nextInt();
        scanner.nextLine();

        Vehicle vehicle = new Vehicle(vin, make, model, year, false, color, type, mileage, price);
        vehicleDao.addVehicle(vehicle);
        inventoryDao.addVehicleToInventory(vin, dealershipId);

        System.out.println("Vehicle added successfully.");
    }

    // Mremoving a vehicle
    private static void removeVehicleMenu(VehicleDao vehicleDao, InventoryDao inventoryDao, Scanner scanner) {
        System.out.print("Enter VIN of the vehicle to remove (must not be sold or leased): ");
        String vin = scanner.nextLine();

        inventoryDao.removeVehicleFromInventory(vin);
        vehicleDao.removeVehicle(vin);
        System.out.println("Vehicle removed successfully.");
    }

    // generating a random VIN
    public static String generateRandomVin() {
        UUID uuid = UUID.randomUUID();
        String vin = uuid.toString().toUpperCase().replaceAll("-", "").substring(0, 17);
        return vin;
    }
}

