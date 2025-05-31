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

1. 1.0 to mimic migrated data from excel (for both services & spare parts orders), support mapping of between truck and spare parts from order
2. **2.0** migrate to new data model, service to consists of spare part usage, data fields validation
4. 3.0 workmanship tasks creation
5. 4.0 spare part management and proactive alert

## Support This Project

If you find this project useful, consider donating!

[Stripe Donate](https://buy.stripe.com/7sY5kCbtj2dR8b1cCRfEk00)
