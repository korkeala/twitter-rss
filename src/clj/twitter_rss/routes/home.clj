(ns twitter-rss.routes.home
  "Home routes and supporting functionality"
  (:require [twitter-rss.layout :as layout]
            [twitter-rss.twitter :as tw]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [clojure.spec.alpha :as s]
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

(def search-regex #"^[a-zA-Z0-9._-]{4,30}$")
(s/def :twitter/search-string (s/and string? #(re-matches search-regex %)))

(defn sanitize
  "Removes characters not alphanumeric and then reduces
   string length if length is over 30 characters."
  [x]
  (let [sanitized-string (string/replace x #"\W" "")]
    (if (> (count sanitized-string ) 30)
      (.substring sanitized-string 0 30)
      sanitized-string)))

(defn create-rss-feed
  "Creates rss feed for given parameters"
  [desc uri date items]
  (rss/channel-xml
  false
  {:title         desc
   :link          uri
   :description   desc
   :ttl           "40"
   :lastBuildDate date}
  items))

(defn rss-feed-response
  "Returns rss feed from the given body as a HTTP response."
  [body]
  {:status 200
   :headers {"Content-Type" "application/rss+xml; charset=utf-8"}
   :body body})


(defn construct-feed
  "Loads resource from twitter URI and creates RSS feed"
  [desc uri]
  (let [resource (tw/fetch-twitter-feed uri)
        tweets (tw/parse-tweets resource)
        items (tweets->items tweets)]
    (if (zero? (count items))
      (layout/error-page {:status 404
                          :title "Could not scrape tweets"
                          :message "Could not scrape tweets, try other
                                    search term"})
      (rss-feed-response
       (create-rss-feed desc uri (java.util.Date.) items)))))

(defn home-page
  "Returns index page from home.html template"
  [_]
  (layout/render "home.html" ))

(defn construct-feed-response
  "Constructs response for twitter feed"
  [twitter-handler]
  (if (not (s/valid? :twitter/search-string twitter-handler))
    (layout/error-page {:status 400
                        :title "Twitter handler is not valid"})
    (let [sanitized (sanitize twitter-handler)]
      (construct-feed
       (str "Twitter feed as rss for: " sanitized)
       (tw/twitter-handler-uri sanitized)))))

(defn construct-search-response
  "Constructs response from twitter search"
  [search-term]
  (if (not (s/valid? :twitter/search-string search-term))
    (layout/error-page {:status 400
                        :title "Search term is not valid"})
    (let [sanitized (sanitize search-term)]
      (construct-feed
       (str "Twitter search results as rss for term: " sanitized)
       (tw/twitter-search-uri sanitized)))))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/feed" {:get
             (fn [{{:strs [twitter-handler]} :query-params :as req}]
               (construct-feed-response twitter-handler))}]
   ["/search" {:get
             (fn [{{:strs [search-term]} :query-params :as req}]
               (construct-search-response search-term))}]])
