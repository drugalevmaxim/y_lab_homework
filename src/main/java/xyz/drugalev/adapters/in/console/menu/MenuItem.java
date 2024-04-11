package xyz.drugalev.adapters.in.console.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MenuItem {
    public static final String CLOSE_LABEL = "Exit";
    public static final String CLOSE_ACTION = "EXIT";

    public static final MenuItem CLOSE_MENU_ITEM = new MenuItem(CLOSE_LABEL, CLOSE_ACTION);

    private final String label;
    private final String action;

}
