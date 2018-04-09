package dateNight;

import java.util.ArrayList;
import java.util.HashMap;

import cst8132.restaurant.Appetizer;
import cst8132.restaurant.Drink;
import cst8132.restaurant.MenuItem;


public class Bill {
	private HashMap<String, ArrayList<MenuItem>> orders = new HashMap<String, ArrayList<MenuItem>>(4);

	private boolean isHappyHour = false;
	private double subtotal;
	private double hstRate = 0.15;
	private final int MAX_MENU_ITEM_LENGTH = 30;

	/**
	 *
	 * @param guest
	 * @param item
	 * @return
	 */
	public boolean addOrderItem(String guest, MenuItem item) {

		ArrayList<MenuItem> o = orders.getOrDefault(guest, new ArrayList<MenuItem>(4));
		o.add(item);

		orders.put(guest, o);

		subtotal += item.getPrice();

		return true;
	}

	/**
	 *
	 * @return
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
	 *
	 * @return
	 */
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
	 *
	 * @return
	 */
	public double getSubtotal() {
		return subtotal;
	}

	/**
	 * 
	 * @return
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
	 *
	 * @return
	 */
	public double getHstRate() {

		return hstRate;
	}

	/**
	 *
	 * @return
	 */
	public double getTotal() {
		if (isHappyHour) {

			return (this.subtotal - this.getHappyHourDiscount()) + getHst();

		}

		return this.subtotal += this.getHst();
	}
}
