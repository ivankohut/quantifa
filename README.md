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
buying/selling based on value investing principles. Stock data are retrieved
from [IB Trader Workstation (TWS)](https://www1.interactivebrokers.com/en/index.php?f=14099).

## How to Use

First you need to build the executable `jar` file (requires JDK 15+) by
executing

```shell script
./gradlew assemble
```

Then, make sure TWS is running on localhost accepting connections on port 7496.
Finally, to run application, execute

```shell script
java -jar ./build/libs/quantifa.jar
```

## Frequently Asked Questions

> What does the name *quantifa* stands for?

*quantifa* stands for **quanti**tative **f**undamental **a**nalysis.
