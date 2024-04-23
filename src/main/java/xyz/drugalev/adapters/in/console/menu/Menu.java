package xyz.drugalev.adapters.in.console.menu;

import xyz.drugalev.adapters.in.console.InputUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Menu class.
 *
 * @author Drugalev Maxim
 */
public class Menu {
    /**
     * The constant exit menu item action.
     */
    public static final String MENU_EXIT = "EXIT";
    private final List<MenuItem> menuItems = new ArrayList<>();
    private final InputUtil inputUtil;

    /**
     * Instantiates a new Menu.
     *
     * @param inputUtil the input util
     * @param items     the menu items
     */
    public Menu(InputUtil inputUtil, MenuItem ... items) {
        this.inputUtil = inputUtil;
        MenuItem closeMenuItem = MenuItem.CLOSE_MENU_ITEM;
        menuItems.add(closeMenuItem);
        menuItems.addAll(Arrays.stream(items).toList());
    }

    /**
     * Add menu item to menu.
     *
     * @param menuItem the menu item to add
     */
    public void addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
    }

    /**
     * Gets user input as menu item.
     *
     * @return the user inputted menu item
     */
    public MenuItem getUserInput() {
        while (true) {
            int index = inputUtil.getInt();
            if (index < 0 || index >= menuItems.size()) {
                System.err.println("Invalid menu item. Try again!");
                continue;
            }
            return menuItems.get(index);
        }
    }

    /**
     * Open menu.
     *
     * @return the menu action string
     */
    public String open() {
        drawMenu();
        MenuItem menuItem = getUserInput();
        return menuItem.getAction();
    }

    private void drawMenu() {
        for (int i = 1; i < menuItems.size(); i++) {
            System.out.println(i + " - " + menuItems.get(i).getLabel());
        }

        System.out.println("\n" + 0 + " - " + menuItems.get(0).getLabel());
    }
}
