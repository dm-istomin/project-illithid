(ns illithid.character.spell
  (:require [clojure.spec :as s #?@(:cljs [:include-macros true])]
            [illithid.spec #?@(:clj  [:refer [set-of]]
                               :cljs [:refer-macros [set-of]])]))

(s/def ::id keyword?)
(s/def ::name (s/and string? seq))
(s/def ::level (s/and int? #(<= 0 % 9)))
(s/def ::classes (set-of :illithid.character.cclass/id :min-count 1))
(s/def ::school #{:school/abjuration
                  :school/conjuration
                  :school/divination
                  :school/enchantment
                  :school/evocation
                  :school/illusion
                  :school/necromancy
                  :school/transmutation})
(s/def ::ritual? boolean?)
(s/def ::concentration? boolean?)
(s/def ::component #{:V :S :M})
(s/def ::components (set-of ::component :min-count 1))
(s/def ::description (s/and string? seq))
(s/def ::material-component (s/and string? seq))
;; TODO: more structured data for these four?
(s/def ::at-higher-levels (s/and string? seq))
(s/def ::range (s/and string? seq))
(s/def ::duration (s/and string? seq))
(s/def ::casting-time (s/and string? seq))

(s/def ::spell
  (s/keys :req [::id ::name ::level ::classes ::school ::ritual?
                ::concentration? ::components ::description ::range ::duration
                ::casting-time]
          :opt [::at-higher-levels]))
