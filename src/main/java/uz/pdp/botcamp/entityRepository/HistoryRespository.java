package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.config.DbConfig;
import uz.pdp.botcamp.entity.History;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryRespository {
    static Connection connection= DbConfig.getConnection();

    public static List<History> makeList(int test_id) {
        List<History>histories=new ArrayList<>();

        try {
            CallableStatement callableStatement = connection.prepareCall("{call history(?)}");
            callableStatement.setInt(1,test_id);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()){
                History history=new History();
                history.setQuestion(resultSet.getString(1));
                history.setYour_answer(resultSet.getString(2));
                history.setRight_answer(resultSet.getString(3));
                history.setRate(resultSet.getInt(4));
                histories.add(history);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return histories;
    }
}
