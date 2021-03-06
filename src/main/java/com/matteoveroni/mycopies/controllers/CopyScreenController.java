package com.matteoveroni.mycopies.controllers;

import com.matteoveroni.mycopies.copy.CopyTask;
import java.io.File;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * @author Matteo Veroni
 */
//public class CopyScreenController implements ScreenSettable, ScreenControllable, Initializable {
public class CopyScreenController implements Initializable {

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private Button chooseSourcePathButton;
	@FXML
	private Button chooseTargetPathButton;
	@FXML
	private Button startCopyButton;
	@FXML
	private Button backToMainScreenButton;
	@FXML
	private TextField sourcePathTextField;
	@FXML
	private TextField targetPathTextField;
	@FXML
	private TextArea consolleTextArea;
	@FXML
	private ProgressBar percentageBar;
	@FXML
	private Label percentuale;
	@FXML
	private ProgressIndicator percentageGraphic;

//    private Model model;
	private Stage stage;
//    private ScreensController myController;

	File sourceFile;
	File targetFile;
	Path sourcePath;
	Path targetPath;

//    private static final Logger LOG = LoggerFactory.getLogger(CopyScreenController.class);
	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	private enum RadioButtonStates {

		file, directory
	};

	private RadioButtonStates isRadioButtonFileOrDirectory = RadioButtonStates.file;

//    @Override
//    public void setScreensController(ScreensController screensController) {
//        myController = screensController;
//    }
//
//    @Override
//    public void setModel(Model model) {
//        this.model = model;
//    }
//
//    @Override
//    public void setStage(Stage stage) {
//        this.stage = stage;
//    }
	@FXML
	void initialize() {
	}

	@FXML
	void backToMainScreen(ActionEvent event) {
//        myController.setScreen(ScreenResources.MAIN_SCREEN.screenName());
	}

	@FXML
	void radioButtonFile(ActionEvent event) {
		isRadioButtonFileOrDirectory = RadioButtonStates.file;
		consolleTextArea.appendText(" - radioButtonFile: " + isRadioButtonFileOrDirectory + "\n");
//        LOG.info("radioButtonFile: " + isRadioButtonFileOrDirectory);
	}

	@FXML
	void radioButtonDirectory(ActionEvent event) {
		isRadioButtonFileOrDirectory = RadioButtonStates.directory;
		consolleTextArea.appendText(" - radioButtonDirectory: " + isRadioButtonFileOrDirectory + "\n");
//        LOG.info("radioButtonDirectory: " + isRadioButtonFileOrDirectory);
	}

	@FXML
	void chooseSourcePath(ActionEvent event) {
		sourceFile = chooseFileOrDirectory();
		if (sourceFile != null && !sourceFile.toString().trim().equals("")) {
			sourcePathTextField.setText(sourceFile.getAbsolutePath());
			consolleTextArea.appendText(" - sourcePathChoosen: " + sourceFile + "\n");
//            LOG.info("sourcePathChoosen: " + sourcePathChoosen);
		}
	}

	@FXML
	void chooseTargetPath(ActionEvent event) {
		targetFile = chooseFileOrDirectory();
		if (targetFile != null && targetFile.isDirectory() && !targetFile.toString().trim().equals("")) {
			if (!targetFile.getName().equals(sourceFile.getName())) {
				targetFile = FileSystems.getDefault().getPath(targetFile.getAbsolutePath(), sourceFile.getName()).toFile();
			}
			targetPathTextField.setText(targetFile.getAbsolutePath());
			consolleTextArea.appendText(" - targetPathChoosen: " + targetFile + "\n");
//            LOG.info("targetPathChoosen: " + targetPathChoosen);
		}
	}

	private File chooseFileOrDirectory() {
		File pathChoosen = null;
		switch (isRadioButtonFileOrDirectory) {
			case file:
				pathChoosen = chooseFile();
				break;
			case directory:
				pathChoosen = chooseDirectory();
				break;
		}
		return pathChoosen;
	}

	private File chooseFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		return fileChooser.showOpenDialog(stage);
	}

	private File chooseDirectory() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Open Resource File");
		return directoryChooser.showDialog(stage);
	}

	@FXML
	void startCopy(ActionEvent event) {
		startCopyButton.setDisable(true);
		System.out.println("a");

		String sourcePath = sourcePathTextField.getText().trim();
		String targetPath = targetPathTextField.getText().trim();

		try {
//            model.simpleCopy("name", new File(sourcePathChoosen), new File(targetPathChoosen));
			consolleTextArea.appendText(" - startCopy: " + sourcePath + " -> " + targetPath + "\n");
//            LOG.info("startCopy: " + sourcePathChoosen + " -> " + targetPathChoosen);
		} catch (Exception ex) {
			System.out.println("Qui non entra per il momento");
			consolleTextArea.appendText("copy error: " + ex.getMessage() + "\n");
//            LOG.error(ex.getMessage());
		}

		File originalFile = new File(sourcePath);
		File destinationFile = new File(targetPath);

		CopyTask copyTask = new CopyTask(originalFile, destinationFile);

		percentageGraphic.progressProperty().bind(copyTask.getPercentage());
		percentageBar.progressProperty().bind(copyTask.getPercentage());
		copyTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						percentageGraphic.progressProperty().unbind();
						percentageBar.progressProperty().unbind();
						System.out.println("copia finita");
						percentageGraphic.setProgress(100);
						percentageBar.setProgress(100);
						startCopyButton.setDisable(false);
					}
				});
			}
		});
		new Thread(copyTask).start();
		System.out.println("Copy started");
	}
}
