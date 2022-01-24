package uz.pdp.botcamp;

import uz.pdp.botcamp.entity.*;
import uz.pdp.botcamp.entityRepository.*;
import uz.pdp.botcamp.input.InPutScanner;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        int option;
        UserRepository.refreshUser();
        SubjectRepository.refresh();
        QuestionDifficultyrepository.refresh();
        QuestionRepository.refresh();
        AnswerRepository.refresh();
        TestRepository.refresh();

        boolean rec = true;
        while (rec) {
            startMenu();
            option = InPutScanner.SCANNERNUM.nextInt();
            switch (option) {
                case 1 -> {
                    System.out.print("* Enter phone number: ");
                    String phone = InPutScanner.SCANNERSTR.nextLine();
                    System.out.print("* Enter your password: ");
                    String password = InPutScanner.SCANNERSTR.nextLine();
                    User user = UserRepository.hasUser(phone, password);

                    if (Objects.isNull(user)) {
                        System.out.println("😱 Phone number or password wrong");
                        System.out.println("1.I can't remember my code ");
                        System.out.println("0.Exit ");
                        option = InPutScanner.SCANNERNUM.nextInt();
                        if (option == 1) {
                            UserRepository.recoverCode(phone);
                        }
                    } else {
                        if (!user.isActive()) {
                            System.out.println(" 😱  You are blocked by admin");
                        } else {
                            if (user.getRole().equals("Admin")) {
                                while (rec) {
                                    crudPosition();
                                    option = InPutScanner.SCANNERNUM.nextInt();
                                    switch (option) {
                                        case 1 -> {
                                            while (rec) {
                                                adminMenu();
                                                option = InPutScanner.SCANNERNUM.nextInt();
                                                switch (option) {
                                                    case 1 -> SubjectRepository.addNewSubject();

                                                    case 2 -> QuestionDifficultyrepository.createDifficulty();

                                                    case 3 -> QuestionRepository.addQuestion();

                                                    case 0 -> rec = false;
                                                }
                                            }
                                            rec = true;
                                        }
                                        case 2 -> {
                                            while (rec) {
                                                updateMenu();
                                                option = InPutScanner.SCANNERNUM.nextInt();
                                                switch (option) {
                                                    case 1 -> {
                                                        Response response = SubjectRepository.changeSubject();
                                                        System.out.println(response.getMessage());
                                                    }
                                                    case 2 -> {
                                                        Response response = QuestionDifficultyrepository.changDifficulty();
                                                        System.out.println(response.getMessage());
                                                    }
                                                    case 3 -> {
                                                        Response response = QuestionRepository.changeQuestion();
                                                        System.out.println(response.getMessage());
                                                    }
                                                    case 0 -> rec = false;
                                                }
                                            }
                                            rec = true;
                                        }
                                        case 3 -> {
                                            while (rec) {
                                                deleteMenu();
                                                option = InPutScanner.SCANNERNUM.nextInt();
                                                switch (option) {
                                                    case 1 -> {
                                                        Response response = SubjectRepository.deleteSubject();
                                                        System.out.println(response.getMessage());
                                                    }
                                                    case 2 -> {
                                                        Response response = QuestionDifficultyrepository.deleteDifficulty();
                                                        System.out.println(response.getMessage());
                                                    }
                                                    case 3 -> {
                                                        Response response = QuestionRepository.deleteQuestion();
                                                        System.out.println(response.getMessage());
                                                    }
                                                    case 0 -> rec = false;
                                                }

                                            }
                                            rec = true;
                                        }
                                        case 4 -> SupportForMain.Rollback();
                                        case 5 -> UserRepository.operatino();
                                        case 0 -> rec = false;
                                    }
                                }
                                rec = true;
                            } else {
                                while (rec) {

                                    userMenu();
                                    option = InPutScanner.SCANNERNUM.nextInt();
                                    switch (option) {
                                        case 1 -> UserAnswerRepository.startProcess(user);

                                        case 2 -> SupportForMain.showHistory(user.getId());
                                        case 3->TestRepository.removeTestId(user.getId());
                                        case 0 -> rec = false;
                                    }
                                }
                                rec = true;
                            }
                        }
                    }
                    }
                    case 2 -> UserRepository.registerNewUser();
                    case 0 -> rec = false;
                    default -> System.out.println("Wrong number");
                }

        }

    }

    private static void deleteMenu() {
        System.out.println("1.Delete subject");
        System.out.println("2.Delete question difficulty");
        System.out.println("3.Delete question");
        System.out.println("0.Exit");
        System.out.print("Enter number: ");
    }

    private static void updateMenu() {
        System.out.println("________________________________________");
        System.out.println("1.Update subject");
        System.out.println("2.Update question difficulty");
        System.out.println("3.Update question ");
        System.out.println("0.Exit ");
        System.out.print("Enter number: ");
    }

    private static void userMenu() {
        System.out.println("________________________________________");
        System.out.println("1.Test ishlash");
        System.out.println("2.History");
        System.out.println("3.Remove old history");
        System.out.println("0.Exit");
        System.out.print("Enter number: ");
    }

    private static void startMenu() {
        System.out.println("________________________________________");
        System.out.println("1.Sign in");
        System.out.println("2.Sign up");
        System.out.println("0.Exit");
        System.out.print("* Enter number: ");
    }

    private static void crudPosition() {
        System.out.println("________________________________________");
        System.out.println("1.Create");
        System.out.println("2.Update");
        System.out.println("3.Delete");
        System.out.println("4.Roll back delete things");
        System.out.println("5.User operation");
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
