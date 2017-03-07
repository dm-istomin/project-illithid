(ns illithid.components.view-character.core
  (:require [re-frame.core :refer [subscribe dispatch]]
            [illithid.subs.view-character :as sub]
            [illithid.components.view-character.basic-info :refer [basic-info]]
            [illithid.components.native :refer [view text]]
            [illithid.components.tab-view :refer [tab-view]]
            [illithid.components.spells.list :refer [spell-list]]))

(defn show-character [& _]
  (let [character (subscribe [::sub/current-character])
        spells (subscribe [::sub/prepared-spells])]
    (fn []
      [tab-view
       ^{:tab-label "Basic Info"} [basic-info @character]
       ^{:tab-label "Spells"} [spell-list {:spells @spells}]])))
