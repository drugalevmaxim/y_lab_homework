package xyz.drugalev.adapters.in.console;

import xyz.drugalev.adapters.in.console.menu.Menu;
import xyz.drugalev.adapters.in.console.menu.MenuItem;
import xyz.drugalev.adapters.logger.Audit;
import xyz.drugalev.adapters.logger.AuditRecord;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.repository.TrainingRepository;
import xyz.drugalev.domain.repository.UserRepository;
import xyz.drugalev.domain.repository.impl.*;
import xyz.drugalev.domain.service.TrainingService;
import xyz.drugalev.domain.service.TrainingTypeService;
import xyz.drugalev.domain.service.UserService;
import xyz.drugalev.domain.service.impl.TrainingServiceImpl;
import xyz.drugalev.domain.service.impl.TrainingTypeServiceImpl;
import xyz.drugalev.domain.service.impl.UserServiceImpl;
import xyz.drugalev.domain.validator.UserValidator;

import java.sql.SQLException;

/**
 * Console application class.
 *
 * @author Drugalev Maxim
 */
public class ConsoleApplication {
    private final UserRepository userRepository = new UserRepositoryImpl(new RoleRepositoryImpl(new PrivilegeRepositoryImpl()));
    private final TrainingRepository trainingRepository = new TrainingRepositoryImpl(new TrainingTypeRepositoryImpl(), new TrainingDataRepositoryImpl(), userRepository);
    private final TrainingService trainingService = new TrainingServiceImpl(trainingRepository);
    private final UserService userService = new UserServiceImpl(userRepository, new UserValidator());
    private final TrainingTypeService trainingTypeService = new TrainingTypeServiceImpl(new TrainingTypeRepositoryImpl());
    private final InputUtil inputUtil = new InputUtil();
    private User currentUser;

    /**
     * Run application
     */
    public void run() {
        Menu mainMenu = new Menu(inputUtil,
                new MenuItem("Login", "LOGIN"),
                new MenuItem("Register", "REGISTER")
        );

        while (true) {
            currentUser = null;
            switch (mainMenu.open()) {
                case Menu.MENU_EXIT: {
                    return;
                }
                case "LOGIN": {
                    try {
                        currentUser = AuthController.loginScreen(userService, inputUtil);
                        userScreen();
                    } catch (Exception e) {
                        System.err.println("Couldn't login: " + e.getMessage());
                    }
                    break;
                }
                case "REGISTER": {
                    try {
                        currentUser = AuthController.registerScreen(userService, inputUtil);
                        userScreen();
                    } catch (Exception e) {
                        System.err.println("Couldn't login: " + e.getMessage());
                    }
                    break;
                }
            }
        }
    }

    private void userScreen() {
        Menu menu = new Menu(inputUtil,
                new MenuItem("Add training", "ADD_TRAININGS"),
                new MenuItem("All my trainings", "GET_ALL_USER_TRAININGS"),
                new MenuItem("My trainings in period", "GET_TRAININGS_PERIOD"),
                new MenuItem("My stats in period", "GET_STATS")
        );

        if (currentUser.hasPrivilege("ADD_TRAINING_TYPE")) {
            menu.addMenuItem(new MenuItem("Add training type", "ADD_TRAINING_TYPE"));
        }
        if (currentUser.hasPrivilege("SEE_OTHERS_TRAININGS")) {
            menu.addMenuItem(new MenuItem("Other Users trainings", "OTHER_USERS_TRAININGS"));
        }
        if (currentUser.hasPrivilege("SEE_AUDIT_LOG")) {
            menu.addMenuItem(new MenuItem("See audit log", "SEE_AUDIT_LOG"));
        }

        while (true) {
            switch (menu.open()) {
                case Menu.MENU_EXIT: {
                    return;
                }
                case "ADD_TRAININGS": {
                    try {
                        TrainingController.addTrainingScreen(trainingService, trainingTypeService, currentUser, inputUtil);
                    } catch (Exception e) {
                        System.err.println("Unable to add training: " + e.getMessage());
                    }
                    break;
                }
                case "GET_ALL_USER_TRAININGS": {
                    try {
                        TrainingController.getAllUserTrainingsScreen(trainingService, currentUser, inputUtil);
                    } catch (Exception e) {
                        System.err.println("Unable to get trainings: " + e.getMessage());
                    }
                    break;
                }
                case "GET_TRAININGS_PERIOD": {
                    try {
                        TrainingController.getTrainingsInPeriodScreen(trainingService, currentUser, inputUtil);
                    } catch (Exception e) {
                        System.err.println("Unable to get trainings: " + e.getMessage());
                    }
                    break;
                }
                case "GET_STATS": {
                    try {
                        TrainingController.getStatsInPeriodScreen(trainingService, currentUser, inputUtil);
                    } catch (SQLException e) {
                        System.err.println("Couldn't load stats for given period: " + e.getMessage());
                    }
                    break;
                }
                case "ADD_TRAINING_TYPE": {
                    try {
                        TrainingController.addTrainingTypeScreen(trainingTypeService, currentUser, inputUtil);
                    } catch (SQLException e) {
                        System.err.println("Couldn't add training type: " + e.getMessage());
                    }
                    break;
                }
                case "OTHER_USERS_TRAININGS": {
                    try {
                        TrainingController.getOtherUsersTrainingsScreen(trainingService, currentUser, inputUtil);
                    } catch (Exception e) {
                        System.err.println("Couldn't fetch all trainings: " + e.getMessage());
                    }
                    break;
                }
                case "SEE_AUDIT_LOG": {
                    auditLogScreen();
                    break;
                }
            }
        }
    }

    private void auditLogScreen() {
        for (AuditRecord auditRecord : Audit.getInstance().getAuditRecords()) {
            System.out.println(auditRecord);
        }
    }
}

