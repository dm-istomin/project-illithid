(ns illithid.components.view-character.prepared-spells
  (:require [re-frame.core :refer [subscribe dispatch]]
            [illithid.subs.view-character :as sub]
            [illithid.handlers.view-character :as pub]
            [illithid.components.native :refer [view text touchable-highlight]]
            [illithid.components.spells.list :refer [spell-list]]))

(defn- no-spells []
  [view {:style {:padding-top 200}}
   [text {:style {:text-align "center"
                  :padding-bottom 5}}
    "You have not prepared any spells"]
   [touchable-highlight
    {:on-press #(dispatch [::pub/prepare-spells])
     :style {:padding-top 10
             :padding-bottom 10}}
    [text {:style {:text-align "center"}}
     "Prepare Spells"]]])

(defn prepared-spell-list []
  (let [spells (subscribe [::sub/prepared-spells])]
    (fn []
      (if (empty? @spells)
        [no-spells]
        [spell-list {:spells @spells}]))))

