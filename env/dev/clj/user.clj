(ns user
  (:require [twitter-rss.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [twitter-rss.core :refer [start-app]]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'twitter-rss.core/repl-server))

(defn stop []
  (mount/stop-except #'twitter-rss.core/repl-server))

(defn restart []
  (stop)
  (start))
