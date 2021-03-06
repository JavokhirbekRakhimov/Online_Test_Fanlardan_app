package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.config.DbConfig;
import uz.pdp.botcamp.entity.QuestionDifficulty;
import uz.pdp.botcamp.entity.Response;
import uz.pdp.botcamp.input.InPutScanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDifficultyrepository {
    static Connection connection = DbConfig.getConnection();
    public static List<QuestionDifficulty> questionDifficulties = new ArrayList<>();
    public static List<QuestionDifficulty> deleteQuestionDifficulties = new ArrayList<>();

    public static void refresh() {
        questionDifficulties.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select *from question_difficulty where active=true order by id");
            while (resultSet.next()) {
                QuestionDifficulty questionDif = new QuestionDifficulty();
                questionDif.setId(resultSet.getInt(1));
                questionDif.setDifficulty(resultSet.getString(2));
                questionDifficulties.add(questionDif);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void deleteRefresh() {
        deleteQuestionDifficulties.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select *from question_difficulty where active=false order by id");
            while (resultSet.next()) {
                QuestionDifficulty questionDif = new QuestionDifficulty();
                questionDif.setId(resultSet.getInt(1));
                questionDif.setDifficulty(resultSet.getString(2));
                deleteQuestionDifficulties.add(questionDif);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static Response addDificulty(String difficulty) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call adddifficulty(?,?,?)}");
            callableStatement.setString(1, difficulty);
            callableStatement.registerOutParameter(2, Types.BOOLEAN);
            callableStatement.registerOutParameter(3, Types.VARCHAR);
            callableStatement.execute();
            response.setSuccess(callableStatement.getBoolean(2));
            response.setMessage(callableStatement.getString(3));
            refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return response;
    }

    public static void createDifficulty() {
        if (QuestionDifficultyrepository.questionDifficulties.size() > 0) {
            System.out.println("All question difficulty");
            for (QuestionDifficulty questionDifficulty : QuestionDifficultyrepository.questionDifficulties) {
                System.out.println(questionDifficulty.getId() + ". " + questionDifficulty.getDifficulty());
            }
        } else {
            System.out.println("No question difficulty yet");
        }
        System.out.println("* Write exit if you wat back");
        System.out.println("Enter new question difficulty name");
        String difficulty = InPutScanner.SCANNERSTR.nextLine();
        if (!difficulty.equals("exit")) {
            Response response = QuestionDifficultyrepository.addDificulty(difficulty);
            System.out.println(response.getMessage());
        }
    }

    public static Response changDifficulty() {
        Response response = new Response();
        List<Integer> index = new ArrayList<>();
        int dif_id;
        if (QuestionDifficultyrepository.questionDifficulties.size() > 0) {
            System.out.println("All question difficulty");
            for (QuestionDifficulty questionDifficulty : QuestionDifficultyrepository.questionDifficulties) {
                System.out.println("Id=> " + questionDifficulty.getId() + ". " + questionDifficulty.getDifficulty());
                index.add(questionDifficulty.getId());
            }
        } else {
            System.out.println("No question difficulty yet");
        }
        System.out.println("* Write 0 if you wat back");
        System.out.println("Enter new question difficulty name");
        dif_id = InPutScanner.SCANNERNUM.nextInt();
        if (dif_id != 0) {
            if (index.contains(dif_id)) {
                System.out.print("Enter new name: ");
                String newName = InPutScanner.SCANNERSTR.nextLine();
                return updateNewDif(dif_id, newName);
            } else {
                System.out.println("Wrong Id");
            }
        }

        return response;
    }

    private static Response updateNewDif(int dif_id, String newName) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call update_difficulty(?,?,?,?)}");
            callableStatement.setInt(1, dif_id);
            callableStatement.setString(2, newName);
            callableStatement.registerOutParameter(3, Types.BOOLEAN);
            callableStatement.registerOutParameter(4, Types.VARCHAR);
            callableStatement.execute();
            response.setSuccess(callableStatement.getBoolean(3));
            response.setMessage(callableStatement.getString(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        refresh();
        return response;
    }

    public static Response update_question_difficulty(int quession_id, int edit_new_dif) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call update_quession_diffuculty(?,?,?,?)}");
            callableStatement.setInt(1, quession_id);
            callableStatement.setInt(2, edit_new_dif);
            callableStatement.registerOutParameter(3, Types.BOOLEAN);
            callableStatement.registerOutParameter(4, Types.VARCHAR);
            callableStatement.execute();
            response.setSuccess(callableStatement.getBoolean(3));
            response.setMessage(callableStatement.getString(4));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        refresh();
        return response;
    }

    public static Response deleteDifficulty() {
        Response response = new Response();
        List<Integer> index = new ArrayList<>();
        index.add(0);
        int dif_id;
        System.out.println("-------------------------------------------------");
        if (QuestionDifficultyrepository.questionDifficulties.size() > 0) {
            System.out.println("All question difficulty");
            for (QuestionDifficulty questionDifficulty : QuestionDifficultyrepository.questionDifficulties) {
                System.out.println("Id=> " + questionDifficulty.getId() + ". " + questionDifficulty.getDifficulty());
                index.add(questionDifficulty.getId());
            }
        } else {
            System.out.println("No question difficulty yet");
        }

            System.out.println("* Write 0 if you wat back");
            System.out.println("Enter new question difficulty id");
            dif_id = InPutScanner.SCANNERNUM.nextInt();
            System.out.println("-------------------------------------------------");

        if (dif_id != 0) {
            if(index.contains(dif_id)) {
                return deleteDifficultyById(dif_id);
            }else {
                System.out.println("Wrong id");
            }

        }

        return response;
    }

    private static Response deleteDifficultyById(int dif_id) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call delete_difficulty(?,?,?)}");
            callableStatement.setInt(1, dif_id);
            callableStatement.registerOutParameter(2, Types.BOOLEAN);
            callableStatement.registerOutParameter(3, Types.VARCHAR);
            callableStatement.execute();
            response.setSuccess(callableStatement.getBoolean(2));
            response.setMessage(callableStatement.getString(3));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        refresh();
        return response;
    }

    public static void rollbackDifficulty() {
        Response response = new Response();
        List<Integer> index = new ArrayList<>();
        int dif_id;
        System.out.println("-------------------------------------------------");
        if (deleteQuestionDifficulties.size() > 0) {
            System.out.println("All question difficulty");
            for (QuestionDifficulty questionDifficulty : deleteQuestionDifficulties) {
                System.out.println("Id=> " + questionDifficulty.getId() + ". " + questionDifficulty.getDifficulty());
                index.add(questionDifficulty.getId());
            }
        } else {
            System.out.println("No question difficulty yet");
        }
        System.out.println("* Write 0 if you wat back");
        System.out.println("Enter new question difficulty id");
        dif_id = InPutScanner.SCANNERNUM.nextInt();
        System.out.println("-------------------------------------------------");
        if (dif_id != 0) {
            if(index.contains(dif_id)) {
                response=rollbackDifficultyById(dif_id);
            }else {
                System.out.println("Wrong id");
            }

        }

        System.out.println(response.getMessage());
    }

    private static Response rollbackDifficultyById(int dif_id) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call rollback_difficulty(?,?,?)}");
            callableStatement.setInt(1, dif_id);
            callableStatement.registerOutParameter(2, Types.BOOLEAN);
            callableStatement.registerOutParameter(3, Types.VARCHAR);
            callableStatement.execute();
            response.setSuccess(callableStatement.getBoolean(2));
            response.setMessage(callableStatement.getString(3));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        refresh();
        deleteRefresh();
        return response;
    }
}
