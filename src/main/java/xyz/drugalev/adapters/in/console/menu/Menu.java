package xyz.drugalev.adapters.in.console.menu;

import xyz.drugalev.adapters.in.console.InputUtil;

import java.util.*;

public class Menu {
    public static final String MENU_EXIT = "MENU_EXIT";
    private final List<MenuItem> menuItems = new ArrayList<>();
    private final MenuItem closeMenuItem;
    private final InputUtil inputUtil;

    public Menu(InputUtil inputUtil, MenuItem ... items) {
        this.inputUtil = inputUtil;
        this.closeMenuItem = MenuItem.CLOSE_MENU_ITEM;
        menuItems.add(this.closeMenuItem);
        menuItems.addAll(Arrays.stream(items).toList());
    }

    public void addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
    }

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

    public String open() {
        drawMenu();
        MenuItem menuItem = getUserInput();
        if (menuItem != closeMenuItem) {
            return menuItem.getAction();
        }
        return MENU_EXIT;
    }

    private void drawMenu() {
        for (int i = 1; i < menuItems.size(); i++) {
            System.out.println(i + " - " + menuItems.get(i).getLabel());
        }

        System.out.println("\n" + 0 + " - " + menuItems.get(0).getLabel());
    }
}
