package com.studentapp;

import com.studentapp.io.ConsoleInput;
import com.studentapp.io.StudentFileLoader;
import com.studentapp.service.StudentService;
import com.studentapp.ui.ConsoleMenu;

import java.io.Console;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in, inputCharset())) {
            ConsoleInput input = new ConsoleInput(scanner);
            new ConsoleMenu(new StudentService(), new StudentFileLoader(), input).run();
        }
    }

    private static Charset inputCharset() {
        Console console = System.console();
        return console != null ? console.charset() : Charset.defaultCharset();
    }
}
