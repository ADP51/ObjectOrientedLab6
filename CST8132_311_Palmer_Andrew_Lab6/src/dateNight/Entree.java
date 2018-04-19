package dateNight;

import cst8132.restaurant.MenuItem;

/**
 * Class Entree is a subclass of menu that is used to create instances of entrees on the menu.
 */
public class Entree extends MenuItem{

	/**
	 *This is the constructor for the Entree menuItem and is used to create the entrees on the menu.
	 * @param name Name of the entree.
	 * @param price Price of the entree.
	 */
	public Entree(String name, double price) {
		super(name, price);
	}
}
