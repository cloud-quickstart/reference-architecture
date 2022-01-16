# Reference Architecture | Code | DevOps | SRE artifacts and principles

[![Open this project in Google Cloud Shell](http://gstatic.com/cloudssh/images/open-btn.png)](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/cloud-quickstart/reference-architecture&page=editor&tutorial=README.md)

# Introduction
This set of projects serve the combined purpose of providing a concrete hands on view and set of artifacts around a provisional set of services and associated development, architecture, design, deployment, slow, monitoring, change management, capacity planning, and provisioning.


# Business Requirements
A set of services with a defined SLO

Sli1: availability of 99.95
Sli2: 90 of rest calls complete in 1ms, 99 in 10ms, 99.9 in 100ms
Sli3: report extractions/exports within 60 sec (5Gb, 10 million records)

A phased set of progressive releases that exercise the provisioning pipeline

A maximum coverage yet minimum cost spanning tree of deployment artifacts

A useful business function like biometric tracking, event handling or routing functionality


# Technical Requirements
# DevOps
## Developer Setup
## Cloud Setup

# Quickstart
## Clone
## Build
## Commit/Push
## Local Docker

# Architecture
# Design
## Developer Guide
### Create initial spring boot maven project
Create a new project via the spring initializr https://start.spring.io/ see https://spring.io/guides/gs/serving-web-content/

The following maven archetype will generate a submodule stub project around a single controller to start
    mvn archetype:generate -DgroupId=ca.cloudlift.reference -DartifactId=reference-nbi -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

Add the root pom.xml 


### Add Swagger and OpenAPI 3
### 
### Configure IDEs for Development

## Design Issues

# CI/CD

# Deployment

# Operations

# Links

# References

