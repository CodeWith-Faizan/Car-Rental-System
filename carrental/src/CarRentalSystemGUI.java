import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;
import java.util.List;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay, boolean isAvailable) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = isAvailable;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public boolean rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days));
            return true;
        } else {
            return false;
        }
    }

    public boolean returnCar(String carId) {
        for (Rental rental : rentals) {
            if (rental.getCar().getCarId().equals(carId)) {
                rental.getCar().returnCar();
                rentals.remove(rental);
                return true;
            }
        }
        return false;
    }

    public List<Car> getAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        for (Car car : cars) {
            if (car.isAvailable()) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    public List<Rental> getRentals() {
        return rentals;
    }
}

public class CarRentalSystemGUI extends Application {

    private CarRentalSystem rentalSystem;

    @Override
    public void start(Stage primaryStage) {
        rentalSystem = new CarRentalSystem();
        rentalSystem.addCar(new Car("C001", "TOYOTA", "CAMRY", 1000.0, true));
        rentalSystem.addCar(new Car("C002", "HONDA", "CIVIC", 1000.0, true));
        rentalSystem.addCar(new Car("C003", "SUZUKI", "CARRY VAN", 1000.0, true));

        VBox layout = new VBox(15);
        layout.setPadding(new javafx.geometry.Insets(20));
        layout.setStyle("-fx-background-color: #f0f0f0; -fx-font-family: 'Arial';");
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setTitle("Car Rental System");
        primaryStage.setScene(scene);

        Label titleLabel = new Label("Car Rental System");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setStyle("-fx-text-fill: #2a2a2a;");

        // Rent Car Section
        VBox rentCarSection = new VBox(10);
        rentCarSection.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-radius: 8; -fx-border-color: #d6d6d6; -fx-border-width: 1px;");
        rentCarSection.setAlignment(Pos.CENTER);

        Label rentCarLabel = new Label("Rent a Car");
        rentCarLabel.setFont(new Font("Arial", 18));
        rentCarLabel.setStyle("-fx-text-fill: #2a2a2a;");

        TextField customerNameField = new TextField();
        customerNameField.setPromptText("Enter your name");
        customerNameField.setStyle("-fx-background-color: #e8e8e8; -fx-border-radius: 5px;");

        TextField rentalDaysField = new TextField();
        rentalDaysField.setPromptText("Enter rental days");
        rentalDaysField.setStyle("-fx-background-color: #e8e8e8; -fx-border-radius: 5px;");

        ComboBox<String> carSelection = new ComboBox<>();
        for (Car car : rentalSystem.getAvailableCars()) {
            carSelection.getItems().add(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
        }
        carSelection.setStyle("-fx-background-color: #e8e8e8; -fx-border-radius: 5px;");

        Button rentButton = new Button("Rent");
        rentButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 12px; -fx-border-radius: 5px;");
        Button returnButton = new Button("Return");
        returnButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 12px; -fx-border-radius: 5px;");

        Label rentMessage = new Label();
        Label rentMessage1 = new Label();
        rentMessage.setStyle("-fx-font-size: 14px;");

        rentButton.setOnAction(e -> {
            String selectedCar = carSelection.getValue();
            String rentalDaysText = rentalDaysField.getText();

            if (selectedCar != null && !rentalDaysText.isEmpty() && customerNameField.getText() != null) {
                try {
                    int rentalDays = Integer.parseInt(rentalDaysText);
                    if (rentalDays <= 0) {
                        throw new NumberFormatException("Invalid days input");
                    }

                    String[] carParts = selectedCar.split(" - ");
                    String carId = carParts[0];
                    Car car = null;

                    for (Car c : rentalSystem.getAvailableCars()) {
                        if (c.getCarId().equals(carId)) {
                            car = c;
                            break;
                        }
                    }

                    if (car != null) {
                        Customer newCustomer = new Customer("CUS" + (rentalSystem.getAvailableCars().size() + 1), customerNameField.getText());
                        rentalSystem.addCustomer(newCustomer);
                        boolean success = rentalSystem.rentCar(car, newCustomer, rentalDays);
                        if (success) {
                            double totalPrice = car.calculatePrice(rentalDays);
                            rentMessage.setText("Car rented successfully! Total price: Rs" + totalPrice + " " + "For" + " " + rentalDays + " " + "Days" );
                            rentMessage.setStyle("-fx-text-fill: green;");
                        } else {
                            rentMessage.setText("Car is not available.");
                            rentMessage.setStyle("-fx-text-fill: red;");
                        }
                    }
                } catch (NumberFormatException ex) {
                    rentMessage.setText("Please enter a valid number of rental days.");
                    rentMessage.setStyle("-fx-text-fill: red;");
                }
            } else {
                rentMessage.setText("Please enter your name, select a car, and input rental days.");
                rentMessage.setStyle("-fx-text-fill: red;");
            }
        });

        rentCarSection.getChildren().addAll(rentCarLabel, customerNameField, rentalDaysField, carSelection, rentButton, rentMessage);

        // Return Car Section
        VBox returnCarSection = new VBox(10);
        returnCarSection.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-radius: 8; -fx-border-color: #d6d6d6; -fx-border-width: 1px;");
        returnCarSection.setAlignment(Pos.CENTER);

        Label returnCarLabel = new Label("Return a Car");
        returnCarLabel.setFont(new Font("Arial", 18));
        returnCarLabel.setStyle("-fx-text-fill: #2a2a2a;");

        TextField returnCarIdField = new TextField();
        returnCarIdField.setPromptText("Enter car ID");
        returnCarIdField.setStyle("-fx-background-color: #e8e8e8; -fx-border-radius: 5px;");

        returnButton.setOnAction(e -> {
            String carId = returnCarIdField.getText();
            if (!carId.isEmpty()) {
                boolean success = rentalSystem.returnCar(carId);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Car Return Confirmation");
                alert.setHeaderText(null);

                if (success) {
                    rentMessage1.setText("Car returned successfully!");
                    rentMessage1.setStyle("-fx-text-fill: green;");
                    alert.setContentText("Car returned successfully!");
                } else {
                    rentMessage1.setText("Invalid car ID or car not rented.");
                    rentMessage1.setStyle("-fx-text-fill: red;");
                    alert.setContentText("Invalid car ID or car not rented.");
                }
                alert.showAndWait();
            } else {
                rentMessage1.setText("Please enter a car ID.");
                rentMessage1.setStyle("-fx-text-fill: red;");
            }
        });

        returnCarSection.getChildren().addAll(returnCarLabel, returnCarIdField, returnButton,rentMessage1);

        // Adding sections to layout
        layout.getChildren().addAll(titleLabel, rentCarSection, returnCarSection);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
