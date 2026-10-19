package com.studentapp.service;

import com.studentapp.collection.DynamicArray;
import com.studentapp.model.Student;
import com.studentapp.model.ValidationException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StudentService {

    private final List<Student> students = new DynamicArray<>();

    public void add(Student student) {
        if (containsRecordBookNumber(student.recordBookNumber())) {
            throw new ValidationException("студент с номером зачётной книжки %s уже есть в списке"
                    .formatted(student.recordBookNumber()));
        }
        students.add(student);
    }

    public Student get(int index) {
        return students.get(index);
    }

    public Student remove(int index) {
        return students.remove(index);
    }

    public int size() {
        return students.size();
    }

    public boolean isEmpty() {
        return students.isEmpty();
    }

    public List<Student> findAll() {
        return Collections.unmodifiableList(students);
    }

    public boolean containsRecordBookNumber(String recordBookNumber) {
        return students.stream()
                .anyMatch(student -> student.recordBookNumber().equals(recordBookNumber));
    }

    public List<String> gradeReport(double minGrade) {
        return students.stream()
                .filter(student -> student.averageGrade() >= minGrade)
                .sorted(Comparator.comparingDouble(Student::averageGrade).reversed())
                .map(student -> "%s — %.2f".formatted(student.name(), student.averageGrade()))
                .toList();
    }
}
