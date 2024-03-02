import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

// Класс исключения для некорректного формата данных
class InvalidDataFormatException extends Exception {
    public InvalidDataFormatException(String message) {
        super(message);
    }
}

public class UserDataApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите данные (Фамилия Имя Отчество дата_рождения номер_телефона пол):");
        String userInput = scanner.nextLine();

        try {
            processUserData(userInput);
            System.out.println("Данные введены корректно и записаны в файл.");
        } catch (InvalidDataFormatException | IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void processUserData(String userInput) throws InvalidDataFormatException, IOException {
        String[] data = userInput.split("\\s+");

        if (data.length != 6) {
            throw new InvalidDataFormatException("Неверное количество данных. Требуется 6 значений.");
        }

        String lastName = data[0];
        String firstName = data[1];
        String middleName = data[2];
        String birthDateStr = data[3];
        String phoneNumberStr = data[4];
        String gender = data[5];

        // Проверка формата даты
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date birthDate;
        try {
            birthDate = dateFormat.parse(birthDateStr);
        } catch (ParseException e) {
            throw new InvalidDataFormatException("Неверный формат даты рождения. Ожидается dd.MM.yyyy.");
        }

        // Проверка формата номера телефона
        try {
            Long.parseLong(phoneNumberStr);
        } catch (NumberFormatException e) {
            throw new InvalidDataFormatException("Неверный формат номера телефона. Ожидается целое беззнаковое число.");
        }

        // Проверка формата пола
        if (!gender.equals("f") && !gender.equals("m")) {
            throw new InvalidDataFormatException("Неверный формат пола. Ожидается 'f' или 'm'.");
        }

        // Запись в файл
        String fileName = lastName + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.format("%s %s %s %s %s %s%n",
                    lastName, firstName, middleName, birthDateStr, phoneNumberStr, gender));
        } catch (IOException e) {
            throw new IOException("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}
