package settings;

/**
 * This enum provides properties that are to be loaded via
 * XML files to be used for setting up the application.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public enum AppPropertyType {

    // from app-properties.xml
    APP_WINDOW_WIDTH,
    APP_WINDOW_HEIGHT,
    APP_TITLE,
    APP_LOGO,
    APP_CSS,
    APP_PATH_CSS,

    // APPLICATION ICONS
    NEW_ICON,
    SAVE_ICON,
    LOAD_ICON,
    EXIT_ICON,
    BUZZWORD,
    HINT_ICON,

    // APPLICATION TOOLTIPS FOR BUTTONS
    NEW_TOOLTIP,
    SAVE_TOOLTIP,
    LOAD_TOOLTIP,
    EXIT_TOOLTIP,
    HINT_TOOLTIP,

    PROPERTIES_LOAD_ERROR_MESSAGE,

    PROPERTIES_LOAD_ERROR_TITLE,

}
