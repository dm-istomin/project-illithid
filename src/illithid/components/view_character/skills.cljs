(ns illithid.components.view-character.skills
  (:require [illithid.character.core :as ch]
            [illithid.character.skill :as sk]
            [illithid.components.native :refer [view text]]))

(defn display-modifier [modifier]
  (str (when-not (neg? modifier) "+") modifier))

(defn character-skills [character]
  [view [text "SKILLS"]
   (doall
     (for [skill (sort sk/skills)]
       ^{:key skill}
       [view {:style {:flex-direction "row"}}
        [text (sk/skill-name skill)]
        [text " "]
        [text (display-modifier (ch/skill-modifier character skill))]]))])
