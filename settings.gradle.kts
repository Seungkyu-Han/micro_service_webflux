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
include("payment")
include("payment:payment-domain")
findProject(":payment:payment-domain")?.name = "payment-domain"
include("payment:payment-application")
findProject(":payment:payment-application")?.name = "payment-application"
include("payment:payment-container")
findProject(":payment:payment-container")?.name = "payment-container"
include("payment:payment-messaging")
findProject(":payment:payment-messaging")?.name = "payment-messaging"
include("payment:payment-persistence")
findProject(":payment:payment-persistence")?.name = "payment-persistence"
include("restaurant")
include("restaurant:restaurant-application")
findProject(":restaurant:restaurant-application")?.name = "restaurant-application"
include("restaurant:restaurant-container")
findProject(":restaurant:restaurant-container")?.name = "restaurant-container"
include("restaurant:restaurant-domain")
findProject(":restaurant:restaurant-domain")?.name = "restaurant-domain"
include("restaurant:restaurant-messaging")
findProject(":restaurant:restaurant-messaging")?.name = "restaurant-messaging"
include("restaurant:payment-persistence")
findProject(":restaurant:payment-persistence")?.name = "payment-persistence"
include("restaurant:restaurant-persistence")
findProject(":restaurant:restaurant-persistence")?.name = "restaurant-persistence"
include("customer")
include("customer:customer-application")
findProject(":customer:customer-application")?.name = "customer-application"
include("customer:customer-container")
findProject(":customer:customer-container")?.name = "customer-container"
include("customer:customer-domain")
findProject(":customer:customer-domain")?.name = "customer-domain"
include("customer:customer-messaging")
findProject(":customer:customer-messaging")?.name = "customer-messaging"
include("customer:customer-persistence")
findProject(":customer:customer-persistence")?.name = "customer-persistence"
include("infrastructure")
include("infrastructure:kafka")
findProject(":infrastructure:kafka")?.name = "kafka"
include("infrastructure:saga")
findProject(":infrastructure:saga")?.name = "saga"
