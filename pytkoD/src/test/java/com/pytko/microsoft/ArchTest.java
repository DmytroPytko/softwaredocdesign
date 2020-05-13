package com.pytko.microsoft;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.pytko.microsoft");

        noClasses()
            .that()
                .resideInAnyPackage("com.pytko.microsoft.service..")
            .or()
                .resideInAnyPackage("com.pytko.microsoft.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.pytko.microsoft.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
