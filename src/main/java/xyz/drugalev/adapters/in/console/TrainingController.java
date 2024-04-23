package xyz.drugalev.adapters.in.console;

import xyz.drugalev.adapters.in.console.menu.Menu;
import xyz.drugalev.adapters.in.console.menu.MenuItem;
import xyz.drugalev.adapters.logger.Audit;
import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingData;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.IllegalDateException;
import xyz.drugalev.domain.exception.NegativeArgumentException;
import xyz.drugalev.domain.service.TrainingService;
import xyz.drugalev.domain.service.TrainingTypeService;
import xyz.drugalev.usecase.training.AddTrainingDataUseCase;
import xyz.drugalev.usecase.training.AddTrainingUseCase;
import xyz.drugalev.usecase.training.DeleteTrainingUseCase;
import xyz.drugalev.usecase.training.FindTrainingUseCase;
import xyz.drugalev.usecase.training.GetTrainingStatsUseCase;
import xyz.drugalev.usecase.training.UpdateTrainingUseCase;
import xyz.drugalev.usecase.trainingtype.AddTrainingTypeUseCase;
import xyz.drugalev.usecase.trainingtype.FindTrainingTypeUseCase;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * The Training controller.
 */
public class TrainingController {

    /**
     * Add training screen.
     *
     * @param trainingService     the training service
     * @param trainingTypeService the training type service
     * @param currentUser         the current user
     * @param inputUtil           the input util
     * @throws SQLException              the sql exception
     * @throws NegativeArgumentException the negative argument exception
     * @throws IllegalDateException      the illegal date exception
     */
    public static void addTrainingScreen(TrainingService trainingService, TrainingTypeService trainingTypeService, User currentUser, InputUtil inputUtil) throws SQLException, NegativeArgumentException, IllegalDateException {
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

        System.out.println("Training date");
        LocalDate date = inputUtil.getLocalDate();

        System.out.print("Training duration");
        int duration = inputUtil.getInt();
        System.out.print("Burned Calories");
        int burnedCalories = inputUtil.getInt();

        AddTrainingUseCase addTrainingUseCase = new AddTrainingUseCase(trainingService);
        addTrainingUseCase.add(currentUser, date, selectedType, duration, burnedCalories);

        Audit.getInstance().log(currentUser, "adding training");
    }

