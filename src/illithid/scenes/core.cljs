(ns illithid.scenes.core
  (:require [re-frame.core :refer [dispatch]]
            [illithid.components.native :refer [view text]]
            [illithid.components.spells.detail :refer [spell-detail]]
            [illithid.components.spells.list :refer [spell-list]]
            [illithid.character.spells :refer [spells]]
            [illithid.components.ui :refer [button
                                            card
                                            card-body
                                            subheader]]))

(defn home [params]
  [view
     [text "Welcome to Project Illithid! This is a general navigation screen."]
     [button {:text "Next Scene"
              :on-press #(dispatch [:nav/push {:key :another-scene
                                              :title "Another Scene"}])}]
     [button {:text "Spell List"
              :on-press #(dispatch [:nav/push {:key :spell-list
                                              :title "Spell List"}])}]
     [button {:text "Character Creation Demo"
              :on-press #(dispatch [:nav/push {:key :character-creation-demo
                                              :title "Character Creation"}])}]
     [button {:text "Ability Detail View"
              :on-press #(dispatch [:nav/push
                                    {:key :ability-detail-demo
                                     :title "Spell Detail"}])}]])

(defn another-scene [params]
  [view
   [card
    [card-body
     [text "This is another scene"]
     [button {:text "Go Back"
              :on-press #(dispatch [:nav/pop nil ])}]]]])

(defn ability-detail-demo []
  (spell-detail {:name "Magic Missile"
                 :range "120 feet"
                 :level 1
                 :classes #{:sorcerer :wizard}
                 :components #{:V :S}
                 :school :evocation
                 :duration nil
                 :description "You create three glowing darts of magical force. Each dart hits a creature of your choice that you can see within range. A dart deals 1d4+1 force damage to its target. The darts all strike simultaneously and you can direct them to hit one creature or several."
                 :casting-time "1 action"}))

(defn spell-list-scene [params] (spell-list spells))

(defn spell-detail-scene [params]
  (def spell-data {:name (aget params "spell-data" "name")
                   :range (aget params "spell-data" "range")
                   :level (aget params "spell-data" "level")
                   :classes (aget params "spell-data" "classes")
                   :components (aget params "spell-data" "components")
                   :material-component (aget params "spell-data" "material-component")
                   :school (aget params "spell-data" "school")
                   :duration (aget params "spell-data" "duration")
                   :description (aget params "spell-data" "description")
                   :casting-time (aget params "spell-data" "casting_time")})
  (spell-detail spell-data))

(defn character-creation-demo [params]
  [view
   [card
    [card-body
      [text "This is a the character creation demo"]]]])

(def routes
  {:home home
   :another-scene another-scene
   :ability-detail-demo ability-detail-demo
   :spell-detail spell-detail-scene
   :spell-list spell-list-scene
   :character-creation-demo character-creation-demo})

