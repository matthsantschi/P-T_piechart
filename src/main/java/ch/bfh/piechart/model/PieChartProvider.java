/*
 * Project and Training 2: Pie Chart - Computer Science, Berner Fachhochschule
 */
package ch.bfh.piechart.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import ch.bfh.piechart.datalayer.ConnectionManager;
import ch.bfh.piechart.datalayer.SalesValue;
import ch.bfh.piechart.datalayer.SalesValueRepository;

// TODO Complete import statements

/**
 * Service class providing sales value information as a pie chart. Upon loading
 * this class, then 1) all sales values are loaded from the database, 2) there
 * relative percentage values are computed, and 3) the updated sales value
 * objects are persisted.
 */
public class PieChartProvider {

	private static Connection connection;
	private static SalesValueRepository salesValueRepository;

	/**
	 * Loads all sales values, computes there relative percentage values, and stores
	 * the updated sales values in the database.
	 */
	// CHECKSTYLE:OFF EmptyBlock
	static {
		/*
		 * 1. It makes a connection to the database using the ConnectionManager;
		 * 2. It reads all sales values available;
		 * 3. It computes the relative percentage for the sales values;
		 * 4. It updates the sales values in the database.
		 * Note: If any kind of exception is thrown in the above 4 steps then
		 * catch it in a RuntimeExpetion and throw it from within the static block.
		 */
		connection = ConnectionManager.getConnection(true);

		salesValueRepository = new SalesValueRepository(connection);
		try {
			List<SalesValue> list = salesValueRepository.findAll();
			double sum = 0;
			for (SalesValue sv : list) {
				sum += sv.getValue();
			}
			// CHECKSTYLE:OFF MagicNumber
			for (SalesValue sv : list) {
				sv.setPercentage((sv.getValue() / sum) * 100);
				salesValueRepository.update(sv);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Problem while reding/updating salesValues");
		}

	}

	/**
	 * Creates a pie chart based on the list of sales value tuples.
	 *
	 * @return a pie chart
	 * @throws Exception if sales values cannot be obtained
	 */
	public PieChart getPieChart() throws Exception {
		List<SalesValue> list;
		try {
			list = getPieChartSalesValues();
			if (list.size() == 0) {
				throw new Exception("Sales Values can not be obtained");
			}
		} catch (SQLException e) {
			throw new Exception("Sales Values can not be obtained");
		}
		return new ConcretePieChart(list);
	}

	/**
	 * Returns the sales values for this pie chart repository. That is, the
	 * corresponding pie chart is based on the list of these sales values.
	 *
	 * @return a list of sales values the corresponding pie chart is based on
	 * @throws Exception if sales values cannot be obtained
	 */
	public List<SalesValue> getPieChartSalesValues() throws Exception {
		List<SalesValue> list = salesValueRepository.findAll();
		if (list.size() == 0) {
			throw new UnsupportedOperationException();
		}
		return list;
	}
}
