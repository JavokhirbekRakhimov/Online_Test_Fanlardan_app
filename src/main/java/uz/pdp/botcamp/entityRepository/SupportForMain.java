package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.entity.History;
import uz.pdp.botcamp.entity.Test;
import uz.pdp.botcamp.input.InPutScanner;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SupportForMain {

    public static void Rollback() {
        System.out.println("----------------------------------------------");
        System.out.println("1.Subject");
        System.out.println("2.Difficulty");
        System.out.println("3.Question");
        System.out.println("0.Exit");
        System.out.print("Enter number: ");
        int option= InPutScanner.SCANNERNUM.nextInt();
        switch (option){
            case 1-> SubjectRepository.rollbackSubject();

            case 2->QuestionDifficultyrepository.rollbackDifficulty();

            case 3->QuestionRepository.rollbackQuestion();

        }
    }

    public static void showHistory(int user_id) {

        List<Integer>index=new ArrayList<>();
        index.add(0);
        System.out.println("--------------------------------------------------------");
        for (Test test : TestRepository.tests) {
            if(test.getUser_id()==user_id) {
                    System.out.println("| * Id : " + test.getId() + " | Time: "+
                            new SimpleDateFormat("yyyy.MM.dd  hh:mm:ss").format(test.getStart_time())+"  |");
                    index.add(test.getId());
                }
            }
        int test_id;
        System.out.println("--------------------------------------------------------");
        do {
            System.out.println("Enter 0 if you want back ");
            System.out.print("Enter your test id: ");
            test_id = InPutScanner.SCANNERNUM.nextInt();
            System.out.println("----------------------------------------------");
        }while (!index.contains(test_id));
        if(test_id!=0) {
            List<History> list;
            list = HistoryRespository.makeList(test_id);
            int counter = 1;
            if (!list.isEmpty()) {
                for (History history : list) {
                    System.out.println("* Number: " + counter);
                    System.out.println("* Question: " + history.getQuestion());
                    System.out.println("* Your answer: " + history.getYour_answer());
                    System.out.println("* Right answer: " + history.getRight_answer());
                    int rate = history.getRate();
                    if (rate == 1)
                        System.out.println("* Result: " + "  ✅");
                    else
                        System.out.println("* Result: " + "  ❌");
                    System.out.println("-------------------------------------------------------");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    counter++;
                }
            } else {
                System.out.println("Empty data");
            }

            UserAnswerRepository.showResultTest(test_id);
        }
    }


}
