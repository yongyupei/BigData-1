# Welcome to BigData.Spark

It's about various BigData problems solved with Spark and, in future, NoSQL technologies. While currently it's 70% Java and 30% Kotlin, heading to Kotlin 100%.

## Word count and ranking of multiple data streams
The first one of the most recurrent BigData problems: the word count and ranking.
Set up:
- A Spark Streaming application (SSA) connects to two TCP servers.
- Once the connection is established, the TCP servers start sending data, in the form of randomly generated phrases.
- The SSA aggregates the data by (full-outer) joining the two input streams and finally builds a ranking. 

The beauty of this is that the SSA can continuously receive and apply transformation to the received data, grouping it in pre-defined time frames (batch duration).
Unfortunately, Spark 2.4 doesn't fully support Java 10-11, for instance, in collect operations, so I'd to use Java 8 in the SSA. While I could use Java 11 in the TCP data streaming servers.

## Trades Analytics
The data streamers send JSON representations of Trades (symbol, price, qty, direction (buy/ sell), exchange), the SSA decodes them into Trades object and finally applies stateful transformations based on JavaPairDStream.mapWithState.

```
{"symbol":"CSGN","price":455.4870609871,"direction":"Sell","quantity":28,"exchange":"EUREX"}
{"symbol":"AAPL","price":311.5765300990,"direction":"Sell","quantity":14,"exchange":"FTSE"}
{"symbol":"UBSN","price":339.7060390450,"direction":"Buy","quantity":14,"exchange":"NASDAQ"}
{"symbol":"GOOG","price":264.5499525436,"direction":"Sell","quantity":59,"exchange":"FTSE"}
```
Various real-time analytics will be put in place for metrics like: avg price and quantity per symbol and direction. 

## Development environment and tools
- Ubuntu.
- Intellij.
- Spark 2.4.
- Java 8, 11.
- Kotlin 1.3.
