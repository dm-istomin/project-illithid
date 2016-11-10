(ns illithid.character.core
  (:require [clojure.spec :as s #?@(:cljs [:include-macros true])]
            [illithid.spec #?@(:clj  [:refer [set-of]]
                               :cljs [:refer-macros [set-of]])]
            [illithid.character.cclass :as c]
            [illithid.character.ability :as a]
            [illithid.character.skill :as sk]))

(s/def ::name (s/and string? seq))

(s/def ::level (s/and int? pos? #(< % c/max-level)))

(s/def ::class ::c/class)

(s/def ::abilities
  (s/keys :req [::a/str ::a/dex ::a/con ::a/int ::a/wis ::a/cha]))

(s/def ::skill-proficiencies (set-of ::sk/skill))
(s/def ::saving-throw-proficiencies (set-of ::a/ability))

(s/def ::character
  (s/keys :req [::name
                ::level
                ::class
                ::abilities
                ::skill-proficiencies
                ::saving-throw-proficiencies]))

(defn ability-modifier [character ability]
  (-> character ::abilities ability a/modifier))

(s/fdef ability-modifier
        :args (s/cat :character ::character
                     :ability   ::a/ability)
        :ret ::a/modifier)

(defn proficient? [character skill-or-ability]
  (boolean
    (cond
      (a/ability? skill-or-ability)
      ((::saving-throw-proficiencies character) skill-or-ability)

      (sk/skill? skill-or-ability)
      ((::skill-proficiencies character) skill-or-ability))))

(s/fdef proficient?
        :args (s/cat :character ::character
                     :skill-or-ability (s/or :skill   ::sk/skill
                                             :ability ::a/ability))
        :ret boolean?)

(defn proficiency-bonus [character]
  (-> character ::level ((-> character ::class ::c/proficiency-bonuses))))

(defn skill-modifier [character skill]
  (+ (->> skill sk/ability-for-skill (ability-modifier character))
     (if (proficient? character skill) (proficiency-bonus character) 0)))

(s/fdef skill-modifier
        :args (s/cat :character ::character :skill ::sk/skill)
        :ret  int?)
