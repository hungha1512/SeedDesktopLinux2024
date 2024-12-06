module org.uet.rislab.seed.applicationlinux {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires javafx.swing;
    requires com.sun.jna;

    opens org.uet.rislab.seed.applicationlinux.controller to javafx.fxml;
    exports org.uet.rislab.seed.applicationlinux;
}