package menu;

public interface Menu {
    void start();

    /**
     * Get all menu options with their associated actions
     */
    MenuOption[] getOptions();

    /**
     * Display all menu options
     */
    default void displayOptions() {
        for (MenuOption option : getOptions()) {
            System.out.println(option);
        }
    }

    /**
     * Execute a menu option by its code
     */
    default boolean executeOption(int code) {
        for (MenuOption option : getOptions()) {
            if (option.code() == code) {
                option.execute();
                return true;
            }
        }
        System.out.println("Invalid option: " + code);
        return false;
    }

    /**
     * Check if a code is valid for this menu
     */
    default boolean isValidOption(int code) {
        for (MenuOption option : getOptions()) {
            if (option.code() == code) {
                return true;
            }
        }
        return false;
    }
}