    /**
     * Gets all user trainings screen.
     *
     * @param trainingService the training service
     * @param currentUser     the current user
     * @param inputUtil       the input util
     * @throws SQLException              the sql exception
     * @throws NegativeArgumentException the negative argument exception
     */
    public static void getAllUserTrainingsScreen(TrainingService trainingService, User currentUser, InputUtil inputUtil) throws SQLException, NegativeArgumentException {
        FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);
        List<Training> trainings = useCase.findAllByUser(currentUser);
        Audit.getInstance().log(currentUser, "requesting all his trainings");
        viewTrainingsScreen(trainingService, trainings, currentUser, inputUtil);
    }

    /**
     * Gets trainings in period screen.
     *
     * @param trainingService the training service
     * @param currentUser     the current user
     * @param inputUtil       the input util
     * @throws SQLException              the sql exception
     * @throws NegativeArgumentException the negative argument exception
     */
    public static void getTrainingsInPeriodScreen(TrainingService trainingService, User currentUser, InputUtil inputUtil) throws SQLException, NegativeArgumentException {
        FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);
        LocalDate start = inputUtil.getLocalDate();
        LocalDate end = inputUtil.getLocalDate();
        List<Training> trainings = useCase.findBetween(currentUser, start, end);
        Audit.getInstance().log(currentUser, "requesting all his trainings in period");
        viewTrainingsScreen(trainingService, trainings, currentUser, inputUtil);
    }

    /**
     * Gets stats in period screen.
     *
     * @param trainingService the training service
     * @param currentUser     the current user
     * @param inputUtil       the input util
     * @throws SQLException the sql exception
     */
    public static void getStatsInPeriodScreen(TrainingService trainingService, User currentUser, InputUtil inputUtil) throws SQLException {
        GetTrainingStatsUseCase useCase = new GetTrainingStatsUseCase(trainingService);
        LocalDate start = inputUtil.getLocalDate();
        LocalDate end = inputUtil.getLocalDate();

        Map<String, Integer> stats = useCase.getTrainingsStats(currentUser, start, end);

        System.out.printf("In %d days you trained %d min and burned %d cal\n",
                DAYS.between(start, end) + 1, stats.get("Duration"), stats.get("Calories"));

        Audit.getInstance().log(currentUser, "requesting training stats in period");
    }

    /**
     * Add training type screen.
     *
     * @param trainingTypeService the training type service
     * @param currentUser         the current user
     * @param inputUtil           the input util
     * @throws SQLException the sql exception
     */
    public static void addTrainingTypeScreen(TrainingTypeService trainingTypeService, User currentUser, InputUtil inputUtil) throws SQLException {
        System.out.print("Training Type Name");
        String trainingTypeName = inputUtil.getLine();

        AddTrainingTypeUseCase trainingTypeUseCase = new AddTrainingTypeUseCase(trainingTypeService);
        trainingTypeUseCase.add(trainingTypeName);
        Audit.getInstance().log(currentUser, "adding training type");
    }

    /**
     * Gets other users trainings screen.
     *
     * @param trainingService the training service
     * @param currentUser     the current user
     * @param inputUtil       the input util
     * @throws SQLException              the sql exception
     * @throws NegativeArgumentException the negative argument exception
     */
    public static void getOtherUsersTrainingsScreen(TrainingService trainingService, User currentUser, InputUtil inputUtil) throws SQLException, NegativeArgumentException {
            FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);
            List<Training> trainings = useCase.findAll();
            Audit.getInstance().log(currentUser, "requesting all trainings");
            viewTrainingsScreen(trainingService, trainings, currentUser, inputUtil);
    }

    private static void viewTrainingsScreen(TrainingService trainingService, List<Training> trainings, User currentUser, InputUtil inputUtil) throws SQLException, NegativeArgumentException {
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

        Audit.getInstance().log(currentUser, "requesting training details");
        viewTrainingScreen(trainingService, selectedTraining, currentUser, inputUtil);
    }


    private static void viewTrainingScreen(TrainingService trainingService, Training training, User currentUser, InputUtil inputUtil) throws SQLException, NegativeArgumentException {
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
                case Menu.MENU_EXIT -> {
                    return;
                }
                case "ADD_DATA" ->
                    addTrainingDataScreen(trainingService, training, currentUser, inputUtil);
                case "EDIT" ->
                    editTrainingScreen(trainingService, training, currentUser, inputUtil);
                case "DELETE" ->
                    deleteTrainingScreen(trainingService, training, currentUser);
            }
        }
    }

    private static void addTrainingDataScreen(TrainingService trainingService, Training training, User currentUser, InputUtil inputUtil) {
        System.out.print("Data name");
        String name = inputUtil.getLine();
        System.out.print("Data value");
        int value = inputUtil.getInt();

        try {
            AddTrainingDataUseCase useCase = new AddTrainingDataUseCase(trainingService);
            useCase.addTrainingData(training, name, value);
            Audit.getInstance().log(currentUser, "adding training data to training");
        } catch (SQLException e) {
            System.err.println("Couldn't add data: " + e.getMessage());
        }
    }

    private static void editTrainingScreen(TrainingService trainingService, Training training, User currentUser, InputUtil inputUtil) throws SQLException, NegativeArgumentException {
        System.out.print("Training duration");
        int duration = inputUtil.getInt();
        System.out.print("Burned Calories");
        int burnedCalories = inputUtil.getInt();

        UpdateTrainingUseCase updateTrainingUseCase = new UpdateTrainingUseCase(trainingService);
        updateTrainingUseCase.update(training, duration, burnedCalories);
        Audit.getInstance().log(currentUser, "updating training");
    }

    private static void deleteTrainingScreen(TrainingService trainingService, Training training, User currentUser) throws SQLException {
        DeleteTrainingUseCase useCase = new DeleteTrainingUseCase(trainingService);
        useCase.delete(training);
        Audit.getInstance().log(currentUser, "deleting training");
    }

}
