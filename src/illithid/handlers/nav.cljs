(ns illithid.handlers.nav
  (:require [re-frame.core :refer [reg-event-db reg-event-fx after debug]]
            [clojure.spec :as s :include-macros true]
            [illithid.handlers :refer [middleware]]
            [illithid.lib.core :refer [dec-to-zero]]
            [illithid.db :as db]))

(def default-title "Illithid")
(def titles
  {:characters-index "Characters"
   :characters-new-basic-info "New Character - Basic Info"
   :characters-new-abilities "New Character - Abilities"
   :characters-new-proficiencies "New Character - Proficiencies"

   :spell-detail "Spell Detail"
   :spell-list "Spell List"})

(defn title-for [route-key]
  (get titles route-key default-title))

;;;

(defn push-raw
  [{{:keys [routes]} ::db/nav :as db} [_ {route-key :key :as route}]]
  (let [matches-route (comp #{route-key} :key)]
    (if (some matches-route routes)
      ;; If the route already exists, remove it from the stack and add it to
      ;; the beginning
      (update-in db [::db/nav :routes]
                 #(let [without-dupe (into [] (remove matches-route %))
                        with-new-route (conj without-dupe route)]
                   (into [] with-new-route)))
      ;; Otherwise, just push the route to the stack
      (-> db
          (update-in [::db/nav :index] inc)
          (update-in [::db/nav :routes] conj route)))))
(s/fdef push-raw
        :args (s/cat :db ::db/app-db
                     :action (s/tuple #{:nav/push-raw} ::db/route))
        :ret ::db/app-db
        :fn #(= (-> % :args :action last) (-> % :ret ::db/nav :routes last)))

(reg-event-db :nav/push-raw middleware push-raw)

(reg-event-fx
  :nav/push
  middleware
  (fn [{:keys [db]} [_ route]]
    (let [raw-route
          (cond
            (map? route) (update route :title
                                 #(or % (title-for (:key route))))
            (keyword? route) {:key route, :title (title-for route)})]
      {:dispatch [:nav/push-raw raw-route]})))

(reg-event-db
  :nav/pop
  middleware
  (fn [db [_ _]]
    (-> db
        (update-in [::db/nav :index] dec-to-zero)
        (update-in [::db/nav :routes] pop))))

(reg-event-db
  :nav/home
  middleware
  (fn [db [_ _]]
    (-> db
        (assoc-in [::db/nav :index] 0)
        (assoc-in [::db/nav :routes] [(-> db ::db/nav :routes first)]))))

