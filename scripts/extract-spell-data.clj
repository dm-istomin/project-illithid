;; a wild hack is fast approaching
(require '[clojure.data.json :as json])
(import java.io.FileReader)

(defn remove-nil-vals [x] (into {} (filter (comp (complement nil?) second) x)))

(def spells (json/read (FileReader. (nth *command-line-args* 0))))

(def parsed-spells (for [spell spells] (remove-nil-vals #:illithid.character.spell{:id ((comp keyword #(str/replace % "/" "-") #(str/replace % "'" "") #(str/replace % " " "-") str/lower-case #(get % "name")) spell) :range (spell "range") :ritual? (= "yes" (spell "ritual")) :casting-time (spell "casting_time"), :classes (into #{} (map keyword (-> spell (get "class") str/lower-case (str/split #", ?")))), :name (spell "name"), :level (or (some->> (spell "level") (re-seq #"\d+") first Integer.) 0) :description (spell "desc") :duration (spell "duration") :school (->> (spell "school") str/lower-case (keyword "school")) :material-component (spell "material") :components (into #{} (map keyword (-> spell (get "components") (str/split #", ?")))) :concentration? (= "yes" (spell "concentration"))})))

(doseq [spell parsed-spells]
  (spit (str "resources/spells/" (-> spell :id name) ".edn") (pr-str spell)))
