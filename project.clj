(defproject rapex "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [seancorfield/next.jdbc "1.2.659"]
                 [com.github.seancorfield/honeysql "2.3.928"]
                 [org.duckdb/duckdb_jdbc "0.5.0"]]
  :main ^:skip-aot rapex.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
