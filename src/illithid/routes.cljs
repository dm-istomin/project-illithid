(ns illithid.routes
  (:require [clojure.spec :as s :include-macros true]
            [clojure.test.check]
            [clojure.test.check.generators]
            [clojure.test.check.properties]
            [re-frame.core :refer [dispatch]]
            [illithid.character.cclass :as cl]
            [illithid.character.classes :refer [classes]]
            [illithid.specs.reagent]
            [illithid.scenes.home :as home]
            [illithid.scenes.character.index :refer [character-index]]
            [illithid.scenes.character.new :as new-character]
            [illithid.scenes.character.show :refer [character-show-scene]]
            [illithid.scenes.spells.list :refer [spell-list-scene]]
            [illithid.scenes.spells.detail :refer [spell-detail-scene]]))

(do ;;; Route specs {{{
  (s/def :route/component :reagent/component-fn)
  (s/def :route/params map?)
  (s/def :route/title
    (s/or :static-title (s/and string? seq)
          :title-function (s/fspec :args (s/cat :params? (s/? :route/params))
                                   :ret (s/and string? seq))))

  (s/def :action/icon string?)
  (s/def :action/ios-icon string?)
  (s/def :action/onPress fn?)
  (s/def :route/action (s/keys :req-un [:action/icon
                                        :action/ios-icon
                                        :action/onPress]))

  (s/def ::route (s/keys :req-un [:route/component]
                         :opt-un [:route/title :route/action]))

  (s/def ::routes (s/map-of keyword? ::route)))
;;; }}}

(def routes
  (s/assert
    ::routes
    {:home {:component home/home}

     :characters-index
     {:component character-index
      :title "Characters"
      :action {:icon "add"
               :ios-icon "➕"
               :onPress #(dispatch [:nav/push :characters-new-basic-info])}}

     :character-show
     {:component character-show-scene
      :title "View Character"}

     :characters-new-basic-info
     {:component new-character/basic-info
      :title "New Character - Basic Info"}

     :characters-new-abilities
     {:component new-character/abilities
      :title "New Character - Abilities"}

     :characters-new-proficiencies
     {:component new-character/proficiencies
      :title "New Character - Proficiencies"}

     :spell-list
     {:component spell-list-scene
      :title (fn [& [params]]
               (if-let [cls (some-> params ::cl/id classes)]
                 (str (::cl/name cls) " Spells")
                 "Spell List"))}

     :spell-detail {:component spell-detail-scene :title "Spell Detail"}
     :class-spell-list {:component spell-list-scene :title (fn [params?])}}))

;;; Route accessors

(def default-title "Illithid")

(defn route [route-key] (get routes route-key))

(defn component-for [route-key] (get-in routes [route-key :component]))

(defn title-for [route-key & [params]]
  (let [title (get-in routes [route-key :title] default-title)]
    (if (ifn? title)
      (apply title (if params [params] []))
      (str title))))

(defn action-for [route-key] (get-in routes [route-key :action]))

(defn to-route
  {:arglists '([route-key] [route-map])}
  [route]
  (cond
    (map? route)
    (-> route
        (update :title #(or % (title-for (:key route) (:params route))))
        (update :action #(or % (action-for (:key route)))))

    (keyword? route)
    {:key route
     :title (title-for route)
     :action (action-for route)}))

; vim:fdm=marker:fmr={{{,}}}:
