(ns illithid.db
  (:require [clojure.spec :as s :include-macros true]
            [illithid.character.core :as c]
            [illithid.spec :refer-macros [set-of]]))

;;; Routes

(s/def :route/key keyword?)
(s/def :route/title string?)
(s/def ::route (s/keys :req-un [:route/key :route/title]))

(s/def :nav/index integer?)
(s/def :nav/routes (s/coll-of ::route))
(s/def ::nav (s/keys :req-un [:nav/index :nav/routes]
                     :opt-un [:route/key]))

;;; New Character

(s/def ::state keyword?)
(defmulti state ::state)

(defmethod state ::home [_] (s/keys))

(s/def ::new-character
  (s/keys :opt [::c/name ::c/class ::c/race ::c/abilities
                ::c/skill-proficiencies
                ::previous-page]))

(defmethod state ::new-character [_] (s/keys :req [::new-character]))

(s/def ::character ::c/character)
(defmethod state ::view-character [_] (s/keys :req [::character]))

(s/def ::last-character-id (s/and int? (complement neg?)))
(s/def ::character-ids (set-of ::c/id))

;;; Database

(s/def ::app-db (s/merge (s/multi-spec state ::state)
                         (s/keys :req [::nav]
                                 :opt [::last-character-id
                                       ::character-ids])))

(def initial
  {::state ::home
   ::nav {:index  0
          :routes [{:key :home
                    :title "Home"}]}})
