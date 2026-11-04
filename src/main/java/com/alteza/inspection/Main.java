package com.alteza.inspection;

import com.alteza.inspection.model.*;
import com.alteza.inspection.visitor.*;
import java.util.List;

public final class Main {
    private Main() { }
    public static void main(String[] args) {
        Directory project = new Directory("fintech-platform");
        Directory source = new Directory("src");
        source.addElement(new SourceCodeFile("PaymentService.java", "payments", 240, 8, false, false, true));
        source.addElement(new SourceCodeFile("RiskEngine.java", "risk", 310, 14, true, true, true));
        Directory tests = new Directory("test");
        tests.addElement(new TestFile("PaymentServiceTest.java", 120, "payments", 8));
        tests.addElement(new TestFile("RiskEngineTest.java", 160, "risk", 10));
        project.addElement(source); project.addElement(tests);
        project.addElement(new ConfigurationFile("application.yml", 35, false, true));
        project.addElement(new DependencyFile("pom.xml", 55, List.of(
                new DependencyInfo("junit", "5.10.2", false, false),
                new DependencyInfo("legacy-payment-sdk", "1.4.0", true, false))));

        SecurityAuditVisitor security = new SecurityAuditVisitor(); project.accept(security);
        ProjectStatisticsVisitor statistics = new ProjectStatisticsVisitor(); project.accept(statistics);
        QualityGateVisitor quality = new QualityGateVisitor(); project.accept(quality);
        ComplianceVisitor compliance = new ComplianceVisitor(); project.accept(compliance);
        System.out.println(security.getReport());
        System.out.println(statistics.getReport());
        System.out.println(quality.getReport());
        System.out.println(compliance.getReport());
    }
}
