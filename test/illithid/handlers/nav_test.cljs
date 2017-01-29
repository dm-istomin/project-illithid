(ns illithid.handlers.nav-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [clojure.spec :as s]
            [com.gfredericks.test.chuck.clojure-test :refer-macros [checking]]
            [clojure.test.check.generators :as gen]
            [illithid.db :as db]
            [illithid.handlers.nav :refer [push-raw]]))

(deftest push-raw-test
  (checking "new route adds to the end of the routes list" 5
    [route (s/gen ::db/route)
     db' (s/gen ::db/app-db)
     :let [db (update-in db' [::db/nav :routes]
                         (comp vec (partial remove #{route})))
           result (push-raw db [:nav/push-raw route])]]

    (is (s/valid? ::db/app-db result) (s/explain-str ::db/app-db result))
    (is (= route (-> result ::db/nav :routes last))))

  (checking "duplicate route adds to the end of the routes list" 5
    [db (s/gen ::db/app-db)
     :when (-> db ::db/nav :routes seq)
     route (-> db ::db/nav :routes gen/elements)
     :let [result (push-raw db [:nav/push-raw route])]]

    (is (s/valid? ::db/app-db result) (s/explain-str ::db/app-db result))
    (is (= route (-> result ::db/nav :routes last)))))

