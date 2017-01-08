(ns illithid.components.text-input
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch-sync]]
            [illithid.components.native :as native]))

(defn text-input
  "Either:
     [text-input {:sub [:get/value]
                  :pub :set/value}]
   Or:
     [text-input {:sub [:get/value]
                  :pub (fn [x] [:set/value x :plus-some-other-stuff])}]

   All other props supported by react-native.TextInput work as expected, though
   obviously :value will be overridden"
  [{:keys [pub sub] :as props}]
  (let [value (subscribe sub)
        pub (cond (fn? pub) pub
                  (keyword? pub) (fn [x] [pub x]))
        on-change-text (fn [text]
                         (dispatch-sync (pub text))
                         (reagent/flush)
                         (when-let [passed-handler (:on-change-text props)]
                           (passed-handler text)))]
    (fn []
      [native/text-input
       (-> props
           (dissoc :pub sub)
           (assoc :value @value, :on-change-text on-change-text))])))
