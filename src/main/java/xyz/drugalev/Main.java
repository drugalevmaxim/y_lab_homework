package xyz.drugalev;

import xyz.drugalev.database.MigrationLoader;

public class Main {
    public static void main(String[] args) {
        MigrationLoader.migrate();
    }
}
