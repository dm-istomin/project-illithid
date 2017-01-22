(ns illithid.routes
  (:require [re-frame.core :refer [dispatch]]
            [cljs.spec :as s :include-macros true]
            [illithid.specs.reagent]
            [illithid.scenes.home :as home]
            [illithid.scenes.character.index :refer [character-index]]
            [illithid.scenes.character.new :as new-character]
            [illithid.scenes.spells.list :refer [spell-list-scene]]
            [illithid.scenes.spells.detail :refer [spell-detail-scene]]))

(do ;;; Route specs {{{
  (s/def :route/component :reagent/component-fn)
  (s/def :route/title (s/and string? seq))

  (s/def :action/icon string?)
  (s/def :action/onPress fn?)
  (s/def :route/action (s/keys :req-un [:action/icon
                                        :action/onPress]))

  (s/def ::route (s/keys :req-un [:route/component]
                         :opt-un [:route/title :route/action]))

  (s/def ::routes (s/map-of keyword? ::route)))
;;; }}}

(def routes
  {:home {:component home/home}

   :characters-index
   {:component character-index
    :title "Characters"
    :action {:icon "add"
             :onPress #(dispatch [:nav/push :characters-new-basic-info])}}

   :characters-new-basic-info
   {:component new-character/basic-info
    :title "New Character - Basic Info"}

   :characters-new-abilities
   {:component new-character/abilities
    :title "New Character - Abilities"}

   :characters-new-proficiencies
   {:component new-character/proficiencies
    :title "New Character - Proficiencies"}

   :spell-detail {:component spell-detail-scene :title "Spell Detail"}
   :spell-list {:component spell-list-scene :title "Spell List"}})

(s/assert ::routes routes)

;;; Route accessors

(def default-title "Illithid")

(defn route [route-key] (get routes route-key))

(defn component-for [route-key] (get-in routes [route-key :component]))
(defn title-for [route-key] (get-in routes [route-key :title] default-title))
(defn action-for [route-key] (get-in routes [route-key :action]))

(defn to-route
  {:arglists '([route-key] [route-map])}
  [route]
  (cond
    (map? route) (-> route
                     (update :title #(or % (title-for (:key route))))
                     (update :action #(or % (action-for (:key route)))))
    (keyword? route) {:key route
                      :title (title-for route)
                      :action (action-for route)}))

; vim:fdm=marker:fmr={{{,}}}:
