(ns illithid.character.ability
  (:refer-clojure :exclude [name])
  (:require [clojure.spec :as s #?@(:cljs [:include-macros true])]))

(def abilities #{::str ::dex ::con ::int ::wis ::cha})
(defn ability? [x] (-> x abilities boolean))
(s/def ::ability abilities)

(def max-natural-ability 18)
(s/def ::score (s/and int? pos? #(<= % max-natural-ability)))

(s/def ::str ::score)
(s/def ::dex ::score)
(s/def ::con ::score)
(s/def ::int ::score)
(s/def ::wis ::score)
(s/def ::cha ::score)

(s/def ::modifier (s/and int? #(<= -4 % 4)))

(defn modifier [ability-score]
  (-> ability-score (- 10) (/ 2) Math/floor int))

(s/fdef modifier
        :args (s/cat :ability-score ::score)
        :ret int?
        :fn #(= (-> % :ret pos?)
                (-> % :args :ability-score pos?)))

(def names {::str {::short-name "STR"
                   ::long-name "Strength"}
            ::dex {::short-name "DEX"
                   ::long-name "Dexterity"}
            ::con {::short-name "CON"
                   ::long-name "Constitution"}
            ::int {::short-name "INT"
                   ::long-name "Intelligence"}
            ::wis {::short-name "WIS"
                   ::long-name "Wisdom"}
            ::cha {::short-name "CHA"
                   ::long-name "Charisma"}})

(defn short-name [ability] (get-in names [ability ::short-name]))
(s/fdef short-name :args (s/cat :ability ::ability) :ret (s/and string? seq))

(defn long-name [ability] (get-in names [ability ::long-name]))
(s/fdef long-name :args (s/cat :ability ::ability) :ret (s/and string? seq))
