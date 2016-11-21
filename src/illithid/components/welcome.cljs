(ns illithid.components.welcome
  (:require [re-frame.core :refer [dispatch]]
            [illithid.handlers :as pub]
            [illithid.components.native
             :refer [view text touchable-highlight]]))

(defn welcome []
  [view
   [text "Welcome to Illithid"]
   [touchable-highlight {:on-press #(dispatch [::pub/create-character])}
    [text "Create a new character"]]])
