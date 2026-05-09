const reporter = require('cucumber-html-reporter');

const options = {
    theme: 'bootstrap',
    jsonFile: 'target/cucumber-reports/report.json',
    output: 'target/cucumber-reports/cucumber-report.html',
    reportSuiteAsScenarios: true,
    scenarioTimestamp: true,
    launchReport: true,
    metadata: {
        "App Version": "1.0.0",
        "Test Environment": "QA Practice",
        "Browser": "Chrome",
        "Platform": "Windows 11",
        "Parallel": "Scenarios",
        "Executed": "Local"
    }
};

reporter.generate(options);