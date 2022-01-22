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

    public static void createSubject() {
        int subject_id;
        int quesDif_id;
        String content;
        String right_answer;
        String wrong_answer_1;
        String wrong_answer_2;
        String wrong_answer_3;
        if (SubjectRepository.subjects.size() > 0) {
            System.out.println("All subjects");
            for (Subject subject : SubjectRepository.subjects) {
                System.out.println(subject.getId() + ". " + subject.getName());
            }

            System.out.print("Enter subject number:");
            subject_id = InPutScanner.SCANNERNUM.nextInt();

            if (QuestionDifficultyrepository.questionDifficulties.size() > 0) {
                System.out.println("All question difficulty");
                for (QuestionDifficulty questionDifficulty : QuestionDifficultyrepository.questionDifficulties) {
                    System.out.println(questionDifficulty.getId() + ". " + questionDifficulty.getDifficulty());
                }
                System.out.print("Enter difficulty number:");
                quesDif_id = InPutScanner.SCANNERNUM.nextInt();

                System.out.println("Enter question:");
                content = InPutScanner.SCANNERSTR.nextLine();
                System.out.println("Enter right answer first:");
                right_answer = InPutScanner.SCANNERSTR.nextLine();
                System.out.println("Enter wrong answer 1: ");
                wrong_answer_1 = InPutScanner.SCANNERSTR.nextLine();

                System.out.println("Enter wrong answer 2: ");
                wrong_answer_2 = InPutScanner.SCANNERSTR.nextLine();

                System.out.println("Enter wrong answer 3: ");
                wrong_answer_3 = InPutScanner.SCANNERSTR.nextLine();

                Response response = QuestionRepository.addQustionAndAnswer(content, subject_id, quesDif_id,
                        right_answer, wrong_answer_1, wrong_answer_2, wrong_answer_3);
                System.out.println(response.getMessage());

            } else {
                System.out.println("No question difficulty yet");
            }


        } else {
            System.out.println("No subject yet");
        }
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
}
