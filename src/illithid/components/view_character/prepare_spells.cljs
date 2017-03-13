(ns illithid.components.view-character.prepare-spells
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.ratom :refer-macros [reaction]]
            [illithid.subs.prepared-spells :as sub]
            [illithid.handlers.prepare-spells :as pub]
            [illithid.character.core :as ch]
            [illithid.character.cclass :as cl]
            [illithid.character.spell :as sp]
            [illithid.components.native :refer [view text switch]]
            [illithid.components.spells.list :refer [spell-list]]))

(def style
  {:header {:padding 10
            :background-color "#eee"
            :text-align "center"}})

(defn- spell-prepare-switch [{spell-id ::sp/id}]
  (let [prepared? (subscribe [::sub/prepared? spell-id])]
    (fn [_]
      [switch {:value @prepared?
               :on-value-change #(dispatch [::pub/set-prepared spell-id %])}])))

(defn prepare-spells [character]
  (let [max-prepared     (subscribe [::sub/max-prepared])
        num-prepared     (subscribe [::sub/num-prepared])
        full?            (subscribe [::sub/full?])
        max-spell-level  (subscribe [::sub/max-spell-level])
        available-spells (subscribe [::sub/available-spells])]
    (dispatch [::pub/initialize])
    (fn []
      [view
       [text {:style (:header style)}
        (cond
          (zero? @num-prepared) (str "Prepare " @max-prepared
                                     " spells, up to level " @max-spell-level)
          @full? "All spells prepared"
          :else (str @num-prepared " out of "
                     @max-prepared " spells prepared"))]
       [spell-list
        {:spells @available-spells
         :render-action spell-prepare-switch}]])))
