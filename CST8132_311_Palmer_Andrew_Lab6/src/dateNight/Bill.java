package dateNight;

import java.util.ArrayList;
import java.util.HashMap;

import cst8132.restaurant.Appetizer;
import cst8132.restaurant.Drink;
import cst8132.restaurant.MenuItem;

/**
 * The bill class is used to create bills for the Restaurant class to use. It adds items into a hashmap and then calculates
 * a running total for all the menu items. Applying hst and discounts as appropriate.
 */
public class Bill {
	private HashMap<String, ArrayList<MenuItem>> orders = new HashMap<String, ArrayList<MenuItem>>(4);

	private boolean isHappyHour = false;
	private double subtotal;
	private double hstRate = 0.15;
	private final int MAX_MENU_ITEM_LENGTH = 30;

	/**
	 * This is the method responsible for adding menu items onto the bill. It assigns a guest name and item name.
	 * @param guest the name of the guest that orders the item.
	 * @param item the name of the actual item itself.
	 * @return true or false depending in whether the inputs for name and item are appropriate.
	 */
	public boolean addOrderItem(String guest, MenuItem item) {

		ArrayList<MenuItem> o = orders.getOrDefault(guest, new ArrayList<MenuItem>(4));
		o.add(item);

		orders.put(guest, o);

		subtotal += item.getPrice();

		return true;
	}

	/**
	 * This method is used to apply the happy hour discount to the bill.
	 * @return the appropriate discount for happy hour.
	 */
	public double getHappyHourDiscount() {

		double happyHourDiscount = 0;

		if (!isHappyHour)
			return 0;

		for (ArrayList<MenuItem> a : orders.values()) {

			for (MenuItem m : a) {

				if (m instanceof Drink) {
					happyHourDiscount += 2;
				}

				if (m instanceof Appetizer) {
					happyHourDiscount += m.getPrice() / 2;
				}

			}

		}

		return happyHourDiscount;
	}

	public void setHappyHour() {
		isHappyHour = true;
	}

	/**
	 * This method is to override the toString and print out the object in bill format.
	 * @return A sting in the form of a bill.
	 */
	@Override
	public String toString() {

		String s = "";
		String format = "\t%-" + MAX_MENU_ITEM_LENGTH + "s \t $%6.2f\n";

		for (String o : orders.keySet()) {

			s += "Dinner Guest: " + o + "\n";

			for (MenuItem item : orders.get(o)) {
				s += String.format(format, item.getName(), item.getPrice());
			}

			s += "\n";

		}

		s += String.format(format, "Subtotal", getSubtotal());
		s += String.format(format, "Happy Hour Discount", getHappyHourDiscount());
		s += String.format(format, "HST " + (int) (hstRate * 100) + "%", getHst());

		s += String.format(format, "Total", getTotal());

		return s;
	}

	/**
	 * This method s used to get the subtotal for all the items accumulated on the bill.
	 * @return the sum of all menu items before any adjustments.
	 */
	public double getSubtotal() {
		return subtotal;
	}

	/**
	 * This method is used to calculate and apply the HST to the bill.
	 * @return the value of hst.
	 */
	public double getHst() {
		double hst;

		if (isHappyHour) {

			hst = (this.subtotal - this.getHappyHourDiscount()) * hstRate;

		} else {

			hst = this.subtotal * hstRate;

		}
		return hst;
	}

	/**
	 * This method is used to get the current HST rate.
	 * @return The current HST rate.
	 */
	public double getHstRate() {

		return hstRate;
	}

	/**
	 * This method is used to get calculate and return the total of the bill.
	 * @return The sum of the subtotal plus the HST.
	 */
	public double getTotal() {
		if (isHappyHour) {

			return (this.subtotal - this.getHappyHourDiscount()) + getHst();

		}

		return this.subtotal += this.getHst();
	}
}
