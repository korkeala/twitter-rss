(ns twitter-rss.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [twitter-rss.handler :refer :all]
            [twitter-rss.middleware.formats :as formats]
            [muuntaja.core :as m]
            [mount.core :as mount]))

(defn parse-json [body]
  (m/decode formats/instance "application/json" body))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'twitter-rss.config/env
                 #'twitter-rss.handler/app)
    (f)))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "search route without parameters"
    (let [response (app (request :get "/search"))]
      (is (= 400 (:status response)))))

  (testing "feed route without parameters"
    (let [response (app (request :get "/feed"))]
      (is (= 400 (:status response)))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= 404 (:status response))))))
