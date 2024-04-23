package xyz.drugalev.adapters.in.console.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The Menu item class.
 *
 * @author Drugalev Maxim
 */
@RequiredArgsConstructor
@Getter
public class MenuItem {
    /**
     * The constant CLOSE_LABEL.
     */
    public static final String CLOSE_LABEL = "Exit";
    /**
     * The constant CLOSE_ACTION.
     */
    public static final String CLOSE_ACTION = "EXIT";

    /**
     * The constant CLOSE_MENU_ITEM.
     */
    public static final MenuItem CLOSE_MENU_ITEM = new MenuItem(CLOSE_LABEL, CLOSE_ACTION);

    private final String label;
    private final String action;

}
