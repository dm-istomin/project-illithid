(ns illithid.components.new-character.core
  (:require [re-frame.core :refer [subscribe dispatch]]
            [illithid.db :as db]
            [illithid.subs.new-character :as sub]
            [illithid.handlers.new-character :as pub]
            [illithid.components.native :refer [add-back-listener]]))

(defmulti render-page identity)

(defn new-character []
  (let [page (subscribe [::sub/page])
        prev-page (subscribe [::sub/previous-page])]
    (add-back-listener (fn [] (if-let [prev @prev-page]
                                (do (dispatch [::pub/set-page prev]) true)
                                false)))
    ;; See http://stackoverflow.com/questions/33299746/why-are-multi-methods-not-working-as-functions-for-reagent-re-frame
    (fn [] ^{:key @page} [render-page @page])))
