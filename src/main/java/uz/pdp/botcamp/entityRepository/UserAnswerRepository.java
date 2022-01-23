package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.config.DbConfig;
import uz.pdp.botcamp.entity.*;
import uz.pdp.botcamp.input.InPutScanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserAnswerRepository {
    static Connection connection = DbConfig.getConnection();

    public static Response addNewTest(int test_id, int quession_id, String user_answer, String right_answer) {

        Response response = new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call add_usertest(?,?,?,?,?,?)}");
            callableStatement.setInt(1, test_id);
            callableStatement.setInt(2, quession_id);
            callableStatement.setString(3, user_answer);
            callableStatement.setString(4, right_answer);
            callableStatement.registerOutParameter(5, Types.BOOLEAN);
            callableStatement.registerOutParameter(6, Types.VARCHAR);
            callableStatement.execute();
            response.setSuccess(callableStatement.getBoolean(5));
            response.setMessage(callableStatement.getString(6));
            return response;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void stopProcess(int test_id, int id) {

    }

    public static void startProcess(User user) {
        int test_id;
        int quession_id;
        int subject_id;
        int quesDif_id;
        int randomNumber;
        String user_answer = "";
        int test_amount;

        List<Integer>index=new ArrayList<>();
        index.add(0);
        if (SubjectRepository.subjects.size() > 0) {
            System.out.println("-------------------------------------------------------");
            System.out.println("All subjects");
            for (Subject subject : SubjectRepository.subjects) {
                System.out.println(subject.getId() + ". " + subject.getName());
                index.add(subject.getId());
            }
            do {
                System.out.println("* Enter 0 if you want back");
                System.out.print("* Enter subject number:");
                subject_id = InPutScanner.SCANNERNUM.nextInt();
            }while (!index.contains(subject_id));
            if(subject_id!=0) {
                index.clear();
                index.add(0);
                System.out.println("-------------------------------------------------------");
                if (QuestionDifficultyrepository.questionDifficulties.size() > 0) {
                    System.out.println("All question difficulty");
                    for (QuestionDifficulty questionDifficulty : QuestionDifficultyrepository.questionDifficulties) {
                        System.out.println(questionDifficulty.getId() + ". " + questionDifficulty.getDifficulty());
                        index.add(questionDifficulty.getId());
                    }
                    do {
                        System.out.println("* Enter 0 if you want back");
                        System.out.print("* Enter question difficulty id:");
                        quesDif_id = InPutScanner.SCANNERNUM.nextInt();
                        System.out.println("--------------------------------------------");
                    } while (!index.contains(quesDif_id));
                    if (quesDif_id != 0) {
                        do {
                            System.out.print("* Testlar sonini kiriting: ");
                            test_amount = InPutScanner.SCANNERNUM.nextInt();
                            System.out.println("-------------------------------------------------------");
                        } while (test_amount <= 0);
                        int counter = 1;

                        test_id = TestRepository.addTest(user.getId());

                        List<QuestionForUser> questionForUserList;
                        questionForUserList = QuessionForUserRepository.makeQuession(subject_id, quesDif_id);
                        int size = questionForUserList.size();
                        if (size < test_amount) {
                            System.out.println("Sorry we have only " + size + " test for you");
                        }
                        List<Integer> idNextQuestion = new ArrayList<>();
                        for (QuestionForUser questionForUser : questionForUserList) {
                            idNextQuestion.add(questionForUser.getQuestion_id());
                        }

                        for (int i = 0; i < questionForUserList.size() && counter <= test_amount; i++) {
                            Random random = new Random();
                            do {
                                randomNumber = questionForUserList.get(random.nextInt(questionForUserList.size())).getQuestion_id();
                            } while (!idNextQuestion.contains(randomNumber));
                            for (int i1 = 0; i1 < idNextQuestion.size(); i1++) {
                                if (idNextQuestion.get(i1) == randomNumber) {
                                    idNextQuestion.remove(i1);
                                    break;
                                }
                            }
                            QuestionForUser question = new QuestionForUser();
                            for (QuestionForUser questionForUser : questionForUserList) {
                                if (questionForUser.getQuestion_id() == randomNumber) {
                                    question = questionForUser;
                                    break;
                                }
                            }
                            quession_id = question.getQuestion_id();
                            System.out.println(counter + ". " + question.getQuestion());

                            String[] questionArray = {question.getRight_answer(), question.getWrong_answer_1(),
                                    question.getWrong_answer_2(), question.getWrong_answer_3()};
                            index = new ArrayList<>();
                            int arryId = (int) (Math.random() * 4);
                            index.add(arryId);
                            System.out.println("A) " + questionArray[arryId]);
                            String A = questionArray[arryId];
                            do {
                                arryId = (int) (Math.random() * 4);

                            } while (index.contains(arryId));
                            index.add(arryId);
                            System.out.println("B) " + questionArray[arryId]);
                            String B = questionArray[arryId];
                            do {
                                arryId = (int) (Math.random() * 4);

                            } while (index.contains(arryId));
                            index.add(arryId);
                            System.out.println("C) " + questionArray[arryId]);
                            String C = questionArray[arryId];
                            do {
                                arryId = (int) (Math.random() * 4);

                            } while (index.contains(arryId));
                            index.add(arryId);
                            System.out.println("D) " + questionArray[arryId]);
                            String D = questionArray[arryId];

                            String answerId;
                            do {
                                System.out.println("Enter answer one (a,b,c,d)");
                                System.out.print("Answer: ");
                                answerId = InPutScanner.SCANNERSTR.nextLine();
                                answerId = answerId.toLowerCase();
                            } while (!"abcd".contains(answerId));

                            switch (answerId) {
                                case "a" -> user_answer = A;
                                case "b" -> user_answer = B;
                                case "c" -> user_answer = C;
                                case "d" -> user_answer = D;
                            }
                            Response response = UserAnswerRepository.addNewTest(test_id, quession_id, user_answer, question.getRight_answer());
                            System.out.println(response.getMessage());
                            counter++;

                        }

                        //tugatish
                        String finish_say = TestRepository.stopProcess(user.getId(), test_id);
                        System.out.println(finish_say);
                        System.out.println("------------------------------------------------");
                        showResultTest(test_id);
                        TestRepository.refresh();
                    }
                } else {
                    System.out.println("No question difficulty yet");
                }
            }

        } else {
            System.out.println("No subject yet");
        }
    }

    public static void showResultTest(int test_id) {
        String wasteTime="";
        int totalQuession=0;
        int rate=0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select age(end_time,start_time)from test where id=?");
            preparedStatement.setInt(1,test_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                wasteTime = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] time = wasteTime.split(":");
        String hh=time[0];
        String mm=time[1];
        String ss=time[2];
        System.out.println("* Total time spent: "+hh+" hour "+mm+" minute "+ss+" second");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select count(rate),sum(rate) from user_answer where test_id=?");
            preparedStatement.setInt(1,test_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalQuession = resultSet.getInt(1);
                rate = resultSet.getInt(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("* Total question: "+totalQuession);
        System.out.println("* Right answer: "+rate);
        System.out.println("* Wrong answer: "+(totalQuession-rate));
        double rateInProcent=((double) rate/totalQuession)*100;
        System.out.println("* Level of mastery:"+String.format("%.2f",rateInProcent)+" %");
        System.out.println("! Your test Id: "+test_id);

    }
}
