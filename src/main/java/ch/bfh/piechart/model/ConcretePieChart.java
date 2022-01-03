/*
 * Project and Training 2: Pie Chart - Computer Science, Berner Fachhochschule
 */
package ch.bfh.piechart.model;

import java.util.ArrayList;
import java.util.List;

import ch.bfh.matrix.GraphicOps;
import ch.bfh.matrix.Matrix;
import ch.bfh.piechart.datalayer.SalesValue;

/**
 * Represents a pie chart.
 */
public class ConcretePieChart implements PieChart {

	// position arguments
	private double x;
	private double y;
	private double radius;

	private final List<Slice> slices = new ArrayList<>();

	ConcretePieChart() {
	}

	/**
	 * Creates a pie chart based on percentages in the list of SalesValue objects.
	 *
	 * @param values sales value objects
	 */
	ConcretePieChart(List<SalesValue> values) {
		double startAngle = 2.0 * Math.PI;
		// CHECKSTYLE:OFF MagicNumber
		for (SalesValue salesValue : values) {
			double endAngle = startAngle - salesValue.getPercentage() / 100 * 2.0 * Math.PI;
			slices.add(new Slice(startAngle, endAngle, this, salesValue.getId()));
			startAngle = endAngle;
		}
	}

	/**
	 * Returns the number of slices in this pie chart.
	 *
	 * @return number of slices
	 */
	@Override
	public int getNbOfSlices() {
		return this.slices.size();
	}

	/**
	 * Returns the indexed slice of the pie chart.
	 *
	 * @param index slice index
	 * @return slice object
	 */
	@Override
	public PieChartSlice getSlice(int index) {
		return this.slices.get(index);
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
		this.x = x;
		this.y = y;
		this.radius = r;
		for (Slice slice : this.slices) {
			notifyObservers(slice);
		}
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
		this.notifyObservers(slice);
	}

	public static class Slice implements PieChartSlice {

		private Matrix coords;
		private ConcretePieChart pieChart;
		private Matrix start;
		private Matrix end;
		private Matrix detachMatrix;
		private int id;
		private boolean revert = false;

		public Slice(double startAngle, double endAngle, ConcretePieChart pieChart, int id) {
			start = GraphicOps.rotate(GraphicOps.UNIT_Y_VECTOR, startAngle);
			end = GraphicOps.rotate(GraphicOps.UNIT_Y_VECTOR, endAngle);
			coords = new Matrix(new double[][] {
					{ 0.0, start.get(0, 0), end.get(0, 0) },
					{ 0.0, start.get(1, 0), end.get(1, 0) },
					{ 1.0, 1.0, 1.0 } });
			this.pieChart = pieChart;
			this.detachMatrix = GraphicOps.rotate(
					ConcretePieChart.DETACH_VECTOR,
					(startAngle + (endAngle - startAngle) / 2));
			this.id = id;
		}

		@Override
		public Matrix getCoords() {
			// with fixed screen dimensions
			Matrix scale = GraphicOps.scale(this.coords, pieChart.radius);
			return GraphicOps.translate(scale, pieChart.x, pieChart.y);
		}

		@Override
		public void move() {
			if (this.revert) {
				this.coords = GraphicOps.translate(this.coords, this.detachMatrix);
				// rotate the detach matrix 180%
				this.detachMatrix = GraphicOps.rotate(this.detachMatrix, Math.PI);
				this.revert = false;
			} else {
				this.coords = GraphicOps.translate(this.coords, this.detachMatrix);
				// rotate the detach matrix 180%
				this.detachMatrix = GraphicOps.rotate(this.detachMatrix, Math.PI);
				this.revert = true;
			}

		}

		@Override
		public int getId() {
			return this.id;
		}
	}
}
