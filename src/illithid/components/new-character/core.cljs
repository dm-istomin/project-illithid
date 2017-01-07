(ns illithid.components.new-character.core
  (:require [re-frame.core :refer [subscribe dispatch]]
            [illithid.db :as db]
            [illithid.subs.new-character :as sub]
            [illithid.handlers.new-character :as pub]))

(defmulti render-page identity)

(defn new-character []
  (let [page (subscribe [::sub/page])
        prev-page (subscribe [::sub/previous-page])]
    ;; See http://stackoverflow.com/questions/33299746/why-are-multi-methods-not-working-as-functions-for-reagent-re-frame
    (fn [] ^{:key @page} [render-page @page])))
