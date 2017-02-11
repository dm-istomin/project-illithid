(ns illithid.components.view-character.core
  (:require [reagent.core :as r]
            [illithid.components.view-character.basic-info :refer [basic-info]]
            [illithid.components.native :refer [view text]]
            [illithid.components.tab-view :refer [tab-view]]
            [illithid.scenes.spells.list :refer [spell-list-scene]]))

(defn show-character [character]
  [tab-view
   ^{:tab-label "Basic Info"} [basic-info character]
   ^{:tab-label "Spells"} [spell-list-scene #{:id "cleric"}]])
