package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.config.DbConfig;
import uz.pdp.botcamp.entity.Question;
import uz.pdp.botcamp.entity.Response;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {
   static Connection connection= DbConfig.getConnection();
   public static List<Question>questions=new ArrayList<>();


   public static void refresh(){
       questions.clear();
       try {
           Statement statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery("select *from question order by id");
          while (resultSet.next()){
              Question question=new Question();
              question.setId(resultSet.getInt(1));
              question.setContent(resultSet.getString(2));
              question.setSubject_id(resultSet.getInt(3));
              question.setDifficulty_id(resultSet.getInt(4));
              question.setAnswer_id(resultSet.getInt(5));
              question.setActive(resultSet.getBoolean(6));
              questions.add(question);
          }
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }


    public static Response addQustionAndAnswer(String content, int subject_id, int quesDif_id,
                                               String right_answer, String wrong_answer_1,
                                               String wrong_answer_2,String wrong_answer_3) {
        Response response=new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call add_question_test(?,?,?,?,?,?,?,?,?)}");
            callableStatement.setString(1,content);
            callableStatement.setInt(2,subject_id);
            callableStatement.setInt(3,quesDif_id);
            callableStatement.setString(4,right_answer);
            callableStatement.setString(5,wrong_answer_1);
            callableStatement.setString(6,wrong_answer_2);
            callableStatement.setString(7,wrong_answer_3);
            callableStatement.registerOutParameter(8, Types.BOOLEAN);
            callableStatement.registerOutParameter(9, Types.VARCHAR);
            callableStatement.execute();
            response.setSuccess(callableStatement.getBoolean(8));
            response.setMessage(callableStatement.getString(9));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        refresh();
        AnswerRepository.refresh();

        return response;
    }
}
