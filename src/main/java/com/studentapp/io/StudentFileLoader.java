package com.studentapp.io;

import com.studentapp.collection.DynamicArray;
import com.studentapp.model.Student;
import com.studentapp.model.StudentValidator;
import com.studentapp.model.ValidationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class StudentFileLoader {

    private static final String SEPARATOR = ";";
    private static final String COMMENT_PREFIX = "#";
    private static final int FIELD_COUNT = 3;

    public LoadResult load(Path path) throws IOException {
        List<Student> students = new DynamicArray<>();
        List<String> errors = new DynamicArray<>();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank() || line.startsWith(COMMENT_PREFIX)) {
                    continue;
                }
                try {
                    students.add(parseLine(line));
                } catch (ValidationException e) {
                    errors.add("строка %d: %s".formatted(lineNumber, e.getMessage()));
                }
            }
        }
        return new LoadResult(students, errors);
    }

    private Student parseLine(String line) {
        String[] parts = line.split(SEPARATOR, -1);
        if (parts.length != FIELD_COUNT) {
            throw new ValidationException("ожидалось %d поля через '%s', получено %d"
                    .formatted(FIELD_COUNT, SEPARATOR, parts.length));
        }
        return new Student(parts[0], StudentValidator.parseGrade(parts[1]), parts[2]);
    }
}
