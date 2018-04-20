package test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TestRunner {
    private static Result result;
    public static void main(String[] args) {
        System.out.println("Tests started...");

        PrintStream oldPrintStream = System.out;

        deletePreviousTestResult();

        try (OutputStream outFile = Files.newOutputStream(Paths.get(formUrl()),
            StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            PrintStream newSysOut = new PrintStream(outFile)) {
            System.setOut(newSysOut);

            result = JUnitCore.runClasses(TestSuite.class);
            printResult();
        } catch (IOException ex) {
        } finally {
            System.setOut(oldPrintStream);
            System.out.println("Tests finished.");
        }
    }

    private static String formUrl()
    {
        String url = "";
        url += new File(".").getAbsoluteFile();
        url = url.replace("\\", "//");
        url = url.substring(0, url.length() - 1);
        url += "testResult.txt";

        return url;
    }

    private static void printResult()
    {
        for (Failure failure : result.getFailures()) {
            System.out.println("TEST " + failure.getTestHeader() + " FAILED: " + failure.getMessage());
        }

        if (result.wasSuccessful())
        {
            System.out.println("Successful in " + result.getRunTime() + " ms!");
        }
        else
        {
            System.out.println("Failed in " + result.getRunTime() + " ms!");
        }
    }

    private static void deletePreviousTestResult()
    {
        try
        {
            File file = new File(formUrl());
            file.delete();
        }
        catch (NullPointerException ex)
        {
        }
    }

}