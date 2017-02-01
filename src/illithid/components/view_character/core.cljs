(ns illithid.components.view-character.core
  (:require [re-frame.core :refer [subscribe dispatch]]
            [illithid.character.core :as ch]
            [illithid.components.native :refer [view text scroll-view]]
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
                     :padding-bottom 15)})

(defn show-character [character]
  [scroll-view {:style {:background-color "white"
                        :padding 15}}
   [view
    [view {:style (:header style)}
     [text {:style (:character-name style)}
      (::ch/name character)]]
    [view {:style (:subheader style)}
     [text
      "Level " (::ch/level character) " "
      (ch/race-name character) " " (ch/class-name character)]]
    [character-abilities (::ch/abilities character)]
    [character-skills character]]])

