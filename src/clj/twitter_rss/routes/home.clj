(ns twitter-rss.routes.home
  "Home routes"
  (:require [twitter-rss.layout :as layout]
            [twitter-rss.twitter :as tw]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [twitter-rss.middleware :as middleware]
            [clj-rss.core :as rss]
            [clojure.string :as string]
            [ring.util.http-response :as response]))

(defn tweets->items
  "Transforms tweets to RSS items"
  [tweets]
  (map (fn [tweet]
         (let [header (tw/parse-header tweet)
               pub-date (tw/tweet-pub-date header)
               title (tw/parse-tweet-text tweet)
               author (tw/parse-tweet-author header)
               description (tw/parse-tweet-html tweet)
               uri (tw/construct-tweet-uri header)]
         {:title title
          :author author
          :link  uri
          :guid  uri
          :pubDate pub-date
          :description description}))
       tweets))

(defn sanitize-handler [x]
 (string/replace x #"\W" ""))

(defn create-feed
  "Creates rss feed for handler"
  [handler]
  (let [desc (str "Twitter feed as rss for: " handler)
        resource (tw/fetch-twitter-feed handler)
        link (tw/twitter-link handler)
        tweets (tw/parse-tweets resource)
        items (tweets->items tweets)]
  (rss/channel-xml
  false
  {:title         desc
   :link          link
   :description   desc
   :ttl           "40"
   :lastBuildDate (java.util.Date. )}
  items)))

(defn feed-page
  "Returns rss feed page"
  [handler]
  {:status 200
   :headers {"Content-Type" "application/rss+xml; charset=utf-8"}
   :body (create-feed
          (sanitize-handler handler))
   })

(defn home-page
  "Returns index page"
  [_]
  (layout/render "home.html" ))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/feed" {:get
             (fn [{{:strs [twitter-handler]} :query-params :as req}]
               (feed-page twitter-handler))}]])
