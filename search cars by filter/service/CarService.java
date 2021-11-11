package group5.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.stream.Collectors;

import group8.Car;

public class CarService {

	/*
	 * Gets the option values for each dropdown from the complete car list
	 * 
	 * @param name of the filter, complete list of all cars
	 * 
	 * @return dropdown option values for each filter
	 */
	public Vector<String> getOptionsFromAllCars(String filterName, ArrayList<Car> list) {

		if (filterName.equalsIgnoreCase("make")) {
			return new Vector<String>(list.stream().map(vehicle -> vehicle.getMake()).collect(Collectors.toSet()));
		}

		if (filterName.equalsIgnoreCase("model")) {
			return new Vector<String>(list.stream().map(vehicle -> vehicle.getModel()).collect(Collectors.toSet()));
		}

		if (filterName.equalsIgnoreCase("category")) {
			return new Vector<String>(
					list.stream().map(vehicle -> vehicle.getCarCategory().toString()).collect(Collectors.toSet()));
		}

		if (filterName.equalsIgnoreCase("color")) {
			return new Vector<String>(list.stream().map(vehicle -> vehicle.getColor()).collect(Collectors.toSet()));
		}
		if (filterName.equalsIgnoreCase("type")) {
			return new Vector<String>(list.stream().map(vehicle -> vehicle.getType()).collect(Collectors.toSet()));
		}
		return null;
	}

	/*
	 * Performs the search on complete car list for the word to be searched
	 * 
	 * @param nword to be searched, complete list of all cars
	 * 
	 * @return list of matching records for the searched word
	 */
	public ArrayList<Car> getSearchedData(String searchKeyWord, ArrayList<Car> completeCarList) {

		ArrayList<Car> resultList = (ArrayList<Car>) completeCarList.stream().filter(
				(vehicle -> vehicle.getMake().contains(searchKeyWord) || vehicle.getModel().contains(searchKeyWord)
				// || vehicle.getType().contains(searchKeyWord)
						|| vehicle.getCarCategory().toString().contains(searchKeyWord)
						|| vehicle.getColor().contains(searchKeyWord)
						|| String.valueOf(vehicle.getYear()).contains(searchKeyWord)))
				.collect(Collectors.toList());

		return resultList;

	}

	/*
	 * Performs the sorting based on the option selected by the user and returns the
	 * matching records
	 * 
	 * @param nword to be searched, complete list of all cars
	 * 
	 * @return list of matching records for the selected filter
	 */
	public ArrayList<Car> getFilteredData(HashMap<String, String> searchParamsMap, ArrayList<group8.Car> completeList) {
		ArrayList<ArrayList<Car>> filteredLists = new ArrayList<ArrayList<Car>>();
		ArrayList<Car> filterListOnBrand = new ArrayList<Car>();
		ArrayList<Car> filterListOnModel = new ArrayList<Car>();
		ArrayList<Car> filterListOnType = new ArrayList<Car>();
		ArrayList<Car> filterListOnCategory = new ArrayList<Car>();
		ArrayList<Car> filterListOnColor = new ArrayList<Car>();
		ArrayList<Car> filterListOnPriceMin = new ArrayList<Car>();
		ArrayList<Car> filterListOnPriceMax = new ArrayList<Car>();
		ArrayList<Car> filterListOnMileage = new ArrayList<Car>();
		ArrayList<Car> filterListOnYear = new ArrayList<Car>();

		for (String key : searchParamsMap.keySet()) {
			System.out.println("key" + key);

			if (key.equalsIgnoreCase("make")) {
				filterListOnBrand = (ArrayList<Car>) completeList.stream()
						.filter(vehicle -> vehicle.getMake().equals(searchParamsMap.get(key)))
						.collect(Collectors.toList());
				if (!filterListOnBrand.isEmpty())
					filteredLists.add(filterListOnBrand);
			}

			if (key.equalsIgnoreCase("model")) {
				filterListOnModel = (ArrayList<Car>) completeList.stream()
						.filter(vehicle -> vehicle.getModel().equalsIgnoreCase(searchParamsMap.get(key)))
						.collect(Collectors.toList());
				if (!filterListOnModel.isEmpty())
					filteredLists.add(filterListOnModel);
			}

			/*
			 * if(key.equalsIgnoreCase("type")) { filterListOnType = (ArrayList<Car>)
			 * completeList.stream() .filter(vehicle ->
			 * vehicle.getType().equalsIgnoreCase(searchParamsMap.get(key)))
			 * .collect(Collectors.toList()); if(!filterListOnType.isEmpty())
			 * filteredLists.add(filterListOnType); }
			 */
			if (key.equalsIgnoreCase("category")) {

				filterListOnCategory = (ArrayList<Car>) completeList.stream().filter(
						vehicle -> vehicle.getCarCategory().toString().equalsIgnoreCase(searchParamsMap.get(key)))
						.collect(Collectors.toList());
				if (!filterListOnCategory.isEmpty())
					filteredLists.add(filterListOnCategory);
			}
			if (key.equalsIgnoreCase("color")) {
				filterListOnColor = (ArrayList<Car>) completeList.stream()
						.filter(vehicle -> vehicle.getColor().equalsIgnoreCase(searchParamsMap.get(key)))
						.collect(Collectors.toList());
				if (!filterListOnColor.isEmpty())
					filteredLists.add(filterListOnColor);
			}

			if (key.equalsIgnoreCase("priceMin")) {

				filterListOnPriceMin = (ArrayList<Car>) completeList.stream()
						.filter(vehicle -> vehicle.getMSRP() > Double.parseDouble((searchParamsMap.get(key))))
						.collect(Collectors.toList());
				if (!filterListOnPriceMin.isEmpty())
					filteredLists.add(filterListOnPriceMin);
			}

			if (key.equalsIgnoreCase("priceMax")) {

				filterListOnPriceMax = (ArrayList<Car>) completeList.stream()
						.filter(vehicle -> vehicle.getMSRP() < Double.parseDouble((searchParamsMap.get(key))))
						.collect(Collectors.toList());
				if (!filterListOnPriceMax.isEmpty())
					filteredLists.add(filterListOnPriceMax);
			}

			if (key.equalsIgnoreCase("mileage")) {

				String mile = searchParamsMap.get(key);
				String[] result = mile.split("-");
				int minMile = Integer.parseInt(result[0]);
				int maxMile = Integer.parseInt(result[1]);

				filterListOnMileage = (ArrayList<Car>) completeList.stream()
						.filter(vehicle -> (vehicle.getMileage() > (minMile)) && (vehicle.getMileage() < (maxMile)))
						.collect(Collectors.toList());
				if (!filterListOnMileage.isEmpty())
					filteredLists.add(filterListOnMileage);
			}

			if (key.equalsIgnoreCase("year")) {

				filterListOnYear = (ArrayList<Car>) completeList.stream()
						.filter(vehicle -> String.valueOf(vehicle.getYear()).equalsIgnoreCase(searchParamsMap.get(key)))
						.collect(Collectors.toList());
				if (!filterListOnYear.isEmpty())
					filteredLists.add(filterListOnYear);
			}

		}
		return intersectLists(filteredLists);

	}

	public ArrayList<Car> intersectLists(ArrayList<ArrayList<Car>> filteredLists) {
		
		if(filteredLists.isEmpty()) {
			return new ArrayList<Car>();
		}
		ArrayList<Car> resultFilteredList = filteredLists.get(0);
		for (ArrayList li : filteredLists) {

			if (!li.isEmpty()) {
				resultFilteredList.retainAll(li);
			}
		}
		return resultFilteredList;
	}
}
