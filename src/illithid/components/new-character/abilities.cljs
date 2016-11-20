(ns illithid.components.new-character.abilities
  (:require [re-frame.core :refer [subscribe dispatch]]
            [illithid.character.ability :as a]
            [illithid.subs.new-character :as sub]
            [illithid.handlers.new-character :as pub]
            [illithid.components.new-character.core :refer [render-page]]
            [illithid.components.native
             :refer [view text text-input touchable-highlight]]))

(def ^:private ability-button-styles
  {:padding 5
   :background-color "#ccc"
   :margin-left 2
   :margin-right 2
   :width 30
   :height 30})

(defmethod render-page :abilities []
  (let [abilities (for [ability a/abilities]
                    {:ability  ability
                     :score    (subscribe [::sub/ability ability])
                     :modifier (subscribe [::sub/ability-modifier ability])})]
    (fn []
      [view {:style {:flex-direction "column"
                     :margin 20
                     :align-items "stretch"}}
       (doall
         (for [{:keys [ability score modifier]} abilities]
           [view {:key ability}
            [view {:style {:flex-direction "row"}}
             [text (-> ability name .toUpperCase)]
             [text " " (when-not (neg? @modifier) "+") @modifier]]
            [view {:style {:flex-direction "row"
                           :align-items "center"}}
             [text-input
                {:on-change-text #(dispatch [::pub/set-ability ability
                                             (js/parseInt %)])
                 :value (str @score)
                 :keyboard-type "phone-pad"
                 :select-text-on-focus true
                 :style {:flex 1}}]
             [touchable-highlight
              {:on-press #(dispatch [::pub/inc-ability ability])
               :style ability-button-styles}
              [text {:style {:text-align "center"}} "+"]]
             [touchable-highlight
              {:on-press #(dispatch [::pub/dec-ability ability])
               :style ability-button-styles}
              [text {:style {:text-align "center"}} "-"]]]]))])))
