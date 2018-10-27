(ns twitter-rss.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[twitter-rss started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[twitter-rss has shut down successfully]=-"))
   :middleware identity})
