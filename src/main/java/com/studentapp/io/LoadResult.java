package com.studentapp.io;

import com.studentapp.model.Student;

import java.util.List;

public record LoadResult(List<Student> students, List<String> errors) {
}
