(defproject cfinder "dev"
  :description "Class name finder with clojure"
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot cfinder.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
