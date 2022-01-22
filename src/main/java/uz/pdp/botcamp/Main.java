package uz.pdp.botcamp;

import uz.pdp.botcamp.entity.*;
import uz.pdp.botcamp.entityRepository.*;
import uz.pdp.botcamp.input.InPutScanner;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        int option;
        UserRepository.refreshUser();
        SubjectRepository.refreshSubject();
        QuestionDifficultyrepository.refresh();
        QuestionRepository.refresh();
        AnswerRepository.refresh();

        boolean rec = true;
        while (rec) {
            startMenu();
            option = InPutScanner.SCANNERNUM.nextInt();
            switch (option) {
                case 1 -> {
                    System.out.println("Enter phone number");
                    String phone = InPutScanner.SCANNERSTR.nextLine();
                    System.out.println("Enter your password");
                    String password = InPutScanner.SCANNERSTR.nextLine();
                    User user = UserRepository.hasUser(phone, password);

                    if (Objects.isNull(user)) {
                        System.out.println("Phone number or password wrong");
                    } else {
                        if (user.getRole().equals("Admin")) {
                            while (rec) {
                                dmlPosition();
                                option = InPutScanner.SCANNERNUM.nextInt();
                                switch (option) {
                                    case 1 -> {
                                        //Create
                                        while (rec) {
                                            adminMenu();
                                            option = InPutScanner.SCANNERNUM.nextInt();
                                            switch (option) {
                                                case 1 -> SubjectRepository.addNewSubject();

                                                case 2 -> QuestionDifficultyrepository.createDifficulty();

                                                case 3 -> SubjectRepository.createSubject();

                                                case 0 -> rec = false;
                                            }
                                        }
                                        rec = true;
                                    }
                                    case 2 -> {
                                        updateMenu();
                                        option=InPutScanner.SCANNERNUM.nextInt();
                                        switch (option){
                                            case 1-> SubjectRepository.changeSubject();
                                        }
                                    }
                                    case 3 -> {
                                        //Delete
                                    }
                                    case 0 -> rec = false;
                                }
                            }
                            rec = true;
                        } else {
                            while (rec) {

                                userMenu();
                                option = InPutScanner.SCANNERNUM.nextInt();
                                switch (option) {
                                    case 1 -> {

                                        // testni boshlash
                                        UserAnswerRepository.startProcess(user);

                                    }
                                    case 2 -> {
                                        //history

                                    }
                                }
                            }
                            rec = true;
                        }
                    }

                }
                case 2 -> UserRepository.registerNewUser();
                case 0 -> rec = false;
            }
        }

    }

    private static void updateMenu() {
        System.out.println("1.Update subject");
        System.out.println("2.Update question difficulty");
        System.out.println("3.Update question ");
        System.out.println("0.Exit ");
        System.out.print("Enter number ");
    }

    private static void userMenu() {
        System.out.println("________________________________________");
        System.out.println("1.Test ishlash");
        System.out.println("2.History");
        System.out.println("0.Exit");
        System.out.print("Enter number: ");
    }

    private static void startMenu() {
        System.out.println("________________________________________");
        System.out.println("1.Log in");
        System.out.println("2.Log out");
        System.out.println("0.Exit");
        System.out.print("Enter number: ");
    }

    private static void dmlPosition() {
        System.out.println("________________________________________");
        System.out.println("1.Create");
        System.out.println("2.Update");
        System.out.println("3.Delete");
        System.out.println("4.User operation");
        System.out.println("0.Exit");
        System.out.print("Enter number: ");
    }

    private static void adminMenu() {
        System.out.println("________________________________________");
        System.out.println("1.Add subject");
        System.out.println("2.Add difficulty for question");
        System.out.println("3.Add test");
        System.out.println("0.Exit");
        System.out.print("Enter number: ");
    }

}
