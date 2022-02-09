# Overview
This project was created to analyse [triangular arbitrage](https://en.wikipedia.org/wiki/Triangular_arbitrage) opportunities within BTCMarkets.

There are currently three triangular arbitrage opportunities on BTCMarkets between the following currencies which can all be examined with this app:
- AUD, BTC, ETH
- AUD, BTC, LTC
- AUD, BTC, XRP

The app allows a user to check if an arbitrage opportunity exists at any given time for the above markets. If so, the app displays the steps that could be hypothetically taken to achieve arbitrage.

It also allows for a user to "poll" which involves automatically checking for arbitrage every 10 seconds. The app displays the number of profitable checks to give the user a sense of overall arbitrage opportunity.

Using the app reveals that there is virtually no arbitrage available and the markets are running efficiently.

# Building

This app was built using Maven.

Test cases can be run using `mvn test`

The JAR file can be created with `mvn package`

Then run `BtcArbitrage-1.0-SNAPSHOT-jar-with-dependencies.jar` to start the application