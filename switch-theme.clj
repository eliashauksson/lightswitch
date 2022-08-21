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

;; read the files and save them line by line in a list
(def emacs-config-data
  (str/split-lines (slurp emacs-config-path)))

;; create colortheme file if it doesn't already exist
(when (not (fs/exists? colortheme-file-path))
  (spit colortheme-file-path "DARK"))

;; check which colortheme to switch to
(def current-colortheme (slurp colortheme-file-path))
(def next-colortheme
  (if (= current-colortheme "DARK") "LIGHT" "DARK"))

;; save new colortheme to the colortheme file
(spit colortheme-file-path next-colortheme)

;; change colortheme in emacs config
(with-open [wtr (io/writer emacs-config-path)]
  (doseq [line emacs-config-data]
    (.write wtr (str (if (str/includes? line "load-theme")
                       (if (= next-colortheme "DARK")
                         "  (load-theme 'doom-one t))"
                         "  (load-theme 'doom-one-light t))")
                       line)
                     "\n"))))

;; reload emacs config for all running emacs clients
(shell/sh "emacsclient" "-e"
          (str "(load-file \"" emacs-config-path "\")"))
