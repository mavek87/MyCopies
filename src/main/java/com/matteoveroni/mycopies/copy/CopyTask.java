package com.matteoveroni.mycopies.copy;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import org.apache.commons.io.FileUtils;

public class CopyTask extends Task<Void> {

	private final File origin;
	private final File destination;
	
	private ProgressIndicator percentage = new ProgressIndicator();

//	private final StringProperty copyPercentageCompletatation = new SimpleStringProperty();

	public CopyTask(File origin, File destination) {
		this.origin = origin;
		this.destination = destination;
	}

	@Override
	protected Void call() {
		try {
			createAndLaunchCopyPercentageChekerTask();
			FileUtils.copyDirectory(origin, destination, false);
		} catch (IOException ex) {
			System.out.println("Copy Exception " + ex);
		}
		return null;
	}

//	public StringProperty getCopyPercentageCompletatation() {
//		return copyPercentageCompletatation;
//	}

	private void createAndLaunchCopyPercentageChekerTask() {
		System.out.println("1");
		CopyPercentageCheckerTask copyPercentageCheckerTask = new CopyPercentageCheckerTask(origin, destination);
		System.out.println("2");

		percentage.progressProperty().bind(copyPercentageCheckerTask.progressProperty());
		System.out.println("3");

		copyPercentageCheckerTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						System.out.println("4");
						percentage.progressProperty().unbind();
					}
				});
			}
		}
		);
		System.out.println("5");
		new Thread(copyPercentageCheckerTask).start();
		System.out.println("6");

	}
	
	public DoubleProperty getPercentage(){
		return percentage.progressProperty();
	}

}
