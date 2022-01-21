package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.config.DbConfig;
import uz.pdp.botcamp.entity.QuestionDifficulty;
import uz.pdp.botcamp.entity.Response;
import uz.pdp.botcamp.input.InPutScanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDifficultyrepository {
    static Connection connection= DbConfig.getConnection();
    public static List<QuestionDifficulty>questionDifficulties=new ArrayList<>();

    public static void refresh(){
        questionDifficulties.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select *from question_difficulty order by id");
            while (resultSet.next()){
                QuestionDifficulty questionDif=new QuestionDifficulty();
                questionDif.setId(resultSet.getInt(1));
                questionDif.setDifficulty(resultSet.getString(2));
                questionDifficulties.add(questionDif);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Response addDificulty(String difficulty) {
        Response response=new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call adddifficulty(?,?,?)}");
            callableStatement.setString(1,difficulty);
            callableStatement.registerOutParameter(2,Types.BOOLEAN);
            callableStatement.registerOutParameter(3,Types.VARCHAR);
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
        if (QuestionDifficultyrepository.questionDifficulties.size()>0) {
            System.out.println("All question difficulty");
            for (QuestionDifficulty questionDifficulty : QuestionDifficultyrepository.questionDifficulties) {
                System.out.println(questionDifficulty.getId() + ". " + questionDifficulty.getDifficulty());
            }
        }else {
            System.out.println("No question difficulty yet");
        }
        System.out.println("* Write exit if you wat back");
        System.out.println("Enter new question difficulty name");
        String difficulty= InPutScanner.SCANNERSTR.nextLine();
        if(!difficulty.equals("exit")){
            Response response = QuestionDifficultyrepository.addDificulty(difficulty);
            System.out.println(response.getMessage());
        }
    }
}