package com.studentapp.io;

import com.studentapp.model.ValidationException;

import java.util.Scanner;
import java.util.function.Function;

public class ConsoleInput {

    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public <T> T read(String prompt, Function<String, T> parser) {
        while (true) {
            try {
                return parser.apply(readLine(prompt));
            } catch (ValidationException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    public int readInt(String prompt, int min, int max) {
        return read(prompt, text -> parseInt(text, min, max));
    }

    private static int parseInt(String text, int min, int max) {
        int value;
        try {
            value = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new ValidationException("необходимо ввести целое число");
        }
        if (value < min || value > max) {
            throw new ValidationException("число должно быть от %d до %d".formatted(min, max));
        }
        return value;
    }
}
