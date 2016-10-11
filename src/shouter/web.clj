(ns shouter.web
  (:require [compojure.core :refer [defroutes GET]]
            [ring.adapter.jetty :as ring]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [shouter.controllers.shouts :as shouts]
            [shouter.views.layout :as layout]
            [shouter.models.migration :as schema])
  (:gen-class))

(defroutes routes
  shouts/routes
  (route/resources "/")
  (route/not-found (layout/four-oh-four)))

(def application (wrap-defaults routes site-defaults))

; starts the server within a repl
(defn start [port]
  (ring/run-jetty application {:port port
                               :join? false}))

; starts the server from the command line
(defn -main []
  (schema/migrate)
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (start port)))


; in lein repl,
; (require ('shouter.web))
; (shouter.web/-main)
