(ns illithid.character.class
  (:require [clojure.spec :as s #?@(:cljs [:include-macros true])]
            [clojure.spec.gen :as gen]
            [illithid.die :as die]))

(def max-level 20)

(s/def ::name (s/and string? seq))
(s/def ::hit-die ::die/die)
(s/def ::first-level-hit-points int?)
(s/def ::proficiency-bonuses
  (s/with-gen (s/and (s/coll-of (s/and int? pos?)
                                :count max-level
                                :kind vector? :into [])
                     #(= % (sort %)))
    #(gen/fmap (comp vec sort)
               (s/gen (s/coll-of (s/and int? pos?) :count num-levels)))))

(s/def ::class
  (s/keys :req [::name
                ::hit-die
                ::first-level-hit-points
                ::proficiency-bonuses]))
