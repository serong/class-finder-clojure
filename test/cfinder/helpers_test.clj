(ns cfinder.helpers-test
  (:require [clojure.test :refer :all]
            [cfinder.helpers :refer :all]))

(deftest test-upper-case-char
  (testing "Ahoy there"
    (is (= true (upper-case-char? "S")))
    (is (= false (upper-case-char? "s")))
    )
  )


(deftest test-remove-package-name
  (testing "Ahoy there"
    (is (= "FooBar" (remove-package-name "a.b.c.FooBar")))
    )
  )

(deftest test-sanitize-pattern
  (testing "Ahoy there"
    (is (= "FFF" (sanitize-pattern "fff")))
    (is (= "FFF" (sanitize-pattern "FFF")))
    (is (= "Foo" (sanitize-pattern "Foo")))
    )
  )

(deftest test-split
  (testing "Ahoy there"
    (is (= '("Foo" "Bar") (split-at-uppercase "FooBar")))
    (is (= '("Foo") (split-at-uppercase "Foo")))
    (is (= '("F" "O" "O") (split-at-uppercase "FOO")))
    (is (= '() (split-at-uppercase "foo")))
    )
  )


(deftest test-special-pattern
  (testing "Ahoy there"
    (is (= true (has-pattern-special "Foo" "F*o")))
    (is (= true (has-pattern-special "Boo" "B**")))
    )
  )


(deftest test-pattern
  (testing "Ahoy there"
    (is (= true (has-pattern "Foo" "F*o")))
    (is (= true (has-pattern "Boo" "B**")))
    (is (= true (has-pattern "Foobar" "Foo")))
    (is (= false (has-pattern "Foo" "Fooo")))
    (is (= false (has-pattern "Boo" "Bao")))
    )
  )


(deftest test-subset
  (testing "Ahoy there"
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
  (testing "Ahoy there"
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
