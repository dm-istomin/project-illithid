(ns illithid.components.view-character.abilities
  (:require [illithid.character.ability :as a]
            [illithid.components.native :refer [view text]]))

(def style
  {:abilities {:border-top-width 1
               :border-top-color "#ccc"
               :padding-top 10
               :padding-bottom 10
               :flex-direction "row"
               :flex-wrap "wrap"
               :justify-content "center"}
   :ability-title {:font-weight "bold"
                   :text-align "right"
                   :margin-right 5}})

(defn show-ability [ability-score]
  [view {:style {:flex-direction "row"}}
   [text {:style {:font-weight "bold"}}
    (str ability-score)]
   [text
    (let [modifier (a/modifier ability-score)]
      (if (zero? modifier) "" modifier))]])

(defn character-abilities [abilities]
  (let [sorted (->> abilities seq (sort-by first))]
    [view {:style (:abilities style)}
     [view
      (doall
        (for [ability (map first sorted)]
          ^{:key ability}
          [text {:style (:ability-title style)}
           (a/short-name ability)]))]
     [view
      (doall
        (for [[ability score] sorted]
          ^{:key ability}
          [show-ability score]))]]))
