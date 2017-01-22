(ns illithid.test-helpers.spec-check
  (:require [cljs.test #?@(:clj [:refer [is deftest]]
                           :cljs [:refer-macros [is]])]
            [clojure.pprint :as pprint]
            [clojure.spec.test :as stest]
            #?(:clj [cljs.spec.test :as stest-m])))

(defn summarize-results [spec-result]
  (map #(-> % stest/abbrev-result (pprint/write :stream nil))
       spec-result))

(defmacro check [& args]
  `(let [result# (stest-m/check ~@args)]
     (is (not (some :failure result#))
         (summarize-results result#))))
