(ns illithid.components.view-character.core
  (:require [re-frame.core :refer [subscribe]]
            [illithid.subs.view-character :as sub]
            [illithid.components.view-character.basic-info :refer [basic-info]]
            [illithid.components.native :refer [view text]]
            [illithid.components.tab-view :refer [tab-view]]
            [illithid.components.view-character.prepared-spells
             :refer [prepared-spell-list]]))

(defn show-character [& _]
  (let [character (subscribe [::sub/current-character])]
    (fn []
      [tab-view
       ^{:tab-label "Basic Info"} [basic-info @character]
       ^{:tab-label "Spells"} [prepared-spell-list]])))
