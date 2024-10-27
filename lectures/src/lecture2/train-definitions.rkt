#lang racket

;; Station is one of:
;; -- Commuter-station
;; -- Subway-station
;;

;; Commuter station is:
;; (make-commuter-station (String String Number))
(define-struct commuter-station (name line price))

;; Subway station is:
;; (make-subway-station (String String Boolean))
(define-struct subway-station (name line is-express))

(define COMM1 (make-commuter-station ("Harvard" "Red" 1.25)))
(define COMM2 (make-commuter-station ("Kenmore" "Green" 1.25)))

(define SUB1 (make-subway-station ("Back Bay" "Framingham" #t)))
(define SUB2 (make-subway-station ("West Newton" "Framingham" #f)))