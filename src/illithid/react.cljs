(ns illithid.react)

(def react (if (exists? js/require)
             (js/require "react-native")
             js/React))
(def native? (exists? js/require))
(set! js/window.React react)

(defn alert [title] (.alert (.-Alert react) title))

(def app-registry (.-AppRegistry react))

(def dimensions (.-Dimensions react))

(def animated (.-Animated react))

(def platform (.-Platform react))

(def action-sheet-ios (.-ActionSheetIOS react))

