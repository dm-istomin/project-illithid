(ns illithid.character.cclass
  (:require [clojure.spec :as s #?@(:cljs [:include-macros true])]
            #?(:clj  [clojure.spec.gen :as gen]
               :cljs [cljs.spec.impl.gen :as gen])
            [illithid.die :as die]))

(def max-level 20)

(s/def ::id keyword?)
(s/def ::name (s/and string? seq))
(s/def ::hit-die ::die/die)
(s/def ::first-level-hit-points int?)
(s/def ::proficiency-bonuses
  (s/with-gen (s/and (s/coll-of (s/and int? pos?)
                                :count max-level
                                :kind vector? :into [])
                     #(= % (sort %)))
    #(gen/fmap (comp vec sort)
               (s/gen (s/coll-of (s/and int? pos?) :count max-level)))))

(s/def ::class
  (s/keys :req [::id
                ::name
                ::hit-die
                ::first-level-hit-points
                ::proficiency-bonuses]))

(def empty-class
  {::name "-"
   ::hit-die 2
   ::first-level-hit-points 1
   ::proficiency-bonuses (vec (range 1 (inc max-level)))})
