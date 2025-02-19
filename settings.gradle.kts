plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "msa"
include("order")
include("order:order-domain")
findProject(":order:order-domain")?.name = "order-domain"
include("common")
include("common:common-domain")
findProject(":common:common-domain")?.name = "common-domain"
include("order:order-messaging")
findProject(":order:order-messaging")?.name = "order-messaging"
include("order:order-persistence")
findProject(":order:order-persistence")?.name = "order-persistence"
include("order:order-application")
findProject(":order:order-application")?.name = "order-application"
include("order:order-container")
findProject(":order:order-container")?.name = "order-container"
