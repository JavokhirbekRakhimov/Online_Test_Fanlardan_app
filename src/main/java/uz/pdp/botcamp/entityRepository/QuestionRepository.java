package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.config.DbConfig;
import uz.pdp.botcamp.entity.*;
import uz.pdp.botcamp.input.InPutScanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {
    static Connection connection = DbConfig.getConnection();
    public static List<Question> questions = new ArrayList<>();
    public static List<Question> deleteQuestions = new ArrayList<>();


    public static void refresh() {
        questions.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select *from question where active = true order by id");
            while (resultSet.next()) {
                Question question = new Question();
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
    public static void deleteRefresh() {
        deleteQuestions.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select *from question where active = true order by id");
            while (resultSet.next()) {
                Question question = new Question();
                question.setId(resultSet.getInt(1));
                question.setContent(resultSet.getString(2));
                question.setSubject_id(resultSet.getInt(3));
                question.setDifficulty_id(resultSet.getInt(4));
                question.setAnswer_id(resultSet.getInt(5));
                question.setActive(resultSet.getBoolean(6));
                deleteQuestions.add(question);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Response addQustionAndAnswer(String content, int subject_id, int quesDif_id,
                                               String right_answer, String wrong_answer_1,
                                               String wrong_answer_2, String wrong_answer_3) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call add_question_test(?,?,?,?,?,?,?,?,?)}");
            callableStatement.setString(1, content);
            callableStatement.setInt(2, subject_id);
            callableStatement.setInt(3, quesDif_id);
            callableStatement.setString(4, right_answer);
            callableStatement.setString(5, wrong_answer_1);
            callableStatement.setString(6, wrong_answer_2);
            callableStatement.setString(7, wrong_answer_3);
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

    public static Response changeQuestion() {
        Response response = new Response();
        int quession_id;
        int subject_id;
        int quesDif_id;
        if (SubjectRepository.subjects.size() > 0) {
            System.out.println("-----------------------------------------------------------------");
            System.out.println("All subjects");
            for (Subject subject : SubjectRepository.subjects) {
                System.out.println(subject.getId() + ". " + subject.getName());
            }
            System.out.print("Enter subject number:");
            subject_id = InPutScanner.SCANNERNUM.nextInt();
            System.out.println("-----------------------------------------------------------------");


            if (QuestionDifficultyrepository.questionDifficulties.size() > 0) {
                System.out.println("All question difficulty");
                for (QuestionDifficulty questionDifficulty : QuestionDifficultyrepository.questionDifficulties) {
                    System.out.println(questionDifficulty.getId() + ". " + questionDifficulty.getDifficulty());
                }
                quesDif_id = InPutScanner.SCANNERNUM.nextInt();
                System.out.println("-----------------------------------------------------------------");

                List<QuestionForUser> questionForUserList;
                questionForUserList = QuessionForUserRepository.makeQuession(subject_id, quesDif_id);

                List<Integer> idNextQuestion = new ArrayList<>();
                System.out.println("-----------------------------------------------------------------");
                for (QuestionForUser questionForUser : questionForUserList) {
                    System.out.println("* Id |=> " + questionForUser.getQuestion_id());
                    System.out.println("* Question:       |=> " + questionForUser.getQuestion());
                    System.out.println("* Right answer:   |=> " + questionForUser.getRight_answer());
                    System.out.println("* Wrong answer 1: |=> " + questionForUser.getWrong_answer_1());
                    System.out.println("* Wrong answer 2: |=> " + questionForUser.getWrong_answer_2());
                    System.out.println("* Wrong answer 3: |=> " + questionForUser.getWrong_answer_3());
                    System.out.println("-----------------------------------------------------------------");
                    idNextQuestion.add(questionForUser.getQuestion_id());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                do {

                    System.out.print("Enter Id: ");
                    quession_id = InPutScanner.SCANNERNUM.nextInt();
                } while (!idNextQuestion.contains(quession_id));
                System.out.println("-----------------------------------------------------------------");
                int option;
                do {
                    questionForUserList = QuessionForUserRepository.makeQuession(subject_id, quesDif_id);
                    showQuestionById(subject_id, quession_id, quesDif_id, questionForUserList);
                    System.out.println("1.Update content");
                    System.out.println("2.Update right answer");
                    System.out.println("3.Update wrong answer 1");
                    System.out.println("4.Update wrong answer 2");
                    System.out.println("5.Update wrong answer 3");
                    System.out.println("6.Update difficulty");
                    System.out.println("7.Update subject");
                    System.out.println("0.Exit");
                    System.out.print("Enter number: ");
                    option = InPutScanner.SCANNERNUM.nextInt();
                    switch (option) {
                        case 1 -> {
                            System.out.print("Enter new content:");
                            String newContent = InPutScanner.SCANNERSTR.nextLine();
                            Response response1 = updateContent(quession_id, newContent);
                            System.out.println(response1.getMessage());
                        }
                        case 2 -> {
                            String rightAnswer;
                            List<String> answer = new ArrayList<>();
                            for (QuestionForUser questionForUser : questionForUserList) {
                                if (questionForUser.getQuestion_id() == quession_id) {
                                    answer.add(questionForUser.getRight_answer());
                                }
                            }
                            do {
                                System.out.print("Enter new right answer:");
                                rightAnswer = InPutScanner.SCANNERSTR.nextLine();
                                if (answer.contains(rightAnswer)) {
                                    System.out.println("This already exist");
                                }
                            } while (answer.contains(rightAnswer));
                            Response response1 = updateRightAnswer(quession_id, rightAnswer);
                            System.out.println(response1.getMessage());
                        }
                        case 3 -> {
                            String wrongAnswer1;
                            List<String> answer = new ArrayList<>();
                            for (QuestionForUser questionForUser : questionForUserList) {
                                if (questionForUser.getQuestion_id() == quession_id) {
                                    answer.add(questionForUser.getWrong_answer_1());
                                }
                            }
                            do {

                                System.out.print("Enter new wrong answer 1 :");
                                wrongAnswer1 = InPutScanner.SCANNERSTR.nextLine();
                                if (answer.contains(wrongAnswer1)) {
                                    System.out.println("This already exist");
                                }
                            } while (answer.contains(wrongAnswer1));
                            Response response1 = updateWrongAnswer1(quession_id, wrongAnswer1);
                            System.out.println(response1.getMessage());
                        }
                        case 4 -> {
                            String wrongAnswer2;
                            List<String> answer = new ArrayList<>();
                            for (QuestionForUser questionForUser : questionForUserList) {
                                if (questionForUser.getQuestion_id() == quession_id) {
                                    answer.add(questionForUser.getWrong_answer_2());
                                }
                            }
                            do {

                                System.out.print("Enter new wrong answer 2 :");
                                wrongAnswer2 = InPutScanner.SCANNERSTR.nextLine();
                                if (answer.contains(wrongAnswer2)) {
                                    System.out.println("This already exist");
                                }
                            } while (answer.contains(wrongAnswer2));
                            Response response1 = updateWrongAnswer2(quession_id, wrongAnswer2);
                            System.out.println(response1.getMessage());
                        }
                        case 5 -> {
                            String wrongAnswer3;
                            List<String> answer = new ArrayList<>();
                            for (QuestionForUser questionForUser : questionForUserList) {
                                if (questionForUser.getQuestion_id() == quession_id) {
                                    answer.add(questionForUser.getWrong_answer_3());
                                }
                            }
                            do {

                                System.out.print("Enter new wrong answer 3 :");
                                wrongAnswer3 = InPutScanner.SCANNERSTR.nextLine();
                                if (answer.contains(wrongAnswer3)) {
                                    System.out.println("This already exist");
                                }
                            } while (answer.contains(wrongAnswer3));
                            Response response1 = updateWrongAnswer3(quession_id, wrongAnswer3);
                            System.out.println(response1.getMessage());
                        }
                        case 6 -> {
                            System.out.println("All question difficulty");
                            for (QuestionDifficulty questionDifficulty : QuestionDifficultyrepository.questionDifficulties) {
                                if (questionDifficulty.getId() != quesDif_id) {
                                    System.out.println(questionDifficulty.getId() + ". " + questionDifficulty.getDifficulty());
                                }
                            }
                            System.out.print("Enter number: ");
                            quesDif_id = InPutScanner.SCANNERNUM.nextInt();
                            Response response1 = QuestionDifficultyrepository.update_question_difficulty(quession_id, quesDif_id);
                            System.out.println(response1.getMessage());

                        }
                        case 7 -> {
                            for (Subject subject : SubjectRepository.subjects) {
                                if (subject.getId() != subject_id) {
                                    System.out.println("Id => " + subject.getId() + ".  " + subject.getName());
                                }
                            }
                            System.out.print("Enter subject id: ");
                            subject_id = InPutScanner.SCANNERNUM.nextInt();
                            Response response1 = QuestionRepository.update_questionSubject(quession_id, subject_id);
                            System.out.println(response1.getMessage());
                        }
                    }
                } while (option != 0);

            } else {
                System.out.println("No question difficulty yet");
            }


        } else {
            System.out.println("No subject yet");
        }

        return response;
    }

    private static Response update_questionSubject(int quession_id, int subject_id) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call update_quession_subjecty(?,?,?,?)}");
            callableStatement.setInt(1, quession_id);
            callableStatement.setInt(2, subject_id);
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


    private static void showQuestionById(int subject_id, int quessionId, int quesDif_id, List<QuestionForUser> questionForUserList) {

        for (QuestionForUser questionForUser : questionForUserList) {
            if (questionForUser.getQuestion_id() == quessionId) {
                if (questionForUser.getQuestion_id() == subject_id) {
                    System.out.println("* Id |=> " + questionForUser.getQuestion_id());
                    for (Subject subject : SubjectRepository.subjects) {
                        if (subject.getId() == subject_id) {
                            System.out.println("* Subject:        |=> " + subject.getName());
                            break;
                        }
                    }
                    for (QuestionDifficulty questionDifficulty : QuestionDifficultyrepository.questionDifficulties) {
                        if (questionDifficulty.getId() == quesDif_id) {
                            System.out.println("* Difficulty:     |=> " + questionDifficulty.getDifficulty());
                            break;
                        }
                    }
                    System.out.println("* Question:       |=> " + questionForUser.getQuestion());
                    System.out.println("* Right answer:   |=> " + questionForUser.getRight_answer());
                    System.out.println("* Wrong answer 1: |=> " + questionForUser.getWrong_answer_1());
                    System.out.println("* Wrong answer 2: |=> " + questionForUser.getWrong_answer_2());
                    System.out.println("* Wrong answer 3: |=> " + questionForUser.getWrong_answer_3());
                    System.out.println("-----------------------------------------------------------------");

                    break;
                }
            }
        }
    }

    private static Response updateWrongAnswer3(int quession_id, String wrongAnswer3) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call update_wronganswer3(?,?,?,?)}");
            callableStatement.setInt(1, quession_id);
            callableStatement.setString(2, wrongAnswer3);
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

    private static Response updateWrongAnswer2(int quession_id, String wrongAnswer2) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call update_wronganswer2(?,?,?,?)}");
            callableStatement.setInt(1, quession_id);
            callableStatement.setString(2, wrongAnswer2);
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

    private static Response updateWrongAnswer1(int quession_id, String wrongAnswer1) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call update_wronganswer1(?,?,?,?)}");
            callableStatement.setInt(1, quession_id);
            callableStatement.setString(2, wrongAnswer1);
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

    private static Response updateRightAnswer(int quession_id, String rightAnswer) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call update_rightanswer(?,?,?,?)}");
            callableStatement.setInt(1, quession_id);
            callableStatement.setString(2, rightAnswer);
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

    private static Response updateContent(int quession_id, String newContent) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call update_content(?,?,?,?)}");
            callableStatement.setInt(1, quession_id);
            callableStatement.setString(2, newContent);
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

    public static Response deleteQuestion() {
        Response response = new Response();
        int quession_id;
        int subject_id;
        int quesDif_id;
        if (SubjectRepository.subjects.size() > 0) {
            System.out.println("-----------------------------------------------------------------");
            System.out.println("All subjects");
            for (Subject subject : SubjectRepository.subjects) {
                System.out.println(subject.getId() + ". " + subject.getName());
            }
            System.out.print("Enter subject number:");
            subject_id = InPutScanner.SCANNERNUM.nextInt();
            System.out.println("-----------------------------------------------------------------");


            if (QuestionDifficultyrepository.questionDifficulties.size() > 0) {
                System.out.println("All question difficulty");
                for (QuestionDifficulty questionDifficulty : QuestionDifficultyrepository.questionDifficulties) {
                    System.out.println(questionDifficulty.getId() + ". " + questionDifficulty.getDifficulty());
                }
                quesDif_id = InPutScanner.SCANNERNUM.nextInt();
                System.out.println("-----------------------------------------------------------------");

                List<QuestionForUser> questionForUserList;
                questionForUserList = QuessionForUserRepository.makeQuession(subject_id, quesDif_id);

                List<Integer> idNextQuestion = new ArrayList<>();
                System.out.println("-----------------------------------------------------------------");
                for (QuestionForUser questionForUser : questionForUserList) {
                    System.out.println("* Id |=> " + questionForUser.getQuestion_id());
                    System.out.println("* Question:       |=> " + questionForUser.getQuestion());
                    System.out.println("* Right answer:   |=> " + questionForUser.getRight_answer());
                    System.out.println("* Wrong answer 1: |=> " + questionForUser.getWrong_answer_1());
                    System.out.println("* Wrong answer 2: |=> " + questionForUser.getWrong_answer_2());
                    System.out.println("* Wrong answer 3: |=> " + questionForUser.getWrong_answer_3());
                    System.out.println("-----------------------------------------------------------------");
                    idNextQuestion.add(questionForUser.getQuestion_id());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                do {
                    System.out.println("Enter 0 if you back");
                    System.out.print("Enter Id: ");
                    quession_id = InPutScanner.SCANNERNUM.nextInt();
                } while (!idNextQuestion.contains(quession_id));
                System.out.println("-----------------------------------------------------------------");
                int option = InPutScanner.SCANNERNUM.nextInt();
                if (option > 0) {
                    response = deleteQuestionById(quession_id);
                }

            } else {
                System.out.println("No question difficulty yet");
            }


        } else {
            System.out.println("No subject yet");
        }

        return response;
    }

    private static Response deleteQuestionById(int question_id) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call delete_question(?,?,?)}");
            callableStatement.setInt(1, question_id);
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

    public static void rollbackQuestion() {
        Response response = new Response();
        int quession_id;
        int subject_id;
        int quesDif_id;
        if (SubjectRepository.subjects.size() > 0) {
            System.out.println("-----------------------------------------------------------------");
            System.out.println("All subjects");
            for (Subject subject : SubjectRepository.subjects) {
                System.out.println(subject.getId() + ". " + subject.getName());
            }
            System.out.print("Enter subject number:");
            subject_id = InPutScanner.SCANNERNUM.nextInt();
            System.out.println("-----------------------------------------------------------------");


            if (QuestionDifficultyrepository.questionDifficulties.size() > 0) {
                System.out.println("All question difficulty");
                for (QuestionDifficulty questionDifficulty : QuestionDifficultyrepository.questionDifficulties) {
                    System.out.println(questionDifficulty.getId() + ". " + questionDifficulty.getDifficulty());
                }
                quesDif_id = InPutScanner.SCANNERNUM.nextInt();
                System.out.println("-----------------------------------------------------------------");

                List<QuestionForUser> questionForUserList;
                questionForUserList = QuessionForUserRepository.makeQuession(subject_id, quesDif_id);

                List<Integer> idNextQuestion = new ArrayList<>();
                System.out.println("-----------------------------------------------------------------");
                for (QuestionForUser questionForUser : questionForUserList) {
                    System.out.println("* Id |=> " + questionForUser.getQuestion_id());
                    System.out.println("* Question:       |=> " + questionForUser.getQuestion());
                    System.out.println("* Right answer:   |=> " + questionForUser.getRight_answer());
                    System.out.println("* Wrong answer 1: |=> " + questionForUser.getWrong_answer_1());
                    System.out.println("* Wrong answer 2: |=> " + questionForUser.getWrong_answer_2());
                    System.out.println("* Wrong answer 3: |=> " + questionForUser.getWrong_answer_3());
                    System.out.println("-----------------------------------------------------------------");
                    idNextQuestion.add(questionForUser.getQuestion_id());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                do {
                    System.out.println("Enter 0 if you back");
                    System.out.print("Enter Id: ");
                    quession_id = InPutScanner.SCANNERNUM.nextInt();
                } while (!idNextQuestion.contains(quession_id));
                System.out.println("-----------------------------------------------------------------");
                int option = InPutScanner.SCANNERNUM.nextInt();
                if (option > 0) {
                    response = rollbackQuestionById(quession_id);
                }

            } else {
                System.out.println("No question difficulty yet");
            }


        } else {
            System.out.println("No subject yet");
        }

        System.out.println(response.getMessage());
    }

    private static Response rollbackQuestionById(int quession_id) {
        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call rollback_question(?,?,?)}");
            callableStatement.setInt(1, quession_id);
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

    public static void addQuestion() {
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
}
