package com.matteoveroni.mycopies.copy;

import java.io.File;
import javafx.concurrent.Task;
import org.apache.commons.io.FileUtils;

public class CopyPercentageCheckerTask extends Task<Void> {

	private final File origin;
	private final File destinationToCheck;

	private final long sizeOfOrigin;

	private boolean copyCompletationCheckFlag;

	public CopyPercentageCheckerTask(File origin, File destinationToCheck) {
		this.origin = origin;
		this.destinationToCheck = destinationToCheck;
		sizeOfOrigin = FileUtils.sizeOfDirectory(origin);
	}

	@Override
	protected Void call() throws Exception {
//		updateMessage("0.0%");
		updateProgress(0, sizeOfOrigin);
		setCopyCompleted(false);
		while (!isCopyCompleted()) {
//			try {
//				Thread.sleep(1000);
//				double copyCompletationPercentage = calculateCopyCompletationPercentage();
			long sizeOfDestination = FileUtils.sizeOfDirectory(destinationToCheck);
			updateProgress(sizeOfDestination, sizeOfOrigin);
//				updateMessage(String.format("%.1f", copyCompletationPercentage) + "%");
//			} catch (InterruptedException ex) {
//			}
		}
//		updateMessage("100.0%");
		updateProgress(sizeOfOrigin, sizeOfOrigin);

		return null;
	}

	private double calculateCopyCompletationPercentage() {
		double copyCompletationPercentage;
		long sizeOfDestination = FileUtils.sizeOfDirectory(destinationToCheck);
		try {
			copyCompletationPercentage = (((double) sizeOfDestination / (double) sizeOfOrigin) * 100);
		} catch (ArithmeticException ex) {
			copyCompletationPercentage = 100;
		}
		return copyCompletationPercentage;
	}

	private boolean isCopyCompleted() {
		if (FileUtils.sizeOfDirectory(origin) == FileUtils.sizeOfDirectory(destinationToCheck)) {
			setCopyCompleted(true);
		}
		return copyCompletationCheckFlag;
	}

	private void setCopyCompleted(boolean isCompleted) {
		copyCompletationCheckFlag = isCompleted;
	}

}
