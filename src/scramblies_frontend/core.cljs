(ns scramblies-frontend.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
   [reagent.core :as r]
   [reagent.dom :as d]
   [cljs-http.client :as http]
   [cljs.core.async :refer [<!]]))

(def result (r/atom ""))
(def str1 (r/atom ""))
(def str2 (r/atom ""))

(defn atom-input [value]
  [:input {:type "text"
           :value @value
           :on-change #(reset! value (-> % .-target .-value))}])

;; -------------------------
;; Views

(defn home-page []
  [:div
   [:h2 "Scramblies Frontend"]
   [:div
    [:span "string"]
    [atom-input str1]]
   [:div
    [:span "target"]
    [atom-input str2]]
   [:input {:type "button" :value "target inside string?"
            :on-click #(go (let [response (<! (http/get "http://192.168.0.110:3000/scramble"
                                                        {:with-credentials? false
                                                         :query-params {"str1" @str1 "str2" @str2}}))]
                             (reset! result (:body response))
                             (prn @str1)
                             (prn @result)))}]
   @result])

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
