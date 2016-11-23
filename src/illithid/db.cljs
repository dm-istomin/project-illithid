(ns illithid.db
  (:require [clojure.spec :as s :include-macros true]
            [illithid.character.core :as c]
            [illithid.spec :refer-macros [set-of]]))

(s/def ::state keyword?)
(defmulti state ::state)

(defmethod state ::welcome [_] (s/keys))

(s/def ::new-character-page #{:basic-info
                              :abilities
                              :proficiencies})
(s/def ::previous-page ::new-character-page)

(s/def ::new-character
  (s/keys :req [::new-character-page]
          :opt [::c/name ::c/class ::c/race ::c/abilities
                ::c/skill-proficiencies
                ::previous-page]))

(defmethod state ::new-character [_] (s/keys :req [::new-character]))

(s/def ::character ::c/character)
(defmethod state ::view-character [_] (s/keys :req [::character]))

(s/def ::last-character-id (s/and int? (complement neg?)))
(s/def ::character-ids (set-of ::c/id))

(s/def ::characters (s/map-of ::c/id ::c/character))

(s/def ::db (s/merge (s/multi-spec state ::state)
                     (s/keys :opt [::last-character-id
                                   ::character-ids
                                   ::characters])))

;; initial state of app-db
(def app-db {::state ::welcome})

