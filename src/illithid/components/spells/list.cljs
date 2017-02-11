(ns illithid.components.spells.list
  (:require [re-frame.core :refer [dispatch]]
            [reagent.core :as r]
            [clojure.string :as string]
            [illithid.components.native
             :refer [view text image list-view DataSource]]
            [illithid.character.spell :as sp]
            [illithid.character.cclass :as c]
            [illithid.character.classes :refer [classes]]
            [illithid.components.spells.logo-image :as logo-image]
            [illithid.components.ui :refer [ripple]]))

;; TODO once we have all the class definition files
;; (defn format-classes [class-list]
;;   (->> class-list
;;       (map classes)
;;       (map ::c/name)
;;       (string/join ", ")))
(defn format-classes [classes]
  (string/join ", " (map #(-> % name string/capitalize) classes)))

(def style {:view {:flex-direction "row"
                   :background-color "white"
                   :border-bottom-width 1
                   :border-color "lightgrey"
                   :align-items "center"
                   :padding 10}
            :img {:height 45
                  :width 45
                  :margin-right 10
                  :border-width 3
                  :border-color "darkgrey"
                  :border-radius 25}
            :text {:font-weight "bold"}})

(defn spell-level [{::sp/keys [level]}]
  (if (zero? level) "Cantrip" (str "Level " level)))

(defn spell-list-item [spell]
  [ripple {:key (::sp/name spell)
           :style (:view style)
           :on-press #(dispatch [:nav/push
                                 {:key :spell-detail
                                  :params {:spell-id (::sp/id spell)}}])}
   [image {:style (:img style) :source logo-image/source}]
   [view {:style {:flex-direction "column"}}
    [text {:style (:text style)} (-> spell ::sp/name .toUpperCase)]
    [text {:style {:color "darkgrey"
                   :font-size 13}}
     (str (spell-level spell) " • "
          (format-classes (::sp/classes spell)))]]])

(def data-source (DataSource. #js{:rowHasChanged (fn [a b] false)}))

(defn render-row [spell]
  (r/as-element [spell-list-item spell]))

(defn spell-list [spells]
  [list-view
   {:render-row #(render-row %)
    :dataSource (.cloneWithRows
                  data-source
                  (->> spells
                       (sort-by (juxt ::sp/level ::sp/name))
                       (apply array)))}])
