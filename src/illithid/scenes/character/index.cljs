(ns illithid.scenes.character.index
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as r]
            [illithid.character.core :as ch]
            [illithid.subs.characters :as sub]
            [illithid.handlers.characters :as pub]
            [illithid.components.native :refer [view text list-view DataSource]]
            [illithid.components.ui :refer [button ripple]]))

(defn character-list-item [cid]
  (let [character (subscribe [::sub/get-character cid])]
    (fn []
      [ripple {:key cid
               :style {:background-color "white"
                       :border-bottom-width 1
                       :border-color "lightgrey"
                       :padding 20}
               :on-press #(dispatch [:nav/push
                                     {:key :character-show
                                      :params {:character-id cid}}])}
       [view
        [text {:style {:font-weight "bold"}}
         (::ch/name @character)]
        [view {:style {:flex-direction "row"}}
         [text (ch/race-name @character)]
         [text " "]
         [text (ch/class-name @character)]]]])))

(def data-source
  (DataSource. #js{:rowHasChanged (constantly false)}))

(defn character-list [characters]
  [list-view
   {:render-row #(r/as-element [character-list-item %])
    :dataSource (.cloneWithRows data-source (apply array characters))}])

(defn character-index []
  (let [characters (subscribe [::sub/character-ids])]
    (dispatch [::pub/load-characters])
    (fn []
      [view
       [character-list @characters]])))
