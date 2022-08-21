#!/usr/bin/env bb
(require '[babashka.fs :as fs])
(require '[clojure.string :as str])
(require '[clojure.java.io :as io])
(require '[clojure.java.shell :as shell])

;; define the paths of the files that need changing
(def colortheme-file-path
  (str (System/getenv "HOME") "/.COLORTHEME"))
(def emacs-config-path
  (str (System/getenv "HOME") "/.emacs.d/init.el"))
(def kitty-config-path
  (str (System/getenv "HOME") "/.config/kitty/kitty.conf"))

;; read the files and save them line by line in a list
(def emacs-config-data
  (str/split-lines (slurp emacs-config-path)))
(def kitty-config-data
  (str/split-lines (slurp kitty-config-path)))

;; create colortheme file if it doesn't already exist
(when (not (fs/exists? colortheme-file-path))
  (spit colortheme-file-path "DARK"))

;; check which colortheme to switch to
(def current-colortheme (slurp colortheme-file-path))
(def next-colortheme
  (if (= current-colortheme "DARK") "LIGHT" "DARK"))

;; save new colortheme to the colortheme file
(spit colortheme-file-path next-colortheme)

(defn change-in-file [file-path
                      file-data
                      key-word
                      light-theme
                      dark-theme]
  (with-open [wtr (io/writer file-path)]
    (doseq [line file-data]
      (.write wtr (str (if (str/includes? line key-word)
                         (if (= next-colortheme "DARK")
                           dark-theme
                           light-theme)
                         line)
                       "\n")))))

;; change colortheme in configs
(change-in-file emacs-config-path
                emacs-config-data
                "load-theme"
                "  (load-theme 'doom-one-light t))"
                "  (load-theme 'doom-one t))")

(change-in-file kitty-config-path
                kitty-config-data
                "include"
                "include one-light.conf"
                "include one-dark.conf")

;; reload emacs config for all running emacs instances
(shell/sh "emacsclient" "-e"
          (str "(load-file \"" emacs-config-path "\")"))

;; reload kitty config for all running kitty instances
(map #(shell/sh "kitty" "@" "--to" (str "unix:" %)
                "set-colors" "--configured"
                (str (System/getenv "HOME")
                     (str "/.config/kitty/one-"
                     (str/lower-case next-colortheme) ".conf")))
     (mapv str (filter #(str/includes? % "kitty")
                       (file-seq (io/file "/tmp")))))
