package menu;

/**
 * Represents a menu option with a code, description, and action to execute
 */
public record MenuOption(int code, String description, Runnable action) {

    public void execute() {
        if (action != null) {
            action.run();
        }
    }

    @Override
    public String toString() {
        return code + ". " + description;
    }
}

