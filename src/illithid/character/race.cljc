(ns illithid.character.race
  (:require [clojure.spec :as s #?@(:cljs [:include-macros true])]))

(s/def ::id keyword?)
(s/def ::name (s/and string? seq))

(s/def ::race (s/keys :req [::id ::name]))
