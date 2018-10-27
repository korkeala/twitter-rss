(ns twitter-rss.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [twitter-rss.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info
      "\n-=[twitter-rss started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[twitter-rss has shut down successfully]=-"))
   :middleware wrap-dev})
