(ns illithid.components.view-character.basic-info
  (:require [re-frame.core :refer [subscribe dispatch]]
            [illithid.character.core :as ch]
            [illithid.character.cclass :as cl]
            [illithid.components.native
             :refer [view text scroll-view touchable-highlight]]
            [illithid.components.view-character.abilities
             :refer [character-abilities]]
            [illithid.components.view-character.skills
             :refer [character-skills]]))

(def top-border {:border-top-width 1
                 :border-top-color "#ccc"})
(def style
  {:header {:flex-direction "row"
            :padding 10}
   :character-name {:font-weight "bold"
                    :font-size 22}
   :subheader (assoc top-border
                     :flex-direction "row"
                     :padding-left 10
                     :padding-top 15
                     :padding-bottom 15
                     :justify-content "space-between")
   :link {:color "#3da7ff"}})

(defn basic-info [character]
  [scroll-view {:style {:background-color "white"
                        :padding 15
                        :flex 1}}
   [view
    [view {:style (:header style)}
     [text {:style (:character-name style)}
      (::ch/name character)]]
    [view {:style (:subheader style)}
     [text
      "Level " (::ch/level character) " "
      (ch/race-name character) " " (ch/class-name character)]
     (when (-> character ::ch/class ::cl/spellcaster?)
       [touchable-highlight
        {:on-press
         #(dispatch
            [:nav/push
             {:key :spell-list
              :params {::cl/id (-> character ::cl/id)}}])}
        [text {:style (:link style)} "Spells"]])]
    [character-abilities (::ch/abilities character)]
    [character-skills character]]])

