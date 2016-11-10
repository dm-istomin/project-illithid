(ns illithid.db
  (:require [clojure.spec :as s :include-macros true]
            [illithid.character.core :as c]))

(s/def ::character ::c/character)
(s/def ::db (s/keys :opt [::character]))

;; initial state of app-db
(def app-db {::character c/empty-character})
