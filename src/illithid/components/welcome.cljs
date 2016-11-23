(ns illithid.components.welcome
  (:require [re-frame.core :refer [subscribe dispatch]]
            [illithid.handlers :as pub]
            [illithid.subs.welcome :as sub]
            [illithid.character.core :as c]
            [illithid.components.native
             :refer [view text touchable-highlight]]))

(defn welcome []
  (let [characters (subscribe [::sub/characters])]
    (fn []
      [view
       [text "Welcome to Illithid"]
       [text "Characters"]
       (doall
         (for [[_ character] @characters]
           ^{:key (::c/id character)}
           [text (::c/name character)]))
       [touchable-highlight {:on-press #(dispatch [::pub/create-character])}
        [text "Create a new character"]]])))
