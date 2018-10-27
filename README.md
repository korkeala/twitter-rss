# Twitter-rss

Twitter-rss is a service to transform twitter feeds
to RSS feeds.

Service is created with [Luminus][1] web framework.

[1]: http://www.luminusweb.net/

## Prerequisites

You will need [Leiningen][2] 2.0 or above installed.

[2]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run 
    
The service is available at localhost port 3000.


## Docker
There is also a [docker image][3] available. You can run it:

    docker run -it -p 3000:3000 -d korkeala/twitter-rss:latest

[3]: https://hub.docker.com/r/korkeala/twitter-rss/

## License
Distributed under the Eclipse Public License.
Copyright © 2018 Markku Korkeala
