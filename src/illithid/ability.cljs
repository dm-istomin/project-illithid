(ns illithid.ability
  (:require [clojure.spec :as s]))

(def abilities #{::str ::dex ::con ::int ::wis ::cha})
(s/def ::ability abilities)

(s/def ::str ::score)
(s/def ::dex ::score)
(s/def ::con ::score)
(s/def ::int ::score)
(s/def ::wis ::score)
(s/def ::cha ::score)

(def max-natural-ability 18)
(s/def ::score (s/and int? pos? #(< % max-natural-ability)))
