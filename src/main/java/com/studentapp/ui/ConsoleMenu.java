package com.studentapp.ui;

import com.studentapp.collection.DynamicArray;
import com.studentapp.io.ConsoleInput;
import com.studentapp.io.LoadResult;
import com.studentapp.io.StudentFileLoader;
import com.studentapp.model.Student;
import com.studentapp.model.StudentValidator;
import com.studentapp.model.ValidationException;
import com.studentapp.service.StudentService;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.file.AccessDeniedException;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;

public class ConsoleMenu {

    private static final Path DEFAULT_FILE = Path.of("data", "students.csv");

    private static final String MENU = """

            ===== МЕНЮ =====
             1. Добавить студента вручную
             2. Загрузить студентов из файла
             3. Показать всех студентов
             4. Показать студента по номеру
             5. Удалить студента по номеру
             6. Отчёт по успеваемости (Stream API)
             0. Выход
            """;

    private final StudentService service;
    private final StudentFileLoader fileLoader;
    private final ConsoleInput input;

    public ConsoleMenu(StudentService service, StudentFileLoader fileLoader, ConsoleInput input) {
        this.service = service;
        this.fileLoader = fileLoader;
        this.input = input;
    }

    public void run() {
        try {
            boolean running = true;
            while (running) {
                System.out.print(MENU);
                running = handleCommand(input.readLine("Выберите пункт меню: "));
            }
        } catch (NoSuchElementException e) {
            System.out.println();
            System.out.println("Поток ввода закрыт.");
        }
        System.out.println("Завершение работы программы...");
    }

    private boolean handleCommand(String line) {
        int command;
        try {
            command = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: необходимо ввести число.");
            return true;
        }

        switch (command) {
            case 1 -> addManually();
            case 2 -> loadFromFile();
            case 3 -> showAll();
            case 4 -> showByIndex();
            case 5 -> removeByIndex();
            case 6 -> showGradeReport();
            case 0 -> {
                return false;
            }
            default -> System.out.println("Неизвестная команда.");
        }
        return true;
    }

    private void addManually() {
        System.out.println("Добавление студента");
        String name = input.read("Имя: ", StudentValidator::validateName);
        double grade = input.read("Средний балл (%.1f–%.1f): "
                .formatted(StudentValidator.MIN_GRADE, StudentValidator.MAX_GRADE), StudentValidator::parseGrade);
        String recordBookNumber = input.read("Номер зачётной книжки (6 цифр): ", this::requireFreeRecordBookNumber);

        Student student = new Student(name, grade, recordBookNumber);
        service.add(student);
        System.out.println("Студент добавлен: " + student.name());
    }

    private String requireFreeRecordBookNumber(String text) {
        String number = StudentValidator.validateRecordBookNumber(text);
        if (service.containsRecordBookNumber(number)) {
            throw new ValidationException("такой номер зачётной книжки уже занят");
        }
        return number;
    }

    private void loadFromFile() {
        String text = input.readLine("Путь к файлу [%s]: ".formatted(DEFAULT_FILE));
        Path path;
        try {
            path = text.isEmpty() ? DEFAULT_FILE : Path.of(text);
        } catch (InvalidPathException e) {
            System.out.println("Ошибка: некорректный путь к файлу.");
            return;
        }

        LoadResult result;
        try {
            result = fileLoader.load(path);
        } catch (NoSuchFileException e) {
            System.out.println("Ошибка: файл не найден: " + path);
            return;
        } catch (AccessDeniedException e) {
            System.out.println("Ошибка: нет доступа к файлу: " + path);
            return;
        } catch (CharacterCodingException e) {
            System.out.println("Ошибка: файл должен быть в кодировке UTF-8.");
            return;
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
            return;
        }

        List<String> errors = new DynamicArray<>(result.errors());
        int added = 0;
        for (Student student : result.students()) {
            try {
                service.add(student);
                added++;
            } catch (ValidationException e) {
                errors.add(e.getMessage());
            }
        }

        System.out.printf("Загружено студентов: %d%n", added);
        if (!errors.isEmpty()) {
            System.out.printf("Пропущено записей: %d%n", errors.size());
            errors.forEach(error -> System.out.println("  - " + error));
        }
    }

    private void showAll() {
        if (reportIfEmpty()) {
            return;
        }
        printTable(service.findAll());
    }

    private void showByIndex() {
        if (reportIfEmpty()) {
            return;
        }
        int index = readStudentIndex();
        System.out.println(service.get(index));
    }

    private void removeByIndex() {
        if (reportIfEmpty()) {
            return;
        }
        int index = readStudentIndex();
        Student removed = service.remove(index);
        System.out.println("Удалён студент: " + removed.name());
    }

    private void showGradeReport() {
        if (reportIfEmpty()) {
            return;
        }
        double minGrade = input.read("Минимальный средний балл: ", StudentValidator::parseGrade);
        List<String> report = service.gradeReport(minGrade);

        if (report.isEmpty()) {
            System.out.printf("Нет студентов со средним баллом не ниже %.2f.%n", minGrade);
            return;
        }
        System.out.printf("Студенты со средним баллом не ниже %.2f (%d):%n", minGrade, report.size());
        report.forEach(line -> System.out.println("  " + line));
    }

    private int readStudentIndex() {
        int size = service.size();
        return input.readInt("Номер студента (1–%d): ".formatted(size), 1, size) - 1;
    }

    private boolean reportIfEmpty() {
        if (service.isEmpty()) {
            System.out.println("Список студентов пуст.");
            return true;
        }
        return false;
    }

    private void printTable(List<Student> students) {
        System.out.printf("%4s %-30s %5s   %s%n", "№", "Имя", "Балл", "Зачётка");
        for (int i = 0; i < students.size(); i++) {
            System.out.printf("%3d. %s%n", i + 1, students.get(i));
        }
    }
}
