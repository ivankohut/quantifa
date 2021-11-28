# quantifa

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)

[![Build Status](https://travis-ci.com/ivankohut/quantifa.svg?branch=master)](https://travis-ci.com/ivankohut/quantifa)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ivankohut_quantifa&metric=alert_status)](https://sonarcloud.io/dashboard?id=ivankohut_quantifa)
[![Codecov](https://codecov.io/gh/ivankohut/quantifa/branch/master/graph/badge.svg)](https://codecov.io/gh/ivankohut/quantifa)

[![Hits-of-Code](https://hitsofcode.com/github/ivankohut/quantifa)](https://hitsofcode.com/view/github/ivankohut/quantifa)

---
**Attention:** *quantifa* is just in the beginning of development, nothing
useful is implemented yet.

---

*quantifa* calculates simple metrics (like Graham number) for given list of
stocks. This information can help you to decide which stocks are worth
buying/selling based on value investing principles. Stock fundamental data are
retrieved from [IB Trader Workstation (TWS)](https://www1.interactivebrokers.com/en/index.php?f=14099),
prices are retrieved from TWS or [Financial Modeling Prep (FMP)](https://financialmodelingprep.com/developer/docs/)
or [Alpha Vantage (AV)](https://www.alphavantage.co/documentation/) or [yahoo! finance (YF)](https://finance.yahoo.com).

## How to Use

First you need to build the executable `jar` file (requires JDK 15+) by
executing

```shell script
./gradlew assemble
```

Second, make sure TWS is running and accepting connections.

Then, set environment variables, e.g. (default values are shown for optional
variables):

```shell
export TWS_HOSTNAME=localhost # optional
export TWS_PORT=7496 # optional
export CACHE_DIR=cache/dir
export FUNDAMENTALS_EXCHANGE=NYSE
export FUNDAMENTALS_SYMBOL=CAT
export FUNDAMENTALS_CURRENCY=USD
export FMP_APIKEY=1234567890abcdef
export AV_APIKEY=1234567890abcdef # optional if PRICE_SOURCE is not AV
export PRICE_SOURCE=TWS # possible values: TWS, FMP, AV, YF
export PRICE_EXCHANGE=NYSE # only for TWS price source
export PRICE_SYMBOL=CAT
export PRICE_CURRENCY=USD # only for TWS price source
export PRICE_DIVISOR=1 # optional
export PRICE_SOURCE_COOLDOWN_SECONDS=0 # optional, for AV and YF price sources
```

Finally, to run application, execute

```shell script
java -jar ./build/libs/quantifa.jar
```

## Frequently Asked Questions

> What does the name *quantifa* stands for?

*quantifa* stands for **quanti**tative **f**undamental **a**nalysis.
