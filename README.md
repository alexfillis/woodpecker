woodpecker
==========

A simple service that provides price and other related data for an exchange ticker. It's really a harness for trying
out a few things like:

* [webbit](https://github.com/webbit/webbit)
* [Guava Cache](https://code.google.com/p/guava-libraries/wiki/CachesExplained)
* [Coda Hale Metrics](http://metrics.codahale.com)
* [Elasticsearch](http://www.elasticsearch.org/overview/elasticsearch/)
* [Kibana](http://www.elasticsearch.org/overview/kibana/)

## How to get it up and running

1. Download Elasticsearch.
2. Extract two copies (to run a two node cluster).
3. Startup two instances:
  $ES_HOME/bin/elasticsearch
4.Startup Kibana hosted on webbit.
  java -cp
5. Startup and instance of *woodpeacker*
  java -cp
6. Run the TestClient to generate some requests.