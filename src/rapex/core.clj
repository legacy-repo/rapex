(ns rapex.core
  (:require [next.jdbc :as jdbc]
            [honey.sql :as sql])
  (:import [java.sql DriverManager]
           [java.util Properties])
  (:gen-class))

(def ro-prop (doto (new Properties)
               (.setProperty "duckdb.read_only" "true")))

(defn get-connection [database]
  (DriverManager/getConnection (format "jdbc:duckdb:%s" database) ro-prop))

(defn get-results [db sqlmap]
  (with-open [con (get-connection db)]
    (jdbc/execute! con (sql/format sqlmap))))

(comment
  (def db "/Users/codespace/Documents/Code/Rapex/rapex/db/rapex_expr.duckdb")
  (def sqlmap {:select [:*]
               :from   [:gut_000000_counts]
               :limit 10})
  (get-results db sqlmap)
)

