(ns illithid.character.core
  (:refer-clojure :exclude [class])
  (:require [clojure.spec :as s #?@(:cljs [:include-macros true])]
            #?(:clj  [clojure.spec.gen :as gen]
               :cljs [cljs.spec.impl.gen :as gen])
            [illithid.spec #?@(:clj  [:refer [set-of]]
                               :cljs [:refer-macros [set-of]])]
            [illithid.character.race :as r]
            [illithid.character.cclass :as c]
            [illithid.character.ability :as a]
            [illithid.character.skill :as sk]
            [illithid.character.classes :refer [classes]]
            [illithid.character.races :refer [races]]))

(s/def ::id (s/with-gen (s/and keyword? #(= "illithid.character" (namespace %)))
              #(gen/fmap (partial keyword "illithid.character")
                         (s/gen (s/and string? seq)))))
(s/def ::name string?)
(s/def ::level (s/and int? pos? #(< % c/max-level)))
(s/def ::race ::r/race)

(s/def ::abilities
  (s/keys :req [::a/str ::a/dex ::a/con ::a/int ::a/wis ::a/cha]))

(s/def ::skill-proficiencies (set-of ::sk/skill))
(s/def ::saving-throw-proficiencies (set-of ::a/ability))
(s/def ::prepared-spells (set-of :illithid.character.spell/id))

(s/def ::character
  (s/keys :req [::name
                ::level
                ::c/id
                ::r/id
                ::abilities
                ::skill-proficiencies
                ::saving-throw-proficiencies]
          :opt [::prepared-spells]))

(def empty-character
  {::name "-"
   ::level 1
   ::class c/empty-class
   ::abilities {::a/str 1
                ::a/dex 1
                ::a/con 1
                ::a/int 1
                ::a/wis 1
                ::a/cha 1}
   ::skill-proficiencies #{}
   ::saving-throw-proficiencies #{}})

(defn class [character]
  (get classes (::c/id character)))

(defn race [character]
  (get races (::r/id character)))

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
  (-> character ::level ((-> character class ::c/proficiency-bonuses))))

(defn skill-modifier [character skill]
  (+ (->> skill sk/ability-for-skill (ability-modifier character))
     (if (proficient? character skill) (proficiency-bonus character) 0)))

(s/fdef skill-modifier
        :args (s/cat :character ::character :skill ::sk/skill)
        :ret  int?)

(defn race-name  [character] (-> character race ::r/name))
(defn class-name [character] (-> character class ::c/name))

(defn num-prepared-spells [character]
  (let [cls (class character)]
    (if-not (::c/spellcaster? cls) 0
      (+ (proficiency-bonus character)
         (when-let [{::c/keys [spellcasting-ability]} cls]
           (ability-modifier character spellcasting-ability))))))

(defn spell-slots [character]
  (let [{::c/keys [spellcaster? spell-slots]} (class character)
        ten-zeros (->> 0 repeat (take 10) vec)]
    (if-not spellcaster? ten-zeros
      (nth spell-slots (::level character) ten-zeros))))

(defn max-spell-level [character]
  (let [slots (spell-slots character)]
    (assert (= slots (-> slots sort reverse)))
    ;; the index of the last nonzero element of `slots`
    ;; ie, one below the index of the first zero element of `slots`
    (->> slots
         (map-indexed vector)
         (filter #(-> % second zero?)) first
         first dec)))

