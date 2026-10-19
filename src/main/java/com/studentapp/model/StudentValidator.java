package com.studentapp.model;

import java.util.regex.Pattern;

public final class StudentValidator {

    public static final double MIN_GRADE = 2.0;
    public static final double MAX_GRADE = 5.0;

    private static final int MAX_NAME_LENGTH = 60;
    private static final Pattern NAME_PATTERN = Pattern.compile("\\p{L}+(?:[ '-]\\p{L}+)*");
    private static final Pattern GRADE_PATTERN = Pattern.compile("\\d+(?:[.,]\\d+)?");
    private static final Pattern RECORD_BOOK_PATTERN = Pattern.compile("\\d{6}");

    private StudentValidator() {
    }

    public static String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("имя не может быть пустым");
        }
        String normalized = name.trim().replaceAll("\\s+", " ");
        if (normalized.length() > MAX_NAME_LENGTH) {
            throw new ValidationException("имя длиннее %d символов".formatted(MAX_NAME_LENGTH));
        }
        if (!NAME_PATTERN.matcher(normalized).matches()) {
            throw new ValidationException("имя может содержать только буквы, пробелы, дефис и апостроф");
        }
        return normalized;
    }

    public static double parseGrade(String text) {
        if (text == null || !GRADE_PATTERN.matcher(text.trim()).matches()) {
            throw new ValidationException("средний балл должен быть числом, например 4.5");
        }
        return validateGrade(Double.parseDouble(text.trim().replace(',', '.')));
    }

    public static double validateGrade(double grade) {
        if (!(grade >= MIN_GRADE && grade <= MAX_GRADE)) {
            throw new ValidationException("средний балл должен быть в диапазоне от %.1f до %.1f"
                    .formatted(MIN_GRADE, MAX_GRADE));
        }
        return grade;
    }

    public static String validateRecordBookNumber(String number) {
        if (number == null || !RECORD_BOOK_PATTERN.matcher(number.trim()).matches()) {
            throw new ValidationException("номер зачётной книжки должен состоять ровно из 6 цифр");
        }
        return number.trim();
    }
}
