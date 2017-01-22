(ns illithid.scenes.spells.detail
  (:require [illithid.components.spells.detail :refer [spell-detail]]))

(defn spell-detail-scene [params]
  (let [spell-data (-> params
                       (aget "spell-data")
                       (js->clj :keywordize-keys true))]
    [spell-detail spell-data]))
