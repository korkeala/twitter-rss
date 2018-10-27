# Twitter-rss

Twitter-rss is a service to transform twitter feeds
to RSS feeds.

generated using Luminus version "3.10.9"


## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run 
    
The service is available at localhost port 3000.
## Docker
There is also a [docker image][2] available. You can run it:

    sudo docker run -it -p 3000:3000 -d korkeala/twitter-rss:latest

[2]: https://hub.docker.com/r/korkeala/twitter-rss/

## License

Copyright © 2018 Markku Korkeala
