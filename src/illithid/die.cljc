(ns illithid.die
  (:require [clojure.spec :as s #?@(:cljs [:include-macros true])]))

(def sides #{2 4 6 8 10 12 20})
(s/def ::die sides)
(s/def ::count (s/and int? pos?))
(s/def ::modifier (s/and int? pos?))
(s/def ::roll (s/keys :req [::die ::count]
                      :opt [::modifier]))

(defn show-die [die] (str "d" die))
(s/fdef show-roll :args ::die :ret string?)

(defn show-roll [{::keys [die count modifier]}]
  (str count (show-die die)
       (when modifier
         (str (if (pos? modifier) "+" "-") modifier))))
(s/fdef show-roll :args (s/cat :roll ::roll) :ret string?)
