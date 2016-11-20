(ns illithid.components.new-character.proficiencies
  (:require [re-frame.core :refer [subscribe dispatch]]
            [clojure.string :as string]
            [illithid.subs.new-character :as sub]
            [illithid.handlers.new-character :as pub]
            [illithid.components.new-character.core :refer [render-page]]
            [illithid.components.native
             :refer [view scroll-view text switch]]))

(defn- skill-switch [skill]
  (let [proficient? (subscribe [::sub/proficient? skill])
        full? (subscribe [::sub/proficiency-full?])]
    (fn [_]
      [view {:key skill
             :style {:flex-direction "row"}}
       [switch {:value @proficient?
                :on-value-change
                #(dispatch [::pub/set-skill-proficiency skill %])
                :disabled (and (not @proficient?) @full?)}]
       [text (string/capitalize (name skill))]])))

(defmethod render-page :proficiencies []
  (let [available-skills (subscribe [::sub/available-skill-proficiencies])
        num-skills (subscribe [::sub/num-skill-proficiencies])
        proficient-skills (subscribe [::sub/proficient-skills])
        full? (subscribe [::sub/proficiency-full?])]
    (fn []
      [view
       [text "Pick " (str @num-skills) " of the following skills"]
       (doall (for [skill @available-skills]
                ^{:key skill}
                [skill-switch skill]))])))

