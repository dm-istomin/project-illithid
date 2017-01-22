(ns illithid.scenes.character.index
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as r]
            [illithid.react :refer [list-view-data-source]]
            [illithid.character.core :as ch]
            [illithid.subs.characters :as sub]
            [illithid.handlers.characters :as pub]
            [illithid.components.native :refer [view text list-view]]
            [illithid.components.ui :refer [button ripple]]))

(defn character-list-item [cid]
  (let [character (subscribe [::sub/get-character cid])]
    (fn []
      [ripple {:key cid
               :style {:background-color "white"
                       :border-bottom-width 1
                       :border-color "lightgrey"
                       :padding 20}}
       [view
        [text {:style {:font-weight "bold"}}
         (::ch/name @character)]
        [view {:style {:flex-direction "row"}}
         [text (ch/race-name @character)]
         [text " "]
         [text (ch/class-name @character)]]]])))

(def data-source
  (list-view-data-source. #js{:rowHasChanged (constantly false)}))

(defn character-list [characters]
  [list-view
   {:render-row #(r/as-element [character-list-item %])
    :dataSource (.cloneWithRows data-source (apply array characters))}])

(defn character-index []
  (let [characters (subscribe [::sub/character-ids])]
    (dispatch [::pub/load-characters])
    (fn []
      [view
       [button {:text "Create New"
                :on-press #(dispatch
                             [:nav/push :characters-new-basic-info])}]
       [character-list @characters]])))
