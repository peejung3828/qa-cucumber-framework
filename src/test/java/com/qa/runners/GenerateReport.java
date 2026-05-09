package com.qa.runners;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GenerateReport {

    public static void main(String[] args) {
        File outputDirectory = new File("target/cucumber-reports");
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("target/cucumber-reports/report.json");

        String projectName = "QA Cucumber Framework";
        Configuration configuration = new Configuration(outputDirectory, projectName);

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();
    }
}