package xyz.drugalev.adapters.in.console;

import xyz.drugalev.adapters.in.console.menu.Menu;
import xyz.drugalev.adapters.in.console.menu.MenuItem;
import xyz.drugalev.adapters.logger.Audit;
import xyz.drugalev.domain.entity.*;
import xyz.drugalev.domain.exception.IllegalDatePeriodException;
import xyz.drugalev.domain.repository.TrainingRepositoryImpl;
import xyz.drugalev.domain.repository.TrainingTypeRepositoryImpl;
import xyz.drugalev.domain.repository.UserRepositoryImpl;
import xyz.drugalev.domain.service.*;
import xyz.drugalev.domain.validator.UserValidator;
import xyz.drugalev.usecase.training.*;
import xyz.drugalev.usecase.trainingtype.AddTrainingTypeUseCase;
import xyz.drugalev.usecase.trainingtype.FindTrainingTypeUseCase;
import xyz.drugalev.usecase.user.LoginUserUseCase;
import xyz.drugalev.usecase.user.RegisterUserUseCase;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Console application class.
 *
 * @author Drugalev Maxim
 */
public class ConsoleApplication {
    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl(), new UserValidator());
    private final TrainingTypeService trainingTypeService = new TrainingTypeServiceImpl(new TrainingTypeRepositoryImpl());
    private final TrainingService trainingService = new TrainingServiceImpl(new TrainingRepositoryImpl());
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
                    loginScreen();
                    break;
                }
                case "REGISTER": {
                    registerScreen();
                    break;
                }
            }
        }
    }

    private void loginScreen() {
        System.out.print("Username");
        String username = inputUtil.getLine();
        System.out.print("Password");
        String password = inputUtil.getPassword();

        LoginUserUseCase useCase = new LoginUserUseCase(userService);

        try {
            currentUser = useCase.login(username, password);
            Audit.getInstance().log("User " + username + "  logged in");
            userScreen();
        } catch (Exception e) {
            System.err.println("Couldn't login: " + e.getMessage());
        }
    }

    private void registerScreen() {
        System.out.print("Username");
        String username = inputUtil.getLine();
        System.out.print("Password");
        String password = inputUtil.getPassword();

        RegisterUserUseCase useCase = new RegisterUserUseCase(userService);

        try {
            currentUser = useCase.register(username, password);
            Audit.getInstance().log("User " + username + " registered");
            userScreen();
        } catch (Exception e) {
            System.err.println("Couldn't register: " + e.getMessage());
        }
    }

    private void userScreen() {
        Menu menu = new Menu(inputUtil,
                new MenuItem("Add training", "ADD_TRAININGS"),
                new MenuItem("All my trainings", "GET_ALL_USER_TRAININGS"),
                new MenuItem("My trainings in period", "GET_TRAININGS_PERIOD"),
                new MenuItem("My stats in period", "GET_STATS")
        );

        if (currentUser.hasPrivilege(Privilege.ADD_TRAINING_TYPE)) {
            menu.addMenuItem(new MenuItem("Add training type", "ADD_TRAINING_TYPE"));
        }
        if (currentUser.hasPrivilege(Privilege.SEE_OTHERS_TRAININGS)) {
            menu.addMenuItem(new MenuItem("Other Users trainings", "OTHER_USERS_TRAININGS"));
        }
        if (currentUser.hasPrivilege(Privilege.SEE_AUDIT_LOG)) {
            menu.addMenuItem(new MenuItem("See audit log", "SEE_AUDIT_LOG"));
        }

        while (true) {
            switch (menu.open()) {
                case Menu.MENU_EXIT: {
                    return;
                }
                case "ADD_TRAININGS": {
                    addTrainingScreen();
                    break;
                }
                case "GET_ALL_USER_TRAININGS": {
                    getAllUserTrainingsScreen();
                    break;
                }
                case "GET_TRAININGS_PERIOD": {
                    getTrainingsInPeriodScreen();
                    break;
                }
                case "GET_STATS": {
                    getStatsInPeriodScreen();
                    break;
                }
                case "ADD_TRAINING_TYPE": {
                    addTrainingTypeScreen();
                    break;
                }
                case "OTHER_USERS_TRAININGS": {
                    getOtherUsersTrainingsScreen();
                    break;
                }
                case "SEE_AUDIT_LOG": {
                    auditLogScreen();
                    break;
                }
            }
        }
    }

    private void addTrainingScreen() {
        try {
            Menu selectTypeMenu = new Menu(inputUtil);

            FindTrainingTypeUseCase trainingTypeUseCase = new FindTrainingTypeUseCase(trainingTypeService);
            List<TrainingType> trainingTypes = trainingTypeUseCase.findAll();

            for (int i = 0; i < trainingTypes.size(); i++) {
                selectTypeMenu.addMenuItem(new MenuItem(trainingTypes.get(i).getName(), String.valueOf(i)));
            }

            System.out.println("Select training type");
            String userPick = selectTypeMenu.open();

            if (userPick.equals(Menu.MENU_EXIT)) {
                return;
            }

            TrainingType selectedType = trainingTypes.get(Integer.parseInt(userPick));
            System.out.print("Training duration");
            int duration = inputUtil.getInt();
            System.out.print("Burned Calories");
            int burnedCalories = inputUtil.getInt();

            AddTrainingUseCase addTrainingUseCase = new AddTrainingUseCase(trainingService);
            addTrainingUseCase.add(currentUser, LocalDate.now(), selectedType, duration, burnedCalories);

            Audit.getInstance().log("User " + currentUser.getUsername() + " added training");

        } catch (Exception e) {
            System.err.println("Unable to add training: " + e.getMessage());
        }
    }

    private void getAllUserTrainingsScreen() {
        FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);
        List<Training> trainings = useCase.findAllByUser(currentUser);
        Audit.getInstance().log("User " + currentUser.getUsername() + " requested all his trainings");
        viewTrainingsScreen(trainings);
    }

    private void getTrainingsInPeriodScreen() {
        FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);
        LocalDate start = inputUtil.getLocalDate();
        LocalDate end = inputUtil.getLocalDate();
        try {
            List<Training> trainings = useCase.findBetween(currentUser, start, end);
            Audit.getInstance().log("User " + currentUser.getUsername() + " requested all his trainings in period");
            viewTrainingsScreen(trainings);
        } catch (IllegalDatePeriodException e) {
            System.err.println("Unable to get trainings: " + e.getMessage());
        }
    }

    private void viewTrainingsScreen(List<Training> trainings) {
        Menu selectTrainingMenu = new Menu(inputUtil);

        for (int i = 0; i < trainings.size(); i++) {
            selectTrainingMenu.addMenuItem(new MenuItem(trainings.get(i).toString(), String.valueOf(i)));
        }

        System.out.println("Select training");
        String userPick = selectTrainingMenu.open();

        if (userPick.equals(Menu.MENU_EXIT)) {
            return;
        }

        Training selectedTraining = trainings.get(Integer.parseInt(userPick));

        Audit.getInstance().log("User " + currentUser.getUsername() + " requested training details");
        viewTrainingScreen(selectedTraining);
    }

    private void viewTrainingScreen(Training training) {
        Menu editTrainingMenu = new Menu(inputUtil,
                new MenuItem("Add additional data", "ADD_DATA"),
                new MenuItem("Edit Training", "EDIT"),
                new MenuItem("Delete Training", "DELETE"));

        while (true) {
            System.out.println(training);
            System.out.println("Additional data: ");
            for (TrainingData data : training.getTrainingData()) {
                System.out.println(data);
            }

            switch (editTrainingMenu.open()) {
                case Menu.MENU_EXIT: {
                    return;
                }
                case "ADD_DATA": {
                    addTrainingDataScreen(training);
                    break;
                }
                case "EDIT": {
                    editTrainingScreen(training);
                    break;
                }
                case "DELETE": {
                    deleteTrainingScreen(training);
                    return;
                }
            }

        }

    }

    private void addTrainingDataScreen(Training training) {
        System.out.print("Data name");
        String name = inputUtil.getLine();
        System.out.print("Data value");
        int value = inputUtil.getInt();

        AddTrainingDataUseCase useCase = new AddTrainingDataUseCase(trainingService);
        useCase.addTrainingData(training, new TrainingData(name, value));
        Audit.getInstance().log("User " + currentUser.getUsername() + " added training data to training");
    }

    private void editTrainingScreen(Training training) {
        try {
            System.out.print("Training duration");
            int duration = inputUtil.getInt();
            System.out.print("Burned Calories");
            int burnedCalories = inputUtil.getInt();

            UpdateTrainingUseCase updateTrainingUseCase = new UpdateTrainingUseCase(trainingService);
            updateTrainingUseCase.update(training, duration, burnedCalories);
            Audit.getInstance().log("User " + currentUser.getUsername() + " updated training");
        } catch (Exception e) {
            System.err.println("Couldn't edit training: " + e.getMessage());
        }
    }

    private void deleteTrainingScreen(Training training) {
        try {
            DeleteTrainingUseCase useCase = new DeleteTrainingUseCase(trainingService);
            useCase.delete(training);
            Audit.getInstance().log("User " + currentUser.getUsername() + " deleted training");
        } catch (Exception e) {
            System.err.println("Couldn't edit training: " + e.getMessage());
        }
    }

    private void getStatsInPeriodScreen() {
        GetTrainingStatsUseCase useCase = new GetTrainingStatsUseCase(trainingService);
        LocalDate start = inputUtil.getLocalDate();
        LocalDate end = inputUtil.getLocalDate();

        try {
            int duration = useCase.getTrainingsDuration(currentUser, start, end);
            int burnedCalories = useCase.getTrainingsBurnedCalories(currentUser, start, end);

            System.out.printf("In %d days you trained %d min and burned %d cal\n",
                    DAYS.between(start, end) + 1, duration, burnedCalories);

            Audit.getInstance().log("User " + currentUser.getUsername() + " requested training stats in period");
        } catch (IllegalDatePeriodException e) {
            System.err.println("Couldn't load stats for given period: " + e.getMessage());
        }
    }

    private void addTrainingTypeScreen() {
        System.out.print("Training Type Name");
        String trainingTypeName = inputUtil.getLine();
        AddTrainingTypeUseCase trainingTypeUseCase = new AddTrainingTypeUseCase(trainingTypeService);
        trainingTypeUseCase.add(trainingTypeName);
        Audit.getInstance().log("User " + currentUser.getUsername() + " added training type");
    }

    private void getOtherUsersTrainingsScreen() {
        FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);
        List<Training> trainings = useCase.findAll();
        Audit.getInstance().log("User " + currentUser.getUsername() + " requested all trainings");
        viewTrainingsScreen(trainings);
    }

    private void auditLogScreen() {
        for (String auditRecord : Audit.getInstance().getAuditRecords()) {
            System.out.println(auditRecord);
        }
    }
}

