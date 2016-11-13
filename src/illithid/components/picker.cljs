(ns illithid.components.picker
  (:require [reagent.core :as r]
            [illithid.components.native :as native]))

(defn picker
  "Wrapper around a react-native picker simplifying item selection

   Options:

   :items       - items to display in the picker
   :id-fn       - function to apply to an item to get its unique identifier
   :display-fn  - function to apply to an item to get its label
   :value       - current value of the picker
   :on-change   - called with the (keywordized) ID of the new item when the
                  selection is changed"

  [{:keys [items id-fn display-fn value on-change] :as props
    :or {id-fn identity
         display-fn identity
         on-change identity}}]
  [native/picker (-> props
                     (dissoc :items :id-fn :display-fn :value :on-change)
                     (assoc :selected-value value
                            ;; NOTE: can't use `comp` for the following since
                            ;; the function is actually called with two
                            ;; arguments
                            :on-value-change #(-> % keyword on-change)))
   (clj->js
     (for [item items]
       (let [id (id-fn item)]
         (r/as-element [native/picker-item
                        {:label (display-fn item) :value id :key id}]))))])
