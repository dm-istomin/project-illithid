(ns illithid.components.new-character.basic-info
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [illithid.character.race :as race]
            [illithid.character.cclass :as cclass]
            [illithid.character.races :refer [races]]
            [illithid.character.classes :refer [classes]]
            [illithid.subs.new-character :as sub]
            [illithid.handlers.new-character :as pub]
            [illithid.components.new-character.core :refer [render-page]]
            [illithid.components.native :refer [view text]]
            [illithid.components.text-input :refer [text-input]]
            [illithid.components.picker :refer [picker]]))

(defn basic-info []
  (let [character-name (subscribe [::sub/name])
        character-race (subscribe [::sub/race])
        character-class (subscribe [::sub/class])]
    (fn []
      [view
       [text "Name"]
       [text-input {:placeholder "Gundrik Bjornsson"
                    :sub [::sub/name]
                    :pub ::pub/set-name
                    :auto-capitalize "words"
                    :style {:padding 20}}]

       [text "Race"]
       [picker {:prompt "Pick a race for your character"
                :mode "dialog"
                :items (->> races vals (sort-by ::race/name))
                :id-fn ::race/id
                :display-fn ::race/name
                :value (::race/id @character-race)
                :on-change #(dispatch [::pub/set-race %])}]

       [text "Class"]
       [picker {:prompt "Pick a race for your character"
                :mode "dialog"
                :items (->> classes vals (sort-by ::cclass/name))
                :id-fn ::cclass/id
                :display-fn ::cclass/name
                :value (::cclass/id @character-class)
                :on-change #(dispatch [::pub/set-class %])}]])))
