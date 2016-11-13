(ns illithid.components.new-character.basic-info
  (:refer-clojure :exclude [atom])
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch]]
            [illithid.character.race :as race]
            [illithid.character.races :refer [races]]
            [illithid.subs.new-character :as sub]
            [illithid.handlers.new-character :as pub]
            [illithid.components.native
             :refer [view text text-input picker picker-item]]))

(defn basic-info []
  (let [character-name (subscribe [::sub/name])
        character-race (subscribe [::sub/race])]
    (fn []
      [view
       [text "Name"]
       [text-input
        {:placeholder "Gundrik Bjornsson"
         :value @character-name
         :on-change-text #(dispatch [::pub/set-name %])
         :auto-capitalize "words"}]
       [text "Race"]
       [picker {:prompt "Pick a race for your character"
                :mode "dialog"
                :selected-value (::race/id @character-race)
                :on-value-change #(dispatch [::pub/set-race (keyword %)])}
        (clj->js
          (for [[id race] races]
            (r/as-element [picker-item {:label (::race/name race)
                                        :value id
                                        :key id}])))]
       [text "Class"]])))
