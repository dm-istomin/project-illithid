(ns illithid.character
  (:require [clojure.spec :as s]
            [illithid.ability :as a]
            [illithid.skill :as sk]))

(s/def ::name (s/and string? seq))

(s/def ::abilities
  (s/keys :req [::a/str ::a/dex ::a/con ::a/int ::a/wis ::a/cha]))

(s/def ::skill-proficiencies (s/coll-of ::sk/skill
                                        :kind set? :into #{}))
(s/def ::saving-throw-proficiencies (s/coll-of ::a/ability
                                               :kind set? :into #{}))

(s/def ::character
  (:req s/keys [::name
                ::abilities
                #_::skill-proficiencies
                #_::saving-throw-proficiencies]))
