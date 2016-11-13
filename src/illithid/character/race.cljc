(ns illithid.character.race
  (:require [clojure.spec :as s #?@(:cljs [:include-macros true])]))

(s/def ::name (s/and string? seq))

(s/def ::race (s/keys :req [::name]))
