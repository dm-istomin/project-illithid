(ns illithid.skill
  (:require [clojure.spec :as s]
            [illithid.ability :as a]))

(def ability-for-skill
  {::acrobatics        ::a/dex
   ::animal-handling   ::a/wis
   ::arcana            ::a/int
   ::athletics         ::a/str
   ::deception         ::a/cha
   ::history           ::a/int
   ::insight           ::a/wis
   ::intimidation      ::a/cha
   ::investigation     ::a/int
   ::medicine          ::a/wis
   ::nature            ::a/int
   ::perception        ::a/wis
   ::performance       ::a/cha
   ::persuasion        ::a/cha
   ::religion          ::a/int
   ::sleight-of-hand   ::a/dex
   ::stealth           ::a/dex
   ::survival          ::a/wis})

(def skills (-> ability-for-skill keys set))
(s/def ::skill skills)
