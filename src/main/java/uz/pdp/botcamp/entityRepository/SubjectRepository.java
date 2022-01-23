package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.config.DbConfig;
import uz.pdp.botcamp.entity.QuestionDifficulty;
import uz.pdp.botcamp.entity.Response;
import uz.pdp.botcamp.entity.Subject;
import uz.pdp.botcamp.input.InPutScanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectRepository {
    public static List<Subject> subjects = new ArrayList<>();
    public static List<Subject> deleteSubjects = new ArrayList<>();
    static Connection connection = DbConfig.getConnection();

    public static void refresh() {
        subjects.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select *from subject where active=true order by id");
            while (resultSet.next()) {
                Subject subject = new Subject();
                subject.setId(resultSet.getInt(1));
                subject.setName(resultSet.getString(2));
                subject.setActive(resultSet.getBoolean(3));
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void refreshDelete() {
        deleteSubjects.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select *from subject where active=false order by id");
            while (resultSet.next()) {
                Subject subject = new Subject();
                subject.setId(resultSet.getInt(1));
                subject.setName(resultSet.getString(2));
                subject.setActive(resultSet.getBoolean(3));
                deleteSubjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Response addSubject(String subject_name) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call addsubject(?,?,?)}");
            callableStatement.setString(1, subject_name);
            callableStatement.registerOutParameter(2, Types.BOOLEAN);
            callableStatement.registerOutParameter(3, Types.VARCHAR);
            callableStatement.execute();
            response.setSuccess(callableStatement.getBoolean(2));
            response.setMessage(callableStatement.getString(3));
            refresh();
            return response;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void addNewSubject() {
        if (SubjectRepository.subjects.size() > 0) {
            System.out.println("All subjects");
            for (Subject subject : SubjectRepository.subjects) {
                System.out.println(subject.getId() + ". " + subject.getName());
            }
        } else {
            System.out.println("No subject yet");
        }
        System.out.println("* Write exit if you wat back");
        System.out.println("Enter new Subject name");
        String subject = InPutScanner.SCANNERSTR.nextLine();
        if (!subject.equals("exit")) {
            Response response = SubjectRepository.addSubject(subject);
            System.out.println(response.getMessage());
        }
    }

    public static Response changeSubject() {
        Response response = new Response();
        response.setMessage("Something wrong");
        List<Integer> index = new ArrayList<>();
        if (SubjectRepository.subjects.size() > 0) {
            System.out.println("All subjects");
            for (Subject subject : SubjectRepository.subjects) {
                System.out.println("Id => " + subject.getId() + ". " + subject.getName());
                index.add(subject.getId());
            }
        } else {
            System.out.println("No subject yet");
        }
        System.out.println("* Write 0 if you wat back");
        System.out.print("Enter Subject Id:");
        int subjectId = InPutScanner.SCANNERNUM.nextInt();
        if (subjectId != 0) {
            if (index.contains(subjectId)) {
                System.out.print("Enter new subject name : ");
                String newSubjectName = InPutScanner.SCANNERSTR.nextLine();
                response = updateSubject(subjectId, newSubjectName);
            } else {
                System.out.println("Wrong Id yu entered");
            }
        }
        return response;
    }

    private static Response updateSubject(int subjectId, String newSubjectName) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call update_subject(?,?,?,?)}");
            callableStatement.setInt(1, subjectId);
            callableStatement.setString(2, newSubjectName);
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

    public static Response deleteSubject() {
        System.out.println("-------------------------------------------------");
        Response response = new Response();
        List<Integer> index = new ArrayList<>();
        if (SubjectRepository.subjects.size() > 0) {
            System.out.println("All subjects");
            for (Subject subject : SubjectRepository.subjects) {
                System.out.println("Id => " + subject.getId() + ". " + subject.getName());
                index.add(subject.getId());
            }
        } else {
            System.out.println("No subject yet");
        }
        System.out.println("* Write 0 if you wat back");
        System.out.print("Enter Subject Id:");
        int subjectId = InPutScanner.SCANNERNUM.nextInt();
        System.out.println("-------------------------------------------------");
        if (subjectId != 0) {
            if (index.contains(subjectId)) {
                response = deleteSubjectById(subjectId);
            } else {
                System.out.println("Wrong Id yu entered");
            }
        }
        return response;
    }

    private static Response deleteSubjectById(int subjectId) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call delete_subject(?,?,?)}");
            callableStatement.setInt(1, subjectId);
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

    public static void rollbackSubject() {
        System.out.println("-------------------------------------------------");
        Response response = new Response();
        List<Integer> index = new ArrayList<>();
        if (SubjectRepository.deleteSubjects.size() > 0) {
            System.out.println("All delete subjects");
            for (Subject subject : SubjectRepository.deleteSubjects) {
                System.out.println("Id => " + subject.getId() + ". " + subject.getName());
                index.add(subject.getId());
            }
        } else {
            System.out.println("No subject yet");
        }
        System.out.println("* Write 0 if you wat back");
        System.out.print("Enter Subject Id:");
        int subjectId = InPutScanner.SCANNERNUM.nextInt();
        System.out.println("-------------------------------------------------");
        if (subjectId != 0) {
            if (index.contains(subjectId)) {
                response = rollbackSubjectById(subjectId);
            } else {
                System.out.println("Wrong Id yu entered");
            }
        }
        System.out.println(response.getMessage());

    }

    private static Response rollbackSubjectById(int subjectId) {
        Response response=new Response();

        try {
            CallableStatement callableStatement = connection.prepareCall("{call rollback_subject(?,?,?)}");
            callableStatement.setInt(1, subjectId);
            callableStatement.registerOutParameter(2, Types.BOOLEAN);
            callableStatement.registerOutParameter(3, Types.VARCHAR);
            callableStatement.execute();
            response.setSuccess(callableStatement.getBoolean(2));
            response.setMessage(callableStatement.getString(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        refresh();
        refreshDelete();
        return response;
    }
}
