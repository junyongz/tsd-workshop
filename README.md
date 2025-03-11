# TSD Workshop Reactive Webapp

## What does it do

1. Fetching migrated data
2. APIs work on migrated data model

## Requirements

1. JDK 21
2. Apache Maven
3. Postgresql
4. Skills

## Full packaging with SPA

To copy bundled SPA over here (after `npm run build`)

1. rm -rf src/main/resources/public/
2. mkdir src/main/resources/public/
3. cp -R ../tsd-workshop-spa/build/* ./src/main/resources/public/
4. mvn clean package
