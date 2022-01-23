package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.input.InPutScanner;

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
}
