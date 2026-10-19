package com.studentapp.model;

public record Student(String name, double averageGrade, String recordBookNumber) {

    public Student {
        name = StudentValidator.validateName(name);
        averageGrade = StudentValidator.validateGrade(averageGrade);
        recordBookNumber = StudentValidator.validateRecordBookNumber(recordBookNumber);
    }

    @Override
    public String toString() {
        return "%-30s %5.2f   %s".formatted(name, averageGrade, recordBookNumber);
    }
}
