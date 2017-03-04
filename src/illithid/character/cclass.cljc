(ns illithid.character.cclass
  (:require [clojure.spec :as s #?@(:cljs [:include-macros true])]
            [illithid.spec #?@(:clj  [:refer [set-of]]
                               :cljs [:refer-macros [set-of]])]
            #?(:clj  [clojure.spec.gen :as gen]
               :cljs [cljs.spec.impl.gen :as gen])
            [illithid.die :as die]
            [illithid.character.skill :as sk]
            [illithid.character.ability :as a]
            [illithid.character.spells :as spells]))

(def max-level 20)

(s/def ::id keyword?)
(s/def ::name (s/and string? seq))
(s/def ::hit-die ::die/die)
(s/def ::first-level-hit-points int?)

(s/def :skill-proficiencies/available (set-of ::sk/skill))
(s/def :skill-proficiencies/number (s/and int? pos?))
(s/def ::skill-proficiencies (s/keys :req [:skill-proficiencies/available
                                           :skill-proficiencies/number]))

(s/def ::saving-throw-proficiencies (set-of ::a/ability))

(s/def ::proficiency-bonuses
  (s/with-gen (s/and (s/coll-of (s/and int? pos?)
                                :count max-level
                                :kind vector? :into [])
                     #(= % (sort %)))
    #(gen/fmap (comp vec sort)
               (s/gen (s/coll-of (s/and int? pos?) :count max-level)))))

(s/def ::spellcaster? boolean?)
(s/def ::spellcasting-ability ::a/ability)

(s/def ::spell-slot-count (s/and int? (complement neg?)))
(s/def ::level-spell-slots (s/coll-of ::spell-slot-count
                                      :into []
                                      :kind vector?
                                      :count 10))
(s/def ::spell-slots (s/coll-of ::level-spell-slots
                                :into []
                                :kind vector?
                                :count max-level))

(s/def ::class
  (s/keys :req [::id
                ::name
                ::hit-die
                ::first-level-hit-points
                ::proficiency-bonuses
                ::skill-proficiencies
                ::saving-throw-proficiencies
                ::spellcaster?]
          :opt [::spellcasting-ability ::spell-slots]))

(def empty-class
  {::name "-"
   ::hit-die 2
   ::first-level-hit-points 1
   ::proficiency-bonuses (vec (range 1 (inc max-level)))})

(defn spells [{::keys [id]}]
  (->>
    spells/spells vals (filter #((:illithid.character.spell/classes %) id))))
(s/fdef spells
        :args (s/cat :class ::class)
        :ret (s/coll-of :illithid.character.spell/spell))
