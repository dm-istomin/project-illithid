(require '[clojure.java.io :refer [resource reader writer file]]
         '[clojure.edn :as edn]
         '[clojure.set :refer [rename-keys]]
         '[clojure.spec :as s]
         '[illithid.character.spell :as sp]
         '[clojure.data.json :as json])
(import '[java.io File PushbackReader]

(def files (-> "spells" resource file .listFiles))
(def spells (for [f files]
              (with-open [rd (-> f reader PushbackReader.)]
                (edn/read rd))))

(def raw-spells (-> "./scripts/spell_data.json" reader json/read)))

(every? #(s/valid? ::sp/spell %) spells)

(defn raw-counterpart [spell]
  (first (filter #(= (::sp/name spell) (% "name")) raw-spells)))

(defn fix-spell [spell]
  (let [raw (raw-counterpart spell)]
    (-> spell
        (rename-keys {:id ::sp/id
                      ::sp/ritual ::sp/ritual?
                      ::sp/casting_time ::sp/casting-time})
        (assoc ::sp/duration (get raw "duration")))))

(def fixed (map fix-spell spells))
(def still-invalid (filter #(not (s/valid? ::sp/spell %)) fixed))

(every? #(s/valid? ::sp/spell %) fixed) ;; yay

(doseq [[sp fl] (map vector spells files)]
  (spit fl (-> sp fix-spell pr-str)))

