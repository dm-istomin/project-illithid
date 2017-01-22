(ns illithid.spec
  (:require [clojure.spec :as s #?@(:cljs [:include-macros true])]))

(defmacro set-of [sp & opts]
  (let [opts (->> opts
                  (partition 2)
                  (map vec)
                  (into {})
                  (merge {:kind `set? :into #{}})
                  (into [])
                  (flatten))]
    `(s/coll-of ~sp ~@opts)))
