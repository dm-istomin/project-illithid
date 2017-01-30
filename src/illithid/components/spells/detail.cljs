(ns illithid.components.spells.detail
  (:require [re-frame.core :refer [dispatch]]
            [clojure.string :as string]
            [illithid.components.native :refer [view text image scroll-view]]
            [illithid.character.spell :as sp]
            [illithid.components.ui :refer [icon ripple]]))

(def logo-img (when (exists? js/require)
                (js/require "./images/magic-missle.png")))

(def style {:view {:padding-left 15
                   :padding-right 15
                   :background-color "white"}
            :title {:font-weight "bold"
                    :text-align "right"
                    :margin-right 5}
            :img {:border-radius 75
                  :border-width 5
                  :border-color "#ccc"
                  :height 115
                  :width 115}
            :header {:font-weight "bold"
                     :width 210
                     :font-size 22}
            :dmg-type-label {:background-color "navy"
                             :font-size 12
                             :color "white"
                             :text-align "center"
                             :padding-bottom -5
                             :padding-left 5
                             :padding-right 5
                             :margin-left 5
                             :font-weight "bold"}
            :table {:flex-wrap "wrap"
                    :flex-direction "row"
                    :justify-content "center"
                    :border-color "#ddd"
                    :border-bottom-width 3
                    :border-top-width 3
                    :padding 10
                    :padding-left 0}
            :body {:padding 20}})

(defn format-spell-school [school] (-> school name string/capitalize))

(defn format-classes [classes]
  (string/join ", " (map #(-> % name string/capitalize) classes)))

(defn format-spell-components [components]
  (->> components
       (map {:V "Verbal"
             :S "Somatic"
             :M "Material"})
       sort
       (string/join ", ")))

(defn spell-detail [spell]
  [scroll-view {:style (:view style)}
   [view {:style {:flex-direction "row"
                  :padding 10}}
    [image {:source logo-img :style (:img style)}]
    [view {:style {:margin-left 15}}
      [text {:style (:header style)} (::sp/name spell)]
      [view {:style {:flex-direction "row" :margin-top 15}}
        [icon {:name "redo" :size 16 :style {:margin-right 3}}]
        [text {:style {:margin-right 5 :font-weight "bold"}} "RANGE"]
        [text (::sp/range spell)]]
      [view {:style {:flex-direction "row"}}
        [icon {:name "timer" :size 16 :style {:margin-right 3}}]
        [text {:style {:margin-right 5 :font-weight "bold"}} "CASTING TIME"]
        [text (or (::sp/casting-time spell) "–")]]]]
    [view {:style (:table style)}
     [view {:style {:width 120}}
      [text {:style (:title style)} "LEVEL"]
      [text {:style (:title style)} "CLASSES"]
      [text {:style (:title style)} "SCHOOL"]
      [text {:style (:title style)} "DURATION"]
      [text {:style (:title style)} "COMPONENTS"]
      (if (::sp/material-component spell)
         [text {:style (:title style)} "MATERIAL"])]
     [view {:style {:width 180}}
      [text (::sp/level spell)]
      [text (format-classes (::sp/classes spell))]
      [text (format-spell-school (::sp/school spell))]
      [text (or (::sp/duration spell) "–")]
      [text (format-spell-components (::sp/components spell))]
      (if-let [material-component (::sp/material-component spell)]
        [text {:style {:font-style "italic"}}
          material-component])]]
    [text {:style (:body style)} (::sp/description spell)]])
