/*
 * Project and Training 2: Pie Chart - Computer Science, Berner Fachhochschule
 */
package ch.bfh.piechart.model;

import java.util.ArrayList;
import java.util.List;

import ch.bfh.piechart.datalayer.SalesValue;

/**
 * Represents a pie chart.
 */
class ConcretePieChart implements PieChart {

	// ------------------------------
	// TODO Provide pie chart slice as inner class
	// ------------------------------

	/**
	 * Creates a pie chart based on percentages in the list of SalesValue objects.
	 *
	 * @param values sales value objects
	 */
	ConcretePieChart(List<SalesValue> values) {
		// TODO: implement
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the number of slices in this pie chart.
	 *
	 * @return number of slices
	 */
	@Override
	public int getNbOfSlices() {
		// TODO: implement
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the indexed slice of the pie chart.
	 *
	 * @param index slice index
	 * @return slice object
	 */
	@Override
	public PieChartSlice getSlice(int index) {
		// TODO: implement
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the center position and the radius for the chart.
	 *
	 * @param x the x-value of the center position
	 * @param y the y-value of the center position
	 * @param r the radius for the chart
	 */
	@Override
	public void setPosAndRadius(double x, double y, double r) {
		// TODO: implement
		// Note: Do not forget to notify observers
		throw new UnsupportedOperationException();
	}

	// ---------------------------------
	// implement observable pattern

	private final List<PieChartObserver> observers = new ArrayList<>();

	/**
	 * To be called when observers need to be notified that one or all slices need
	 * to be updated.
	 *
	 * @param slice the slice to be updated or null if all slices need to be updated
	 */
	@SuppressWarnings("unused")
	private void notifyObservers(PieChartSlice slice) {
		observers.forEach(observer -> observer.update(slice));
	}

	/**
	 * Adds an observer to the list of observers.
	 *
	 * @param observer reference to the observer.
	 */
	@Override
	public void addObserver(PieChartObserver observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	/**
	 *
	 * @param index the index of the slice clicked
	 */
	@Override
	public void onClick(int index) {
		PieChartSlice slice = getSlice(index);
		slice.move();
		// TODO Do not forget to notify observers
	}
}
