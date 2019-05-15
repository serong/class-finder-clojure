(ns cfinder.core
  (:gen-class)
  (:require [cfinder.helpers :refer :all]))

(require '[clojure.string :as str])

(defn -main
  "Print the matching class names."
  [& args]

  (def lines
    (->> (str/split-lines (slurp (first args)))
         (map str/trim)
         (filter #(not (= "" %)))
         (filter #(matches % (second args)))
         (sort-by remove-package-name)))

  (doseq
    [line lines]
    (println line))

  )
