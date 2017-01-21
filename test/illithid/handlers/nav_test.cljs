(ns illithid.handlers.nav-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [clojure.spec :as s]
            [illithid.test-helpers.spec-check :refer-macros [check]]
            [illithid.handlers.nav :refer [push-raw]]))

(deftest push-raw-test
  (s/exercise-fn push-raw)
  (check
    `push-raw
    {:gen {:illithid.db/state  #(s/gen #{:illithid.db/home})
           :illithid.db/app-db #(s/gen (s/keys :req [:illithid.db/nav
                                                     :illithid.db/state]))}}))
