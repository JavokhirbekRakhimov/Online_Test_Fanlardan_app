package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.config.DbConfig;
import uz.pdp.botcamp.entity.Answer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AnswerRepository {
    static Connection connection= DbConfig.getConnection();

    public static List<Answer>answers=new ArrayList<>();

    public static void refresh(){
        answers.clear();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select *from answer order by id");

            while (resultSet.next()){
                Answer answer=new Answer();
                answer.setId(resultSet.getInt(1));
                answer.setRight_answer(resultSet.getString(2));
                answer.setWrong_answer_1(resultSet.getString(3));
                answer.setWrong_answer_2(resultSet.getString(4));
                answer.setWrong_answer_3(resultSet.getString(5));
                answers.add(answer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
