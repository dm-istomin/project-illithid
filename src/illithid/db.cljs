(ns illithid.db
  (:require [clojure.spec :as s :include-macros true]
            [illithid.character.core :as c]))

(s/def ::state keyword?)
(defmulti state ::state)

(s/def ::new-character (s/keys :opt [::c/name ::c/class ::c/race]))

(defmethod state ::new-character [_]
  (s/keys :req [::new-character]))

(s/def ::character ::c/character)
(s/def ::db (s/multi-spec state ::state))

;; initial state of app-db
(def app-db {::state ::new-character
             ::new-character {}})
