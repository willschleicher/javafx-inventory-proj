module schleicher.softwareoneproj {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens schleicher.softwareoneproj to javafx.fxml;
    exports schleicher.softwareoneproj;
}