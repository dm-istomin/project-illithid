(ns illithid.android.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [illithid.handlers]
            [illithid.subs]
            [illithid.character.ability :as a]
            [illithid.components.new-character.basic-info :refer [basic-info]]))

(def react-native (js/require "react-native"))

(def app-registry (.-AppRegistry react-native))
(def text (r/adapt-react-class (.-Text react-native)))
(def view (r/adapt-react-class (.-View react-native)))
(def image (r/adapt-react-class (.-Image react-native)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight react-native)))
(def text-input (r/adapt-react-class (.-TextInput react-native)))

(def logo-img (js/require "./images/cljs.png"))

(defn alert [title]
      (.alert (.-Alert react-native) title))

(defn app-root []
  (let [abilities (for [ability a/abilities]
                    {:ability  ability
                     :score    (subscribe [:get-ability ability])
                     :modifier (subscribe [:get-ability-modifier ability])})]
    (fn []
      [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
       (doall
         (for [{:keys [ability score modifier]} abilities]
           [view {:key ability}
            [text (name ability)]
            [text (when (pos? @modifier) "+") @modifier]
            [text-input
             {:on-change-text #(dispatch [:set-ability ability (js/parseInt %)])
              :value (str @score)}]]))])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "Illithid" #(r/reactify-component app-root)))
