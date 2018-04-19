package dateNight;

import cst8132.restaurant.Menu;
import cst8132.restaurant.MenuItem;
import cst8132.restaurant.Restaurant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Double Date class id used to run a simulation of a date night. It contains all the information for the restaurant and
 * the movie theater, and initializes the people going out on the date. Then it runs a simulation and prints out the output of the movie
 * choice and the bill for the restaurant.
 */
public class DoubleDate extends JFrame {

	/**
	 * This is an arraylist of the guests that you pass as an argument to the DoubleDate constructor.
	 */
	protected ArrayList<String> guests;

	/**
	 * This is an array that contains the movies that are being played at the movie theater.
	 */
	protected String[] movies;

	/**
	 * This is where the movie that is randomly selected from the movies array is stored. It just stores the name
	 * of the movie.
	 */
	protected String movieTitle;

	/**
	 * There are two show times for every movie. A time is randomly assigned to this variable.
	 */
	protected int movieTime;

	/**
	 * This is where we create our restaurant for the double date class.
	 */
	protected Restaurant restaurant;

	/**
	 * This is the menu that is created for the restaurant.
	 */
	protected Menu menu;

	/**
	 * This is the bill that is generated from the addMenuItems method.
	 */
	protected Bill bill;

	private JPanel inputPanel;
	private JPanel guestList;
	private JLabel addGuestPrompt;
	private JLabel guestListHeader;
	private JTextField newGuestName;
	private JButton addGuest;
	private JButton letsGo;


	public static void main(String[] args) {

		DoubleDate date = new DoubleDate();
		date.setVisible(true);

	}

	/**
	 * The constructor is responsible for creating the JFrame GUI. Which takes input for the person who is setting up the
	 * date, as well as the guests that are coming. Initializes the restaurant, menu and bill for the date. Populates
	 * the movies array.
	 */
	public DoubleDate() {
		//Set the name of the JFrame
		super("Double Date");

		//set the layout of the DoubleDate
		this.setLayout(new GridLayout(2, 2));

		//instantiate the addGuestPrompt and add it the Double date frame.
		addGuestPrompt = new JLabel("Enter a guest name:");
		this.add(addGuestPrompt);

		guestListHeader = new JLabel("Guest List");
		this.add(guestListHeader);

		inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());

		newGuestName = new JTextField(20);
		inputPanel.add(newGuestName);

		addGuest = new JButton("Add Guest to list.");
		inputPanel.add(addGuest);

		letsGo = new JButton("Let's go Out!");
		inputPanel.add(letsGo);

		letsGo.setVisible(false);

		this.add(inputPanel);

		guestList = new JPanel();
		guestList.setLayout(new FlowLayout());

