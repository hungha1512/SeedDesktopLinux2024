module org.uet.rislab.seed.applicationlinux {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires javafx.swing;

    opens org.uet.rislab.seed.applicationlinux.controller to javafx.fxml;
    exports org.uet.rislab.seed.applicationlinux;
}