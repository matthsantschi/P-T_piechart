/*
	* Project and Training 2: Pie Chart - Computer Science, Berner Fachhochschule
	*/
package ch.bfh.piechart.ui;

import ch.bfh.piechart.model.PieChart;
import ch.bfh.piechart.model.PieChartObserver;
import ch.bfh.piechart.model.PieChartProvider;
import ch.bfh.piechart.model.PieChartSlice;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * Controller for the JavaFX representation of the pie chart.
 */
public class ChartController implements PieChartObserver {

	@FXML
	Pane pane;
	@FXML
	Label errorMsg;

	private PieChart pieChart;

	private CircleSector[] sectors;

	/**
	 * Creates the controller. Gets the pie chart model from the provider class and
	 * creates its visual representation.
	 */
	public ChartController() {

		Platform.runLater(() -> {

			try {
				// TODO Initialize a pie chart provider
				PieChartProvider pieCharProvider = new PieChartProvider();

				// TODO Get the pie chart, assign it to instance variable 'pieChart';
				// Try out with the MockPieChart (the class Matrix must be functioning)
				// pieChart = new MockPieChart();
				pieChart = pieCharProvider.getPieChart();
				// TODO Replace the MockPieChart with your implementation

				// TODO if there is no pie chart then throw an exception
				setPosAndRadius();

				// TODO Register this ChartController instance as an observer at the pie chart
				pieChart.addObserver(this);
				// TODO For each slice in the chart, create a CircleSector instance and add it
				// to the collection of circle sectors. In addition:
				// - register the pieChart.onClick() method as event handler at each circle
				// sector
				// - add the circle sector to the children of the pane

				sectors = new CircleSector[pieChart.getNbOfSlices()];
				for (int i = 0; i < pieChart.getNbOfSlices(); i++) {
					// For each slice in the chart, create a CircleSector instance and add it
					// to the collection of circle sectors.
					sectors[i] = new CircleSector();
					sectors[i].update(pieChart.getSlice(i).getCoords());
					// TODO: register the piechart.onClick() method as event handler at each circle
					// sector
					int sectorNumber = i;
					sectors[i].setOnMouseClicked(e -> pieChart.onClick(sectorNumber));

					// add the circle sector to the children of the pane
					pane.getChildren().add(sectors[i]);
				}

				setPosAndRadius();
			} catch (Exception ex) {
				errorMsg.setText(ex.getMessage());
				ex.printStackTrace();
			}
		});
	}

	/**
	 * Calculate position and radius for the pie chart depending on the window size.
	 */
	void setPosAndRadius() {
		// CHECKSTYLE:OFF MagicNumber
		if (pieChart != null) {
			double x = pane.getWidth() * 0.5;
			double y = pane.getHeight() * 0.5;
			double r = ((x > y) ? y : x) * 0.8;
			// Adjusts the center and the radius of the pie chart
			pieChart.setPosAndRadius(x, y, r);
		}
	}

	/**
	 * Called by the JavaFX framework. Sets listeners to be informed when the window
	 * size changes.
	 */
	public void initialize() {

		ChangeListener<Number> paneSizeListener = (observable, oldValue, newValue) -> {
			setPosAndRadius();
		};

		pane.widthProperty().addListener(paneSizeListener);
		pane.heightProperty().addListener(paneSizeListener);
	}

	/**
	 * Update the visual representation of one or all slices.
	 *
	 * @param slice The slice to be updated or null if all slices need to be
	 *              updated.
	 */
	@Override
	public void update(PieChartSlice slice) {
		// TODO Given the slice of a pie chart then update the corresponding circle
		// sector
		// else if null is given then update all circle sectors.
		if (slice != null) {
			sectors[slice.getId() - 1].update(slice.getCoords());
		} else {
			for (int i = 0; i < pieChart.getNbOfSlices(); i++) {
				sectors[i].update(pieChart.getSlice(i).getCoords());
			}
		}
	}
}
