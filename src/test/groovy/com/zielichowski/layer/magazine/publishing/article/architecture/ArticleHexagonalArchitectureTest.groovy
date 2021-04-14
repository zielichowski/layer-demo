package com.zielichowski.layer.magazine.publishing.article.architecture

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.library.DependencyRules
import com.tngtech.archunit.library.GeneralCodingRules
import spock.lang.Shared
import spock.lang.Specification

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

class ArticleHexagonalArchitectureTest extends Specification {
    @Shared
    def classes = new ClassFileImporter().importPackages("com.zielichowski.layer.magazine.publishing.article")


    def "Should not throw generic exceptions"() {
        given:
        ArchRule archRule = GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS

        expect:
        archRule.check(classes)
    }

    def "Should no accesses to upper package"() {
        given:
        ArchRule archRule = DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES

        expect:
        archRule.check(classes)
    }


    def "Domain model should not depend on application"() {
        given:
        ArchRule archRule =
                ArchRuleDefinition.classes()
                        .that().resideInAPackage("..domain..")
                        .should().onlyBeAccessed().byClassesThat().resideInAPackage("..domain..")
        expect:
        archRule.check(classes)
    }

    def "Domain model should be framework agnostic"(){
        ArchRule archRule =
                noClasses()
                        .that()
                        .resideInAPackage(
                                "..model..")
                        .should()
                        .dependOnClassesThat()
                        .resideInAPackage("org.springframework..");
        expect:
        archRule.check(classes)
    }


}
