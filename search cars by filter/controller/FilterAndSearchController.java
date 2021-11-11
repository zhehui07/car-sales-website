package group5.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import group8.Car;
import group5.service.CarService;
import group8.data.DbDealerStorage;

public class FilterAndSearchController {

	DbDealerStorage carDAO;
	CarService carService = new CarService();

	public FilterAndSearchController() throws ClassNotFoundException, SQLException {
		carDAO = new DbDealerStorage();
	}

	/*
	 * Gets data for all the cars
	 * 
	 * @param dealerID
	 * 
	 * @return list of all cars
	 */
	public ArrayList<Car> getAllCars(String dealerID) throws SQLException {
		List<group8.Car> list = carDAO.getAllCarsByDealerId(dealerID);
		ArrayList<Car> arrlist = new ArrayList(list);
		return arrlist;
	}

	/*
	 * Gets options data of dropdowns
	 * 
	 * @param filtername, completeList of all cars
	 * 
	 * @return vector of options
	 */
	public Vector<String> getValidOption(String filterName, ArrayList<Car> completeList) {
		return carService.getOptionsFromAllCars(filterName, completeList);
	}

	/*
	 * Gets matching records for the searched keyword
	 * 
	 * @param wordTosearch,completeList of all cars
	 * 
	 * @return list of matching records
	 */
	public ArrayList<Car> getSearchedData(String searchKeyWord, ArrayList<Car> completeCarList) {
		return carService.getSearchedData(searchKeyWord, completeCarList);
	}

	/*
	 * Gets matching records for the selected sort options from dropdown
	 * 
	 * @param map of selected options ex- (make,Audi) , completeList of all cars
	 * 
	 * @return list of matching records
	 */
	public ArrayList<Car> getFilteredData(HashMap<String, String> searchParamsMap, ArrayList<Car> completeList) {
		return carService.getFilteredData(searchParamsMap, completeList);
	}

}
