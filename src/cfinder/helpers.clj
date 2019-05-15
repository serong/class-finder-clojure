(ns cfinder.helpers
  (:gen-class)
  )

(require '[clojure.string :as str])

(defn upper-case-char?
  [c]
  "Check if the string is Uppercase"
  (= c (str/upper-case c)))

(defn remove-package-name
  [class-name]
  "Remove the package name from: 'a.b.FooBar'"
  (->
    (str/split class-name #"\.")
    (last)
    )
  )

(defn split-at-uppercase
  [class-name]
  "Split 'FooBarBaz' to ['Foo', 'Bar', 'Baz']"
  (loop
    [[cs temp collect-to] [(str/split class-name #"") "" '()]]
    (cond
      (= (str/lower-case class-name) class-name) '()
      (empty? cs) (concat collect-to [temp])
      (and (upper-case-char? (first cs)) (empty? temp))
      (recur [(rest cs) (first cs) collect-to])

      (and (upper-case-char? (first cs)) (not (empty? temp)))
      (recur [(rest cs) (first cs) (concat collect-to [temp])])


      :else (recur [(rest cs) (str temp (first cs)) collect-to])
      )
    )
  )

(defn has-pattern-special
  [original pattern]
  "Check if pattern exists for patterns with *"
  (loop
    [[cs ps] [(str/split original #"") (str/split pattern #"")]]
    (cond
      (empty? ps) true
      (= "*" (first ps)) (recur [(rest cs) (rest ps)])
      (= (first cs) (first ps)) (recur [(rest cs) (rest ps)])
      :else false
      )
    )
  )


(defn has-pattern
  [original pattern]
  "Generic check if pattern exists in a given piece of string."
  (cond
    (< (count original) (count pattern)) false
    (str/includes? pattern "*") (has-pattern-special original pattern)
    :else (str/starts-with? original pattern)
    )
  )

(defn get-subset-of
  [class-coll pattern]
  "Get a subset from a list based on pattern matching."
  (loop
    [[cc p] [class-coll pattern]]
    (cond
      (empty? cc) '()
      (and (has-pattern (first cc) p) (= 1 (count cc))) (list (subs (first cc) (count p)))
      (has-pattern (first cc) p) (rest cc)
      :else (recur [(rest cc) p])
      )
    )
  )

(defn sanitize-pattern
  [pattern]
  (cond
    (= pattern (str/lower-case pattern)) (str/upper-case pattern)
    :else pattern
    )
  )


(defn match-loop
  [ccoll pcoll strict]

  (loop
    [[cc pc is-strict] [ccoll pcoll strict]]
    (cond
      (not (empty? pc)) (recur [(get-subset-of cc (first pc)) (rest pc) is-strict])
      (empty? cc) false
      (and (not (empty? cc)) is-strict) (= (str/lower-case (str/join cc)) (str/join cc))
      :else true
      )
    )
  )

(defn matches
  [class-name pattern]

  (match-loop (split-at-uppercase class-name)
              (split-at-uppercase (sanitize-pattern (str/trim pattern)))
              (not (= (str/trim pattern) pattern)))
  )
