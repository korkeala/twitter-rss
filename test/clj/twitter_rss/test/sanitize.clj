(ns twitter-rss.test.sanitize
  (:require [clojure.test :refer :all]
            [twitter-rss.routes.home :as home]))

(deftest sanitize
  (testing "sanitising"
    (let [response (home/sanitize "test")]
      (is (= "test" response))))

  (testing "sanitize long string"
    (let [response (home/sanitize "testtesttesttesttesttesttesttesttesttest")]
      (is (= 30 (count response)))))

  (testing "sanitize long string"
    (let [response (home/sanitize "?*#?*#?*#?*#test?*#?*#?*#?*#?*#test?*#?*#")]
      (is (= "testtest" response))))

  (testing "sanitize only special chars"
    (let [response (home/sanitize "?*#?*#?*#?*#?*#?*#?*#/)(¤%@)")]
      (is (= "" response))))

  (testing "sanitising"
    (let [response (home/sanitize "..http://test*")]
      (is (= "httptest" response)))))
