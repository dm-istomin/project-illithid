(ns illithid.core
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [illithid.handlers]
            [illithid.handlers.nav]
            [illithid.subs.nav]
            [illithid.react :refer [react]]
            [illithid.platform :as platform :include-macros true]
            [illithid.routes :as routes]
            [illithid.scenes.not-found :refer [not-found]]
            [illithid.components.native :refer [view text card-stack]]
            [illithid.components.header :refer [header]]))

(def app-registry (.-AppRegistry react))

(def style
  {:view {:flex-direction "column"
          :flex 1
          :border-color "lightgrey"
          :border-top-width 1
          :margin-top (platform/cond :ios 0 :android 56)}
   :card-stack {:flex 1}})

(defn scene [props]
  (let [route (aget props "scene" "route" "key")
        title (aget props "scene" "route" "title")
        params (aget props "scene" "route" "params")]
     [view (:view style)
      (if-let [route-view (routes/component-for (keyword route))]
        [route-view params]
        [not-found])]))

(defn app-root []
  (let [nav (subscribe [:nav/state])]
    (fn []
      [card-stack {:on-navigate-back #(dispatch [:nav/pop])
                   :render-header    #(r/as-element [header %])
                   :navigation-state @nav
                   :style            (:card-stack style)
                   :render-scene     #(r/as-element [scene %])}])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "Illithid" #(r/reactify-component app-root)))
