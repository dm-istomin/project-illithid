(ns illithid.character.ability
  (:require [clojure.spec :as s #?@(:cljs [:include-macros true])]))

(def abilities #{::str ::dex ::con ::int ::wis ::cha})
(s/def ::ability abilities)

(def max-natural-ability 18)
(s/def ::score (s/and int? pos? #(< % max-natural-ability)))

(s/def ::str ::score)
(s/def ::dex ::score)
(s/def ::con ::score)
(s/def ::int ::score)
(s/def ::wis ::score)
(s/def ::cha ::score)

(s/def ::modifier (s/and int? #(< -4 % 4)))

(defn modifier [ability-score]
  (-> ability-score (- 10) (/ 2) Math/floor int))

(s/fdef modifier
        :args (s/cat :ability-score ::score)
        :ret int?
        :fn #(= (-> % :ret pos?)
                (-> % :args :ability-score pos?)))
