(ns cfinder.helpers
  (:gen-class)
  )

(require '[clojure.string :as str])

(defn upper-case-char?
  "Check if the string is Uppercase"
  [c]
  (= c (str/upper-case c)))

(defn remove-package-name
  "Remove the package name from class name.

    Example: a.b.c.FooBar > FooBar"
  [class-name]
  (->
    (str/split class-name #"\.")
    (last)
    )
  )

(defn split-at-uppercase
  "Split a given string at uppercase letters.

    Example: FooBarBaz > ('Foo', 'Bar', 'Baz')"
  [class-name]
  (loop
    [[cs temp collect-to] [(str/split class-name #"") "" '()]]
    (cond
      (= (str/lower-case class-name) class-name) '()

      (empty? cs) (concat collect-to [temp])                          ;; Collect the split part.

      (and (upper-case-char? (first cs)) (empty? temp))
           (recur [(rest cs) (first cs) collect-to])

      (and (upper-case-char? (first cs)) (not (empty? temp)))
           (recur [(rest cs) (first cs) (concat collect-to [temp])])

      :else (recur [(rest cs) (str temp (first cs)) collect-to])
      )
    )
  )

(defn has-pattern-special
  "Check if pattern exists for patterns that has *.

    Example: FooBar, F*o > true"
  [original pattern]
  (loop
    [[cs ps] [(str/split original #"") (str/split pattern #"")]]
    (cond
      (empty? ps) true
      (= "*" (first ps)) (recur [(rest cs) (rest ps)])                ;; Don't validate *
      (= (first cs) (first ps)) (recur [(rest cs) (rest ps)])
      :else false
      )
    )
  )


(defn has-pattern
  "Generic check if pattern exists in a given piece of string.

    Example: FooBar, FoBa > true
             FooBar, F*Ba > true"
  [original pattern]
  (cond
    (< (count original) (count pattern)) false
    (str/includes? pattern "*") (has-pattern-special original pattern)
    :else (str/starts-with? original pattern)
    )
  )

(defn get-subset-of
  "Get a subset from a list based on pattern matching.

    Example: (Foo Bar Baz), Foo > (Bar Baz)"
  [class-coll pattern]
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
  "Capitalize the pattern if it is all lowercase.
  Provides an easy workaround for making the search case-insensitive if
  the pattern is something like: 'Bar '

    Example: fff > FFF
  "
  [pattern]
  (cond
    (= pattern (str/lower-case pattern)) (str/upper-case pattern)
    :else pattern
    )
  )


(defn match-loop
  "Inner loop/recur for matching the pattern to the classname.
  Recurs through classname-collection and pattern-collection until a
  final state is reached to calculate if the classname matches the
  patterns. "
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
  "Matches the classname to pattern.

    Example: FooBarBaz, FoBa > true"
  [class-name pattern]

  (match-loop (split-at-uppercase class-name)
              (split-at-uppercase (sanitize-pattern (str/trim pattern)))
              (not (= (str/trim pattern) pattern)))
  )
