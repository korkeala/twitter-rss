(ns twitter-rss.test.validation
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [twitter-rss.routes.home :refer :all]))

(deftest validation-tests
  (testing "too long string"
    (is (= false (s/valid?
                  :twitter/search-string
                  "testtesttesttesttesttesttesttesttesttest")))))

(deftest sanitize-tests
  (testing "sanitising"
    (let [response (sanitize "test")]
      (is (= "test" response))))

  (testing "sanitize long string"
    (let [response (sanitize
                    "testtesttesttesttesttesttesttesttesttest")]
      (is (= 30 (count response)))))

  (testing "sanitize long string"
    (let [response (sanitize
                    "?*#?*#?*#?*#test?*#?*#?*#?*#?*#test?*#?*#")]
      (is (= "testtest" response))))

  (testing "sanitize only special chars"
    (let [response (sanitize
                    "?*#?*#?*#?*#?*#?*#?*#/)(¤%@)")]
      (is (= "" response))))

  (testing "sanitising"
    (let [response (sanitize
                    "..http://test*")]
      (is (= "httptest" response)))))
