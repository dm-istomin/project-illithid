(ns illithid.db
  (:require [clojure.spec :as s #?@(:cljs [:include-macros true])]
            #?(:clj  [clojure.spec.gen :as gen]
               :cljs [cljs.spec.impl.gen :as gen])
            [illithid.spec #?@(:clj  [:refer [set-of]]
                               :cljs [:refer-macros [set-of]])]
            [taoensso.encore :refer [xdistinct]]
            [illithid.character.core :as c]
            [illithid.character.cclass :as cl]
            [illithid.character.race :as r]
            [illithid.character.spell :as sp]))

;;; Routes

(s/def :route/key keyword?)
(s/def :route/title string?)
(s/def :route/params map?)
(s/def ::route (s/keys :req-un [:route/key :route/title]
                       :opt-un [:route/params]))

(s/def :nav/index integer?)
(s/def :nav/routes
  (s/with-gen
      (s/and (s/coll-of ::route :kind vector? :into [])
             #(or (empty? %) (apply distinct? (map :key %))))
      #(gen/fmap (partial into [] (xdistinct :key))
                 (s/gen (s/coll-of ::route)))))
(s/def ::nav (s/keys :req-un [:nav/index :nav/routes]
                     :opt-un [:route/key]))

;;; New Character

(s/def ::state #{::home ::new-character ::prepare-spells})
(defmulti state ::state)

(defmethod state ::home [_] (s/keys))

(s/def ::new-character
  (s/keys :opt [::c/name ::cl/id ::r/id ::c/abilities
                ::c/skill-proficiencies]))

(defmethod state ::new-character [_] (s/keys :req [::new-character]))

;;; Preparing spells

(s/def ::prepared-spells (set-of ::sp/id))
(defmethod state ::prepare-spells [_] (s/keys :req [::prepared-spells]))

;;;

(s/def ::last-character-id (s/and int? (complement neg?)))
(s/def ::character-ids (set-of ::c/id))

(s/def ::characters (s/map-of ::c/id ::c/character))

;;; Database

(s/def ::app-db (s/merge (s/multi-spec state ::state)
                         (s/keys :req [::nav]
                                 :opt [::last-character-id
                                       ::character-ids
                                       ::characters])))

(def initial
  {::state ::home
   ::nav {:index  0
          :routes [{:key :home
                    :title "Home"}]}})
