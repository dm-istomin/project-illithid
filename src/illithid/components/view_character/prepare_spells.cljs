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
  (let [max-prepared (reaction (ch/num-prepared-spells character))
        num-prepared (subscribe [::sub/num-prepared])
        full?        (reaction (>= @num-prepared @max-prepared))
        max-spell-level (reaction (ch/max-spell-level character))]
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
        {:spells (->> character ch/class cl/spells
                      (filter #(>= @max-spell-level (::sp/level %))))
         :render-action spell-prepare-switch}]])))
