package schleicher.softwareoneproj;

/**
 * @author William Zapata9Schleicher at WGU
 */

/**
 * RUNTIME ERROR
 *
 * A runtime error I ran into whilst programming the project was an error stating that a string could not be stored as an int in my Add Product page.
 * This was due to me first assuming that the user would input an int in a text box for int-only fields, such as stock, min, or max, when adding a product.
 * However, when I attempted to save user input that was not an int, for example, with code such as "int min = textBox.getText();", I ran into errors.
 * I attempted to fix these errors by using the Integer function to specifically cast the String input into an int, with code such as "int min = Integer.parseInt(textBox.getText());".
 * However, that had its own issues. If the use entered a non-integer input, the program ran into a runtime error and refused to do anything else on the page.
 * Thus, solution that I found to end the runtime errors was to include the code in a try {} catch {} block, where if the user caused a runtime error due to non-integer input,
 * the program would catch that error and a warning alert could be delivered to the user in the catch {} block describing how to alter their input to make the Add Product page work.
 */

/**
 * FUTURE ENHANCEMENT
 *
 * I think the biggest future enhancement one could add to this Inventory Management program is to add permanence to the data after the program is closed.
 * Right now, everything disappears if the program closes, whether due to user input or something like a power outage.
 * If the data was saved into an external source in realtime, regular intervals, or upon application exit, it would be more useful as an actual inventory management system.
 * Perhaps the program could hook into a database, and use SQL commands to make this happen. For instance, the allParts and allProducts lists could be stored on that database
 * and maintain their data between instances of the program.
 *
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * This class is the main class that starts the program.
 * */
public class HelloApplication extends Application {

    /**Starts the window view*/
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader root = new FXMLLoader(HelloApplication.class.getResource("mainView.fxml"));
        Scene scene = new Scene(root.load(), 807, 393);
        stage.setTitle("Inventory Management System - Home");
        stage.setScene(scene);
        stage.show();
    }

    /**Starts the program*/
    public static void main(String[] args) {
        launch();
    }
}