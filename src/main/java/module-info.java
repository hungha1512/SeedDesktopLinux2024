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
    opens org.uet.rislab.seed.applicationlinux.controller.homepage to javafx.fxml;
    opens org.uet.rislab.seed.applicationlinux.controller.analyze to javafx.fxml;
    opens org.uet.rislab.seed.applicationlinux.controller.camera to javafx.fxml;
    opens org.uet.rislab.seed.applicationlinux.controller.project to javafx.fxml;
    opens org.uet.rislab.seed.applicationlinux.controller.result to javafx.fxml;

    opens org.uet.rislab.seed.applicationlinux.model.entity to javafx.base;
}