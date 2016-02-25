package com.matteoveroni.mycopies;

import com.matteoveroni.mycopies.copy.CopyPercentageCheckerThread;
import com.matteoveroni.mycopies.copy.CopyPercentagePrinter;
import com.matteoveroni.mycopies.system.events.bus.SystemEventBus;
import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

public class MainApp extends Application {

    private static final String ORIGINAL_FILE_PATH = "/media/mavek/DATA/Uninettuno";
    private static final String DESTINATION_FILE_PATH = "/home/mavek/cartella";

    @Override
    public void start(Stage stage) throws Exception {

        File originalFile = new File(ORIGINAL_FILE_PATH);
        File destinationFile = new File(DESTINATION_FILE_PATH);

        SystemEventBus systemEventBus = new SystemEventBus();

        CopyPercentagePrinter cpPrinter = new CopyPercentagePrinter();
        systemEventBus.addEventListener(cpPrinter);
        
        Thread copyCheckerThread = new Thread(new CopyPercentageCheckerThread(systemEventBus, originalFile, destinationFile));

        copyCheckerThread.start();
        FileUtils.copyDirectory(originalFile, destinationFile, false);

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
