(ns rapex.routes.duckdb
  (:require [ring.util.http-response :refer [ok not-found bad-request internal-server-error]]
            [rapex.db.query-duckdb :as qd]
            [clojure.tools.logging :as log]))

(defn get-error-response
  [e]
  (let [code (:code (.getData e))]
    (cond
      (= code :not-found) (not-found {:msg (.getMessage e)
                                      :context (.getData e)})
      (= code :bad-request) (bad-request {:msg (.getMessage e)
                                          :context (.getData e)})
      :else (internal-server-error {:msg (.getMessage e)
                                    :context (.getData e)}))))

(defn get-results
  [title dbname]
  {:summary    title
   :parameters {:query {:query_str string?
                        :page pos-int?
                        :page_size pos-int?}}
   :responses  {200 {:body {:total    nat-int?
                            :page     pos-int?
                            :page_size pos-int?
                            :data     any?}}
                404 {:body {:msg string?
                            :context any?}}
                400 {:body {:msg string?
                            :context any?}}
                500 {:body {:msg string?
                            :context any?}}}
   :handler    (fn [{{{:keys [page page_size query_str]} :query} :parameters
                     {:as headers} :headers}]
                 (try
                   (let [query-map (qd/read-string-as-map query_str)
                         query-map (merge query-map {:limit page_size :offset (* (- page 1) page_size)})
                         dbpath (qd/get-db-path dbname)]
                     (log/info "database:" dbpath "page:" page, "page_size:" page_size, "query-map:" query-map)
                     (ok {:total (qd/get-total dbpath query-map)
                          :page page
                          :page_size page_size
                          :data (qd/get-results dbpath query-map)}))
                   (catch Exception e
                     (log/debug "Error: " e)
                     (get-error-response e))))})

(def routes
  [""
   {:swagger {:tags ["Omics Data"]}}

   ["/omics-data"
    {:get  (get-results "Get Omics Data" "rapex_expr")}]
   
   ["/degs"
    {:get  (get-results "Get DEGs" "rapex_degs")}]
   
   ["/pathways"
    {:get  (get-results "Get Pathways" "rapex_pathway")}]])
