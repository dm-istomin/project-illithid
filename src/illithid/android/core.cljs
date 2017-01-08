(ns illithid.android.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [illithid.handlers.nav]
            [illithid.subs.nav]
            [illithid.scenes.core :as s]
            [illithid.scenes.not-found :refer [not-found]]
            [illithid.components.ui :refer [toolbar]]))

(def react-native (js/require "react-native"))

(def app-registry (.-AppRegistry react-native))
(def view (r/adapt-react-class (.-View react-native)))
(def card-stack (r/adapt-react-class
                  (-> react-native .-NavigationExperimental .-CardStack)))

(def style
  {:view {:flex-direction "column"
          :height 600
          :border-color "lightgrey"
          :border-top-width 1
          :margin-top 56}
   :card-stack {:flex 1}})

(defn header [props]
  (if (= (aget props "scene" "route" "key") "home")
    [toolbar {:title (aget props "scene" "route" "title")
              :overrides {:background-color "#08708a"
                          :color "#f5f5f5"
                          :left-icon-color "#f5f5f5"}}]
    [toolbar {:title (aget props "scene" "route" "title")
              :overrides {:background-color "#08708a"
                          :color "#f5f5f5"
                          :left-icon-color "#f5f5f5"}
              :on-icon-press #(dispatch [:nav/pop nil])
              :icon "arrow-back"}]))

(defn scene [props]
  (let [route (aget props "scene" "route" "key")
        title (aget props "scene" "route" "title")
        params (aget props "scene" "route" "params")]
     [view (:view style)
      (if-let [route-view (get s/routes (keyword route))]
        [route-view params]
        [not-found])]))

(defn app-root []
  (let [nav (subscribe [:nav/state])]
    (fn []
      [card-stack {:on-navigate-back #(dispatch [:nav/pop nil])
                   :render-header    #(r/as-element (header %))
                   :navigation-state @nav
                   :style            (:card-stack style)
                   :render-scene     #(r/as-element (scene %))}])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "Illithid" #(r/reactify-component app-root)))

