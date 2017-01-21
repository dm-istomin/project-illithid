(ns illithid.handlers.nav-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [clojure.spec :as s]
            [clojure.test.check.clojure-test :refer-macros [defspec]]
            [clojure.test.check.properties :as prop :include-macros true]
            [clojure.test.check.generators :as gen]
            [clojure.spec.test :as stest]
            [illithid.db :as db]
            [illithid.handlers.nav :refer [push-raw]]))

(stest/instrument)

(defspec new-route-adds-to-end 5
  (prop/for-all
    [[db route] (gen/such-that
                  #(every? (complement #{%2}) (-> %1 ::db/nav :routes))
                  (gen/tuple (s/gen ::db/app-db)
                             (s/gen ::db/route)))]
    (let [result (push-raw db [:nav/push-raw route])]
      (= route (-> result ::db/nav :routes last)))))

(defspec duplicate-route-adds-to-end 5
  (prop/for-all
    [[db route] (gen/bind
                  (gen/such-that #(-> % ::db/nav :routes seq)
                                 (s/gen ::db/app-db))
                  (fn [db]
                    (gen/tuple (gen/return db)
                               (-> db ::db/nav :routes gen/elements))))]
    (let [result (push-raw db [:nav/push-raw route])]
      (= route (-> result ::db/nav :routes last)))))

