package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.config.DbConfig;
import uz.pdp.botcamp.entity.QuestionForUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuessionForUserRepository {
    static Connection connection = DbConfig.getConnection();


    public static List<QuestionForUser> makeQuession(int i_subject_id, int i_quesDif_id) {
        List<QuestionForUser> question = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select q.id, q.content,right_answer,wrong_answer_1,wrong_answer_2,wrong_answer_3 from answer join question q on answer.id = q.answer_id\n" +
                    "where q.subject_id=? and q.difficulty_id=? and q.active=true order by id");
            preparedStatement.setInt(1, i_subject_id);
            preparedStatement.setInt(2, i_quesDif_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                QuestionForUser questionForUser = new QuestionForUser();
                questionForUser.setQuestion_id(resultSet.getInt(1));
                questionForUser.setQuestion(resultSet.getString(2));
                questionForUser.setRight_answer(resultSet.getString(3));
                questionForUser.setWrong_answer_1(resultSet.getString(4));
                questionForUser.setWrong_answer_2(resultSet.getString(5));
                questionForUser.setWrong_answer_3(resultSet.getString(6));
                question.add(questionForUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return question;
    }
}
