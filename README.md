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

## Roadmap

1. **1.0** to mimic migrated data from excel (for both services & spare parts orders), support mapping of between truck and spare parts from order
2. 2.0 migrate to new data model, service to consists of spare part usage
3. 2.1 data fields validation
4. 2.5 workmanship tasks creation
5. 3.0 proactive spare parts and standard service alert