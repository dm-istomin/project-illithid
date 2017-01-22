(ns illithid.react)

(def react (js/require "react-native"))
(set! js/window.React react)

(defn alert [title] (.alert (.-Alert react) title))

(def app-registry (.-AppRegistry react))

(def dimensions (.-Dimensions react))

(def animated (.-Animated react))

(def platform (.-Platform react))

(def list-view (.-ListView react))
(def list-view-data-source (.-DataSource list-view))

