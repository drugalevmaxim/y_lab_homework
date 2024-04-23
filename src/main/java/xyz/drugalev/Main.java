package xyz.drugalev;

import xyz.drugalev.adapters.in.console.ConsoleApplication;
import xyz.drugalev.config.MigrationLoader;

/**
 * The entry point class.
 */
public class Main
{
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        if (args.length == 1 && args[0].equals("migrate")) {
            MigrationLoader.migrate();
        }

        ConsoleApplication application = new ConsoleApplication();
        application.run();
    }
}
