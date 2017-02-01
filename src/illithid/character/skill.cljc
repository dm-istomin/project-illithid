(ns illithid.character.skill
  (:require [clojure.spec :as s #?@(:cljs [:include-macros true])]
            [illithid.character.ability :as a]))

(def skill-data
  {::acrobatics        {::ability ::a/dex
                        ::name "Acrobatics"}
   ::animal-handling   {::ability ::a/wis
                        ::name "Animal Handling"}
   ::arcana            {::ability ::a/int
                        ::name "Arcana"}
   ::athletics         {::ability ::a/str
                        ::name "Athletics"}
   ::deception         {::ability ::a/cha
                        ::name "Deception"}
   ::history           {::ability ::a/int
                        ::name "History"}
   ::insight           {::ability ::a/wis
                        ::name "Insight"}
   ::intimidation      {::ability ::a/cha
                        ::name "Intimidation"}
   ::investigation     {::ability ::a/int
                        ::name "Investigation"}
   ::medicine          {::ability ::a/wis
                        ::name "Medicine"}
   ::nature            {::ability ::a/int
                        ::name "Nature"}
   ::perception        {::ability ::a/wis
                        ::name "Perception"}
   ::performance       {::ability ::a/cha
                        ::name "Performance"}
   ::persuasion        {::ability ::a/cha
                        ::name "Persuasion"}
   ::religion          {::ability ::a/int
                        ::name "Religion"}
   ::sleight-of-hand   {::ability ::a/dex
                        ::name "Sleight of Hand"}
   ::stealth           {::ability ::a/dex
                        ::name "Stealth"}
   ::survival          {::ability ::a/wis
                        ::name "Survival"}})

(def skills (-> skill-data keys set))
(defn skill? [x] (-> x skills boolean))
(s/def ::skill skills)

(defn ability-for-skill [skill] (get-in skill-data [skill ::ability]))
(s/fdef ability-for-skill :args (s/cat :skill ::skill) :ret ::a/ability)

(defn skill-name [skill] (get-in skill-data [skill ::name]))
(s/fdef skill-name :args (s/cat :skill ::skill) :ret (s/and string? seq))
