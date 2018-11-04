(ns twitter-rss.twitter
  "Functions to scrape tweets and tweet attributes."
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]))

(def TWITTER_URL "https://twitter.com")

(defn parse-tweet-text
  "Scrapes tweet content as text"
  [tweet]
  (first (html/texts (html/select tweet [:p.tweet-text]))))

(defn parse-tweet-html
  "Scrapes tweet content as html"
  [tweet]
  (str "<![CDATA["
       (string/join (html/emit* (html/select tweet [:p.tweet-text])))
       "]]>"))

(defn parse-tweet-timestamp
  "Parses tweet timestamp as ms from tweet header"
  [tweet-header]
  (get-in (first (html/select tweet-header
                              [:small.time :a.tweet-timestamp :span]))
          [:attrs :data-time-ms]))

(defn parse-header
  "Scrapes tweet header from tweet"
  [tweet]
  (html/select tweet [:div.stream-item-header ]))

(defn tweet-pub-date
  "Constructs date of publication from tweet header"
  [tweet-header]
  (java.util.Date.
   (.longValue (Long/valueOf
                (parse-tweet-timestamp tweet-header)))))

(defn parse-tweet-author
  "Scrapes tweet author from tweet header"
  [tweet-header]
  (html/text (first (html/select tweet-header [:a :strong.fullname ]))))

(defn construct-tweet-uri
  "Scrapes tweet URI from tweet."
  [tweet-header]
  (str TWITTER_URL
       (get-in (first (html/select
                       tweet-header
                       [:small.time :a.tweet-timestamp]))
               [:attrs :href])))

(defn parse-tweets
  "Scrapes tweets from html-resource"
  [resource]
  (html/select resource
               [:li.js-stream-item :div.tweet :div.content]))

(defn twitter-handler-uri
  "Returns link to handlers page"
  [handler]
  (str TWITTER_URL  "/" handler))

(defn twitter-search-uri
  "Returns link to handlers page"
  [term]
  (str TWITTER_URL  "/search?q=" term "&src=typd"))

(defn fetch-twitter-feed
  "Fetches link as an Enlive html-resource."
  [link]
  (html/html-resource (java.net.URL. link)))
