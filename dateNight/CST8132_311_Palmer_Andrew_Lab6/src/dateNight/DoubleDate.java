package dateNight;

import cst8132.restaurant.Menu;
import cst8132.restaurant.MenuItem;
import cst8132.restaurant.Restaurant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;


public class DoubleDate extends JFrame {

    protected ArrayList<String> guests;
    protected String[] movies;
    protected String movieTitle;
    protected int movieTime;
    protected Restaurant restaurant;
    protected Menu menu;
    protected Bill bill;

    private JPanel inputPanel;
    private JPanel guestList;
    private JLabel addGuestPrompt;
    private JLabel guestListHeader;
    private JTextField newGuestName;
    private JButton addGuest;
    private JButton letsGo;


    private Random random;


    public static void main(String[] args) {

        DoubleDate date = new DoubleDate();
        date.setVisible(true);

    }

    public DoubleDate() {
        //Set the name of the JFrame
        super("Double Date");

        //set the layout of the DoubleDate
        this.setLayout(new GridLayout(2,2));

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
        setSize(500,250);

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
     * This method populates the restaurant menu.
     */
    public void addMenuItems() {

        menu.addMenuItem("Drinks", "Coke", 6);
        menu.addMenuItem("Drinks", "Beer", 8);
        menu.addMenuItem("Drinks", "Wine", 10);
        menu.addMenuItem("Drinks", "Sparkling Water", 4);

        menu.addMenuItem("Appetizers", "Spinach Dip", 14);
        menu.addMenuItem("Appetizers", "Wings", 16);
        menu.addMenuItem("Appetizers", "Nachos", 16);
        menu.addMenuItem("Appetizers", "Cheese Sticks", 12);

        menu.addMenuItem("Entrees", "Burger", 16);
        menu.addMenuItem("Entrees", "Club Sandwich", 16);
        menu.addMenuItem("Entrees", "Chicken Salad", 14);
        menu.addMenuItem("Entrees", "Striploin Steak", 28);

        menu.addMenuItem("Desserts", "Tiramisu", 14);
        menu.addMenuItem("Desserts", "Chocolate brownie", 12);
        menu.addMenuItem("Desserts", "Gelato", 14);
        menu.addMenuItem("Desserts", "Churros", 14);

    }

    /**
     * THe placeOrder method is used to generate a random order and add it to the bill.
     * @param guest Is the guest who is placing the order.
     * @param itemtype Is the type of food that they are ordering (apps, entrees, drinks, or desserts)
     * @return returns true or false depending on whether on not the other parameters are done correctly.
     */
    public boolean placeOrder(String guest, String itemtype) {
        MenuItem item = menu.getRandomMenuItem(itemtype);
        return bill.addOrderItem(guest, item);
    }

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

    public void goOnDate(DoubleDate date) {
        date.pickAMovie();
        date.getShowing();

        if (date.movieTime == 10) {
            date.bill.setHappyHour();
        }

        for(String i : guests) {
            if (i.equals(guests.get(0))) {
                date.placeOrder(i,"appetizers");
            }
            date.placeOrder(i, "drinks");
            date.placeOrder(i, "entrees");
        }

        date.placeOrder(guests.get(0),"desserts");

        if (guests.size() > 2) {
            date.placeOrder(guests.get(2), "desserts");
        }

        System.out.println(date);
    }

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
