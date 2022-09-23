(ns rapex.routes.plot
  (:require [ring.util.http-response :refer [ok not-found bad-request internal-server-error]]
            [rapex.R.core :as rcore]
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
  [title]
  {:summary    title
   :parameters {:query {:query_str string?}}
   :responses  {200 {:body {:msg  string?
                            :data any?}}
                404 {:body {:msg string?
                            :context any?}}
                400 {:body {:msg string?
                            :context any?}}
                500 {:body {:msg string?
                            :context any?}}}
   :handler    (fn [{{{:keys [query_str]} :query} :parameters
                     {:as headers} :headers}]
                 (try
                   (let [output (rcore/test-rcode)]
                     (ok {:msg "success"
                          :data output}))
                   (catch Exception e
                     (log/debug "Error: " e)
                     (get-error-response e))))})

(def routes
  [""
   {:swagger {:tags ["Visualization for Omics Data"]}}

   ["/plot"
    {:get  (get-results "Basic Plots")}]])
