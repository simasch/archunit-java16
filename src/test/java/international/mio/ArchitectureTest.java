package com.example.archuitjava16;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

public class ArchitectureTest {

    public static final String MODULE_ASSORTMENT = "international.mio.assortment..";
    public static final String MODULE_CUSTOMER = "international.mio.customer..";
    public static final String MODULE_ORDER = "international.mio.order..";
    public static final String MODULE_WAREHOUSE = "international.mio.warehouse..";
    public static final String MODULE_SHIPMENT = "international.mio.shipment..";
    public static final String MODULE_INVOICING = "international.mio.invoicing..";

    private final JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("international.mio");

    @Test
    void checkModules() {
        noClasses().that().resideInAPackage(MODULE_ASSORTMENT)
                .should().dependOnClassesThat().resideInAnyPackage(MODULE_CUSTOMER, MODULE_ORDER, MODULE_WAREHOUSE, MODULE_SHIPMENT, MODULE_INVOICING)
                .check(importedClasses);

        noClasses().that().resideInAPackage(MODULE_CUSTOMER)
                .should().dependOnClassesThat().resideInAnyPackage(MODULE_ASSORTMENT, MODULE_ORDER, MODULE_WAREHOUSE, MODULE_SHIPMENT, MODULE_INVOICING)
                .check(importedClasses);

        noClasses().that().resideInAPackage(MODULE_ORDER)
                .should().dependOnClassesThat().resideInAnyPackage(MODULE_WAREHOUSE, MODULE_SHIPMENT, MODULE_INVOICING)
                .check(importedClasses);

        noClasses().that().resideInAPackage(MODULE_WAREHOUSE)
                .should().dependOnClassesThat().resideInAnyPackage(MODULE_SHIPMENT, MODULE_INVOICING)
                .check(importedClasses);

        noClasses().that().resideInAPackage(MODULE_SHIPMENT)
                .should().dependOnClassesThat().resideInAnyPackage(MODULE_WAREHOUSE, MODULE_INVOICING)
                .check(importedClasses);

        noClasses().that().resideInAPackage(MODULE_INVOICING)
                .should().dependOnClassesThat().resideInAnyPackage(MODULE_WAREHOUSE, MODULE_SHIPMENT)
                .check(importedClasses);
    }

    @Test
    void checkCycles() {
        slices().matching("international.mio.(*)..")
                .should().beFreeOfCycles()

                .check(importedClasses);
    }
}