		this.add(guestList);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 250);

		addGuest.addActionListener(new AddGuestHandler());

		letsGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goOnDate((DoubleDate) SwingUtilities.getRoot((Component) e.getSource()));
				setVisible(false);
				dispose();
			}
		});

		//initialize array list capacity and add your name
		this.guests = new ArrayList<String>(4);


		//initialize restaurant
		restaurant = Restaurant.getInstance("The Waverly");

		//initialize a menu
		menu = restaurant.getMenu();

		//populate the menu
		addMenuItems();

		bill = new Bill();

		movies = new String[4];
		movies[0] = "Snatch";
		movies[1] = "The Shawshank Redemption";
		movies[2] = "Baby Driver";
		movies[3] = "Snowpiercer";
	}

	/**
	 * Assigns a random number to a variable. Then depending on the value
	 * of that variable will return one of the movies in the movies array.
	 *
	 * @return moveTitle
	 */
	public String pickAMovie() {
		double rando = Math.random();

		if (rando < .25) {
			movieTitle = movies[0];
		} else if (rando < .5 && rando >= .25) {
			movieTitle = movies[1];
		} else if (rando < .75) {
			movieTitle = movies[2];
		} else if (rando < 1) {
			movieTitle = movies[3];
		}

		return movieTitle;
	}

	/**
	 * Flips a coin to decide what show time the group is going to see.
	 *
	 * @return 6 or 10 depending on the coin flip outcome.
	 */
	public int getShowing() {
		double coinFlip = Math.random();

		if (coinFlip <= .5) {
			movieTime = 6;
		} else {
			movieTime = 10;
		}

		return movieTime;

	}

	/**
	 * This method populates the restaurant menu. It takes a text file and imports all
	 * the menu items from it to form the menu.
	 */
	public void addMenuItems() {

		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileReader("menu.txt"));
			scanner.useDelimiter(",");
			while (scanner.hasNextLine()) {
				String type = scanner.next();
				scanner.skip(scanner.delimiter());
				double price = scanner.nextDouble();
				scanner.skip(scanner.delimiter());
				String itemName = scanner.nextLine();

				menu.addMenuItem(type, itemName, price);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

//		menu.addMenuItem("Drinks", "Coke", 6);
//		menu.addMenuItem("Drinks", "Beer", 8);
//		menu.addMenuItem("Drinks", "Wine", 10);
//		menu.addMenuItem("Drinks", "Sparkling Water", 4);
//
//		menu.addMenuItem("Appetizers", "Spinach Dip", 14);
//		menu.addMenuItem("Appetizers", "Wings", 16);
//		menu.addMenuItem("Appetizers", "Nachos", 16);
//		menu.addMenuItem("Appetizers", "Cheese Sticks", 12);
//
//		menu.addMenuItem("Entrees", "Burger", 16);
//		menu.addMenuItem("Entrees", "Club Sandwich", 16);
//		menu.addMenuItem("Entrees", "Chicken Salad", 14);
//		menu.addMenuItem("Entrees", "Striploin Steak", 28);
//
//		menu.addMenuItem("Desserts", "Tiramisu", 14);
//		menu.addMenuItem("Desserts", "Chocolate brownie", 12);
//		menu.addMenuItem("Desserts", "Gelato", 14);
//		menu.addMenuItem("Desserts", "Churros", 14);

	}

	/**
	 * THe placeOrder method is used to generate a random order and add it to the bill.
	 *
	 * @param guest    Is the guest who is placing the order.
	 * @param itemtype Is the type of food that they are ordering (apps, entrees, drinks, or desserts)
	 * @return returns true or false depending on whether on not the other parameters are done correctly.
	 */
	public boolean placeOrder(String guest, String itemtype) {
		MenuItem item = menu.getRandomMenuItem(itemtype);
		return bill.addOrderItem(guest, item);
	}

	/**
	 * This method overrides the default toString method and prints the movies array and then the selection.
	 * Then prints the restaurant information and whether or not the group will be making happy hour.
	 *
	 * @return A String of all the information for the date.
	 */
	@Override
	public String toString() {

		if (movieTime == 6) {
			return "Movies: " + "\n" + movies[0] + "\n" + movies[1] + "\n" + movies[2] + "\n" + movies[3] + "\n" + "\n" + "You have" +
					" chosen " + this.movieTitle + "! \n" + "We will be meeting at " + this.restaurant.getName() + " after the movie." +
					"\n" + "We will be missing happy hour, but we will still be happy! \n\n" + "MENU: \n" + menu.toString() + "\n" + bill.toString();
		} else {
			return "Movies: " + "\n" + movies[0] + "\n" + movies[1] + "\n" + movies[2] + "\n" + movies[3] + "\n" + "\n" + "You have" +
					" chosen " + this.movieTitle + "! \n" + "We will be meeting at " + this.restaurant.getName() + " before the movie.\n" +
					"It's happy hour! $2 off drinks, and 1/2 price appetizers!! \n\n" + "MENU: \n" + menu.toString() + "\n" + bill.toString();
		}
	}

	/**
	 * This method is used to call the pickAMovie method and the getShowing method for the date. Then starts assigning
	 * random menu items to guests based on the guidelines given. Then it outputs to a file named bill.txt.
	 *
	 * @param date implicitly calls the toString method to print out the details of the date once they've been
	 *             simulated.
	 */
	public void goOnDate(DoubleDate date) {
		date.pickAMovie();
		date.getShowing();

		if (date.movieTime == 10) {
			date.bill.setHappyHour();
		}

		for (String i : guests) {
			if (i.equals(guests.get(0))) {
				date.placeOrder(i, "appetizers");
			}
			date.placeOrder(i, "drinks");
			date.placeOrder(i, "entrees");
		}

		date.placeOrder(guests.get(0), "desserts");

		if (guests.size() > 2) {
			date.placeOrder(guests.get(2), "desserts");
		}

		printBill();

		System.out.println(date);
	}

	/**
	 * This method is used to to print the bill.toString() of the DoubleDate object to a text file.
	 */
	private void printBill() {
		FileWriter locFile = null;
		try {
			locFile = new FileWriter("bill.txt");
			locFile.write(String.valueOf(this.bill));
			locFile.close();
		} catch (IOException e) {
			System.err.println("Problem creating output file.");
		}
	}

	/**
	 * This is used to color the GUI
	 *
	 * @param g is the graphic passed to the gui.
	 */
	public void paint(Graphics g) {
		super.paint(g);
	}

	private class AddGuestHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String guest = newGuestName.getText();
			guests.add(guest);
			newGuestName.setText("");
			guestList.add(new JLabel(guest));
			letsGo.setVisible(true);

			if (guests.size() < 4) {
				addGuest.setVisible(true);
			} else {
				addGuest.setVisible(false);
			}

			guestList.validate();
			guestList.repaint();
		}

	}


}
