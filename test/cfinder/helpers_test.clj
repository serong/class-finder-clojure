(ns cfinder.helpers-test
  (:require [clojure.test :refer :all]
            [cfinder.helpers :refer :all]))

(deftest test-upper-case-char
  (testing "Checking if a character is uppercase"
    (is (= true (upper-case-char? "S")))
    (is (= false (upper-case-char? "s")))
    )
  )

(deftest test-remove-package-name
  (testing "Removing package name from the whole name"
    (is (= "FooBar" (remove-package-name "a.b.c.FooBar")))
    )
  )

(deftest test-sanitize-pattern
  (testing "Correctly sanitizing a string."
    (is (= "FFF" (sanitize-pattern "fff")))
    (is (= "FFF" (sanitize-pattern "FFF")))
    (is (= "Foo" (sanitize-pattern "Foo")))
    )
  )

(deftest test-split
  (testing "Splitting a string at uppercase letters."
    (is (= '("Foo" "Bar") (split-at-uppercase "FooBar")))
    (is (= '("Foo") (split-at-uppercase "Foo")))
    (is (= '("F" "O" "O") (split-at-uppercase "FOO")))
    (is (= '() (split-at-uppercase "foo")))
    )
  )


(deftest test-special-pattern
  (testing "Pattern checking when * is involved."
    (is (= true (has-pattern-special "Foo" "F*o")))
    (is (= true (has-pattern-special "Boo" "B**")))
    )
  )


(deftest test-pattern
  (testing "Pattern checking in general."
    (is (= true (has-pattern "Foo" "F*o")))
    (is (= true (has-pattern "Boo" "B**")))
    (is (= true (has-pattern "Foobar" "Foo")))
    (is (= false (has-pattern "Foo" "Fooo")))
    (is (= false (has-pattern "Boo" "Bao")))
    (is (= false (has-pattern "FooBar" "FoB*")))
    )
  )


(deftest test-subset
  (testing "Getting a subset of given list of strings based on the pattern."
    (is (= '("Bar" "Baz") (get-subset-of '("Foo" "Bar" "Baz") "Foo")))
    (is (= '("") (get-subset-of '("Foo" "Bar" "Baz") "Baz")))
    (is (= '() (get-subset-of '("Foo" "Bar" "Baz") "Bad")))
    (is (= '("Baza" "Ar") (get-subset-of '("Foo" "Bar" "Baza" "Ar") "B")))
    (is (= '("r") (get-subset-of '("Baza" "Ar") "A")))
    (is (= '() (get-subset-of '("Baza" "Ar") "Z")))
    (is (= '() (get-subset-of '("r") "A")))
    )
  )

(deftest test-matches
  (testing "Matching a classname to the individual pattern."
    (is (= true (matches "FooBarBaz" "Foo")))
    (is (= true (matches "FooBarBaz" "Baz")))
    (is (= true (matches "FooBarBazaar" "Baz")))
    (is (= true (matches "FooBarBazaar" "FoBaz")))
    (is (= false (matches "FooBarBazaar" "BAZ")))
    (is (= true (matches "FooBarBazaAr" "fbb")))
    (is (= false (matches "FooBarBazaAr" "fbz")))
    (is (= false (matches "FooBarBazaAr" "BAZ")))
    (is (= true (matches "FooBarBazaAr" "BA")))
    (is (= false (matches "FooBarBazaAr" "Baz ")))
    (is (= true (matches "FooBarzoo" "FoBar ")))
    )
  )
